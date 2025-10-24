package models;

import java.math.BigDecimal;

public class CartCombo {
    private int cartComboId;
    private int userId;
    private int comboId;
    private int quantity;
    private boolean selected;
    private Combo combo; // Reference to the combo
    
    // Constructors
    public CartCombo() {}
    
    public CartCombo(int userId, int comboId, int quantity) {
        this.userId = userId;
        this.comboId = comboId;
        this.quantity = quantity;
        this.selected = true; // Default to selected
    }
    
    // Getters and Setters
    public int getCartComboId() {
        return cartComboId;
    }
    
    public void setCartComboId(int cartComboId) {
        this.cartComboId = cartComboId;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public int getComboId() {
        return comboId;
    }
    
    public void setComboId(int comboId) {
        this.comboId = comboId;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Combo getCombo() {
        return combo;
    }

    public void setCombo(Combo combo) {
        this.combo = combo;
    }
    
    // Tính tổng tiền cho combo
    public BigDecimal getTotal() {
        if (combo != null) {
            return combo.getComboPrice().multiply(BigDecimal.valueOf(quantity));
        }
        return BigDecimal.ZERO;
    }
    
    @Override
    public String toString() {
        return "CartCombo{" +
                "cartComboId=" + cartComboId +
                ", userId=" + userId +
                ", comboId=" + comboId +
                ", quantity=" + quantity +
                ", selected=" + selected +
                '}';
    }
}
