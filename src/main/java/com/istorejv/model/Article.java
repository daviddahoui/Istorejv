package com.istorejv.model;

/**
 * Représente un article dans l'inventaire.
 */
public class Article {
    private int id;
    private String name;
    private double price;
    private int stockQuantity;

    public Article(int id, String name, double price, int stockQuantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    // Ajoutez un constructeur sans argument si nécessaire (par exemple, pour la sérialisation)
    public Article() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }
}
