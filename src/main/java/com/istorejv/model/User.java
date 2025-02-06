package com.istorejv.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Représente un utilisateur dans l'application.
 * <p>
 * Un utilisateur est défini par son identifiant, son email, son pseudo, son mot de passe hashé et son rôle.
 * </p>
 */
@Getter
public class User {

    /**
     * L'identifiant de l'utilisateur.
     * <p>
     * Cet identifiant n'est pas final afin de permettre sa mise à jour après insertion en base de données.
     * </p>
     */
    @Setter
    private int id;

    /**
     * L'email de l'utilisateur.
     */
    private final String email;

    /**
     * Le pseudo de l'utilisateur.
     */
    private final String pseudo;

    /**
     * Le mot de passe hashé de l'utilisateur.
     */
    private final String passwordHash;

    /**
     * Le rôle de l'utilisateur (ADMIN ou USER).
     */
    private final Role role;

    /**
     * Construit un nouvel utilisateur.
     *
     * @param id           Le numéro d'identification de l'utilisateur.
     * @param email        L'email de l'utilisateur.
     * @param pseudo       Le pseudo de l'utilisateur.
     * @param passwordHash Le mot de passe hashé de l'utilisateur.
     * @param role         Le rôle de l'utilisateur (ADMIN ou USER).
     */
    public User(int id, String email, String pseudo, String passwordHash, Role role) {
        this.id = id;
        this.email = email;
        this.pseudo = pseudo;
        this.passwordHash = passwordHash;
        this.role = role;
    }
}
