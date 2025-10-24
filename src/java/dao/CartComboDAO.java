package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import models.CartCombo;
import utils.DbConnect;

// DAO cho giỏ hàng combo
public class CartComboDAO {

    // Lấy giỏ hàng combo theo user
    public List<CartCombo> getCartCombosByUserId(int userId) {
        List<CartCombo> cartComboList = new ArrayList<>();
        String sql = "SELECT * FROM cart_combos WHERE user_id = ?";
        try (Connection conn = DbConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                CartCombo cartCombo = new CartCombo();
                cartCombo.setCartComboId(rs.getInt("cart_combo_id"));
                cartCombo.setUserId(rs.getInt("user_id"));
                cartCombo.setComboId(rs.getInt("combo_id"));
                cartCombo.setQuantity(rs.getInt("quantity"));
                cartCombo.setSelected(rs.getBoolean("selected"));
                cartComboList.add(cartCombo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cartComboList;
    }

    // Thêm combo vào giỏ
    public boolean addComboToCart(int userId, int comboId, int quantity) {
        Connection conn = null;
        PreparedStatement checkPs = null;
        PreparedStatement insertPs = null;
        PreparedStatement updatePs = null;
        ResultSet rs = null;
        
        try {
            conn = DbConnect.getInstance().getConnection();
            conn.setAutoCommit(false);
            
            // Kiểm tra xem combo có tồn tại trong giỏ hàng không
            String checkSql = "SELECT cart_combo_id, quantity FROM cart_combos WHERE user_id = ? AND combo_id = ? FOR UPDATE";
            checkPs = conn.prepareStatement(checkSql);
            checkPs.setInt(1, userId);
            checkPs.setInt(2, comboId);
            rs = checkPs.executeQuery();
            
            boolean success = false;
            if (rs.next()) {
                // Cập nhật số lượng combo hiện có
                int cartComboId = rs.getInt("cart_combo_id");
                int currentQty = rs.getInt("quantity");
                String updateSql = "UPDATE cart_combos SET quantity = ? WHERE cart_combo_id = ?";
                updatePs = conn.prepareStatement(updateSql);
                updatePs.setInt(1, currentQty + quantity);
                updatePs.setInt(2, cartComboId);
                success = updatePs.executeUpdate() > 0;
            } else {
                // Thêm combo mới vào giỏ
                String insertSql = "INSERT INTO cart_combos (user_id, combo_id, quantity, selected) VALUES (?, ?, ?, true)";
                insertPs = conn.prepareStatement(insertSql);
                insertPs.setInt(1, userId);
                insertPs.setInt(2, comboId);
                insertPs.setInt(3, quantity);
                success = insertPs.executeUpdate() > 0;
            }
            
            if (success) {
                conn.commit();
                return true;
            } else {
                conn.rollback();
                return false;
            }
        } catch (Exception e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (rs != null) rs.close();
                if (checkPs != null) checkPs.close();
                if (insertPs != null) insertPs.close();
                if (updatePs != null) updatePs.close();
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Cập nhật số lượng combo trong giỏ
    public boolean updateCartComboItem(int cartComboId, int quantity) {
        String sql = "UPDATE cart_combos SET quantity = ? WHERE cart_combo_id = ?";
        try (Connection conn = DbConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setInt(2, cartComboId);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Cập nhật trạng thái chọn của combo
    public boolean updateCartComboSelection(int cartComboId, boolean selected) {
        String sql = "UPDATE cart_combos SET selected = ? WHERE cart_combo_id = ?";
        try (Connection conn = DbConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, selected);
            ps.setInt(2, cartComboId);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Lấy chi tiết combo trong giỏ theo cart_combo_id
    public CartCombo getCartComboById(int cartComboId) {
        String sql = "SELECT * FROM cart_combos WHERE cart_combo_id = ?";
        try (Connection conn = DbConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cartComboId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                CartCombo cartCombo = new CartCombo();
                cartCombo.setCartComboId(rs.getInt("cart_combo_id"));
                cartCombo.setUserId(rs.getInt("user_id"));
                cartCombo.setComboId(rs.getInt("combo_id"));
                cartCombo.setQuantity(rs.getInt("quantity"));
                cartCombo.setSelected(rs.getBoolean("selected"));
                return cartCombo;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Xóa combo khỏi giỏ
    public boolean removeCartComboItem(int cartComboId) {
        String sql = "DELETE FROM cart_combos WHERE cart_combo_id = ?";
        try (Connection conn = DbConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cartComboId);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Xóa toàn bộ combo trong giỏ của user
    public boolean clearCartCombos(int userId) {
        String sql = "DELETE FROM cart_combos WHERE user_id = ?";
        try (Connection conn = DbConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
