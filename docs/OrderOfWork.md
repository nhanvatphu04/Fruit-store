# Cửa hàng Trái cây - Quy trình Thực hiện Công việc

Tài liệu này mô tả quy trình công việc hoàn chỉnh được triển khai trong hệ thống Cửa hàng Trái cây, từ đăng ký người dùng đến hoàn tất đơn hàng và theo dõi.

---

## 1. Quy trình Quản lý Người dùng

### 1.1 Đăng ký Người dùng
1. Người dùng truy cập trang `/register`
2. Người dùng điền vào biểu mẫu đăng ký: tên người dùng, email, mật khẩu, họ tên, số điện thoại, địa chỉ
3. **AuthController** nhận yêu cầu POST
4. **AuthService** xác thực:
   - Tính duy nhất của tên người dùng (thông qua UserDAO)
   - Tính duy nhất của email (thông qua UserDAO)
5. **PasswordUtils** mã hóa mật khẩu bằng thuật toán bảo mật
6. **UserDAO** thêm người dùng mới vào bảng `users` với vai trò='customer'
7. Người dùng được chuyển hướng đến trang đăng nhập

### 1.2 Đăng nhập Người dùng
1. Người dùng truy cập trang `/login`
2. Người dùng nhập tên người dùng/email và mật khẩu
3. **AuthController** nhận yêu cầu POST
4. **AuthService** gọi **UserDAO.getUserByUsername()** để lấy thông tin người dùng
5. **PasswordUtils** xác minh mật khẩu đã mã hóa
6. Nếu hợp lệ:
   - Phiên đăng nhập được tạo với đối tượng người dùng
   - Nếu vai trò='admin': chuyển hướng đến `/admin/dashboard`
   - Nếu vai trò='customer': chuyển hướng đến `/home`
7. Nếu không hợp lệ: hiển thị thông báo lỗi trên trang đăng nhập

### 1.3 Đăng xuất Người dùng
1. Người dùng nhấn nút đăng xuất
2. **AuthController** hủy phiên đăng nhập
3. Người dùng được chuyển hướng đến `/home`

---

## 2. Quy trình Duyệt sản phẩm

### 2.1 Hiển thị Trang chủ
1. Người dùng truy cập `/home`
2. **HomeController** thực hiện:
   - **ProductService.getBestSellerProductsByFlag(4)** → lấy 4 sản phẩm bán chạy nhất
   - **ProductService.getNewProductsByFlag(4)** → lấy 4 sản phẩm mới
   - **ComboService.getActiveCombo()** → lấy các combo/khuyến mãi đang hoạt động
3. Dữ liệu được chuyển đến `home.jsp` để hiển thị
4. Trang hiển thị các sản phẩm nổi bật và khuyến mãi đang hoạt động

### 2.2 Tìm kiếm Sản phẩm
1. Người dùng nhập từ khóa vào ô tìm kiếm (tối thiểu 2 ký tự)
2. Yêu cầu AJAX được gửi đến `/api/search-suggest`
3. **SearchApiController** truy vấn **ProductDAO** để tìm sản phẩm phù hợp
4. Kết quả được trả về dưới dạng JSON với gợi ý tự động hoàn thành
5. Người dùng nhấp vào gợi ý → chuyển hướng đến `/product?id={productId}`

### 2.3 Chi tiết Sản phẩm
1. Người dùng truy cập `/product?id={productId}`
2. **ProductController** lấy chi tiết sản phẩm thông qua **ProductDAO**
3. **ProductService** tải thông tin liên quan:
   - Thống kê sản phẩm (số lượng tồn kho, số lượng bán ra)
   - Thông tin danh mục
   - Phần trăm giảm giá
4. Chi tiết sản phẩm được hiển thị trên `product.jsp`
5. Người dùng có thể thêm sản phẩm vào giỏ hàng từ trang này

---

## 3. Quy trình Giỏ hàng

### 3.1 Thêm vào Giỏ hàng
1. Người dùng nhấn nút "Thêm vào giỏ hàng" trên trang sản phẩm
2. Yêu cầu AJAX POST được gửi đến `/cart/add` với productId và số lượng
3. **CartController** nhận yêu cầu
4. **CartService** xác thực:
   - Số lượng > 0
   - Sản phẩm tồn tại
   - Số lượng tồn kho >= số lượng yêu cầu
