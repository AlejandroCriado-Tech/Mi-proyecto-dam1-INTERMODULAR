package com.alejandro.gastosapp.service;

import com.alejandro.gastosapp.model.Presupuesto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase PresupuestoService — Gestiona todas las operaciones de la tabla PRESUPUESTO.
 *
 * Operaciones disponibles:
 * - listarPresupuestos()     → obtiene todos los presupuestos de un usuario
 * - añadirPresupuesto()      → crea un nuevo presupuesto
 * - editarPresupuesto()      → modifica un presupuesto existente
 * - eliminarPresupuesto()    → elimina un presupuesto
 * - obtenerLimiteMensual()   → obtiene el límite global de un mes
 * - comprobarAlerta()        → comprueba si se ha superado el presupuesto
 *
 * Autor: Alejandro Criado Pérez
 */
public class PresupuestoService {

    // ============================================================
    // LISTAR PRESUPUESTOS — SELECT
    // Devuelve todos los presupuestos de un usuario.
    // ============================================================
    public List<Presupuesto> listarPresupuestos(int idUsuario) {

        List<Presupuesto> presupuestos = new ArrayList<>();

        String sql = "SELECT id_presupuesto, id_usuario, id_categoria, limite, mes, anio " +
                "FROM PRESUPUESTO " +
                "WHERE id_usuario = ? " +
                "ORDER BY anio DESC, mes DESC";

        try (PreparedStatement ps = ConexionDB.getConexion().prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Presupuesto p = new Presupuesto();
                p.setIdPresupuesto(rs.getInt("id_presupuesto"));
                p.setIdUsuario(rs.getInt("id_usuario"));
                p.setIdCategoria(rs.getInt("id_categoria"));
                p.setLimite(rs.getDouble("limite"));
                p.setMes(rs.getInt("mes"));
                p.setAnio(rs.getInt("anio"));
                presupuestos.add(p);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al listar presupuestos: " + e.getMessage());
        }

        return presupuestos;
    }

    // ============================================================
    // AÑADIR PRESUPUESTO — INSERT
    // ============================================================
    public boolean añadirPresupuesto(Presupuesto presupuesto) {

        String sql = "INSERT INTO PRESUPUESTO (id_usuario, id_categoria, limite, mes, anio) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = ConexionDB.getConexion().prepareStatement(sql)) {

            ps.setInt(1, presupuesto.getIdUsuario());
            // Si id_categoria es 0, lo guardamos como NULL (presupuesto global)
            if (presupuesto.getIdCategoria() == 0) {
                ps.setNull(2, Types.INTEGER);
            } else {
                ps.setInt(2, presupuesto.getIdCategoria());
            }
            ps.setDouble(3, presupuesto.getLimite());
            ps.setInt(4, presupuesto.getMes());
            ps.setInt(5, presupuesto.getAnio());

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("❌ Error al añadir presupuesto: " + e.getMessage());
            return false;
        }
    }

    // ============================================================
    // EDITAR PRESUPUESTO — UPDATE
    // ============================================================
    public boolean editarPresupuesto(Presupuesto presupuesto) {

        String sql = "UPDATE PRESUPUESTO SET limite = ?, mes = ?, anio = ? " +
                "WHERE id_presupuesto = ? AND id_usuario = ?";

        try (PreparedStatement ps = ConexionDB.getConexion().prepareStatement(sql)) {

            ps.setDouble(1, presupuesto.getLimite());
            ps.setInt(2, presupuesto.getMes());
            ps.setInt(3, presupuesto.getAnio());
            ps.setInt(4, presupuesto.getIdPresupuesto());
            ps.setInt(5, presupuesto.getIdUsuario());

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("❌ Error al editar presupuesto: " + e.getMessage());
            return false;
        }
    }

    // ============================================================
    // ELIMINAR PRESUPUESTO — DELETE
    // ============================================================
    public boolean eliminarPresupuesto(int idPresupuesto, int idUsuario) {

        String sql = "DELETE FROM PRESUPUESTO WHERE id_presupuesto = ? AND id_usuario = ?";

        try (PreparedStatement ps = ConexionDB.getConexion().prepareStatement(sql)) {

            ps.setInt(1, idPresupuesto);
            ps.setInt(2, idUsuario);

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("❌ Error al eliminar presupuesto: " + e.getMessage());
            return false;
        }
    }

    // ============================================================
    // OBTENER LÍMITE MENSUAL GLOBAL — SELECT
    // Devuelve el límite global del mes para un usuario.
    // (el presupuesto donde id_categoria es NULL)
    // ============================================================
    public double obtenerLimiteMensual(int idUsuario, int mes, int anio) {

        String sql = "SELECT limite FROM PRESUPUESTO " +
                "WHERE id_usuario = ? AND mes = ? AND anio = ? " +
                "AND id_categoria IS NULL";

        try (PreparedStatement ps = ConexionDB.getConexion().prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            ps.setInt(2, mes);
            ps.setInt(3, anio);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getDouble("limite");
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al obtener límite mensual: " + e.getMessage());
        }

        return 0.0; // 0 significa que no hay presupuesto definido
    }

    // ============================================================
    // COMPROBAR ALERTA — lógica de negocio
    // Compara el total gastado en el mes con el límite establecido.
    // Devuelve un mensaje de estado:
    //   🟢 OK           → por debajo del 80% del límite
    //   🟡 CERCA        → entre el 80% y el 100%
    //   🔴 SUPERADO     → por encima del límite
    // ============================================================
    public String comprobarAlerta(int idUsuario, int mes, int anio, double totalGastado) {

        double limite = obtenerLimiteMensual(idUsuario, mes, anio);

        // Si no hay presupuesto definido, no hay alerta
        if (limite == 0.0) return "ℹ️ Sin presupuesto definido";

        double porcentaje = (totalGastado / limite) * 100;

        if (porcentaje > 100) {
            return "🔴 SUPERADO — Llevas " + String.format("%.2f", totalGastado) +
                    "€ de " + limite + "€";
        } else if (porcentaje >= 80) {
            return "🟡 CERCA DEL LÍMITE — Llevas " + String.format("%.2f", totalGastado) +
                    "€ de " + limite + "€";
        } else {
            return "🟢 OK — Llevas " + String.format("%.2f", totalGastado) +
                    "€ de " + limite + "€";
        }
    }
}