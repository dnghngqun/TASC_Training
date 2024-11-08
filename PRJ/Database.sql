DROP DATABASE gomsu;
CREATE DATABASE gomsu;
USE gomsu;

-- Tạo bảng users
CREATE TABLE users (
    user_id CHAR(36) PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255),
    phone_number VARCHAR(20),
    full_name VARCHAR(255) NOT NULL,
    address TEXT,
    provider VARCHAR(255),
    provider_id VARCHAR(255),
    role_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL
);

-- Tạo bảng roles
CREATE TABLE roles (
    role_id INT AUTO_INCREMENT PRIMARY KEY,
    role_name VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL
);

INSERT INTO roles (role_name) VALUES ('admin'), ('shipper'), ('user');

-- Tạo bảng products
CREATE TABLE products (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    image_url VARCHAR(255),
    category_id INT,
    stock INT,
    discount DECIMAL(10,2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL
);

-- Tạo bảng categories
CREATE TABLE categories (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL
);

-- Tạo bảng orders
CREATE TABLE orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id CHAR(36),
    total_price DECIMAL(10, 2),
    status ENUM('pending', 'shipped', 'delivered', 'cancelled') DEFAULT 'pending',
    discount_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL
);

-- Tạo bảng order_details
CREATE TABLE order_details (
    order_detail_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT,
    product_id INT,
    quantity INT NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL
);

-- Tạo bảng cart
CREATE TABLE cart (
    cart_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id CHAR(36),
    product_id INT,
    quantity INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Tạo bảng payments
CREATE TABLE payments (
    payment_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT,
    payment_method ENUM('online', 'on_delivery'),
    payment_status ENUM('pending', 'completed') DEFAULT 'pending',
    paid_at TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL
);

-- Tạo bảng discounts
CREATE TABLE discounts (
    discount_id INT AUTO_INCREMENT PRIMARY KEY,
    amount DECIMAL(10, 2) NOT NULL,
    expires TIMESTAMP,
    quantity INT,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL
);

-- Tạo bảng notification
CREATE TABLE notification (
    notification_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id CHAR(36),
    product_id INT,
    order_time TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL
);

-- Tạo bảng tokens
CREATE TABLE tokens (
    token_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id CHAR(36),
    token INT,
    token_type ENUM('OTP') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP NULL,
    is_revoked BOOLEAN DEFAULT FALSE
);

-- Tạo bảng posts
CREATE TABLE posts (
    post_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id CHAR(36),
    content TEXT,
    image_link VARCHAR(255),
    post_time TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Tạo bảng comments
CREATE TABLE comments (
    comment_id INT AUTO_INCREMENT PRIMARY KEY,
    post_id INT,
    email VARCHAR(255),
    name VARCHAR(255),
    content TEXT,
    time_comment TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tạo các khóa ngoại
ALTER TABLE users
    ADD CONSTRAINT fk_role_id FOREIGN KEY (role_id) REFERENCES roles(role_id);

ALTER TABLE products
    ADD CONSTRAINT fk_category_id FOREIGN KEY (category_id) REFERENCES categories(category_id);

ALTER TABLE orders
    ADD CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES users(user_id),
    ADD CONSTRAINT fk_discount_id FOREIGN KEY (discount_id) REFERENCES discounts(discount_id);

ALTER TABLE order_details
    ADD CONSTRAINT fk_order_id FOREIGN KEY (order_id) REFERENCES orders(order_id),
    ADD CONSTRAINT fk_product_id FOREIGN KEY (product_id) REFERENCES products(product_id);

ALTER TABLE cart
    ADD CONSTRAINT fk_cart_user_id FOREIGN KEY (user_id) REFERENCES users(user_id),
    ADD CONSTRAINT fk_cart_product_id FOREIGN KEY (product_id) REFERENCES products(product_id);

ALTER TABLE payments
    ADD CONSTRAINT fk_payment_order_id FOREIGN KEY (order_id) REFERENCES orders(order_id);

ALTER TABLE notification
    ADD CONSTRAINT fk_notification_user_id FOREIGN KEY (user_id) REFERENCES users(user_id),
    ADD CONSTRAINT fk_notification_product_id FOREIGN KEY (product_id) REFERENCES products(product_id);

ALTER TABLE tokens
    ADD CONSTRAINT fk_token_user_id FOREIGN KEY (user_id) REFERENCES users(user_id);

ALTER TABLE posts
    ADD CONSTRAINT fk_post_user_id FOREIGN KEY (user_id) REFERENCES users(user_id);

ALTER TABLE comments
    ADD CONSTRAINT fk_comment_post_id FOREIGN KEY (post_id) REFERENCES posts(post_id);
