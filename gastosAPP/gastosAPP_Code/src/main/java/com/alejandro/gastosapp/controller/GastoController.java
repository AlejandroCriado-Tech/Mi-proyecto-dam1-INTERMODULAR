package com.alejandro.gastosapp.controller;

import com.alejandro.gastosapp.model.Categoria;
import com.alejandro.gastosapp.model.Gasto;
import com.alejandro.gastosapp.service.CategoriaService;
import com.alejandro.gastosapp.service.GastoService;
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
 * Clase GastoController — Controla la pantalla de gestión de gastos.
 *
 * Funcionalidades:
 * - Mostrar lista de gastos en una tabla
 * - Añadir nuevo gasto
 * - Editar gasto seleccionado
 * - Eliminar gasto seleccionado
 * - Filtrar por categoría o por fecha
 * - Buscar por descripción
 *
 * Implementa Initializable para ejecutar código al cargar la pantalla.
 *
 * Autor: Alejandro Criado Pérez
 */
public class GastoController implements Initializable {

    // ============================================================
    // ID DEL USUARIO ACTIVO — en un proyecto completo vendría
    // de una pantalla de login. Por ahora usamos el usuario 1.
    // ============================================================
    private static final int ID_USUARIO = 1;

    // ============================================================
    // SERVICIOS — los usamos para hablar con la base de datos
    // ============================================================
    private final GastoService     gastoService     = new GastoService();
    private final CategoriaService categoriaService = new CategoriaService();

    // ============================================================
    // COMPONENTES DE LA PANTALLA — conectados con gastos.fxml
    // ============================================================

    // Tabla principal de gastos
    @FXML private TableView<Gasto>          tablaGastos;
    @FXML private TableColumn<Gasto, Integer>   colId;
    @FXML private TableColumn<Gasto, Integer>   colCategoria;
    @FXML private TableColumn<Gasto, Double>    colImporte;
    @FXML private TableColumn<Gasto, String>    colDescripcion;
    @FXML private TableColumn<Gasto, LocalDate> colFecha;

    // Formulario para añadir/editar gastos
    @FXML private ComboBox<Categoria> cmbCategoria;
    @FXML private TextField           txtImporte;
    @FXML private TextField           txtDescripcion;
    @FXML private DatePicker          dpFecha;

    // Filtros
    @FXML private ComboBox<Categoria> cmbFiltroCategoria;
    @FXML private DatePicker          dpFiltroDesde;
    @FXML private DatePicker          dpFiltroHasta;
    @FXML private TextField           txtBuscar;

    // Botones
    @FXML private Button btnVolver;

