package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import models.ProductStats;
import utils.DbConnect;

// DAO cho thống kê sản phẩm
public class ProductStatsDAO {

    // Lấy thống kê theo product_id
    public ProductStats getProductStatsById(int productId) {
        String sql = "SELECT * FROM product_stats WHERE product_id = ?";
        try (Connection conn = DbConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToProductStats(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Lấy tất cả thống kê
    public List<ProductStats> getAllProductStats() {
        List<ProductStats> list = new ArrayList<>();
        String sql = "SELECT * FROM product_stats";
        try (Connection conn = DbConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToProductStats(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Thêm thống kê mới
    public boolean addProductStats(ProductStats productStats) {
        String sql = "INSERT INTO product_stats (product_id, total_sold, last_sold_at) VALUES (?, ?, ?)";
        try (Connection conn = DbConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productStats.getProductId());
            ps.setInt(2, productStats.getTotalSold());
            ps.setTimestamp(3, productStats.getLastSoldAt());
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Cập nhật thống kê
    public boolean updateProductStats(ProductStats productStats) {
        String sql = "UPDATE product_stats SET total_sold=?, last_sold_at=? WHERE product_id=?";
        try (Connection conn = DbConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productStats.getTotalSold());
            ps.setTimestamp(2, productStats.getLastSoldAt());
            ps.setInt(3, productStats.getProductId());
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Cập nhật số lượng đã bán
    public boolean updateTotalSold(int productId, int additionalSold) {
        String sql = "UPDATE product_stats SET total_sold = total_sold + ?, last_sold_at = NOW() WHERE product_id = ?";
        try (Connection conn = DbConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, additionalSold);
            ps.setInt(2, productId);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Xóa thống kê
    public boolean deleteProductStats(int productId) {
        String sql = "DELETE FROM product_stats WHERE product_id = ?";
        try (Connection conn = DbConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Lấy sản phẩm bán chạy
    public List<ProductStats> getTopSellingProducts(int limit) throws SQLException {
        List<ProductStats> list = new ArrayList<>();
        String sql = "SELECT * FROM product_stats ORDER BY total_sold DESC LIMIT ?";
        try (Connection conn = DbConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToProductStats(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Hàm tiện ích chuyển ResultSet sang ProductStats
    private ProductStats mapResultSetToProductStats(ResultSet rs) throws SQLException {
        ProductStats ps = new ProductStats();
        ps.setProductId(rs.getInt("product_id"));
        ps.setTotalSold(rs.getInt("total_sold"));
        ps.setLastSoldAt(rs.getTimestamp("last_sold_at"));
        return ps;
    }
}
