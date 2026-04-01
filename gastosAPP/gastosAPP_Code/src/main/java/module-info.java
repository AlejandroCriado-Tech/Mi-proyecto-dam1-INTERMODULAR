module com.example.gastosapp_code {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.gastosapp_code to javafx.fxml;
    exports com.example.gastosapp_code;
}