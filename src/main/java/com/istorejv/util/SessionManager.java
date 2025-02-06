package com.istorejv.util;

import com.istorejv.model.User;
import lombok.Setter;
import lombok.Getter;

public class SessionManager {
    @Getter
    @Setter
    private static User loggedInUser;

    public static void clearSession() {
        loggedInUser = null;
    }
}
