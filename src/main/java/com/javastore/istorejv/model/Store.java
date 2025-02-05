package com.javastore.istorejv.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class Store {
    // Getters et setters
    private int id;
    private String name;
    private Inventory inventory;
    private List<User> employees; // Liste des utilisateurs ayant accès à ce magasin

    public Store(int id, String name, Inventory inventory) {
        this.id = id;
        this.name = name;
        this.inventory = inventory;
    }

}
