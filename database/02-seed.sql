-- Données de démonstration.
-- Comptes : admin@example.com / admin123, employe@example.com / employe123, client@example.com / client123

SET NAMES utf8mb4;

INSERT INTO users (id, email, pseudo, password, role) VALUES
    (1, 'admin@example.com',   'Admin',   '$2a$10$8BqfyvAvArbMnOdcI64L9.pyHfj74hqINv.LN2JolcelcEgD1d0Fq', 'ADMIN'),
    (2, 'employe@example.com', 'Camille', '$2a$10$G/UEK9yYA/DqAE0LtXI9oeoOW914Ibdu9mSOrgapkOyoqPjdMrV8O', 'EMPLOYEE'),
    (3, 'client@example.com',  'Sacha',   '$2a$10$Cusjtvh6XXNJDm4P7XsIpeffzC.3AXYWi4uhCJJhgjAVfgHDVQXdG', 'USER');

INSERT INTO whitelist (email) VALUES
    ('admin@example.com'), ('employe@example.com'), ('client@example.com'), ('nouveau@example.com');

INSERT INTO stores (id, name) VALUES (1, 'iStore Paris'), (2, 'iStore Tours');
INSERT INTO inventories (id, store_id) VALUES (1, 1), (2, 2);

INSERT INTO articles (name, price, stockQuantity, inventory_id) VALUES
    ('Écouteurs sans fil',  79.90, 24, 1),
    ('Chargeur USB-C 30 W', 24.90, 60, 1),
    ('Coque de protection', 14.90, 85, 1),
    ('Câble HDMI 2 m',      12.50, 40, 2),
    ('Clavier sans fil',    49.00, 12, 2),
    ('Souris ergonomique',  34.90, 18, 2);

-- L'employée travaille au magasin de Tours.
INSERT INTO store_employees (store_id, user_id) VALUES (2, 2);
