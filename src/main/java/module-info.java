module com.javastore.istorejv {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.sql;
    requires jbcrypt;
    requires static lombok;
    requires mysql.connector.j;

    opens com.javastore.istorejv to javafx.fxml;
    opens com.javastore.istorejv.controller to javafx.fxml; // Ouvre le package contrôleur

    exports com.javastore.istorejv;
    exports com.javastore.istorejv.controller; //Permet d'accéder aux classes du contrôleur
}
