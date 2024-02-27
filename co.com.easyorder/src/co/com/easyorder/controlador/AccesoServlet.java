package co.com.easyorder.controlador;

import java.io.IOException;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

/**
 * Servlet AccesoServlet que maneja la autenticación de usuarios en el sistema.
 * Este servlet se encarga de procesar las solicitudes de inicio de sesión,
 * validando las credenciales proporcionadas contra los registros de usuario. En
 * caso de éxito, redirige al usuario a la página de inicio o dashboard. En caso
 * de fallo, redirige de nuevo al formulario de inicio de sesión con un mensaje
 * de error.
 *
 * Este servlet está mapeado a la ruta "/login" y responde a solicitudes HTTP
 * POST.
 */
@WebServlet("/login")
public class AccesoServlet extends HttpServlet {

	/**
	 * Maneja la solicitud POST para el inicio de sesión del usuario.
	 * 
	 * Este método extrae las credenciales del usuario desde la solicitud, realiza
	 * la validación de estas credenciales, y según el resultado, redirige al
	 * usuario a la página correspondiente: al dashboard si las credenciales son
	 * válidas, o de vuelta al formulario de inicio de sesión con un mensaje de
	 * error si no lo son.
	 *
	 * @param request  La solicitud HTTP que contiene las credenciales del usuario.
	 * @param response La respuesta HTTP para redirigir al usuario.
	 * @throws ServletException Si ocurre un error en el servlet.
	 * @throws IOException      Si se produce un error de I/O.
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Obtener las credenciales del usuario desde el request
		String usuario = request.getParameter("usuario");
		String contrasena = request.getParameter("contrasena");

		// Validación de credenciales

		if (validarUsuario(usuario, contrasena)) {
			// Si las credenciales son válidas, manejar la sesión del usuario
			HttpSession sesion = request.getSession();
			sesion.setAttribute("usuario", usuario);
			// Redirigir al usuario a la página de inicio o dashboard
			response.sendRedirect("home.jsp");
		} else {
			// En caso de credenciales inválidas, redirigir al formulario de login con un
			// mensaje de error
			request.setAttribute("mensajeError", "Usuario o contraseña inválidos");
			request.getRequestDispatcher("login.jsp").forward(request, response);
		}
	}

	/**
	 * Verifica si las credenciales proporcionadas coinciden con las esperadas.
	 * 
	 * @param usuario    El nombre de usuario ingresado.
	 * @param contrasena La contraseña ingresada.
	 * @return true si las credenciales son correctas, false en caso contrario.
	 */
	private boolean validarUsuario(String usuario, String contrasena) {
		return "admin".equals(usuario) && "password".equals(contrasena);
	}
}