5. **CartDAO** thêm/cập nhật bản ghi trong bảng `cart_items`
6. Phản hồi JSON được trả về với thông báo thành công/thất bại
7. Số lượng giỏ hàng được cập nhật trên thanh tiêu đề

### 3.2 Thêm Combo vào Giỏ hàng
1. Người dùng nhấn "Thêm vào giỏ hàng" trên thẻ combo
2. Yêu cầu AJAX POST được gửi đến `/cart/add-combo` với comboId
3. **CartApiController** xác thực:
   - Người dùng đã đăng nhập
   - Combo tồn tại
   - Combo đang hoạt động (trong khoảng thời gian áp dụng)
4. **CartComboDAO** thêm bản ghi vào bảng `cart_combos`
5. Phản hồi JSON được trả về

### 3.3 Xem Giỏ hàng
1. Người dùng truy cập `/cart`
2. **CartController** lấy:
   - **CartService.getCartByUserId()** → các mục trong giỏ hàng với chi tiết sản phẩm
   - **CartService.getCartCombosByUserId()** → các combo trong giỏ hàng với chi tiết combo
3. **CartService** tính toán:
   - Tổng phụ của các mục đã chọn
   - Giảm giá được áp dụng (nếu có từ phiên)
   - Tổng số tiền
4. Dữ liệu được chuyển đến `cart.jsp` để hiển thị
5. Người dùng có thể:
   - Cập nhật số lượng
   - Xóa mục
   - Chọn/bỏ chọn mục để thanh toán
   - Áp dụng mã giảm giá

### 3.4 Cập nhật Mục Giỏ hàng
1. Người dùng thay đổi số lượng hoặc xóa mục
2. Yêu cầu AJAX POST được gửi đến `/cart/update` hoặc `/cart/remove`
3. **CartService** xác thực số lượng mới
4. **CartDAO** cập nhật bảng `cart_items`
5. Tổng giỏ hàng được tính toán lại và trả về dưới dạng JSON

### 3.5 Áp dụng Mã Giảm giá
1. Người dùng nhập mã giảm giá trên trang giỏ hàng
2. Yêu cầu AJAX POST được gửi đến `/discount/apply`
3. **DiscountApiController** nhận mã
4. **DiscountService** xác thực:
   - Mã tồn tại trong bảng `discounts`
   - Mã đang hoạt động (trong khoảng thời gian áp dụng)
   - Tổng số tiền đơn hàng đạt yêu cầu tối thiểu
   - Giới hạn sử dụng chưa bị vượt quá
5. Nếu hợp lệ:
   - Tính toán số tiền giảm giá
   - Lưu mã vào phiên dưới dạng `appliedDiscount`
   - Tăng số lần sử dụng
   - Trả về số tiền giảm giá cho giao diện
6. Tổng giỏ hàng được cập nhật với giảm giá được áp dụng

---

## 4. Quy trình Xử lý Đơn hàng

### 4.1 Quy trình Thanh toán
1. Người dùng nhấn nút "Thanh toán" trong giỏ hàng
2. Người dùng được chuyển hướng đến `/checkout` (Lưu ý: CheckoutController chưa được triển khai)
3. Quy trình dự kiến (dựa trên SystemDesign.md):
   - Xác thực các mục đã chọn tồn tại và có đủ hàng trong kho
   - Xác thực mã giảm giá đã áp dụng
   - Tính tổng số tiền cuối cùng với giảm giá
   - Tạo bản ghi Đơn hàng trong bảng `orders`
   - Tạo bản ghi OrderItem trong bảng `order_items` cho mỗi mục đã chọn
   - Giảm số lượng tồn kho trong bảng `product_stats`
   - Xóa các mục trong giỏ hàng khỏi bảng `cart_items` và `cart_combos`
   - Hiển thị trang xác nhận đơn hàng

### 4.2 Theo dõi Trạng thái Đơn hàng
1. Người dùng truy cập `/orders`
2. **OrderController** lấy:
   - **OrderDAO.getOrdersByUserId()** → tất cả đơn hàng của người dùng đã đăng nhập
3. Đơn hàng được hiển thị trên `orders.jsp` với các trạng thái:
   - PENDING: Chờ xác nhận
   - PROCESSING: Đang xử lý
   - SHIPPED: Đang giao hàng
   - DELIVERED: Đã giao hàng
   - CANCELLED: Đã hủy

