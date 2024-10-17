-- Insert products into the products table

INSERT INTO products (price, name, description, product_type) VALUES
(19.99, 'Java Programming Book', 'A comprehensive guide to Java programming', 'BOOK'),
(49.99, 'Wireless Headphones', 'Noise-cancelling over-ear headphones', 'ELECTRONIC'),
(29.99, 'Winter Jacket', 'Water-resistant winter jacket', 'CLOTHING'),
(15.50, 'Sketchbook', 'Hardcover sketchbook with 200 pages', 'BOOK'),
(99.00, 'Smartwatch', 'Fitness tracker and smartwatch with heart rate monitor', 'ELECTRONIC');

-- Crear un cliente
INSERT INTO customers (id, name, last_name, birth_date, email, phone, identification_number)
VALUES (1, 'John', 'Doe', '1990-01-01', 'john.doe@example.com', '123-456-7890', 'ID12345');
;
-- Crear un carrito de compras
INSERT INTO shopping_carts (id, customer_id, status)
VALUES (1, 1, 'DRAFT'); -- Asumiendo que el cliente con id 1 existe
