## Business Process Analysis: Fruit Store

Tài liệu này phác thảo các quy trình kinh doanh chính trong ứng dụng Cửa hàng Trái cây, tập trung vào tương tác của người dùng và phản hồi của hệ thống.

### 1. Quản lý Người dùng

#### 1.1 Đăng ký & Đăng nhập

* **Đăng ký Tài khoản:**
  * Người dùng cung cấp thông tin cá nhân: username, email, mật khẩu, họ tên, số điện thoại, địa chỉ
  * Hệ thống kiểm tra tính duy nhất của username và email
  * Mật khẩu được mã hóa trước khi lưu vào database
  * Tài khoản mới được tạo với role mặc định là 'customer'

* **Đăng nhập:**
  * Người dùng nhập username/email và mật khẩu
  * Hệ thống xác thực thông tin đăng nhập
  * Phiên làm việc được tạo sau khi đăng nhập thành công

#### 1.2 Quản lý Thông tin Cá nhân

* Người dùng có thể cập nhật:
  * Thông tin cá nhân (họ tên, số điện thoại, địa chỉ)
  * Avatar
  * Mật khẩu
* Xem lịch sử đơn hàng và trạng thái

### 2. Quản lý Sản phẩm

#### 2.1 Danh mục Sản phẩm

* **Quản lý Danh mục:**
  * Admin có thể tạo, cập nhật, xóa danh mục
  * Mỗi danh mục có: tên, mô tả, hình ảnh, icon, và slug URL
  * Danh mục được sử dụng để phân loại và lọc sản phẩm

* **Quản lý Sản phẩm:**
  * Admin quản lý thông tin sản phẩm: tên, mô tả, giá, số lượng tồn kho, hình ảnh
  * Có thể đánh dấu sản phẩm là "mới" hoặc "bán chạy"
  * Thiết lập phần trăm giảm giá cho từng sản phẩm
  * Theo dõi số lượng đã bán và thời gian bán cuối cùng

### 3. Quy trình Mua hàng

#### 3.1 Duyệt Sản phẩm

* **Hiển thị Sản phẩm:**
  * Danh sách sản phẩm theo danh mục
  * Lọc và sắp xếp theo nhiều tiêu chí
  * Hiển thị thông tin chi tiết: giá, số lượng tồn, mô tả
  * Đánh dấu sản phẩm mới và bán chạy

#### 3.2 Giỏ hàng

* **Quản lý Giỏ hàng:**
  * Thêm sản phẩm với số lượng chỉ định
  * Thêm combo với số lượng chỉ định
  * Chọn/bỏ chọn sản phẩm để thanh toán
  * Cập nhật số lượng hoặc xóa sản phẩm
  * Áp dụng mã giảm giá

#### 3.3 Combo và Khuyến mãi

* **Quản lý Combo:**
  * Admin tạo combo từ nhiều sản phẩm
  * Thiết lập giá combo ưu đãi
  * Quy định thời gian bắt đầu và kết thúc
  * Theo dõi trạng thái hoạt động của combo

#### 3.4 Mã Giảm giá

* **Quản lý Mã giảm giá:**
  * Admin tạo mã với các thông số:
    * Loại giảm giá (phần trăm hoặc số tiền cố định)
    * Giá trị giảm
    * Giá trị đơn hàng tối thiểu
    * Giảm giá tối đa
    * Thời gian hiệu lực
    * Giới hạn sử dụng
  * Theo dõi số lần sử dụng
  * Kích hoạt/vô hiệu hóa mã

### 4. Quy trình Đặt hàng

#### 4.1 Tạo Đơn hàng

* **Xử lý Đơn hàng:**
  * Chuyển sản phẩm từ giỏ hàng sang đơn hàng
  * Tính tổng tiền sau khi áp dụng giảm giá
  * Lưu thông tin chi tiết từng sản phẩm (số lượng, giá)
  * Cập nhật trạng thái đơn hàng

#### 4.2 Theo dõi Đơn hàng

* **Quản lý Trạng thái:**
  * Admin có thể cập nhật trạng thái đơn hàng
  * Người dùng xem được lịch sử đơn hàng
  * Hệ thống gửi thông báo khi có cập nhật

### 5. Hệ thống Thông báo

* **Quản lý Thông báo:**
  * Tạo thông báo cho các sự kiện:
    * Sản phẩm mới
    * Khuyến mãi
    * Cập nhật đơn hàng
    * Thông báo chung
  * Đánh dấu đã đọc/chưa đọc
  * Lưu thời gian tạo thông báo

### 6. Thống kê và Báo cáo

* **Theo dõi Hiệu suất:**
  * Thống kê sản phẩm bán chạy
  * Theo dõi số lượng đã bán của từng sản phẩm
  * Phân tích xu hướng mua hàng
  * Đánh giá hiệu quả của các chương trình khuyến mãi