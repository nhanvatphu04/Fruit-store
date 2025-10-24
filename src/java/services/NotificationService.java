package services;

import dao.NotificationDAO;
import models.Notification;
import java.util.List;

// Service gửi và quản lý thông báo
public class NotificationService {
    private NotificationDAO notificationDAO;

    public NotificationService() {
        notificationDAO = new NotificationDAO();
    }

    // Lấy thông báo theo user
    public List<Notification> getNotificationsByUserId(int userId) {
        return notificationDAO.getNotificationsByUserId(userId);
    }

    // Thêm thông báo
    public boolean addNotification(Notification notification) {
        return notificationDAO.addNotification(notification);
    }

    // Đánh dấu đã đọc
    public boolean markAsRead(int notificationId) {
        return notificationDAO.markAsRead(notificationId);
    }

    // Xoá thông báo
    public boolean deleteNotification(int notificationId) {
        return notificationDAO.deleteNotification(notificationId);
    }
}
