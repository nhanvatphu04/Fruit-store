-- =============================
-- Seeder cho bảng categories
INSERT INTO categories (name, description, image_url, icon, slug) VALUES
('Trái cây nhập khẩu', 'Các loại trái cây nhập khẩu chất lượng cao', 'assets/images/cat_imported.jpg', 'fas fa-plane-arrival', 'trai-cay-nhap-khau'),
('Trái cây nội địa', 'Trái cây tươi ngon từ các vùng miền Việt Nam', 'assets/images/cat_vn.jpg', 'fas fa-tree', 'trai-cay-noi-dia'),
('Combo tiết kiệm', 'Combo trái cây giá tốt', 'assets/images/cat_combo.jpg', 'fas fa-tags', 'combo-tiet-kiem'),
('Trái cây sấy', 'Các loại trái cây sấy khô', 'assets/images/cat_dried.jpg', 'fas fa-sun', 'trai-cay-say');

-- =============================
-- Seeder cho bảng products
INSERT INTO products (name, description, price, stock_quantity, image_url, category_id, discount_percent, is_new, is_best_seller)
SELECT 'Táo Envy New Zealand', 'Táo Envy nhập khẩu từ New Zealand, giòn ngọt', 120000, 50, 'assets/images/apple_envy.jpg', c.category_id, 0, TRUE, TRUE
FROM categories c WHERE c.name = 'Trái cây nhập khẩu'
UNION ALL
SELECT 'Nho mẫu đơn Nhật', 'Nho mẫu đơn cao cấp, vị ngọt thanh', 350000, 20, 'assets/images/grape_japan.jpg', c.category_id, 10, TRUE, TRUE
FROM categories c WHERE c.name = 'Trái cây nhập khẩu'
UNION ALL
SELECT 'Cam sành miền Tây', 'Cam sành tươi ngon, mọng nước', 40000, 100, 'assets/images/orange_vn.jpg', c.category_id, 0, FALSE, TRUE
FROM categories c WHERE c.name = 'Trái cây nội địa'
UNION ALL
SELECT 'Xoài cát Hòa Lộc', 'Xoài cát Hòa Lộc nổi tiếng miền Tây', 60000, 80, 'assets/images/mango_vn.jpg', c.category_id, 5, FALSE, FALSE
FROM categories c WHERE c.name = 'Trái cây nội địa'
UNION ALL
SELECT 'Mít sấy', 'Mít sấy giòn, thơm ngon', 50000, 40, 'assets/images/jackfruit_dried.jpg', c.category_id, 0, FALSE, FALSE
FROM categories c WHERE c.name = 'Trái cây sấy'
UNION ALL
SELECT 'Dứa mật thơm ngon', 'Dứa mật thơm mọng, ngọt dịu', 45000, 60, 'assets/images/pineapple.jpg', c.category_id, 0, TRUE, FALSE
FROM categories c WHERE c.name = 'Trái cây nội địa'
UNION ALL
SELECT 'Lựu Israel', 'Quả lựu đỏ mọng, chứa nhiều vitamin', 180000, 25, 'assets/images/pomegranate.jpg', c.category_id, 8, FALSE, TRUE
FROM categories c WHERE c.name = 'Trái cây nhập khẩu'
UNION ALL
SELECT 'Chuối tây chín vàng', 'Chuối chín mềm, ngọt tự nhiên', 25000, 120, 'assets/images/banana.jpg', c.category_id, 0, TRUE, FALSE
FROM categories c WHERE c.name = 'Trái cây nội địa'
UNION ALL
SELECT 'Kiwi New Zealand', 'Kiwi xanh nhập khẩu New Zealand, thơm ngon', 90000, 45, 'assets/images/kiwi_nz.jpg', c.category_id, 5, TRUE, FALSE
FROM categories c WHERE c.name = 'Trái cây nhập khẩu'
UNION ALL
SELECT 'Chanh dây vàng', 'Chanh dây vàng chua nhẹ, thơm', 55000, 70, 'assets/images/passionfruit.jpg', c.category_id, 0, FALSE, FALSE
FROM categories c WHERE c.name = 'Trái cây nhập khẩu'
UNION ALL
SELECT 'Dâu tây Đà Lạt', 'Dâu tây Đà Lạt tươi, ngọt nhẹ', 80000, 35, 'assets/images/strawberry_dalat.jpg', c.category_id, 12, TRUE, TRUE
FROM categories c WHERE c.name = 'Trái cây nội địa';

