module com.javastore.istorejv {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires java.sql;
    requires jbcrypt;
    requires static lombok;
    requires mysql.connector.j;

    // Ouvre les packages utilisés dans le FXML pour l'injection de dépendances
    opens com.javastore.istorejv to javafx.fxml;
    opens com.javastore.istorejv.controller to javafx.fxml;
    opens com.javastore.istorejv.model to javafx.fxml; // Optionnel, si nécessaire

    // Exporte les packages pour l'accès public
    exports com.javastore.istorejv;
    exports com.javastore.istorejv.controller;
}
