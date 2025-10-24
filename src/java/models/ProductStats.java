package models;

import java.sql.Timestamp;

public class ProductStats {
    private int productId;
    private int totalSold;
    private Timestamp lastSoldAt;
    
    // Constructors
    public ProductStats() {}
    
    public ProductStats(int productId, int totalSold, Timestamp lastSoldAt) {
        this.productId = productId;
        this.totalSold = totalSold;
        this.lastSoldAt = lastSoldAt;
    }
    
    // Getters and Setters
    public int getProductId() {
        return productId;
    }
    
    public void setProductId(int productId) {
        this.productId = productId;
    }
    
    public int getTotalSold() {
        return totalSold;
    }
    
    public void setTotalSold(int totalSold) {
        this.totalSold = totalSold;
    }
    
    public Timestamp getLastSoldAt() {
        return lastSoldAt;
    }
    
    public void setLastSoldAt(Timestamp lastSoldAt) {
        this.lastSoldAt = lastSoldAt;
    }
    
    @Override
    public String toString() {
        return "ProductStats{" +
                "productId=" + productId +
                ", totalSold=" + totalSold +
                ", lastSoldAt=" + lastSoldAt +
                '}';
    }
}
