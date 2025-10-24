package models;

import java.sql.Timestamp;

public class Category {
    private int categoryId;
    private String name;
    private String description;
    private String imageUrl;
    private String icon;
    private String slug;
    private Timestamp createdAt;
    
    // Constructors
    public Category() {}
    
    public Category(String name, String description, String imageUrl, String icon, String slug) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.icon = icon;
        this.slug = slug;
    }
    
    // Getters and Setters
    public int getCategoryId() {
        return categoryId;
    }
    
    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
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
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    public String getIcon() {
        return icon;
    }
    
    public void setIcon(String icon) {
        this.icon = icon;
    }
    
    public String getSlug() {
        return slug;
    }
    
    public void setSlug(String slug) {
        this.slug = slug;
    }
    
    @Override
    public String toString() {
        return "Category{" +
                "categoryId=" + categoryId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", icon='" + icon + '\'' +
                ", slug='" + slug + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}