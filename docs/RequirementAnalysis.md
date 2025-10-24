# Fruit Store - Requirement Analysis

## 1. Tổng quan hệ thống
Fruit Store là website bán hàng trực tuyến, cho phép người dùng xem sản phẩm, thêm vào giỏ hàng, thanh toán, theo dõi đơn hàng; đồng thời hỗ trợ quản trị viên quản lý dữ liệu sản phẩm, đơn hàng, mã giảm giá, combo,...

**Công nghệ sử dụng:**
- Backend: Java Servlet/JSP, JDBC
- Frontend: HTML/CSS/JS, JSTL
- Server: Apache Tomcat
- Database: MySQL
- Kiến trúc: MVC (Model - View - Controller)

---

## 2. Phân tích yêu cầu

### 2.1 Mục tiêu
- Xây dựng nền tảng mua bán trái cây trực tuyến.
- Quản lý tập trung dữ liệu người dùng, sản phẩm, đơn hàng, combo, mã giảm giá.
- Dễ bảo trì, dễ mở rộng thêm các loại sản phẩm và chương trình khuyến mãi.

### 2.2 Đối tượng sử dụng (Actors)
| Vai trò      | Quyền hạn                                                      |
|-------------|----------------------------------------------------------------|
| Khách hàng  | Xem, tìm kiếm, thêm vào giỏ, mua hàng, xem đơn hàng            |
| Admin       | Quản lý người dùng, sản phẩm, đơn hàng, mã giảm giá, combo     |

### 2.3 Yêu cầu chức năng

#### Trang chủ (`home.jsp`)
- Slider sản phẩm bán chạy, mới về (mỗi loại 4 sản phẩm).
- Hiển thị combo/flash sale nổi bật.
- Footer: thông tin liên hệ, chính sách.

#### Trang sản phẩm (`products.jsp`)
- Danh mục trái cây.
- Thanh tìm kiếm (autocomplete).
- Hiển thị: tên, ảnh, giá, tồn kho.
- Nút "Add to Cart".

#### Trang giỏ hàng (`cart.jsp`)
- Hiển thị danh sách sản phẩm/combos trong giỏ.
- Cho phép: cập nhật số lượng, xóa, tick chọn thanh toán.
- Nhập mã giảm giá.
- Nút "Checkout".

#### Trang đơn hàng (`orders.jsp`)
- Khách hàng: xem trạng thái đơn hàng (Pending, Shipped, Delivered…)
- Admin: cập nhật trạng thái đơn hàng.

#### Admin Panel (`admin.jsp`)
- Dashboard tổng quan.
- CRUD: Users, Products, Orders, Discounts, Combos.

#### Xác thực
- Đăng ký / đăng nhập / phân quyền (USER, ADMIN).
- Lưu session người dùng.

#### Thanh toán
- Validate giỏ hàng, kiểm tra tồn kho, mã giảm giá.
- Lưu địa chỉ giao hàng.
- Tạo mới Order + OrderItem.

### 2.4 Yêu cầu phi chức năng
- UI thân thiện, responsive.
- Bảo mật (hash mật khẩu, session, hạn chế truy cập admin).
- Hiệu năng cao, truy vấn tối ưu.
- Dễ mở rộng (thêm loại sản phẩm, chương trình khuyến mãi).