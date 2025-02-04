package com.javastore.istorejv.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Database {

    private static final Logger LOGGER = Logger.getLogger(Database.class.getName());

    // Vous pouvez externaliser ces valeurs via un fichier de configuration ou des variables d'environnement
    private static final String URL = System.getenv().getOrDefault("DB_URL", "jdbc:mysql://localhost:3306/istore");
    private static final String USER = System.getenv().getOrDefault("DB_USER", "root");
    private static final String PASSWORD = System.getenv().getOrDefault("DB_PASSWORD", "");

    // Chargement explicite du driver (facultatif avec les drivers récents)
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            LOGGER.log(Level.INFO, "Driver JDBC MySQL chargé avec succès.");
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Impossible de charger le driver JDBC MySQL.", e);
        }
    }

    /**
     * Obtient une connexion à la base de données.
     *
     * @return une instance de {@link Connection}
     * @throws SQLException si une erreur survient lors de l'établissement de la connexion
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * Méthode principale pour tester la connexion à la base de données.
     *
     * @param args les arguments de la ligne de commande
     */
    public static void main(String[] args) {
        try (Connection conn = getConnection()) {
            if (conn != null && !conn.isClosed()) {
                LOGGER.info("✅ Connexion à la base de données réussie !");
            } else {
                LOGGER.severe("❌ Connexion échouée !");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "❌ Erreur lors de la connexion à la base de données : {0}", e.getMessage());
        }
    }
}
