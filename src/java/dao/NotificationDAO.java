package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import models.Notification;
import utils.DbConnect;

// DAO cho thông báo
public class NotificationDAO {

	// Lấy thông báo theo user
	public List<Notification> getNotificationsByUserId(int userId) {
		List<Notification> list = new ArrayList<>();
		String sql = "SELECT * FROM notifications WHERE user_id = ? ORDER BY created_at DESC";
		try (Connection conn = DbConnect.getInstance().getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, userId);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				list.add(mapResultSetToNotification(rs));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	// Thêm thông báo
	public boolean addNotification(Notification notification) {
		String sql = "INSERT INTO notifications (user_id, title, message, type, is_read) VALUES (?, ?, ?, ?, ?)";
		try (Connection conn = DbConnect.getInstance().getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, notification.getUserId());
			ps.setString(2, notification.getTitle());
			ps.setString(3, notification.getMessage());
			ps.setString(4, notification.getType());
			ps.setBoolean(5, notification.isRead());
			int rows = ps.executeUpdate();
			return rows > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	// Đánh dấu đã đọc
	public boolean markAsRead(int notificationId) {
		String sql = "UPDATE notifications SET is_read = TRUE WHERE notification_id = ?";
		try (Connection conn = DbConnect.getInstance().getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, notificationId);
			int rows = ps.executeUpdate();
			return rows > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	// Xoá thông báo
	public boolean deleteNotification(int notificationId) {
		String sql = "DELETE FROM notifications WHERE notification_id = ?";
		try (Connection conn = DbConnect.getInstance().getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, notificationId);
			int rows = ps.executeUpdate();
			return rows > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	// Hàm tiện ích chuyển ResultSet sang Notification
	private Notification mapResultSetToNotification(ResultSet rs) throws SQLException {
		Notification n = new Notification();
		n.setNotificationId(rs.getInt("notification_id"));
		n.setUserId(rs.getInt("user_id"));
		n.setTitle(rs.getString("title"));
		n.setMessage(rs.getString("message"));
		n.setType(rs.getString("type"));
		n.setRead(rs.getBoolean("is_read"));
		n.setCreatedAt(rs.getTimestamp("created_at"));
		return n;
	}
}
