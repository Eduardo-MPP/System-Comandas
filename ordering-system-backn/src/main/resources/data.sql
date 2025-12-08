-- Insertar roles y usuarios de prueba
INSERT INTO users (username, password, full_name, email, role, active) VALUES
('mesero1', '$2a$10$YourBCryptHashedPasswordHere', 'Juan Pérez', 'mesero1@restaurant.com', 'MESERO', true),
('cocinero1', '$2a$10$YourBCryptHashedPasswordHere', 'María García', 'cocinero1@restaurant.com', 'COCINERO', true),
('cajero1', '$2a$10$YourBCryptHashedPasswordHere', 'Carlos López', 'cajero1@restaurant.com', 'CAJERO', true),
('admin', '$2a$10$YourBCryptHashedPasswordHere', 'Admin Sistema', 'admin@restaurant.com', 'ADMIN', true);

-- Insertar mesas
INSERT INTO tables (table_number, status, zone, capacity) VALUES
('M-01', 'LIBRE', 'Salón Principal', 4),
('M-02', 'LIBRE', 'Salón Principal', 4),
('M-03', 'LIBRE', 'Salón Principal', 2),
('M-04', 'LIBRE', 'Terraza', 6),
('M-05', 'LIBRE', 'Terraza', 4);

-- Insertar categorías
INSERT INTO categories (name, description) VALUES
('Entradas', 'Platos de entrada'),
('Platos Principales', 'Platos principales'),
('Bebidas', 'Bebidas y refrescos'),
('Postres', 'Postres y dulces');

-- Insertar productos del menú
INSERT INTO menu_items (name, description, price, category_id, image_url, available, stock) VALUES
('Ensalada César', 'Ensalada fresca con aderezo César', 15.00, 1, NULL, true, NULL),
('Lomo Saltado', 'Lomo de res con papas fritas', 28.00, 2, NULL, true, NULL),
('Arroz con Pollo', 'Arroz con pollo y verduras', 22.00, 2, NULL, true, NULL),
('Coca Cola', 'Bebida gaseosa 500ml', 5.00, 3, NULL, true, 50),
('Chicha Morada', 'Bebida tradicional peruana', 4.00, 3, NULL, true, 30),
('Tiramisú', 'Postre italiano', 12.00, 4, NULL, true, NULL);
