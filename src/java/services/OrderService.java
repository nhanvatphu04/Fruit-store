package services;

import dao.OrderDAO;
import models.Order;
import java.util.List;

// Service xử lý nghiệp vụ đơn hàng
public class OrderService {
	private OrderDAO orderDAO;

	public OrderService() {
		orderDAO = new OrderDAO();
	}

	// Lấy danh sách đơn hàng theo user
	public List<Order> getOrdersByUserId(int userId) {
		return orderDAO.getOrdersByUserId(userId);
	}

	// Lấy đơn hàng theo id
	public Order getOrderById(int orderId) {
		return orderDAO.getOrderById(orderId);
	}

	// Thêm đơn hàng mới
	public boolean addOrder(Order order) {
		return orderDAO.addOrder(order);
	}

	// Cập nhật trạng thái đơn hàng
	public boolean updateOrderStatus(int orderId, String status) {
		return orderDAO.updateOrderStatus(orderId, status);
	}

	// Lấy tất cả đơn hàng (cho admin)
	public List<Order> getAllOrders() {
		return orderDAO.getAllOrders();
	}
}
