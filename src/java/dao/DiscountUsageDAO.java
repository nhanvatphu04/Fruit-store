package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import models.DiscountUsage;
import utils.DbConnect;

/**
 * DAO class for managing discount usage records
 * Handles database operations for tracking discount code usage
 */
public class DiscountUsageDAO {
    
    /**
     * Record a discount usage
     * @param usage DiscountUsage object to save
     * @return true if successful, false otherwise
     */
    public boolean recordUsage(DiscountUsage usage) {
        String sql = "INSERT INTO discount_usage (discount_id, user_id, order_id, discount_code, discount_amount) " +
                     "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DbConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, usage.getDiscountId());
            ps.setInt(2, usage.getUserId());
            
            if (usage.getOrderId() != null) {
                ps.setInt(3, usage.getOrderId());
            } else {
                ps.setNull(3, Types.INTEGER);
            }
            
            ps.setString(4, usage.getDiscountCode());
            ps.setBigDecimal(5, usage.getDiscountAmount());
            
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Get all usage records for a specific discount code
     * @param discountCode The discount code
     * @return List of DiscountUsage records
     */
    public List<DiscountUsage> getUsageByCode(String discountCode) {
        String sql = "SELECT * FROM discount_usage WHERE discount_code = ? ORDER BY used_at DESC";
        List<DiscountUsage> usages = new ArrayList<>();
        
        try (Connection conn = DbConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, discountCode);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                usages.add(mapResultSetToUsage(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return usages;
    }
    
    /**
     * Get all usage records for a specific user
     * @param userId The user ID
     * @return List of DiscountUsage records
     */
    public List<DiscountUsage> getUsageByUser(int userId) {
        String sql = "SELECT * FROM discount_usage WHERE user_id = ? ORDER BY used_at DESC";
        List<DiscountUsage> usages = new ArrayList<>();
        
        try (Connection conn = DbConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                usages.add(mapResultSetToUsage(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return usages;
    }
    
    /**
     * Get all usage records for a specific order
     * @param orderId The order ID
     * @return List of DiscountUsage records
     */
    public List<DiscountUsage> getUsageByOrder(int orderId) {
        String sql = "SELECT * FROM discount_usage WHERE order_id = ?";
        List<DiscountUsage> usages = new ArrayList<>();
        
        try (Connection conn = DbConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                usages.add(mapResultSetToUsage(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return usages;
    }
    
    /**
     * Count total usage of a discount code
     * @param discountCode The discount code
     * @return Number of times the code has been used
     */
    public int countUsageByCode(String discountCode) {
        String sql = "SELECT COUNT(*) as count FROM discount_usage WHERE discount_code = ?";
        
        try (Connection conn = DbConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, discountCode);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    /**
     * Count usage of a discount code by a specific user
     * @param discountCode The discount code
     * @param userId The user ID
     * @return Number of times the user has used this code
     */
    public int countUsageByCodeAndUser(String discountCode, int userId) {
        String sql = "SELECT COUNT(*) as count FROM discount_usage WHERE discount_code = ? AND user_id = ?";
        
        try (Connection conn = DbConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, discountCode);
            ps.setInt(2, userId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    /**
     * Update order ID for a discount usage record
     * @param usageId The usage ID
     * @param orderId The order ID
     * @return true if successful, false otherwise
     */
    public boolean updateOrderId(int usageId, int orderId) {
        String sql = "UPDATE discount_usage SET order_id = ? WHERE usage_id = ?";
        
        try (Connection conn = DbConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ps.setInt(2, usageId);
            
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Update order ID for a discount usage record by code and user
     * @param discountCode The discount code
     * @param userId The user ID
     * @param orderId The order ID
     * @return true if successful, false otherwise
     */
    public boolean updateOrderIdByCodeAndUser(String discountCode, int userId, int orderId) {
        String sql = "UPDATE discount_usage SET order_id = ? WHERE discount_code = ? AND user_id = ? AND order_id IS NULL ORDER BY used_at DESC LIMIT 1";

        try (Connection conn = DbConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ps.setString(2, discountCode);
            ps.setInt(3, userId);

            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Delete a discount usage record
     * @param usageId The usage ID
     * @return true if successful, false otherwise
     */
    public boolean deleteUsage(int usageId) {
        String sql = "DELETE FROM discount_usage WHERE usage_id = ?";

        try (Connection conn = DbConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, usageId);

            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Helper method to map ResultSet to DiscountUsage object
     */
    private DiscountUsage mapResultSetToUsage(ResultSet rs) throws SQLException {
        DiscountUsage usage = new DiscountUsage();
        usage.setUsageId(rs.getInt("usage_id"));
        usage.setDiscountId(rs.getInt("discount_id"));
        usage.setUserId(rs.getInt("user_id"));
        
        int orderId = rs.getInt("order_id");
        usage.setOrderId(rs.wasNull() ? null : orderId);
        
        usage.setDiscountCode(rs.getString("discount_code"));
        usage.setDiscountAmount(rs.getBigDecimal("discount_amount"));
        usage.setUsedAt(rs.getTimestamp("used_at"));
        
        return usage;
    }
}

