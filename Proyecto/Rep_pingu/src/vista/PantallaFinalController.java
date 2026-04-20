package vista;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import modelo.Jugador;

public class PantallaFinalController {

    @FXML
    private Label textoGanador;

    public void setGanador(Jugador j) {
        textoGanador.setText(  j.getNombre() + "!");
    }

    @FXML
    private void volverMenu() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/PantallaMenu.fxml"));
            Stage stage = (Stage) textoGanador.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}