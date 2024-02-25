package co.com.easyorder.repositorio;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase ConexionBaseDatos que establece una conexión a una base de datos MySQL.
 * Esta clase intenta conectarse a la base de datos 'usuarios' usando JDBC. Si la conexión
 * es exitosa, imprime un mensaje de confirmación. En caso de fallar, captura y muestra
 * el error mediante el manejo de excepciones.
 */
public class ConexionBaseDatos {
    public static void main(String[] args) {
        // Datos de conexión a la base de datos
        String url = "jdbc:mysql://localhost:3306/usuarios?serverTimezone=UTC"; // URL de la base de datos que incluye el servidor, puerto, nombre de la base y configuración de zona horaria
        String user = "root"; // Nombre de usuario para la autenticación en la base de datos
        String password = "Daticos.1"; // Contraseña asociada al usuario para la autenticación

        try {
            // Establece la conexión con la base de datos
            Connection connection = DriverManager.getConnection(url, user, password);
            System.out.println("Conexión exitosa a la base de datos");
        } catch (SQLException e) {
            // Informa sobre errores ocurridos durante el intento de conexión
            System.out.println("Error al conectar a la base de datos");
            e.printStackTrace();
        }
    }
}