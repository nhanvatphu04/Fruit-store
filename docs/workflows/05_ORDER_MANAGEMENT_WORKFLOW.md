# 📋 Luồng Hoạt Động: Quản Lý Đơn Hàng (Order Management)

## A. Vai Trò Của Các Lớp

### 1. Model Order & OrderItem
- Order: Đại diện cho đơn hàng (orderId, userId, totalAmount, status, orderDate, discountCode, discountAmount, ...)
- OrderItem: Đại diện cho chi tiết đơn hàng (orderItemId, orderId, productId, quantity, price, ...)

### 2. OrderController (Customer)
**Nhiệm vụ:**
- Tiếp nhận yêu cầu từ khách hàng (xem danh sách đơn hàng)
- Xác thực: Kiểm tra người dùng đã đăng nhập
- Gọi OrderService để lấy dữ liệu
- Trả về phản hồi: Hiển thị danh sách đơn hàng

### 3. AdminOrderController (Admin)
**Nhiệm vụ:**
- Tiếp nhận yêu cầu từ admin (xem, cập nhật trạng thái đơn hàng)
- Xác thực quyền admin
- Lấy dữ liệu từ request
- Gọi OrderService để xử lý
- Trả về JSON response

### 4. OrderService
**Nhiệm vụ:**
- Nhận yêu cầu từ Controller
- Xử lý logic nghiệp vụ:
  - Kiểm tra trạng thái hợp lệ
  - Tính toán thống kê đơn hàng
- Điều phối OrderDAO, OrderItemDAO

### 5. OrderDAO & OrderItemDAO
**Nhiệm vụ:**
- Nhận yêu cầu từ Service
- Thực thi SQL: getOrdersByUserId(), getOrderById(), updateOrderStatus(), countOrdersByStatus(), ...
- Chuyển đổi ResultSet thành đối tượng Order/OrderItem

---

## B. Luồng Hoạt Động

### 1️⃣ Luồng Xem Danh Sách Đơn Hàng (Customer)

```
1. Khách hàng truy cập trang đơn hàng (/orders)
   ↓
2. OrderController.doGet() kiểm tra:
   - Người dùng đã đăng nhập chưa?
   ↓
3. OrderController gọi OrderDAO.getOrdersByUserId(userId)
   ↓
4. OrderDAO thực thi SQL SELECT từ bảng orders
   ↓
5. OrderDAO chuyển đổi ResultSet thành danh sách Order
   ↓
6. Với mỗi Order:
   - OrderDAO gọi loadOrderItems(order)
   - Lấy danh sách OrderItem từ bảng order_items
   - Gán vào order.orderItems
   ↓
7. OrderController gửi dữ liệu đến orders.jsp
   ↓
8. orders.jsp hiển thị:
   - Danh sách đơn hàng của khách hàng
   - Trạng thái, ngày tạo, tổng tiền
   - Chi tiết sản phẩm trong mỗi đơn hàng
   - Nút xem chi tiết
```

### 2️⃣ Luồng Xem Chi Tiết Đơn Hàng (Customer)

```
1. Khách hàng nhấn vào đơn hàng
   ↓
2. Request được gửi đến OrderController (/orders?id=123)
   ↓
3. OrderController kiểm tra:
   - orderId có hợp lệ không?
   - Đơn hàng có thuộc về người dùng không?
   ↓
4. OrderController gọi OrderDAO.getOrderById(orderId)
   ↓
5. OrderDAO thực thi SQL SELECT từ bảng orders
   ↓
6. OrderDAO gọi loadOrderItems(order)
   ↓
7. OrderController gửi dữ liệu đến order-detail.jsp
   ↓
8. order-detail.jsp hiển thị:
   - Thông tin đơn hàng
   - Danh sách sản phẩm chi tiết
   - Tổng tiền, tiền giảm, tổng thanh toán
   - Trạng thái đơn hàng
```

### 3️⃣ Luồng Xem Danh Sách Đơn Hàng (Admin)

```
1. Admin truy cập trang quản lý đơn hàng (/admin/orders)
   ↓
2. AdminOrderController.doGet() kiểm tra:
   - Người dùng là admin không?
   ↓
3. AdminOrderController gọi OrderDAO.getAllOrders()
   ↓
4. OrderDAO thực thi SQL SELECT tất cả đơn hàng
   ↓
5. OrderDAO chuyển đổi ResultSet thành danh sách Order
   ↓
6. Với mỗi Order:
   - OrderDAO gọi loadOrderItems(order)
   ↓
7. AdminOrderController tính toán thống kê:
   - Đếm đơn hàng theo trạng thái (pending, completed, cancelled)
   - Gọi OrderDAO.countOrdersByStatus(status)
   ↓
8. AdminOrderController gửi dữ liệu đến admin/orders.jsp:
   - Danh sách tất cả đơn hàng
   - Thống kê theo trạng thái
   ↓
9. admin/orders.jsp hiển thị:
   - Bộ lọc theo trạng thái
   - Bảng danh sách đơn hàng
   - Nút cập nhật trạng thái, xem chi tiết
```

