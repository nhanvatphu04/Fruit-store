package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import models.OrderItem;
import utils.DbConnect;

// DAO cho chi tiết đơn hàng
public class OrderItemDAO {

    // Lấy các item theo đơn hàng
    public List<OrderItem> getOrderItems(int orderId) {
        List<OrderItem> list = new ArrayList<>();
        String sql = "SELECT * FROM order_items WHERE order_id = ?";
        try (Connection conn = DbConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                OrderItem item = new OrderItem();
                item.setOrderItemId(rs.getInt("order_item_id"));
                item.setOrderId(rs.getInt("order_id"));
                item.setProductId(rs.getInt("product_id"));
                item.setQuantity(rs.getInt("quantity"));
                item.setPrice(rs.getBigDecimal("price"));
                list.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Thêm item vào đơn hàng
    public boolean addOrderItem(OrderItem item) {
        String sql = "INSERT INTO order_items (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
        try (Connection conn = DbConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, item.getOrderId());
            ps.setInt(2, item.getProductId());
            ps.setInt(3, item.getQuantity());
            ps.setBigDecimal(4, item.getPrice());
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Xóa item khỏi đơn hàng
    public boolean deleteOrderItem(int orderItemId) {
        String sql = "DELETE FROM order_items WHERE order_item_id = ?";
        try (Connection conn = DbConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderItemId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Xóa tất cả item của một đơn hàng
    public boolean deleteOrderItems(int orderId) {
        String sql = "DELETE FROM order_items WHERE order_id = ?";
        try (Connection conn = DbConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
