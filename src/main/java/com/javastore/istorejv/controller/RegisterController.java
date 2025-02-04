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

public class RegisterController {

    private static final Logger LOGGER = Logger.getLogger(RegisterController.class.getName());

    // Expression régulière simple pour valider le format d'un email
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w-.]+@[\\w-]+\\.[a-z]{2,}$", Pattern.CASE_INSENSITIVE);

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Label messageLabel;

    @FXML
    protected void onRegisterClick() {
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Vérification des champs vides
        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showMessage("Veuillez remplir tous les champs.", AlertType.WARNING);
            return;
        }

        // Validation du format de l'email
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            showMessage("Veuillez entrer une adresse e-mail valide.", AlertType.WARNING);
            return;
        }

        // Vérification que les mots de passe correspondent
        if (!password.equals(confirmPassword)) {
            showMessage("Les mots de passe ne correspondent pas !", AlertType.WARNING);
            return;
        }

        // Tentative d'inscription de l'utilisateur
        try {
            boolean isRegistered = AuthService.registerUser(email, password);
            if (isRegistered) {
                showMessage("Inscription réussie !", AlertType.INFORMATION);
            } else {
                showMessage("Échec de l'inscription !", AlertType.ERROR);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de l'inscription : ", e);
            showMessage("Une erreur est survenue lors de l'inscription.", AlertType.ERROR);
        }
    }

    /**
     * Affiche un message à l'utilisateur via le label et une boîte de dialogue.
     *
     * @param message Le message à afficher
     * @param type Le type d'alerte (Information, Warning, Error)
     */
    private void showMessage(String message, AlertType type) {
        messageLabel.setText(message);
        Alert alert = new Alert(type);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
