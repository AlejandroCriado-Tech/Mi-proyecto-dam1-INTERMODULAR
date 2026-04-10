module com.alejandro.gastosapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires static lombok;

    opens com.alejandro.gastosapp.controller to javafx.fxml;
    opens com.alejandro.gastosapp.model to javafx.base;
    opens com.alejandro.gastosapp.view to javafx.fxml;

    exports com.alejandro.gastosapp.view;
}