package view;

import javafx.application.Platform;
import javafx.scene.input.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import model.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PantallaJuego {

    @FXML private GridPane tablero;

    @FXML private Text dadoResultText;
    @FXML private Text rapido_t;
    @FXML private Text lento_t;
    @FXML private Text peces_t;
    @FXML private Text nieve_t;
    @FXML private Text eventos;

    @FXML private Button dado;
    @FXML private Button rapido;
    @FXML private Button lento;
    @FXML private Button peces;
    @FXML private Button nieve;

    @FXML private MenuItem newGame;
    @FXML private MenuItem saveGame;
    @FXML private MenuItem loadGame;
    @FXML private MenuItem quitGame;

    @FXML private Circle P1;
    @FXML private Circle P2;
    @FXML private Circle P3;
    @FXML private Circle P4;

    private Partida partida;
    private final Map<Integer, Circle> fitxes = new HashMap<>();
    private final Map<Integer, StackPane> casellesUI = new HashMap<>();

    public void setPartida(Partida partida) {
        this.partida = partida;
        inicialitzarVista();
    }
    @FXML
    private void handleNewGame(ActionEvent event) {
        System.out.println("Nueva partida");
        eventos.setText("Nova partida iniciada");
    }

    @FXML
    private void handleSaveGame(ActionEvent event) {
        System.out.println("Guardar partida");
        eventos.setText("Partida guardada");
    }

    @FXML
    private void handleLoadGame(ActionEvent event) {
        System.out.println("Cargar partida");
        eventos.setText("Partida carregada");
    }

    @FXML
    private void handlePeces(ActionEvent event) {
        System.out.println("Usar peces");
        eventos.setText("Funció de peixos no implementada encara");
    }

    @FXML
    public void initialize() {
        fitxes.put(1, P1);
        fitxes.put(2, P2);
        fitxes.put(3, P3);
        fitxes.put(4, P4);
    }

    private void inicialitzarVista() {
        construirTaulell();
        actualitzarTot();
    }

    private void construirTaulell() {
        tablero.getChildren().clear();
        casellesUI.clear();

        Taulell taulell = partida.getTaulell();
        int total = taulell.getNumCaselles();
        int columnes = 5;

        for (int pos = 0; pos <= total; pos++) {
            int fila = pos / columnes;
            int colBase = pos % columnes;

            int col = (fila % 2 == 0) ? colBase : columnes - 1 - colBase;

            StackPane cell = crearCasellaUI(pos, taulell.obtenirCasella(pos));
            tablero.add(cell, col, fila);
            casellesUI.put(pos, cell);
        }

        for (Circle fitxa : fitxes.values()) {
            if (fitxa != null) {
                tablero.getChildren().add(fitxa);
            }
        }
    }

    private StackPane crearCasellaUI(int posicio, Casella casella) {
        StackPane cell = new StackPane();
        cell.setMinSize(110, 48);

        String color = obtenirColorCasella(casella, posicio);
        cell.setStyle("""
                -fx-background-color: %s;
                -fx-border-color: #d9dde7;
                -fx-border-width: 1;
                -fx-background-radius: 8;
                -fx-border-radius: 8;
                """.formatted(color));

        VBox contingut = new VBox(2);
        contingut.setAlignment(Pos.CENTER);

        Text numero = new Text(String.valueOf(posicio));
        Text tipus = new Text(obtenirTextCasella(casella, posicio));

        contingut.getChildren().addAll(numero, tipus);
        cell.getChildren().add(contingut);

        return cell;
    }

    private String obtenirTextCasella(Casella casella, int posicio) {
        if (posicio == 0) return "🐧 Inici";
        if (posicio == partida.getTaulell().getNumCaselles()) return "🏁 Meta";

        String nom = casella.getClass().getSimpleName().toLowerCase();

        if (nom.contains("os")) return "🐻 Ós";
        if (nom.contains("forat")) return "🕳 Forat";
        if (nom.contains("trineu")) return "🛷 Trineu";
        if (nom.contains("interrog")) return "❓ Event";
        if (nom.contains("terra")) return "❄ Trencadís";

        return "Normal";
    }

    private String obtenirColorCasella(Casella casella, int posicio) {
        if (posicio == 0) return "#dff6ff";
        if (posicio == partida.getTaulell().getNumCaselles()) return "#dcfce7";

        String nom = casella.getClass().getSimpleName().toLowerCase();

        if (nom.contains("os")) return "#fee2e2";
        if (nom.contains("forat")) return "#dbeafe";
        if (nom.contains("trineu")) return "#fef3c7";
        if (nom.contains("interrog")) return "#f3e8ff";
        if (nom.contains("terra")) return "#cffafe";

        return "#f8fafc";
    }

    // =========================
    // BOTONS
    // =========================

    @FXML
    private void handleDado(ActionEvent event) {
        if (partida == null || partida.hiHaGuanyador()) return;

        Dau dauNormal = Dau.crearDau(Dau.TipusDau.NORMAL);
        int tirada = dauNormal.tirar();

        partida.jugarTornTirarDau(dauNormal);

        dadoResultText.setText("Ha salido: " + tirada);

        actualitzarTot();
    }

    @FXML
    private void handleRapido(ActionEvent event) {
        if (partida == null || partida.hiHaGuanyador()) return;

        Jugador jugador = partida.obtenirJugadorActual();

        if (!jugador.getInventari().gastarDauRapid()) {
            eventos.setText("No té daus ràpids.");
            return;
        }

        Dau dau = Dau.crearDau(Dau.TipusDau.RAPID);
        int tirada = dau.tirar();

        partida.jugarTornTirarDau(dau);

        dadoResultText.setText("Ha salido: " + tirada);

        actualitzarTot();
    }

    @FXML
    private void handleLento(ActionEvent event) {
        if (partida == null || partida.hiHaGuanyador()) return;

        Jugador jugador = partida.obtenirJugadorActual();

        if (!jugador.getInventari().gastarDauLent()) {
            eventos.setText("No té daus lents.");
            return;
        }

        Dau dau = Dau.crearDau(Dau.TipusDau.LENT);
        int tirada = dau.tirar();

        partida.jugarTornTirarDau(dau);

        dadoResultText.setText("Ha salido: " + tirada);

        actualitzarTot();
    }

    @FXML
    private void handleNieve(ActionEvent event) {
        if (partida == null || partida.hiHaGuanyador()) return;

        Jugador atacant = partida.obtenirJugadorActual();
        Jugador objectiu = buscarPrimerRivalDisponible(atacant);

        if (objectiu == null) {
            eventos.setText("No hi ha rival.");
            return;
        }

        partida.jugarTornBolaNeu(objectiu);

        actualitzarTot();
    }

    // =========================
    // LOGICA
    // =========================

    private Jugador buscarPrimerRivalDisponible(Jugador atacant) {
        for (Jugador j : partida.getJugadors()) {
            if (!j.equals(atacant)) return j;
        }
        return null;
    }

    private void actualitzarTot() {
        if (partida == null) return;

        actualitzarInventari();
        actualitzarFitxes();
        destacarJugadorActual();
        mostrarUltimEvent();
        comprovarGuanyador();
    }

    private void actualitzarInventari() {
        Jugador actual = partida.obtenirJugadorActual();

        rapido_t.setText("Ràpid: " + actual.getInventari().getDausRapids());
        lento_t.setText("Lent: " + actual.getInventari().getDausLents());
        peces_t.setText("Peixos: " + actual.getInventari().getPeixos());
        nieve_t.setText("Neu: " + actual.getInventari().getBolesNeu());
    }

    private void actualitzarFitxes() {
        for (Circle fitxa : fitxes.values()) {
            GridPane.setColumnIndex(fitxa, null);
            GridPane.setRowIndex(fitxa, null);
        }

        Map<Integer, Integer> ocupacio = new HashMap<>();

        for (Jugador jugador : partida.getJugadors()) {

            Circle fitxa = fitxes.get(jugador.getIdJugador());
            int pos = jugador.getPosicioActual();

            int fila = pos / 5;
            int colBase = pos % 5;
            int col = (fila % 2 == 0) ? colBase : 4 - colBase;

            int offset = ocupacio.getOrDefault(pos, 0);
            ocupacio.put(pos, offset + 1);

            GridPane.setColumnIndex(fitxa, col);
            GridPane.setRowIndex(fitxa, fila);

            fitxa.setTranslateX(8 + offset * 28);
        }
    }

    private void destacarJugadorActual() {

        for (StackPane cell : casellesUI.values()) {
            cell.setStyle(cell.getStyle().replace("-fx-border-color: red;", ""));
        }

        int pos = partida.obtenirJugadorActual().getPosicioActual();
        StackPane cell = casellesUI.get(pos);

        if (cell != null) {
            cell.setStyle(cell.getStyle() + "-fx-border-color: red; -fx-border-width: 2;");
        }
    }

    private void mostrarUltimEvent() {
        List<String> historial = partida.getHistorialAccions();
        if (!historial.isEmpty()) {
            eventos.setText(historial.get(historial.size() - 1));
        }
    }

    private void comprovarGuanyador() {
        if (partida.hiHaGuanyador()) {
            eventos.setText("🏆 " + partida.getGuanyador().getNickname());
            desactivarAccions();
        }
    }

    private void desactivarAccions() {
        dado.setDisable(true);
        rapido.setDisable(true);
        lento.setDisable(true);
        peces.setDisable(true);
        nieve.setDisable(true);
    }


    @FXML
    private void handleQuitGame(ActionEvent event) {
        Platform.exit();
    }
}

