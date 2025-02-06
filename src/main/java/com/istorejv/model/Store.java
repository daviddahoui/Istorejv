package com.istorejv.model;

import java.util.List;

/**
 * Représente un magasin dans l'application.
 */
public class Store {
    private int id;
    private final String name;
    private final Inventory inventory;
    private List<User> employees;

    public Store(int id, String name, Inventory inventory) {
        this.id = id;
        this.name = name;
        this.inventory = inventory;
    }

    public int getId() {
        return id;
    }

    // Setter pour l'ID (nécessaire pour la mise à jour après insertion)
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public List<User> getEmployees() {
        return employees;
    }

    public void setEmployees(List<User> employees) {
        this.employees = employees;
    }
}
