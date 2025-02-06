package com.istorejv.controller;

import com.istorejv.dao.StoreDAO;
import com.istorejv.dao.UserDAO;
import com.istorejv.dao.WhitelistDAO;
import com.istorejv.model.Role;
import com.istorejv.model.Store;
import com.istorejv.model.User;
import com.istorejv.service.UserService;
import com.istorejv.util.PasswordUtils;
import com.istorejv.util.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class UserManagementController {

    @FXML
    private ListView<String> userListView;

    @FXML
    public void initialize() {
        refreshUserList();
    }

    /**
     * Met à jour l'email, le pseudo et le mot de passe d'un utilisateur.
     * Seul l'utilisateur lui-même ou un administrateur peut modifier un compte.
     */
    @FXML
    private void handleUpdateUserAction(ActionEvent event) {
        String selected = userListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez sélectionner un utilisateur à modifier.");
            return;
        }
        int userId = Integer.parseInt(selected.split(":")[0].trim());
        User userToUpdate = UserDAO.findById(userId);
        if (userToUpdate == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Utilisateur non trouvé.");
            return;
        }

        // Vérification des droits : seul l'utilisateur lui-même ou un administrateur peut modifier
        User currentUser = SessionManager.getLoggedInUser();
        if (currentUser.getId() != userToUpdate.getId() && currentUser.getRole() != Role.ADMIN) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Vous ne pouvez modifier que votre propre compte.");
            return;
        }

        // Saisir le nouveau pseudo
        TextInputDialog pseudoDialog = new TextInputDialog(userToUpdate.getPseudo());
        pseudoDialog.setTitle("Mise à jour du compte");
        pseudoDialog.setHeaderText("Modification du pseudo de l'utilisateur");
        pseudoDialog.setContentText("Nouveau pseudo :");
        Optional<String> pseudoResult = pseudoDialog.showAndWait();
        if (pseudoResult.isEmpty() || pseudoResult.get().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le pseudo ne peut pas être vide.");
            return;
        }
        String newPseudo = pseudoResult.get().trim();

        // Saisir le nouvel email
        TextInputDialog emailDialog = new TextInputDialog(userToUpdate.getEmail());
        emailDialog.setTitle("Mise à jour du compte");
        emailDialog.setHeaderText("Modification de l'email de l'utilisateur");
        emailDialog.setContentText("Nouvel email :");
        Optional<String> emailResult = emailDialog.showAndWait();
        if (emailResult.isEmpty() || emailResult.get().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "L'email ne peut pas être vide.");
            return;
        }
        String newEmail = emailResult.get().trim();
        // Vérifier le format de l'email
        if (!newEmail.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Format d'email invalide.");
            return;
        }

        // Saisir le nouveau mot de passe (laisser vide pour conserver l'ancien)
        TextInputDialog passwordDialog = new TextInputDialog();
        passwordDialog.setTitle("Mise à jour du compte");
        passwordDialog.setHeaderText("Modification du mot de passe de l'utilisateur");
        passwordDialog.setContentText("Nouveau mot de passe (laisser vide pour conserver l'ancien) :");
        Optional<String> passwordResult = passwordDialog.showAndWait();
        String newHashedPassword;
        if (passwordResult.isPresent() && !passwordResult.get().trim().isEmpty()) {
            String newPassword = passwordResult.get().trim();
            if (newPassword.length() < 6) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Le mot de passe doit comporter au moins 6 caractères.");
                return;
            }
            newHashedPassword = PasswordUtils.hashPassword(newPassword);
        } else {
            newHashedPassword = userToUpdate.getPasswordHash();
        }

        // Création de l'objet utilisateur mis à jour
        User updatedUser = new User(userToUpdate.getId(), newEmail, newPseudo, newHashedPassword, userToUpdate.getRole());
        String updateResult = UserService.updateUser(currentUser, updatedUser);
        showAlert(Alert.AlertType.INFORMATION, "Résultat", updateResult);
        refreshUserList();
    }

    /**
     * Met à jour le rôle d'un utilisateur.
     * Seul un administrateur peut modifier le rôle d'un utilisateur.
     */
    @FXML
    private void handleUpdateUserRoleAction(ActionEvent event) {
        String selected = userListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez sélectionner un utilisateur à mettre à jour.");
            return;
        }
        int userId = Integer.parseInt(selected.split(":")[0].trim());
        User userToUpdate = UserDAO.findById(userId);
        if (userToUpdate == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Utilisateur non trouvé.");
            return;
        }
        TextInputDialog dialog = new TextInputDialog(userToUpdate.getRole().toString());
        dialog.setTitle("Mise à jour du rôle");
        dialog.setHeaderText("Modification du rôle de l'utilisateur");
        dialog.setContentText("Nouveau rôle (ADMIN/USER/EMPLOYEE) :");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String newRoleStr = result.get().trim().toUpperCase();
            try {
                Role newRole = Role.valueOf(newRoleStr);
                User updatedUser = new User(
                        userToUpdate.getId(),
                        userToUpdate.getEmail(),
                        userToUpdate.getPseudo(),
                        userToUpdate.getPasswordHash(),
                        newRole
                );
                String updateResult = UserService.updateUser(SessionManager.getLoggedInUser(), updatedUser);
                showAlert(Alert.AlertType.INFORMATION, "Résultat", updateResult);
                refreshUserList();
            } catch (IllegalArgumentException ex) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Le rôle entré est invalide. Veuillez entrer ADMIN/USER/EMPLOYEE.");
            }
        }
    }

    /**
     * Supprime un utilisateur.
     * Seul l'utilisateur lui-même ou un administrateur peut supprimer un compte.
     */
    @FXML
    private void handleDeleteUserAction(ActionEvent event) {
        String selected = userListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez sélectionner un utilisateur à supprimer.");
            return;
        }
        int userId = Integer.parseInt(selected.split(":")[0].trim());
        User currentUser = SessionManager.getLoggedInUser();
        String result = UserService.deleteUser(currentUser, userId);
        showAlert(Alert.AlertType.INFORMATION, "Résultat", result);
        refreshUserList();
    }

    /**
     * Ajoute un nouvel utilisateur à la whitelist.
     * L'administrateur saisit les informations complètes pour créer l'utilisateur,
     * puis l'email est ajouté à la whitelist.
     */
    @FXML
    private void handleAddUserToWhitelistAction(ActionEvent event) {
        // Saisir l'email
        TextInputDialog emailDialog = new TextInputDialog();
        emailDialog.setTitle("Ajouter utilisateur à la whitelist");
        emailDialog.setHeaderText("Entrez l'email de l'utilisateur :");
        Optional<String> emailResult = emailDialog.showAndWait();
        if (emailResult.isEmpty() || emailResult.get().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "L'email ne peut être vide.");
            return;
        }
        String email = emailResult.get().trim();
        // Vérifier le format de l'email (regex simple)
        if (!email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Format d'email invalide.");
            return;
        }

        // Saisir le pseudo
        TextInputDialog pseudoDialog = new TextInputDialog();
        pseudoDialog.setTitle("Ajouter utilisateur à la whitelist");
        pseudoDialog.setHeaderText("Entrez le pseudo de l'utilisateur :");
        Optional<String> pseudoResult = pseudoDialog.showAndWait();
        if (pseudoResult.isEmpty() || pseudoResult.get().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le pseudo ne peut être vide.");
            return;
        }
        String pseudo = pseudoResult.get().trim();

        // Saisir le mot de passe
        TextInputDialog passwordDialog = new TextInputDialog();
        passwordDialog.setTitle("Ajouter utilisateur à la whitelist");
        passwordDialog.setHeaderText("Entrez le mot de passe de l'utilisateur :");
        Optional<String> passwordResult = passwordDialog.showAndWait();
        if (passwordResult.isEmpty() || passwordResult.get().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le mot de passe ne peut être vide.");
            return;
        }
        String password = passwordResult.get().trim();
        if (password.length() < 6) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le mot de passe doit comporter au moins 6 caractères.");
            return;
        }

        // Saisir le rôle
        TextInputDialog roleDialog = new TextInputDialog("USER");
        roleDialog.setTitle("Ajouter utilisateur à la whitelist");
        roleDialog.setHeaderText("Entrez le rôle de l'utilisateur (ADMIN/USER/EMPLOYEE) :");
        Optional<String> roleResult = roleDialog.showAndWait();
        if (roleResult.isEmpty() || roleResult.get().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le rôle ne peut être vide.");
            return;
        }
        String roleStr = roleResult.get().trim().toUpperCase();
        Role role;
        try {
            role = Role.valueOf(roleStr);
        } catch (IllegalArgumentException ex) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le rôle entré est invalide. Veuillez entrer ADMIN/USER/EMPLOYEE.");
            return;
        }

        // Hachage du mot de passe
        String hashedPassword = PasswordUtils.hashPassword(password);

        // Création de l'utilisateur
        User newUser = new User(0, email, pseudo, hashedPassword, role);
        boolean created = UserDAO.createUser(newUser);
        if (created) {
            boolean whitelisted = WhitelistDAO.addEmailToWhitelist(email);
            if (whitelisted) {
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Utilisateur créé et ajouté à la whitelist.");
            } else {
                showAlert(Alert.AlertType.WARNING, "Attention", "Utilisateur créé, mais l'ajout à la whitelist a échoué.");
            }
            refreshUserList();
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la création de l'utilisateur.");
        }
    }

    /**
     * Attribue un magasin à un employé.
     * L'administrateur saisit l'ID du magasin à attribuer à l'utilisateur sélectionné.
     */
    @FXML
    private void handleAssignStoreToEmployeeAction(ActionEvent event) {
        String selected = userListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez sélectionner un utilisateur.");
            return;
        }
        int userId = Integer.parseInt(selected.split(":")[0].trim());
        // Saisir l'ID du magasin
        TextInputDialog storeDialog = new TextInputDialog();
        storeDialog.setTitle("Attribuer un magasin");
        storeDialog.setHeaderText("Attribuer un magasin à l'utilisateur sélectionné");
        storeDialog.setContentText("Entrez l'ID du magasin :");
        Optional<String> storeResult = storeDialog.showAndWait();
        if (storeResult.isEmpty() || storeResult.get().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "L'ID du magasin ne peut être vide.");
            return;
        }
        int storeId;
        try {
            storeId = Integer.parseInt(storeResult.get().trim());
        } catch (NumberFormatException ex) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "L'ID du magasin doit être un nombre.");
            return;
        }
        // Vérifier si le magasin existe
        Store store = StoreDAO.getStoreById(storeId);
        if (store == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Magasin non trouvé pour l'ID fourni.");
            return;
        }
        boolean success = StoreDAO.addEmployeeToStore(storeId, userId);
        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Succès", "L'utilisateur a été affecté au magasin : " + store.getName());
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'attribution du magasin à l'utilisateur.");
        }
        refreshUserList();
    }

    /**
     * Retourne au Dashboard.
     */
    @FXML
    private void handleBackAction(ActionEvent event) {
        try {
            Parent dashboardRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/DashboardView.fxml")));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(dashboardRoot));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Recharge la liste des utilisateurs depuis la base.
     */
    private void refreshUserList() {
        List<User> users = UserDAO.getAllUsers();
        userListView.getItems().clear();
        for (User user : users) {
            // Format : "id: email - pseudo (ROLE)"
            String item = user.getId() + ": " + user.getEmail() + " - " + user.getPseudo() + " (" + user.getRole() + ")";
            userListView.getItems().add(item);
        }
    }

    /**
     * Affiche une alerte avec le type, le titre et le message spécifiés.
     */
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
