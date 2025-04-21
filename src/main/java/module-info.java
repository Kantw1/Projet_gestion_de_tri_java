module com.example.gestion_dechet {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.junit.jupiter.api;

    opens com.example.gestion_dechet to javafx.fxml;
    exports com.example.gestion_dechet;
    opens model to javafx.fxml;
}