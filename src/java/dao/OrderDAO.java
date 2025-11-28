package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import models.Order;
import models.OrderItem;
import models.OrderCombo;
import models.Product;
import models.Combo;
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
		String sql = "INSERT INTO orders (user_id, total_amount, status, discount_code, discount_amount, shipping_address) VALUES (?, ?, ?, ?, ?, ?)";
		try (Connection conn = DbConnect.getInstance().getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, order.getUserId());
			ps.setBigDecimal(2, order.getTotalAmount());
			ps.setString(3, order.getStatus());
			ps.setString(4, order.getDiscountCode());
			ps.setBigDecimal(5, order.getDiscountAmount());
			ps.setString(6, order.getShippingAddress());
			int rows = ps.executeUpdate();
			return rows > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	// Cập nhật trạng thái đơn hàng
	public boolean updateOrderStatus(int orderId, String newStatus) {
		// Validate status transitions: only pending -> completed or pending -> cancelled
		Order currentOrder = getOrderById(orderId);
		if (currentOrder == null) {
			return false;
		}

		String currentStatus = currentOrder.getStatus();
		
		// Only allow transitions from pending
		if (!"pending".equals(currentStatus)) {
			return false;
		}
		
		// Only allow transitions to completed or cancelled
		if (!"completed".equals(newStatus) && !"cancelled".equals(newStatus)) {
			return false;
		}

		String sql = "UPDATE orders SET status = ? WHERE order_id = ?";
		try (Connection conn = DbConnect.getInstance().getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, newStatus);
			ps.setInt(2, orderId);
			int rows = ps.executeUpdate();
			
			// If status changed to completed, update product stock and total sold
			if (rows > 0 && "completed".equals(newStatus)) {
				updateProductStatsOnOrderCompletion(orderId);
			}
			
			return rows > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	// Update product stock and total sold when order is completed
	private void updateProductStatsOnOrderCompletion(int orderId) {
		Order order = getOrderById(orderId);
		if (order == null) {
			return;
		}

		try (Connection conn = DbConnect.getInstance().getConnection()) {
			// Update stock and stats for each product in the order
			for (OrderItem item : order.getOrderItems()) {
				// Decrease stock quantity
				String updateStockSql = "UPDATE products SET stock_quantity = stock_quantity - ? WHERE product_id = ?";
				try (PreparedStatement ps = conn.prepareStatement(updateStockSql)) {
					ps.setInt(1, item.getQuantity());
					ps.setInt(2, item.getProductId());
					ps.executeUpdate();
				}

				// Update or insert product stats
				String updateStatsSql = "INSERT INTO product_stats (product_id, total_sold) VALUES (?, ?) " +
						"ON DUPLICATE KEY UPDATE total_sold = total_sold + ?";
				try (PreparedStatement ps = conn.prepareStatement(updateStatsSql)) {
					ps.setInt(1, item.getProductId());
					ps.setInt(2, item.getQuantity());
					ps.setInt(3, item.getQuantity());
					ps.executeUpdate();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
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
		try {
			o.setShippingAddress(rs.getString("shipping_address"));
		} catch (SQLException ignored) {
			// Cột có thể không tồn tại trên các DB chưa được migrate
		}
		return o;
	}

	// Load order items with product details
	private void loadOrderItems(Order order) {
		OrderItemDAO orderItemDAO = new OrderItemDAO();
		ProductDAO productDAO = new ProductDAO();
		OrderComboDAO orderComboDAO = new OrderComboDAO();
		ComboDAO comboDAO = new ComboDAO();
		List<OrderItem> orderItems = orderItemDAO.getOrderItems(order.getOrderId());

		// Load product details for each order item
		for (OrderItem item : orderItems) {
			Product product = productDAO.getProductById(item.getProductId());
			item.setProduct(product);
		}

		order.setOrderItems(orderItems);

		// Load combos for this order
		List<OrderCombo> orderCombos = orderComboDAO.getOrderCombosByOrderId(order.getOrderId());
		for (OrderCombo oc : orderCombos) {
			Combo combo = comboDAO.getComboById(oc.getComboId());
			oc.setCombo(combo);
		}
		order.setOrderCombos(orderCombos);
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

	// Lấy doanh thu theo ngày (cho biểu đồ)
	public Map<String, Double> getRevenueByDay(int days) throws SQLException {
		Map<String, Double> revenueMap = new java.util.LinkedHashMap<>();
		String sql = "SELECT DATE(order_date) as order_day, SUM(total_amount) as daily_revenue " +
				"FROM orders WHERE status = 'completed' " +
				"AND order_date >= DATE_SUB(CURDATE(), INTERVAL ? DAY) " +
				"GROUP BY DATE(order_date) ORDER BY order_day ASC";

		try (Connection conn = DbConnect.getInstance().getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, days);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String day = rs.getString("order_day");
				double revenue = rs.getDouble("daily_revenue");
				revenueMap.put(day, revenue);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return revenueMap;
	}

	// Lấy doanh thu theo tháng (cho biểu đồ)
	public Map<String, Double> getRevenueByMonth(int months) throws SQLException {
		Map<String, Double> revenueMap = new java.util.LinkedHashMap<>();
		String sql = "SELECT DATE_FORMAT(order_date, '%Y-%m') as order_month, " +
				"SUM(total_amount) as monthly_revenue " +
				"FROM orders WHERE status = 'completed' " +
				"AND order_date >= DATE_SUB(CURDATE(), INTERVAL ? MONTH) " +
				"GROUP BY DATE_FORMAT(order_date, '%Y-%m') ORDER BY order_month ASC";

		try (Connection conn = DbConnect.getInstance().getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, months);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String month = rs.getString("order_month");
				double revenue = rs.getDouble("monthly_revenue");
				revenueMap.put(month, revenue);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return revenueMap;
	}

	// Lấy doanh thu hôm nay
	public double getRevenueTodayAmount() throws SQLException {
		String sql = "SELECT COALESCE(SUM(total_amount), 0) as total FROM orders WHERE status = 'completed' AND DATE(order_date) = CURDATE()";
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

	// Lấy doanh thu tháng này
	public double getRevenueThisMonthAmount() throws SQLException {
		String sql = "SELECT COALESCE(SUM(total_amount), 0) as total FROM orders WHERE status = 'completed' AND YEAR(order_date) = YEAR(CURDATE()) AND MONTH(order_date) = MONTH(CURDATE())";
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

	// Lấy top khách hàng theo tổng chi tiêu
	public List<models.CustomerStats> getTopCustomers(int limit) throws SQLException {
		List<models.CustomerStats> customerStatsList = new ArrayList<>();
		String sql = "SELECT u.user_id, u.username, u.full_name, " +
				"COUNT(o.order_id) as total_orders, " +
				"SUM(o.total_amount) as total_spend, " +
				"MAX(o.order_date) as last_order_date " +
				"FROM users u " +
				"INNER JOIN orders o ON u.user_id = o.user_id " +
				"WHERE o.status = 'completed' " +
				"GROUP BY u.user_id, u.username, u.full_name " +
				"ORDER BY total_spend DESC LIMIT ?";

		try (Connection conn = DbConnect.getInstance().getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, limit);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				models.CustomerStats stats = new models.CustomerStats();
				stats.setUserId(rs.getInt("user_id"));
				stats.setUsername(rs.getString("username"));
				stats.setFullName(rs.getString("full_name"));
				stats.setTotalOrders(rs.getInt("total_orders"));
				stats.setTotalSpend(rs.getBigDecimal("total_spend"));
				stats.setLastOrderDate(rs.getTimestamp("last_order_date"));
				customerStatsList.add(stats);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return customerStatsList;
	}

	// Lấy lịch sử mua hàng của khách hàng
	public List<Order> getCustomerPurchaseHistory(int userId, int limit) throws SQLException {
		List<Order> list = new ArrayList<>();
		String sql = "SELECT o.*, u.username as user_name FROM orders o " +
				"INNER JOIN users u ON o.user_id = u.user_id " +
				"WHERE o.user_id = ? " +
				"ORDER BY o.order_date DESC LIMIT ?";
		try (Connection conn = DbConnect.getInstance().getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, userId);
			ps.setInt(2, limit);
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
