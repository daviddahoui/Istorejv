package com.istorejv.dao;

import com.javastore.istorejv.dao.UserDAO;
import com.javastore.istorejv.model.Role;
import com.javastore.istorejv.model.User;
import com.javastore.istorejv.util.DBConnection;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserDAOTest {

    @BeforeAll
    public void setupDatabase() throws SQLException {
        // Optionnel : Nettoyer la table pour éviter les interférences avec d'autres tests
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM users WHERE email LIKE 'testuser%@example.com'")) {
            stmt.executeUpdate();
        }
    }

    @Test
    public void testFindByEmailNotExisting() {
        User user = UserDAO.findByEmail("nonexistent@example.com");
        assertNull(user, "L'utilisateur n'existe pas, la méthode doit retourner null");
    }

    @Test
    public void testCreateAndFindUser() {
        String email = "testuser1@example.com";
        String pseudo = "TestUser1";
        // Pour le test, vous pouvez utiliser PasswordUtils.hashPassword("votreMotDePasse")
        String passwordHash = "dummyhash";
        User newUser = new User(0, email, pseudo, passwordHash, Role.USER);

        // Créer l'utilisateur dans la base de données
        boolean created = UserDAO.createUser(newUser);
        assertTrue(created, "La création de l'utilisateur doit réussir");

        // Vérifier que l'utilisateur peut être retrouvé via son email
        User fetched = UserDAO.findByEmail(email);
        assertNotNull(fetched, "L'utilisateur créé doit être trouvé");
        assertEquals(pseudo, fetched.getPseudo(), "Le pseudo doit correspondre");

        // Nettoyage : supprimer l'utilisateur de test
        boolean deleted = UserDAO.deleteUserById(fetched.getId());
        assertTrue(deleted, "L'utilisateur de test doit être supprimé");
    }

    @Test
    public void testUpdateUser() {
        String email = "testuser2@example.com";
        String pseudo = "TestUser2";
        String passwordHash = "dummyhash";
        User newUser = new User(0, email, pseudo, passwordHash, Role.USER);
        boolean created = UserDAO.createUser(newUser);
        assertTrue(created, "La création de l'utilisateur doit réussir");

        User fetched = UserDAO.findByEmail(email);
        assertNotNull(fetched, "L'utilisateur doit être retrouvé après création");

        // Mise à jour du pseudo
        User updatedUser = new User(fetched.getId(), email, "UpdatedUser", passwordHash, Role.USER);
        boolean updated = UserDAO.updateUser(updatedUser);
        assertTrue(updated, "La mise à jour de l'utilisateur doit réussir");

        User fetchedUpdated = UserDAO.findByEmail(email);
        assertEquals("UpdatedUser", fetchedUpdated.getPseudo(), "Le pseudo mis à jour doit être 'UpdatedUser'");

        // Nettoyage
        boolean deleted = UserDAO.deleteUserById(fetchedUpdated.getId());
        assertTrue(deleted, "L'utilisateur de test doit être supprimé après mise à jour");
    }

    @Test
    public void testIsEmailWhitelisted() {
        assertTrue(UserDAO.isEmailWhitelisted("admin@example.com"), "L'email admin@example.com doit être en liste blanche");
        assertFalse(UserDAO.isEmailWhitelisted("notallowed@example.com"), "Cet email ne doit pas être en liste blanche");
    }
}
