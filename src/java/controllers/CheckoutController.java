package controllers;

import com.google.gson.Gson;
import dao.OrderDAO;
import dao.OrderItemDAO;
import dao.ProductDAO;
import dao.CartDAO;
import dao.CartComboDAO;
import dao.DiscountUsageDAO;
import models.Order;
import models.OrderItem;
import models.CartItem;
import models.CartCombo;
import models.Product;
import models.User;
import models.Discount;
import services.CartService;
import services.DiscountService;
import services.OrderService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@WebServlet(name = "CheckoutController", urlPatterns = {"/checkout"})
public class CheckoutController extends HttpServlet {
    private OrderService orderService;
    private CartService cartService;
    private DiscountService discountService;
    private OrderDAO orderDAO;
    private OrderItemDAO orderItemDAO;
    private ProductDAO productDAO;
    private CartDAO cartDAO;
    private CartComboDAO cartComboDAO;
    private DiscountUsageDAO discountUsageDAO;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        orderService = new OrderService();
        cartService = new CartService();
        discountService = new DiscountService();
        orderDAO = new OrderDAO();
        orderItemDAO = new OrderItemDAO();
        productDAO = new ProductDAO();
        cartDAO = new CartDAO();
        cartComboDAO = new CartComboDAO();
        discountUsageDAO = new DiscountUsageDAO();
        gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        // Kiểm tra xem người dùng đã đăng nhập chưa
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            // Lấy giỏ hàng của user
            List<CartItem> cartItems = cartService.getCartByUserId(user.getUserId());
            List<CartCombo> cartCombos = cartService.getCartCombosByUserId(user.getUserId());

            // Lọc chỉ lấy các sản phẩm được chọn
            List<CartItem> selectedItems = cartItems.stream()
                    .filter(CartItem::isSelected)
                    .toList();
            
            List<CartCombo> selectedCombos = cartCombos.stream()
                    .filter(CartCombo::isSelected)
                    .toList();

            // Kiểm tra xem có sản phẩm nào được chọn không
            if (selectedItems.isEmpty() && selectedCombos.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/cart");
                return;
            }

            // Tính tổng tiền
            BigDecimal subtotal = cartService.calculateSelectedTotal(selectedItems, selectedCombos);
            
            // Lấy giảm giá từ session
            BigDecimal discount = BigDecimal.ZERO;
            String appliedDiscountCode = (String) session.getAttribute("appliedDiscount");
            BigDecimal sessionDiscount = (BigDecimal) session.getAttribute("cartDiscount");
            if (sessionDiscount != null) {
                discount = sessionDiscount;
            }

            BigDecimal total = subtotal.subtract(discount);

            // Set attributes cho JSP
            request.setAttribute("selectedItems", selectedItems);
            request.setAttribute("selectedCombos", selectedCombos);
            request.setAttribute("subtotal", subtotal);
            request.setAttribute("discount", discount);
            request.setAttribute("discountCode", appliedDiscountCode);
            request.setAttribute("total", total);
            request.setAttribute("user", user);

            // Forward đến checkout.jsp
            request.getRequestDispatcher("/jsp/checkout.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/error");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        // Kiểm tra xem người dùng đã đăng nhập chưa
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            // Lấy giỏ hàng của user
            List<CartItem> cartItems = cartService.getCartByUserId(user.getUserId());
            List<CartCombo> cartCombos = cartService.getCartCombosByUserId(user.getUserId());

            // Lọc chỉ lấy các sản phẩm được chọn
            List<CartItem> selectedItems = cartItems.stream()
                    .filter(CartItem::isSelected)
                    .toList();
            
            List<CartCombo> selectedCombos = cartCombos.stream()
                    .filter(CartCombo::isSelected)
                    .toList();

            // Kiểm tra xem có sản phẩm nào được chọn không
            if (selectedItems.isEmpty() && selectedCombos.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/cart");
                return;
            }

            // Tính tổng tiền
            BigDecimal subtotal = cartService.calculateSelectedTotal(selectedItems, selectedCombos);
            
            // Lấy giảm giá từ session
            BigDecimal discount = BigDecimal.ZERO;
            String appliedDiscountCode = (String) session.getAttribute("appliedDiscount");
            BigDecimal sessionDiscount = (BigDecimal) session.getAttribute("cartDiscount");
            if (sessionDiscount != null) {
                discount = sessionDiscount;
            }

            BigDecimal total = subtotal.subtract(discount);

            // Tạo đơn hàng
            Order order = new Order(user.getUserId(), total, "pending", appliedDiscountCode, discount);
            
            if (!orderDAO.addOrder(order)) {
                throw new Exception("Không thể tạo đơn hàng");
            }

            // Lấy order ID vừa tạo
            Order createdOrder = orderDAO.getOrdersByUserId(user.getUserId()).stream()
                    .filter(o -> o.getStatus().equals("pending"))
                    .findFirst()
                    .orElse(null);

            if (createdOrder == null) {
                throw new Exception("Không thể lấy thông tin đơn hàng");
            }

            int orderId = createdOrder.getOrderId();

            // Thêm order items cho các sản phẩm được chọn
            for (CartItem item : selectedItems) {
                Product product = item.getProduct();
                if (product != null) {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrderId(orderId);
                    orderItem.setProductId(product.getProductId());
                    orderItem.setQuantity(item.getQuantity());
                    orderItem.setPrice(product.getPrice());

                    if (!orderItemDAO.addOrderItem(orderItem)) {
                        throw new Exception("Không thể thêm sản phẩm vào đơn hàng");
                    }

                    // Giảm số lượng tồn kho
                    product.setStockQuantity(product.getStockQuantity() - item.getQuantity());
                    if (!productDAO.updateProduct(product)) {
                        throw new Exception("Không thể cập nhật tồn kho");
                    }

                    // Xóa sản phẩm khỏi giỏ hàng
                    cartDAO.removeCartItem(item.getCartId());
                }
            }

            // Xóa các combo khỏi giỏ hàng
            for (CartCombo combo : selectedCombos) {
                cartComboDAO.removeCartComboItem(combo.getCartComboId());
            }

            // Cập nhật discount_usage với order ID nếu có discount
            if (appliedDiscountCode != null && !appliedDiscountCode.isEmpty()) {
                Discount discountObj = discountService.getDiscountByCode(appliedDiscountCode);
                if (discountObj != null) {
                    discountUsageDAO.updateOrderIdByCodeAndUser(
                        appliedDiscountCode,
                        user.getUserId(),
                        orderId
                    );
                }
            }

            // Xóa discount từ session
            session.removeAttribute("appliedDiscount");
            session.removeAttribute("cartDiscount");

            // Redirect đến trang xác nhận
            response.sendRedirect(request.getContextPath() + "/orders?orderId=" + orderId);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/error");
        }
    }
}

