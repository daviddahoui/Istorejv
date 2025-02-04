package com.javastore.istorejv;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends Application {

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    @Override
    public void start(Stage primaryStage) {
        try {
            // Chargement du fichier FXML pour la vue de connexion
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/istore/views/login.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            primaryStage.setScene(scene);
            primaryStage.setTitle("iStore - Connexion");
            primaryStage.show();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du chargement du fichier FXML", e);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
