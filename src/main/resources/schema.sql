CREATE TABLE IF NOT EXISTS customers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    birth_date DATE,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(50),
    identification_number VARCHAR(100) UNIQUE
);

CREATE TABLE IF NOT EXISTS products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    price DECIMAL(10, 2) NOT NULL,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    product_type VARCHAR(31) NOT NULL
);

CREATE TABLE IF NOT EXISTS orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_type VARCHAR(50),
    total_amount DECIMAL(10, 2) NULL
);

CREATE TABLE IF NOT EXISTS discounts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    rate DECIMAL(5, 4) NOT NULL,
    category VARCHAR(255),
    type VARCHAR(255),
    start_date DATE NOT NULL,
    end_date DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS order_products (
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    PRIMARY KEY (order_id, product_id),
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS order_discounts (
    order_id BIGINT NOT NULL,
    discount_id BIGINT NOT NULL,
    PRIMARY KEY (order_id, discount_id),
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (discount_id) REFERENCES discounts(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS standard_orders (
    id BIGINT PRIMARY KEY,
    FOREIGN KEY (id) REFERENCES orders(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS shopping_carts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT,
    status VARCHAR(20) NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE
);

-- Intermediate table for the many-to-many relationship between shopping_carts and products
CREATE TABLE IF NOT EXISTS shopping_cart_products (
    product_id BIGINT NOT NULL,
    shopping_cart_id BIGINT NOT NULL,
    PRIMARY KEY (product_id, shopping_cart_id),
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    FOREIGN KEY (shopping_cart_id) REFERENCES shopping_carts(id) ON DELETE CASCADE
);

