module com.istorejv {
    // Modules JavaFX
    requires javafx.controls;
    requires javafx.fxml;

    // Autres dépendances
    requires org.controlsfx.controls;
    requires java.sql;
    requires jbcrypt;
    requires static lombok;
    requires mysql.connector.j;

    // Ouverture des packages pour l'injection FXML
    opens com.istorejv to javafx.fxml;
    opens com.istorejv.controller to javafx.fxml;
    opens com.istorejv.model to javafx.fxml; // Nécessaire si des classes du modèle sont référencées dans vos fichiers FXML
    opens com.istorejv.util to javafx.fxml;
    opens com.istorejv.dao to javafx.fxml;
    opens com.istorejv.service to javafx.fxml;

    // Exportation des packages pour qu'ils soient accessibles aux autres modules ou lors de l'exécution
    exports com.istorejv;
    exports com.istorejv.controller;
    exports com.istorejv.dao;
    exports com.istorejv.model;
    exports com.istorejv.service;
    exports com.istorejv.util;
}
