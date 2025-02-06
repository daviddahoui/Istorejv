package com.istorejv.model;

/**
 * Représente un utilisateur dans l'application.
 */
public class User {
    private int id;
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

    public int getId() {
        return id;
    }

    public void setId(int id) { // Permet de modifier l'ID après insertion en BDD
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public String getPseudo() {
        return pseudo;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public Role getRole() {
        return role;
    }
}