---

## 5. Quy trình Quản lý Quản trị viên

### 5.1 Bảng điều khiển Quản trị viên
1. Quản trị viên truy cập `/admin/dashboard`
2. **AdminDashboardController** lấy:
   - **DashboardService** tổng hợp thống kê
   - Số lượng đơn hàng theo trạng thái
   - Thống kê sản phẩm
   - Dữ liệu doanh thu
3. Bảng điều khiển được hiển thị trên `admin/dashboard.jsp`

### 5.2 Quản lý Đơn hàng
1. Quản trị viên truy cập `/admin/orders`
2. **AdminOrderController** lấy:
   - Tất cả đơn hàng thông qua **OrderDAO.getAllOrders()**
   - Thống kê đơn hàng theo trạng thái
3. Quản trị viên có thể:
   - Xem chi tiết đơn hàng
   - Cập nhật trạng thái đơn hàng qua `/admin/orders/update-status`
   - **OrderDAO.updateOrderStatus()** cập nhật bảng `orders`

### 5.3 Quản lý Sản phẩm
1. Quản trị viên truy cập `/admin/products`
2. **AdminProductController** xử lý các thao tác CRUD:
   - Tạo: POST đến `/admin/products/add`
   - Đọc: GET `/admin/products`
   - Cập nhật: POST đến `/admin/products/update`
   - Xóa: POST đến `/admin/products/delete`
3. **ProductDAO** quản lý bảng `products` và `product_stats`

### 5.4 Quản lý Giảm giá
1. Quản trị viên truy cập `/admin/discounts`
2. **AdminDiscountController** xử lý CRUD giảm giá
3. **DiscountDAO** quản lý bảng `discounts`

### 5.5 Quản lý Combo
1. Quản trị viên truy cập `/admin/combos`
2. **AdminComboController** xử lý CRUD combo
3. **ComboDAO** quản lý bảng `combos` và `combo_items`

---

## 6. Kiến trúc Luồng Dữ liệu

### Các Thành phần Chính:
- **Controllers**: Định tuyến yêu cầu, xác thực đầu vào, gọi dịch vụ
- **Services**: Thực hiện logic nghiệp vụ, điều phối DAOs
- **DAOs**: Thực thi truy vấn SQL, trả về đối tượng mô hình
- **Models**: Đối tượng truyền dữ liệu (User, Product, Order, v.v.)
- **Utils**: Các lớp hỗ trợ (DbConnect, PasswordUtils, JsonResponse)

---

## 7. Quản lý Phiên

- Thời gian hết hạn phiên: 30 phút (cấu hình trong web.xml)
- Đối tượng người dùng được lưu trong phiên sau khi đăng nhập
- Mã giảm giá đã áp dụng được lưu trong phiên
- Phiên bị hủy khi đăng xuất

---

## 8. Các Bảng Cơ sở dữ liệu Chính

| Bảng | Mục đích |
|-------|---------|
| users | Tài khoản và hồ sơ người dùng |
| products | Danh mục sản phẩm |
| categories | Danh mục sản phẩm |
| product_stats | Theo dõi tồn kho và doanh số |
| cart_items | Các mục trong giỏ hàng |
| cart_combos | Các combo trong giỏ hàng |
| orders | Đơn hàng của khách hàng |
| order_items | Các mục trong đơn hàng |
| combos | Gói khuyến mãi |
| combo_items | Thành phần combo |
| discounts | Mã giảm giá |

---

## 9. Trạng thái Triển khai Hiện tại

✅ **Đã triển khai:**
- Đăng ký và xác thực người dùng
- Duyệt và tìm kiếm sản phẩm
- Quản lý giỏ hàng
- Áp dụng mã giảm giá
- Xem và theo dõi đơn hàng
- Bảng điều khiển và quản lý quản trị viên

⚠️ **Triển khai một phần:**
- Quy trình thanh toán (được đề cập nhưng CheckoutController chưa được tìm thấy)
- Tạo đơn hàng từ giỏ hàng

🔄 **Cải tiến trong tương lai:**
- Tích hợp cổng thanh toán (VNPay, Momo)
- Thông báo qua email
- Đánh giá và xếp hạng sản phẩm
- Phân tích nâng cao
- API cho thiết bị di động