package com.alejandro.gastosapp.controller;

import com.alejandro.gastosapp.model.Categoria;
import com.alejandro.gastosapp.model.Presupuesto;
import com.alejandro.gastosapp.service.CategoriaService;
import com.alejandro.gastosapp.service.GastoService;
import com.alejandro.gastosapp.service.PresupuestoService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Clase PresupuestoController — Controla la pantalla de presupuestos.
 *
 * Funcionalidades:
 * - Mostrar lista de presupuestos en una tabla
 * - Añadir nuevo presupuesto
 * - Editar presupuesto seleccionado
 * - Eliminar presupuesto seleccionado
 * - Mostrar resumen mensual con alerta de gasto
 *
 * Autor: Alejandro Criado Pérez
 */
public class PresupuestoController implements Initializable {

    private static final int ID_USUARIO = 1;

    private final PresupuestoService presupuestoService = new PresupuestoService();
    private final CategoriaService   categoriaService   = new CategoriaService();
    private final GastoService       gastoService       = new GastoService();

    // Tabla de presupuestos
    @FXML private TableView<Presupuesto>             tablaPresupuestos;
    @FXML private TableColumn<Presupuesto, Integer>  colId;
    @FXML private TableColumn<Presupuesto, Integer>  colCategoria;
    @FXML private TableColumn<Presupuesto, Double>   colLimite;
    @FXML private TableColumn<Presupuesto, Integer>  colMes;
    @FXML private TableColumn<Presupuesto, Integer>  colAnio;

    // Formulario
    @FXML private ComboBox<Categoria> cmbCategoria;
    @FXML private TextField           txtLimite;
    @FXML private ComboBox<Integer>   cmbMes;
    @FXML private TextField           txtAnio;

    // Resumen mensual
    @FXML private Label lblAlerta;
    @FXML private Label lblTotalGastado;
    @FXML private ComboBox<Integer> cmbMesResumen;
    @FXML private TextField         txtAnioResumen;

    @FXML private Button btnVolver;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configurarColumnas();
        cargarCategorias();
        cargarMeses();
        cargarPresupuestos();
    }

    private void configurarColumnas() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idPresupuesto"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("idCategoria"));
        colLimite.setCellValueFactory(new PropertyValueFactory<>("limite"));
        colMes.setCellValueFactory(new PropertyValueFactory<>("mes"));
        colAnio.setCellValueFactory(new PropertyValueFactory<>("anio"));
    }

    private void cargarCategorias() {
        List<Categoria> categorias = categoriaService.listarCategorias();
        ObservableList<Categoria> lista = FXCollections.observableArrayList(categorias);
        cmbCategoria.setItems(lista);
    }

    // Rellenamos los desplegables de mes con los números del 1 al 12
    private void cargarMeses() {
        ObservableList<Integer> meses = FXCollections.observableArrayList(
                1,2,3,4,5,6,7,8,9,10,11,12);
        cmbMes.setItems(meses);
        cmbMesResumen.setItems(meses);

        // Ponemos el mes y año actual por defecto
        cmbMes.setValue(LocalDate.now().getMonthValue());
        cmbMesResumen.setValue(LocalDate.now().getMonthValue());
        txtAnio.setText(String.valueOf(LocalDate.now().getYear()));
        txtAnioResumen.setText(String.valueOf(LocalDate.now().getYear()));
    }

    private void cargarPresupuestos() {
        List<Presupuesto> presupuestos = presupuestoService.listarPresupuestos(ID_USUARIO);
        tablaPresupuestos.setItems(FXCollections.observableArrayList(presupuestos));
    }

    // ============================================================
    // AÑADIR PRESUPUESTO
    // ============================================================
    @FXML
    private void añadirPresupuesto(ActionEvent event) {

        if (txtLimite.getText().isEmpty() || cmbMes.getValue() == null
                || txtAnio.getText().isEmpty()) {
            mostrarAlerta("Error", "Por favor, rellena todos los campos obligatorios.");
            return;
        }

        try {
            Presupuesto p = new Presupuesto();
            p.setIdUsuario(ID_USUARIO);
            // Si no se selecciona categoría, es presupuesto global (0 = NULL en BD)
            p.setIdCategoria(cmbCategoria.getValue() != null ?
                    cmbCategoria.getValue().getIdCategoria() : 0);
            p.setLimite(Double.parseDouble(txtLimite.getText().replace(",", ".")));
            p.setMes(cmbMes.getValue());
            p.setAnio(Integer.parseInt(txtAnio.getText()));

            if (presupuestoService.añadirPresupuesto(p)) {
                mostrarInfo("Presupuesto añadido correctamente.");
                limpiarFormulario();
                cargarPresupuestos();
            } else {
                mostrarAlerta("Error", "No se pudo añadir el presupuesto.");
            }

        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "Comprueba que el límite y el año son números válidos.");
        }
    }

    // ============================================================
    // ELIMINAR PRESUPUESTO
    // ============================================================
    @FXML
    private void eliminarPresupuesto(ActionEvent event) {

        Presupuesto seleccionado = tablaPresupuestos.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            mostrarAlerta("Aviso", "Selecciona un presupuesto para eliminarlo.");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setContentText("¿Estás seguro de que quieres eliminar este presupuesto?");

        confirmacion.showAndWait().ifPresent(respuesta -> {
            if (respuesta == ButtonType.OK) {
                if (presupuestoService.eliminarPresupuesto(
                        seleccionado.getIdPresupuesto(), ID_USUARIO)) {
                    mostrarInfo("Presupuesto eliminado correctamente.");
                    cargarPresupuestos();
                } else {
                    mostrarAlerta("Error", "No se pudo eliminar el presupuesto.");
                }
            }
        });
    }

    // ============================================================
    // VER RESUMEN MENSUAL CON ALERTA
    // ============================================================
    @FXML
    private void verResumen(ActionEvent event) {

        if (cmbMesResumen.getValue() == null || txtAnioResumen.getText().isEmpty()) {
            mostrarAlerta("Aviso", "Selecciona un mes y un año para ver el resumen.");
            return;
        }

        try {
            int mes  = cmbMesResumen.getValue();
            int anio = Integer.parseInt(txtAnioResumen.getText());

            // Obtenemos el total gastado ese mes desde GastoService
            double totalGastado = gastoService.totalGastadoPorMes(ID_USUARIO, mes, anio);

            // Obtenemos el mensaje de alerta desde PresupuestoService
            String alerta = presupuestoService.comprobarAlerta(ID_USUARIO, mes, anio, totalGastado);

            // Mostramos los resultados en los labels de la pantalla
            lblTotalGastado.setText("Total gastado: " + String.format("%.2f", totalGastado) + " €");
            lblAlerta.setText(alerta);

        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "El año debe ser un número válido.");
        }
    }

    // ============================================================
    // VOLVER A LA PANTALLA PRINCIPAL
    // ============================================================
    @FXML
    private void volver(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/com/example/gastosapp_code/main.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnVolver.getScene().getWindow();
            stage.setTitle("GastosApp");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("❌ Error al volver al menú: " + e.getMessage());
        }
    }

    private void limpiarFormulario() {
        cmbCategoria.setValue(null);
        txtLimite.clear();
        cmbMes.setValue(LocalDate.now().getMonthValue());
        txtAnio.setText(String.valueOf(LocalDate.now().getYear()));
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarInfo(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}