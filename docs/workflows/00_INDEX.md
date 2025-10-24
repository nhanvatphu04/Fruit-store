# 📚 Chỉ Mục Luồng Hoạt Động Hệ Thống FruitStore

Tài liệu này cung cấp tổng quan về tất cả các luồng hoạt động của hệ thống FruitStore. Mỗi chức năng được tài liệu hóa chi tiết theo mô hình MVC.

---

## 📋 Danh Sách Các Luồng Hoạt Động

### 1. 🔐 [Xác Thực (Authentication)](01_AUTHENTICATION_WORKFLOW.md)
**Mô tả:** Quản lý đăng ký, đăng nhập, đăng xuất người dùng

**Các Luồng:**
- Đăng ký tài khoản mới
- Đăng nhập vào hệ thống
- Đăng xuất khỏi hệ thống
- Xác thực quyền truy cập

**Các Endpoint:**
- `POST /login` - Xử lý đăng nhập
- `POST /register` - Xử lý đăng ký
- `GET /logout` - Xử lý đăng xuất

**Các Lớp Chính:**
- AuthController
- AuthService
- UserDAO

---

### 2. 🛒 [Giỏ Hàng (Cart)](02_CART_WORKFLOW.md)
**Mô tả:** Quản lý giỏ hàng, thêm/xóa/cập nhật sản phẩm

**Các Luồng:**
- Xem giỏ hàng
- Thêm sản phẩm vào giỏ
- Cập nhật số lượng sản phẩm
- Xóa sản phẩm khỏi giỏ
- Chọn sản phẩm thanh toán

**Các Endpoint:**
- `GET /cart` - Xem giỏ hàng
- `POST /cart/add` - Thêm vào giỏ
- `POST /cart/update` - Cập nhật giỏ
- `POST /cart/remove` - Xóa khỏi giỏ
- `POST /cart/select` - Chọn sản phẩm

**Các Lớp Chính:**
- CartController
- CartService
- CartDAO

---

### 3. 💳 [Thanh Toán (Checkout)](03_CHECKOUT_WORKFLOW.md)
**Mô tả:** Xử lý quy trình thanh toán và tạo đơn hàng

**Các Luồng:**
- Xem trang xác nhận thanh toán
- Xử lý thanh toán
- Tạo đơn hàng mới
- Cập nhật tồn kho
- Xóa giỏ hàng

**Các Endpoint:**
- `GET /checkout` - Xem trang checkout
- `POST /checkout` - Xử lý thanh toán

**Các Lớp Chính:**
- CheckoutController
- OrderService
- OrderDAO
- OrderItemDAO

---

### 4. 📦 [Quản Lý Sản Phẩm (Product Management)](04_PRODUCT_MANAGEMENT_WORKFLOW.md)
**Mô tả:** Quản lý sản phẩm, danh mục, tìm kiếm

**Các Luồng:**
- Xem danh sách sản phẩm
- Xem chi tiết sản phẩm
- Thêm sản phẩm (admin)
- Sửa sản phẩm (admin)
- Xóa sản phẩm (admin)

**Các Endpoint:**
- `GET /products` - Xem danh sách sản phẩm
- `POST /admin/products/add` - Thêm sản phẩm
- `POST /admin/products/edit` - Sửa sản phẩm
- `POST /admin/products/delete` - Xóa sản phẩm

**Các Lớp Chính:**
- ProductController
- AdminProductController
- ProductService
- ProductDAO

---

### 5. 📋 [Quản Lý Đơn Hàng (Order Management)](05_ORDER_MANAGEMENT_WORKFLOW.md)
**Mô tả:** Quản lý đơn hàng, cập nhật trạng thái

**Các Luồng:**
- Xem danh sách đơn hàng (customer)
- Xem chi tiết đơn hàng
- Xem danh sách đơn hàng (admin)
- Cập nhật trạng thái đơn hàng
- Lọc đơn hàng theo trạng thái

**Các Endpoint:**
- `GET /orders` - Xem đơn hàng (customer)
- `GET /admin/orders` - Quản lý đơn hàng (admin)
- `POST /admin/orders/update-status` - Cập nhật trạng thái
- `POST /admin/orders/by-status` - Lọc theo trạng thái

**Các Lớp Chính:**
- OrderController
- AdminOrderController
- OrderService
- OrderDAO

---

### 6. 🎟️ [Mã Giảm Giá (Discount)](06_DISCOUNT_WORKFLOW.md)
**Mô tả:** Quản lý mã giảm giá, áp dụng mã

**Các Luồng:**
- Áp dụng mã giảm giá
- Xóa mã giảm giá
- Liên kết mã với đơn hàng
- Xác thực mã giảm giá

**Các Endpoint:**
- `POST /api/discount/apply` - Áp dụng mã
- `POST /api/discount/remove` - Xóa mã

