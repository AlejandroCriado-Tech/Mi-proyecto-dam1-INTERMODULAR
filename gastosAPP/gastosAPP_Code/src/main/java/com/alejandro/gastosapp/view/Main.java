package com.alejandro.gastosapp.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Clase Main — Punto de entrada de GastosApp.
 *
 * Extiende Application, que es la clase base de JavaFX.
 * El método start() es el equivalente al main() en una app JavaFX:
 * se ejecuta automáticamente al arrancar y carga la primera pantalla.
 *
 * Autor: Alejandro Criado Pérez
 */
public class Main extends Application {

    // ============================================================
    // start() — se ejecuta al arrancar la aplicación
    // Stage es la ventana principal de JavaFX (como un marco de ventana)
    // Scene es el contenido que se muestra dentro de esa ventana
    // ============================================================
    @Override
    public void start(Stage stage) throws Exception {

        // Cargamos el archivo main.fxml como pantalla inicial
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/example/gastosapp_code/main.fxml"));
        Parent root = loader.load();

        // Configuramos la ventana principal
        stage.setTitle("💰 GastosApp — Alejandro Criado Pérez");
        stage.setScene(new Scene(root, 400, 450));
        stage.setResizable(false); // ventana de tamaño fijo
        stage.show();
    }

    // ============================================================
    // main() — punto de entrada de Java
    // Llama a launch() que arranca el ciclo de vida de JavaFX
    // ============================================================
    public static void main(String[] args) {
        launch(args);
    }
}