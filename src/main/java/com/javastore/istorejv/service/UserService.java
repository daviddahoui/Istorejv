package com.javastore.istorejv.service;

import com.javastore.istorejv.dao.UserDAO;
import com.javastore.istorejv.model.User;
import com.javastore.istorejv.util.PasswordUtils;

public class UserService {

    /**
     * Vérifie l’authentification et retourne l’utilisateur connecté si la vérification réussit.
     *
     * @param email    l'email de l'utilisateur
     * @param password le mot de passe saisi en clair
     * @return l'utilisateur correspondant ou null si l'authentification échoue
     */
    public static User login(String email, String password) {
        User user = UserDAO.findByEmail(email);
        if (user != null && PasswordUtils.verifyPassword(password, user.getPasswordHash())) {
            return user;
        }
        return null;
    }

    /**
     * Méthode d’authentification retournant un booléen.
     *
     * @param email    l'email de l'utilisateur
     * @param password le mot de passe saisi
     * @return true si l'authentification réussit, false sinon
     */
    public static boolean authenticate(String email, String password) {
        User user = UserDAO.findByEmail(email);
        if (user == null) {
            return false;
        }
        return PasswordUtils.verifyPassword(password, user.getPasswordHash());
    }

    /**
     * Crée un nouvel utilisateur après validation.
     *
     * @param email           l'email de l'utilisateur
     * @param pseudo          le pseudo de l'utilisateur
     * @param password        le mot de passe saisi en clair
     * @param confirmPassword la confirmation du mot de passe
     * @return "OK" en cas de succès ou un message d'erreur
     */
    public static String createUser(String email, String pseudo, String password, String confirmPassword) {
        if (email == null || email.isEmpty() ||
                pseudo == null || pseudo.isEmpty() ||
                password == null || password.isEmpty() ||
                confirmPassword == null || confirmPassword.isEmpty()) {
            return "Tous les champs doivent être remplis.";
        }
        if (!password.equals(confirmPassword)) {
            return "Les mots de passe ne correspondent pas.";
        }
        // Vérification de la liste blanche
        if (!UserDAO.isEmailWhitelisted(email)) {
            return "Cet email n'est pas autorisé à s'inscrire.";
        }
        // Vérification si l'utilisateur existe déjà
        if (UserDAO.findByEmail(email) != null) {
            return "Un compte avec cet email existe déjà.";
        }
        // Hachage du mot de passe et création de l'utilisateur avec rôle par défaut USER
        String hashedPassword = PasswordUtils.hashPassword(password);
        User newUser = new User(0, email, pseudo, hashedPassword, com.javastore.istorejv.model.Role.USER);
        boolean success = UserDAO.createUser(newUser);
        return success ? "OK" : "Erreur lors de la création du compte.";
    }
}
