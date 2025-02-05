package com.javastore.istorejv.dao;

import com.javastore.istorejv.model.Store;
import com.javastore.istorejv.util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StoreDAO {

    private static final Logger LOGGER = Logger.getLogger(StoreDAO.class.getName());

    /**
     * Crée un nouveau magasin dans la table stores et crée l'inventaire associé.
     * Le magasin est ensuite mis à jour avec son identifiant généré.
     *
     * @param store l'objet Store à créer (le nom doit être défini)
     * @return true si l'insertion est réussie, false sinon
     */
    public static boolean createStore(Store store) {
        if (store == null || store.getName() == null || store.getName().trim().isEmpty()) {
            LOGGER.warning("Le magasin est null ou le nom du magasin est vide.");
            return false;
        }
        String sql = "INSERT INTO stores(name) VALUES(?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, store.getName().trim());
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int storeId = generatedKeys.getInt(1);
                        store.setId(storeId);
                    } else {
                        LOGGER.warning("Aucune clé générée lors de la création du magasin.");
                    }
                }
                // Crée l'inventaire associé au magasin
                if (!InventoryDAO.createInventoryForStore(store.getId())) {
                    LOGGER.warning("Échec de la création de l'inventaire pour le magasin avec l'id: " + store.getId());
                    return false;
                }
                return true;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la création du magasin: " + store.getName(), e);
        }
        return false;
    }

    /**
     * Ajoute un employé à un magasin.
     *
     * @param storeId L'identifiant du magasin.
     * @param userId  L'identifiant de l'utilisateur à ajouter.
     * @return true si l'insertion a réussi, false sinon.
     */
    public static boolean addEmployeeToStore(int storeId, int userId) {
        String sql = "INSERT INTO store_employees (store_id, user_id) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, storeId);
            stmt.setInt(2, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de l'ajout de l'employé " + userId + " au magasin " + storeId, e);
        }
        return false;
    }

    /**
     * Récupère la liste des employés affectés à un magasin.
     *
     * @param storeId L'identifiant du magasin.
     * @return une liste d'utilisateurs.
     */
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
                    // Création de l'utilisateur en utilisant le rôle converti en majuscules
                    com.javastore.istorejv.model.User user = new com.javastore.istorejv.model.User(
                            id, email, pseudo, passwordHash, com.javastore.istorejv.model.Role.valueOf(roleStr.toUpperCase())
                    );
                    employees.add(user);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération des employés pour le magasin " + storeId, e);
        }
        return employees;
    }
    /**
     * Récupère le magasin auquel un employé est affecté.
     *
     * @param userId L'identifiant de l'employé.
     * @return L'objet Store correspondant, ou null si aucun magasin n'est trouvé.
     */
    public static Store getStoreForEmployee(int userId) {
        String sql = "SELECT s.id, s.name " +
                "FROM stores s " +
                "JOIN store_employees se ON s.id = se.store_id " +
                "WHERE se.user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int storeId = rs.getInt("id");
                    String storeName = rs.getString("name");
                    return new Store(storeId, storeName, null);
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(StoreDAO.class.getName()).log(Level.SEVERE, "Erreur lors de la récupération du magasin pour l'employé " + userId, e);
        }
        return null;
    }

    /**
     * Récupère la liste de tous les magasins.
     *
     * @return une liste d'objets Store.
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
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération de tous les magasins.", e);
        }
        return stores;
    }

    /**
     * Récupère un magasin par son identifiant.
     *
     * @param id L'identifiant du magasin.
     * @return un objet Store s'il est trouvé, sinon null.
     */
    public static Store getStoreById(int id) {
        String sql = "SELECT * FROM stores WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("name");
                    return new Store(id, name, null);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération du magasin avec l'id: " + id, e);
        }
        return null;
    }

    /**
     * Supprime un magasin de la base de données.
     * Attention : Vous devrez peut-être gérer la suppression en cascade (inventaire, affectations…).
     *
     * @param id L'identifiant du magasin à supprimer.
     * @return true si la suppression a réussi, false sinon.
     */
    public static boolean deleteStore(int id) {
        String sql = "DELETE FROM stores WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la suppression du magasin avec l'id: " + id, e);
        }
        return false;
    }
}
