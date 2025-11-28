package controllers;

import dao.OrderDAO;
import dao.OrderItemDAO;
import dao.ProductDAO;
import dao.CartDAO;
import dao.CartComboDAO;
import dao.DiscountUsageDAO;
import dao.OrderComboDAO;
import models.Order;
import models.OrderItem;
import models.CartItem;
import models.CartCombo;
import models.Product;
import models.User;
import models.Discount;
import services.CartService;
import services.DiscountService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet(name = "CheckoutController", urlPatterns = {"/checkout"})
public class CheckoutController extends HttpServlet {
    private CartService cartService;
    private DiscountService discountService;
    private OrderDAO orderDAO;
    private OrderItemDAO orderItemDAO;
    private ProductDAO productDAO;
    private CartDAO cartDAO;
    private CartComboDAO cartComboDAO;
    private DiscountUsageDAO discountUsageDAO;
    private OrderComboDAO orderComboDAO;

    @Override
    public void init() throws ServletException {
        cartService = new CartService();
        discountService = new DiscountService();
        orderDAO = new OrderDAO();
        orderItemDAO = new OrderItemDAO();
        productDAO = new ProductDAO();
        cartDAO = new CartDAO();
        cartComboDAO = new CartComboDAO();
        discountUsageDAO = new DiscountUsageDAO();
        orderComboDAO = new OrderComboDAO();
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

            // Địa chỉ giao hàng (mặc định lấy từ user)
            String shippingAddress = user.getAddress();

            // Set attributes cho JSP
            request.setAttribute("selectedItems", selectedItems);
            request.setAttribute("selectedCombos", selectedCombos);
            request.setAttribute("subtotal", subtotal);
            request.setAttribute("discount", discount);
            request.setAttribute("discountCode", appliedDiscountCode);
            request.setAttribute("total", total);
            request.setAttribute("shippingAddress", shippingAddress);
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

            // Lấy địa chỉ giao hàng từ form (nếu có)
            String shippingAddress = request.getParameter("shippingAddress");
            if (shippingAddress == null || shippingAddress.trim().isEmpty()) {
                shippingAddress = user.getAddress();
            }

            // Tạo đơn hàng
            Order order = new Order(user.getUserId(), total, "pending", appliedDiscountCode, discount);
            order.setShippingAddress(shippingAddress);
            
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
                    // Use the discounted price that the user actually paid, not the current product price
                    orderItem.setPrice(item.getDiscountedPrice());

                    if (!orderItemDAO.addOrderItem(orderItem)) {
                        throw new Exception("Không thể thêm sản phẩm vào đơn hàng");
                    }

                    // Note: Stock reduction happens when order status is changed to 'completed', not here
                    // This allows for order cancellation without affecting inventory

                    // Xóa sản phẩm khỏi giỏ hàng
                    cartDAO.removeCartItem(item.getCartId());
                }
            }

            // Thêm combos vào đơn hàng và xóa khỏi giỏ
            for (CartCombo combo : selectedCombos) {
                if (combo.getCombo() != null) {
                    models.OrderCombo orderCombo = new models.OrderCombo();
                    orderCombo.setOrderId(orderId);
                    orderCombo.setComboId(combo.getComboId());
                    orderCombo.setQuantity(combo.getQuantity());
                    orderCombo.setPrice(combo.getCombo().getSalePrice());

                    if (!orderComboDAO.addOrderCombo(orderCombo)) {
                        throw new Exception("Không thể thêm combo vào đơn hàng");
                    }
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

