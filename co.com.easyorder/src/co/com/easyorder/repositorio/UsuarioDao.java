package co.com.easyorder.repositorio;

import java.sql.*;
import java.util.*;
import co.com.easyorder.modelo.Usuario;

/**
 * Clase UsuarioDao que gestiona la conexión a la base de datos para las
 * operaciones con los usuarios. Proporciona las credenciales y la URL de
 * conexión necesarias para acceder a la base de datos.
 */
public class UsuarioDao {

	/**
	 * Atributos para la conexión a la base de datos. url: URL de conexión a la base
	 * de datos. user: Nombre de usuario para autenticarse en la base de datos.
	 * password: Contraseña para el usuario especificado.
	 */
	private String url = "jdbc:mysql://localhost:3306/usuarios?serverTimezone=UTC";
	private String user = "root";
	private String password = "Daticos.1";

	/**
	 * Método para agregar un nuevo usuario a la base de datos. Inserta un registro
	 * en la tabla de usuarios con los datos proporcionados: nombre de usuario,
	 * contraseña y rol.
	 *
	 * Utiliza una consulta SQL de inserción preparada para añadir los valores
	 * específicos del nuevo usuario. La conexión a la base de datos se maneja
	 * mediante un bloque try-with-resources para asegurar que tanto la conexión
	 * como el PreparedStatement se cierren automáticamente una vez finalizada la
	 * operación, ya sea exitosamente o tras una excepción.
	 *
	 * @param nombre     Nombre de usuario, que será usado como identificador único
	 *                   para el acceso al sistema.
	 * @param contrasena Clave secreta para la autenticación del usuario.
	 * @param rol        Define el nivel de acceso y los permisos dentro del
	 *                   sistema.
	 *
	 * @return true si el usuario se insertó correctamente en la base de datos,
	 *         false si ocurrió un error durante la inserción o la conexión a la
	 *         base de datos.
	 */
	public boolean agregarUsuario(String nombre, String contrasena, String rol) {
		// SQL query para insertar un nuevo usuario
		String sql = "INSERT INTO usuarios (nombre_usuario, contrasena, rol) VALUES (?, ?, ?)";

		// Intenta con recursos para asegurar que la conexión y el PreparedStatement se
		// cierren automáticamente
		try (Connection conn = DriverManager.getConnection(url, user, password);
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			// Establece los parámetros para el INSERT
			pstmt.setString(1, nombre);
			pstmt.setString(2, contrasena);
			pstmt.setString(3, rol);

			// Ejecuta el INSERT y verifica si se insertó el usuario
			int affectedRows = pstmt.executeUpdate();
			return affectedRows > 0; // Retorna true si se insertó al menos un usuario
		} catch (SQLException e) {
			System.out.println("Error al agregar el usuario");
			e.printStackTrace();
			return false; // Retorna false si ocurre una excepción
		}
	}

