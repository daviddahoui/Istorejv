package com.javastore.istorejv.dao;

import com.javastore.istorejv.model.Store;
import com.javastore.istorejv.util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StoreDAO {

    /**
     * Crée un nouveau magasin dans la table stores et crée l'inventaire associé.
     * Le magasin est ensuite renvoyé avec son identifiant généré.
     */
    public static boolean createStore(Store store) {
        String sql = "INSERT INTO stores(name) VALUES(?)";
        try (Connection conn = DBConnection.getConnection();
             // Permet de récupérer la clé auto-générée
             PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, store.getName());
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                // Récupérer l'identifiant généré
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int storeId = generatedKeys.getInt(1);
                    store.setId(storeId);
                }
                // Créer l'inventaire pour ce magasin
                InventoryDAO.createInventoryForStore(store.getId());
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean addEmployeeToStore(int storeId, int userId) {
        String sql = "INSERT INTO store_employees (store_id, user_id) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, storeId);
            stmt.setInt(2, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<com.javastore.istorejv.model.User> getEmployeesForStore(int storeId) {
        List<com.javastore.istorejv.model.User> employees = new ArrayList<>();
        String sql = "SELECT u.id, u.email, u.pseudo, u.password, u.role " +
                "FROM users u JOIN store_employees se ON u.id = se.user_id " +
                "WHERE se.store_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, storeId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String email = rs.getString("email");
                    String pseudo = rs.getString("pseudo");
                    String passwordHash = rs.getString("password");
                    String roleStr = rs.getString("role");
                    com.javastore.istorejv.model.User user = new com.javastore.istorejv.model.User(
                            id, email, pseudo, passwordHash, com.javastore.istorejv.model.Role.valueOf(roleStr.toUpperCase())
                    );
                    employees.add(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }

    /**
     * Récupère la liste de tous les magasins.
     */
    public static List<Store> getAllStores() {
        List<Store> stores = new ArrayList<>();
        String sql = "SELECT * FROM stores";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                // L'inventaire peut être chargé séparément si nécessaire.
                Store store = new Store(id, name, null);
                stores.add(store);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stores;
    }

    /**
     * Récupère un magasin par son identifiant.
     */
    public static Store getStoreById(int id) {
        String sql = "SELECT * FROM stores WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                Store store = new Store(id, name, null);
                return store;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Supprime un magasin de la base de données.
     * Attention : Vous devrez peut-être gérer la suppression en cascade (inventaire, affectations…).
     */
    public static boolean deleteStore(int id) {
        String sql = "DELETE FROM stores WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
