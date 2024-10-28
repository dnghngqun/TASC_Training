CREATE DATABASE gomsu;
USE gomsu;
-- Bảng Users (Người dùng)
CREATE TABLE Users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    address VARCHAR(255),
    role_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL
);

-- Bảng Roles (Vai trò)
CREATE TABLE Roles (
    role_id INT AUTO_INCREMENT PRIMARY KEY,
    role_name VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL
);

-- Bảng Products (Sản phẩm)
CREATE TABLE Products (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    image_url VARCHAR(255),
    category_id INT,
    stock INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL
);

-- Bảng Categories (Danh mục sản phẩm)
CREATE TABLE Categories (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL
);

-- Bảng Orders (Đơn hàng)
CREATE TABLE Orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    total_price DECIMAL(10, 2),
    status ENUM('pending', 'shipped', 'delivered', 'cancelled') DEFAULT 'pending',
    discount_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL
);

-- Bảng OrderDetails (Chi tiết đơn hàng)
CREATE TABLE OrderDetails (
    order_detail_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT,
    product_id INT,
    quantity INT NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL
);

-- Bảng Cart (Giỏ hàng)
CREATE TABLE Cart (
    cart_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    product_id INT,
    quantity INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL
);

-- Bảng Payments (Thanh toán)
CREATE TABLE Payments (
    payment_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT,
    payment_method ENUM('online', 'on_delivery'),
    payment_status ENUM('pending', 'completed') DEFAULT 'pending',
    paid_at TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL
);

-- Bảng Discounts (Mã giảm giá)
CREATE TABLE Discounts (
    discount_id INT AUTO_INCREMENT PRIMARY KEY,
    amount DECIMAL(10, 2) NOT NULL,
    expires TIMESTAMP,
    quantity INT,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL
);

-- Bảng Notification (Thông báo)
CREATE TABLE Notification (
    notification_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    product_id INT,
    order_time TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL
);

-- Bảng Tokens
CREATE TABLE Tokens (
    token_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    token VARCHAR(255),
    token_type ENUM('JWT', 'Reset_token') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP,
    is_revoked BOOLEAN DEFAULT FALSE
);

-- Bảng Posts (Bài viết)
CREATE TABLE Posts (
    post_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    content TEXT,
    image_link VARCHAR(255),
    post_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Bảng Comments (Bình luận)
CREATE TABLE Comments (
    comment_id INT AUTO_INCREMENT PRIMARY KEY,
    post_id INT,
    email VARCHAR(255),
    name VARCHAR(255),
    content TEXT,
    time_comment TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Thêm khóa ngoại cho Users
ALTER TABLE Users 
ADD CONSTRAINT fk_users_roles FOREIGN KEY (role_id) REFERENCES Roles(role_id);

-- Thêm khóa ngoại cho Products
ALTER TABLE Products 
ADD CONSTRAINT fk_products_categories FOREIGN KEY (category_id) REFERENCES Categories(category_id);

-- Thêm khóa ngoại cho Orders
ALTER TABLE Orders 
ADD CONSTRAINT fk_orders_users FOREIGN KEY (user_id) REFERENCES Users(user_id),
ADD CONSTRAINT fk_orders_discounts FOREIGN KEY (discount_id) REFERENCES Discounts(discount_id);

-- Thêm khóa ngoại cho OrderDetails
ALTER TABLE OrderDetails 
ADD CONSTRAINT fk_orderdetails_orders FOREIGN KEY (order_id) REFERENCES Orders(order_id),
ADD CONSTRAINT fk_orderdetails_products FOREIGN KEY (product_id) REFERENCES Products(product_id);

-- Thêm khóa ngoại cho Cart
ALTER TABLE Cart 
ADD CONSTRAINT fk_cart_users FOREIGN KEY (user_id) REFERENCES Users(user_id),
ADD CONSTRAINT fk_cart_products FOREIGN KEY (product_id) REFERENCES Products(product_id);

-- Thêm khóa ngoại cho Payments
ALTER TABLE Payments 
ADD CONSTRAINT fk_payments_orders FOREIGN KEY (order_id) REFERENCES Orders(order_id);

-- Thêm khóa ngoại cho Notification
ALTER TABLE Notification 
ADD CONSTRAINT fk_notification_users FOREIGN KEY (user_id) REFERENCES Users(user_id),
ADD CONSTRAINT fk_notification_products FOREIGN KEY (product_id) REFERENCES Products(product_id);

-- Thêm khóa ngoại cho Tokens
ALTER TABLE Tokens 
ADD CONSTRAINT fk_tokens_users FOREIGN KEY (user_id) REFERENCES Users(user_id);

-- Thêm khóa ngoại cho Posts
ALTER TABLE Posts 
ADD CONSTRAINT fk_posts_users FOREIGN KEY (user_id) REFERENCES Users(user_id);

-- Thêm khóa ngoại cho Comments
ALTER TABLE Comments 
ADD CONSTRAINT fk_comments_posts FOREIGN KEY (post_id) REFERENCES Posts(post_id);
