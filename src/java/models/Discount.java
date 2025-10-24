package models;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Discount {
    private int discountId;
    private String code;
    private String description;
    private String discountType; // 'percentage' or 'fixed_amount'
    private BigDecimal discountValue;
    private BigDecimal minOrderAmount;
    private BigDecimal maxDiscountAmount;
    private Timestamp startDate;
    private Timestamp endDate;
    private int usageLimit;
    private int usedCount;
    private boolean isActive;
    private Timestamp createdAt;
    
    // Constructors
    public Discount() {}
    
    public Discount(String code, String description, String discountType, BigDecimal discountValue, 
                   BigDecimal minOrderAmount, BigDecimal maxDiscountAmount, Timestamp startDate, 
                   Timestamp endDate, int usageLimit) {
        this.code = code;
        this.description = description;
        this.discountType = discountType;
        this.discountValue = discountValue;
        this.minOrderAmount = minOrderAmount;
        this.maxDiscountAmount = maxDiscountAmount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.usageLimit = usageLimit;
        this.usedCount = 0;
        this.isActive = true;
    }
    
    // Getters and Setters
    public int getDiscountId() {
        return discountId;
    }
    
    public void setDiscountId(int discountId) {
        this.discountId = discountId;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getDiscountType() {
        return discountType;
    }
    
    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }
    
    public BigDecimal getDiscountValue() {
        return discountValue;
    }
    
    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue;
    }
    
    public BigDecimal getMinOrderAmount() {
        return minOrderAmount;
    }
    
    public void setMinOrderAmount(BigDecimal minOrderAmount) {
        this.minOrderAmount = minOrderAmount;
    }
    
    public BigDecimal getMaxDiscountAmount() {
        return maxDiscountAmount;
    }
    
    public void setMaxDiscountAmount(BigDecimal maxDiscountAmount) {
        this.maxDiscountAmount = maxDiscountAmount;
    }
    
    public Timestamp getStartDate() {
        return startDate;
    }
    
    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }
    
    public Timestamp getEndDate() {
        return endDate;
    }
    
    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }
    
    public int getUsageLimit() {
        return usageLimit;
    }
    
    public void setUsageLimit(int usageLimit) {
        this.usageLimit = usageLimit;
    }
    
    public int getUsedCount() {
        return usedCount;
    }
    
    public void setUsedCount(int usedCount) {
        this.usedCount = usedCount;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean active) {
        isActive = active;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    @Override
    public String toString() {
        return "Discount{" +
                "discountId=" + discountId +
                ", code='" + code + '\'' +
                ", description='" + description + '\'' +
                ", discountType='" + discountType + '\'' +
                ", discountValue=" + discountValue +
                ", minOrderAmount=" + minOrderAmount +
                ", maxDiscountAmount=" + maxDiscountAmount +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", usageLimit=" + usageLimit +
                ", usedCount=" + usedCount +
                ", isActive=" + isActive +
                ", createdAt=" + createdAt +
                '}';
    }
}