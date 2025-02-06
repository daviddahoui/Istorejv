package com.istorejv.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Représente un inventaire contenant une liste d'articles.
 * <p>
 * Cette classe permet de gérer dynamiquement la collection d'articles,
 * en ajoutant ou retirant des articles de l'inventaire.
 * </p>
 */
public class Inventory {

    /**
     * La liste des articles présents dans l'inventaire.
     */
    private List<Article> articles;

    /**
     * Construit un nouvel inventaire vide.
     */
    public Inventory() {
        this.articles = new ArrayList<>();
    }

    /**
     * Retourne la liste des articles de l'inventaire.
     *
     * @return la liste des articles.
     */
    public List<Article> getArticles() {
        return articles;
    }

    /**
     * Définit la liste des articles de l'inventaire.
     *
     * @param articles la nouvelle liste d'articles.
     */
    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    /**
     * Ajoute un article à l'inventaire.
     *
     * @param article L'article à ajouter. Ne doit pas être null.
     * @throws IllegalArgumentException si l'article est null.
     */
    public void addArticle(Article article) {
        if (article == null) {
            throw new IllegalArgumentException("L'article ne peut pas être null.");
        }
        articles.add(article);
    }

    /**
     * Retire un article de l'inventaire.
     *
     * @param article L'article à retirer. Ne doit pas être null.
     * @throws IllegalArgumentException si l'article est null.
     */
    public void removeArticle(Article article) {
        if (article == null) {
            throw new IllegalArgumentException("L'article ne peut pas être null.");
        }
        articles.remove(article);
    }
}
