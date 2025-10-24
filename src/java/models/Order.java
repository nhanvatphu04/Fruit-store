package models;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

public class Order {
    private int orderId;
    private int userId;
    private BigDecimal totalAmount;
    private String status;
    private Timestamp orderDate;
    private String userName;  // Thêm trường userName
    private String discountCode;  // Mã giảm giá được áp dụng
    private BigDecimal discountAmount;  // Số tiền giảm
    private List<OrderItem> orderItems;  // Danh sách các mặt hàng trong đơn hàng

    // Constructors
    public Order() {}

    public Order(int userId, BigDecimal totalAmount, String status) {
        this.userId = userId;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    public Order(int userId, BigDecimal totalAmount, String status, String discountCode, BigDecimal discountAmount) {
        this.userId = userId;
        this.totalAmount = totalAmount;
        this.status = status;
        this.discountCode = discountCode;
        this.discountAmount = discountAmount;
    }
    
    // Getters and Setters
    public int getOrderId() {
        return orderId;
    }
    
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Timestamp getOrderDate() {
        return orderDate;
    }
    
    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }
    
    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
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

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", userId=" + userId +
                ", totalAmount=" + totalAmount +
                ", status='" + status + '\'' +
                ", orderDate=" + orderDate +
                ", discountCode='" + discountCode + '\'' +
                ", discountAmount=" + discountAmount +
                '}';
    }
}