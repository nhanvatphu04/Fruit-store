package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import models.User;
import utils.DbConnect;

// DAO cho người dùng
public class UserDAO {

	// Lấy user theo id
	public User getUserById(int id) {
		String sql = "SELECT * FROM users WHERE user_id = ?";
		try (Connection conn = DbConnect.getInstance().getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return mapResultSetToUser(rs);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Lấy user theo username
	public User getUserByUsername(String username) {
		String sql = "SELECT * FROM users WHERE username = ?";
		try (Connection conn = DbConnect.getInstance().getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return mapResultSetToUser(rs);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Lấy tất cả user
	public List<User> getAllUsers() {
		List<User> list = new ArrayList<>();
		String sql = "SELECT * FROM users";
		try (Connection conn = DbConnect.getInstance().getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				list.add(mapResultSetToUser(rs));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	// Thêm user mới
	public boolean addUser(User user) {
		String sql = "INSERT INTO users (username, email, password, role) VALUES (?, ?, ?, ?)";
		try (Connection conn = DbConnect.getInstance().getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, user.getUsername());
			ps.setString(2, user.getEmail());
			ps.setString(3, user.getPassword());
			ps.setString(4, user.getRole());
			int rows = ps.executeUpdate();
			return rows > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

    // Cập nhật thông tin user
    public boolean updateUser(User user) {
        String sql = "UPDATE users SET username=?, email=?, password=?, role=?, full_name=?, phone=?, address=?, avatar_url=? WHERE user_id=?";
        try (Connection conn = DbConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getRole());
            ps.setString(5, user.getFullName());
            ps.setString(6, user.getPhone());
            ps.setString(7, user.getAddress());
            ps.setString(8, user.getAvatarUrl());
            ps.setInt(9, user.getUserId());
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }	// Xoá user
	public boolean deleteUser(int id) {
		String sql = "DELETE FROM users WHERE user_id = ?";
		try (Connection conn = DbConnect.getInstance().getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, id);
			int rows = ps.executeUpdate();
			return rows > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	// Lấy tổng số user
	public int getTotalUsers() throws SQLException {
		String sql = "SELECT COUNT(*) as total FROM users";
		try (Connection conn = DbConnect.getInstance().getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getInt("total");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	// Hàm tiện ích chuyển ResultSet sang User
	private User mapResultSetToUser(ResultSet rs) throws SQLException {
		User u = new User();
		u.setUserId(rs.getInt("user_id"));
		u.setUsername(rs.getString("username"));
		u.setEmail(rs.getString("email"));
		u.setPassword(rs.getString("password"));
		u.setRole(rs.getString("role"));
		u.setFullName(rs.getString("full_name"));
		u.setPhone(rs.getString("phone"));
		u.setAddress(rs.getString("address"));
		u.setAvatarUrl(rs.getString("avatar_url"));
		u.setCreatedAt(rs.getTimestamp("created_at"));
		return u;
	}
}
