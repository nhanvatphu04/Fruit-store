# 🎟️ Luồng Hoạt Động: Mã Giảm Giá (Discount)

## A. Vai Trò Của Các Lớp

### 1. Model Discount & DiscountUsage
- Discount: Đại diện cho mã giảm giá (discountId, code, discountType, discountValue, minOrderAmount, ...)
- DiscountUsage: Ghi nhận sử dụng mã giảm giá (usageId, discountId, userId, orderId, discountCode, discountAmount, ...)

### 2. DiscountApiController
**Nhiệm vụ:**
- Tiếp nhận yêu cầu từ khách hàng (áp dụng mã giảm giá)
- Xác thực cơ bản: Kiểm tra mã có bị trống không
- Lấy mã giảm giá từ request
- Gọi DiscountService để xác thực
- Trả về JSON response

### 3. DiscountService
**Nhiệm vụ:**
- Nhận yêu cầu từ Controller
- Xử lý logic nghiệp vụ:
  - Kiểm tra mã giảm giá có tồn tại không
  - Kiểm tra mã có còn hiệu lực không
  - Kiểm tra tổng tiền có đạt minimum không
  - Kiểm tra người dùng đã sử dụng mã chưa
  - Tính toán số tiền giảm
- Điều phối DiscountDAO, DiscountUsageDAO

### 4. DiscountDAO & DiscountUsageDAO
**Nhiệm vụ:**
- Nhận yêu cầu từ Service
- Thực thi SQL: getDiscountByCode(), addDiscountUsage(), updateOrderIdByCodeAndUser(), ...
- Chuyển đổi ResultSet thành đối tượng Discount/DiscountUsage

---

## B. Luồng Hoạt Động

### 1️⃣ Luồng Áp Dụng Mã Giảm Giá

```
1. Khách hàng xem giỏ hàng (/cart)
   ↓
2. Khách hàng nhập mã giảm giá vào input
   ↓
3. Khách hàng nhấn nút "Áp dụng"
   ↓
4. Request được gửi đến DiscountApiController (/api/discount/apply) - POST
   ↓
5. DiscountApiController kiểm tra:
   - Người dùng đã đăng nhập chưa?
   - Mã giảm giá có bị trống không?
   ↓
6. DiscountApiController lấy tham số:
   - code (mã giảm giá)
   - subtotal (tổng tiền giỏ hàng)
   ↓
7. DiscountApiController gọi DiscountService.validateAndApplyDiscount(code, userId, subtotal)
   ↓
8. DiscountService gọi DiscountDAO.getDiscountByCode(code)
   ↓
9. DiscountDAO thực thi SQL SELECT từ bảng discounts
   ↓
10. DiscountService kiểm tra:
    - Mã có tồn tại không?
    - Mã có còn hiệu lực không (is_active = true)?
    - Ngày hiện tại có nằm trong khoảng start_date - end_date không?
    - Tổng tiền có đạt min_order_amount không?
    - Số lần sử dụng có vượt quá usage_limit không?
    ↓
11. Nếu tất cả kiểm tra hợp lệ:
    - DiscountService tính toán số tiền giảm:
      * Nếu discount_type = "percentage":
        discountAmount = subtotal * (discount_value / 100)
      * Nếu discount_type = "fixed_amount":
        discountAmount = discount_value
      * Nếu discountAmount > max_discount_amount:
        discountAmount = max_discount_amount
    ↓
12. DiscountService gọi DiscountUsageDAO.addDiscountUsage()
    - Ghi nhận sử dụng mã (chưa liên kết với order)
    ↓
13. DiscountApiController lưu vào session:
    - session.setAttribute("appliedDiscount", code)
    - session.setAttribute("cartDiscount", discountAmount)
    ↓
14. DiscountApiController trả về JSON response:
    {
      "success": true,
      "message": "Áp dụng mã giảm giá thành công",
      "discountAmount": 50000,
      "newTotal": 450000
    }
    ↓
15. JavaScript cập nhật giao diện:
    - Hiển thị số tiền giảm
    - Cập nhật tổng tiền
    - Vô hiệu hóa input mã giảm giá
    ↓
    Nếu kiểm tra thất bại:
    - DiscountApiController trả về JSON response:
      {
        "success": false,
        "message": "Mã giảm giá không hợp lệ"
      }
    - JavaScript hiển thị lỗi
```

