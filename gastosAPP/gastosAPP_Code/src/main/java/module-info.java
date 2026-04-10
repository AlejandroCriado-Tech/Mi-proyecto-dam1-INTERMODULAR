module com.example.gastosapp_code {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires static lombok;


    opens com.example.gastosapp_code to javafx.fxml;
    exports com.example.gastosapp_code;
}