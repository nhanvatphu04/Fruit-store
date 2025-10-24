# 📦 Luồng Hoạt Động: Quản Lý Sản Phẩm (Product Management)

## A. Vai Trò Của Các Lớp

### 1. Model Product & Category
- Product: Đại diện cho sản phẩm (productId, name, description, price, stockQuantity, imageUrl, categoryId, ...)
- Category: Đại diện cho danh mục (categoryId, name, description, imageUrl, slug, ...)

### 2. ProductController (Customer)
**Nhiệm vụ:**
- Tiếp nhận yêu cầu từ khách hàng (xem danh sách, tìm kiếm, lọc, xem chi tiết)
- Lấy tham số từ request (category, search, filter, sort, page)
- Gọi ProductService để lấy dữ liệu
- Trả về phản hồi: Hiển thị danh sách sản phẩm

### 3. AdminProductController (Admin)
**Nhiệm vụ:**
- Tiếp nhận yêu cầu từ admin (thêm, sửa, xóa sản phẩm)
- Xác thực quyền admin
- Lấy dữ liệu từ request
- Gọi ProductService để xử lý
- Trả về JSON response

### 4. ProductService
**Nhiệm vụ:**
- Nhận yêu cầu từ Controller
- Xử lý logic nghiệp vụ:
  - Kiểm tra tên sản phẩm có trùng không
  - Kiểm tra dữ liệu hợp lệ
  - Tính toán giá sau giảm giá
- Điều phối ProductDAO, CategoryDAO

### 5. ProductDAO
**Nhiệm vụ:**
- Nhận yêu cầu từ Service
- Thực thi SQL: getAll(), getById(), search(), filter(), add(), update(), delete(), ...
- Chuyển đổi ResultSet thành đối tượng Product

---

## B. Luồng Hoạt Động

### 1️⃣ Luồng Xem Danh Sách Sản Phẩm (Customer)

```
1. Khách hàng truy cập trang sản phẩm (/products)
   ↓
2. ProductController.doGet() lấy tham số:
   - category (danh mục)
   - q (tìm kiếm)
   - filter (bộ lọc: best-selling, new)
   - sort (sắp xếp: price-asc, price-desc)
   - page (trang)
   ↓
3. ProductController gọi CategoryService.getAllCategories()
   ↓
4. Dựa vào tham số, ProductController gọi:
   - Nếu có category: ProductService.getProductsByCategory(categorySlug)
   - Nếu có search: ProductService.searchProducts(query)
   - Nếu có filter: ProductService.getProductsByFilter(filter)
   - Nếu không: ProductService.getAllProducts()
   ↓
5. ProductService gọi ProductDAO để lấy dữ liệu
   ↓
6. ProductDAO thực thi SQL SELECT với điều kiện phù hợp
   ↓
7. ProductDAO chuyển đổi ResultSet thành danh sách Product
   ↓
8. ProductController gửi dữ liệu đến products.jsp:
   - Danh sách sản phẩm
   - Danh sách danh mục
   - Thông tin phân trang
   ↓
9. products.jsp hiển thị:
   - Danh sách sản phẩm dạng grid
   - Bộ lọc, tìm kiếm
   - Phân trang
```

### 2️⃣ Luồng Xem Chi Tiết Sản Phẩm

```
1. Khách hàng nhấn vào sản phẩm
   ↓
2. Request được gửi đến ProductController (/products?id=123)
   ↓
3. ProductController kiểm tra:
   - productId có hợp lệ không?
   ↓
4. ProductController gọi ProductService.getProductById(productId)
   ↓
5. ProductService gọi ProductDAO.getProductById(productId)
   ↓
6. ProductDAO thực thi SQL SELECT từ bảng products
   ↓
7. ProductDAO chuyển đổi ResultSet thành đối tượng Product
   ↓
8. ProductController gửi dữ liệu đến product.jsp
   ↓
9. product.jsp hiển thị:
   - Hình ảnh sản phẩm
   - Tên, mô tả, giá
   - Tồn kho
   - Nút "Thêm vào giỏ"
```

### 3️⃣ Luồng Thêm Sản Phẩm (Admin)