    // ============================================================
    // INITIALIZE — se ejecuta automáticamente al cargar la pantalla
    // Es el equivalente al constructor para los controladores FXML
    // ============================================================
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configurarColumnas();   // configuramos qué muestra cada columna
        cargarCategorias();     // cargamos las categorías en los desplegables
        cargarGastos();         // cargamos los gastos en la tabla
    }

    // ============================================================
    // CONFIGURAR COLUMNAS DE LA TABLA
    // PropertyValueFactory conecta cada columna con el getter del modelo.
    // "importe" busca el método getImporte() en la clase Gasto.
    // ============================================================
    private void configurarColumnas() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idGasto"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("idCategoria"));
        colImporte.setCellValueFactory(new PropertyValueFactory<>("importe"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
    }

    // ============================================================
    // CARGAR CATEGORÍAS en los desplegables
    // ============================================================
    private void cargarCategorias() {
        List<Categoria> categorias = categoriaService.listarCategorias();
        ObservableList<Categoria> lista = FXCollections.observableArrayList(categorias);
        cmbCategoria.setItems(lista);
        cmbFiltroCategoria.setItems(lista);
    }

    // ============================================================
    // CARGAR GASTOS en la tabla
    // ============================================================
    private void cargarGastos() {
        List<Gasto> gastos = gastoService.listarGastos(ID_USUARIO);
        ObservableList<Gasto> lista = FXCollections.observableArrayList(gastos);
        tablaGastos.setItems(lista);
    }

    // ============================================================
    // AÑADIR GASTO — recoge los datos del formulario y los guarda
    // ============================================================
    @FXML
    private void añadirGasto(ActionEvent event) {

        // Validamos que todos los campos estén rellenos
        if (cmbCategoria.getValue() == null || txtImporte.getText().isEmpty()
                || dpFecha.getValue() == null) {
            mostrarAlerta("Error", "Por favor, rellena todos los campos obligatorios.");
            return;
        }

        try {
            // Creamos el objeto Gasto con los datos del formulario
            Gasto gasto = new Gasto();
            gasto.setIdUsuario(ID_USUARIO);
            gasto.setIdCategoria(cmbCategoria.getValue().getIdCategoria());
            gasto.setImporte(Double.parseDouble(txtImporte.getText().replace(",", ".")));
            gasto.setDescripcion(txtDescripcion.getText());
            gasto.setFecha(dpFecha.getValue());

            // Llamamos al service para guardarlo en la BD
            if (gastoService.añadirGasto(gasto)) {
                mostrarInfo("Gasto añadido correctamente.");
                limpiarFormulario();
                cargarGastos(); // recargamos la tabla
            } else {
                mostrarAlerta("Error", "No se pudo añadir el gasto.");
            }

        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "El importe debe ser un número válido. Ejemplo: 25.50");
        }
    }

    // ============================================================
    // EDITAR GASTO — carga el gasto seleccionado en el formulario
    // ============================================================
    @FXML
    private void editarGasto(ActionEvent event) {

        Gasto seleccionado = tablaGastos.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            mostrarAlerta("Aviso", "Selecciona un gasto de la tabla para editarlo.");
            return;
        }

        try {
            seleccionado.setIdCategoria(cmbCategoria.getValue().getIdCategoria());
            seleccionado.setImporte(Double.parseDouble(txtImporte.getText().replace(",", ".")));
            seleccionado.setDescripcion(txtDescripcion.getText());
            seleccionado.setFecha(dpFecha.getValue());

            if (gastoService.editarGasto(seleccionado)) {
                mostrarInfo("Gasto editado correctamente.");
                limpiarFormulario();
                cargarGastos();
            } else {
                mostrarAlerta("Error", "No se pudo editar el gasto.");
            }

        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "El importe debe ser un número válido.");
        }
    }

    // ============================================================
    // ELIMINAR GASTO — elimina el gasto seleccionado en la tabla
    // ============================================================
    @FXML
    private void eliminarGasto(ActionEvent event) {

        Gasto seleccionado = tablaGastos.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            mostrarAlerta("Aviso", "Selecciona un gasto de la tabla para eliminarlo.");
            return;
        }

        // Pedimos confirmación antes de borrar
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setContentText("¿Estás seguro de que quieres eliminar este gasto?");

        confirmacion.showAndWait().ifPresent(respuesta -> {
            if (respuesta == ButtonType.OK) {
                if (gastoService.eliminarGasto(seleccionado.getIdGasto(), ID_USUARIO)) {
                    mostrarInfo("Gasto eliminado correctamente.");
                    cargarGastos();
                } else {
                    mostrarAlerta("Error", "No se pudo eliminar el gasto.");
                }
            }
        });
    }

    // ============================================================
    // FILTRAR POR CATEGORÍA
    // ============================================================
    @FXML
    private void filtrarPorCategoria(ActionEvent event) {
        if (cmbFiltroCategoria.getValue() == null) {
            cargarGastos();
            return;
        }
        List<Gasto> gastos = gastoService.filtrarPorCategoria(
                ID_USUARIO, cmbFiltroCategoria.getValue().getIdCategoria());
        tablaGastos.setItems(FXCollections.observableArrayList(gastos));
    }

    // ============================================================
    // FILTRAR POR FECHA
    // ============================================================
    @FXML
    private void filtrarPorFecha(ActionEvent event) {
        if (dpFiltroDesde.getValue() == null || dpFiltroHasta.getValue() == null) {
            mostrarAlerta("Aviso", "Selecciona una fecha de inicio y una fecha de fin.");
            return;
        }
        List<Gasto> gastos = gastoService.filtrarPorFecha(
                ID_USUARIO, dpFiltroDesde.getValue(), dpFiltroHasta.getValue());
        tablaGastos.setItems(FXCollections.observableArrayList(gastos));
    }

    // ============================================================
    // BUSCAR POR DESCRIPCIÓN
    // ============================================================
    @FXML
    private void buscar(ActionEvent event) {
        if (txtBuscar.getText().isEmpty()) {
            cargarGastos();
            return;
        }
        List<Gasto> gastos = gastoService.buscarPorDescripcion(ID_USUARIO, txtBuscar.getText());
        tablaGastos.setItems(FXCollections.observableArrayList(gastos));
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

    // ============================================================
    // MÉTODOS AUXILIARES
    // ============================================================

    // Limpia el formulario después de añadir o editar
    private void limpiarFormulario() {
        cmbCategoria.setValue(null);
        txtImporte.clear();
        txtDescripcion.clear();
        dpFecha.setValue(null);
    }

    // Muestra un mensaje de error
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    // Muestra un mensaje informativo
    private void mostrarInfo(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}