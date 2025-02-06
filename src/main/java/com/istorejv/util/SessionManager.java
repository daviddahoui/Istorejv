package com.istorejv.util;

import com.istorejv.model.User;

/**
 * Gère la session utilisateur.
 */
public class SessionManager {
    private static User loggedInUser;

    public static User getLoggedInUser() {
        return loggedInUser;
    }

    public static void setLoggedInUser(User user) {
        loggedInUser = user;
    }

    public static void clearSession() {
        loggedInUser = null;
    }
}
