package services;

import dao.UserDAO;
import models.User;
import utils.PasswordUtils;

// Service xác thực, phân quyền, quản lý session đăng nhập
public class AuthService {
    private UserDAO userDAO;

    public AuthService() {
        userDAO = new UserDAO();
    }

    // Đăng ký user mới
    public boolean register(User user) {
        // Kiểm tra username đã tồn tại
        if (userDAO.getUserByUsername(user.getUsername()) != null) {
            return false;
        }
        
        // Mã hóa password trước khi lưu
        String hashedPassword = PasswordUtils.hashPassword(user.getPassword());
        if (hashedPassword == null) {
            return false;
        }
        user.setPassword(hashedPassword);
        
        // Thêm user mới
        return userDAO.addUser(user);
    }

    // Đăng nhập: trả về User nếu thành công, null nếu thất bại
    public User login(String username, String password) {
        User user = userDAO.getUserByUsername(username);
        String hashedPassword = PasswordUtils.hashPassword(password);
        if (user != null && hashedPassword != null && user.getPassword().equals(hashedPassword)) {
            return user;
        }
        return null;
    }

    // Kiểm tra quyền admin
    public boolean isAdmin(User user) {
        return user != null && "admin".equals(user.getRole());
    }

    // Kiểm tra quyền customer
    public boolean isCustomer(User user) {
        return user != null && "customer".equals(user.getRole());
    }
    
    // Kiểm tra password cũ
    public boolean validatePassword(User user, String password) {
        String hashedPassword = PasswordUtils.hashPassword(password);
        return hashedPassword != null && hashedPassword.equals(user.getPassword());
    }
}
