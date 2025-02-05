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
        // Création d'un inventaire de test
        String sql = "INSERT INTO inventories(store_id) VALUES(?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, 9999); // Utiliser un identifiant de magasin fictif
            int affected = stmt.executeUpdate();
            assertTrue(affected > 0, "La création de l'inventaire de test doit réussir");
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    testInventoryId = rs.getInt(1);
                }
            }
        }
    }

    @AfterAll
    public void cleanupInventory() throws SQLException {
        // Suppression de l'inventaire de test
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM inventories WHERE id = ?")) {
            stmt.setInt(1, testInventoryId);
            stmt.executeUpdate();
        }
    }

    @BeforeEach
    public void clearArticles() throws SQLException {
        // Supprimer les articles associés à l'inventaire de test
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM articles WHERE inventory_id = ?")) {
            stmt.setInt(1, testInventoryId);
            stmt.executeUpdate();
        }
    }

    @Test
    public void testCreateAndGetArticle() {
        Article article = new Article(0, "TestArticle1", 9.99, 100);
        boolean created = ArticleDAO.createArticle(article, testInventoryId);
        assertTrue(created, "La création de l'article doit réussir");

        List<Article> articles = ArticleDAO.getArticlesByInventoryId(testInventoryId);
        boolean found = articles.stream().anyMatch(a -> a.getName().equals("TestArticle1"));
        assertTrue(found, "L'article créé doit être retrouvé dans l'inventaire");

        // Test de mise à jour
        article.setStockQuantity(150);
        boolean updated = ArticleDAO.updateArticle(article);
        assertTrue(updated, "La mise à jour de l'article doit réussir");

        List<Article> updatedArticles = ArticleDAO.getArticlesByInventoryId(testInventoryId);
        Article updatedArticle = updatedArticles.stream().filter(a -> a.getId() == article.getId()).findFirst().orElse(null);
        assertNotNull(updatedArticle, "L'article mis à jour doit être retrouvé");
        assertEquals(150, updatedArticle.getStockQuantity(), "La quantité en stock doit être mise à jour");

        // Test de suppression
        boolean deleted = ArticleDAO.deleteArticle(article.getId());
        assertTrue(deleted, "La suppression de l'article doit réussir");
    }
}

