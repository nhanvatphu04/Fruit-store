package controllers;

import com.google.gson.Gson;

import dao.CartComboDAO;
import dao.ComboDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Combo;
import models.User;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "CartApiController", urlPatterns = {"/cart/add-combo"})
public class CartApiController extends HttpServlet {
    private final ComboDAO comboDAO = new ComboDAO();
    private final CartComboDAO cartComboDAO = new CartComboDAO();
    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        Map<String, Object> jsonResponse = new HashMap<>();
        
        try {
            // Kiểm tra người dùng đã đăng nhập chưa
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");
            if (user == null) {
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Vui lòng đăng nhập để thêm vào giỏ hàng");
                response.getWriter().write(gson.toJson(jsonResponse));
                return;
            }
            
            // Lấy thông tin combo từ request
            int comboId = Integer.parseInt(request.getParameter("comboId"));
            Combo combo = comboDAO.getComboById(comboId);
            
            if (combo == null) {
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Không tìm thấy combo");
                response.getWriter().write(gson.toJson(jsonResponse));
                return;
            }
            
            // Kiểm tra combo có còn active không
            if (!combo.isActive()) {
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Combo này không còn khả dụng");
                response.getWriter().write(gson.toJson(jsonResponse));
                return;
            }
            
            // Thêm combo vào giỏ hàng
            boolean success = cartComboDAO.addComboToCart(user.getUserId(), comboId, 1);
            
            if (success) {
                jsonResponse.put("success", true);
                jsonResponse.put("message", "Đã thêm combo vào giỏ hàng");
            } else {
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Không thể thêm combo vào giỏ hàng");
            }
        } catch (NumberFormatException e) {
            jsonResponse.put("success", false);
            jsonResponse.put("message", "ID combo không hợp lệ");
        } catch (Exception e) {
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Đã xảy ra lỗi: " + e.getMessage());
        }
        
        response.getWriter().write(gson.toJson(jsonResponse));
    }
}