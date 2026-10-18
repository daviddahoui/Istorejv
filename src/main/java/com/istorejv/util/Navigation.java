package com.istorejv.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;

import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Passe d'un écran à l'autre en remplaçant le contenu de la fenêtre courante.
 * La scène est conservée : la taille de la fenêtre et la feuille de style restent les mêmes.
 */
public final class Navigation {
    private static final Logger LOGGER = Logger.getLogger(Navigation.class.getName());

    private Navigation() {
    }

    public static Parent load(String view) throws IOException {
        return FXMLLoader.load(Objects.requireNonNull(Navigation.class.getResource(view),
                "Vue introuvable : " + view));
    }

    /**
     * @param from n'importe quel élément de l'écran actuel
     * @param view chemin de la vue, par exemple "/view/DashboardView.fxml"
     */
    public static void goTo(Node from, String view) {
        try {
            from.getScene().setRoot(load(view));
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Impossible d'ouvrir la vue " + view, e);
        }
    }
}
