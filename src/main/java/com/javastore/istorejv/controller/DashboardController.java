package com.javastore.istorejv.controller;

import com.javastore.istorejv.dao.StoreDAO;
import com.javastore.istorejv.model.Role;
import com.javastore.istorejv.model.Store;
import com.javastore.istorejv.model.User;
import com.javastore.istorejv.util.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.List;
import java.util.Objects;

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
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/StoreManagementView.fxml")));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleInventoryManagementAction(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/InventoryManagementView.fxml")));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleUserManagementAction(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/UserManagementView.fxml")));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogoutAction(ActionEvent event) {
        SessionManager.clearSession();
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/LoginView.fxml")));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
