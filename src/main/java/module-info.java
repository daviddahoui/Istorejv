module com.javastore.istorejv {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.sql;
    requires jbcrypt;
    requires mysql.connector.j;

    opens com.javastore.istorejv to javafx.fxml;
    exports com.javastore.istorejv;
}