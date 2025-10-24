package models;

public class ComboItem {
    private int comboItemId;
    private int comboId;
    private int productId;
    private int quantity;
    
    // Constructors
    public ComboItem() {}
    
    public ComboItem(int comboId, int productId, int quantity) {
        this.comboId = comboId;
        this.productId = productId;
        this.quantity = quantity;
    }
    
    // Getters and Setters
    public int getComboItemId() {
        return comboItemId;
    }
    
    public void setComboItemId(int comboItemId) {
        this.comboItemId = comboItemId;
    }
    
    public int getComboId() {
        return comboId;
    }
    
    public void setComboId(int comboId) {
        this.comboId = comboId;
    }
    
    public int getProductId() {
        return productId;
    }
    
    public void setProductId(int productId) {
        this.productId = productId;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    @Override
    public String toString() {
        return "ComboItem{" +
                "comboItemId=" + comboItemId +
                ", comboId=" + comboId +
                ", productId=" + productId +
                ", quantity=" + quantity +
                '}';
    }
}