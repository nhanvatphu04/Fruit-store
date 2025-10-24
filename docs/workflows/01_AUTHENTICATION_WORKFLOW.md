# 🔐 Luồng Hoạt Động: Xác Thực (Authentication)

## A. Vai Trò Của Các Lớp

### 1. Model User
- Đại diện cho dữ liệu người dùng
- Chứa các thuộc tính: userId, username, email, password, fullName, phone, address, role, ...

### 2. AuthController
**Nhiệm vụ:**
- Tiếp nhận yêu cầu (Request) từ người dùng (login, register, logout)
- Xác thực cơ bản: Kiểm tra dữ liệu có bị trống không
- Lấy dữ liệu từ request (username, password, email, ...)
- Gọi AuthService để xử lý logic xác thực
- Trả về phản hồi: Điều hướng đến trang phù hợp hoặc hiển thị lỗi

### 3. AuthService
**Nhiệm vụ:**
- Nhận yêu cầu từ Controller
- Xử lý logic nghiệp vụ:
  - Kiểm tra username/email đã tồn tại chưa (khi đăng ký)
  - Kiểm tra password có khớp không (khi đăng nhập)
  - Mã hóa password trước khi lưu
- Điều phối UserDAO để lấy/lưu dữ liệu

### 4. UserDAO
**Nhiệm vụ:**
- Nhận yêu cầu từ Service
- Thực thi các câu lệnh SQL: getUserByUsername(), getUserByEmail(), addUser(), ...
- Chuyển đổi ResultSet thành đối tượng User

---

## B. Luồng Hoạt Động

### 1️⃣ Luồng Đăng Ký (Register)

```
1. Người dùng truy cập trang đăng ký (/register)
   ↓
2. AuthController.doGet() hiển thị form register
   ↓
3. Người dùng điền thông tin (username, email, password, confirmPassword)
   ↓
4. Người dùng nhấn nút "Đăng ký"
   ↓
5. Request được gửi đến AuthController.doPost()
   ↓
6. AuthController kiểm tra:
   - Dữ liệu có bị trống không?
   - Password có khớp với confirmPassword không?
   ↓
7. AuthController tạo đối tượng User từ dữ liệu request
   ↓
8. AuthController gọi AuthService.register(user)
   ↓
9. AuthService kiểm tra:
   - Username đã tồn tại chưa?
   - Email đã tồn tại chưa?
   ↓
10. AuthService gọi UserDAO.addUser(user)
    ↓
11. UserDAO thực thi SQL INSERT vào bảng users
    ↓
12. Nếu thành công:
    - AuthController điều hướng đến trang login
    Nếu thất bại:
    - AuthController hiển thị lỗi trên form register
```

### 2️⃣ Luồng Đăng Nhập (Login)

```
1. Người dùng truy cập trang đăng nhập (/login)
   ↓
2. AuthController.doGet() hiển thị form login
   ↓
3. Người dùng điền thông tin (username, password)
   ↓
4. Người dùng nhấn nút "Đăng nhập"
   ↓
5. Request được gửi đến AuthController.doPost()
   ↓
6. AuthController kiểm tra:
   - Dữ liệu có bị trống không?
   ↓
7. AuthController gọi AuthService.login(username, password)
   ↓
8. AuthService gọi UserDAO.getUserByUsername(username)
   ↓
9. UserDAO thực thi SQL SELECT từ bảng users
   ↓
10. AuthService kiểm tra:
    - User có tồn tại không?
    - Password có khớp không?
    ↓
11. Nếu thành công:
    - AuthController lưu user vào session
    - Nếu role = "admin" → điều hướng đến /admin/dashboard
    - Nếu role = "customer" → điều hướng đến /home
    ↓
    Nếu thất bại:
    - AuthController hiển thị lỗi trên form login
```

### 3️⃣ Luồng Đăng Xuất (Logout)

```
1. Người dùng nhấn nút "Đăng xuất"
   ↓
2. Request được gửi đến AuthController (/logout)
   ↓
3. AuthController.doGet() xử lý:
   - Lấy session hiện tại
   - Xóa session (session.invalidate())
   ↓
4. AuthController điều hướng đến trang home
```

---

## C. Các Endpoint

| Endpoint | Method | Mô Tả |
|----------|--------|-------|
| `/login` | GET | Hiển thị form đăng nhập |
| `/login` | POST | Xử lý đăng nhập |
| `/register` | GET | Hiển thị form đăng ký |
| `/register` | POST | Xử lý đăng ký |
| `/logout` | GET | Xử lý đăng xuất |

---

## D. Dữ Liệu Lưu Trong Session

```java
session.setAttribute("user", user);
// Chứa thông tin người dùng đã đăng nhập
// Sử dụng để kiểm tra quyền truy cập các trang
```

---

## E. Xác Thực Quyền Truy Cập

Các trang yêu cầu đăng nhập sẽ kiểm tra:

```java
User user = (User) session.getAttribute("user");
if (user == null) {
    response.sendRedirect(request.getContextPath() + "/login");
    return;
}
```

Các trang admin yêu cầu role = "admin":

```java
if (!"admin".equals(user.getRole())) {
    response.sendError(HttpServletResponse.SC_FORBIDDEN);
    return;
}
```

