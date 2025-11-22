package controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dao.OrderDAO;
import dao.ProductDAO;
import dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import models.Order;
import models.OrderItem;
import models.OrderCombo;
import models.Product;
import models.Combo;
import models.User;


@WebServlet(name = "AdminOrderController", urlPatterns = {
    "/admin/orders",
    "/admin/orders/details/*",
    "/admin/orders/update-status",
    "/admin/orders/by-status"
})
public class AdminOrderController extends HttpServlet {
    private final OrderDAO orderDAO = new OrderDAO();
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
        String pathInfo = request.getPathInfo();
        System.out.println("DEBUG: ServletPath=" + path + ", PathInfo=" + pathInfo);
        
        if (path.equals("/admin/orders")) {
            System.out.println("DEBUG: Handling /admin/orders");
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
            System.out.println("DEBUG: Handling /admin/orders/by-status");
            // API lấy đơn hàng theo trạng thái
            String status = request.getParameter("status");
            List<Order> orders = status != null && !status.isEmpty() ?
                               orderDAO.getOrdersByStatus(status) :
                               orderDAO.getAllOrders();

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(gson.toJson(orders));
        }
        else if (path.startsWith("/admin/orders/details")) {
            System.out.println("DEBUG: Handling /admin/orders/details");
            // API lấy chi tiết đơn hàng
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            try {
                System.out.println("Processing order details request for path: " + path);
                System.out.println("PathInfo: " + pathInfo);

                // Extract order ID from pathInfo (e.g., "/1" from "/admin/orders/details/1")
                int orderId;
                if (pathInfo != null && !pathInfo.isEmpty() && !pathInfo.equals("/")) {
                    orderId = Integer.parseInt(pathInfo.substring(1)); // Remove leading "/"
                } else {
                    // Fallback: try to extract from path
                    orderId = Integer.parseInt(path.substring(path.lastIndexOf('/') + 1));
                }
                System.out.println("Parsed order ID: " + orderId);
                
                Order order = orderDAO.getOrderById(orderId);
                System.out.println("Order retrieved: " + (order != null ? "Yes (ID: " + order.getOrderId() + ")" : "No (null)"));

                if (order != null) {
                    // Lấy thông tin người dùng
                    User user = userDAO.getUserById(order.getUserId());
                    System.out.println("User retrieved: " + (user != null ? "Yes (ID: " + user.getUserId() + ")" : "No (null)"));

                    // Lấy danh sách sản phẩm trong đơn hàng
                    List<OrderItem> items = order.getOrderItems();
                    if (items == null) {
                        items = new ArrayList<>();
                    }
                    System.out.println("Order items count: " + items.size());

                    // Lấy thông tin sản phẩm cho mỗi item
                    for (OrderItem item : items) {
                        try {
                            Product product = productDAO.getProductById(item.getProductId());
                            item.setProduct(product);
                            System.out.println("Loaded product for item: " + item.getProductId() + 
                                             " - " + (product != null ? product.getName() : "null"));
                        } catch (Exception e) {
                            System.err.println("Error loading product " + item.getProductId() + ": " + e.getMessage());
                            e.printStackTrace();
                        }
                    }

                    // Tạo response object với items được serialize đúng cách
                    Map<String, Object> orderDetails = new HashMap<>();
                    orderDetails.put("orderId", order.getOrderId());
                    orderDetails.put("orderDate", order.getOrderDate() != null ? order.getOrderDate().toString() : "");
                    orderDetails.put("totalAmount", order.getTotalAmount() != null ? order.getTotalAmount().toString() : "0");
                    orderDetails.put("status", order.getStatus() != null ? order.getStatus() : "unknown");
                    
                    // Tạo customer map để đảm bảo serialization đúng
                    Map<String, Object> customerMap = new HashMap<>();
                    if (user != null) {
                        customerMap.put("userId", user.getUserId());
                        customerMap.put("username", user.getUsername() != null ? user.getUsername() : "");
                        customerMap.put("fullName", user.getFullName() != null ? user.getFullName() : "");
                        customerMap.put("email", user.getEmail() != null ? user.getEmail() : "");
                        customerMap.put("phone", user.getPhone() != null ? user.getPhone() : "");
                    }
                    orderDetails.put("customer", customerMap);

                    // Tạo danh sách items với thông tin sản phẩm
                    List<Map<String, Object>> itemsList = new ArrayList<>();
                    for (OrderItem item : items) {
                        Map<String, Object> itemMap = new HashMap<>();
                        itemMap.put("orderItemId", item.getOrderItemId());
                        itemMap.put("orderId", item.getOrderId());
                        itemMap.put("productId", item.getProductId());
                        itemMap.put("quantity", item.getQuantity());
                        itemMap.put("price", item.getPrice() != null ? item.getPrice().toString() : "0");

                        // Thêm thông tin sản phẩm
                        if (item.getProduct() != null) {
                            Map<String, Object> productMap = new HashMap<>();
                            productMap.put("productId", item.getProduct().getProductId());
                            productMap.put("name", item.getProduct().getName() != null ? item.getProduct().getName() : "Unknown Product");
                            productMap.put("imageUrl", item.getProduct().getImageUrl() != null ? item.getProduct().getImageUrl() : "");
                            itemMap.put("product", productMap);
                        } else {
                            // Đảm bảo có product object ngay cả khi null
                            Map<String, Object> productMap = new HashMap<>();
                            productMap.put("name", "Unknown Product");
                            itemMap.put("product", productMap);
                        }
                        itemsList.add(itemMap);
                    }
                    orderDetails.put("items", itemsList);

                    // Thêm danh sách combo trong đơn hàng
                    List<Map<String, Object>> combosList = new ArrayList<>();
                    List<OrderCombo> orderCombos = order.getOrderCombos();
                    if (orderCombos != null) {
                        for (OrderCombo oc : orderCombos) {
                            Map<String, Object> comboMap = new HashMap<>();
                            comboMap.put("orderComboId", oc.getOrderComboId());
                            comboMap.put("orderId", oc.getOrderId());
                            comboMap.put("comboId", oc.getComboId());
                            comboMap.put("quantity", oc.getQuantity());
                            comboMap.put("price", oc.getPrice() != null ? oc.getPrice().toString() : "0");

                            Combo combo = oc.getCombo();
                            if (combo != null) {
                                Map<String, Object> comboInfo = new HashMap<>();
                                comboInfo.put("comboId", combo.getComboId());
                                comboInfo.put("name", combo.getName() != null ? combo.getName() : "Combo");
                                comboInfo.put("imageUrl", combo.getImageUrl() != null ? combo.getImageUrl() : "");
                                comboInfo.put("description", combo.getDescription() != null ? combo.getDescription() : "");
                                comboInfo.put("salePrice", combo.getSalePrice() != null ? combo.getSalePrice().toString() : "0");
                                comboMap.put("combo", comboInfo);
                            }

                            combosList.add(comboMap);
                        }
                    }
                    orderDetails.put("combos", combosList);

                    // Gửi response dạng JSON
                    String jsonResponse = gson.toJson(orderDetails);
                    System.out.println("Order details JSON length: " + jsonResponse.length());
                    System.out.println("Order details response: " + jsonResponse);
                    try (java.io.PrintWriter out = response.getWriter()) {
                        out.write(jsonResponse);
                        out.flush();
                        System.out.println("Response written and flushed successfully");
                    }
                } else {
                    System.out.println("Order not found, returning 404");
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    Map<String, String> error = new HashMap<>();
                    error.put("message", "Order not found");
                    String errorJson = gson.toJson(error);
                    try (java.io.PrintWriter out = response.getWriter()) {
                        out.write(errorJson);
                        out.flush();
                    }
                }
            } catch (NumberFormatException e) {
                System.err.println("Invalid order ID format: " + e.getMessage());
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                Map<String, String> error = new HashMap<>();
                error.put("message", "Invalid order ID");
                try (java.io.PrintWriter out = response.getWriter()) {
                    out.write(gson.toJson(error));
                    out.flush();
                }
            } catch (Exception e) {
                System.err.println("Unexpected error in order details: " + e.getMessage());
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                Map<String, String> error = new HashMap<>();
                error.put("message", "Internal server error: " + e.getMessage());
                try (java.io.PrintWriter out = response.getWriter()) {
                    out.write(gson.toJson(error));
                    out.flush();
                }
            }
        }
        else {
            // Unmatched path
            System.out.println("Unmatched path in doGet: " + path);
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            Map<String, String> error = new HashMap<>();
            error.put("message", "Endpoint not found");
            response.getWriter().write(gson.toJson(error));
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