### 4️⃣ Luồng Cập Nhật Trạng Thái Đơn Hàng (Admin)

```
1. Admin nhấn nút "Cập nhật trạng thái" trên đơn hàng
   ↓
2. Hiển thị dialog chọn trạng thái mới
   ↓
3. Admin chọn trạng thái (pending, completed, cancelled)
   ↓
4. Admin nhấn "Cập nhật"
   ↓
5. Request được gửi đến AdminOrderController (/admin/orders/update-status) - POST
   ↓
6. AdminOrderController kiểm tra:
   - Người dùng là admin không?
   - orderId có hợp lệ không?
   - status có hợp lệ không?
   ↓
7. AdminOrderController gọi isValidStatus(status)
   - Kiểm tra status có trong danh sách hợp lệ không
   - Hợp lệ: pending, completed, cancelled
   ↓
8. AdminOrderController gọi OrderDAO.updateOrderStatus(orderId, status)
   ↓
9. OrderDAO thực thi SQL UPDATE bảng orders
   ↓
10. AdminOrderController trả về JSON response:
    {
      "success": true,
      "message": "Cập nhật trạng thái thành công"
    }
```

### 5️⃣ Luồng Lọc Đơn Hàng Theo Trạng Thái (Admin)

```
1. Admin nhấn nút lọc (pending, completed, cancelled)
   ↓
2. Request được gửi đến AdminOrderController (/admin/orders/by-status) - POST
   ↓
3. AdminOrderController lấy tham số:
   - status (trạng thái cần lọc)
   ↓
4. AdminOrderController gọi OrderDAO.getOrdersByStatus(status)
   ↓
5. OrderDAO thực thi SQL SELECT với điều kiện status
   ↓
6. OrderDAO chuyển đổi ResultSet thành danh sách Order
   ↓
7. Với mỗi Order:
   - OrderDAO gọi loadOrderItems(order)
   ↓
8. AdminOrderController trả về JSON response:
    {
      "success": true,
      "orders": [...]
    }
    ↓
9. JavaScript cập nhật bảng hiển thị
```

### 6️⃣ Luồng Xem Chi Tiết Đơn Hàng (Admin)

```
1. Admin nhấn nút "Xem chi tiết" trên đơn hàng
   ↓
2. Request được gửi đến AdminOrderController (/admin/orders/details/123)
   ↓
3. AdminOrderController kiểm tra:
   - Người dùng là admin không?
   - orderId có hợp lệ không?
   ↓
4. AdminOrderController gọi OrderDAO.getOrderById(orderId)
   ↓
5. OrderDAO thực thi SQL SELECT từ bảng orders
   ↓
6. OrderDAO gọi loadOrderItems(order)
   ↓
7. AdminOrderController gửi dữ liệu đến admin/order-detail.jsp
   ↓
8. admin/order-detail.jsp hiển thị:
   - Thông tin đơn hàng chi tiết
   - Danh sách sản phẩm
   - Thông tin khách hàng
   - Nút cập nhật trạng thái
```

---

## C. Các Endpoint

| Endpoint | Method | Mô Tả |
|----------|--------|-------|
| `/orders` | GET | Xem danh sách đơn hàng (customer) |
| `/admin/orders` | GET | Quản lý đơn hàng (admin) |
| `/admin/orders/details/*` | GET | Xem chi tiết đơn hàng (admin) |
| `/admin/orders/update-status` | POST | Cập nhật trạng thái (admin) |
| `/admin/orders/by-status` | POST | Lọc theo trạng thái (admin) |

---

## D. Trạng Thái Đơn Hàng

| Status | Mô Tả |
|--------|-------|
| **pending** | Đơn hàng vừa tạo, chờ xử lý |
| **completed** | Đơn hàng đã hoàn thành |
| **cancelled** | Đơn hàng đã bị hủy |

---

## E. Dữ Liệu Lưu Trong Database

**Bảng orders:**
```sql
order_id | user_id | total_amount | status | order_date | discount_code | discount_amount
```

**Bảng order_items:**
```sql
order_item_id | order_id | product_id | quantity | price
```

---

## F. Tính Năng Đặc Biệt

### Thống Kê Đơn Hàng
- Đếm số đơn hàng theo trạng thái
- Hiển thị trên dashboard admin

### Lọc Đơn Hàng
- Lọc theo trạng thái (pending, completed, cancelled)
- Hiển thị danh sách đơn hàng phù hợp

### Xác Thực Trạng Thái
- Chỉ chấp nhận 3 trạng thái hợp lệ
- Từ chối các trạng thái không hợp lệ

