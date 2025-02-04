package com.javastore.istorejv.model;

import java.util.Objects;

/**
 * Représente un utilisateur du système.
 * Cette classe est immutable.
 */
public record User(int id, String email, String hashedPassword) {
    /**
     * Construit un nouvel utilisateur.
     *
     * @param id             l'identifiant unique de l'utilisateur
     * @param email          l'adresse e-mail de l'utilisateur ; ne doit pas être null ou vide
     * @param hashedPassword le mot de passe haché ; ne doit pas être null ou vide
     * @throws IllegalArgumentException si l'email ou le mot de passe haché est null ou vide
     */
    public User {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("L'email ne peut pas être null ou vide.");
        }
        if (hashedPassword == null || hashedPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Le mot de passe haché ne peut pas être null ou vide.");
        }
    }

    /**
     * Retourne l'identifiant de l'utilisateur.
     *
     * @return l'identifiant unique
     */
    @Override
    public int id() {
        return id;
    }

    /**
     * Retourne l'adresse e-mail de l'utilisateur.
     *
     * @return l'e-mail
     */
    @Override
    public String email() {
        return email;
    }

    /**
     * Retourne le mot de passe haché de l'utilisateur.
     *
     * @return le mot de passe haché
     */
    @Override
    public String hashedPassword() {
        return hashedPassword;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && email.equals(user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }
}
