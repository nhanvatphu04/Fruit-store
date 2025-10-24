package controllers;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import models.CartItem;
import models.CartCombo;
import models.User;
import services.CartService;
import services.ProductService;

@WebServlet(name = "CartController", urlPatterns = {
    "/cart",
    "/cart/add",
    "/cart/update",
    "/cart/remove",
    "/cart/select",
    "/cart/totals",
    "/cart/count",
    "/cart/remove-combo",
    "/cart/select-combo",
    "/cart/update-combo"
})
public class CartController extends HttpServlet {
    private CartService cartService;
    private ProductService productService;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        super.init();
        cartService = new CartService();
        productService = new ProductService();
        gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getServletPath();
        
        switch (action) {
            case "/cart":
                viewCart(request, response);
                break;
            case "/cart/totals":
                getCartTotals(request, response);
                break;
            case "/cart/count":
                getCartCount(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getServletPath();
        
        switch (action) {
            case "/cart/add":
                addToCart(request, response);
                break;
            case "/cart/update":
                updateCartItem(request, response);
                break;
            case "/cart/remove":
                removeCartItem(request, response);
                break;
            case "/cart/select":
                selectCartItem(request, response);
                break;
            case "/cart/remove-combo":
                removeCartCombo(request, response);
                break;
            case "/cart/select-combo":
                selectCartCombo(request, response);
                break;
            case "/cart/update-combo":
                updateCartCombo(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    // Hiển thị trang giỏ hàng
    private void viewCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Lấy sản phẩm trong giỏ
        List<CartItem> cartItems = cartService.getCartByUserId(user.getUserId());
        
        // Lấy combos trong giỏ
        List<CartCombo> cartCombos = cartService.getCartCombosByUserId(user.getUserId());
        
        // Tính tổng tiền
        BigDecimal subtotal = cartService.calculateSelectedTotal(cartItems, cartCombos);
        BigDecimal discount = BigDecimal.ZERO;
        
        // Lấy giảm giá từ session nếu có
        BigDecimal sessionDiscount = (BigDecimal) session.getAttribute("cartDiscount");
        if (sessionDiscount != null) {
            discount = sessionDiscount;
        }
        
        request.setAttribute("cartItems", cartItems);
        request.setAttribute("cartCombos", cartCombos);
        request.setAttribute("subtotal", subtotal);
        request.setAttribute("discount", discount);
        request.setAttribute("total", subtotal.subtract(discount));
        
        request.getRequestDispatcher("/jsp/cart.jsp").forward(request, response);
    }

    // Thêm sản phẩm vào giỏ hàng
    private void addToCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        Map<String, Object> result = new HashMap<>();
        
        if (user == null) {
            result.put("success", false);
            result.put("message", "Vui lòng đăng nhập để thêm vào giỏ hàng");
            out.print(gson.toJson(result));
            return;
        }

        try {
            String productIdStr = request.getParameter("productId");
            String quantityStr = request.getParameter("quantity");
            
            System.out.println("Received parameters - productId: " + productIdStr + ", quantity: " + quantityStr);
            
            if (productIdStr == null || productIdStr.trim().isEmpty()) {
                result.put("success", false);
                result.put("message", "ID sản phẩm không được để trống");
                out.print(gson.toJson(result));
                return;
            }
            
            if (quantityStr == null || quantityStr.trim().isEmpty()) {
                result.put("success", false);
                result.put("message", "Số lượng không được để trống");
                out.print(gson.toJson(result));
                return;
            }
            
            int productId = Integer.parseInt(productIdStr);
            int quantity = Integer.parseInt(quantityStr);
            
            boolean success = cartService.addToCart(user.getUserId(), productId, quantity);
            result.put("success", success);
            result.put("message", success ? "Đã thêm vào giỏ hàng" : "Không thể thêm vào giỏ hàng");
            
        } catch (NumberFormatException e) {
            System.out.println("NumberFormatException: " + e.getMessage());
            result.put("success", false);
            result.put("message", "Dữ liệu không hợp lệ: " + e.getMessage());
        }
        
        out.print(gson.toJson(result));
    }

    // Cập nhật số lượng sản phẩm trong giỏ
    private void updateCartItem(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        Map<String, Object> result = new HashMap<>();
        
        try {
            int cartId = Integer.parseInt(request.getParameter("cartId"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            
            if (quantity < 1) {
                result.put("success", false);
                result.put("message", "Số lượng phải lớn hơn 0");
            } else {
                boolean success = cartService.updateCartItem(cartId, quantity);
                result.put("success", success);
                result.put("message", success ? "Đã cập nhật giỏ hàng" : "Không thể cập nhật giỏ hàng");
            }
            
        } catch (NumberFormatException e) {
            result.put("success", false);
            result.put("message", "Dữ liệu không hợp lệ");
        }
        
        out.print(gson.toJson(result));
    }

    // Xoá sản phẩm khỏi giỏ
    private void removeCartItem(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        Map<String, Object> result = new HashMap<>();
        
        try {
            int cartId = Integer.parseInt(request.getParameter("cartId"));
            boolean success = cartService.removeCartItem(cartId);
            
            result.put("success", success);
            result.put("message", success ? "Đã xoá sản phẩm" : "Không thể xoá sản phẩm");
            
        } catch (NumberFormatException e) {
            result.put("success", false);
            result.put("message", "Dữ liệu không hợp lệ");
        }
        
        out.print(gson.toJson(result));
    }

    // Chọn/bỏ chọn sản phẩm để thanh toán
    private void selectCartItem(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        Map<String, Object> result = new HashMap<>();
        
        try {
            int cartId = Integer.parseInt(request.getParameter("cartId"));
            boolean selected = Boolean.parseBoolean(request.getParameter("selected"));
            
            boolean success = cartService.updateCartItemSelection(cartId, selected);
            
            result.put("success", success);
            result.put("message", success ? "Đã cập nhật lựa chọn" : "Không thể cập nhật lựa chọn");
            
        } catch (NumberFormatException e) {
            result.put("success", false);
            result.put("message", "Dữ liệu không hợp lệ");
        }
        
        out.print(gson.toJson(result));
    }

    // Lấy tổng tiền giỏ hàng
    private void getCartTotals(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        Map<String, Object> result = new HashMap<>();
        
        if (user == null) {
            result.put("success", false);
            result.put("message", "Phiên đăng nhập đã hết hạn");
            out.print(gson.toJson(result));
            return;
        }

        List<CartItem> cartItems = cartService.getCartByUserId(user.getUserId());
        List<CartCombo> cartCombos = cartService.getCartCombosByUserId(user.getUserId());
        BigDecimal subtotal = cartService.calculateSelectedTotal(cartItems, cartCombos);
        BigDecimal discount = BigDecimal.ZERO;

        // Lấy giảm giá từ session nếu có
        BigDecimal sessionDiscount = (BigDecimal) session.getAttribute("cartDiscount");
        if (sessionDiscount != null) {
            discount = sessionDiscount;
        }

        result.put("success", true);
        result.put("subtotal", subtotal);
        result.put("discount", discount);
        result.put("total", subtotal.subtract(discount));

        out.print(gson.toJson(result));
    }
    
    // Lấy tổng số lượng sản phẩm và combo trong giỏ hàng
    private void getCartCount(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        Map<String, Object> result = new HashMap<>();
        
        if (user == null) {
            result.put("success", false);
            result.put("message", "Phiên đăng nhập đã hết hạn");
            result.put("count", 0);
        } else {
            // Lấy số lượng sản phẩm thường
            int cartCount = cartService.getCartCount(user.getUserId());
            // Lấy số lượng combo
            int comboCount = cartService.getCartComboCount(user.getUserId());
            // Tổng số lượng
            int totalCount = cartCount + comboCount;
            
            result.put("success", true);
            result.put("count", totalCount);
        }
        
        out.print(gson.toJson(result));
    }

    // Xoá combo khỏi giỏ hàng
    private void removeCartCombo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        Map<String, Object> jsonResponse = new HashMap<>();
        
        try {
            int cartComboId = Integer.parseInt(request.getParameter("cartComboId"));
            boolean success = cartService.removeCartCombo(cartComboId);
            
            jsonResponse.put("success", success);
            if (!success) {
                jsonResponse.put("message", "Không thể xoá combo khỏi giỏ hàng");
            }
        } catch (NumberFormatException e) {
            jsonResponse.put("success", false);
            jsonResponse.put("message", "ID combo không hợp lệ");
        } catch (Exception e) {
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Lỗi: " + e.getMessage());
        }
        
        response.getWriter().write(gson.toJson(jsonResponse));
    }
    
    // Cập nhật trạng thái chọn của combo
    private void selectCartCombo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        Map<String, Object> jsonResponse = new HashMap<>();
        
        try {
            int cartComboId = Integer.parseInt(request.getParameter("cartComboId"));
            boolean selected = Boolean.parseBoolean(request.getParameter("selected"));
            
            boolean success = cartService.updateCartComboSelection(cartComboId, selected);
            jsonResponse.put("success", success);
            
            if (success) {
                // Cập nhật lại tổng tiền
                User user = (User) request.getSession().getAttribute("user");
                List<CartItem> cartItems = cartService.getCartByUserId(user.getUserId());
                List<CartCombo> cartCombos = cartService.getCartCombosByUserId(user.getUserId());
                BigDecimal subtotal = cartService.calculateSelectedTotal(cartItems, cartCombos);
                BigDecimal discount = BigDecimal.ZERO;
                
                // Lấy giảm giá từ session nếu có
                BigDecimal sessionDiscount = (BigDecimal) request.getSession().getAttribute("cartDiscount");
                if (sessionDiscount != null) {
                    discount = sessionDiscount;
                }
                
                jsonResponse.put("subtotal", subtotal);
                jsonResponse.put("discount", discount);
                jsonResponse.put("total", subtotal.subtract(discount));
            }
        } catch (NumberFormatException e) {
            jsonResponse.put("success", false);
            jsonResponse.put("message", "ID combo không hợp lệ");
        } catch (Exception e) {
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Lỗi: " + e.getMessage());
        }
        
        response.getWriter().write(gson.toJson(jsonResponse));
    }

    // Cập nhật số lượng combo trong giỏ
    private void updateCartCombo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        Map<String, Object> result = new HashMap<>();

        try {
            int cartComboId = Integer.parseInt(request.getParameter("cartComboId"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));

            if (quantity < 1) {
                result.put("success", false);
                result.put("message", "Số lượng phải lớn hơn 0");
            } else {
                boolean success = cartService.updateCartComboQuantity(cartComboId, quantity);
                result.put("success", success);
                result.put("message", success ? "Đã cập nhật giỏ hàng" : "Không thể cập nhật giỏ hàng");
            }

        } catch (NumberFormatException e) {
            result.put("success", false);
            result.put("message", "Dữ liệu không hợp lệ");
        }

        out.print(gson.toJson(result));
    }
}
