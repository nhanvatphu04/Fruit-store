package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import models.Order;
import models.OrderItem;
import models.Product;
import utils.DbConnect;

// DAO cho đơn hàng
public class OrderDAO {

	// Lấy đơn hàng theo user
	public List<Order> getOrdersByUserId(int userId) {
		List<Order> list = new ArrayList<>();
		String sql = "SELECT * FROM orders WHERE user_id = ? ORDER BY order_date DESC";
		try (Connection conn = DbConnect.getInstance().getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, userId);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Order order = mapResultSetToOrder(rs);
				list.add(order);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Load order items AFTER connection is closed
		for (Order order : list) {
			loadOrderItems(order);
		}

		return list;
	}

	// Lấy đơn hàng theo id
	public Order getOrderById(int orderId) {
		String sql = "SELECT * FROM orders WHERE order_id = ?";
		Order order = null;
		try (Connection conn = DbConnect.getInstance().getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, orderId);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				order = mapResultSetToOrder(rs);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Load order items AFTER connection is closed
		if (order != null) {
			loadOrderItems(order);
		}

		return order;
	}

	// Thêm đơn hàng
	public boolean addOrder(Order order) {
		String sql = "INSERT INTO orders (user_id, total_amount, status, discount_code, discount_amount) VALUES (?, ?, ?, ?, ?)";
		try (Connection conn = DbConnect.getInstance().getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, order.getUserId());
			ps.setBigDecimal(2, order.getTotalAmount());
			ps.setString(3, order.getStatus());
			ps.setString(4, order.getDiscountCode());
			ps.setBigDecimal(5, order.getDiscountAmount());
			int rows = ps.executeUpdate();
			return rows > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	// Cập nhật trạng thái đơn hàng
	public boolean updateOrderStatus(int orderId, String status) {
		String sql = "UPDATE orders SET status = ? WHERE order_id = ?";
		try (Connection conn = DbConnect.getInstance().getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, status);
			ps.setInt(2, orderId);
			int rows = ps.executeUpdate();
			return rows > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	// Lấy tất cả đơn hàng
	public List<Order> getAllOrders() {
		List<Order> list = new ArrayList<>();
		String sql = "SELECT o.*, u.username as user_name FROM orders o " +
					"INNER JOIN users u ON o.user_id = u.user_id " +
					"ORDER BY o.order_date DESC";
		try (Connection conn = DbConnect.getInstance().getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Order order = mapResultSetToOrder(rs);
				order.setUserName(rs.getString("user_name"));
				list.add(order);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Load order items AFTER connection is closed
		for (Order order : list) {
			loadOrderItems(order);
		}

		return list;
	}

	// Lấy đơn hàng theo trạng thái
	public List<Order> getOrdersByStatus(String status) {
		List<Order> list = new ArrayList<>();
		String sql = "SELECT o.*, u.username as user_name FROM orders o " +
					"INNER JOIN users u ON o.user_id = u.user_id " +
					"WHERE o.status = ? ORDER BY o.order_date DESC";
		try (Connection conn = DbConnect.getInstance().getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, status);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Order order = mapResultSetToOrder(rs);
				order.setUserName(rs.getString("user_name"));
				list.add(order);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Load order items AFTER connection is closed
		for (Order order : list) {
			loadOrderItems(order);
		}

		return list;
	}

	// Đếm số đơn hàng theo trạng thái 
	public int countOrdersByStatus(String status) {
		String sql = "SELECT COUNT(*) FROM orders WHERE status = ?";
		try (Connection conn = DbConnect.getInstance().getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, status);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	// Hàm tiện ích chuyển ResultSet sang Order
	private Order mapResultSetToOrder(ResultSet rs) throws SQLException {
		Order o = new Order();
		o.setOrderId(rs.getInt("order_id"));
		o.setUserId(rs.getInt("user_id"));
		o.setTotalAmount(rs.getBigDecimal("total_amount"));
		o.setStatus(rs.getString("status"));
		o.setOrderDate(rs.getTimestamp("order_date"));
		o.setDiscountCode(rs.getString("discount_code"));
		o.setDiscountAmount(rs.getBigDecimal("discount_amount"));
		return o;
	}

	// Load order items with product details
	private void loadOrderItems(Order order) {
		OrderItemDAO orderItemDAO = new OrderItemDAO();
		ProductDAO productDAO = new ProductDAO();
		List<OrderItem> orderItems = orderItemDAO.getOrderItems(order.getOrderId());

		System.out.println("DEBUG: Loading order items for order " + order.getOrderId() + ", found " + orderItems.size() + " items");

		// Load product details for each order item
		for (OrderItem item : orderItems) {
			System.out.println("DEBUG: Loading product for item " + item.getOrderItemId() + ", productId=" + item.getProductId());
			Product product = productDAO.getProductById(item.getProductId());
			item.setProduct(product);
			System.out.println("DEBUG: Product loaded: " + (product != null ? product.getName() : "null"));
		}

		order.setOrderItems(orderItems);
		System.out.println("DEBUG: Order items set, total items: " + order.getOrderItems().size());
	}

	// Lấy tổng số đơn hàng
	public int getTotalOrders() throws SQLException {
		String sql = "SELECT COUNT(*) as total FROM orders";
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

	// Lấy tổng doanh thu
	public double getTotalRevenue() throws SQLException {
		String sql = "SELECT SUM(total_amount) as total FROM orders WHERE status = 'completed'";
		try (Connection conn = DbConnect.getInstance().getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getDouble("total");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0.0;
	}

	// Lấy đơn hàng gần đây
	public List<Order> getRecentOrders(int limit) throws SQLException {
		List<Order> list = new ArrayList<>();
		String sql = "SELECT o.*, u.username as user_name FROM orders o " +
					"INNER JOIN users u ON o.user_id = u.user_id " +
					"ORDER BY o.order_date DESC LIMIT ?";
		try (Connection conn = DbConnect.getInstance().getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, limit);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Order o = mapResultSetToOrder(rs);
				o.setUserName(rs.getString("user_name"));
				list.add(o);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Load order items AFTER connection is closed
		for (Order order : list) {
			loadOrderItems(order);
		}

		return list;
	}
}
