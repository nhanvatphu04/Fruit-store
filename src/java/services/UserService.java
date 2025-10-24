package services;

import dao.UserDAO;
import models.User;
import java.util.List;

// Service quản lý người dùng
public class UserService {
    private UserDAO userDAO;

    public UserService() {
        userDAO = new UserDAO();
    }

    // Đăng ký user mới
    public boolean register(User user) {
        return userDAO.addUser(user);
    }

    // Lấy user theo id
    public User getUserById(int id) {
        return userDAO.getUserById(id);
    }

    // Lấy user theo username
    public User getUserByUsername(String username) {
        return userDAO.getUserByUsername(username);
    }

    // Lấy tất cả user
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    // Cập nhật thông tin user
    public boolean updateUser(User user) {
        return userDAO.updateUser(user);
    }

    // Xoá user
    public boolean deleteUser(int id) {
        return userDAO.deleteUser(id);
    }
}
