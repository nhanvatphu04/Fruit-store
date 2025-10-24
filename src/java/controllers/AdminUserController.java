package controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import models.User;
import services.UserService;
import utils.JsonResponse;

@WebServlet(name = "AdminUserController", urlPatterns = {
    "/admin/users",
    "/admin/users/get/*",
    "/admin/users/update-role"
})

public class AdminUserController extends HttpServlet {
    private UserService userService;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        super.init();
        userService = new UserService();
        gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Kiểm tra xem người dùng đã đăng nhập và có vai trò quản trị viên chưa
        User currentUser = (User) request.getSession().getAttribute("user");
        if (currentUser == null || !"admin".equals(currentUser.getRole())) {
            response.sendRedirect(request.getContextPath() + "/jsp/error/403.jsp");
            return;
        }
        
        String servletPath = request.getServletPath();
        String pathInfo = request.getPathInfo();
        
        // Lấy người dùng đơn lẻ để chỉnh sửa
        if ("/admin/users/get".equals(servletPath) && pathInfo != null) {
            try {
                String userId = pathInfo.substring(1); // Loại bỏ dấu / đầu tiên
                User user = userService.getUserById(Integer.parseInt(userId));
                
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                
                if (user != null) {
                    out.print(gson.toJson(user));
                } else {
                    JsonResponse jsonResponse = new JsonResponse();
                    jsonResponse.setSuccess(false);
                    jsonResponse.setMessage("User not found");
                    out.print(gson.toJson(jsonResponse));
                }
                out.flush();
                return;
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid user ID");
                return;
            }
        }
        
        // Liệt kê tất cả người dùng
        List<User> users = userService.getAllUsers();
        request.setAttribute("users", users);
        request.getRequestDispatcher("/jsp/admin/users.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        JsonResponse jsonResponse = new JsonResponse();

        // Kiểm tra xem người dùng đã đăng nhập và có vai trò quản trị viên chưa
        User currentUser = (User) request.getSession().getAttribute("user");
        if (currentUser == null || !"admin".equals(currentUser.getRole())) {
            jsonResponse.setSuccess(false);
            jsonResponse.setMessage("Access denied. Admin privileges required.");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            out.print(gson.toJson(jsonResponse));
            out.flush();
            return;
        }
        
        try {
            if ("/admin/users/update-role".equals(request.getServletPath())) {
                handleUpdateRole(request, jsonResponse);
            } else {
                jsonResponse.setSuccess(false);
                jsonResponse.setMessage("Invalid operation");
            }
        } catch (Exception e) {
            jsonResponse.setSuccess(false);
            jsonResponse.setMessage("Error: " + e.getMessage());
        }
        
        out.print(gson.toJson(jsonResponse));
        out.flush();
    }
    
    
    private void handleUpdateRole(HttpServletRequest request, JsonResponse response) {
        try {
            int userId = Integer.parseInt(request.getParameter("userId"));
            String role = request.getParameter("role");
            
            // Ngăn người dùng admin thay đổi vai trò của chính họ cho mục bảo mật
            User currentUser = (User) request.getSession().getAttribute("user");
            if (currentUser.getUserId() == userId) {
                response.setSuccess(false);
                response.setMessage("Không thể thay đổi vai trò của chính mình!");
                return;
            }
            
            User user = userService.getUserById(userId);
            if (user != null) {
                user.setRole(role);
                if (userService.updateUser(user)) {
                    response.setSuccess(true);
                    response.setMessage("Cập nhật vai trò thành công!");
                } else {
                    response.setSuccess(false);
                    response.setMessage("Không thể cập nhật vai trò!");
                }
            } else {
                response.setSuccess(false);
                response.setMessage("Không tìm thấy người dùng!");
            }
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Có lỗi xảy ra: " + e.getMessage());
        }
    }
}