**Các Lớp Chính:**
- DiscountApiController
- DiscountService
- DiscountDAO
- DiscountUsageDAO

---

### 7. 🎁 [Combo (Flash Sale)](07_COMBO_WORKFLOW.md)
**Mô tả:** Quản lý combo/flash sale

**Các Luồng:**
- Xem combo trên trang home
- Xem chi tiết combo
- Thêm combo vào giỏ
- Thêm combo (admin)
- Sửa combo (admin)
- Xóa combo (admin)

**Các Endpoint:**
- `GET /combo` - Xem danh sách combo
- `POST /admin/combos/add` - Thêm combo
- `POST /admin/combos/edit` - Sửa combo
- `POST /admin/combos/delete` - Xóa combo

**Các Lớp Chính:**
- ComboController
- AdminComboController
- ComboService
- ComboDAO

---

### 8. 🔍 [Tìm Kiếm (Search)](08_SEARCH_WORKFLOW.md)
**Mô tả:** Tìm kiếm sản phẩm, autocomplete

**Các Luồng:**
- Tìm kiếm cơ bản
- Autocomplete (gợi ý)
- Tìm kiếm nâng cao (lọc, sắp xếp)

**Các Endpoint:**
- `GET /products?q=keyword` - Tìm kiếm
- `POST /api/search/autocomplete` - Gợi ý

**Các Lớp Chính:**
- SearchController
- SearchApiController
- ProductService
- ProductDAO

---

### 9. 📊 [Dashboard Admin](09_ADMIN_DASHBOARD_WORKFLOW.md)
**Mô tả:** Xem thống kê và báo cáo hệ thống

**Các Luồng:**
- Xem dashboard
- Xem thống kê đơn hàng
- Xem thống kê sản phẩm
- Xem thống kê người dùng
- Xem biểu đồ doanh thu

**Các Endpoint:**
- `GET /admin/dashboard` - Xem dashboard

**Các Lớp Chính:**
- AdminDashboardController
- OrderService
- ProductService
- UserService

---

## 🏗️ Kiến Trúc MVC

### Model (Mô Hình Dữ Liệu)
- User, Product, Category, Order, OrderItem
- Cart, CartItem, CartCombo
- Discount, DiscountUsage
- Combo, ComboItem

### View (Giao Diện)
- JSP files trong thư mục `/jsp`
- HTML, CSS, JavaScript
- Bootstrap 5 UI Framework

### Controller (Điều Khiển)
- Servlet classes trong thư mục `/controllers`
- Xử lý HTTP requests
- Gọi Service layer

### Service Layer (Lớp Dịch Vụ)
- Xử lý logic nghiệp vụ
- Điều phối DAO
- Xác thực dữ liệu

### DAO Layer (Truy Cập Dữ Liệu)
- Thực thi SQL queries
- Chuyển đổi ResultSet thành objects
- Quản lý kết nối database

---

## 🔄 Luồng Chung

```
1. Người dùng gửi request từ JSP
   ↓
2. Request đến Controller
   ↓
3. Controller xác thực cơ bản
   ↓
4. Controller gọi Service
   ↓
5. Service xử lý logic nghiệp vụ
   ↓
6. Service gọi DAO
   ↓
7. DAO thực thi SQL
   ↓
8. DAO trả về dữ liệu
   ↓
9. Service xử lý dữ liệu
   ↓
10. Controller nhận kết quả
    ↓
11. Controller gửi dữ liệu đến JSP
    ↓
12. JSP hiển thị cho người dùng
```

---

## 📝 Quy Ước Tài Liệu

Mỗi luồng hoạt động được tài liệu hóa với cấu trúc:

1. **A. Vai Trò Của Các Lớp** - Mô tả nhiệm vụ của từng lớp
2. **B. Luồng Hoạt Động** - Chi tiết từng bước xử lý
3. **C. Các Endpoint** - Danh sách URL và HTTP methods
4. **D-H. Thông Tin Bổ Sung** - Dữ liệu, xác thực, tính năng đặc biệt

---

## 🚀 Cách Sử Dụng Tài Liệu

1. **Hiểu luồng hoạt động:** Đọc phần "B. Luồng Hoạt Động"
2. **Tìm endpoint:** Xem phần "C. Các Endpoint"
3. **Hiểu vai trò lớp:** Đọc phần "A. Vai Trò Của Các Lớp"
4. **Xác thực dữ liệu:** Xem phần "Xác Thực Dữ Liệu"
5. **Tìm tính năng đặc biệt:** Xem phần cuối cùng

---

## 📞 Liên Hệ & Hỗ Trợ

Nếu có câu hỏi về luồng hoạt động, vui lòng:
1. Kiểm tra tài liệu tương ứng
2. Xem code trong thư mục `/src/java`
3. Kiểm tra database schema trong `/docs/database.sql`

---

**Cập nhật lần cuối:** 2025-10-24
**Phiên bản:** 1.0

