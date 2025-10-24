package models;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Combo {
    private int comboId;
    private String name;
    private String description;
    private BigDecimal originalPrice;
    private BigDecimal comboPrice;
    private String imageUrl;
    private Timestamp startDate;
    private Timestamp endDate;
    private boolean isActive;
    private Timestamp createdAt;
    
    // Constructors
    public Combo() {}
    
    public Combo(String name, String description, BigDecimal originalPrice, BigDecimal comboPrice, 
                String imageUrl, Timestamp startDate, Timestamp endDate) {
        this.name = name;
        this.description = description;
        this.originalPrice = originalPrice;
        this.comboPrice = comboPrice;
        this.imageUrl = imageUrl;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isActive = true;
    }
    
    // Getters and Setters
    public int getComboId() {
        return comboId;
    }
    
    public void setComboId(int comboId) {
        this.comboId = comboId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }
    
    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }
    
    public BigDecimal getComboPrice() {
        return comboPrice;
    }
    
    public void setComboPrice(BigDecimal comboPrice) {
        this.comboPrice = comboPrice;
    }
    
    public BigDecimal getSalePrice() {
        return comboPrice;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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
    
    // Tính phần trăm giảm giá
    public int getDiscountPercentage() {
        if (originalPrice == null || comboPrice == null || originalPrice.compareTo(BigDecimal.ZERO) <= 0) {
            return 0;
        }
        BigDecimal discount = originalPrice.subtract(comboPrice);
        return discount.multiply(new BigDecimal("100"))
                      .divide(originalPrice, java.math.RoundingMode.HALF_UP)
                      .intValue();
    }
    
    @Override
    public String toString() {
        return "Combo{" +
                "comboId=" + comboId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", originalPrice=" + originalPrice +
                ", comboPrice=" + comboPrice +
                ", imageUrl='" + imageUrl + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", isActive=" + isActive +
                ", createdAt=" + createdAt +
                '}';
    }
}