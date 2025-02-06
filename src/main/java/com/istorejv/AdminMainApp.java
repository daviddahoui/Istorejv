package com.istorejv;

import com.istorejv.model.Role;
import com.istorejv.model.User;
import com.istorejv.util.SessionManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.Objects;

public class AdminMainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Crée un utilisateur admin fictif.
        // Vous pouvez soit créer un utilisateur directement, soit le récupérer depuis la base de données.
        User adminUser = new User(
                1,
                "admin@example.com",
                "Admin",
                "$2a$10$TKh8H1.PCjKXQK1EoZOJeOG13aGrQK5GhiKhU.v/hlH80AAeKQy7S", // Hash du mot de passe "admin"
                Role.ADMIN
        );
        SessionManager.setLoggedInUser(adminUser);

        // Charge directement la vue du Dashboard.
        Parent dashboardRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/DashboardView.fxml")));
        primaryStage.setTitle("iStore - Dashboard (Admin Mode)");
        primaryStage.setScene(new Scene(dashboardRoot));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
