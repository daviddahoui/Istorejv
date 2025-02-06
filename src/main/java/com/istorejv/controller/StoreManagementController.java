package com.istorejv.controller;

import com.istorejv.dao.StoreDAO;
import com.istorejv.model.Store;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.List;
import java.util.Objects;

public class StoreManagementController {

    @FXML
    private TextField storeNameField;

    @FXML
    private ListView<String> storeListView;

    private final ObservableList<String> storeList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        refreshStoreList();
    }

    @FXML
    private void handleCreateStoreAction(ActionEvent event) {
        String storeName = storeNameField.getText();
        if (storeName == null || storeName.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le nom du magasin est requis.");
            return;
        }
        Store store = new Store(0, storeName, null);
        boolean success = StoreDAO.createStore(store);
        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Magasin créé avec succès.");
            storeNameField.clear();
            refreshStoreList();
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la création du magasin.");
        }
    }

    @FXML
    private void handleDeleteStoreAction(ActionEvent event) {
        String selectedStore = storeListView.getSelectionModel().getSelectedItem();
        if (selectedStore == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez sélectionner un magasin à supprimer.");
            return;
        }
        // Format attendu : "ID: Nom"
        int id = Integer.parseInt(selectedStore.split(":")[0].trim());
        boolean deleted = StoreDAO.deleteStore(id);
        if (deleted) {
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Magasin supprimé.");
            refreshStoreList();
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la suppression du magasin.");
        }
    }

    private void refreshStoreList() {
        storeList.clear();
        List<Store> stores = StoreDAO.getAllStores();
        for (Store store : stores) {
            storeList.add(store.getId() + ": " + store.getName());
        }
        storeListView.setItems(storeList);
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    private void handleBackToDashboardAction(ActionEvent event) {
        try {
            Parent dashboardRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/DashboardView.fxml")));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(dashboardRoot));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
