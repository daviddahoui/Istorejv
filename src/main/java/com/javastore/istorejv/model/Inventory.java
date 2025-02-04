package com.javastore.istorejv.model;

import com.javastore.istorejv.item.InventoryItem;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Inventory {
    private Store store;
    private List<InventoryItem> items = new ArrayList<>();

    public Inventory(Store store) {
        this.store = store;
    }

    public void addItem(InventoryItem item) {
        items.add(item);
    }

    public boolean removeItem(int itemId) {
        return items.removeIf(item -> item.getId() == itemId);
    }

    public InventoryItem getItemById(int id) {
        return items.stream()
                .filter(item -> item.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public String toString() {
        return "Inventory for Store: " + store.getName() + ", Items: " + items.size();
    }
}
