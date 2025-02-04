package com.javastore.istorejv.model;


public class User {
    private final int id;
    private final String email;
    private final String hashedPassword;

    public User(int id, String email, String hashedPassword) {
        this.id = id;
        this.email = email;
        this.hashedPassword = hashedPassword;
    }

    public int getId() { return id; }
    public String getEmail() { return email; }
    public String getHashedPassword() { return hashedPassword; }
}
