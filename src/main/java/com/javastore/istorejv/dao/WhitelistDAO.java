package com.javastore.istorejv.dao;

import com.javastore.istorejv.util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WhitelistDAO {

    /**
     * Ajoute un e-mail à la whitelist.
     * @param email L’e-mail à ajouter.
     * @return true si l’insertion a réussi.
     */
    public static boolean addEmailToWhitelist(String email) {
        String sql = "INSERT INTO whitelist (email) VALUES (?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Supprime un e-mail de la whitelist.
     * @param email L’e-mail à supprimer.
     * @return true si la suppression a réussi.
     */
    public static boolean removeEmailFromWhitelist(String email) {
        String sql = "DELETE FROM whitelist WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Récupère tous les e-mails de la whitelist.
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
            e.printStackTrace();
        }
        return emails;
    }

    /**
     * Vérifie si un e-mail est présent dans la whitelist.
     * @param email L’e-mail à vérifier.
     * @return true si l’e-mail est autorisé.
     */
    public static boolean isEmailWhitelisted(String email) {
        String sql = "SELECT COUNT(*) FROM whitelist WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
