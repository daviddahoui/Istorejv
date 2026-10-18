package com.istorejv.controller;

import com.istorejv.dao.StoreDAO;
import com.istorejv.model.Role;
import com.istorejv.model.Store;
import com.istorejv.model.User;
import com.istorejv.util.Navigation;
import com.istorejv.util.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.Node;

import java.util.List;

public class DashboardController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private Button userManagementButton;

    // ListView pour afficher la liste des magasins et des employés associés.
    @FXML
    private ListView<String> storeEmployeesListView;

    @FXML
    public void initialize() {
        User user = SessionManager.getLoggedInUser();
        if (user != null) {
            welcomeLabel.setText("Bienvenue " + user.getPseudo());
            if (user.getRole() == Role.ADMIN) {
                // Pour l'administrateur : afficher tous les boutons de gestion et la liste de tous les magasins avec leurs employés
                userManagementButton.setVisible(true);
                if (storeEmployeesListView != null) {
                    storeEmployeesListView.setVisible(true);
                    storeEmployeesListView.getItems().clear();
                    List<Store> allStores = StoreDAO.getAllStores();
                    if (allStores.isEmpty()) {
                        storeEmployeesListView.getItems().add("Aucun magasin n'est enregistré.");
                    } else {
                        for (Store store : allStores) {
                            storeEmployeesListView.getItems().add("Magasin: " + store.getName());
                            storeEmployeesListView.getItems().add("Employés affectés:");
                            List<User> employees = StoreDAO.getEmployeesForStore(store.getId());
                            if (employees.isEmpty()) {
                                storeEmployeesListView.getItems().add("  Aucun employé affecté.");
                            } else {
                                for (User emp : employees) {
                                    String item = "  " + emp.getId() + ": " + emp.getEmail() + " - " + emp.getPseudo() + " (" + emp.getRole() + ")";
                                    storeEmployeesListView.getItems().add(item);
                                }
                            }
                            storeEmployeesListView.getItems().add(""); // Ligne vide pour séparer les magasins
                        }
                    }
                }
            } else {
                // Pour un employé : masquer les options de gestion non autorisées
                userManagementButton.setVisible(false);
                if (storeEmployeesListView != null) {
                    // Afficher uniquement le magasin auquel l'employé est affecté
                    Store store = StoreDAO.getStoreForEmployee(user.getId());
                    if (store != null) {
                        storeEmployeesListView.setVisible(true);
                        storeEmployeesListView.getItems().clear();
                        storeEmployeesListView.getItems().add("Magasin: " + store.getName());
                        storeEmployeesListView.getItems().add("Employés affectés:");
                        List<User> employees = StoreDAO.getEmployeesForStore(store.getId());
                        if (employees.isEmpty()) {
                            storeEmployeesListView.getItems().add("  Aucun employé affecté.");
                        } else {
                            for (User emp : employees) {
                                String item = "  " + emp.getId() + ": " + emp.getEmail() + " - " + emp.getPseudo() + " (" + emp.getRole() + ")";
                                storeEmployeesListView.getItems().add(item);
                            }
                        }
                    } else {
                        storeEmployeesListView.setVisible(false);
                        welcomeLabel.setText("Bienvenue " + user.getPseudo() + " (aucun magasin attribué)");
                    }
                }
            }
        }
    }

    @FXML
    private void handleStoreManagementAction(ActionEvent event) {
        Navigation.goTo((Node) event.getSource(), "/view/StoreManagementView.fxml");
    }

    @FXML
    private void handleInventoryManagementAction(ActionEvent event) {
        Navigation.goTo((Node) event.getSource(), "/view/InventoryManagementView.fxml");
    }

    @FXML
    private void handleUserManagementAction(ActionEvent event) {
        Navigation.goTo((Node) event.getSource(), "/view/UserManagementView.fxml");
    }

    @FXML
    private void handleLogoutAction(ActionEvent event) {
        SessionManager.clearSession();
        Navigation.goTo((Node) event.getSource(), "/view/LoginView.fxml");
    }
}
