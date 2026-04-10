package com.alejandro.gastosapp.service;

import com.alejandro.gastosapp.model.Categoria;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase CategoriaService — Gestiona todas las operaciones de la tabla CATEGORIA.
 *
 * Operaciones disponibles:
 * - listarCategorias() → obtiene todas las categorías
 * - obtenerPorId()     → obtiene una categoría por su ID
 * - añadirCategoria()  → inserta una nueva categoría
 * - eliminarCategoria() → elimina una categoría
 *
 * Autor: Alejandro Criado Pérez
 */
public class CategoriaService {

    // ============================================================
    // LISTAR TODAS LAS CATEGORÍAS — SELECT
    // Devuelve todas las categorías disponibles en la base de datos.
    // Se usa para rellenar los desplegables de la interfaz.
    // ============================================================
    public List<Categoria> listarCategorias() {

        List<Categoria> categorias = new ArrayList<>();

        String sql = "SELECT id_categoria, nombre, descripcion FROM CATEGORIA ORDER BY nombre";

        try (PreparedStatement ps = ConexionDB.getConexion().prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Categoria c = new Categoria();
                c.setIdCategoria(rs.getInt("id_categoria"));
                c.setNombre(rs.getString("nombre"));
                c.setDescripcion(rs.getString("descripcion"));
                categorias.add(c);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al listar categorías: " + e.getMessage());
        }

        return categorias;
    }

    // ============================================================
    // OBTENER CATEGORÍA POR ID — SELECT con WHERE
    // Devuelve una categoría concreta buscando por su ID.
    // Útil para mostrar el nombre de la categoría en la tabla de gastos.
    // ============================================================
    public Categoria obtenerPorId(int idCategoria) {

        String sql = "SELECT id_categoria, nombre, descripcion FROM CATEGORIA WHERE id_categoria = ?";

        try (PreparedStatement ps = ConexionDB.getConexion().prepareStatement(sql)) {

            ps.setInt(1, idCategoria);
            ResultSet rs = ps.executeQuery();

            // Si encontramos la categoría la devolvemos
            if (rs.next()) {
                Categoria c = new Categoria();
                c.setIdCategoria(rs.getInt("id_categoria"));
                c.setNombre(rs.getString("nombre"));
                c.setDescripcion(rs.getString("descripcion"));
                return c;
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al obtener categoría: " + e.getMessage());
        }

        // Si no se encontró, devolvemos null
        return null;
    }

    // ============================================================
    // AÑADIR CATEGORÍA — INSERT
    // Inserta una nueva categoría en la base de datos.
    // ============================================================
    public boolean añadirCategoria(Categoria categoria) {

        String sql = "INSERT INTO CATEGORIA (nombre, descripcion) VALUES (?, ?)";

        try (PreparedStatement ps = ConexionDB.getConexion().prepareStatement(sql)) {

            ps.setString(1, categoria.getNombre());
            ps.setString(2, categoria.getDescripcion());

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("❌ Error al añadir categoría: " + e.getMessage());
            return false;
        }
    }

    // ============================================================
    // ELIMINAR CATEGORÍA — DELETE
    // Elimina una categoría por su ID.
    // Ojo: si hay gastos asociados a esta categoría,
    // la restricción ON DELETE RESTRICT de la BD lo impedirá.
    // ============================================================
    public boolean eliminarCategoria(int idCategoria) {

        String sql = "DELETE FROM CATEGORIA WHERE id_categoria = ?";

        try (PreparedStatement ps = ConexionDB.getConexion().prepareStatement(sql)) {

            ps.setInt(1, idCategoria);
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("❌ Error al eliminar categoría: " + e.getMessage());
            return false;
        }
    }
}
