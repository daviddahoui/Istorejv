package com.javastore.istorejv.dao;

import com.javastore.istorejv.config.Database;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TestDataInserter {

    // Méthode pour supprimer les données existantes dans les tables users et whitelisted_emails
    private static void deleteExistingData() {
        String deleteUserQuery = "DELETE FROM users WHERE email = ?";
        String deleteWhitelistQuery = "DELETE FROM whitelisted_emails WHERE email = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement deleteUserStmt = conn.prepareStatement(deleteUserQuery);
             PreparedStatement deleteWhitelistStmt = conn.prepareStatement(deleteWhitelistQuery)) {

            // Liste des emails à supprimer
            String[] emails = {"test1@example.com", "test2@example.com", "admin@istore.com"};

            // Supprimer les utilisateurs et les emails whitelistés
            for (String email : emails) {
                deleteUserStmt.setString(1, email);
                deleteUserStmt.executeUpdate();

                deleteWhitelistStmt.setString(1, email);
                deleteWhitelistStmt.executeUpdate();
            }

            System.out.println("Données existantes supprimées.");
        } catch (SQLException e) {
            e.printStackTrace();  // Affiche les erreurs SQL
        }
    }

    // Méthode pour insérer les nouvelles données
    public static void insertTestData() {
        deleteExistingData();  // Supprime les données existantes avant d'insérer

        try (Connection conn = Database.getConnection()) {
            String insertWhitelistQuery = "INSERT INTO whitelisted_emails (email) VALUES (?)";
            String insertUserQuery = "INSERT INTO users (email, hashed_password) VALUES (?, ?)";

            try (PreparedStatement whitelistStmt = conn.prepareStatement(insertWhitelistQuery);
                 PreparedStatement userStmt = conn.prepareStatement(insertUserQuery)) {

                // Liste des emails à insérer dans la table whitelisted_emails
                String[] emails = {"test1@example.com", "test2@example.com", "admin@istore.com"};
                for (String email : emails) {
                    whitelistStmt.setString(1, email);
                    whitelistStmt.executeUpdate();
                }

                // Insérer les utilisateurs avec des mots de passe hashés
                userStmt.setString(1, "test1@example.com");
                userStmt.setString(2, BCrypt.hashpw("password123", BCrypt.gensalt()));
                userStmt.executeUpdate();

                userStmt.setString(1, "test2@example.com");
                userStmt.setString(2, BCrypt.hashpw("securePass", BCrypt.gensalt()));
                userStmt.executeUpdate();

                userStmt.setString(1, "admin@istore.com");
                userStmt.setString(2, BCrypt.hashpw("adminPass", BCrypt.gensalt()));
                userStmt.executeUpdate();

                System.out.println("Données de test insérées avec succès !");
            }
        } catch (SQLException e) {
            e.printStackTrace();  // Affiche les erreurs SQL
        }
    }
}
