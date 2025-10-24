# 🛒 Luồng Hoạt Động: Giỏ Hàng (Cart)

## A. Vai Trò Của Các Lớp

### 1. Model CartItem & CartCombo
- Đại diện cho sản phẩm/combo trong giỏ hàng
- CartItem: chứa cartId, userId, productId, quantity, selected, product
- CartCombo: chứa cartComboId, userId, comboId, quantity, selected, combo

### 2. CartController
**Nhiệm vụ:**
- Tiếp nhận yêu cầu từ người dùng (thêm, xóa, cập nhật, chọn sản phẩm)
- Xác thực cơ bản: Kiểm tra dữ liệu có hợp lệ không
- Lấy dữ liệu từ request (productId, quantity, ...)
- Gọi CartService để xử lý logic
- Trả về phản hồi (JSON hoặc redirect)

### 3. CartService
**Nhiệm vụ:**
- Nhận yêu cầu từ Controller
- Xử lý logic nghiệp vụ:
  - Kiểm tra sản phẩm có tồn tại không
  - Kiểm tra số lượng có hợp lệ không
  - Kiểm tra tồn kho
  - Tính toán tổng tiền
- Điều phối CartDAO, ProductDAO để lấy/lưu dữ liệu

### 4. CartDAO
**Nhiệm vụ:**
- Nhận yêu cầu từ Service
- Thực thi các câu lệnh SQL: getCartByUserId(), addToCart(), updateCart(), removeFromCart(), ...
- Chuyển đổi ResultSet thành đối tượng CartItem

---

## B. Luồng Hoạt Động

### 1️⃣ Luồng Xem Giỏ Hàng (View Cart)

```
1. Người dùng truy cập trang giỏ hàng (/cart)
   ↓
2. CartController.doGet() kiểm tra:
   - Người dùng đã đăng nhập chưa?
   ↓
3. CartController gọi CartService.getCartByUserId(userId)
   ↓
4. CartService gọi CartDAO.getCartByUserId(userId)
   ↓
5. CartDAO thực thi SQL SELECT từ bảng cart
   ↓
6. CartDAO chuyển đổi ResultSet thành danh sách CartItem
   ↓
7. CartService tính toán:
   - Tổng tiền từng sản phẩm (quantity * price)
   - Tổng tiền giỏ hàng
   ↓
8. CartController gửi dữ liệu đến cart.jsp
   ↓
9. cart.jsp hiển thị:
   - Danh sách sản phẩm trong giỏ
   - Số lượng, giá, tổng tiền
   - Nút cập nhật, xóa, thanh toán
```

### 2️⃣ Luồng Thêm Vào Giỏ Hàng (Add to Cart)

```
1. Người dùng xem sản phẩm trên trang product.jsp
   ↓
2. Người dùng nhập số lượng và nhấn "Thêm vào giỏ"
   ↓
3. Request được gửi đến CartController (/cart/add)
   ↓
4. CartController kiểm tra:
   - Người dùng đã đăng nhập chưa?
   - productId có hợp lệ không?
   - quantity > 0 không?
   ↓
5. CartController gọi CartService.addToCart(userId, productId, quantity)
   ↓
6. CartService kiểm tra:
   - Sản phẩm có tồn tại không?
   - Tồn kho có đủ không?
   - Sản phẩm đã có trong giỏ chưa?
   ↓
7. Nếu sản phẩm đã có:
   - CartService gọi CartDAO.updateCart() để tăng quantity
   Nếu sản phẩm chưa có:
   - CartService gọi CartDAO.addToCart() để thêm mới
   ↓
8. CartDAO thực thi SQL INSERT hoặc UPDATE
   ↓
9. CartController trả về JSON response:
   {
     "success": true,
     "message": "Đã thêm vào giỏ hàng",
     "cartCount": 5
   }
```

### 3️⃣ Luồng Cập Nhật Giỏ Hàng (Update Cart)

```
1. Người dùng thay đổi số lượng sản phẩm trên trang giỏ hàng
   ↓
2. Người dùng nhấn nút "Cập nhật"
   ↓
3. Request được gửi đến CartController (/cart/update)
   ↓
4. CartController kiểm tra:
   - cartId có hợp lệ không?
   - quantity > 0 không?
   ↓
5. CartController gọi CartService.updateCart(cartId, quantity)
   ↓
6. CartService kiểm tra:
   - Tồn kho có đủ không?
   ↓
7. CartService gọi CartDAO.updateCart(cartId, quantity)
   ↓
8. CartDAO thực thi SQL UPDATE bảng cart
   ↓
9. CartController trả về JSON response với tổng tiền mới
```

### 4️⃣ Luồng Xóa Khỏi Giỏ Hàng (Remove from Cart)

```
1. Người dùng nhấn nút "Xóa" trên sản phẩm
   ↓
2. Request được gửi đến CartController (/cart/remove)
   ↓
3. CartController kiểm tra:
   - cartId có hợp lệ không?
   ↓
4. CartController gọi CartService.removeFromCart(cartId)
   ↓
5. CartService gọi CartDAO.removeFromCart(cartId)
   ↓
6. CartDAO thực thi SQL DELETE từ bảng cart
   ↓
7. CartController trả về JSON response
```

### 5️⃣ Luồng Chọn Sản Phẩm Thanh Toán (Select Item)

```
1. Người dùng tick chọn sản phẩm cần thanh toán
   ↓
2. Request được gửi đến CartController (/cart/select)
   ↓
3. CartController gọi CartService.selectCartItem(cartId, selected)
   ↓
4. CartService gọi CartDAO.updateCartItemSelection(cartId, selected)
   ↓
5. CartDAO thực thi SQL UPDATE cột "selected" trong bảng cart
   ↓
6. CartController trả về JSON response với tổng tiền sản phẩm được chọn
```

---

## C. Các Endpoint

| Endpoint | Method | Mô Tả |
|----------|--------|-------|
| `/cart` | GET | Hiển thị giỏ hàng |
| `/cart/add` | POST | Thêm sản phẩm vào giỏ |
| `/cart/update` | POST | Cập nhật số lượng |
| `/cart/remove` | POST | Xóa sản phẩm khỏi giỏ |
| `/cart/select` | POST | Chọn sản phẩm thanh toán |
| `/cart/totals` | POST | Lấy tổng tiền |
| `/cart/count` | POST | Lấy số lượng sản phẩm |
| `/cart/remove-combo` | POST | Xóa combo khỏi giỏ |
| `/cart/select-combo` | POST | Chọn combo thanh toán |

---

## D. Dữ Liệu Lưu Trong Database

**Bảng cart:**
```sql
cart_id | user_id | product_id | quantity | selected
```

**Bảng cart_combos:**
```sql
cart_combo_id | user_id | combo_id | quantity | selected
```

---

## E. Tính Năng Đặc Biệt

### Tính Toán Tổng Tiền
```
Tổng tiền = Σ(quantity * price) cho các sản phẩm được chọn
```

### Kiểm Tra Tồn Kho
```
Nếu quantity > stock_quantity → Lỗi
```

### Combo trong Giỏ
- Giỏ hàng hỗ trợ cả sản phẩm lẻ và combo
- Mỗi combo chứa nhiều sản phẩm
- Tính toán tổng tiền dựa trên combo_price

