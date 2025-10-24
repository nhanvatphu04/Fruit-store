# 💳 Luồng Hoạt Động: Thanh Toán (Checkout)

## A. Vai Trò Của Các Lớp

### 1. Model Order & OrderItem
- Order: Đại diện cho đơn hàng (orderId, userId, totalAmount, status, orderDate, ...)
- OrderItem: Đại diện cho chi tiết đơn hàng (orderItemId, orderId, productId, quantity, price)

### 2. CheckoutController
**Nhiệm vụ:**
- Tiếp nhận yêu cầu từ người dùng (xem trang checkout, xử lý thanh toán)
- Xác thực: Kiểm tra người dùng đã đăng nhập, có sản phẩm được chọn
- Lấy dữ liệu từ session (giỏ hàng, mã giảm giá)
- Gọi OrderService, CartService, DiscountService để xử lý
- Trả về phản hồi: Hiển thị trang checkout hoặc điều hướng

### 3. OrderService
**Nhiệm vụ:**
- Nhận yêu cầu từ Controller
- Xử lý logic nghiệp vụ:
  - Kiểm tra sản phẩm được chọn
  - Kiểm tra tồn kho
  - Tính toán tổng tiền
  - Xác thực mã giảm giá
  - Tạo đơn hàng và chi tiết đơn hàng
- Điều phối OrderDAO, OrderItemDAO, ProductDAO, DiscountUsageDAO

### 4. OrderDAO & OrderItemDAO
**Nhiệm vụ:**
- Nhận yêu cầu từ Service
- Thực thi SQL: addOrder(), addOrderItem(), getOrderById(), ...
- Chuyển đổi ResultSet thành đối tượng Order/OrderItem

---

## B. Luồng Hoạt Động

### 1️⃣ Luồng Xem Trang Checkout (GET)

```
1. Người dùng nhấn nút "Thanh toán" trên trang giỏ hàng
   ↓
2. Request được gửi đến CheckoutController (/checkout)
   ↓
3. CheckoutController.doGet() kiểm tra:
   - Người dùng đã đăng nhập chưa?
   ↓
4. CheckoutController lấy dữ liệu từ session:
   - user (thông tin người dùng)
   - cartItems (sản phẩm trong giỏ)
   - cartCombos (combo trong giỏ)
   - appliedDiscount (mã giảm giá)
   - cartDiscount (số tiền giảm)
   ↓
5. CheckoutController lọc chỉ lấy sản phẩm được chọn (selected = true)
   ↓
6. CheckoutController kiểm tra:
   - Có sản phẩm được chọn không?
   - Nếu không → điều hướng về /cart
   ↓
7. CheckoutController tính toán:
   - Subtotal = Σ(quantity * price) cho sản phẩm được chọn
   - Total = Subtotal - discount
   ↓
8. CheckoutController gửi dữ liệu đến checkout.jsp:
   - Thông tin người dùng
   - Danh sách sản phẩm được chọn
   - Tổng tiền, tiền giảm, tổng thanh toán
   ↓
9. checkout.jsp hiển thị:
   - Xác nhận thông tin giao hàng
   - Danh sách sản phẩm
   - Tóm tắt đơn hàng
   - Nút "Xác nhận thanh toán"
```

### 2️⃣ Luồng Xử Lý Thanh Toán (POST)

```
1. Người dùng xem lại thông tin và nhấn "Xác nhận thanh toán"
   ↓
2. Request được gửi đến CheckoutController (/checkout) - POST
   ↓
3. CheckoutController.doPost() kiểm tra:
   - Người dùng đã đăng nhập chưa?
   ↓
4. CheckoutController lấy dữ liệu từ session:
   - cartItems, cartCombos, appliedDiscount, cartDiscount
   ↓
5. CheckoutController lọc sản phẩm được chọn
   ↓
6. CheckoutController kiểm tra:
   - Có sản phẩm được chọn không?
   ↓
7. CheckoutController tính toán:
   - Subtotal = Σ(quantity * price)
   - Total = Subtotal - discount
   ↓
8. CheckoutController tạo đối tượng Order:
   Order order = new Order(
     userId, 
     total, 
     "pending",  // Status mặc định
     appliedDiscountCode,
     discountAmount
   )
   ↓
9. CheckoutController gọi OrderDAO.addOrder(order)
   ↓
10. OrderDAO thực thi SQL INSERT vào bảng orders
    ↓
11. CheckoutController lấy orderId vừa tạo
    ↓
12. Với mỗi sản phẩm được chọn:
    - Tạo đối tượng OrderItem
    - Gọi OrderItemDAO.addOrderItem(orderItem)
    - OrderItemDAO thực thi SQL INSERT vào bảng order_items
    ↓
13. Với mỗi sản phẩm:
    - Cập nhật tồn kho: stock_quantity -= quantity
    - Gọi ProductDAO.updateProduct()
    ↓
14. Nếu có mã giảm giá:
    - Gọi DiscountUsageDAO.updateOrderIdByCodeAndUser()
    - Liên kết discount_usage với order vừa tạo
    ↓
15. Xóa giỏ hàng:
    - Gọi CartDAO.clearCart(userId)
    - Gọi CartComboDAO.clearCartCombo(userId)
    ↓
16. Xóa dữ liệu từ session:
    - session.removeAttribute("appliedDiscount")
    - session.removeAttribute("cartDiscount")
    ↓
17. CheckoutController điều hướng đến /orders
    ↓
18. Người dùng xem danh sách đơn hàng với đơn hàng mới vừa tạo
```

---

## C. Các Endpoint

| Endpoint | Method | Mô Tả |
|----------|--------|-------|
| `/checkout` | GET | Hiển thị trang xác nhận thanh toán |
| `/checkout` | POST | Xử lý thanh toán, tạo đơn hàng |

---

## D. Dữ Liệu Lưu Trong Database

**Bảng orders:**
```sql
order_id | user_id | total_amount | status | order_date | discount_code | discount_amount
```

**Bảng order_items:**
```sql
order_item_id | order_id | product_id | quantity | price
```

---

## E. Trạng Thái Đơn Hàng

| Status | Mô Tả |
|--------|-------|
| pending | Đơn hàng vừa tạo, chờ xử lý |
| completed | Đơn hàng đã hoàn thành |
| cancelled | Đơn hàng đã bị hủy |

---

## F. Xác Thực Dữ Liệu

### Kiểm Tra Tồn Kho
```
Nếu quantity > stock_quantity → Lỗi
```

### Kiểm Tra Mã Giảm Giá
```
- Mã có tồn tại không?
- Mã có còn hiệu lực không?
- Tổng tiền có đạt minimum không?
```

### Kiểm Tra Sản Phẩm Được Chọn
```
- Phải có ít nhất 1 sản phẩm được chọn
- Sản phẩm phải tồn tại trong database
```

---

## G. Tính Năng Đặc Biệt

### Áp Dụng Mã Giảm Giá
- Mã giảm giá được lưu trong session trước khi checkout
- Khi checkout, mã giảm giá được liên kết với đơn hàng
- Cập nhật discount_usage để ghi nhận sử dụng mã

### Cập Nhật Tồn Kho
- Sau khi tạo đơn hàng, tồn kho sản phẩm được giảm
- Nếu tồn kho không đủ, checkout thất bại

### Xóa Giỏ Hàng
- Sau khi checkout thành công, giỏ hàng được xóa
- Người dùng có thể tiếp tục mua sắm

