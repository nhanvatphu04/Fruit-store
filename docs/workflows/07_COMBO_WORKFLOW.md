# 🎁 Luồng Hoạt Động: Combo (Flash Sale)

## A. Vai Trò Của Các Lớp

### 1. Model Combo & ComboItem
- Combo: Đại diện cho combo/flash sale (comboId, name, description, originalPrice, comboPrice, imageUrl, startDate, endDate, isActive, ...)
- ComboItem: Đại diện cho sản phẩm trong combo (comboItemId, comboId, productId, quantity, ...)

### 2. ComboController (Customer)
**Nhiệm vụ:**
- Tiếp nhận yêu cầu từ khách hàng (xem danh sách combo, xem chi tiết)
- Lấy dữ liệu từ request
- Gọi ComboService để lấy dữ liệu
- Trả về phản hồi: Hiển thị combo

### 3. AdminComboController (Admin)
**Nhiệm vụ:**
- Tiếp nhận yêu cầu từ admin (thêm, sửa, xóa combo)
- Xác thực quyền admin
- Lấy dữ liệu từ request
- Gọi ComboService để xử lý
- Trả về JSON response

### 4. ComboService
**Nhiệm vụ:**
- Nhận yêu cầu từ Controller
- Xử lý logic nghiệp vụ:
  - Kiểm tra combo có hợp lệ không
  - Kiểm tra combo có đang hoạt động không
  - Tính toán giá tiết kiệm
- Điều phối ComboDAO, ComboItemDAO

### 5. ComboDAO & ComboItemDAO
**Nhiệm vụ:**
- Nhận yêu cầu từ Service
- Thực thi SQL: getAll(), getById(), getActiveCombo(), add(), update(), delete(), ...
- Chuyển đổi ResultSet thành đối tượng Combo/ComboItem

---

## B. Luồng Hoạt Động

### 1️⃣ Luồng Xem Combo Trên Trang Home

```
1. Khách hàng truy cập trang home (/)
   ↓
2. HomeController.doGet() gọi ComboService.getActiveCombo()
   ↓
3. ComboService gọi ComboDAO.getActiveCombo()
   ↓
4. ComboDAO thực thi SQL SELECT từ bảng combos:
   - WHERE is_active = true
   - AND start_date <= NOW()
   - AND end_date >= NOW()
   ↓
5. ComboDAO chuyển đổi ResultSet thành danh sách Combo
   ↓
6. Với mỗi Combo:
   - ComboDAO gọi loadComboItems(combo)
   - Lấy danh sách ComboItem từ bảng combo_items
   - Gán vào combo.comboItems
   ↓
7. HomeController gửi dữ liệu đến home.jsp
   ↓
8. home.jsp hiển thị:
   - Danh sách combo đang hoạt động
   - Hình ảnh, tên, giá gốc, giá combo
   - Nút "Thêm vào giỏ"
```

### 2️⃣ Luồng Xem Chi Tiết Combo

```
1. Khách hàng nhấn vào combo
   ↓
2. Request được gửi đến ComboController (/combo?id=123)
   ↓
3. ComboController kiểm tra:
   - comboId có hợp lệ không?
   ↓
4. ComboController gọi ComboService.getComboById(comboId)
   ↓
5. ComboService gọi ComboDAO.getComboById(comboId)
   ↓
6. ComboDAO thực thi SQL SELECT từ bảng combos
   ↓
7. ComboDAO gọi loadComboItems(combo)
   ↓
8. ComboController gửi dữ liệu đến combo.jsp
   ↓
9. combo.jsp hiển thị:
   - Thông tin combo chi tiết
   - Danh sách sản phẩm trong combo
   - Giá gốc, giá combo, tiền tiết kiệm
   - Nút "Thêm vào giỏ"
```

### 3️⃣ Luồng Thêm Combo Vào Giỏ Hàng

```
1. Khách hàng xem combo
   ↓
2. Khách hàng nhập số lượng combo
   ↓
3. Khách hàng nhấn "Thêm vào giỏ"
   ↓
4. Request được gửi đến CartController (/cart/add) - POST
   ↓
5. CartController kiểm tra:
   - Người dùng đã đăng nhập chưa?
   - comboId có hợp lệ không?
   - quantity > 0 không?
   ↓
6. CartController gọi CartService.addComboToCart(userId, comboId, quantity)
   ↓
7. CartService kiểm tra:
   - Combo có tồn tại không?
   - Combo có đang hoạt động không?
   - Combo đã có trong giỏ chưa?
   ↓
8. Nếu combo đã có:
   - CartService gọi CartComboDAO.updateCartCombo() để tăng quantity
   Nếu combo chưa có:
   - CartService gọi CartComboDAO.addToCartCombo() để thêm mới
   ↓
9. CartComboDAO thực thi SQL INSERT hoặc UPDATE
   ↓
10. CartController trả về JSON response:
    {
      "success": true,
      "message": "Đã thêm combo vào giỏ hàng",
      "cartCount": 3
    }
```

### 4️⃣ Luồng Thêm Combo (Admin)

