package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import model.*;

public class GameController {

    // =========================
    // UI ELEMENTS
    // =========================

    @FXML private Label labelJugador;
    @FXML private Label labelInfo;
    @FXML private TextArea textHistorial;

    @FXML private Button btnTirarDau;
    @FXML private Button btnAtacar;

    // =========================
    // LOGICA
    // =========================

    private Partida partida;
    private PartidaController partidaController;

    // =========================
    // INIT
    // =========================

    public void inicialitzarPartida() {

        Taulell taulell = new Taulell(50);
        partida = new Partida(1, taulell);

        // Jugadors
        partida.afegirJugador(new Jugador(1, "Jugador 1", 1));
        partida.afegirJugador(new FocaIA(2, "Foca IA", 2));

        partida.iniciarPartida();

        partidaController = new PartidaController(partida);

        actualitzarUI();
    }

    // =========================
    // BOTONS
    // =========================

    @FXML
    private void onTirarDau() {

        partidaController.jugarTornDau();

        actualitzarUI();
    }

    @FXML
    private void onAtacar() {

        partidaController.atacarSeguentJugador();

        actualitzarUI();
    }

    // =========================
    // UI UPDATE
    // =========================

    private void actualitzarUI() {

        Jugador jugadorActual = partida.getJugadorActual();

        labelJugador.setText("Torn de: " + jugadorActual.getNickname());
        labelInfo.setText("Posició: " + jugadorActual.getPosicioActual());

        // Historial
        textHistorial.clear();

        for (String s : partida.getHistorialAccions()) {
            textHistorial.appendText(s + "\n");
        }

        // Guanyador
        if (partida.hiHaGuanyador()) {

            labelInfo.setText(" Guanyador: " + partida.getGuanyador().getNickname());

            btnTirarDau.setDisable(true);
            btnAtacar.setDisable(true);
        }
    }
}