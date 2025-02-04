package com.javastore.istorejv.service;

import com.javastore.istorejv.config.Database;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AuthService {

    private static final Logger LOGGER = Logger.getLogger(AuthService.class.getName());

    /**
     * Vérifie si l'e-mail fourni est dans la liste blanche.
     *
     * @param email l'adresse e-mail à vérifier
     * @return {@code true} si l'e-mail est whitelisté, sinon {@code false}
     */
    public static boolean isEmailWhitelisted(String email) {
        if (email == null || email.trim().isEmpty()) {
            LOGGER.warning("L'email fourni est null ou vide.");
            return false;
        }

        String query = "SELECT email FROM whitelisted_emails WHERE email = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                boolean whitelisted = rs.next();
                if (!whitelisted) {
                    LOGGER.info("L'email " + email + " n'est pas dans la liste blanche.");
                }
                return whitelisted;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la vérification de l'email whitelisté : " + email, e);
            return false;
        }
    }

    /**
     * Enregistre un utilisateur dans la base de données après vérification de l'email et hachage du mot de passe.
     *
     * @param email    l'adresse e-mail de l'utilisateur
     * @param password le mot de passe en clair
     * @return {@code true} si l'inscription est réussie, sinon {@code false}
     */
    public static boolean registerUser(String email, String password) {
        if (email == null || password == null || email.trim().isEmpty() || password.isEmpty()) {
            LOGGER.warning("L'email ou le mot de passe est null ou vide.");
            return false;
        }

        if (!isEmailWhitelisted(email)) {
            LOGGER.warning("Tentative d'inscription avec un email non whitelisté : " + email);
            return false;
        }

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        String query = "INSERT INTO users (email, hashed_password) VALUES (?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            stmt.setString(2, hashedPassword);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                LOGGER.info("Inscription réussie pour l'email : " + email);
                return true;
            } else {
                LOGGER.warning("Aucune ligne insérée lors de l'inscription pour l'email : " + email);
                return false;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de l'inscription de l'utilisateur avec l'email : " + email, e);
            return false;
        }
    }

    /**
     * Authentifie un utilisateur en vérifiant que le mot de passe correspond à celui enregistré (haché).
     *
     * @param email    l'adresse e-mail de l'utilisateur
     * @param password le mot de passe en clair fourni lors de la connexion
     * @return {@code true} si l'authentification est réussie, sinon {@code false}
     */
    public static boolean loginUser(String email, String password) {
        if (email == null || password == null || email.trim().isEmpty() || password.isEmpty()) {
            LOGGER.warning("L'email ou le mot de passe est null ou vide lors de la tentative de connexion.");
            return false;
        }

        String query = "SELECT hashed_password FROM users WHERE email = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String hashedPassword = rs.getString("hashed_password");
                    boolean isValid = BCrypt.checkpw(password, hashedPassword);
                    if (isValid) {
                        LOGGER.info("Authentification réussie pour l'email : " + email);
                    } else {
                        LOGGER.info("Mot de passe incorrect pour l'email : " + email);
                    }
                    return isValid;
                } else {
                    LOGGER.info("Aucun utilisateur trouvé pour l'email : " + email);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de l'authentification pour l'email : " + email, e);
        }
        return false;
    }
}
