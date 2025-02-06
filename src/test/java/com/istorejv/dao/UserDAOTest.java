package com.istorejv.dao;

import com.istorejv.model.Role;
import com.istorejv.model.User;
import com.istorejv.util.DBConnection;
import com.istorejv.util.PasswordUtils;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserDAOTest {

    @BeforeAll
    public void setupDatabase() throws SQLException {
        // Nettoyer la table pour éviter les interférences avec d'autres tests.
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM users WHERE email LIKE 'testuser%@example.com'")) {
            int deleted = stmt.executeUpdate();
            System.out.println("Avant tests, utilisateurs supprimés: " + deleted);
        }
    }

    @AfterAll
    public void cleanUpDatabase() throws SQLException {
        // Nettoyer à nouveau après tous les tests (optionnel si @BeforeAll suffit)
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM users WHERE email LIKE 'testuser%@example.com'")) {
            stmt.executeUpdate();
        }
    }

    @Test
    public void testFindByEmailNotExisting() {
        // Teste la recherche d'un utilisateur inexistant.
        User user = UserDAO.findByEmail("nonexistent@example.com");
        assertNull(user, "La méthode findByEmail doit retourner null pour un email inexistant.");
    }

    @Test
    public void testCreateAndFindUser() {
        // Préparation des données de test
        String email = "testuser1@example.com";
        String pseudo = "TestUser1";
        // Utilisation d'un hash réaliste pour le mot de passe "password123"
        String passwordHash = PasswordUtils.hashPassword("password123");
        User newUser = new User(0, email, pseudo, passwordHash, Role.USER);

        // Création de l'utilisateur.
        boolean created = UserDAO.createUser(newUser);
        assertTrue(created, "La création de l'utilisateur avec l'email " + email + " doit réussir.");

        // Vérification que l'utilisateur peut être retrouvé via son email.
        User fetched = UserDAO.findByEmail(email);
        assertNotNull(fetched, "L'utilisateur créé doit être retrouvé via l'email " + email + ".");
        assertEquals(pseudo, fetched.getPseudo(), "Le pseudo doit correspondre à " + pseudo + ".");

        // Nettoyage : suppression de l'utilisateur de test.
        boolean deleted = UserDAO.deleteUserById(fetched.getId());
        assertTrue(deleted, "L'utilisateur de test avec l'ID " + fetched.getId() + " doit être supprimé.");
    }

    @Test
    public void testUpdateUser() {
        // Création d'un utilisateur de test.
        String email = "testuser2@example.com";
        String pseudo = "TestUser2";
        String passwordHash = PasswordUtils.hashPassword("password123");
        User newUser = new User(0, email, pseudo, passwordHash, Role.USER);
        boolean created = UserDAO.createUser(newUser);
        assertTrue(created, "La création de l'utilisateur avec l'email " + email + " doit réussir.");

        // Récupération de l'utilisateur créé.
        User fetched = UserDAO.findByEmail(email);
        assertNotNull(fetched, "L'utilisateur doit être retrouvé après création.");

        // Mise à jour du pseudo.
        String newPseudo = "UpdatedUser";
        User updatedUser = new User(fetched.getId(), email, newPseudo, passwordHash, Role.USER);
        boolean updated = UserDAO.updateUser(updatedUser);
        assertTrue(updated, "La mise à jour de l'utilisateur avec l'ID " + fetched.getId() + " doit réussir.");

        // Vérification que le pseudo a bien été mis à jour.
        User fetchedUpdated = UserDAO.findByEmail(email);
        assertNotNull(fetchedUpdated, "L'utilisateur mis à jour doit être retrouvé.");
        assertEquals(newPseudo, fetchedUpdated.getPseudo(), "Le pseudo doit être mis à jour en " + newPseudo + ".");

        // Nettoyage : suppression de l'utilisateur de test.
        boolean deleted = UserDAO.deleteUserById(fetchedUpdated.getId());
        assertTrue(deleted, "L'utilisateur de test avec l'ID " + fetchedUpdated.getId() + " doit être supprimé après mise à jour.");
    }

    @Test
    public void testIsEmailWhitelisted() {
        // Teste si les e-mails sont présents dans la whitelist.
        assertTrue(UserDAO.isEmailWhitelisted("admin@example.com"),
                "L'email admin@example.com doit être en liste blanche.");
        assertFalse(UserDAO.isEmailWhitelisted("notallowed@example.com"),
                "L'email notallowed@example.com ne doit pas être en liste blanche.");
    }
}
