package vista;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.Jugador;
import model.Partida;
import model.Taulell;

public class PantallaMenu {

    @FXML
    private TextField userField;

    @FXML
    private PasswordField passField;

    @FXML
    private void handleLogin(ActionEvent event) {
        iniciarDemo();
    }

    @FXML
    private void handleRegister(ActionEvent event) {
        iniciarDemo();
    }

    private void iniciarDemo() {
        String user = userField.getText() == null || userField.getText().isBlank()
                ? "Jugador1"
                : userField.getText().trim();

        Taulell taulell = new Taulell(50);
        Partida partida = new Partida(1, taulell);

        partida.afegirJugador(new Jugador(1, user, 1));
        partida.afegirJugador(new Jugador(2, "Jugador2", 2));
        partida.iniciarPartida();

        SceneManager.mostrarJoc(partida);
    }
}