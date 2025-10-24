## Service

1. AuthService: Service xác thực, phân quyền, quản lý session đăng nhập
- register: Đăng ký user mới
- login: Đăng nhập: trả về User nếu thành công, null nếu thất bại
- isAdmin: Kiểm tra quyền admin
- isCustomer: Kiểm tra quyền customer
- validatePassword: Kiểm tra password cũ

2. CartService: Service xử lý nghiệp vụ giỏ hàng
- getCartByUserId: Lấy giỏ hàng theo user
- addToCart: Thêm sản phẩm vào giỏ hàng
- updateCartItem: Cập nhật số lượng sản phẩm trong giỏ hàng
- updateCartItemSelection: Cập nhật trạng thái chọn sản phẩm trong giỏ hàng
- removeCartItem: Xóa sản phẩm khỏi giỏ hàng
- clearCart: Xóa toàn bộ giỏ hàng
- calculateSelectedTotal: Tính tổng tiền các sản phẩm được chọn trong giỏ hàng

3. CategoryService: Service quản lý danh mục sản phẩm
- getAllCategories: Lấy tất cả danh mục
- getCategoryById: Lấy danh mục theo ID
- addCategory: Tạo mới danh mục
- updateCategory: Cập nhật danh mục
- deleteCategory: Xóa danh mục
- getCategoryBySlug: Lấy danh mục theo slug

4. ComboService: Service quản lý combo sản phẩm, flash sale
- getAllCombos: Lấy tất cả combo
- getComboById: Lấy combo theo ID
- addCombo: Tạo mới combo
- updateCombo: Cập nhật combo
- deleteCombo: Xóa combo
- getComboItemsByComboId: Lấy các sản phẩm trong combo theo ID combo
- addComboItem: Thêm sản phẩm vào combo
- removeComboItem: Xóa sản phẩm khỏi combo
- getActiveCombo: Lấy danh sách combo đang hoạt động (flash sale)

5. DiscountService: Service quản lý mã giảm giá
- getDiscountByCode: Lấy mã giảm giá theo code
- getAllDiscounts: Lấy tất cả mã giảm giá
- addDiscount: Thêm mã giảm giá mới
- updateDiscount: Cập nhật mã giảm giá
- deleteDiscount: Xóa mã giảm giá theo ID

6. NotificationService: Service gửi và quản lý thông báo
- getNotificationsByUserId: Lấy thông báo theo user
- addNotification: Thêm thông báo mới
- markAsRead: Đánh dấu thông báo đã đọc
- deleteNotification: Xóa thông báo

7. OrderService: Service xử lý nghiệp vụ đơn hàng
- getOrdersByUserId: Lấy danh sách đơn hàng theo user
- getOrderById: Lấy đơn hàng theo ID
- addOrder: Thêm đơn hàng mới
- updateOrderStatus: Cập nhật trạng thái đơn hàng
- getAllOrders: Lấy tất cả đơn hàng (cho admin)

8. ProductService: Service xử lý nghiệp vụ sản phẩm
- getAllProducts: Lấy tất cả sản phẩm
- getProductById: Lấy sản phẩm theo ID
- getProductsByCategory: Lấy sản phẩm theo danh mục
- searchProducts: Tìm kiếm sản phẩm theo từ khóa
- addProduct: Thêm sản phẩm mới
- updateProduct: Cập nhật thông tin sản phẩm
- deleteProduct: Xóa sản phẩm
- updateStock: Cập nhật tồn kho sản phẩm
- getBestSellers: Lấy danh sách sản phẩm bán chạy
- getNewProducts: Lấy danh sách sản phẩm mới về

9. UserService: Service quản lý người dùng
- register: Đăng ký user mới
- getUserById: Lấy user theo ID
- getUserByUsername: Lấy user theo username
- getAllUsers: Lấy tất cả user
- updateUser: Cập nhật thông tin user
- deleteUser: Xóa user