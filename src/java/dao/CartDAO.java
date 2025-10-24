package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import models.CartItem;
import utils.DbConnect;

// DAO cho giỏ hàng
public class CartDAO {

    // Lấy giỏ hàng theo user
    public List<CartItem> getCartByUserId(int userId) {
        List<CartItem> cartList = new ArrayList<>();
        String sql = "SELECT * FROM cart WHERE user_id = ?";
        try (Connection conn = DbConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                CartItem cart = new CartItem();
                cart.setCartId(rs.getInt("cart_id"));
                cart.setUserId(rs.getInt("user_id"));
                cart.setProductId(rs.getInt("product_id"));
                cart.setQuantity(rs.getInt("quantity"));
                cart.setSelected(rs.getBoolean("selected"));
                cartList.add(cart);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cartList;
    }

    // Thêm sản phẩm vào giỏ
    public boolean addToCart(int userId, int productId, int quantity) {
        // Kiểm tra xem sản phẩm đã có trong giỏ chưa
        String checkSql = "SELECT cart_id, quantity FROM cart WHERE user_id = ? AND product_id = ?";
        String insertSql = "INSERT INTO cart (user_id, product_id, quantity, selected) VALUES (?, ?, ?, true)";
        String updateSql = "UPDATE cart SET quantity = ? WHERE cart_id = ?";
        
        try (Connection conn = DbConnect.getInstance().getConnection();
             PreparedStatement checkPs = conn.prepareStatement(checkSql);
             PreparedStatement insertPs = conn.prepareStatement(insertSql);
             PreparedStatement updatePs = conn.prepareStatement(updateSql)) {
            
            // Kiểm tra sản phẩm trong giỏ
            checkPs.setInt(1, userId);
            checkPs.setInt(2, productId);
            ResultSet rs = checkPs.executeQuery();
            
            if (rs.next()) {
                // Nếu đã có, cập nhật số lượng
                int cartId = rs.getInt("cart_id");
                int currentQty = rs.getInt("quantity");
                updatePs.setInt(1, currentQty + quantity);
                updatePs.setInt(2, cartId);
                return updatePs.executeUpdate() > 0;
            } else {
                // Nếu chưa có, thêm mới
                insertPs.setInt(1, userId);
                insertPs.setInt(2, productId);
                insertPs.setInt(3, quantity);
                return insertPs.executeUpdate() > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Cập nhật số lượng sản phẩm trong giỏ
    public boolean updateCartItem(int cartItemId, int quantity) {
        String sql = "UPDATE cart SET quantity = ? WHERE cart_id = ?";
        try (Connection conn = DbConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setInt(2, cartItemId);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Cập nhật trạng thái chọn của sản phẩm
    public boolean updateCartItemSelection(int cartItemId, boolean selected) {
        String sql = "UPDATE cart SET selected = ? WHERE cart_id = ?";
        try (Connection conn = DbConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, selected);
            ps.setInt(2, cartItemId);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Lấy chi tiết giỏ hàng theo cart_id
    public CartItem getCartItemById(int cartId) {
        String sql = "SELECT * FROM cart WHERE cart_id = ?";
        try (Connection conn = DbConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cartId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                CartItem cart = new CartItem();
                cart.setCartId(rs.getInt("cart_id"));
                cart.setUserId(rs.getInt("user_id"));
                cart.setProductId(rs.getInt("product_id"));
                cart.setQuantity(rs.getInt("quantity"));
                cart.setSelected(rs.getBoolean("selected"));
                return cart;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Xoá sản phẩm khỏi giỏ
    public boolean removeCartItem(int cartItemId) {
        String sql = "DELETE FROM cart WHERE cart_id = ?";
        try (Connection conn = DbConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cartItemId);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Xoá toàn bộ giỏ hàng của user
    public boolean clearCart(int userId) {
        String sql = "DELETE FROM cart WHERE user_id = ?";
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
    
    // Lấy số lượng sản phẩm trong giỏ hàng
    public int getCartCount(int userId) {
        String sql = "SELECT COUNT(*) FROM cart WHERE user_id = ?";
        try (Connection conn = DbConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
