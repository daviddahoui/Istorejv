package com.javastore.istorejv.controller;

import com.javastore.istorejv.item.InventoryItem;
import com.javastore.istorejv.model.Inventory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;

@Getter
public class InventoryController {
    private Inventory inventory;
    private ObservableList<InventoryItem> items;

    public InventoryController(Inventory inventory) {
        this.inventory = inventory;
        this.items = FXCollections.observableArrayList(inventory.getItems());
    }

    public void addItem(InventoryItem item) {
        inventory.addItem(item);
        items.add(item);
    }

    public boolean removeItem(int itemId) {
        boolean removed = inventory.removeItem(itemId);
        if (removed) items.removeIf(item -> item.getId() == itemId);
        return removed;
    }

    public boolean updateStock(int itemId, int amount, boolean increase) {
        InventoryItem item = inventory.getItemById(itemId);
        if (item != null) {
            if (increase) item.increaseStock(amount);
            else return item.decreaseStock(amount);
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "InventoryController managing " + inventory.getStore().getName();
    }
}

