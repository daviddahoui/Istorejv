package com.istorejv.controller;

import com.istorejv.util.Navigation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import com.istorejv.service.UserService;


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
            Navigation.goTo(emailField, "/view/LoginView.fxml");
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
        Navigation.goTo(emailField, "/view/LoginView.fxml");
    }
}
