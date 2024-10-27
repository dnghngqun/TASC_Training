CREATE DATABASE gomsu;
USE gomsu;

CREATE TABLE Users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    phone_number VARCHAR(15) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    address TEXT,
    role_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL
);

CREATE TABLE Roles (
    role_id INT AUTO_INCREMENT PRIMARY KEY,
    role_name VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL
);

CREATE TABLE Categories (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL
);

CREATE TABLE Products (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    image_url VARCHAR(255),
    category_id INT,
    stock INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL
);

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

CREATE TABLE Cart (
    cart_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    product_id INT,
    quantity INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL
);

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

CREATE TABLE Notifications (
    notification_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    product_id INT,
    order_time TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL
);

CREATE TABLE JWTTokens (
    token_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    token TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP,
    is_revoked BOOLEAN DEFAULT FALSE
);

CREATE TABLE PasswordResetToken (
    prt_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    reset_token TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP,
    is_used BOOLEAN DEFAULT FALSE
);

-- Tạo các khóa tham chiếu (Foreign Keys)
ALTER TABLE Users ADD CONSTRAINT fk_role FOREIGN KEY (role_id) REFERENCES Roles(role_id);
ALTER TABLE Products ADD CONSTRAINT fk_category FOREIGN KEY (category_id) REFERENCES Categories(category_id);
ALTER TABLE Orders ADD CONSTRAINT fk_user_order FOREIGN KEY (user_id) REFERENCES Users(user_id);
ALTER TABLE Orders ADD CONSTRAINT fk_discount FOREIGN KEY (discount_id) REFERENCES Discounts(discount_id);
ALTER TABLE OrderDetails ADD CONSTRAINT fk_order_detail_order FOREIGN KEY (order_id) REFERENCES Orders(order_id);
ALTER TABLE OrderDetails ADD CONSTRAINT fk_order_detail_product FOREIGN KEY (product_id) REFERENCES Products(product_id);
ALTER TABLE Cart ADD CONSTRAINT fk_cart_user FOREIGN KEY (user_id) REFERENCES Users(user_id);
ALTER TABLE Cart ADD CONSTRAINT fk_cart_product FOREIGN KEY (product_id) REFERENCES Products(product_id);
ALTER TABLE Payments ADD CONSTRAINT fk_payment_order FOREIGN KEY (order_id) REFERENCES Orders(order_id);
ALTER TABLE Notifications ADD CONSTRAINT fk_notification_user FOREIGN KEY (user_id) REFERENCES Users(user_id);
ALTER TABLE Notifications ADD CONSTRAINT fk_notification_product FOREIGN KEY (product_id) REFERENCES Products(product_id);
ALTER TABLE JWTTokens ADD CONSTRAINT fk_token_user FOREIGN KEY (user_id) REFERENCES Users(user_id);
ALTER TABLE PasswordResetToken ADD CONSTRAINT fk_prt_user FOREIGN KEY (user_id) REFERENCES Users(user_id);
