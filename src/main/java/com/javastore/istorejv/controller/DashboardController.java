package com.javastore.istorejv.controller;

import com.javastore.istorejv.model.Role;
import com.javastore.istorejv.model.User;
import com.javastore.istorejv.util.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DashboardController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private Button userManagementButton;

    @FXML
    public void initialize() {
        User user = SessionManager.getLoggedInUser();
        if (user != null) {
            welcomeLabel.setText("Bienvenue " + user.getPseudo());
            // Afficher le bouton de gestion des utilisateurs uniquement pour l'administrateur.
            if (user.getRole() == Role.ADMIN) {
                userManagementButton.setVisible(true);
            } else {
                userManagementButton.setVisible(false);
            }
        }
    }

    @FXML
    private void handleStoreManagementAction(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/StoreManagementView.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleInventoryManagementAction(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/InventoryManagementView.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleUserManagementAction(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/UserManagementView.fxml"));
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
            Parent root = FXMLLoader.load(getClass().getResource("/view/LoginView.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
