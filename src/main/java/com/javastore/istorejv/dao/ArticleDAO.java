package com.javastore.istorejv.dao;

import com.javastore.istorejv.model.Article;
import com.javastore.istorejv.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ArticleDAO {

    private static final Logger LOGGER = Logger.getLogger(ArticleDAO.class.getName());

    /**
     * Crée un nouvel article dans un inventaire donné.
     *
     * @param article     L'article à créer. Doit être non null et avoir un nom défini.
     * @param inventoryId L'identifiant de l'inventaire auquel ajouter l'article.
     * @return true si l'insertion a réussi, false sinon.
     */
    public static boolean createArticle(Article article, int inventoryId) {
        if (article == null || article.getName() == null || article.getName().trim().isEmpty()) {
            LOGGER.warning("L'article est null ou son nom est vide.");
            return false;
        }
        String sql = "INSERT INTO articles(name, price, stockQuantity, inventory_id) VALUES(?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, article.getName().trim());
            stmt.setDouble(2, article.getPrice());
            stmt.setInt(3, article.getStockQuantity());
            stmt.setInt(4, inventoryId);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        int generatedId = rs.getInt(1);
                        article.setId(generatedId);
                    } else {
                        LOGGER.warning("Aucune clé générée lors de la création de l'article.");
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la création de l'article dans l'inventaire " + inventoryId, e);
        }
        return false;
    }

    /**
     * Récupère la liste des articles associés à un inventaire donné.
     *
     * @param inventoryId L'identifiant de l'inventaire.
     * @return Une liste d'articles.
     */
    public static List<Article> getArticlesByInventoryId(int inventoryId) {
        List<Article> articles = new ArrayList<>();
        String sql = "SELECT * FROM articles WHERE inventory_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, inventoryId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    double price = rs.getDouble("price");
                    int stockQuantity = rs.getInt("stockQuantity");
                    Article article = new Article(id, name, price, stockQuantity);
                    articles.add(article);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération des articles pour l'inventaire " + inventoryId, e);
        }
        return articles;
    }

    /**
     * Récupère la liste de tous les articles présents dans la table articles.
     *
     * @return Une liste d'articles.
     */
    public static List<Article> getAllArticles() {
        List<Article> articles = new ArrayList<>();
        String sql = "SELECT * FROM articles";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                int stockQuantity = rs.getInt("stockQuantity");
                Article article = new Article(id, name, price, stockQuantity);
                articles.add(article);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération de tous les articles.", e);
        }
        return articles;
    }

    /**
     * Récupère un article spécifique par son identifiant.
     *
     * @param id L'identifiant de l'article.
     * @return L'article correspondant ou null s'il n'est pas trouvé.
     */
    public static Article getArticleById(int id) {
        String sql = "SELECT * FROM articles WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("name");
                    double price = rs.getDouble("price");
                    int stockQuantity = rs.getInt("stockQuantity");
                    return new Article(id, name, price, stockQuantity);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération de l'article avec l'id: " + id, e);
        }
        return null;
    }

    /**
     * Met à jour un article existant dans la base de données.
     *
     * @param article L'article avec les informations mises à jour.
     * @return true si la mise à jour a réussi, false sinon.
     */
    public static boolean updateArticle(Article article) {
        if (article == null || article.getId() <= 0) {
            LOGGER.warning("L'article est null ou l'identifiant est invalide.");
            return false;
        }
        String sql = "UPDATE articles SET name = ?, price = ?, stockQuantity = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, article.getName().trim());
            stmt.setDouble(2, article.getPrice());
            stmt.setInt(3, article.getStockQuantity());
            stmt.setInt(4, article.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la mise à jour de l'article avec l'id: " + article.getId(), e);
        }
        return false;
    }

    /**
     * Supprime un article de la base de données.
     *
     * @param id L'identifiant de l'article à supprimer.
     * @return true si la suppression a réussi, false sinon.
     */
    public static boolean deleteArticle(int id) {
        String sql = "DELETE FROM articles WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la suppression de l'article avec l'id: " + id, e);
        }
        return false;
    }
}
