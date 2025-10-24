-- Tạo database FruitStore
CREATE DATABASE IF NOT EXISTS FruitStore CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE FruitStore;

-- Bảng người dùng
CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE,
    email VARCHAR(100) UNIQUE,
    password VARCHAR(255),
    full_name VARCHAR(100),
    phone VARCHAR(15),
    address TEXT,
    avatar_url VARCHAR(255),
    role ENUM('customer', 'admin'),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Bảng danh mục
CREATE TABLE categories (
    category_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100),
    description TEXT,
    image_url VARCHAR(255),
    icon VARCHAR(50),
    slug VARCHAR(100) UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Bảng sản phẩm/trái cây
CREATE TABLE products (
    product_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100),
    description TEXT,
    price DECIMAL(10,2),
    stock_quantity INT,
    image_url VARCHAR(255),
    category_id INT,
    discount_percent INT DEFAULT 0,
    is_new BOOLEAN DEFAULT FALSE,
    is_best_seller BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES categories(category_id)
);

-- Bảng combo/khuyến mãi đặc biệt
CREATE TABLE combos (
    combo_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100),
    description TEXT,
    original_price DECIMAL(10,2),
    combo_price DECIMAL(10,2),
    image_url VARCHAR(255),
    start_date TIMESTAMP NULL DEFAULT NULL,
    end_date TIMESTAMP NULL DEFAULT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Bảng giỏ hàng (chỉ cho sản phẩm lẻ)
CREATE TABLE cart (
    cart_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    product_id INT,
    quantity INT,
    selected BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (product_id) REFERENCES products(product_id)
);

-- Bảng giỏ hàng cho combo
CREATE TABLE cart_combos (
    cart_combo_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    combo_id INT,
    quantity INT,
    selected BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (combo_id) REFERENCES combos(combo_id)
);

-- Bảng đơn hàng
CREATE TABLE orders (
    order_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    total_amount DECIMAL(10,2),
    status ENUM('pending', 'completed', 'cancelled') DEFAULT 'pending',
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- Bảng chi tiết đơn hàng
CREATE TABLE order_items (
    order_item_id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT,
    product_id INT,
    quantity INT,
    price DECIMAL(10,2),
    FOREIGN KEY (order_id) REFERENCES orders(order_id),
    FOREIGN KEY (product_id) REFERENCES products(product_id)
);

-- Bảng mã giảm giá
CREATE TABLE discounts (
    discount_id INT PRIMARY KEY AUTO_INCREMENT,
    code VARCHAR(50) UNIQUE,
    description TEXT,
    discount_type ENUM('percentage', 'fixed_amount'),
    discount_value DECIMAL(10,2),
    min_order_amount DECIMAL(10,2),
    max_discount_amount DECIMAL(10,2),
    start_date TIMESTAMP NULL DEFAULT NULL,
    end_date TIMESTAMP NULL DEFAULT NULL,
    usage_limit INT,
    used_count INT DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Bảng thông báo
CREATE TABLE notifications (
    notification_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    title VARCHAR(255),
    message TEXT,
    type ENUM('new_product', 'discount', 'order_update', 'general'),
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- Bảng sản phẩm trong combo
CREATE TABLE combo_items (
    combo_item_id INT PRIMARY KEY AUTO_INCREMENT,
    combo_id INT,
    product_id INT,
    quantity INT,
    FOREIGN KEY (combo_id) REFERENCES combos(combo_id),
    FOREIGN KEY (product_id) REFERENCES products(product_id)
);

-- Bảng thống kê sản phẩm
CREATE TABLE product_stats (
    product_id INT PRIMARY KEY,
    total_sold INT DEFAULT 0,
    last_sold_at TIMESTAMP NULL DEFAULT NULL,
    FOREIGN KEY (product_id) REFERENCES products(product_id)
);


-- Create discount_usage table
CREATE TABLE IF NOT EXISTS discount_usage (
    usage_id INT PRIMARY KEY AUTO_INCREMENT,
    discount_id INT NOT NULL,
    user_id INT NOT NULL,
    order_id INT,
    discount_code VARCHAR(50) NOT NULL,
    discount_amount DECIMAL(10,2) NOT NULL,
    used_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Foreign keys
    CONSTRAINT fk_discount_usage_discount FOREIGN KEY (discount_id) 
        REFERENCES discounts(discount_id) ON DELETE CASCADE,
    CONSTRAINT fk_discount_usage_user FOREIGN KEY (user_id) 
        REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_discount_usage_order FOREIGN KEY (order_id) 
        REFERENCES orders(order_id) ON DELETE SET NULL,
    
    -- Indexes for performance
    INDEX idx_discount_id (discount_id),
    INDEX idx_user_id (user_id),
    INDEX idx_order_id (order_id),
    INDEX idx_discount_code (discount_code),
    INDEX idx_used_at (used_at)
);

-- Add discount_code and discount_amount columns to orders table if they don't exist
ALTER TABLE orders ADD COLUMN IF NOT EXISTS discount_code VARCHAR(50);
ALTER TABLE orders ADD COLUMN IF NOT EXISTS discount_amount DECIMAL(10,2) DEFAULT 0;

-- Create index for discount_code in orders table
ALTER TABLE orders ADD INDEX IF NOT EXISTS idx_discount_code (discount_code);

SELECT 'discount_usage table created successfully' as status;

-- =============================