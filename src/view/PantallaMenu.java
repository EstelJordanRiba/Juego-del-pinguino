package view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.*;

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

        // 🔥 Nom jugador amb validació
        String user = (userField.getText() == null || userField.getText().isBlank())
                ? "Jugador1"
                : userField.getText().trim();

        // 🔥 Crear partida
        Taulell taulell = new Taulell(50);
        Partida partida = new Partida(1, taulell);

        // 🔥 Jugadors (1 usuari + 1 IA)
        partida.afegirJugador(new Jugador(1, user, 1));
        partida.afegirJugador(new FocaIA(2, "Foca IA", 2));

        partida.iniciarPartida();

        // 🔥 Canvi d’escena
        SceneManager.mostrarJoc(partida);
    }
}