	/**
	 * Método para obtener un usuario de la base de datos por su nombre de usuario.
	 * Realiza una consulta SQL para buscar un usuario específico por su nombre de
	 * usuario, extrayendo su nombre, contraseña y rol de la base de datos.
	 *
	 * Utiliza una conexión a la base de datos para ejecutar la consulta preparada
	 * con el nombre de usuario proporcionado como parámetro. Si encuentra el
	 * usuario, crea una nueva instancia de Usuario, establece sus propiedades
	 * basándose en los resultados obtenidos de la base de datos, y retorna el
	 * objeto Usuario. En caso de no encontrar un usuario con el nombre
	 * especificado, retorna null o puede considerarse lanzar una excepción.
	 *
	 * @param nombre El nombre del usuario a buscar en la base de datos.
	 * @return Usuario El usuario encontrado con el nombre proporcionado, o null si
	 *         no se encuentra.
	 */
	public Usuario obtenerUsuarioPorNombre(String nombre) {
		String sql = "SELECT nombre_usuario, contrasena, rol FROM usuarios WHERE nombre_usuario = ?";

		try (Connection conn = DriverManager.getConnection(url, user, password);
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, nombre);

			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					Usuario usuario = new Usuario();
					usuario.setNombre(rs.getString("nombre_usuario"));
					usuario.setContrasena(rs.getString("contrasena"));
					String rolStr = rs.getString("rol");
					Usuario.Rol rol = Usuario.Rol.valueOf(rolStr.toUpperCase());
					usuario.setRol(rol);
					return usuario;
				}
			}
		} catch (SQLException e) {
			System.out.println("Error al obtener el usuario");
			e.printStackTrace();
		}
		return null; // O considera lanzar una excepción si el usuario no se encuentra
	}

	/**
	 * Método para obtener una lista de todos los usuarios registrados en la base de
	 * datos. Realiza una consulta SQL para seleccionar los nombres de usuario,
	 * contraseñas y roles de todos los usuarios, creando y retornando una lista de
	 * objetos Usuario con esta información.
	 *
	 * @return Una lista de objetos Usuario que representa a todos los usuarios
	 *         registrados. La lista estará vacía si no hay usuarios o si ocurre un
	 *         error durante la consulta.
	 */
	public List<Usuario> listarUsuarios() {
		// Lista para almacenar los objetos Usuario obtenidos de la base de datos
		List<Usuario> usuarios = new ArrayList<>();
		// Consulta SQL para seleccionar la información básica de todos los usuarios
		String sql = "SELECT nombre_usuario, contrasena, rol FROM usuarios";

		try (Connection conn = DriverManager.getConnection(url, user, password);
				PreparedStatement pstmt = conn.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery()) {

			// Itera sobre el conjunto de resultados para crear objetos Usuario
			while (rs.next()) {
				Usuario usuario = new Usuario();
				usuario.setNombre(rs.getString("nombre_usuario"));
				usuario.setContrasena(rs.getString("contrasena")); // Seguridad: considerar el manejo de contraseñas
				usuario.setRol(Usuario.Rol.valueOf(rs.getString("rol").toUpperCase())); // Convierte el string a enum

				// Agrega el usuario a la lista
				usuarios.add(usuario);
			}
		} catch (SQLException e) {
			System.out.println("Error al listar los usuarios");
			e.printStackTrace();
		}
		return usuarios; // Retorna la lista de usuarios
	}

	/**
	 * Método para actualizar la contraseña y el rol de un usuario específico en la
	 * base de datos. Utiliza una sentencia SQL de actualización para cambiar la
	 * contraseña y el rol basándose en el nombre de usuario proporcionado como
	 * identificador.
	 *
	 * @param nombreUsuario   El nombre del usuario cuyos datos se van a actualizar.
	 * @param nuevaContrasena La nueva contraseña del usuario.
	 * @param nuevoRol        El nuevo rol del usuario, reflejando la actualización
	 *                        de sus permisos.
	 * @return Un valor booleano que indica si la actualización fue exitosa. Retorna
	 *         true si se actualizó al menos un registro, false en caso contrario.
	 */
	public boolean actualizarUsuario(String nombre, String nuevaContrasena, String nuevoRol) {
		// Sentencia SQL para actualizar los datos del usuario
		String sql = "UPDATE usuarios SET contrasena = ?, rol = ? WHERE nombre_usuario = ?";
		boolean actualizado = false;

		try (Connection conn = DriverManager.getConnection(url, user, password);
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, nuevaContrasena); // Establece la nueva contraseña
			pstmt.setString(2, nuevoRol); // Establece el nuevo rol
			pstmt.setString(3, nombre); // Identifica el usuario a actualizar

			// Ejecuta la actualización y verifica el número de filas afectadas
			int affectedRows = pstmt.executeUpdate();
			actualizado = affectedRows > 0; // Confirma si se realizó la actualización
		} catch (SQLException e) {
			System.out.println("Error al actualizar el usuario");
			e.printStackTrace();
		}
		return actualizado; // Retorna el estado de la actualización
	}

	/**
	 * Método para eliminar un usuario de la base de datos basándose en su nombre de
	 * usuario. Realiza una operación de eliminación SQL donde el criterio de
	 * búsqueda es el nombre de usuario proporcionado. Esta operación elimina el
	 * registro del usuario correspondiente de la base de datos.
	 *
	 * @param nombre El nombre de usuario del registro a eliminar.
	 * @return Un valor booleano que indica si la eliminación fue exitosa. Retorna
	 *         true si se eliminó al menos un registro, false en caso contrario.
	 */
	public boolean eliminarUsuarioPorNombre(String nombre) {
		// Sentencia SQL para eliminar un usuario específico
		String sql = "DELETE FROM usuarios WHERE nombre_usuario = ?";
		boolean eliminado = false;

		try (Connection conn = DriverManager.getConnection(url, user, password);
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, nombre); // Establece el nombre de usuario a eliminar

			// Ejecuta la sentencia de eliminación y verifica las filas afectadas
			int affectedRows = pstmt.executeUpdate();
			eliminado = affectedRows > 0; // Determina si se eliminó el usuario
		} catch (SQLException e) {
			System.out.println("Error al eliminar el usuario");
			e.printStackTrace();
		}
		return eliminado; // Retorna el estado de la operación de eliminación
	}

}
