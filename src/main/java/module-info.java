module com.istorejv {
    // Modules JavaFX
    requires javafx.controls;
    requires javafx.fxml;

    // Autres dépendances
    requires org.controlsfx.controls;
    requires java.sql;
    requires jbcrypt;          // Vérifiez que le module automatique pour jBCrypt est bien "jbcrypt"
    requires static lombok;    // Lombok est utilisé à la compilation uniquement
    requires mysql.connector.j;

    // Ouverture des packages pour l'injection de dépendances FXML
    opens com.istorejv to javafx.fxml;
    opens com.istorejv.controller to javafx.fxml;
    opens com.istorejv.model to javafx.fxml; // Optionnel si des classes du modèle sont référencées dans vos fichiers FXML

    // Exportation des packages pour qu'ils soient accessibles aux autres modules
    exports com.istorejv;
    exports com.istorejv.controller;
}
