package controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dao.DiscountDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import models.Discount;
import models.User;

@WebServlet(name = "AdminDiscountController", urlPatterns = {
    "/admin/discounts",
    "/admin/discounts/list",
    "/admin/discounts/add",
    "/admin/discounts/update",
    "/admin/discounts/delete"
})
public class AdminDiscountController extends HttpServlet {
    private final DiscountDAO discountDAO = new DiscountDAO();
    private final Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Kiểm tra xem người dùng đã đăng nhập và có vai trò quản trị viên chưa
        User user = (User) request.getSession().getAttribute("user");
        if (user == null || !"admin".equals(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/jsp/error/403.jsp");
            return;
        }
        
        String path = request.getServletPath();
        
        if (path.equals("/admin/discounts")) {
            // Hiển thị trang quản lý mã giảm giá
            List<Discount> discounts = discountDAO.getAllDiscounts();
            request.setAttribute("discounts", discounts);
            request.getRequestDispatcher("/jsp/admin/discounts.jsp").forward(request, response);
        } 
        else if (path.equals("/admin/discounts/list")) {
            // API lấy danh sách mã giảm giá
            List<Discount> discounts = discountDAO.getAllDiscounts();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(gson.toJson(discounts));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        // Kiểm tra xem người dùng đã đăng nhập và có vai trò quản trị viên chưa
        User user = (User) request.getSession().getAttribute("user");
        if (user == null || !"admin".equals(user.getRole())) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Access denied. Admin privileges required.");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write(gson.toJson(error));
            return;
        }
        
        String path = request.getServletPath();
        Map<String, Object> result = new HashMap<>();
        
        try {
            if (path.equals("/admin/discounts/add") || path.equals("/admin/discounts/update")) {
                // Phân tích các tham số yêu cầu
                Discount discount = new Discount();
                if (path.equals("/admin/discounts/update")) {
                    discount.setDiscountId(Integer.parseInt(request.getParameter("discountId")));
                }
                
                discount.setCode(request.getParameter("code"));
                discount.setDescription(request.getParameter("description"));
                discount.setDiscountType(request.getParameter("discountType"));
                discount.setDiscountValue(new BigDecimal(request.getParameter("discountValue")));
                discount.setMinOrderAmount(new BigDecimal(request.getParameter("minOrderAmount")));
                discount.setMaxDiscountAmount(new BigDecimal(request.getParameter("maxDiscountAmount")));
                
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String startDate = request.getParameter("startDate");
                String endDate = request.getParameter("endDate");
                
                if (startDate != null && !startDate.isEmpty()) {
                    discount.setStartDate(new Timestamp(sdf.parse(startDate).getTime()));
                }
                if (endDate != null && !endDate.isEmpty()) {
                    discount.setEndDate(new Timestamp(sdf.parse(endDate).getTime()));
                }
                
                discount.setUsageLimit(Integer.parseInt(request.getParameter("usageLimit")));
                discount.setUsedCount(Integer.parseInt(request.getParameter("usedCount")));
                discount.setActive(Boolean.parseBoolean(request.getParameter("isActive")));
                
                boolean success;
                if (path.equals("/admin/discounts/add")) {
                    success = discountDAO.addDiscount(discount);
                    result.put("message", success ? "Discount added successfully" : "Failed to add discount");
                } else {
                    success = discountDAO.updateDiscount(discount);
                    result.put("message", success ? "Discount updated successfully" : "Failed to update discount");
                }
                result.put("success", success);
                
            } else if (path.equals("/admin/discounts/delete")) {
                int discountId = Integer.parseInt(request.getParameter("discountId"));
                boolean success = discountDAO.deleteDiscount(discountId);
                result.put("success", success);
                result.put("message", success ? "Discount deleted successfully" : "Failed to delete discount");
            }
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Error: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        
        response.getWriter().write(gson.toJson(result));
    }
}