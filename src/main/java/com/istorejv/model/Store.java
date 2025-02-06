package com.istorejv.model;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

/**
 * Représente un magasin dans l'application.
 * Un magasin possède un identifiant, un nom, un inventaire associé et une liste d'employés ayant accès à ce magasin.
 */
@Getter
@Setter
public class Store {
    private int id;
    private String name;
    private Inventory inventory;
    private List<User> employees; // Liste des utilisateurs ayant accès à ce magasin

    /**
     * Constructeur pour créer un magasin.
     *
     * @param id        l'identifiant du magasin
     * @param name      le nom du magasin
     * @param inventory l'inventaire associé au magasin
     */
    public Store(int id, String name, Inventory inventory) {
        this.id = id;
        this.name = name;
        this.inventory = inventory;
    }
}
