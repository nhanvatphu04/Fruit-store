package controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dao.OrderDAO;
import dao.OrderItemDAO;
import dao.ProductDAO;
import dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import models.Order;
import models.OrderItem;
import models.Product;
import models.User;


@WebServlet(name = "AdminOrderController", urlPatterns = {
    "/admin/orders",
    "/admin/orders/details/*",
    "/admin/orders/update-status",
    "/admin/orders/by-status"
})
public class AdminOrderController extends HttpServlet {
    private final OrderDAO orderDAO = new OrderDAO();
    private final OrderItemDAO orderItemDAO = new OrderItemDAO();
    private final UserDAO userDAO = new UserDAO();
    private final ProductDAO productDAO = new ProductDAO();
    private final Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Kiểm tra xem người dùng đã đăng nhập và có vai trò quản trị viên chưa
        User currentUser = (User) request.getSession().getAttribute("user");
        if (currentUser == null || !"admin".equals(currentUser.getRole())) {
            response.sendRedirect(request.getContextPath() + "/jsp/error/403.jsp");
            return;
        }
        
        String path = request.getServletPath();
        
        if (path.equals("/admin/orders")) {
            // Hiển thị trang quản lý đơn hàng
            List<Order> orders = orderDAO.getAllOrders();

            // Thống kê đơn hàng theo trạng thái (only valid ENUM values)
            Map<String, Integer> stats = new HashMap<>();
            stats.put("pending", orderDAO.countOrdersByStatus("pending"));
            stats.put("completed", orderDAO.countOrdersByStatus("completed"));
            stats.put("cancelled", orderDAO.countOrdersByStatus("cancelled"));

            request.setAttribute("orders", orders);
            request.setAttribute("orderStats", stats);
            request.getRequestDispatcher("/jsp/admin/orders.jsp").forward(request, response);
        }
        else if (path.equals("/admin/orders/by-status")) {
            // API lấy đơn hàng theo trạng thái
            String status = request.getParameter("status");
            List<Order> orders = status != null && !status.isEmpty() ? 
                               orderDAO.getOrdersByStatus(status) :
                               orderDAO.getAllOrders();
                               
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(gson.toJson(orders));
        }
        else if (path.startsWith("/admin/orders/details/")) {
            // API lấy chi tiết đơn hàng
            try {
                int orderId = Integer.parseInt(path.substring(path.lastIndexOf('/') + 1));
                Order order = orderDAO.getOrderById(orderId);
                
                if (order != null) {
                    // Lấy thông tin người dùng
                    User user = userDAO.getUserById(order.getUserId());
                    
                    // Lấy danh sách sản phẩm trong đơn hàng
                    List<OrderItem> items = orderItemDAO.getOrderItems(orderId);
                    
                    // Lấy thông tin sản phẩm cho mỗi item
                    for (OrderItem item : items) {
                        Product product = productDAO.getProductById(item.getProductId());
                        if (product != null) {
                            Map<String, Object> productInfo = new HashMap<>();
                            productInfo.put("name", product.getName());
                            productInfo.put("image", product.getImageUrl());
                            item.setProduct(product);
                        }
                    }
                    
                    // Tạo response object
                    Map<String, Object> orderDetails = new HashMap<>();
                    orderDetails.put("orderId", order.getOrderId());
                    orderDetails.put("orderDate", order.getOrderDate());
                    orderDetails.put("totalAmount", order.getTotalAmount());
                    orderDetails.put("status", order.getStatus());
                    orderDetails.put("customer", user);
                    orderDetails.put("items", items);
                    
                    // Gửi response dạng JSON
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(gson.toJson(orderDetails));
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    Map<String, String> error = new HashMap<>();
                    error.put("message", "Order not found");
                    response.getWriter().write(gson.toJson(error));
                }
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                Map<String, String> error = new HashMap<>();
                error.put("message", "Invalid order ID");
                response.getWriter().write(gson.toJson(error));
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Đặt loại phản hồi sớm để xử lý lỗi phù hợp
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        // Kiểm tra xem người dùng đã đăng nhập và có vai trò quản trị viên chưa
        User currentUser = (User) request.getSession().getAttribute("user");
        if (currentUser == null || !"admin".equals(currentUser.getRole())) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Access denied. Admin privileges required.");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write(gson.toJson(error));
            return;
        }
        
        String path = request.getServletPath();
        
        if (path.equals("/admin/orders/update-status")) {
            // API cập nhật trạng thái đơn hàng
            Map<String, Object> result = new HashMap<>();
            
            try {
                int orderId = Integer.parseInt(request.getParameter("orderId"));
                String status = request.getParameter("status");
                
                // Xác thực trạng thái
                if (status == null || status.trim().isEmpty()) {
                    throw new IllegalArgumentException("Status cannot be empty");
                }
                
                if (!isValidStatus(status)) {
                    throw new IllegalArgumentException("Invalid status value");
                }
                
                Order order = orderDAO.getOrderById(orderId);
                if (order == null) {
                    throw new IllegalArgumentException("Order not found");
                }
                
                boolean updated = orderDAO.updateOrderStatus(orderId, status);
                result.put("success", updated);
                result.put("message", updated ? "Status updated successfully" : "Failed to update status");
                
            } catch (NumberFormatException e) {
                result.put("success", false);
                result.put("message", "Invalid order ID");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            } catch (IllegalArgumentException e) {
                result.put("success", false);
                result.put("message", e.getMessage());
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            } catch (Exception e) {
                result.put("success", false);
                result.put("message", "Error updating status: " + e.getMessage());
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
            
            // Gửi response dạng JSON
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(gson.toJson(result));
        }
    }
    
    private boolean isValidStatus(String status) {
        return status != null && (
            status.equals("pending") ||
            status.equals("completed") ||
            status.equals("cancelled")
        );
    }
}
