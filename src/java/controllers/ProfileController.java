package controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.User;
import services.AuthService;
import services.UserService;
import utils.PasswordUtils;
import java.io.IOException;

@WebServlet(name = "ProfileController", urlPatterns = {"/profile"})
public class ProfileController extends HttpServlet {
    private AuthService authService;
    private UserService userService;
    
    @Override
    public void init() throws ServletException {
        super.init();
        authService = new AuthService();
        userService = new UserService();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        request.getRequestDispatcher("/jsp/profile.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        User currentUser = (User) session.getAttribute("user");
        User updatedUser = new User();
        updatedUser.setUserId(currentUser.getUserId());
        updatedUser.setUsername(currentUser.getUsername());
        updatedUser.setRole(currentUser.getRole());
        
        // Lấy thông tin từ form
        String email = request.getParameter("email");
        String fullName = request.getParameter("fullName");
        String phone = request.getParameter("phone");
        String address = buildFullAddress(request);
        String currentPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");
        
        // Cập nhật thông tin cơ bản
        updatedUser.setEmail(currentUser.getEmail()); // Giữ nguyên email cũ nếu không được cập nhật
        if (email != null && !email.trim().isEmpty()) {
            updatedUser.setEmail(email);
        }
        updatedUser.setFullName(fullName);
        updatedUser.setPhone(phone);
        updatedUser.setAddress(address);
        updatedUser.setPassword(currentUser.getPassword());
        
        // Xử lý avatar được chọn
        String selectedAvatar = request.getParameter("selectedAvatar");
        if (selectedAvatar != null && !selectedAvatar.trim().isEmpty()) {
            updatedUser.setAvatarUrl(selectedAvatar);
        } else {
            updatedUser.setAvatarUrl(currentUser.getAvatarUrl());
        }
        
        // Xử lý đổi mật khẩu
        if (currentPassword != null && !currentPassword.isEmpty()) {
            if (newPassword == null || newPassword.isEmpty()) {
                request.setAttribute("error", "Vui lòng nhập mật khẩu mới!");
                request.getRequestDispatcher("/jsp/profile.jsp").forward(request, response);
                return;
            }
            
            // Kiểm tra mật khẩu hiện tại
            if (!authService.validatePassword(currentUser, currentPassword)) {
                request.setAttribute("error", "Mật khẩu hiện tại không đúng!");
                request.getRequestDispatcher("/jsp/profile.jsp").forward(request, response);
                return;
            }

            String hashedPassword = PasswordUtils.hashPassword(newPassword);
            if (hashedPassword == null) {
                request.setAttribute("error", "Có lỗi xảy ra khi mã hóa mật khẩu!");
                request.getRequestDispatcher("/jsp/profile.jsp").forward(request, response);
                return;
            }
            
            updatedUser.setPassword(hashedPassword);
        }
        
        // Cập nhật thông tin
        try {
            if (userService.updateUser(updatedUser)) {
                // Cập nhật session
                session.setAttribute("user", userService.getUserById(currentUser.getUserId()));
                request.setAttribute("success", "Cập nhật thông tin thành công!");
            } else {
                request.setAttribute("error", "Có lỗi xảy ra, vui lòng thử lại!");
            }
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
        }
        
        request.getRequestDispatcher("/jsp/profile.jsp").forward(request, response);
    }
    
    /**
     * Build full address from individual components or use combined address
     * Supports Province Open API integration with backward compatibility
     * 
     * @param request HttpServletRequest containing address parameters
     * @return Full address string
     */
    private String buildFullAddress(HttpServletRequest request) {
        // First, check if combined address is already provided (from hidden field in JSP)
        String combinedAddress = request.getParameter("address");
        if (combinedAddress != null && !combinedAddress.trim().isEmpty()) {
            return combinedAddress.trim();
        }
        
        // Otherwise, try to build from individual components (backward compatibility)
        String detailedAddress = request.getParameter("detailedAddress");
        String ward = request.getParameter("ward");
        String district = request.getParameter("district");
        String province = request.getParameter("province");
        
        // Build address from components
        StringBuilder addressBuilder = new StringBuilder();
        
        if (detailedAddress != null && !detailedAddress.trim().isEmpty()) {
            addressBuilder.append(detailedAddress.trim());
        }
        
        if (ward != null && !ward.trim().isEmpty()) {
            if (addressBuilder.length() > 0) {
                addressBuilder.append(", ");
            }
            addressBuilder.append(ward.trim());
        }
        
        if (district != null && !district.trim().isEmpty()) {
            if (addressBuilder.length() > 0) {
                addressBuilder.append(", ");
            }
            addressBuilder.append(district.trim());
        }
        
        if (province != null && !province.trim().isEmpty()) {
            if (addressBuilder.length() > 0) {
                addressBuilder.append(", ");
            }
            addressBuilder.append(province.trim());
        }
        
        return addressBuilder.toString();
    }

}