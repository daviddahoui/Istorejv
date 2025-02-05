package com.javastore.istorejv.controller;

import com.javastore.istorejv.dao.ArticleDAO;
import com.javastore.istorejv.model.Article;
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

import java.util.Objects;

public class InventoryManagementController {

    @FXML
    private TextField articleNameField;

    @FXML
    private TextField articlePriceField;

    @FXML
    private TextField articleStockField;

    @FXML
    private ListView<String> articleListView;

    private ObservableList<String> articleList = FXCollections.observableArrayList();

    // Pour cet exemple, nous utilisons un identifiant d'inventaire fixe.
    // Dans une application complète, cette valeur serait déterminée dynamiquement.
    private int inventoryId = 1;

    @FXML
    public void initialize() {
        refreshArticleList();
    }

    @FXML
    private void handleAddArticleAction(ActionEvent event) {
        String name = articleNameField.getText().trim();
        String priceStr = articlePriceField.getText().trim();
        String stockStr = articleStockField.getText().trim();

        if (name.isEmpty() || priceStr.isEmpty() || stockStr.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Tous les champs doivent être remplis.");
            return;
        }

        try {
            double price = Double.parseDouble(priceStr);
            int stock = Integer.parseInt(stockStr);
            // L'ID sera généré automatiquement par la base de données (on passe 0)
            Article article = new Article(0, name, price, stock);
            boolean success = ArticleDAO.createArticle(article, inventoryId);
            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Article ajouté.");
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
        String selectedArticle = articleListView.getSelectionModel().getSelectedItem();
        if (selectedArticle == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez sélectionner un article à supprimer.");
            return;
        }
        // Format attendu : "ID: Nom - Prix€ - Stock: quantité"
        int id;
        try {
            id = Integer.parseInt(selectedArticle.split(":")[0].trim());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Format de l'article invalide.");
            return;
        }
        boolean deleted = ArticleDAO.deleteArticle(id);
        if (deleted) {
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Article supprimé.");
            refreshArticleList();
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la suppression de l'article.");
        }
    }

    @FXML
    private void handleUpdateArticleAction(ActionEvent event) {
        String selectedArticle = articleListView.getSelectionModel().getSelectedItem();
        if (selectedArticle == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez sélectionner un article à mettre à jour.");
            return;
        }
        int id;
        try {
            id = Integer.parseInt(selectedArticle.split(":")[0].trim());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Format de l'article invalide.");
            return;
        }
        // Récupère l'article depuis la base de données
        Article article = ArticleDAO.getArticleById(id);
        if (article != null) {
            String newStockStr = articleStockField.getText().trim();
            if (newStockStr.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez saisir une nouvelle quantité.");
                return;
            }
            try {
                int newStock = Integer.parseInt(newStockStr);
                article.setStockQuantity(newStock);
                boolean updated = ArticleDAO.updateArticle(article);
                if (updated) {
                    showAlert(Alert.AlertType.INFORMATION, "Succès", "Article mis à jour.");
                    refreshArticleList();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la mise à jour de l'article.");
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Quantité invalide.");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Article introuvable.");
        }
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


    private void refreshArticleList() {
        articleList.clear();
        // On récupère les articles pour l'inventaire défini
        for (Article article : ArticleDAO.getArticlesByInventoryId(inventoryId)) {
            String item = article.getId() + ": " + article.getName() + " - " +
                    article.getPrice() + "€ - Stock: " + article.getStockQuantity();
            articleList.add(item);
        }
        articleListView.setItems(articleList);
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
