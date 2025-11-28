package models;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Product {
    private int productId;
    private String name;
    private String description;
    private BigDecimal price;
    private int stockQuantity;
    private String imageUrl;
    private int categoryId;
    private int discountPercent;
    private boolean isNew;
    private boolean isBestSeller;
    private int totalSold;
    private Timestamp createdAt;
    private Category category = new Category();
    
    // Constructors
    public Product() {}
    
    public Product(String name, String description, BigDecimal price, int stockQuantity, String imageUrl, int categoryId, int discountPercent) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.imageUrl = imageUrl;
        this.categoryId = categoryId;
        this.discountPercent = discountPercent;
        this.isNew = false;
        this.isBestSeller = false;
    }
    
    // Getters and Setters
    public int getProductId() {
        return productId;
    }
    
    public void setProductId(int productId) {
        this.productId = productId;
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
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    public int getStockQuantity() {
        return stockQuantity;
    }
    
    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    public int getCategoryId() {
        return categoryId;
    }
    
    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
        this.categoryId = category.getCategoryId();
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public int getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(int discountPercent) {
        this.discountPercent = discountPercent;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public boolean isBestSeller() {
        return isBestSeller;
    }

    public void setBestSeller(boolean bestSeller) {
        isBestSeller = bestSeller;
    }

    public int getTotalSold() {
        return totalSold;
    }

    public void setTotalSold(int totalSold) {
        this.totalSold = totalSold;
    }

    // Tính giá sau giảm giá
    public BigDecimal getDiscountedPrice() {
        if (discountPercent > 0) {
            BigDecimal discountAmount = price.multiply(
                BigDecimal.valueOf(discountPercent).divide(BigDecimal.valueOf(100))
            );
            return price.subtract(discountAmount);
        }
        return price;
    }

    // Kiểm tra sản phẩm có đang giảm giá không
    public boolean isOnSale() {
        return discountPercent > 0;
    }

    // Lấy phần trăm tiết kiệm
    public BigDecimal getSavingsAmount() {
        if (discountPercent > 0) {
            return price.subtract(getDiscountedPrice());
        }
        return BigDecimal.ZERO;
    }
    
    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", stockQuantity=" + stockQuantity +
                ", imageUrl='" + imageUrl + '\'' +
                ", categoryId=" + categoryId +
                ", discountPercent=" + discountPercent +
                ", isNew=" + isNew +
                ", isBestSeller=" + isBestSeller +
                ", totalSold=" + totalSold +
                ", createdAt=" + createdAt +
                '}';
    }
}