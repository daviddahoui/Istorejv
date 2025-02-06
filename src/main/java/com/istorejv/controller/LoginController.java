package com.istorejv.controller;

import com.istorejv.model.User;
import com.istorejv.service.UserService;
import com.istorejv.util.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Objects;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    /**
     * Méthode appelée lors du clic sur "Se connecter".
     * Elle authentifie l’utilisateur, stocke sa session et le redirige vers le Dashboard.
     */
    @FXML
    private void handleLoginAction(ActionEvent event) {
        String email = emailField.getText().trim();
        String password = passwordField.getText();

        // Vérifier que les champs ne sont pas vides
        if (email.isEmpty() || password.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs.");
            alert.showAndWait();
            return;
        }

        User user = UserService.login(email, password);
        if (user != null) {
            SessionManager.setLoggedInUser(user);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Connexion réussie");
            alert.setHeaderText(null);
            alert.setContentText("Bienvenue " + user.getPseudo() + " !");
            alert.showAndWait();
            try {
                Parent dashboardRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/DashboardView.fxml")));
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(dashboardRoot));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de connexion");
            alert.setHeaderText(null);
            alert.setContentText("Email ou mot de passe incorrect.");
            alert.showAndWait();
        }
    }

    /**
     * Redirige vers la vue de création de compte.
     */
    @FXML
    private void handleCreateAccountAction(ActionEvent event) {
        try {
            Parent createAccountRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/CreateAccountView.fxml")));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(createAccountRoot));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
