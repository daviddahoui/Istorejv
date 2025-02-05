package com.javastore.istorejv.dao;

import com.javastore.istorejv.model.Article;
import com.javastore.istorejv.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InventoryDAO {

    private static final Logger LOGGER = Logger.getLogger(InventoryDAO.class.getName());

    /**
     * Crée un inventaire pour un magasin donné en insérant une ligne dans la table inventories.
     *
     * @param storeId L'identifiant du magasin pour lequel créer l'inventaire.
     * @return true si l'insertion a réussi, false sinon.
     */
    public static boolean createInventoryForStore(int storeId) {
        String sql = "INSERT INTO inventories(store_id) VALUES(?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, storeId);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                return true;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la création de l'inventaire pour le magasin avec l'id: " + storeId, e);
        }
        return false;
    }

    /**
     * Récupère l'identifiant de l'inventaire associé à un magasin.
     *
     * @param storeId L'identifiant du magasin.
     * @return l'identifiant de l'inventaire si trouvé, -1 sinon.
     */
    public static int getInventoryIdByStoreId(int storeId) {
        String sql = "SELECT id FROM inventories WHERE store_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, storeId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération de l'inventaire pour le magasin avec l'id: " + storeId, e);
        }
        return -1;
    }

    /**
     * Récupère la liste des articles pour un inventaire donné via l'identifiant du magasin.
     *
     * @param storeId L'identifiant du magasin.
     * @return la liste des articles associés à l'inventaire du magasin.
     */
    public static List<Article> getArticlesByStoreId(int storeId) {
        int inventoryId = getInventoryIdByStoreId(storeId);
        if (inventoryId == -1) {
            LOGGER.warning("Aucun inventaire trouvé pour le magasin avec l'id: " + storeId);
            return new ArrayList<>();
        }
        return ArticleDAO.getArticlesByInventoryId(inventoryId);
    }
}
