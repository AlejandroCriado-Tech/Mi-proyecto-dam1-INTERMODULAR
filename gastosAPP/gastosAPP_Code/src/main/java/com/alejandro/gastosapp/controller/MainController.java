package com.alejandro.gastosapp.controller;

import com.alejandro.gastosapp.service.ConexionDB;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;

import java.io.IOException;

/**
 * Clase MainController — Controla la pantalla principal de GastosApp.
 *
 * Desde aquí el usuario puede navegar a:
 * - La pantalla de Gastos
 * - La pantalla de Presupuestos
 * - Cerrar la aplicación
 *
 * Autor: Alejandro Criado Pérez
 */
public class MainController {

    // ============================================================
    // @FXML — conecta los botones del archivo main.fxml con este código
    // El nombre de la variable debe coincidir exactamente con el
    // atributo "fx:id" del botón en el archivo .fxml
    // ============================================================
    @FXML private Button btnGastos;
    @FXML private Button btnPresupuestos;
    @FXML private Button btnSalir;

    // ============================================================
    // NAVEGAR A GASTOS
    // Cuando el usuario pulsa "Gastos", cargamos la pantalla gastos.fxml
    // ============================================================
    @FXML
    private void abrirGastos(ActionEvent event) {
        cambiarPantalla("/com/alejandro/gastosapp/view/gastos.fxml", "GastosApp — Mis Gastos");
    }

    // ============================================================
    // NAVEGAR A PRESUPUESTOS
    // Cuando el usuario pulsa "Presupuestos", cargamos presupuesto.fxml
    // ============================================================
    @FXML
    private void abrirPresupuestos(ActionEvent event) {
        cambiarPantalla("/com/alejandro/gastosapp/view/presupuesto.fxml", "GastosApp — Presupuestos");
    }

    // ============================================================
    // SALIR DE LA APLICACIÓN
    // Cierra la conexión con la BD y cierra la ventana
    // ============================================================
    @FXML
    private void salir(ActionEvent event) {
        ConexionDB.cerrarConexion(); // siempre cerramos la conexión antes de salir
        Stage stage = (Stage) btnSalir.getScene().getWindow();
        stage.close();
    }

    // ============================================================
    // MÉTODO AUXILIAR — cambiarPantalla()
    // Carga un archivo .fxml y lo muestra en la ventana actual.
    // Lo usamos para navegar entre pantallas sin abrir ventanas nuevas.
    // ============================================================
    private void cambiarPantalla(String rutaFxml, String titulo) {
        try {
            // FXMLLoader carga el archivo .fxml y crea la pantalla
            FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFxml));
            Parent root = loader.load();

            // Obtenemos la ventana actual y le ponemos la nueva pantalla
            Stage stage = (Stage) btnGastos.getScene().getWindow();
            stage.setTitle(titulo);
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            System.err.println("❌ Error al cargar la pantalla: " + e.getMessage());
        }
    }
}