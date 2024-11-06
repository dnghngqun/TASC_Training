DROP DATABASE gomsu;
CREATE DATABASE gomsu;
USE gomsu;

-- Tạo bảng Users
CREATE TABLE Users (
    user_id CHAR(36) PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255),
    phone_number VARCHAR(20) ,
    full_name VARCHAR(255) NOT NULL,
    address TEXT,
    provider VARCHAR(255),
    provider_id VARCHAR(255),
    role_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL
);

-- Tạo bảng Roles
CREATE TABLE Roles (
    role_id INT AUTO_INCREMENT PRIMARY KEY,
    role_name VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL
);

INSERT INTO Roles (role_name) VALUES ('admin'), ('shipper'), ('user');

-- Tạo bảng Products
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
CREATE TABLE PriceRange (
    price_range_id INT PRIMARY KEY,
    range_description VARCHAR(255)
);
CREATE TABLE Pattern (
    pattern_id INT PRIMARY KEY,
    pattern_name VARCHAR(255)
);
CREATE TABLE Color (
    color_id INT PRIMARY KEY,
    color_name VARCHAR(255)
);
CREATE TABLE Capacity (
    capacity_id INT PRIMARY KEY,
    size DECIMAL(5, 2) -- Dung tích theo cm
);
CREATE TABLE Material (
    material_id INT PRIMARY KEY,
    material_name VARCHAR(255)
);
CREATE TABLE Product_PriceRange (
    product_id INT,
    price_range_id INT,
    PRIMARY KEY (product_id, price_range_id),
    FOREIGN KEY (product_id) REFERENCES Products(product_id),
    FOREIGN KEY (price_range_id) REFERENCES PriceRange(price_range_id)
);

CREATE TABLE Product_Pattern (
    product_id INT,
    pattern_id INT,
    PRIMARY KEY (product_id, pattern_id),
    FOREIGN KEY (product_id) REFERENCES Products(product_id),
    FOREIGN KEY (pattern_id) REFERENCES Pattern(pattern_id)
);

CREATE TABLE Product_Color (
    product_id INT,
    color_id INT,
    PRIMARY KEY (product_id, color_id),
    FOREIGN KEY (product_id) REFERENCES Products(product_id),
    FOREIGN KEY (color_id) REFERENCES Color(color_id)
);

CREATE TABLE Product_Capacity (
    product_id INT,
    capacity_id INT,
    PRIMARY KEY (product_id, capacity_id),
    FOREIGN KEY (product_id) REFERENCES Products(product_id),
    FOREIGN KEY (capacity_id) REFERENCES Capacity(capacity_id)
);

CREATE TABLE Product_Material (
    product_id INT,
    material_id INT,
    PRIMARY KEY (product_id, material_id),
    FOREIGN KEY (product_id) REFERENCES Products(product_id),
    FOREIGN KEY (material_id) REFERENCES Material(material_id)
);


-- Tạo bảng Categories
CREATE TABLE Categories (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL
);

-- Tạo bảng Orders
CREATE TABLE Orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id CHAR(36),
    total_price DECIMAL(10, 2),
    status ENUM('pending', 'shipped', 'delivered', 'cancelled') DEFAULT 'pending',
    discount_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL
);

-- Tạo bảng OrderDetails
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

-- Tạo bảng Cart
CREATE TABLE Cart (
    cart_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id CHAR(36),
    product_id INT,
    quantity INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Tạo bảng Payments
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

-- Tạo bảng Discounts
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

-- Tạo bảng Notification
CREATE TABLE Notification (
    notification_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id CHAR(36),
    product_id INT,
    order_time TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL
);

-- Tạo bảng Tokens: reset password, OTP
CREATE TABLE Tokens (
    token_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id CHAR(36),
    token INT,
    token_type ENUM('OTP') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP NULL,
    is_revoked BOOLEAN DEFAULT FALSE
);

-- Tạo bảng Posts
CREATE TABLE Posts (
    post_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id CHAR(36),
    content TEXT,
    image_link VARCHAR(255),
    post_time TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Tạo bảng Comments
CREATE TABLE Comments (
    comment_id INT AUTO_INCREMENT PRIMARY KEY,
    post_id INT,
    email VARCHAR(255),
    name VARCHAR(255),
    content TEXT,
    time_comment TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tạo các khóa ngoại
ALTER TABLE Users
    ADD CONSTRAINT fk_role_id FOREIGN KEY (role_id) REFERENCES Roles(role_id);

ALTER TABLE Products
    ADD CONSTRAINT fk_category_id FOREIGN KEY (category_id) REFERENCES Categories(category_id);

ALTER TABLE Orders
    ADD CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES Users(user_id),
    ADD CONSTRAINT fk_discount_id FOREIGN KEY (discount_id) REFERENCES Discounts(discount_id);

ALTER TABLE OrderDetails
    ADD CONSTRAINT fk_order_id FOREIGN KEY (order_id) REFERENCES Orders(order_id),
    ADD CONSTRAINT fk_product_id FOREIGN KEY (product_id) REFERENCES Products(product_id);

ALTER TABLE Cart
    ADD CONSTRAINT fk_cart_user_id FOREIGN KEY (user_id) REFERENCES Users(user_id),
    ADD CONSTRAINT fk_cart_product_id FOREIGN KEY (product_id) REFERENCES Products(product_id);

ALTER TABLE Payments
    ADD CONSTRAINT fk_payment_order_id FOREIGN KEY (order_id) REFERENCES Orders(order_id);

ALTER TABLE Notification
    ADD CONSTRAINT fk_notification_user_id FOREIGN KEY (user_id) REFERENCES Users(user_id),
    ADD CONSTRAINT fk_notification_product_id FOREIGN KEY (product_id) REFERENCES Products(product_id);

ALTER TABLE Tokens
    ADD CONSTRAINT fk_token_user_id FOREIGN KEY (user_id) REFERENCES Users(user_id);
ALTER TABLE Posts
    ADD CONSTRAINT fk_post_user_id FOREIGN KEY (user_id) REFERENCES Users(user_id);

ALTER TABLE Comments
    ADD CONSTRAINT fk_comment_post_id FOREIGN KEY (post_id) REFERENCES Posts(post_id);