```
1. Admin truy cập trang quản lý combo (/admin/combos)
   ↓
2. Admin nhấn nút "Thêm combo"
   ↓
3. Hiển thị form thêm combo
   ↓
4. Admin điền thông tin:
   - name, description, originalPrice, comboPrice
   - imageUrl, startDate, endDate
   - Chọn sản phẩm và số lượng
   ↓
5. Admin nhấn nút "Thêm"
   ↓
6. Request được gửi đến AdminComboController (/admin/combos/add) - POST
   ↓
7. AdminComboController kiểm tra:
   - Người dùng là admin không?
   - Dữ liệu có bị trống không?
   ↓
8. AdminComboController tạo đối tượng Combo từ request
   ↓
9. AdminComboController gọi ComboService.addCombo(combo)
   ↓
10. ComboService kiểm tra:
    - Tên combo có trùng không?
    - Dữ liệu có hợp lệ không?
    ↓
11. ComboService gọi ComboDAO.addCombo(combo)
    ↓
12. ComboDAO thực thi SQL INSERT vào bảng combos
    ↓
13. ComboDAO lấy comboId vừa tạo
    ↓
14. Với mỗi sản phẩm được chọn:
    - Tạo đối tượng ComboItem
    - Gọi ComboItemDAO.addComboItem(comboItem)
    - ComboItemDAO thực thi SQL INSERT vào bảng combo_items
    ↓
15. AdminComboController trả về JSON response:
    {
      "success": true,
      "message": "Thêm combo thành công"
    }
```

### 5️⃣ Luồng Sửa Combo (Admin)

```
1. Admin nhấn nút "Sửa" trên combo
   ↓
2. Hiển thị form sửa combo với dữ liệu cũ
   ↓
3. Admin thay đổi thông tin
   ↓
4. Admin nhấn nút "Cập nhật"
   ↓
5. Request được gửi đến AdminComboController (/admin/combos/edit) - POST
   ↓
6. AdminComboController kiểm tra:
   - Người dùng là admin không?
   - comboId có hợp lệ không?
   ↓
7. AdminComboController tạo đối tượng Combo từ request
   ↓
8. AdminComboController gọi ComboService.updateCombo(combo)
   ↓
9. ComboService kiểm tra dữ liệu hợp lệ
   ↓
10. ComboService gọi ComboDAO.updateCombo(combo)
    ↓
11. ComboDAO thực thi SQL UPDATE bảng combos
    ↓
12. Xóa combo_items cũ:
    - ComboItemDAO.deleteComboItemsByComboId(comboId)
    ↓
13. Thêm combo_items mới:
    - Với mỗi sản phẩm được chọn:
      - Tạo đối tượng ComboItem
      - Gọi ComboItemDAO.addComboItem(comboItem)
    ↓
14. AdminComboController trả về JSON response
```

### 6️⃣ Luồng Xóa Combo (Admin)

```
1. Admin nhấn nút "Xóa" trên combo
   ↓
2. Hiển thị xác nhận xóa
   ↓
3. Admin xác nhận
   ↓
4. Request được gửi đến AdminComboController (/admin/combos/delete) - POST
   ↓
5. AdminComboController kiểm tra:
   - Người dùng là admin không?
   - comboId có hợp lệ không?
   ↓
6. AdminComboController gọi ComboService.deleteCombo(comboId)
   ↓
7. ComboService gọi ComboDAO.deleteCombo(comboId)
   ↓
8. ComboDAO thực thi SQL DELETE từ bảng combos
   ↓
9. ComboItemDAO thực thi SQL DELETE từ bảng combo_items
   ↓
10. AdminComboController trả về JSON response
```

---

## C. Các Endpoint

| Endpoint | Method | Mô Tả |
|----------|--------|-------|
| `/combo` | GET | Xem danh sách combo |
| `/combo?id=123` | GET | Xem chi tiết combo |
| `/admin/combos` | GET | Quản lý combo (admin) |
| `/admin/combos/add` | POST | Thêm combo (admin) |
| `/admin/combos/edit` | POST | Sửa combo (admin) |
| `/admin/combos/delete` | POST | Xóa combo (admin) |

---

## D. Dữ Liệu Lưu Trong Database

**Bảng combos:**
```sql
combo_id | name | description | original_price | combo_price | 
image_url | start_date | end_date | is_active | created_at
```

**Bảng combo_items:**
```sql
combo_item_id | combo_id | product_id | quantity
```

**Bảng cart_combos:**
```sql
cart_combo_id | user_id | combo_id | quantity | selected
```

---

## E. Tính Năng Đặc Biệt

### Combo Đang Hoạt Động
```
- is_active = true
- start_date <= NOW()
- end_date >= NOW()
```

### Tính Toán Tiền Tiết Kiệm
```
savings = original_price - combo_price
savingsPercent = (savings / original_price) * 100
```

### Combo Trong Giỏ Hàng
- Giỏ hàng hỗ trợ cả sản phẩm lẻ và combo
- Mỗi combo chứa nhiều sản phẩm
- Tính toán tổng tiền dựa trên combo_price