-- =============================
-- Seeder cho bảng combos
INSERT INTO combos (name, description, original_price, combo_price, image_url, start_date, end_date, is_active) VALUES
('Combo Vitamin C', 'Combo gồm cam, quýt, bưởi giúp tăng sức đề kháng', 180000, 150000, 'assets/images/combo_vitc.jpg', NOW(), DATE_ADD(NOW(), INTERVAL 30 DAY), TRUE),
('Combo Trái cây nhập khẩu', 'Combo gồm táo Envy, nho mẫu đơn Nhật', 470000, 420000, 'assets/images/combo_imported.jpg', NOW(), DATE_ADD(NOW(), INTERVAL 30 DAY), TRUE);

-- =============================
-- Seeder cho bảng combo_items (liên kết sản phẩm trong combo)
INSERT INTO combo_items (combo_id, product_id, quantity)
SELECT c.combo_id, p.product_id, 1
FROM combos c JOIN products p 
WHERE c.name = 'Combo Vitamin C' AND p.name IN ('Cam sành miền Tây', 'Xoài cát Hòa Lộc');

INSERT INTO combo_items (combo_id, product_id, quantity)
SELECT c.combo_id, p.product_id, 1
FROM combos c JOIN products p 
WHERE c.name = 'Combo Trái cây nhập khẩu' AND p.name IN ('Táo Envy New Zealand', 'Nho mẫu đơn Nhật');

-- =============================
-- Seeder cho bảng discounts
INSERT INTO discounts (code, description, discount_type, discount_value, min_order_amount, max_discount_amount, start_date, end_date, usage_limit, is_active) VALUES
('WELCOME', 'Giảm 10% cho đơn hàng đầu tiên', 'percentage', 10, 100000, 100000, NOW(), DATE_ADD(NOW(), INTERVAL 90 DAY), 1000, TRUE),
('FRUIT50K', 'Giảm 50K cho đơn từ 300K', 'fixed_amount', 50000, 300000, 50000, NOW(), DATE_ADD(NOW(), INTERVAL 30 DAY), 500, TRUE),
('FRUITVIP', 'Giảm 15% cho thành viên VIP', 'percentage', 15, 500000, 150000, NOW(), DATE_ADD(NOW(), INTERVAL 365 DAY), 100, TRUE),
('WEEKEND', 'Giảm 20K cho đơn cuối tuần', 'fixed_amount', 20000, 200000, 20000, NOW(), DATE_ADD(NOW(), INTERVAL 7 DAY), 200, TRUE),
('SUMMER2025', 'Giảm 20% mừng hè 2025', 'percentage', 20, 400000, 200000, '2025-06-01', '2025-08-31', 1000, TRUE);

-- =============================
-- Seeder cho bảng admin
INSERT INTO `users` (`user_id`, `username`, `email`, `password`, `full_name`, `phone`, `address`, `avatar_url`, `role`, `created_at`, `updated_at`) VALUES
(1, 'admin1', 'admin@fruitstore.com', 'JAvlGPq9JyTdtvBO6x2llnRI1+gxwIyPqCKAn3THIKk=', 'Administrator', '0123456678', 'hn', '/assets/images/avatars/avatar2.png', 'admin', '2025-10-09 11:23:14', '2025-10-09 11:26:34');

-- =============================
-- Seeder cho bảng product_stats (thống kê ban đầu)
INSERT INTO product_stats (product_id, total_sold, last_sold_at)
SELECT product_id, 
       CASE 
           WHEN is_best_seller = TRUE THEN FLOOR(RAND() * 200) + 50 
           ELSE FLOOR(RAND() * 20)
       END AS total_sold,
       NOW()
FROM products;
