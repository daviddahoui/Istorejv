package com.istorejv.dao;

import com.istorejv.util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WhitelistDAO {

    private static final Logger LOGGER = Logger.getLogger(WhitelistDAO.class.getName());

    /**
     * Ajoute un e-mail à la whitelist.
     *
     * @param email L’e-mail à ajouter.
     * @return true si l’insertion a réussi, false sinon.
     */
    public static boolean addEmailToWhitelist(String email) {
        if (email == null || email.trim().isEmpty()) {
            LOGGER.warning("L'email fourni est null ou vide.");
            return false;
        }
        String sql = "INSERT INTO whitelist (email) VALUES (?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email.trim());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de l'ajout de l'email à la whitelist: " + email, e);
        }
        return false;
    }

    /**
     * Supprime un e-mail de la whitelist.
     *
     * @param email L’e-mail à supprimer.
     * @return true si la suppression a réussi, false sinon.
     */
    public static boolean removeEmailFromWhitelist(String email) {
        if (email == null || email.trim().isEmpty()) {
            LOGGER.warning("L'email fourni est null ou vide.");
            return false;
        }
        String sql = "DELETE FROM whitelist WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email.trim());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la suppression de l'email de la whitelist: " + email, e);
        }
        return false;
    }

    /**
     * Récupère tous les e-mails de la whitelist.
     *
     * @return une liste d’e-mails.
     */
    public static List<String> getAllWhitelistedEmails() {
        List<String> emails = new ArrayList<>();
        String sql = "SELECT email FROM whitelist";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                emails.add(rs.getString("email"));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération des emails de la whitelist.", e);
        }
        return emails;
    }
}
