package com.javastore.istorejv.controller;

import com.javastore.istorejv.service.AuthService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label messageLabel;

    @FXML
    protected void onLoginClick() {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (AuthService.loginUser(email, password)) {
            messageLabel.setText("Connexion réussie !");
        } else {
            messageLabel.setText("Échec de la connexion !");
        }
    }
}
