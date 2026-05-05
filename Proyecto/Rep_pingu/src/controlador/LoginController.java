package controlador;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;

public class LoginController {
    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtPassword;
    
    private GestorBBDD gestorBBDD = new GestorBBDD();

    @FXML
    private void handleLogin() {
        String user = txtUsuario.getText();
        String pass = txtPassword.getText();
        
        // El método login retorna el ID del jugador o -1 si falla [cite: 379, 389]
        int jugadorId = gestorBBDD.login(user, pass);
        
        if (jugadorId != -1) {
            abrirJuego();
        } else {
            mostrarAlerta("Error", "Usuario o contraseña incorrectos.");
        }
    }

    @FXML
    private void handleRegister() {
        String user = txtUsuario.getText();
        String pass = txtPassword.getText();
        
        if (user.isEmpty() || pass.isEmpty()) {
            mostrarAlerta("Error", "Rellena todos los campos.");
            return;
        }

        // El método registrarJugador guarda los datos en Oracle [cite: 395, 416]
        int nuevoId = gestorBBDD.registrarJugador(user, pass);
        
        if (nuevoId != -1) {
            mostrarAlerta("Éxito", "Registrado correctamente. Ya puedes iniciar sesión.");
        } else {
            mostrarAlerta("Error", "El usuario ya existe o hubo un error en la BD.");
        }
    }

    private void abrirJuego() {
        try {
            // Carga la pantalla del juego que ya tienes creada [cite: 642]
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/PantallaMenu.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) txtUsuario.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}