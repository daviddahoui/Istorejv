package com.istorejv.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import com.istorejv.service.UserService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class CreateAccountController {

    @FXML
    private TextField emailField;

    @FXML
    private TextField pseudoField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    /**
     * Gère la création du compte lors du clic sur "Créer un compte".
     */
    @FXML
    private void handleCreateAccountAction(ActionEvent event) {
        String email = emailField.getText();
        String pseudo = pseudoField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Appel du service pour créer l'utilisateur
        String result = UserService.createUser(email, pseudo, password, confirmPassword);
        if ("OK".equals(result)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Création de compte");
            alert.setHeaderText(null);
            alert.setContentText("Compte créé avec succès !");
            alert.showAndWait();
            // Redirection vers la vue de connexion
            try {
                Parent loginRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/LoginView.fxml")));
                Stage stage = (Stage) emailField.getScene().getWindow();
                stage.setScene(new Scene(loginRoot));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur lors de la création du compte");
            alert.setHeaderText(null);
            alert.setContentText(result);
            alert.showAndWait();
        }
    }

    /**
     * Annule la création de compte et retourne à la vue de connexion.
     */
    @FXML
    private void handleCancelAction(ActionEvent event) {
        try {
            Parent loginRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/LoginView.fxml")));
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(new Scene(loginRoot));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
