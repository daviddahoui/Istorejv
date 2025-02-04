package com.javastore.istorejv.controller;

import com.javastore.istorejv.service.AuthService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class LoginController {

    private static final Logger LOGGER = Logger.getLogger(LoginController.class.getName());

    // Expression régulière simple pour valider le format d'un email
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w-.]+@[\\w-]+\\.[a-z]{2,}$", Pattern.CASE_INSENSITIVE);

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label messageLabel;

    /**
     * Méthode appelée lors du clic sur le bouton de connexion.
     */
    @FXML
    protected void onLoginClick() {
        String email = emailField.getText().trim();
        String password = passwordField.getText();

        // Vérification des champs vides
        if (email.isEmpty() || password.isEmpty()) {
            showMessage("Veuillez remplir tous les champs.", AlertType.WARNING);
            return;
        }

        // Validation du format de l'e-mail
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            showMessage("Veuillez entrer une adresse e-mail valide.", AlertType.WARNING);
            return;
        }

        try {
            // Tentative d'authentification de l'utilisateur
            if (AuthService.loginUser(email, password)) {
                showMessage("Connexion réussie !", AlertType.INFORMATION);
            } else {
                showMessage("Échec de la connexion !", AlertType.ERROR);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la tentative de connexion : ", e);
            showMessage("Une erreur est survenue lors de la connexion.", AlertType.ERROR);
        }
    }

    /**
     * Affiche un message à l'utilisateur dans le label et via une alerte JavaFX.
     *
     * @param message Le message à afficher
     * @param type Le type d'alerte (Information, Warning, Error)
     */
    private void showMessage(String message, AlertType type) {
        messageLabel.setText(message);
        Alert alert = new Alert(type);
        alert.setTitle("Connexion");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
