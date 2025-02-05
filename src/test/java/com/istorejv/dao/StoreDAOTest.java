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
        // Supprime tous les magasins de test dont le nom commence par "TestStore"
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM stores WHERE name LIKE 'TestStore%'")) {
            int deletedCount = stmt.executeUpdate();
            System.out.println("Stores supprimés avant test : " + deletedCount);
        }
    }

    @AfterEach
    public void cleanUpStores() throws SQLException {
        // Nettoyage supplémentaire pour être sûr que la base reste propre
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM stores WHERE name LIKE 'TestStore%'")) {
            stmt.executeUpdate();
        }
    }

    @Test
    public void testCreateAndGetStore() {
        // Préparation des données de test
        String storeName = "TestStore1";
        Store store = new Store(0, storeName, null);

        // Création du magasin
        boolean created = StoreDAO.createStore(store);
        assertTrue(created, "La création du magasin '" + storeName + "' doit réussir.");

        // Récupération de tous les magasins pour vérifier la présence du magasin créé
        List<Store> stores = StoreDAO.getAllStores();
        boolean found = stores.stream().anyMatch(s -> s.getName().equals(storeName));
        assertTrue(found, "Le magasin '" + storeName + "' créé doit être présent dans la liste.");

        // Test de récupération par ID
        Store fetched = StoreDAO.getStoreById(store.getId());
        assertNotNull(fetched, "Le magasin doit être retrouvé par son ID.");
        assertEquals(storeName, fetched.getName(), "Le nom du magasin récupéré doit correspondre à '" + storeName + "'.");

        // Test de suppression
        boolean deleted = StoreDAO.deleteStore(fetched.getId());
        assertTrue(deleted, "La suppression du magasin avec l'ID " + fetched.getId() + " doit réussir.");

        // Vérifier que le magasin n'est plus présent après suppression
        Store afterDelete = StoreDAO.getStoreById(fetched.getId());
        assertNull(afterDelete, "Le magasin doit être supprimé et introuvable après suppression.");
    }
}
