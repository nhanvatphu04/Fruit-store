package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import models.OrderCombo;
import utils.DbConnect;

public class OrderComboDAO {

    public boolean addOrderCombo(OrderCombo orderCombo) {
        String sql = "INSERT INTO order_combos (order_id, combo_id, quantity, price) VALUES (?, ?, ?, ?)";
        try (Connection conn = DbConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderCombo.getOrderId());
            ps.setInt(2, orderCombo.getComboId());
            ps.setInt(3, orderCombo.getQuantity());
            ps.setBigDecimal(4, orderCombo.getPrice());
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<OrderCombo> getOrderCombosByOrderId(int orderId) {
        List<OrderCombo> list = new ArrayList<>();
        String sql = "SELECT * FROM order_combos WHERE order_id = ?";
        try (Connection conn = DbConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                OrderCombo oc = new OrderCombo();
                oc.setOrderComboId(rs.getInt("order_combo_id"));
                oc.setOrderId(rs.getInt("order_id"));
                oc.setComboId(rs.getInt("combo_id"));
                oc.setQuantity(rs.getInt("quantity"));
                oc.setPrice(rs.getBigDecimal("price"));
                list.add(oc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}

