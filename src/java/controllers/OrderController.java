package controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dao.OrderDAO;
import dao.ProductDAO;
import models.Order;
import models.OrderItem;
import models.Product;
import models.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "OrderController", urlPatterns = {
    "/orders", 
    "/orders/details/*",
    "/orders/cancel"
})
public class OrderController extends HttpServlet {
    private final OrderDAO orderDAO = new OrderDAO();
    private final ProductDAO productDAO = new ProductDAO();
    private final Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

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

        String path = request.getServletPath();
        
        if (path.equals("/orders")) {
            // Display orders page
            try {
                // Lấy danh sách đơn hàng của user
                List<Order> orders = orderDAO.getOrdersByUserId(user.getUserId());
                request.setAttribute("orders", orders);
                
                // Chuyển hướng đến orders.jsp
                request.getRequestDispatcher("/jsp/orders.jsp").forward(request, response);
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect(request.getContextPath() + "/error");
            }
        }
        else if (path.startsWith("/orders/details")) {
            // API endpoint for order details
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            
            try {
                String pathInfo = request.getPathInfo();
                System.out.println("Customer order details request - PathInfo: " + pathInfo);
                
                int orderId;
                if (pathInfo != null && !pathInfo.isEmpty() && !pathInfo.equals("/")) {
                    orderId = Integer.parseInt(pathInfo.substring(1));
                } else {
                    orderId = Integer.parseInt(path.substring(path.lastIndexOf('/') + 1));
                }
                
                System.out.println("Customer requesting order ID: " + orderId);
                
                Order order = orderDAO.getOrderById(orderId);
                
                if (order != null) {
                    // Verify that this order belongs to the logged-in user
                    if (order.getUserId() != user.getUserId()) {
                        System.out.println("Access denied: Order " + orderId + " does not belong to user " + user.getUserId());
                        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                        Map<String, String> error = new HashMap<>();
                        error.put("message", "Bạn không có quyền xem đơn hàng này");
                        response.getWriter().write(gson.toJson(error));
                        return;
                    }
                    
                    // Get order items
                    List<OrderItem> items = order.getOrderItems();
                    System.out.println("DEBUG: Order items from order object: " + (items != null ? items.size() : "null"));
                    if (items == null) {
                        items = new ArrayList<>();
                    }

                    System.out.println("DEBUG: Processing " + items.size() + " items for order " + orderId);

                    // Load product details for each item
                    for (OrderItem item : items) {
                        try {
                            System.out.println("DEBUG: Loading product for item " + item.getOrderItemId() + ", productId=" + item.getProductId());
                            Product product = productDAO.getProductById(item.getProductId());
                            item.setProduct(product);
                            System.out.println("DEBUG: Product loaded: " + (product != null ? product.getName() : "null"));
                        } catch (Exception e) {
                            System.err.println("Error loading product " + item.getProductId() + ": " + e.getMessage());
                        }
                    }
                    
                    // Build response
                    Map<String, Object> orderDetails = new HashMap<>();
                    orderDetails.put("orderId", order.getOrderId());
                    orderDetails.put("orderDate", order.getOrderDate() != null ? order.getOrderDate().toString() : "");
                    orderDetails.put("totalAmount", order.getTotalAmount() != null ? order.getTotalAmount().toString() : "0");
                    orderDetails.put("status", order.getStatus() != null ? order.getStatus() : "unknown");
                    orderDetails.put("discountCode", order.getDiscountCode());
                    orderDetails.put("discountAmount", order.getDiscountAmount() != null ? order.getDiscountAmount().toString() : "0");
                    
                    // Customer info
                    Map<String, Object> customerMap = new HashMap<>();
                    customerMap.put("userId", user.getUserId());
                    customerMap.put("username", user.getUsername() != null ? user.getUsername() : "");
                    customerMap.put("fullName", user.getFullName() != null ? user.getFullName() : "");
                    customerMap.put("email", user.getEmail() != null ? user.getEmail() : "");
                    customerMap.put("phone", user.getPhone() != null ? user.getPhone() : "");
                    orderDetails.put("customer", customerMap);
                    
                    // Order items
                    List<Map<String, Object>> itemsList = new ArrayList<>();
                    System.out.println("DEBUG: Building items list, total items: " + items.size());
                    for (OrderItem item : items) {
                        System.out.println("DEBUG: Building item map for item " + item.getOrderItemId());
                        Map<String, Object> itemMap = new HashMap<>();
                        itemMap.put("orderItemId", item.getOrderItemId());
                        itemMap.put("orderId", item.getOrderId());
                        itemMap.put("productId", item.getProductId());
                        itemMap.put("quantity", item.getQuantity());
                        itemMap.put("price", item.getPrice() != null ? item.getPrice().toString() : "0");

                        if (item.getProduct() != null) {
                            Map<String, Object> productMap = new HashMap<>();
                            productMap.put("productId", item.getProduct().getProductId());
                            productMap.put("name", item.getProduct().getName() != null ? item.getProduct().getName() : "Unknown Product");
                            productMap.put("imageUrl", item.getProduct().getImageUrl() != null ? item.getProduct().getImageUrl() : "");
                            itemMap.put("product", productMap);
                            System.out.println("DEBUG: Added product: " + item.getProduct().getName());
                        } else {
                            Map<String, Object> productMap = new HashMap<>();
                            productMap.put("name", "Unknown Product");
                            itemMap.put("product", productMap);
                            System.out.println("DEBUG: Product is null, using Unknown Product");
                        }
                        itemsList.add(itemMap);
                    }
                    System.out.println("DEBUG: Final items list size: " + itemsList.size());
                    orderDetails.put("items", itemsList);
                    
                    String jsonResponse = gson.toJson(orderDetails);
                    System.out.println("Customer order details response length: " + jsonResponse.length());
                    System.out.println("DEBUG: JSON Response: " + jsonResponse);
                    response.getWriter().write(jsonResponse);
                    response.getWriter().flush();
                } else {
                    System.out.println("Order not found: " + orderId);
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    Map<String, String> error = new HashMap<>();
                    error.put("message", "Không tìm thấy đơn hàng");
                    response.getWriter().write(gson.toJson(error));
                }
            } catch (NumberFormatException e) {
                System.err.println("Invalid order ID format: " + e.getMessage());
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                Map<String, String> error = new HashMap<>();
                error.put("message", "Mã đơn hàng không hợp lệ");
                response.getWriter().write(gson.toJson(error));
            } catch (Exception e) {
                System.err.println("Error loading customer order details: " + e.getMessage());
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                Map<String, String> error = new HashMap<>();
                error.put("message", "Lỗi hệ thống: " + e.getMessage());
                response.getWriter().write(gson.toJson(error));
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        // Kiểm tra xem người dùng đã đăng nhập chưa
        if (user == null) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Vui lòng đăng nhập");
            response.getWriter().write(gson.toJson(error));
            return;
        }

        String path = request.getServletPath();
        
        if (path.equals("/orders/cancel")) {
            // Cancel order
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            
            Map<String, Object> result = new HashMap<>();
            
            try {
                int orderId = Integer.parseInt(request.getParameter("orderId"));
                System.out.println("User " + user.getUserId() + " requesting to cancel order " + orderId);
                
                // Get the order
                Order order = orderDAO.getOrderById(orderId);
                
                if (order == null) {
                    result.put("success", false);
                    result.put("message", "Không tìm thấy đơn hàng");
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                } else if (order.getUserId() != user.getUserId()) {
                    // Verify ownership
                    result.put("success", false);
                    result.put("message", "Bạn không có quyền hủy đơn hàng này");
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                } else if (!"pending".equals(order.getStatus())) {
                    // Can only cancel pending orders
                    result.put("success", false);
                    result.put("message", "Chỉ có thể hủy đơn hàng đang chờ xác nhận");
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                } else {
                    // Cancel the order
                    boolean cancelled = orderDAO.updateOrderStatus(orderId, "cancelled");
                    result.put("success", cancelled);
                    result.put("message", cancelled ? "Đơn hàng đã được hủy thành công" : "Không thể hủy đơn hàng");
                    
                    if (cancelled) {
                        System.out.println("Order " + orderId + " cancelled successfully by user " + user.getUserId());
                    }
                }
            } catch (NumberFormatException e) {
                result.put("success", false);
                result.put("message", "Mã đơn hàng không hợp lệ");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            } catch (Exception e) {
                System.err.println("Error cancelling order: " + e.getMessage());
                e.printStackTrace();
                result.put("success", false);
                result.put("message", "Lỗi hệ thống: " + e.getMessage());
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
            
            response.getWriter().write(gson.toJson(result));
            response.getWriter().flush();
        }
    }
}