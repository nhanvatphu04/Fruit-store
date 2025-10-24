package models;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Model class representing a discount usage record
 * Tracks each time a discount code is used by a user in an order
 */
public class DiscountUsage {
    private int usageId;
    private int discountId;
    private int userId;
    private Integer orderId;  // Nullable - discount may be used but order not completed
    private String discountCode;
    private BigDecimal discountAmount;
    private Timestamp usedAt;
    
    // Constructors
    public DiscountUsage() {}
    
    /**
     * Constructor for creating a new discount usage record
     * @param discountId ID of the discount
     * @param userId ID of the user using the discount
     * @param discountCode The discount code
     * @param discountAmount The discount amount applied
     */
    public DiscountUsage(int discountId, int userId, String discountCode, BigDecimal discountAmount) {
        this.discountId = discountId;
        this.userId = userId;
        this.discountCode = discountCode;
        this.discountAmount = discountAmount;
        this.orderId = null;
    }
    
    /**
     * Constructor for creating a discount usage record with order
     * @param discountId ID of the discount
     * @param userId ID of the user using the discount
     * @param orderId ID of the order (can be null)
     * @param discountCode The discount code
     * @param discountAmount The discount amount applied
     */
    public DiscountUsage(int discountId, int userId, Integer orderId, String discountCode, BigDecimal discountAmount) {
        this.discountId = discountId;
        this.userId = userId;
        this.orderId = orderId;
        this.discountCode = discountCode;
        this.discountAmount = discountAmount;
    }
    
    // Getters and Setters
    public int getUsageId() {
        return usageId;
    }
    
    public void setUsageId(int usageId) {
        this.usageId = usageId;
    }
    
    public int getDiscountId() {
        return discountId;
    }
    
    public void setDiscountId(int discountId) {
        this.discountId = discountId;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public Integer getOrderId() {
        return orderId;
    }
    
    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }
    
    public String getDiscountCode() {
        return discountCode;
    }
    
    public void setDiscountCode(String discountCode) {
        this.discountCode = discountCode;
    }
    
    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }
    
    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }
    
    public Timestamp getUsedAt() {
        return usedAt;
    }
    
    public void setUsedAt(Timestamp usedAt) {
        this.usedAt = usedAt;
    }
    
    @Override
    public String toString() {
        return "DiscountUsage{" +
                "usageId=" + usageId +
                ", discountId=" + discountId +
                ", userId=" + userId +
                ", orderId=" + orderId +
                ", discountCode='" + discountCode + '\'' +
                ", discountAmount=" + discountAmount +
                ", usedAt=" + usedAt +
                '}';
    }
}

