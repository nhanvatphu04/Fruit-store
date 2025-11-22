package models;

import java.math.BigDecimal;

public class OrderCombo {
    private int orderComboId;
    private int orderId;
    private int comboId;
    private int quantity;
    private BigDecimal price;
    private transient Combo combo;

    public int getOrderComboId() {
        return orderComboId;
    }

    public void setOrderComboId(int orderComboId) {
        this.orderComboId = orderComboId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Combo getCombo() {
        return combo;
    }

    public void setCombo(Combo combo) {
        this.combo = combo;
    }
}

