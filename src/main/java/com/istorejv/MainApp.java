package com.istorejv;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Chargement du fichier FXML pour la vue de connexion
        Parent root = FXMLLoader.load(Objects.requireNonNull(
                getClass().getResource("/view/LoginView.fxml"),
                "Le fichier FXML /view/LoginView.fxml est introuvable. Vérifiez le chemin et la présence du fichier dans src/main/resources."));
        primaryStage.setTitle("iStore - Connexion");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
