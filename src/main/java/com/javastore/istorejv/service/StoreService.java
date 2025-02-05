package com.javastore.istorejv.service;

import com.javastore.istorejv.dao.StoreDAO;
import com.javastore.istorejv.model.Inventory;
import com.javastore.istorejv.model.Store;

public class StoreService {

    /**
     * Crée un nouveau magasin avec un inventaire vide.
     */
    public static boolean createStore(String storeName) {
        // Génération d'un identifiant (ici : nombre de magasins + 1)
        int newId = StoreDAO.getAllStores().size() + 1;
        Inventory inventory = new Inventory();
        Store store = new Store(newId, storeName, inventory);
        return StoreDAO.createStore(store);
    }
}
