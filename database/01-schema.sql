-- Structure de la base iStore (MySQL 8).
-- Exécuté automatiquement au premier démarrage du conteneur (voir docker-compose.yml).

SET NAMES utf8mb4;

CREATE TABLE IF NOT EXISTS users (
    id       INT AUTO_INCREMENT PRIMARY KEY,
    email    VARCHAR(255) NOT NULL UNIQUE,
    pseudo   VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL COMMENT 'Hash bcrypt',
    role     ENUM('ADMIN', 'EMPLOYEE', 'USER') NOT NULL DEFAULT 'USER'
);

CREATE TABLE IF NOT EXISTS stores (
    id   INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

-- Un inventaire par magasin, supprimé avec lui.
CREATE TABLE IF NOT EXISTS inventories (
    id       INT AUTO_INCREMENT PRIMARY KEY,
    store_id INT NOT NULL UNIQUE,
    CONSTRAINT fk_inventory_store FOREIGN KEY (store_id) REFERENCES stores (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS articles (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    name          VARCHAR(150)   NOT NULL,
    price         DECIMAL(10, 2) NOT NULL CHECK (price >= 0),
    stockQuantity INT            NOT NULL DEFAULT 0 CHECK (stockQuantity >= 0),
    inventory_id  INT            NOT NULL,
    CONSTRAINT fk_article_inventory FOREIGN KEY (inventory_id) REFERENCES inventories (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS store_employees (
    store_id INT NOT NULL,
    user_id  INT NOT NULL,
    PRIMARY KEY (store_id, user_id),
    CONSTRAINT fk_employee_store FOREIGN KEY (store_id) REFERENCES stores (id) ON DELETE CASCADE,
    CONSTRAINT fk_employee_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

-- Seuls les e-mails présents ici peuvent créer un compte.
CREATE TABLE IF NOT EXISTS whitelist (
    email VARCHAR(255) PRIMARY KEY
);
