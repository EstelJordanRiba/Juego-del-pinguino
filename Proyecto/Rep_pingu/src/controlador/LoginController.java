package controlador;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;

public class LoginController {
    // Referencias a los componentes visuales definidos en el archivo FXML
    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtPassword;
    
    // Instancia del gestor para comunicar esta pantalla con la base de datos Oracle
    private GestorBBDD gestorBBDD = new GestorBBDD();

    /**
     * Se ejecuta cuando el usuario hace clic en el botón de "Login".
     */
    @FXML
    private void handleLogin() {
        // Obtenemos el texto que el usuario ha escrito en las cajas
        String user = txtUsuario.getText();
        String pass = txtPassword.getText();
        
        // Llamamos al gestor. Si las credenciales coinciden (encriptadas), nos da un ID > 0
        int jugadorId = gestorBBDD.login(user, pass);
        
        if (jugadorId != -1) {
            // Si el login es correcto, pasamos a la siguiente pantalla
            abrirJuego();
        } else {
            // Si falla, avisamos al usuario con una ventana emergente
            mostrarAlerta("Error", "Usuario o contraseña incorrectos.");
        }
    }

    /**
     * Se ejecuta cuando el usuario hace clic en el botón de "Registro".
     */
    @FXML
    private void handleRegister() {
        String user = txtUsuario.getText();
        String pass = txtPassword.getText();
        
        // Validación básica: no permitir campos vacíos
        if (user.isEmpty() || pass.isEmpty()) {
            mostrarAlerta("Error", "Rellena todos los campos.");
            return;
        }

        // Intenta insertar el nuevo jugador en la tabla 'Jugadors'
        int nuevoId = gestorBBDD.registrarJugador(user, pass);
        
        if (nuevoId != -1) {
            // El registro fue exitoso (el nickname no estaba pillado)
            mostrarAlerta("Éxito", "Registrado correctamente. Ya puedes iniciar sesión.");
        } else {
            // Error común: el nombre de usuario ya existe en la base de datos
            mostrarAlerta("Error", "El usuario ya existe o hubo un error en la BD.");
        }
    }

    /**
     * Cambia la escena actual de la ventana por la del menú principal.
     */
    private void abrirJuego() {
        try {
            // Cargamos el archivo visual (FXML) del menú principal
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/PantallaMenu.fxml"));
            Parent root = loader.load();
            
            // Obtenemos la ventana (Stage) actual a partir de cualquier elemento (como el txtUsuario)
            Stage stage = (Stage) txtUsuario.getScene().getWindow();
            
            // Ponemos la nueva escena en la ventana y la mostramos
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            // Si el archivo FXML no existe o tiene errores, salta este error en consola
            e.printStackTrace();
        }
    }

    /**
     * Método auxiliar para mostrar pequeñas ventanas de información al usuario.
     */
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null); // Quita el encabezado por defecto
        alert.setContentText(mensaje);
        alert.showAndWait(); // Pausa el programa hasta que el usuario cierra la alerta
    }
}