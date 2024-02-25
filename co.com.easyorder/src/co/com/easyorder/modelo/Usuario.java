package co.com.easyorder.modelo;

/**
 * Clase Usuario que representa un usuario en el sistema. Almacena información
 * básica del usuario, incluyendo su nombre, contraseña y rol. Proporciona
 * métodos para acceder y modificar estos datos.
 */
public class Usuario {

	/**
	 * Enumeración para los roles de usuario disponibles en el sistema. Define dos
	 * roles posibles: OPERARIO y ADMINISTRADOR, los cuales se utilizan para
	 * controlar el acceso a diferentes partes del sistema.
	 */
	public enum Rol {
		OPERARIO, ADMINISTRADOR
	}

	/**
	 * Atributos privados para almacenar la información esencial del usuario.
	 * nombreUsuario: Identificador único para el acceso al sistema. contrasena:
	 * Clave secreta para la autenticación del usuario. rol: Define los permisos y
	 * el nivel de acceso dentro del sistema.
	 */
	String nombre;
	private String contrasena;
	Rol rol;

	/**
	 * Constructor de la clase Usuario. Inicializa un nuevo usuario con los datos
	 * proporcionados.
	 *
	 * @param nombreUsuario El nombre de usuario, utilizado para la identificación y
	 *                      el acceso.
	 * @param contrasena    La contraseña del usuario, usada para la autenticación.
	 * @param rol           El rol del usuario, que define sus permisos y acceso
	 *                      dentro del sistema.
	 */
	public Usuario(String nombre, String contrasena, Rol rol) {
		this.nombre = nombre;
		this.contrasena = contrasena;
		this.rol = rol;
	}

	public Usuario() {
	}

	/**
	 * Métodos getter y setter para acceder y modificar los atributos de la clase.
	 * Permiten la manipulación segura de las propiedades del usuario, como su
	 * nombre, contraseña y rol, siguiendo el principio de encapsulamiento.
	 */

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getContrasena() {
		return contrasena;
	}

	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}

	public Rol getRol() {
		return rol;
	}

	public void setRol(Rol rol) {
		this.rol = rol;
	}

	/**
	 * Imprime la información básica del usuario. Muestra el nombre de usuario y el
	 * rol para propósitos de depuración o información. Omite la contraseña por
	 * razones de seguridad, siguiendo las mejores prácticas de privacidad.
	 */
	public void mostrarInformacion() {
		System.out.println("Nombre de Usuario: " + nombre);
		System.out.println("Rol: " + rol);
		// La contraseña no se muestra por seguridad
	}

	/**
	 * Valida si la contraseña cumple con los requisitos de seguridad básicos.
	 *
	 * Primero verifica que la contraseña no sea nula y tenga una longitud mínima de
	 * 8 caracteres, lo cual es un requisito mínimo común para asegurar contraseñas
	 * robustas.
	 *
	 * Luego, busca al menos una letra mayúscula en la contraseña, lo cual es otro
	 * criterio común para aumentar la complejidad de las contraseñas y mejorar la
	 * seguridad.
	 *
	 * @param contrasena La contraseña a validar.
	 * @return true si la contraseña cumple con todos los criterios de seguridad,
	 *         false en caso contrario.
	 */
	public boolean validarContrasena(String contrasena) {
		// Validación previa de la contraseña nula y longitud mínima
		if (contrasena == null || contrasena.length() < 8) {
			return false;
		}

		// Inicialmente asumimos que no tiene una mayúscula
		boolean tieneMayuscula = false;

		// Recorrer cada caracter de la contraseña para verificar la presencia de una
		// mayúscula
		for (int i = 0; i < contrasena.length(); i++) {
			if (Character.isUpperCase(contrasena.charAt(i))) {
				tieneMayuscula = true;
				break; // Si se encuentra una mayúscula, se detiene el bucle
			}
		}

		// Retorna verdadero solo si la contraseña tiene al menos una letra mayúscula
		return tieneMayuscula;
	}

}
