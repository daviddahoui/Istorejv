package com.javastore.istorejv.dao;

import com.javastore.istorejv.config.Database;
import com.javastore.istorejv.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe DAO (Data Access Object) pour la gestion des opérations liées aux utilisateurs.
 */
public class UserDao {

    private static final Logger LOGGER = Logger.getLogger(UserDao.class.getName());

    /**
     * Récupère un utilisateur en fonction de son adresse e-mail.
     *
     * @param email L'adresse e-mail de l'utilisateur recherché.
     * @return Un objet {@link User} si l'utilisateur est trouvé, sinon {@code null}.
     */
    public User getUserByEmail(String email) {
        String query = "SELECT id, email, hashed_password FROM users WHERE email = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String userEmail = rs.getString("email");
                    String hashedPassword = rs.getString("hashed_password");
                    return new User(id, userEmail, hashedPassword);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération de l'utilisateur avec l'email : " + email, e);
        }
        return null;
    }

    /**
     * Insère un nouvel utilisateur dans la base de données.
     *
     * @param user L'objet {@link User} à insérer. Notez que l'ID de l'utilisateur sera généré par la base de données.
     * @return {@code true} si l'insertion a réussi, sinon {@code false}.
     */
    public boolean insertUser(User user) {
        String query = "INSERT INTO users (email, hashed_password) VALUES (?, ?)";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, user.email());
            stmt.setString(2, user.hashedPassword());
            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                // Optionnel : récupérer et utiliser la clé générée (id)
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        // Par exemple, vous pourriez vouloir créer un nouvel objet User avec l'ID généré
                        // int generatedId = generatedKeys.getInt(1);
                        // return new User(generatedId, user.getEmail(), user.getHashedPassword());
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de l'insertion de l'utilisateur avec l'email : " + user.email(), e);
        }
        return false;
    }
}
