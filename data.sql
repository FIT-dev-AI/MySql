-- Run this SQL after the application starts and JPA creates the tables automatically.
-- Connect to MySQL and execute:

USE product_db;

INSERT INTO categories (name) VALUES ('Laptop'), ('Điện thoại'), ('Tablet');

INSERT INTO products (name, price, image_url, category_id) VALUES
('Lenovo ThinkPad T15 15.6" Laptop Intel Core i7-10610U 512GB SSD 16GB RAM FHD', 27000, 'https://m.media-amazon.com/images/I/71Ycr9EzlsL._AC_SL1500_.jpg', 1),
('iPhone 16 Pro Max 1TB', 41990, 'https://store.storeimages.cdn-apple.com/4982/as-images.apple.com/is/iphone-16-pro-finish-select-202409-6-9inch_GEO_US', 2);

CREATE TABLE IF NOT EXISTS orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    account_username VARCHAR(255),
    order_date DATETIME,
    total_amount DOUBLE,
    status VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS order_details (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT,
    product_id BIGINT,
    quantity INT,
    price DOUBLE,
    FOREIGN KEY (order_id) REFERENCES orders(id),
    FOREIGN KEY (product_id) REFERENCES products(id)
);
