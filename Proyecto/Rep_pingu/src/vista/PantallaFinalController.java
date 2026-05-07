package vista;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import modelo.Jugador;

public class PantallaFinalController {

    // Etiqueta de texto vinculada al FXML para mostrar el nombre del ganador
    @FXML
    private Label textoGanador;

    /**
     * Este método es llamado desde la pantalla de juego justo antes de cambiar a esta vista.
     * @param j El objeto Jugador (Pinguino o Foca) que ha llegado primero a la meta.
     */
    public void setGanador(Jugador j) {
        // Actualizamos el contenido de la etiqueta con el nombre del campeón
        textoGanador.setText(j.getNombre() + "!");
    }

    /**
     * Se ejecuta al pulsar el botón de "Volver al Menú" o "Reiniciar".
     * Limpia la pantalla actual y carga de nuevo el menú principal.
     */
    @FXML
    private void volverMenu() {
        try {
            // 1. Cargamos el archivo FXML del menú principal
            Parent root = FXMLLoader.load(getClass().getResource("/PantallaMenu.fxml"));
            
            // 2. Obtenemos la ventana actual (Stage) a través de la escena de la etiqueta
            Stage stage = (Stage) textoGanador.getScene().getWindow();
            
            // 3. Cambiamos la escena por la del menú
            stage.setScene(new Scene(root));
            
        } catch (Exception e) {
            // Imprime el error en la consola si el archivo FXML no se encuentra
            e.printStackTrace();
        }
    }
}