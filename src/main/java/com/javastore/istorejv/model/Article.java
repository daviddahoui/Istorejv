package com.javastore.istorejv.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Représente un article dans l'inventaire.
 * Contient l'identifiant, le nom, le prix et la quantité en stock.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Article {
    private int id;
    private String name;
    private double price;
    private int stockQuantity;

    /**
     * Augmente la quantité en stock.
     *
     * @param amount la quantité à ajouter (doit être positive)
     * @throws IllegalArgumentException si amount est négatif
     */
    public void addStock(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Le montant à ajouter doit être positif.");
        }
        this.stockQuantity += amount;
    }

    /**
     * Diminue la quantité en stock.
     *
     * @param amount la quantité à retirer (doit être positive)
     * @return true si l'opération a réussi, false si la quantité demandée dépasse le stock disponible
     * @throws IllegalArgumentException si amount est négatif
     */
    public boolean reduceStock(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Le montant à retirer doit être positif.");
        }
        if (this.stockQuantity - amount < 0) {
            return false; // Stock insuffisant
        }
        this.stockQuantity -= amount;
        return true;
    }
}
