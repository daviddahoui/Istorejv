package com.javastore.istorejv.controller;

import com.javastore.istorejv.dao.WhitelistDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import java.util.List;
import java.util.Objects;

public class WhitelistController {

    @FXML private TextField emailField;
    @FXML private ListView<String> whitelistListView;

    @FXML
    public void initialize() {
        refreshWhitelist();
    }

    @FXML
    private void handleAddEmail(ActionEvent event) {
        String email = emailField.getText().trim();
        if(email.isEmpty()){
            showAlert(Alert.AlertType.ERROR, "Erreur", "L'email ne peut être vide.");
            return;
        }
        boolean success = WhitelistDAO.addEmailToWhitelist(email);
        if(success) {
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Email ajouté à la whitelist.");
            emailField.clear();
            refreshWhitelist();
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'ajout de l'email.");
        }
    }

    @FXML
    private void handleRemoveEmail(ActionEvent event) {
        String selectedEmail = whitelistListView.getSelectionModel().getSelectedItem();
        if(selectedEmail == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez sélectionner un email à supprimer.");
            return;
        }
        boolean success = WhitelistDAO.removeEmailFromWhitelist(selectedEmail);
        if(success) {
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Email supprimé de la whitelist.");
            refreshWhitelist();
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la suppression de l'email.");
        }
    }

    @FXML
    private void handleBackToDashboard(ActionEvent event) {
        try {
            Parent dashboardRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/DashboardView.fxml")));
            Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(dashboardRoot));
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void refreshWhitelist() {
        List<String> emails = WhitelistDAO.getAllWhitelistedEmails();
        whitelistListView.getItems().setAll(emails);
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

