# 📊 Luồng Hoạt Động: Dashboard Admin

## A. Vai Trò Của Các Lớp

### 1. AdminDashboardController
**Nhiệm vụ:**
- Tiếp nhận yêu cầu từ admin (xem dashboard)
- Xác thực quyền admin
- Gọi các Service để lấy dữ liệu thống kê
- Trả về phản hồi: Hiển thị dashboard

### 2. OrderService, ProductService, UserService, DiscountService
**Nhiệm vụ:**
- Nhận yêu cầu từ Controller
- Xử lý logic thống kê:
  - Đếm số đơn hàng
  - Tính tổng doanh thu
  - Đếm số sản phẩm
  - Đếm số người dùng
  - Lấy đơn hàng gần đây
- Điều phối DAO

### 3. DAO Layer
**Nhiệm vụ:**
- Thực thi SQL để lấy dữ liệu thống kê
- Chuyển đổi ResultSet thành đối tượng

---

## B. Luồng Hoạt Động

### 1️⃣ Luồng Xem Dashboard

```
1. Admin truy cập trang dashboard (/admin/dashboard)
   ↓
2. AdminDashboardController.doGet() kiểm tra:
   - Người dùng đã đăng nhập chưa?
   - Người dùng là admin không?
   ↓
3. AdminDashboardController gọi các Service để lấy dữ liệu:
   
   a) Thống kê đơn hàng:
      - OrderService.countTotalOrders()
      - OrderService.countOrdersByStatus("pending")
      - OrderService.countOrdersByStatus("completed")
      - OrderService.countOrdersByStatus("cancelled")
      ↓
   b) Thống kê doanh thu:
      - OrderService.calculateTotalRevenue()
      - OrderService.calculateRevenueByMonth()
      ↓
   c) Thống kê sản phẩm:
      - ProductService.countTotalProducts()
      - ProductService.getTopSellingProducts(5)
      - ProductService.getLowStockProducts()
      ↓
   d) Thống kê người dùng:
      - UserService.countTotalUsers()
      - UserService.countNewUsersThisMonth()
      ↓
   e) Đơn hàng gần đây:
      - OrderService.getRecentOrders(10)
      ↓
   f) Mã giảm giá:
      - DiscountService.countActiveDiscounts()
      - DiscountService.getTopUsedDiscounts(5)
   ↓
4. Các Service gọi DAO để lấy dữ liệu
   ↓
5. DAO thực thi SQL:
   - SELECT COUNT(*) FROM orders
   - SELECT SUM(total_amount) FROM orders
   - SELECT * FROM orders ORDER BY order_date DESC LIMIT 10
   - ...
   ↓
6. DAO chuyển đổi ResultSet thành dữ liệu
   ↓
7. AdminDashboardController gửi dữ liệu đến admin/dashboard.jsp:
   - Tổng số đơn hàng
   - Tổng doanh thu
   - Số đơn hàng theo trạng thái
   - Tổng số sản phẩm
   - Tổng số người dùng
   - Đơn hàng gần đây
   - Sản phẩm bán chạy
   - Mã giảm giá đang hoạt động
   ↓
8. admin/dashboard.jsp hiển thị:
   - Các thẻ thống kê (cards)
   - Biểu đồ doanh thu
   - Bảng đơn hàng gần đây
   - Bảng sản phẩm bán chạy
   - Bảng mã giảm giá
```

---

## C. Các Endpoint

| Endpoint | Method | Mô Tả |
|----------|--------|-------|
| `/admin/dashboard` | GET | Xem dashboard |

---

## D. Dữ Liệu Thống Kê

### Thống Kê Đơn Hàng
```
- Tổng số đơn hàng
- Số đơn hàng pending
- Số đơn hàng completed
- Số đơn hàng cancelled
- Tổng doanh thu
- Doanh thu theo tháng
```

### Thống Kê Sản Phẩm
```
- Tổng số sản phẩm
- Sản phẩm bán chạy nhất
- Sản phẩm tồn kho thấp
- Sản phẩm mới nhất
```

### Thống Kê Người Dùng
```
- Tổng số người dùng
- Người dùng mới trong tháng
- Người dùng hoạt động
```

### Thống Kê Mã Giảm Giá
```
- Số mã giảm giá đang hoạt động
- Mã giảm giá được sử dụng nhiều nhất
- Tổng tiền giảm
```

---

## E. Biểu Đồ & Visualizations

### Biểu Đồ Doanh Thu
```
- Doanh thu theo tháng (line chart)
- Doanh thu theo trạng thái đơn hàng (pie chart)
```

### Biểu Đồ Sản Phẩm
```
- Sản phẩm bán chạy (bar chart)
- Tồn kho sản phẩm (bar chart)
```

### Biểu Đồ Người Dùng
```
- Người dùng mới theo tháng (line chart)
```

---

## F. Xác Thực Quyền Truy Cập

```java
User user = (User) session.getAttribute("user");
if (user == null) {
    response.sendRedirect(request.getContextPath() + "/login");
    return;
}

if (!"admin".equals(user.getRole())) {
    response.sendError(HttpServletResponse.SC_FORBIDDEN);
    return;
}
```

---

## G. Tính Năng Đặc Biệt

### Thống Kê Thời Gian Thực
```
- Dữ liệu được cập nhật mỗi khi tải trang
- Có thể thêm auto-refresh
```

### Xuất Báo Cáo
```
- Xuất dữ liệu thống kê ra Excel
- Xuất báo cáo PDF
```

### Bộ Lọc Thời Gian
```
- Xem thống kê theo ngày
- Xem thống kê theo tháng
- Xem thống kê theo năm
```

### Cảnh Báo
```
- Cảnh báo sản phẩm tồn kho thấp
- Cảnh báo mã giảm giá sắp hết hạn
- Cảnh báo đơn hàng chưa xử lý
```

