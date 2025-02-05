package com.istorejv.dao;

import com.javastore.istorejv.dao.ArticleDAO;
import com.javastore.istorejv.model.Article;
import com.javastore.istorejv.util.DBConnection;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ArticleDAOTest {

    private int testInventoryId;

    @BeforeAll
    public void setupInventory() throws SQLException {
        // Créer un magasin de test pour obtenir un inventaire valide.
        String createStoreSql = "INSERT INTO stores(name) VALUES(?)";
        int testStoreId;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(createStoreSql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, "Test Store");
            int affected = stmt.executeUpdate();
            assertTrue(affected > 0, "La création du magasin de test doit réussir");
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    testStoreId = rs.getInt(1);
                } else {
                    fail("Aucune clé générée lors de la création du magasin de test.");
                    return; // Pour la compilation, jamais atteint en cas d'échec.
                }
            }
        }

        // Créer un inventaire de test en utilisant l'identifiant du magasin de test créé
        String sql = "INSERT INTO inventories(store_id) VALUES(?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, testStoreId);
            int affected = stmt.executeUpdate();
            assertTrue(affected > 0, "La création de l'inventaire de test doit réussir");
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    testInventoryId = rs.getInt(1);
                } else {
                    fail("Aucune clé générée lors de la création de l'inventaire de test.");
                }
            }
        }
    }

    @AfterAll
    public void cleanupInventory() throws SQLException {
        // Supprimer l'inventaire de test.
        String sql = "DELETE FROM inventories WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, testInventoryId);
            stmt.executeUpdate();
        }
    }

    @BeforeEach
    public void clearArticles() throws SQLException {
        // Supprimer tous les articles associés à l'inventaire de test pour isoler chaque test.
        String sql = "DELETE FROM articles WHERE inventory_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, testInventoryId);
            stmt.executeUpdate();
        }
    }

    @Test
    public void testCreateAndGetArticle() {
        // Création d'un article de test
        Article article = new Article(0, "TestArticle1", 9.99, 100);
        boolean created = ArticleDAO.createArticle(article, testInventoryId);
        assertTrue(created, "La création de l'article doit réussir");

        // Vérifier que l'article apparaît dans la liste des articles de l'inventaire de test
        List<Article> articles = ArticleDAO.getArticlesByInventoryId(testInventoryId);
        boolean found = articles.stream().anyMatch(a -> a.getName().equals("TestArticle1"));
        assertTrue(found, "L'article créé doit être retrouvé dans l'inventaire");

        // Test de mise à jour : modification de la quantité en stock
        article.setStockQuantity(150);
        boolean updated = ArticleDAO.updateArticle(article);
        assertTrue(updated, "La mise à jour de l'article doit réussir");

        List<Article> updatedArticles = ArticleDAO.getArticlesByInventoryId(testInventoryId);
        Article updatedArticle = updatedArticles.stream()
                .filter(a -> a.getId() == article.getId())
                .findFirst()
                .orElse(null);
        assertNotNull(updatedArticle, "L'article mis à jour doit être retrouvé");
        assertEquals(150, updatedArticle.getStockQuantity(), "La quantité en stock doit être mise à jour");

        // Test de suppression : supprimer l'article et vérifier qu'il n'est plus présent
        boolean deleted = ArticleDAO.deleteArticle(article.getId());
        assertTrue(deleted, "La suppression de l'article doit réussir");

        List<Article> articlesAfterDeletion = ArticleDAO.getArticlesByInventoryId(testInventoryId);
        boolean stillPresent = articlesAfterDeletion.stream().anyMatch(a -> a.getId() == article.getId());
        assertFalse(stillPresent, "L'article doit être supprimé et introuvable après suppression");
    }
}
