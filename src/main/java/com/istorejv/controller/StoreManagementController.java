package com.istorejv.controller;

import com.istorejv.dao.StoreDAO;
import com.istorejv.model.Role;
import com.istorejv.model.Store;
import com.istorejv.model.User;
import com.istorejv.service.StoreService;
import com.istorejv.util.Navigation;
import com.istorejv.util.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 * Liste des magasins. Création et suppression réservées aux administrateurs (contrôlé par StoreService).
 */
public class StoreManagementController {

    @FXML private HBox adminActions;
    @FXML private Label readOnlyLabel;
    @FXML private TextField storeNameField;
    @FXML private ListView<Store> storeListView;

    @FXML
    public void initialize() {
        User user = SessionManager.getLoggedInUser();
        boolean admin = user != null && user.getRole() == Role.ADMIN;
        adminActions.setVisible(admin);
        adminActions.setManaged(admin);
        readOnlyLabel.setVisible(!admin);
        readOnlyLabel.setManaged(!admin);

        storeListView.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(Store store, boolean empty) {
                super.updateItem(store, empty);
                if (empty || store == null) {
                    setText(null);
                } else {
                    int employees = StoreDAO.getEmployeesForStore(store.getId()).size();
                    setText(store.getName() + "  ·  " + employees + (employees > 1 ? " employés" : " employé"));
                }
            }
        });
        refreshStoreList();
    }

    @FXML
    private void handleCreateStoreAction(ActionEvent event) {
        String storeName = storeNameField.getText() == null ? "" : storeNameField.getText().trim();
        if (storeName.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le nom du magasin est requis.");
            return;
        }
        String result = StoreService.createStore(new Store(0, storeName, null), SessionManager.getLoggedInUser());
        if ("OK".equals(result)) {
            storeNameField.clear();
            refreshStoreList();
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur", result);
        }
    }

    @FXML
    private void handleDeleteStoreAction(ActionEvent event) {
        Store selected = storeListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez sélectionner un magasin à supprimer.");
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Supprimer « " + selected.getName() + " » ainsi que son inventaire ?");
        confirm.setHeaderText(null);
        if (confirm.showAndWait().filter(b -> b.getButtonData().isDefaultButton()).isEmpty()) {
            return;
        }
        String result = StoreService.deleteStore(selected.getId(), SessionManager.getLoggedInUser());
        if ("OK".equals(result)) {
            refreshStoreList();
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur", result);
        }
    }

    @FXML
    private void handleBackToDashboardAction(ActionEvent event) {
        Navigation.goTo((Node) event.getSource(), "/view/DashboardView.fxml");
    }

    private void refreshStoreList() {
        storeListView.getItems().setAll(StoreDAO.getAllStores());
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
