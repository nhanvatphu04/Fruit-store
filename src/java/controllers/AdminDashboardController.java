package controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import models.Order;
import models.User;
import services.DashboardService;
import com.google.gson.Gson;

@WebServlet(name = "AdminDashboardController", urlPatterns = {"/admin/dashboard"})
public class AdminDashboardController extends HttpServlet {
    private final DashboardService dashboardService;
    private final Gson gson;

    public AdminDashboardController() {
        this.dashboardService = new DashboardService();
        this.gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Kiểm tra xem người dùng đã đăng nhập và có vai trò quản trị viên chưa
        User user = (User) request.getSession().getAttribute("user");
        if (user == null || !"admin".equals(user.getRole())) {
            // Chuyển hướng đến trang lỗi hoặc trang đăng nhập
            response.sendRedirect(request.getContextPath() + "/jsp/error/403.jsp");
            return;
        }
        
        try {
            // Lấy dữ liệu thống kê
            int totalUsers = dashboardService.getTotalUsers();
            int totalOrders = dashboardService.getTotalOrders();
            double totalRevenue = dashboardService.getTotalRevenue();
            
            // Lấy danh sách đơn hàng gần đây
            List<Order> recentOrders = dashboardService.getRecentOrders(10);
            
            // Lấy danh sách sản phẩm bán chạy nhất
            Map<String, Integer> topProducts = dashboardService.getTopSellingProducts(5);
            
            // Chuyển đổi dữ liệu sản phẩm hàng đầu cho biểu đồ
            String[] labels = topProducts.keySet().toArray(new String[0]);
            Integer[] data = topProducts.values().toArray(new Integer[0]);
            
            // Đặt thuộc tính cho JSP
            request.setAttribute("totalUsers", totalUsers);
            request.setAttribute("totalOrders", totalOrders);
            request.setAttribute("totalRevenue", String.format("%.2f", totalRevenue));
            request.setAttribute("recentOrders", recentOrders);
            request.setAttribute("topProductsLabels", gson.toJson(labels));
            request.setAttribute("topProductsData", gson.toJson(data));
            
            // Chuyển tiếp đến bảng điều khiển
            request.getRequestDispatcher("/jsp/admin/dashboard.jsp")
                  .forward(request, response);
            
        } catch (SQLException e) {
            // Log error and forward to error page
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/error");
        }
    }
}