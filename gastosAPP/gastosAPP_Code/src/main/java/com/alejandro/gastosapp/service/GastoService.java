package com.alejandro.gastosapp.service;

import com.alejandro.gastosapp.model.Gasto;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase GastoService — Gestiona todas las operaciones de la tabla GASTO.
 *
 * Aquí se hace el CRUD completo:
 * - Create  → añadirGasto()
 * - Read    → listarGastos(), filtrarPorCategoria(), filtrarPorFecha()
 * - Update  → editarGasto()
 * - Delete  → eliminarGasto()
 *
 * También incluye:
 * - totalGastadoPorMes()  → resumen mensual
 * - buscarPorDescripcion() → búsqueda por texto
 *
 * Autor: Alejandro Criado Pérez
 */
public class GastoService {

    // ============================================================
    // AÑADIR GASTO — INSERT INTO
    // Recibe un objeto Gasto y lo inserta en la base de datos.
    // Devuelve true si se insertó correctamente, false si hubo error.
    // ============================================================
    public boolean añadirGasto(Gasto gasto) {

        // La consulta SQL que vamos a ejecutar.
        // Los "?" son parámetros que rellenaremos después.
        // Así evitamos inyección SQL (un tipo de ataque muy común).
        String sql = "INSERT INTO GASTO (id_usuario, id_categoria, importe, descripcion, fecha) " +
                "VALUES (?, ?, ?, ?, ?)";

        // try-with-resources: cierra el PreparedStatement automáticamente al terminar
        try (PreparedStatement ps = ConexionDB.getConexion().prepareStatement(sql)) {

            // Rellenamos cada "?" con el valor correspondiente del objeto Gasto
            ps.setInt(1, gasto.getIdUsuario());
            ps.setInt(2, gasto.getIdCategoria());
            ps.setDouble(3, gasto.getImporte());
            ps.setString(4, gasto.getDescripcion());
            ps.setDate(5, Date.valueOf(gasto.getFecha())); // convertimos LocalDate a SQL Date

            // executeUpdate() ejecuta la consulta y devuelve cuántas filas se afectaron
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0; // si afectó al menos 1 fila, fue exitoso

        } catch (SQLException e) {
            System.err.println("❌ Error al añadir gasto: " + e.getMessage());
            return false;
        }
    }

