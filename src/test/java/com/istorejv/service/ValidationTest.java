package com.istorejv.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Règles de validation vérifiées avant tout accès à la base.
 */
class ValidationTest {

    @Test
    void createUserRefusesEmptyFields() {
        assertEquals("Tous les champs doivent être remplis.",
                UserService.createUser("", "pseudo", "secret1", "secret1"));
    }

    @Test
    void createUserRefusesDifferentPasswords() {
        assertEquals("Les mots de passe ne correspondent pas.",
                UserService.createUser("a@example.com", "pseudo", "secret1", "secret2"));
    }

    @Test
    void stockChangesMustBePositive() {
        assertThrows(IllegalArgumentException.class, () -> InventoryService.increaseStock(1, -5));
        assertThrows(IllegalArgumentException.class, () -> InventoryService.decreaseStock(1, -5));
    }
}
