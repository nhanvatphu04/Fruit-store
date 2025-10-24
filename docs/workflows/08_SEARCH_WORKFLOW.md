# 🔍 Luồng Hoạt Động: Tìm Kiếm (Search)

## A. Vai Trò Của Các Lớp

### 1. SearchController
**Nhiệm vụ:**
- Tiếp nhận yêu cầu từ khách hàng (tìm kiếm sản phẩm)
- Lấy từ khóa tìm kiếm từ request
- Gọi ProductService để tìm kiếm
- Trả về phản hồi: Hiển thị kết quả tìm kiếm

### 2. SearchApiController
**Nhiệm vụ:**
- Tiếp nhận yêu cầu AJAX từ khách hàng (autocomplete)
- Lấy từ khóa tìm kiếm từ request
- Gọi ProductService để tìm kiếm
- Trả về JSON response với danh sách gợi ý

### 3. ProductService
**Nhiệm vụ:**
- Nhận yêu cầu từ Controller
- Xử lý logic tìm kiếm:
  - Tìm kiếm theo tên sản phẩm
  - Tìm kiếm theo mô tả
  - Sắp xếp kết quả
- Điều phối ProductDAO

### 4. ProductDAO
**Nhiệm vụ:**
- Nhận yêu cầu từ Service
- Thực thi SQL: searchProducts(), searchProductsByName(), ...
- Chuyển đổi ResultSet thành danh sách Product

---

## B. Luồng Hoạt Động

### 1️⃣ Luồng Tìm Kiếm Cơ Bản (Search)

```
1. Khách hàng truy cập trang sản phẩm (/products)
   ↓
2. Khách hàng nhập từ khóa tìm kiếm vào input
   ↓
3. Khách hàng nhấn nút "Tìm kiếm" hoặc Enter
   ↓
4. Request được gửi đến ProductController (/products?q=keyword)
   ↓
5. ProductController lấy tham số:
   - q (từ khóa tìm kiếm)
   - page (trang)
   ↓
6. ProductController kiểm tra:
   - Từ khóa có bị trống không?
   ↓
7. ProductController gọi ProductService.searchProducts(keyword)
   ↓
8. ProductService gọi ProductDAO.searchProducts(keyword)
   ↓
9. ProductDAO thực thi SQL SELECT:
   SELECT * FROM products
   WHERE name LIKE '%keyword%'
      OR description LIKE '%keyword%'
   ↓
10. ProductDAO chuyển đổi ResultSet thành danh sách Product
    ↓
11. ProductController gửi dữ liệu đến products.jsp:
    - Danh sách sản phẩm tìm được
    - Số lượng kết quả
    - Từ khóa tìm kiếm
    ↓
12. products.jsp hiển thị:
    - Kết quả tìm kiếm dạng grid
    - Số lượng sản phẩm tìm được
    - Thông báo nếu không tìm thấy
```

### 2️⃣ Luồng Autocomplete (Gợi Ý Tìm Kiếm)

```
1. Khách hàng nhập từ khóa vào input tìm kiếm
   ↓
2. Sự kiện "input" được kích hoạt
   ↓
3. JavaScript gửi AJAX request đến SearchApiController (/api/search/autocomplete)
   ↓
4. SearchApiController lấy tham số:
   - q (từ khóa tìm kiếm)
   ↓
5. SearchApiController kiểm tra:
   - Từ khóa có bị trống không?
   - Độ dài từ khóa >= 2 ký tự không?
   ↓
6. SearchApiController gọi ProductService.searchProducts(keyword)
   ↓
7. ProductService gọi ProductDAO.searchProducts(keyword)
   ↓
8. ProductDAO thực thi SQL SELECT:
   SELECT * FROM products
   WHERE name LIKE '%keyword%'
   LIMIT 10
   ↓
9. ProductDAO chuyển đổi ResultSet thành danh sách Product
   ↓
10. SearchApiController trả về JSON response:
    {
      "success": true,
      "suggestions": [
        {
          "productId": 1,
          "name": "Táo đỏ",
          "imageUrl": "..."
        },
        ...
      ]
    }
    ↓
11. JavaScript hiển thị danh sách gợi ý dưới input
    ↓
12. Khách hàng nhấn vào gợi ý
    ↓
13. JavaScript điều hướng đến trang chi tiết sản phẩm
```

