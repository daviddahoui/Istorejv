package com.javastore.istorejv.item;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryItem {
    private int id;
    private String name;
    private double price;
    private int quantity;

    public InventoryItem(int id, String name, double price, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public void increaseStock(int amount) {
        if (amount > 0) this.quantity += amount;
    }

    public boolean decreaseStock(int amount) {
        if (amount > 0 && this.quantity >= amount) {
            this.quantity -= amount;
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("Item{id=%d, name='%s', price=%.2f, quantity=%d}",
                id, name, price, quantity);
    }
}
