package com.javastore.istorejv.controller;

import com.javastore.istorejv.dao.UserDAO;
import com.javastore.istorejv.model.User;
import com.javastore.istorejv.model.Role;
import com.javastore.istorejv.util.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import java.util.List;

public class UserManagementController {

    @FXML
    private ListView<String> userListView;

    private ObservableList<String> userList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Seul un administrateur doit pouvoir accéder à cette vue
        if (SessionManager.getLoggedInUser() == null ||
                SessionManager.getLoggedInUser().getRole() != Role.ADMIN) {
            showAlert(Alert.AlertType.ERROR, "Accès refusé", "Vous n'êtes pas autorisé à accéder à la gestion des utilisateurs.");
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/view/DashboardView.fxml"));
                Stage stage = (Stage) userListView.getScene().getWindow();
                stage.setScene(new Scene(root));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        refreshUserList();
    }

    private void refreshUserList() {
        userList.clear();
        List<User> users = UserDAO.getAllUsers();
        for (User user : users) {
            userList.add(user.getId() + ": " + user.getEmail() + " - " + user.getPseudo() + " (" + user.getRole() + ")");
        }
        userListView.setItems(userList);
    }

    @FXML
    private void handleUpdateUserAction(ActionEvent event) {
        String selected = userListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez sélectionner un utilisateur à modifier.");
            return;
        }
        // Pour cet exemple, nous simulons la modification par un message.
        showAlert(Alert.AlertType.INFORMATION, "Modifier Utilisateur", "Fonctionnalité de modification à implémenter.");
        // TODO : Implémenter un formulaire de modification complet.
    }

    @FXML
    private void handleDeleteUserAction(ActionEvent event) {
        String selected = userListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez sélectionner un utilisateur à supprimer.");
            return;
        }
        int id = Integer.parseInt(selected.split(":")[0].trim());
        // L'utilisateur peut se supprimer lui-même ou l'admin peut supprimer n'importe quel compte.
        User currentUser = SessionManager.getLoggedInUser();
        if (currentUser.getId() == id || currentUser.getRole() == Role.ADMIN) {
            boolean deleted = UserDAO.deleteUserById(id);
            if (deleted) {
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Utilisateur supprimé.");
                refreshUserList();
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la suppression de l'utilisateur.");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Vous ne pouvez supprimer qu'un utilisateur vous-même ou agir en tant qu'administrateur.");
        }
    }

    @FXML
    private void handleBackAction(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/DashboardView.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
