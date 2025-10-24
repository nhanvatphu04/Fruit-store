# 🍎 Fruit Store

Hệ thống website bán trái cây trực tuyến được xây dựng bằng Java (JSP/Servlet).

## 📋 Tổng quan

- **Công nghệ:** Java (JSP + Servlet + JSTL + EL)
- **Database:** MySQL
- **Web Server:** Apache Tomcat
- **UI Framework:** Bootstrap 5
- **Thư viện bổ sung:** jQuery, FontAwesome, SweetAlert2

## 🌟 Tính năng chính

### 👥 Người dùng
- Xem danh sách sản phẩm, tìm kiếm với autocomplete
- Thêm sản phẩm vào giỏ hàng, quản lý giỏ hàng
- Áp dụng mã giảm giá khi thanh toán
- Theo dõi đơn hàng

### 👨‍💼 Quản trị viên
- Dashboard tổng quan
- Quản lý sản phẩm, danh mục
- Quản lý đơn hàng
- Quản lý mã giảm giá, combo
- Quản lý người dùng

## 🎨 Giao diện

- **Màu sắc chủ đạo:** Xanh lá (Green) + Cam/Vàng (Orange/Yellow)
- **Font chữ:** Roboto, Open Sans, Lato
- **Phong cách:** Clean, nhiều khoảng trắng
- **Components:** Bootstrap Cards, Sliders

## 🏗️ Kiến trúc

Hệ thống được xây dựng theo mô hình MVC:

```
Client (Browser)
     ↓
JSP (View) ←→ Servlet (Controller) ←→ Service Layer ←→ DAO Layer ←→ MySQL
```

## 📦 Thư viện sử dụng

- `jakarta.servlet-api-5.0.0.jar`: Servlet API
- `jakarta.servlet.jsp.jstl-2.0.0.jar`: JSTL support
- `mysql-connector-j-9.3.0.jar`: MySQL JDBC Driver
- `gson-2.10.1.jar`: JSON processing
- `jakarta.mail-2.0.2.jar`: Email support

## 👥 Tác giả

dxtruong16a4
...

## 📄 License

Dự án này được phân phối dưới [Giấy phép MIT](LICENSE).