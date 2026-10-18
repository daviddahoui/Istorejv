package com.istorejv.controller;

import com.istorejv.dao.ArticleDAO;
import com.istorejv.dao.InventoryDAO;
import com.istorejv.dao.StoreDAO;
import com.istorejv.model.Article;
import com.istorejv.model.Role;
import com.istorejv.model.Store;
import com.istorejv.model.User;
import com.istorejv.util.Navigation;
import com.istorejv.util.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;

import java.util.List;

/**
 * Inventaire d'un magasin.
 * <ul>
 *   <li>Administrateur : choisit n'importe quel magasin, ajoute, modifie et supprime des articles.</li>
 *   <li>Employé : voit uniquement le magasin auquel il est affecté et peut ajuster les stocks.</li>
 *   <li>Utilisateur : consultation seule du magasin auquel il est rattaché, s'il y en a un.</li>
 * </ul>
 */
public class InventoryManagementController {

    @FXML private ComboBox<Store> storeSelector;
    @FXML private Label scopeLabel;
    @FXML private ListView<Article> articleListView;
    @FXML private HBox articleForm;
    @FXML private TextField articleNameField;
    @FXML private TextField articlePriceField;
    @FXML private TextField articleStockField;
    @FXML private Button addButton;
    @FXML private Button updateButton;
    @FXML private Button deleteButton;

    private int inventoryId = -1;

    @FXML
    public void initialize() {
        User user = SessionManager.getLoggedInUser();
        boolean admin = user != null && user.getRole() == Role.ADMIN;
        boolean canAdjustStock = admin || (user != null && user.getRole() == Role.EMPLOYEE);

        storeSelector.setConverter(new StringConverter<>() {
            @Override public String toString(Store store) { return store == null ? "" : store.getName(); }
            @Override public Store fromString(String text) { return null; }
        });
        articleListView.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(Article article, boolean empty) {
                super.updateItem(article, empty);
                setText(empty || article == null ? null
                        : String.format("%s  ·  %.2f €  ·  stock : %d", article.getName(), article.getPrice(), article.getStockQuantity()));
            }
        });

        // Ajout et suppression d'articles : administrateurs uniquement.
        articleForm.setVisible(admin);
        articleForm.setManaged(admin);
        deleteButton.setVisible(admin);
        deleteButton.setManaged(admin);
        updateButton.setVisible(canAdjustStock);
        updateButton.setManaged(canAdjustStock);
        if (canAdjustStock && !admin) {
            // L'employé ne garde que le champ quantité, utilisé par « Mettre à jour le stock ».
            articleForm.setVisible(true);
            articleForm.setManaged(true);
            articleNameField.setVisible(false);
            articleNameField.setManaged(false);
            articlePriceField.setVisible(false);
            articlePriceField.setManaged(false);
            addButton.setVisible(false);
            addButton.setManaged(false);
            articleStockField.setPromptText("Nouvelle quantité");
            articleStockField.setPrefWidth(180);
        }

        List<Store> stores = admin ? StoreDAO.getAllStores() : assignedStore(user);
        storeSelector.getItems().setAll(stores);
        storeSelector.setDisable(!admin);
        storeSelector.valueProperty().addListener((obs, old, store) -> selectStore(store));

        if (stores.isEmpty()) {
            scopeLabel.setText(admin ? "Aucun magasin n'existe encore. Créez-en un depuis l'écran Magasins."
                    : "Vous n'êtes affecté à aucun magasin : aucun inventaire à afficher.");
            articleForm.setDisable(true);
            updateButton.setDisable(true);
            deleteButton.setDisable(true);
        } else {
            scopeLabel.setText(admin ? "Choisissez le magasin dont vous voulez gérer l'inventaire."
                    : canAdjustStock ? "Vous pouvez ajuster les quantités de votre magasin."
                    : "Consultation seule.");
            storeSelector.getSelectionModel().selectFirst();
        }
    }

    private static List<Store> assignedStore(User user) {
        Store store = user == null ? null : StoreDAO.getStoreForEmployee(user.getId());
        return store == null ? List.of() : List.of(store);
    }

    private void selectStore(Store store) {
        inventoryId = store == null ? -1 : InventoryDAO.getInventoryIdByStoreId(store.getId());
        refreshArticleList();
    }

    @FXML
    private void handleAddArticleAction(ActionEvent event) {
        if (inventoryId < 0) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Choisissez d'abord un magasin.");
            return;
        }
        String name = articleNameField.getText().trim();
        String priceStr = articlePriceField.getText().trim().replace(',', '.');
        String stockStr = articleStockField.getText().trim();
        if (name.isEmpty() || priceStr.isEmpty() || stockStr.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Tous les champs doivent être remplis.");
            return;
        }
        try {
            double price = Double.parseDouble(priceStr);
            int stock = Integer.parseInt(stockStr);
            if (price < 0 || stock < 0) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Le prix et la quantité doivent être positifs.");
                return;
            }
            if (ArticleDAO.createArticle(new Article(0, name, price, stock), inventoryId)) {
                articleNameField.clear();
                articlePriceField.clear();
                articleStockField.clear();
                refreshArticleList();
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'ajout de l'article.");
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Prix ou quantité invalide.");
        }
    }

    @FXML
    private void handleDeleteArticleAction(ActionEvent event) {
        Article selected = articleListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez sélectionner un article à supprimer.");
            return;
        }
        if (ArticleDAO.deleteArticle(selected.getId())) {
            refreshArticleList();
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la suppression de l'article.");
        }
    }

    @FXML
    private void handleUpdateArticleAction(ActionEvent event) {
        Article selected = articleListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez sélectionner un article à mettre à jour.");
            return;
        }
        String newStockStr = articleStockField.getText().trim();
        if (newStockStr.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Saisissez la nouvelle quantité dans le champ « Quantité ».");
            return;
        }
        try {
            int newStock = Integer.parseInt(newStockStr);
            if (newStock < 0) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "La quantité ne peut pas être négative.");
                return;
            }
            selected.setStockQuantity(newStock);
            if (ArticleDAO.updateArticle(selected)) {
                articleStockField.clear();
                refreshArticleList();
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la mise à jour de l'article.");
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Quantité invalide.");
        }
    }

    @FXML
    private void handleBackToDashboardAction(ActionEvent event) {
        Navigation.goTo((Node) event.getSource(), "/view/DashboardView.fxml");
    }

    private void refreshArticleList() {
        articleListView.getItems().setAll(inventoryId < 0 ? List.of() : ArticleDAO.getArticlesByInventoryId(inventoryId));
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