    // ============================================================
    // LISTAR GASTOS — SELECT con JOIN
    // Devuelve todos los gastos de un usuario ordenados por fecha.
    // Usamos JOIN para obtener el nombre de la categoría.
    // ============================================================
    public List<Gasto> listarGastos(int idUsuario) {

        // Lista donde iremos guardando los gastos que encontremos
        List<Gasto> gastos = new ArrayList<>();

        String sql = "SELECT g.id_gasto, g.id_usuario, g.id_categoria, " +
                "g.importe, g.descripcion, g.fecha " +
                "FROM GASTO g " +
                "WHERE g.id_usuario = ? " +
                "ORDER BY g.fecha DESC";

        try (PreparedStatement ps = ConexionDB.getConexion().prepareStatement(sql)) {

            ps.setInt(1, idUsuario);

            // executeQuery() ejecuta el SELECT y devuelve un ResultSet
            // ResultSet es como un cursor que va fila por fila
            ResultSet rs = ps.executeQuery();

            // Mientras haya filas, las leemos y creamos objetos Gasto
            while (rs.next()) {
                Gasto g = new Gasto();
                g.setIdGasto(rs.getInt("id_gasto"));
                g.setIdUsuario(rs.getInt("id_usuario"));
                g.setIdCategoria(rs.getInt("id_categoria"));
                g.setImporte(rs.getDouble("importe"));
                g.setDescripcion(rs.getString("descripcion"));
                // convertimos SQL Date a LocalDate
                g.setFecha(rs.getDate("fecha").toLocalDate());
                gastos.add(g); // añadimos el gasto a la lista
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al listar gastos: " + e.getMessage());
        }

        return gastos;
    }

    // ============================================================
    // FILTRAR POR CATEGORÍA — SELECT con WHERE
    // Devuelve los gastos de un usuario filtrados por categoría.
    // ============================================================
    public List<Gasto> filtrarPorCategoria(int idUsuario, int idCategoria) {

        List<Gasto> gastos = new ArrayList<>();

        String sql = "SELECT id_gasto, id_usuario, id_categoria, importe, descripcion, fecha " +
                "FROM GASTO " +
                "WHERE id_usuario = ? AND id_categoria = ? " +
                "ORDER BY fecha DESC";

        try (PreparedStatement ps = ConexionDB.getConexion().prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            ps.setInt(2, idCategoria);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Gasto g = new Gasto();
                g.setIdGasto(rs.getInt("id_gasto"));
                g.setIdUsuario(rs.getInt("id_usuario"));
                g.setIdCategoria(rs.getInt("id_categoria"));
                g.setImporte(rs.getDouble("importe"));
                g.setDescripcion(rs.getString("descripcion"));
                g.setFecha(rs.getDate("fecha").toLocalDate());
                gastos.add(g);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al filtrar por categoría: " + e.getMessage());
        }

        return gastos;
    }

    // ============================================================
    // FILTRAR POR FECHA — SELECT con BETWEEN
    // Devuelve los gastos de un usuario entre dos fechas.
    // ============================================================
    public List<Gasto> filtrarPorFecha(int idUsuario, LocalDate desde, LocalDate hasta) {

        List<Gasto> gastos = new ArrayList<>();

        String sql = "SELECT id_gasto, id_usuario, id_categoria, importe, descripcion, fecha " +
                "FROM GASTO " +
                "WHERE id_usuario = ? AND fecha BETWEEN ? AND ? " +
                "ORDER BY fecha DESC";

        try (PreparedStatement ps = ConexionDB.getConexion().prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            ps.setDate(2, Date.valueOf(desde));
            ps.setDate(3, Date.valueOf(hasta));
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Gasto g = new Gasto();
                g.setIdGasto(rs.getInt("id_gasto"));
                g.setIdUsuario(rs.getInt("id_usuario"));
                g.setIdCategoria(rs.getInt("id_categoria"));
                g.setImporte(rs.getDouble("importe"));
                g.setDescripcion(rs.getString("descripcion"));
                g.setFecha(rs.getDate("fecha").toLocalDate());
                gastos.add(g);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al filtrar por fecha: " + e.getMessage());
        }

        return gastos;
    }

    // ============================================================
    // EDITAR GASTO — UPDATE
    // Actualiza los datos de un gasto existente en la base de datos.
    // ============================================================
    public boolean editarGasto(Gasto gasto) {

        String sql = "UPDATE GASTO SET id_categoria = ?, importe = ?, " +
                "descripcion = ?, fecha = ? " +
                "WHERE id_gasto = ? AND id_usuario = ?";

        try (PreparedStatement ps = ConexionDB.getConexion().prepareStatement(sql)) {

            ps.setInt(1, gasto.getIdCategoria());
            ps.setDouble(2, gasto.getImporte());
            ps.setString(3, gasto.getDescripcion());
            ps.setDate(4, Date.valueOf(gasto.getFecha()));
            ps.setInt(5, gasto.getIdGasto());
            ps.setInt(6, gasto.getIdUsuario());

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("❌ Error al editar gasto: " + e.getMessage());
            return false;
        }
    }

    // ============================================================
    // ELIMINAR GASTO — DELETE
    // Elimina un gasto de la base de datos por su ID.
    // ============================================================
    public boolean eliminarGasto(int idGasto, int idUsuario) {

        String sql = "DELETE FROM GASTO WHERE id_gasto = ? AND id_usuario = ?";

        try (PreparedStatement ps = ConexionDB.getConexion().prepareStatement(sql)) {

            ps.setInt(1, idGasto);
            ps.setInt(2, idUsuario);

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("❌ Error al eliminar gasto: " + e.getMessage());
            return false;
        }
    }

    // ============================================================
    // TOTAL GASTADO POR MES — SELECT con SUM y GROUP BY
    // Devuelve el total gastado en un mes y año concretos.
    // ============================================================
    public double totalGastadoPorMes(int idUsuario, int mes, int anio) {

        String sql = "SELECT SUM(importe) AS total FROM GASTO " +
                "WHERE id_usuario = ? AND MONTH(fecha) = ? AND YEAR(fecha) = ?";

        try (PreparedStatement ps = ConexionDB.getConexion().prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            ps.setInt(2, mes);
            ps.setInt(3, anio);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getDouble("total"); // devuelve el total o 0 si no hay gastos
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al calcular total mensual: " + e.getMessage());
        }

        return 0.0;
    }

    // ============================================================
    // BUSCAR POR DESCRIPCIÓN — SELECT con LIKE
    // Busca gastos cuya descripción contenga el texto indicado.
    // ============================================================
    public List<Gasto> buscarPorDescripcion(int idUsuario, String texto) {

        List<Gasto> gastos = new ArrayList<>();

        String sql = "SELECT id_gasto, id_usuario, id_categoria, importe, descripcion, fecha " +
                "FROM GASTO " +
                "WHERE id_usuario = ? AND descripcion LIKE ? " +
                "ORDER BY fecha DESC";

        try (PreparedStatement ps = ConexionDB.getConexion().prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            ps.setString(2, "%" + texto + "%"); // % significa "cualquier cosa antes/después"
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Gasto g = new Gasto();
                g.setIdGasto(rs.getInt("id_gasto"));
                g.setIdUsuario(rs.getInt("id_usuario"));
                g.setIdCategoria(rs.getInt("id_categoria"));
                g.setImporte(rs.getDouble("importe"));
                g.setDescripcion(rs.getString("descripcion"));
                g.setFecha(rs.getDate("fecha").toLocalDate());
                gastos.add(g);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al buscar por descripción: " + e.getMessage());
        }

        return gastos;
    }
}