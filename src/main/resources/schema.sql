-- Create table for customers
CREATE TABLE customers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,  -- Auto-incremented primary key for customers
    name VARCHAR(255) NOT NULL,             -- Customer's first name
    last_name VARCHAR(255) NOT NULL,        -- Customer's last name
    birth_date DATE,                        -- Customer's birth date
    email VARCHAR(255) NOT NULL UNIQUE,     -- Customer's email, must be unique
    phone VARCHAR(50),                       -- Customer's phone number
    identification_number VARCHAR(100) UNIQUE -- Customer's identification number, must be unique
);

-- Create table for products
CREATE TABLE products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,    -- Auto-incremented primary key for products
    price DECIMAL(10, 2) NOT NULL,           -- Price of the product
    name VARCHAR(255) NOT NULL,              -- Product name
    description VARCHAR(255),                 -- Description of the product
    product_type VARCHAR(31) NOT NULL                -- Discriminator column to identify the type (e.g., "BOOK", "CLOTHING", "ELECTRONIC")
);


-- Create table for orders
CREATE TABLE orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,     -- Auto-incremented primary key for orders
    total_amount DECIMAL(10, 2) NOT NULL      -- Total amount of the order
);

-- Create table for order_products to establish a many-to-many relationship
CREATE TABLE order_products (
    order_id BIGINT NOT NULL,                 -- Foreign key referencing orders
    product_id BIGINT NOT NULL,               -- Foreign key referencing products
    PRIMARY KEY (order_id, product_id),       -- Composite primary key
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE, -- Foreign key constraint for order_id
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE -- Foreign key constraint for product_id
);

-- Create table for discounts
CREATE TABLE discounts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,        -- Auto-incremented primary key for discounts
    rate DECIMAL(5, 4) NOT NULL,                 -- Discount rate (e.g., 0.10 for 10%)
    category VARCHAR(255),                        -- Product category (consider renaming or removing if not needed)
    type VARCHAR(255),                           -- Discount type
    start_date DATE NOT NULL,                    -- Discount start date
    end_date DATE NOT NULL                       -- Discount end date
);

-- Create table for order_discounts to manage discounts applied to orders
CREATE TABLE order_discounts (
    order_id BIGINT NOT NULL,                 -- Foreign key referencing orders
    discount_id BIGINT NOT NULL,              -- Foreign key referencing discounts
    PRIMARY KEY (order_id, discount_id),      -- Composite primary key
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE, -- Foreign key constraint for order_id
    FOREIGN KEY (discount_id) REFERENCES discounts(id) ON DELETE CASCADE -- Foreign key constraint for discount_id
);

-- Create table for standard_orders, extending orders
CREATE TABLE IF NOT EXISTS standard_orders (
    id BIGINT PRIMARY KEY,                     -- ID of the standard order (reference to orders)
    FOREIGN KEY (id) REFERENCES orders(id) ON DELETE CASCADE -- Foreign key relationship with orders
);

-- Create table for shopping carts
CREATE TABLE IF NOT EXISTS shopping_carts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,      -- Auto-incremented primary key for shopping carts
    customer_id BIGINT,                        -- Foreign key referencing customers
    status VARCHAR(20) NOT NULL,               -- Status of the cart (DRAFT or SUBMIT)
    FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE -- Foreign key constraint for customer_id
);

-- Create an intermediate table for the many-to-many relationship between shopping_carts and products
CREATE TABLE IF NOT EXISTS shopping_cart_products (
    cart_id BIGINT,                            -- Foreign key referencing shopping carts
    product_id BIGINT,                         -- Foreign key referencing products
    PRIMARY KEY (cart_id, product_id),         -- Composite primary key
    FOREIGN KEY (cart_id) REFERENCES shopping_carts(id) ON DELETE CASCADE, -- Foreign key constraint for cart_id
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE -- Foreign key constraint for product_id
);

