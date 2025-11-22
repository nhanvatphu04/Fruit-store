-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th10 22, 2025 lúc 01:15 PM
-- Phiên bản máy phục vụ: 10.4.32-MariaDB
-- Phiên bản PHP: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `fruitstore`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `cart`
--

CREATE TABLE `cart` (
  `cart_id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `product_id` int(11) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `selected` tinyint(1) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `cart_combos`
--

CREATE TABLE `cart_combos` (
  `cart_combo_id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `combo_id` int(11) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `selected` tinyint(1) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `categories`
--

CREATE TABLE `categories` (
  `category_id` int(11) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `description` text DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `icon` varchar(50) DEFAULT NULL,
  `slug` varchar(100) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `categories`
--

INSERT INTO `categories` (`category_id`, `name`, `description`, `image_url`, `icon`, `slug`, `created_at`) VALUES
(1, 'Trái cây nhập khẩu', 'Các loại trái cây nhập khẩu chất lượng cao', 'assets/images/cat_imported.jpg', 'fas fa-plane-arrival', 'trai-cay-nhap-khau', '2025-11-22 12:00:05'),
(2, 'Trái cây nội địa', 'Trái cây tươi ngon từ các vùng miền Việt Nam', 'assets/images/cat_vn.jpg', 'fas fa-tree', 'trai-cay-noi-dia', '2025-11-22 12:00:05'),
(3, 'Combo tiết kiệm', 'Combo trái cây giá tốt', 'assets/images/cat_combo.jpg', 'fas fa-tags', 'combo-tiet-kiem', '2025-11-22 12:00:05'),
(4, 'Trái cây sấy', 'Các loại trái cây sấy khô', 'assets/images/cat_dried.jpg', 'fas fa-sun', 'trai-cay-say', '2025-11-22 12:00:05');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `combos`
--

CREATE TABLE `combos` (
  `combo_id` int(11) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `description` text DEFAULT NULL,
  `original_price` decimal(10,2) DEFAULT NULL,
  `combo_price` decimal(10,2) DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `start_date` timestamp NULL DEFAULT NULL,
  `end_date` timestamp NULL DEFAULT NULL,
  `is_active` tinyint(1) DEFAULT 1,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `combos`
--

INSERT INTO `combos` (`combo_id`, `name`, `description`, `original_price`, `combo_price`, `image_url`, `start_date`, `end_date`, `is_active`, `created_at`) VALUES
(1, 'Combo Vitamin C', 'Combo gồm cam, quýt, bưởi giúp tăng sức đề kháng', 180000.00, 150000.00, 'assets/images/combo_vitc.jpg', '2025-11-20 12:00:05', '2025-12-22 12:00:05', 1, '2025-11-22 12:00:05'),
(2, 'Combo Trái cây nhập khẩu', 'Combo gồm táo Envy, nho mẫu đơn Nhật', 470000.00, 420000.00, 'assets/images/combo_imported.jpg', '2025-11-20 12:00:05', '2025-12-22 12:00:05', 1, '2025-11-22 12:00:05');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `combo_items`
--

CREATE TABLE `combo_items` (
  `combo_item_id` int(11) NOT NULL,
  `combo_id` int(11) DEFAULT NULL,
  `product_id` int(11) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `combo_items`
--

INSERT INTO `combo_items` (`combo_item_id`, `combo_id`, `product_id`, `quantity`) VALUES
(1, 1, 3, 1),
(2, 1, 4, 1),
(4, 2, 1, 1),
(5, 2, 2, 1);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `discounts`
--

CREATE TABLE `discounts` (
  `discount_id` int(11) NOT NULL,
  `code` varchar(50) DEFAULT NULL,
  `description` text DEFAULT NULL,
  `discount_type` enum('percentage','fixed_amount') DEFAULT NULL,
  `discount_value` decimal(10,2) DEFAULT NULL,
  `min_order_amount` decimal(10,2) DEFAULT NULL,
  `max_discount_amount` decimal(10,2) DEFAULT NULL,
  `start_date` timestamp NULL DEFAULT NULL,
  `end_date` timestamp NULL DEFAULT NULL,
  `usage_limit` int(11) DEFAULT NULL,
  `used_count` int(11) DEFAULT 0,
  `is_active` tinyint(1) DEFAULT 1,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `discounts`
--

INSERT INTO `discounts` (`discount_id`, `code`, `description`, `discount_type`, `discount_value`, `min_order_amount`, `max_discount_amount`, `start_date`, `end_date`, `usage_limit`, `used_count`, `is_active`, `created_at`) VALUES
(1, 'WELCOME', 'Giảm 10% cho đơn hàng đầu tiên', 'percentage', 10.00, 100000.00, 100000.00, '2025-11-20 12:00:05', '2026-02-20 12:00:05', 1000, 0, 1, '2025-11-22 12:00:05'),
(2, 'FRUIT50K', 'Giảm 50K cho đơn từ 300K', 'fixed_amount', 50000.00, 300000.00, 50000.00, '2025-11-20 12:00:05', '2025-12-22 12:00:05', 500, 0, 1, '2025-11-22 12:00:05'),
(3, 'FRUITVIP', 'Giảm 15% cho thành viên VIP', 'percentage', 15.00, 500000.00, 150000.00, '2025-11-20 12:00:05', '2026-11-22 12:00:05', 100, 0, 1, '2025-11-22 12:00:05'),
(4, 'WEEKEND', 'Giảm 20K cho đơn cuối tuần', 'fixed_amount', 20000.00, 200000.00, 20000.00, '2025-11-20 12:00:05', '2025-11-29 12:00:05', 200, 0, 1, '2025-11-22 12:00:05'),
(5, 'SUMMER2025', 'Giảm 20% mừng hè 2025', 'percentage', 20.00, 400000.00, 200000.00, '2025-05-31 17:00:00', '2025-08-30 17:00:00', 1000, 0, 1, '2025-11-22 12:00:05');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `discount_usage`
--

CREATE TABLE `discount_usage` (
  `usage_id` int(11) NOT NULL,
  `discount_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `order_id` int(11) DEFAULT NULL,
  `discount_code` varchar(50) NOT NULL,
  `discount_amount` decimal(10,2) NOT NULL,
  `used_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `discount_usage`
--

INSERT INTO `discount_usage` (`usage_id`, `discount_id`, `user_id`, `order_id`, `discount_code`, `discount_amount`, `used_at`) VALUES
(1, 1, 2, 1, 'WELCOME', 12000.00, '2025-11-22 12:02:33'),
(2, 4, 2, 2, 'WEEKEND', 20000.00, '2025-11-22 12:03:42'),
(3, 4, 2, 3, 'WEEKEND', 20000.00, '2025-11-22 12:03:56');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `notifications`
--

CREATE TABLE `notifications` (
  `notification_id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `message` text DEFAULT NULL,
  `type` enum('new_product','discount','order_update','general') DEFAULT NULL,
  `is_read` tinyint(1) DEFAULT 0,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `orders`
--

CREATE TABLE `orders` (
  `order_id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `total_amount` decimal(10,2) DEFAULT NULL,
  `status` enum('pending','completed','cancelled') DEFAULT 'pending',
  `order_date` timestamp NOT NULL DEFAULT current_timestamp(),
  `discount_code` varchar(50) DEFAULT NULL,
  `discount_amount` decimal(10,2) DEFAULT 0.00,
  `shipping_address` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `orders`
--

INSERT INTO `orders` (`order_id`, `user_id`, `total_amount`, `status`, `order_date`, `discount_code`, `discount_amount`, `shipping_address`) VALUES
(1, 2, 108000.00, 'completed', '2025-11-20 12:02:36', 'WELCOME', 12000.00, '123, Xã Văn Khê, Huyện Mê Linh, Thành phố Hà Nội'),
(2, 2, 442000.00, 'completed', '2025-11-06 12:03:45', 'WEEKEND', 20000.00, '123, Xã Văn Khê, Huyện Mê Linh, Thành phố Hà Nội'),
(3, 2, 865000.00, 'completed', '2025-11-13 12:03:58', 'WEEKEND', 20000.00, '123, Xã Văn Khê, Huyện Mê Linh, Thành phố Hà Nội'),
(4, 3, 446600.00, 'completed', '2025-11-22 12:05:04', NULL, 0.00, '456, Phường Quảng Minh, Thị xã Việt Yên, Tỉnh Bắc Giang'),
(5, 3, 470000.00, 'completed', '2025-11-03 12:05:11', NULL, 0.00, '456, Phường Quảng Minh, Thị xã Việt Yên, Tỉnh Bắc Giang'),
(6, 4, 240000.00, 'completed', '2025-11-20 12:06:21', NULL, 0.00, '789, Xã Hoà An, Huyện Giồng Riềng, Tỉnh Kiên Giang'),
(7, 4, 945000.00, 'completed', '2025-11-04 12:06:26', NULL, 0.00, '789, Xã Hoà An, Huyện Giồng Riềng, Tỉnh Kiên Giang'),
(8, 4, 360000.00, 'completed', '2025-11-05 12:06:31', NULL, 0.00, '789, Xã Hoà An, Huyện Giồng Riềng, Tỉnh Kiên Giang'),
(9, 4, 456000.00, 'completed', '2025-11-19 12:06:35', NULL, 0.00, '789, Xã Hoà An, Huyện Giồng Riềng, Tỉnh Kiên Giang'),
(10, 4, 45000.00, 'completed', '2025-11-10 12:06:39', NULL, 0.00, '789, Xã Hoà An, Huyện Giồng Riềng, Tỉnh Kiên Giang'),
(11, 4, 25000.00, 'completed', '2025-11-12 12:06:44', NULL, 0.00, '789, Xã Hoà An, Huyện Giồng Riềng, Tỉnh Kiên Giang'),
(12, 4, 585600.00, 'completed', '2025-11-14 12:06:50', NULL, 0.00, '789, Xã Hoà An, Huyện Giồng Riềng, Tỉnh Kiên Giang'),
(13, 4, 150000.00, 'cancelled', '2025-11-08 12:06:54', NULL, 0.00, '789, Xã Hoà An, Huyện Giồng Riềng, Tỉnh Kiên Giang');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `order_combos`
--

CREATE TABLE `order_combos` (
  `order_combo_id` int(11) NOT NULL,
  `order_id` int(11) NOT NULL,
  `combo_id` int(11) NOT NULL,
  `quantity` int(11) NOT NULL,
  `price` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `order_combos`
--

INSERT INTO `order_combos` (`order_combo_id`, `order_id`, `combo_id`, `quantity`, `price`) VALUES
(1, 3, 1, 1, 150000.00),
(2, 3, 2, 1, 420000.00),
(3, 5, 2, 1, 420000.00),
(4, 12, 2, 1, 420000.00),
(5, 13, 1, 1, 150000.00);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `order_items`
--

CREATE TABLE `order_items` (
  `order_item_id` int(11) NOT NULL,
  `order_id` int(11) DEFAULT NULL,
  `product_id` int(11) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `price` decimal(10,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `order_items`
--

INSERT INTO `order_items` (`order_item_id`, `order_id`, `product_id`, `quantity`, `price`) VALUES
(1, 1, 1, 1, 120000.00),
(2, 2, 3, 3, 40000.00),
(3, 2, 9, 4, 85500.00),
(4, 3, 2, 1, 315000.00),
(5, 4, 11, 4, 70400.00),
(6, 4, 10, 3, 55000.00),
(7, 5, 5, 1, 50000.00),
(8, 6, 1, 2, 120000.00),
(9, 7, 2, 3, 315000.00),
(10, 8, 3, 9, 40000.00),
(11, 9, 4, 8, 57000.00),
(12, 10, 6, 1, 45000.00),
(13, 11, 8, 1, 25000.00),
(14, 12, 7, 1, 165600.00);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `products`
--

CREATE TABLE `products` (
  `product_id` int(11) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `description` text DEFAULT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  `stock_quantity` int(11) DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `category_id` int(11) DEFAULT NULL,
  `discount_percent` int(11) DEFAULT 0,
  `is_new` tinyint(1) DEFAULT 0,
  `is_best_seller` tinyint(1) DEFAULT 0,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `products`
--

INSERT INTO `products` (`product_id`, `name`, `description`, `price`, `stock_quantity`, `image_url`, `category_id`, `discount_percent`, `is_new`, `is_best_seller`, `created_at`) VALUES
(1, 'Táo Envy New Zealand', 'Táo Envy nhập khẩu từ New Zealand, giòn ngọt', 120000.00, 47, 'assets/images/apple_envy.jpg', 1, 0, 1, 1, '2025-11-22 12:00:05'),
(2, 'Nho mẫu đơn Nhật', 'Nho mẫu đơn cao cấp, vị ngọt thanh', 350000.00, 16, 'assets/images/grape_japan.jpg', 1, 10, 1, 1, '2025-11-22 12:00:05'),
(3, 'Cam sành miền Tây', 'Cam sành tươi ngon, mọng nước', 40000.00, 88, 'assets/images/orange_vn.jpg', 2, 0, 0, 1, '2025-11-22 12:00:05'),
(4, 'Xoài cát Hòa Lộc', 'Xoài cát Hòa Lộc nổi tiếng miền Tây', 60000.00, 72, 'assets/images/mango_vn.jpg', 2, 5, 0, 0, '2025-11-22 12:00:05'),
(5, 'Mít sấy', 'Mít sấy giòn, thơm ngon', 50000.00, 39, 'assets/images/jackfruit_dried.jpg', 4, 0, 0, 0, '2025-11-22 12:00:05'),
(6, 'Dứa mật thơm ngon', 'Dứa mật thơm mọng, ngọt dịu', 45000.00, 59, 'assets/images/pineapple.jpg', 2, 0, 1, 0, '2025-11-22 12:00:05'),
(7, 'Lựu Israel', 'Quả lựu đỏ mọng, chứa nhiều vitamin', 180000.00, 24, 'assets/images/pomegranate.jpg', 1, 8, 0, 1, '2025-11-22 12:00:05'),
(8, 'Chuối tây chín vàng', 'Chuối chín mềm, ngọt tự nhiên', 25000.00, 119, 'assets/images/banana.jpg', 2, 0, 1, 0, '2025-11-22 12:00:05'),
(9, 'Kiwi New Zealand', 'Kiwi xanh nhập khẩu New Zealand, thơm ngon', 90000.00, 41, 'assets/images/kiwi_nz.jpg', 1, 5, 1, 0, '2025-11-22 12:00:05'),
(10, 'Chanh dây vàng', 'Chanh dây vàng chua nhẹ, thơm', 55000.00, 67, 'assets/images/passionfruit.jpg', 1, 0, 0, 0, '2025-11-22 12:00:05'),
(11, 'Dâu tây Đà Lạt', 'Dâu tây Đà Lạt tươi, ngọt nhẹ', 80000.00, 31, 'assets/images/strawberry_dalat.jpg', 2, 12, 1, 1, '2025-11-22 12:00:05');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `product_stats`
--

CREATE TABLE `product_stats` (
  `product_id` int(11) NOT NULL,
  `total_sold` int(11) DEFAULT 0,
  `last_sold_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `product_stats`
--

INSERT INTO `product_stats` (`product_id`, `total_sold`, `last_sold_at`) VALUES
(1, 74, '2025-11-22 12:00:05'),
(2, 220, '2025-11-22 12:00:05'),
(3, 226, '2025-11-22 12:00:05'),
(4, 17, '2025-11-22 12:00:05'),
(5, 13, '2025-11-22 12:00:05'),
(6, 17, '2025-11-22 12:00:05'),
(7, 91, '2025-11-22 12:00:05'),
(8, 9, '2025-11-22 12:00:05'),
(9, 14, '2025-11-22 12:00:05'),
(10, 6, '2025-11-22 12:00:05'),
(11, 110, '2025-11-22 12:00:05');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `users`
--

CREATE TABLE `users` (
  `user_id` int(11) NOT NULL,
  `username` varchar(50) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `full_name` varchar(100) DEFAULT NULL,
  `phone` varchar(15) DEFAULT NULL,
  `address` text DEFAULT NULL,
  `avatar_url` varchar(255) DEFAULT NULL,
  `role` enum('customer','admin') DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `users`
--

INSERT INTO `users` (`user_id`, `username`, `email`, `password`, `full_name`, `phone`, `address`, `avatar_url`, `role`, `created_at`, `updated_at`) VALUES
(1, 'admin1', 'admin@fruitstore.com', 'JAvlGPq9JyTdtvBO6x2llnRI1+gxwIyPqCKAn3THIKk=', 'Administrator', '0123456678', 'hn', '/assets/images/avatars/avatar2.png', 'admin', '2025-10-09 04:23:14', '2025-10-09 04:26:34'),
(2, 'user01', 'user01@example.com', 'JAvlGPq9JyTdtvBO6x2llnRI1+gxwIyPqCKAn3THIKk=', 'Nguyen Van A', '1234567890', '123, Xã Văn Khê, Huyện Mê Linh, Thành phố Hà Nội', NULL, 'customer', '2025-11-22 12:01:30', '2025-11-22 12:03:08'),
(3, 'user02', 'user02@example.com', 'JAvlGPq9JyTdtvBO6x2llnRI1+gxwIyPqCKAn3THIKk=', 'Le Thi B', '2224446660', '456, Phường Quảng Minh, Thị xã Việt Yên, Tỉnh Bắc Giang', NULL, 'customer', '2025-11-22 12:01:43', '2025-11-22 12:04:38'),
(4, 'user03', 'user03@example.com', 'JAvlGPq9JyTdtvBO6x2llnRI1+gxwIyPqCKAn3THIKk=', 'John Smith', '9999999990', '789, Xã Hoà An, Huyện Giồng Riềng, Tỉnh Kiên Giang', NULL, 'customer', '2025-11-22 12:01:57', '2025-11-22 12:05:44');

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `cart`
--
ALTER TABLE `cart`
  ADD PRIMARY KEY (`cart_id`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `product_id` (`product_id`);

--
-- Chỉ mục cho bảng `cart_combos`
--
ALTER TABLE `cart_combos`
  ADD PRIMARY KEY (`cart_combo_id`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `combo_id` (`combo_id`);

--
-- Chỉ mục cho bảng `categories`
--
ALTER TABLE `categories`
  ADD PRIMARY KEY (`category_id`),
  ADD UNIQUE KEY `slug` (`slug`);

--
-- Chỉ mục cho bảng `combos`
--
ALTER TABLE `combos`
  ADD PRIMARY KEY (`combo_id`);

--
-- Chỉ mục cho bảng `combo_items`
--
ALTER TABLE `combo_items`
  ADD PRIMARY KEY (`combo_item_id`),
  ADD KEY `combo_id` (`combo_id`),
  ADD KEY `product_id` (`product_id`);

--
-- Chỉ mục cho bảng `discounts`
--
ALTER TABLE `discounts`
  ADD PRIMARY KEY (`discount_id`),
  ADD UNIQUE KEY `code` (`code`);

--
-- Chỉ mục cho bảng `discount_usage`
--
ALTER TABLE `discount_usage`
  ADD PRIMARY KEY (`usage_id`),
  ADD KEY `idx_discount_id` (`discount_id`),
  ADD KEY `idx_user_id` (`user_id`),
  ADD KEY `idx_order_id` (`order_id`),
  ADD KEY `idx_discount_code` (`discount_code`),
  ADD KEY `idx_used_at` (`used_at`);

--
-- Chỉ mục cho bảng `notifications`
--
ALTER TABLE `notifications`
  ADD PRIMARY KEY (`notification_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Chỉ mục cho bảng `orders`
--
ALTER TABLE `orders`
  ADD PRIMARY KEY (`order_id`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `idx_discount_code` (`discount_code`);

--
-- Chỉ mục cho bảng `order_combos`
--
ALTER TABLE `order_combos`
  ADD PRIMARY KEY (`order_combo_id`),
  ADD KEY `order_id` (`order_id`),
  ADD KEY `combo_id` (`combo_id`);

--
-- Chỉ mục cho bảng `order_items`
--
ALTER TABLE `order_items`
  ADD PRIMARY KEY (`order_item_id`),
  ADD KEY `order_id` (`order_id`),
  ADD KEY `product_id` (`product_id`);

--
-- Chỉ mục cho bảng `products`
--
ALTER TABLE `products`
  ADD PRIMARY KEY (`product_id`),
  ADD KEY `category_id` (`category_id`);

--
-- Chỉ mục cho bảng `product_stats`
--
ALTER TABLE `product_stats`
  ADD PRIMARY KEY (`product_id`);

--
-- Chỉ mục cho bảng `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `username` (`username`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `cart`
--
ALTER TABLE `cart`
  MODIFY `cart_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT cho bảng `cart_combos`
--
ALTER TABLE `cart_combos`
  MODIFY `cart_combo_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT cho bảng `categories`
--
ALTER TABLE `categories`
  MODIFY `category_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT cho bảng `combos`
--
ALTER TABLE `combos`
  MODIFY `combo_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT cho bảng `combo_items`
--
ALTER TABLE `combo_items`
  MODIFY `combo_item_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT cho bảng `discounts`
--
ALTER TABLE `discounts`
  MODIFY `discount_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT cho bảng `discount_usage`
--
ALTER TABLE `discount_usage`
  MODIFY `usage_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT cho bảng `notifications`
--
ALTER TABLE `notifications`
  MODIFY `notification_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `orders`
--
ALTER TABLE `orders`
  MODIFY `order_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT cho bảng `order_combos`
--
ALTER TABLE `order_combos`
  MODIFY `order_combo_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT cho bảng `order_items`
--
ALTER TABLE `order_items`
  MODIFY `order_item_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT cho bảng `products`
--
ALTER TABLE `products`
  MODIFY `product_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT cho bảng `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `cart`
--
ALTER TABLE `cart`
  ADD CONSTRAINT `cart_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
  ADD CONSTRAINT `cart_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`);

--
-- Các ràng buộc cho bảng `cart_combos`
--
ALTER TABLE `cart_combos`
  ADD CONSTRAINT `cart_combos_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
  ADD CONSTRAINT `cart_combos_ibfk_2` FOREIGN KEY (`combo_id`) REFERENCES `combos` (`combo_id`);

--
-- Các ràng buộc cho bảng `combo_items`
--
ALTER TABLE `combo_items`
  ADD CONSTRAINT `combo_items_ibfk_1` FOREIGN KEY (`combo_id`) REFERENCES `combos` (`combo_id`),
  ADD CONSTRAINT `combo_items_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`);

--
-- Các ràng buộc cho bảng `discount_usage`
--
ALTER TABLE `discount_usage`
  ADD CONSTRAINT `fk_discount_usage_discount` FOREIGN KEY (`discount_id`) REFERENCES `discounts` (`discount_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_discount_usage_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`) ON DELETE SET NULL,
  ADD CONSTRAINT `fk_discount_usage_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE;

--
-- Các ràng buộc cho bảng `notifications`
--
ALTER TABLE `notifications`
  ADD CONSTRAINT `notifications_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);

--
-- Các ràng buộc cho bảng `orders`
--
ALTER TABLE `orders`
  ADD CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);

--
-- Các ràng buộc cho bảng `order_combos`
--
ALTER TABLE `order_combos`
  ADD CONSTRAINT `order_combos_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`),
  ADD CONSTRAINT `order_combos_ibfk_2` FOREIGN KEY (`combo_id`) REFERENCES `combos` (`combo_id`);

--
-- Các ràng buộc cho bảng `order_items`
--
ALTER TABLE `order_items`
  ADD CONSTRAINT `order_items_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`),
  ADD CONSTRAINT `order_items_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`);

--
-- Các ràng buộc cho bảng `products`
--
ALTER TABLE `products`
  ADD CONSTRAINT `products_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `categories` (`category_id`);

--
-- Các ràng buộc cho bảng `product_stats`
--
ALTER TABLE `product_stats`
  ADD CONSTRAINT `product_stats_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
