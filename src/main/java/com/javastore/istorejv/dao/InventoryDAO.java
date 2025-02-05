package com.javastore.istorejv.dao;

import com.javastore.istorejv.model.Article;
import com.javastore.istorejv.util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class InventoryDAO {

    /**
     * Crée un inventaire pour un magasin donné en insérant une ligne dans la table inventories.
     */
    public static void createInventoryForStore(int storeId) {
        String sql = "INSERT INTO inventories(store_id) VALUES(?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, storeId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Récupère l'identifiant de l'inventaire associé à un magasin.
     */
    public static int getInventoryIdByStoreId(int storeId) {
        String sql = "SELECT id FROM inventories WHERE store_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, storeId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Récupère la liste des articles pour un inventaire donné via son identifiant.
     */
    public static List<Article> getArticlesByStoreId(int storeId) {
        int inventoryId = getInventoryIdByStoreId(storeId);
        return ArticleDAO.getArticlesByInventoryId(inventoryId);
    }
}
