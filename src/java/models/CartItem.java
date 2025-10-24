package models;

import java.math.BigDecimal;

public class CartItem {
    private int cartId;
    private int userId;
    private int productId;
    private int quantity;
    private boolean selected;
    private Product product; // Reference to the product
    
    // Constructors
    public CartItem() {}
    
    public CartItem(int userId, int productId, int quantity) {
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
        this.selected = true; // Default to selected
    }
    
    // Getters and Setters
    public int getCartId() {
        return cartId;
    }
    
    public void setCartId(int cartId) {
        this.cartId = cartId;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
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

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
    
    // Nhận giá chiết khấu cho mỗi đơn vị
    public BigDecimal getDiscountedPrice() {
        if (product != null) {
            BigDecimal price = product.getPrice();
            
            // Áp dụng giảm giá sản phẩm nếu có
            if (product.getDiscountPercent() > 0) {
                BigDecimal discount = price.multiply(
                    new BigDecimal(product.getDiscountPercent())
                        .divide(new BigDecimal(100))
                );
                price = price.subtract(discount);
            }
            
            return price;
        }
        return BigDecimal.ZERO;
    }
    
    // Tính tổng số tiền cho mặt hàng trong giỏ hàng này
    public BigDecimal getTotal() {
        if (product != null) {
            return getDiscountedPrice().multiply(BigDecimal.valueOf(quantity));
        }
        return BigDecimal.ZERO;
    }
    
    @Override
    public String toString() {
        return "CartItem{" +
                "cartId=" + cartId +
                ", userId=" + userId +
                ", productId=" + productId +
                ", quantity=" + quantity +
                ", selected=" + selected +
                '}';
    }
}