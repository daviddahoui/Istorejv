package com.javastore.istorejv.dao;

import com.javastore.istorejv.model.User;
import com.javastore.istorejv.model.Role;
import com.javastore.istorejv.util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    /**
     * Recherche un utilisateur par email.
     *
     * @param email l'email de l'utilisateur recherché
     * @return un objet User s'il est trouvé, sinon null
     */
    public static User findByEmail(String email) {
        User user = null;
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String emailDb = rs.getString("email");
                    String pseudo = rs.getString("pseudo");
                    String passwordHash = rs.getString("password");
                    String roleStr = rs.getString("role");
                    user = new User(id, emailDb, pseudo, passwordHash, Role.valueOf(roleStr.toUpperCase()));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    /**
     * Retourne la liste de tous les utilisateurs.
     *
     * @return une List de User
     */
    public static List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String email = rs.getString("email");
                String pseudo = rs.getString("pseudo");
                String passwordHash = rs.getString("password");
                String roleStr = rs.getString("role");
                User user = new User(id, email, pseudo, passwordHash, Role.valueOf(roleStr.toUpperCase()));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    /**
     * Crée un nouvel utilisateur dans la base de données.
     *
     * @param user l'utilisateur à créer
     * @return true si l'insertion est réussie, false sinon
     */
    public static boolean createUser(User user) {
        String sql = "INSERT INTO users(email, pseudo, password, role) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getPseudo());
            stmt.setString(3, user.getPasswordHash());
            stmt.setString(4, user.getRole().toString());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Met à jour un utilisateur existant.
     *
     * @param user l'utilisateur avec les nouvelles informations
     * @return true si la mise à jour a réussi, false sinon
     */
    public static boolean updateUser(User user) {
        String sql = "UPDATE users SET email = ?, pseudo = ?, password = ?, role = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getPseudo());
            stmt.setString(3, user.getPasswordHash());
            stmt.setString(4, user.getRole().toString());
            stmt.setInt(5, user.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Supprime un utilisateur par son identifiant.
     *
     * @param id l'identifiant de l'utilisateur à supprimer
     * @return true si la suppression a réussi, false sinon
     */
    public static boolean deleteUserById(int id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Vérifie si l'email fait partie de la liste blanche.
     * Vous pouvez adapter cette méthode pour lire une table dédiée dans MySQL.
     *
     * @param email l'email à vérifier
     * @return true si l'email est autorisé, false sinon
     */
    /**
     * Vérifie si l'email fait partie de la whitelist.
     *
     * @param email l'email à vérifier
     * @return true si l'email est autorisé, false sinon
     */
    public static boolean isEmailWhitelisted(String email) {
        String sql = "SELECT COUNT(*) FROM whitelist WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
