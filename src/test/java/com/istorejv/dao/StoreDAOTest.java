package com.istorejv.dao;

import com.javastore.istorejv.dao.StoreDAO;
import com.javastore.istorejv.model.Store;
import com.javastore.istorejv.util.DBConnection;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StoreDAOTest {

    @BeforeEach
    public void clearStores() throws SQLException {
        // Supprimer les magasins de test dont le nom commence par 'TestStore'
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM stores WHERE name LIKE 'TestStore%'")) {
            stmt.executeUpdate();
        }
    }

    @Test
    public void testCreateAndGetStore() {
        String storeName = "TestStore1";
        Store store = new Store(0, storeName, null);
        boolean created = StoreDAO.createStore(store);
        assertTrue(created, "La création du magasin doit réussir");

        List<Store> stores = StoreDAO.getAllStores();
        boolean found = stores.stream().anyMatch(s -> s.getName().equals(storeName));
        assertTrue(found, "Le magasin créé doit être présent dans la liste");

        // Optionnel : tester la suppression
        Store fetched = StoreDAO.getStoreById(store.getId());
        assertNotNull(fetched, "Le magasin doit être retrouvé par son ID");
        boolean deleted = StoreDAO.deleteStore(fetched.getId());
        assertTrue(deleted, "La suppression du magasin doit réussir");
    }
}
