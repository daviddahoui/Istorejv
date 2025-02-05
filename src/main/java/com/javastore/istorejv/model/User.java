package com.javastore.istorejv.model;

import lombok.Getter;

@Getter
public class User {
    private final int id;
    private final String email;
    private final String pseudo;
    private final String passwordHash;
    private final Role role;

    public User(int id, String email, String pseudo, String passwordHash, Role role) {
        this.id = id;
        this.email = email;
        this.pseudo = pseudo;
        this.passwordHash = passwordHash;
        this.role = role;
    }

}
