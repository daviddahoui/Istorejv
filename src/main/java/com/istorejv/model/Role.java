package com.istorejv.model;

/**
 * Enumération représentant les rôles des utilisateurs dans l'application.
 * <p>
 * Chaque rôle détermine les permissions et l'accès aux fonctionnalités de l'application.
 * </p>
 * <ul>
 *   <li><b>ADMIN</b> : Administrateur qui a tous les droits (création, modification et suppression de comptes, magasins, articles, etc.).</li>
 *   <li><b>EMPLOYEE</b> : Employé qui a accès uniquement aux informations et fonctionnalités du magasin auquel il est affecté.</li>
 *   <li><b>USER</b> : Utilisateur standard avec des droits d'accès limités (principalement en lecture sur les informations d'autres utilisateurs, sans accès aux opérations sensibles).</li>
 * </ul>
 */
public enum Role {
    ADMIN,
    EMPLOYEE,
    USER
}
