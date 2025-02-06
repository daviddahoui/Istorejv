package com.istorejv.service;

import com.istorejv.dao.StoreDAO;
import com.istorejv.model.Store;
import com.istorejv.model.User;
import com.istorejv.model.Role;
import java.util.List;

public class StoreService {

    /**
     * Crée un nouveau magasin. Seul un administrateur peut créer un magasin.
     */
    public static String createStore(Store store, User currentUser) {
        if (currentUser.getRole() != Role.ADMIN) {
            return "Seul un administrateur peut créer un magasin.";
        }
        boolean success = StoreDAO.createStore(store);
        return success ? "OK" : "Erreur lors de la création du magasin.";
    }

    /**
     * Supprime un magasin. Seul un administrateur peut supprimer un magasin.
     */
    public static String deleteStore(int storeId, User currentUser) {
        if (currentUser.getRole() != Role.ADMIN) {
            return "Seul un administrateur peut supprimer un magasin.";
        }
        boolean success = StoreDAO.deleteStore(storeId);
        return success ? "OK" : "Erreur lors de la suppression du magasin.";
    }

    /**
     * Ajoute un employé à un magasin.
     */
    public static String addEmployeeToStore(int storeId, int userId, User currentUser) {
        if (currentUser.getRole() != Role.ADMIN) {
            return "Seul un administrateur peut ajouter des employés aux magasins.";
        }
        boolean success = StoreDAO.addEmployeeToStore(storeId, userId);
        return success ? "OK" : "Erreur lors de l'ajout de l'employé au magasin.";
    }

    /**
     * Récupère la liste des employés affectés à un magasin.
     */
    public static List<User> getEmployeesForStore(int storeId) {
        return StoreDAO.getEmployeesForStore(storeId);
    }
}