### 3️⃣ Luồng Tìm Kiếm Nâng Cao (Advanced Search)

```
1. Khách hàng truy cập trang sản phẩm
   ↓
2. Khách hàng sử dụng bộ lọc:
   - Danh mục
   - Khoảng giá
   - Sắp xếp
   ↓
3. Request được gửi đến ProductController (/products?category=...&sort=...)
   ↓
4. ProductController lấy tham số:
   - category (danh mục)
   - sort (sắp xếp)
   - filter (bộ lọc)
   ↓
5. ProductController gọi ProductService với điều kiện phù hợp:
   - Nếu có category: getProductsByCategory(categorySlug)
   - Nếu có filter: getProductsByFilter(filter)
   - Nếu có sort: sắp xếp kết quả
   ↓
6. ProductService gọi ProductDAO với SQL phù hợp
   ↓
7. ProductDAO thực thi SQL SELECT với WHERE/ORDER BY
   ↓
8. ProductDAO chuyển đổi ResultSet thành danh sách Product
   ↓
9. ProductController gửi dữ liệu đến products.jsp
   ↓
10. products.jsp hiển thị kết quả tìm kiếm nâng cao
```

---

## C. Các Endpoint

| Endpoint | Method | Mô Tả |
|----------|--------|-------|
| `/products?q=keyword` | GET | Tìm kiếm sản phẩm |
| `/api/search/autocomplete` | POST | Gợi ý tìm kiếm (autocomplete) |
| `/products?category=...` | GET | Lọc theo danh mục |
| `/products?filter=...` | GET | Lọc theo bộ lọc |
| `/products?sort=...` | GET | Sắp xếp sản phẩm |

---

## D. Tham Số Tìm Kiếm

| Tham Số | Mô Tả | Ví Dụ |
|---------|-------|-------|
| `q` | Từ khóa tìm kiếm | `q=táo` |
| `category` | Danh mục | `category=fruits` |
| `filter` | Bộ lọc | `filter=best-selling` |
| `sort` | Sắp xếp | `sort=price-asc` |
| `page` | Trang | `page=2` |

---

## E. Bộ Lọc Sản Phẩm

| Bộ Lọc | Mô Tả |
|--------|-------|
| **best-selling** | Sản phẩm bán chạy (is_best_seller = true) |
| **new** | Sản phẩm mới (is_new = true) |

---

## F. Sắp Xếp Sản Phẩm

| Sắp Xếp | Mô Tả |
|---------|-------|
| **price-asc** | Giá tăng dần |
| **price-desc** | Giá giảm dần |
| **newest** | Mới nhất |
| **popular** | Phổ biến nhất |

---

## G. Xác Thực Dữ Liệu

### Kiểm Tra Từ Khóa
```
- Từ khóa không được bị trống
- Độ dài từ khóa >= 2 ký tự (cho autocomplete)
```

### Kiểm Tra Tham Số
```
- category phải tồn tại trong database
- sort phải là một trong các giá trị hợp lệ
- filter phải là một trong các giá trị hợp lệ
- page phải là số nguyên dương
```

---

## H. Tính Năng Đặc Biệt

### Tìm Kiếm Toàn Văn
```
Tìm kiếm trong:
- Tên sản phẩm (name)
- Mô tả sản phẩm (description)
```

### Autocomplete
```
- Gợi ý tối đa 10 sản phẩm
- Cập nhật khi người dùng nhập
- Hiển thị hình ảnh và tên sản phẩm
```

### Phân Trang
```
- Hiển thị 9 sản phẩm mỗi trang
- Hỗ trợ điều hướng trang
```

### Caching
```
- Có thể cache kết quả tìm kiếm phổ biến
- Giảm tải database
```