```
1. Admin truy cập trang quản lý sản phẩm (/admin/products)
   ↓
2. Admin nhấn nút "Thêm sản phẩm"
   ↓
3. Hiển thị form thêm sản phẩm
   ↓
4. Admin điền thông tin:
   - name, description, price, stockQuantity, imageUrl, categoryId, ...
   ↓
5. Admin nhấn nút "Thêm"
   ↓
6. Request được gửi đến AdminProductController (/admin/products/add) - POST
   ↓
7. AdminProductController kiểm tra:
   - Người dùng là admin không?
   - Dữ liệu có bị trống không?
   ↓
8. AdminProductController tạo đối tượng Product từ request
   ↓
9. AdminProductController gọi ProductService.addProduct(product)
   ↓
10. ProductService kiểm tra:
    - Tên sản phẩm có trùng không?
    - Dữ liệu có hợp lệ không?
    ↓
11. ProductService gọi ProductDAO.addProduct(product)
    ↓
12. ProductDAO thực thi SQL INSERT vào bảng products
    ↓
13. AdminProductController trả về JSON response:
    {
      "success": true,
      "message": "Thêm sản phẩm thành công"
    }
```

### 4️⃣ Luồng Sửa Sản Phẩm (Admin)

```
1. Admin truy cập trang quản lý sản phẩm
   ↓
2. Admin nhấn nút "Sửa" trên sản phẩm
   ↓
3. Hiển thị form sửa sản phẩm với dữ liệu cũ
   ↓
4. Admin thay đổi thông tin
   ↓
5. Admin nhấn nút "Cập nhật"
   ↓
6. Request được gửi đến AdminProductController (/admin/products/edit) - POST
   ↓
7. AdminProductController kiểm tra:
   - Người dùng là admin không?
   - productId có hợp lệ không?
   ↓
8. AdminProductController tạo đối tượng Product từ request
   ↓
9. AdminProductController gọi ProductService.updateProduct(product)
   ↓
10. ProductService kiểm tra dữ liệu hợp lệ
    ↓
11. ProductService gọi ProductDAO.updateProduct(product)
    ↓
12. ProductDAO thực thi SQL UPDATE bảng products
    ↓
13. AdminProductController trả về JSON response
```

### 5️⃣ Luồng Xóa Sản Phẩm (Admin)

```
1. Admin nhấn nút "Xóa" trên sản phẩm
   ↓
2. Hiển thị xác nhận xóa
   ↓
3. Admin xác nhận
   ↓
4. Request được gửi đến AdminProductController (/admin/products/delete) - POST
   ↓
5. AdminProductController kiểm tra:
   - Người dùng là admin không?
   - productId có hợp lệ không?
   ↓
6. AdminProductController gọi ProductService.deleteProduct(productId)
   ↓
7. ProductService gọi ProductDAO.deleteProduct(productId)
   ↓
8. ProductDAO thực thi SQL DELETE từ bảng products
   ↓
9. AdminProductController trả về JSON response
```

---

## C. Các Endpoint

| Endpoint | Method | Mô Tả |
|----------|--------|-------|
| `/products` | GET | Xem danh sách sản phẩm |
| `/products?id=123` | GET | Xem chi tiết sản phẩm |
| `/admin/products` | GET | Quản lý sản phẩm (admin) |
| `/admin/products/add` | POST | Thêm sản phẩm (admin) |
| `/admin/products/edit` | POST | Sửa sản phẩm (admin) |
| `/admin/products/delete` | POST | Xóa sản phẩm (admin) |

---

## D. Tính Năng Đặc Biệt

### Tìm Kiếm Sản Phẩm
- Tìm kiếm theo tên sản phẩm
- Hỗ trợ autocomplete

### Lọc Sản Phẩm
- Lọc theo danh mục
- Lọc sản phẩm bán chạy (is_best_seller = true)
- Lọc sản phẩm mới (is_new = true)

### Sắp Xếp Sản Phẩm
- Sắp xếp theo giá (tăng/giảm)
- Sắp xếp theo ngày tạo

### Phân Trang
- Hiển thị 9 sản phẩm mỗi trang
- Hỗ trợ điều hướng trang

