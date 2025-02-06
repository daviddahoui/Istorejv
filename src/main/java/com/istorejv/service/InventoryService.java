package com.istorejv.service;

import com.istorejv.dao.ArticleDAO;
import com.istorejv.model.Article;

public class InventoryService {

    /**
     * Augmente le stock d'un article.
     * @param articleId L'identifiant de l'article.
     * @param amount La quantité à ajouter (doit être positive).
     * @return true si la mise à jour a réussi.
     */
    public static boolean increaseStock(int articleId, int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Le montant à ajouter doit être positif.");
        }
        Article article = ArticleDAO.getArticleById(articleId);
        if (article == null) {
            return false;
        }
        article.setStockQuantity(article.getStockQuantity() + amount);
        return ArticleDAO.updateArticle(article);
    }

    /**
     * Diminue le stock d'un article.
     * @param articleId L'identifiant de l'article.
     * @param amount La quantité à retirer (doit être positive).
     * @return true si la mise à jour a réussi, false si le stock est insuffisant.
     */
    public static boolean decreaseStock(int articleId, int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Le montant à retirer doit être positif.");
        }
        Article article = ArticleDAO.getArticleById(articleId);
        if (article == null) {
            return false;
        }
        if (article.getStockQuantity() < amount) {
            return false;
        }
        article.setStockQuantity(article.getStockQuantity() - amount);
        return ArticleDAO.updateArticle(article);
    }
}
