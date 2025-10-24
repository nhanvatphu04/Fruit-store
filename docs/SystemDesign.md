# Fruit Store - System Design

## 1. Kiến trúc & phân tầng

### 1.1 Mô hình tổng quát
```
Client (Browser)
   ↓
JSP (View)
   ↓
Servlet (Controller)
   ↓
Service Layer (Logic)
   ↓
DAO Layer (Data Access)
   ↓
MySQL Database
```

### 1.2 Vai trò các lớp
| Lớp         | Mô tả                                         |
|-------------|-----------------------------------------------|
| JSP         | Hiển thị dữ liệu, gửi request (View)          |
| Servlet     | Điều hướng request/response, gọi service      |
| Service     | Xử lý nghiệp vụ, tính toán, xác thực logic    |
| DAO         | Thao tác CRUD trực tiếp với MySQL             |
| Model       | Lớp dữ liệu (Product, User, Order, Combo, Discount, …) |

---

## 2. Luồng xử lý chính

### 2.1 Thêm vào giỏ hàng
1. User click “Add to Cart”.
2. AJAX gửi POST /cart?action=add&productId=...&qty=1.
3. CartServlet gọi CartService.
4. CartService kiểm tra tồn kho → cập nhật giỏ (CartDAO).
5. Trả JSON kết quả.

### 2.2 Cập nhật giỏ hàng
1. User thay đổi số lượng hoặc bỏ chọn item.
2. CartDAO cập nhật cart_items hoặc cart_combos.

### 2.3 Thanh toán
1. Client gửi POST /checkout.
2. CheckoutServlet kiểm tra: tồn kho, discount, combo.
3. Tạo Order và OrderItem.
4. Giảm tồn kho (ProductDAO cập nhật product_stats).
5. Trả trang xác nhận đơn hàng.

---

## 3. Thiết kế cơ sở dữ liệu

### 3.1 Bảng chính
| Bảng           | Chức năng                        |
|----------------|----------------------------------|
| users          | Thông tin người dùng             |
| categories     | Danh mục sản phẩm                |
| products       | Sản phẩm (trái cây)              |
| product_stats  | Theo dõi tồn kho, lượt mua       |
| cart_items     | Sản phẩm trong giỏ               |
| cart_combos    | Combo trong giỏ                  |
| combos         | Gói sản phẩm (combo)             |
| combo_items    | Liên kết combo ↔ product         |
| discounts      | Phiếu giảm giá                   |
| orders         | Đơn hàng                         |
| order_items    | Chi tiết đơn hàng                |

### 3.2 Quan hệ chính
- users (1-n) orders
- products (1-n) order_items
- categories (1-n) products
- combos (1-n) combo_items
- cart_items, cart_combos thuộc về user hoặc session_id
- product_stats (1-1) products

---

## 4. Sơ đồ & mở rộng

### 4.1 Mở rộng tương lai
- Tích hợp cổng thanh toán (VNPay, Momo).
- Thêm phân hệ thống kê doanh thu (Admin Dashboard).
- API RESTful phục vụ mobile app.
- Hệ thống xếp hạng sản phẩm, review từ người dùng.