package servicie;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {
    // ============================================================
    // DATOS DE CONEXIÓN
    // Cambia estos valores si tu MySQL tiene otra configuración
    // ============================================================
    private static final String URL = "jdbc:mysql://localhost:3307/gastosapp";
    private static final String USUARIO  = "root";
    private static final String PASSWORD = "";
    private static Connection instancia = null;

    private ConexionDB() {}

    public static Connection getConexion() {

        try {

            if (instancia == null || instancia.isClosed()) {

                instancia = DriverManager.getConnection(URL, USUARIO, PASSWORD);
                System.out.println("✅ Conexión con la base de datos establecida.");
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al conectar con la base de datos: " + e.getMessage());
        }

        return instancia;
    }
}
