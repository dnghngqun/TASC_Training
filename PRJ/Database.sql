
CREATE DATABASE gomsu;
USE gomsu;

-- Tạo bảng users
CREATE TABLE users (
    user_id CHAR(36) PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255),
    phone_number VARCHAR(20),
    full_name VARCHAR(255) NOT NULL,
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

-- Bảng addressbook
CREATE TABLE address_book(
	address_book_id INT AUTO_INCREMENT PRIMARY KEY,
	full_name VARCHAR(255) NOT NULL,
    phoneNumber VARCHAR(255) NOT NULL,
	address TEXT,
	user_id CHAR(36)
);

-- Tạo bảng orders
CREATE TABLE orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id CHAR(36),
    total_price DECIMAL(10, 2),
    status ENUM('pending', 'delivered', 'success', 'cancel', 'error') DEFAULT 'pending',
    address_book_id INT,
    note TEXT,
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
    status ENUM ('pending', 'success', 'cancel', 'error') DEFAULT 'pending' ,
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
    payment_id varchar(500) PRIMARY KEY,
    order_id INT,
    payment_method ENUM('online', 'on_delivery'),
    payment_status ENUM('pending','success','error', 'cancel') DEFAULT 'pending',
    paid_at TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL
);

-- Tạo bảng discounts
CREATE TABLE discounts (
    discount_id INT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(255) UNIQUE,
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
CREATE TABLE email_retry_queue (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email_to VARCHAR(255),
    subject VARCHAR(255),
    body TEXT, -- Lưu object dưới dạng JSON
    status VARCHAR(50) DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
-- Tạo các khóa ngoại
ALTER TABLE users
    ADD CONSTRAINT fk_role_id FOREIGN KEY (role_id) REFERENCES roles(role_id);

ALTER TABLE address_book
	ADD CONSTRAINT fk_address_book_user_id FOREIGN KEY (user_id) REFERENCES users(user_id);

ALTER TABLE products
    ADD CONSTRAINT fk_category_id FOREIGN KEY (category_id) REFERENCES categories(category_id);

ALTER TABLE orders
    ADD CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES users(user_id),
    ADD CONSTRAINT fk_discount_id FOREIGN KEY (discount_id) REFERENCES discounts(discount_id),
   	ADD CONSTRAINT fk_order_address_book_id FOREIGN KEY (address_book_id) REFERENCES address_book(address_book_id);

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
   
DELIMITER $$

CREATE PROCEDURE add_order_with_details(
    IN userId CHAR(36), 
    IN totalPrice DECIMAL(10,2), 
    IN discountId INT,
    IN noteRq TEXT,
    IN addressBookId INT,
    IN orderDetails JSON
)
BEGIN
    DECLARE i INT DEFAULT 0;
    DECLARE orderDetail JSON;
    DECLARE productId INT;
    DECLARE quantity INT;
    DECLARE price DECIMAL(10, 2);

    -- Thêm Order
    INSERT INTO orders (user_id, total_price, discount_id, status, note, address_book_id) 
    VALUES (userId, totalPrice, IFNULL(discountId, NULL), 'pending', IFNULL(noteRq, NULL), addressBookId);
    
    SET @orderId = LAST_INSERT_ID();

    -- Thêm Order Details với status 'pending'
    WHILE i < JSON_LENGTH(orderDetails) DO
        SET orderDetail = JSON_EXTRACT(orderDetails, CONCAT('$[', i, ']'));
        SET productId = JSON_UNQUOTE(JSON_EXTRACT(orderDetail, '$.productId'));
        SET quantity = JSON_UNQUOTE(JSON_EXTRACT(orderDetail, '$.quantity'));
        SET price = JSON_UNQUOTE(JSON_EXTRACT(orderDetail, '$.price'));

        INSERT INTO order_details (order_id, product_id, quantity, price, status) 
        VALUES (@orderId, productId, quantity, price, 'pending');

        SET i = i + 1;
    END WHILE;

    -- Trả về thông tin Order vừa được thêm
    SELECT * FROM orders WHERE order_id = @orderId;
END$$

DELIMITER ;

DELIMITER $$

CREATE PROCEDURE delete_order_and_details(
    IN orderId INT
)
BEGIN
    -- Xóa các order_details liên quan đến order
    DELETE FROM order_details WHERE order_id = orderId;

    -- Xóa order
    DELETE FROM orders WHERE order_id = orderId;
END$$

DELIMITER ;


INSERT INTO categories (name) VALUES
('Nồi sứ dưỡng sinh'),
('Phòng ăn'),
('Sứ dưỡng sinh'),
('Trà - cà phê'),
('Phụ kiện bàn ăn'),
('Sứ nghệ thuật');

INSERT INTO products (name, description, price, image_url, category_id, stock, discount)
VALUES
('Nồi sứ dưỡng sinh 3.5L', 'Nồi sứ dưỡng sinh cao cấp, giữ nhiệt tốt, an toàn cho sức khoẻ.', 780000, 'images/noi_35l.jpg', 1, 20, 0),
('Bộ chén dĩa bàn ăn 18 món', 'Bộ chén dĩa gốm sứ nghệ thuật dành cho 6 người ăn.', 1150000, 'images/chen_dia_18.jpg', 2, 10, 5.0),
('Tô sứ dưỡng sinh nhỏ', 'Tô nhỏ dùng để đựng súp hoặc cháo, thích hợp cho trẻ em.', 185000, 'images/to_su_nho.jpg', 3, 50, 0),
('Bộ bình trà và 6 ly', 'Bộ trà gốm sứ men rạn, họa tiết hoa sen thủ công.', 620000, 'images/bo_tra_hoa_sen.jpg', 4, 15, 10.0),
('Khay gỗ tre đựng chén', 'Phụ kiện bàn ăn bằng gỗ tre, giúp sắp xếp chén dĩa gọn gàng.', 125000, 'images/khay_go.jpg', 5, 30, 0),
('Bình hoa sứ nghệ thuật', 'Bình hoa gốm sứ vẽ tay, dùng trang trí phòng khách.', 970000, 'images/binh_hoa.jpg', 6, 8, 15.0);
