package models;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Model class for customer statistics
 */
public class CustomerStats {
    private int userId;
    private String username;
    private String fullName;
    private int totalOrders;
    private BigDecimal totalSpend;
    private Timestamp lastOrderDate;
    
    // Constructors
    public CustomerStats() {}
    
    public CustomerStats(int userId, String username, int totalOrders, BigDecimal totalSpend) {
        this.userId = userId;
        this.username = username;
        this.totalOrders = totalOrders;
        this.totalSpend = totalSpend;
    }
    
    // Getters and Setters
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public int getTotalOrders() {
        return totalOrders;
    }
    
    public void setTotalOrders(int totalOrders) {
        this.totalOrders = totalOrders;
    }
    
    public BigDecimal getTotalSpend() {
        return totalSpend;
    }
    
    public void setTotalSpend(BigDecimal totalSpend) {
        this.totalSpend = totalSpend;
    }
    
    public Timestamp getLastOrderDate() {
        return lastOrderDate;
    }
    
    public void setLastOrderDate(Timestamp lastOrderDate) {
        this.lastOrderDate = lastOrderDate;
    }
    
    @Override
    public String toString() {
        return "CustomerStats{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", fullName='" + fullName + '\'' +
                ", totalOrders=" + totalOrders +
                ", totalSpend=" + totalSpend +
                ", lastOrderDate=" + lastOrderDate +
                '}';
    }
}
