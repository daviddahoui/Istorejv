package com.javastore.istorejv.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static final String URL = "jdbc:mysql://localhost:3306/istore";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Méthode principale pour tester la connexion
    public static void main(String[] args) {
        try (Connection conn = getConnection()) {
            if (conn != null) {
                System.out.println("✅ Connexion à la base de données réussie !");
            } else {
                System.out.println("❌ Connexion échouée !");
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la connexion à la base de données : " + e.getMessage());
        }
    }
}

