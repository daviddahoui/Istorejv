package com.istorejv.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Fournit les connexions à la base MySQL.
 * <p>
 * Les paramètres se règlent avec les variables d'environnement ISTORE_DB_URL, ISTORE_DB_USER et
 * ISTORE_DB_PASSWORD. Sans elles, les valeurs par défaut correspondent au docker-compose.yml du projet.
 * </p>
 */
public class DBConnection {
    private static final String URL = env("ISTORE_DB_URL",
            "jdbc:mysql://localhost:3306/istoredb?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC");
    private static final String USER = env("ISTORE_DB_USER", "istore");
    private static final String PASSWORD = env("ISTORE_DB_PASSWORD", "istore");

    private static String env(String name, String fallback) {
        String value = System.getenv(name);
        return value == null || value.isBlank() ? fallback : value;
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