### 2️⃣ Luồng Xóa Mã Giảm Giá

```
1. Khách hàng nhấn nút "Xóa" mã giảm giá
   ↓
2. Request được gửi đến DiscountApiController (/api/discount/remove) - POST
   ↓
3. DiscountApiController xóa từ session:
   - session.removeAttribute("appliedDiscount")
   - session.removeAttribute("cartDiscount")
   ↓
4. DiscountApiController trả về JSON response:
    {
      "success": true,
      "message": "Xóa mã giảm giá thành công"
    }
    ↓
5. JavaScript cập nhật giao diện:
    - Xóa hiển thị số tiền giảm
    - Cập nhật tổng tiền về giá gốc
    - Kích hoạt lại input mã giảm giá
```

### 3️⃣ Luồng Liên Kết Mã Giảm Giá Với Đơn Hàng

```
1. Khách hàng hoàn thành checkout
   ↓
2. CheckoutController tạo đơn hàng mới
   ↓
3. CheckoutController lấy từ session:
   - appliedDiscount (mã giảm giá)
   - cartDiscount (số tiền giảm)
   ↓
4. CheckoutController tạo Order:
   Order order = new Order(
     userId,
     total,
     "pending",
     appliedDiscountCode,  // Lưu mã giảm giá
     discountAmount        // Lưu số tiền giảm
   )
   ↓
5. CheckoutController gọi OrderDAO.addOrder(order)
   ↓
6. OrderDAO thực thi SQL INSERT vào bảng orders
   ↓
7. CheckoutController lấy orderId vừa tạo
   ↓
8. Nếu có mã giảm giá:
   - CheckoutController gọi DiscountUsageDAO.updateOrderIdByCodeAndUser()
   - Cập nhật discount_usage.order_id = orderId
   - Cập nhật discounts.used_count += 1
   ↓
9. CheckoutController xóa từ session:
   - session.removeAttribute("appliedDiscount")
   - session.removeAttribute("cartDiscount")
```

---

## C. Các Endpoint

| Endpoint | Method | Mô Tả |
|----------|--------|-------|
| `/api/discount/apply` | POST | Áp dụng mã giảm giá |
| `/api/discount/remove` | POST | Xóa mã giảm giá |

---

## D. Dữ Liệu Lưu Trong Database

**Bảng discounts:**
```sql
discount_id | code | description | discount_type | discount_value | 
min_order_amount | max_discount_amount | start_date | end_date | 
usage_limit | used_count | is_active
```

**Bảng discount_usage:**
```sql
usage_id | discount_id | user_id | order_id | discount_code | 
discount_amount | used_at
```

---

## E. Dữ Liệu Lưu Trong Session

```java
session.setAttribute("appliedDiscount", code);      // Mã giảm giá
session.setAttribute("cartDiscount", amount);       // Số tiền giảm
```

---

## F. Loại Mã Giảm Giá

| Loại | Mô Tả | Ví Dụ |
|------|-------|-------|
| **percentage** | Giảm theo phần trăm | 10% = 0.1 |
| **fixed_amount** | Giảm số tiền cố định | 50,000 VND |

---

## G. Xác Thực Mã Giảm Giá

### Kiểm Tra Tồn Tại
```
- Mã có tồn tại trong database không?
```

### Kiểm Tra Hiệu Lực
```
- is_active = true?
- Ngày hiện tại >= start_date?
- Ngày hiện tại <= end_date?
```

### Kiểm Tra Tổng Tiền
```
- subtotal >= min_order_amount?
```

### Kiểm Tra Lượt Sử Dụng
```
- used_count < usage_limit?
```

### Kiểm Tra Người Dùng
```
- Người dùng đã sử dụng mã này chưa?
```

---

## H. Tính Toán Số Tiền Giảm

### Loại Percentage
```
discountAmount = subtotal * (discount_value / 100)
Nếu discountAmount > max_discount_amount:
  discountAmount = max_discount_amount
```

### Loại Fixed Amount
```
discountAmount = discount_value
Nếu discountAmount > max_discount_amount:
  discountAmount = max_discount_amount
```

### Tổng Tiền Cuối Cùng
```
finalTotal = subtotal - discountAmount
```

