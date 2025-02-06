package com.istorejv.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils {

    /**
     * Hash le mot de passe en clair.
     *
     * @param plainTextPassword le mot de passe en clair
     * @return le mot de passe hashé
     */
    public static String hashPassword(String plainTextPassword) {
        // Génère un salt et retourne le hash du mot de passe
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    /**
     * Vérifie qu’un mot de passe en clair correspond au hash stocké.
     *
     * @param plainTextPassword le mot de passe en clair
     * @param hashedPassword le mot de passe hashé stocké
     * @return true si le mot de passe correspond, false sinon
     */
    public static boolean verifyPassword(String plainTextPassword, String hashedPassword) {
        return BCrypt.checkpw(plainTextPassword, hashedPassword);
    }
}

