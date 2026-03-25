package vista;

import javafx.application.Platform;
import javafx.scene.input.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import model.Dau;
import model.Jugador;
import model.Partida;
import model.Taulell;
import model.Casella;

import java.util.ArrayList;
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
        int total = taulell.getNumCaselles(); // 50
        int files = 10;
        int columnes = 5;

        for (int pos = 0; pos <= total; pos++) {
            int fila = pos / columnes;
            int colBase = pos % columnes;

            int col;
            if (fila % 2 == 0) {
                col = colBase;
            } else {
                col = columnes - 1 - colBase;
            }

            StackPane cell = crearCasellaUI(pos, taulell.obtenirCasella(pos));
            tablero.add(cell, col, fila);
            casellesUI.put(pos, cell);
        }

        // Torna a afegir les fitxes per sobre
        for (Circle fitxa : fitxes.values()) {
            if (fitxa != null) {
                tablero.getChildren().add(fitxa);
            }
        }
    }

    private StackPane crearCasellaUI(int posicio, Casella casella) {
        StackPane cell = new StackPane();
        cell.setMinSize(110, 48);
        cell.setPrefSize(110, 48);
        cell.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

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
        numero.getStyleClass().add("cell-title");

        Text tipus = new Text(obtenirTextCasella(casella, posicio));
        tipus.getStyleClass().add("cell-type");

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

    @FXML
    private void handleDado(ActionEvent event) {
        if (partida == null) return;

        Dau dauNormal = Dau.crearDau(Dau.TipusDau.NORMAL);
        Jugador jugadorAbans = partida.obtenirJugadorActual();
        int posAbans = jugadorAbans.getPosicioActual();

        partida.jugarTornTirarDau(dauNormal);

        int posDespres = jugadorAbans.getPosicioActual();
        int tirada = posDespres - posAbans;
        dadoResultText.setText("Ha salido: " + Math.max(tirada, 0));

        mostrarUltimEvent();
        actualitzarTot();
    }

    @FXML
    private void handleRapido(ActionEvent event) {
        if (partida == null) return;

        Jugador jugador = partida.obtenirJugadorActual();
        if (!jugador.getInventari().gastarDauRapid()) {
            eventos.setText("Aquest jugador no té daus ràpids.");
            actualitzarTot();
            return;
        }

        int posAbans = jugador.getPosicioActual();
        Dau dauRapid = Dau.crearDau(Dau.TipusDau.RAPID);
        partida.jugarTornTirarDau(dauRapid);

        int posDespres = jugador.getPosicioActual();
        dadoResultText.setText("Ha salido: " + Math.max(posDespres - posAbans, 0));

        mostrarUltimEvent();
        actualitzarTot();
    }

    @FXML
    private void handleLento(ActionEvent event) {
        if (partida == null) return;

        Jugador jugador = partida.obtenirJugadorActual();
        if (!jugador.getInventari().gastarDauLent()) {
            eventos.setText("Aquest jugador no té daus lents.");
            actualitzarTot();
            return;
        }

        int posAbans = jugador.getPosicioActual();
        Dau dauLent = Dau.crearDau(Dau.TipusDau.LENT);
        partida.jugarTornTirarDau(dauLent);

        int posDespres = jugador.getPosicioActual();
        dadoResultText.setText("Ha salido: " + Math.max(posDespres - posAbans, 0));

        mostrarUltimEvent();
        actualitzarTot();
    }

    @FXML
    private void handlePeces(ActionEvent event) {
        if (partida == null) return;

        Jugador jugador = partida.obtenirJugadorActual();
        boolean usat = jugador.utilitzarPeix();

        if (usat) {
            eventos.setText(jugador.getNickname() + " ha utilitzat un peix.");
        } else {
            eventos.setText(jugador.getNickname() + " no té peixos.");
        }

        actualitzarTot();
    }

    @FXML
    private void handleNieve(ActionEvent event) {
        if (partida == null) return;

        Jugador atacant = partida.obtenirJugadorActual();
        Jugador objectiu = buscarPrimerRivalDisponible(atacant);

        if (objectiu == null) {
            eventos.setText("No hi ha cap rival disponible.");
            return;
        }

        boolean ok = partida.jugarTornBolaNeu(objectiu);

        if (ok) {
            eventos.setText(atacant.getNickname() + " ataca " + objectiu.getNickname() + " amb una bola de neu.");
        } else {
            eventos.setText(atacant.getNickname() + " no té boles de neu.");
        }

        actualitzarTot();
    }

    private Jugador buscarPrimerRivalDisponible(Jugador atacant) {
        for (Jugador j : partida.getJugadors()) {
            if (!j.equals(atacant)) {
                return j;
            }
        }
        return null;
    }

    @FXML
    private void handleNewGame(ActionEvent event) {
        SceneManager.mostrarMenu();
    }

    @FXML
    private void handleSaveGame(ActionEvent event) {
        partida.guardarEstat();
        mostrarUltimEvent();
        actualitzarTot();
    }

    @FXML
    private void handleLoadGame(ActionEvent event) {
        partida.carregarPartida(partida.getIdPartida());
        mostrarUltimEvent();
        actualitzarTot();
    }

    @FXML
    private void handleQuitGame(ActionEvent event) {
        Platform.exit();
    }

    private void actualitzarTot() {
        if (partida == null) return;

        actualitzarInventari();
        actualitzarFitxes();
        destacarJugadorActual();
        comprovarGuanyador();
    }

    private void actualitzarInventari() {
        Jugador actual = partida.obtenirJugadorActual();
        if (actual == null) return;

        rapido_t.setText("Dado rápido: " + actual.getInventari().getDausRapids());
        lento_t.setText("Dado lento: " + actual.getInventari().getDausLents());
        peces_t.setText("Peces: " + actual.getInventari().getPeixos());
        nieve_t.setText("Bolas de nieve: " + actual.getInventari().getBolesNeu());
    }

    private void actualitzarFitxes() {
        // Treure posicions antigues
        for (Circle fitxa : fitxes.values()) {
            if (fitxa == null) continue;
            GridPane.setColumnIndex(fitxa, null);
            GridPane.setRowIndex(fitxa, null);
        }

        Map<Integer, Integer> ocupacio = new HashMap<>();

        for (Jugador jugador : partida.getJugadors()) {
            Circle fitxa = fitxes.get(jugador.getIdJugador());
            if (fitxa == null) continue;

            int pos = jugador.getPosicioActual();
            int fila = pos / 5;
            int colBase = pos % 5;
            int col = (fila % 2 == 0) ? colBase : 4 - colBase;

            int offset = ocupacio.getOrDefault(pos, 0);
            ocupacio.put(pos, offset + 1);

            GridPane.setColumnIndex(fitxa, col);
            GridPane.setRowIndex(fitxa, fila);
            GridPane.setHalignment(fitxa, HPos.LEFT);

            switch (offset) {
                case 0 -> fitxa.setTranslateX(8);
                case 1 -> fitxa.setTranslateX(36);
                case 2 -> fitxa.setTranslateX(64);
                case 3 -> fitxa.setTranslateX(92);
                default -> fitxa.setTranslateX(8);
            }
            fitxa.setTranslateY(0);
        }
    }

    private void destacarJugadorActual() {
        for (Circle fitxa : fitxes.values()) {
            if (fitxa != null) {
                fitxa.getStyleClass().remove("current-player");
            }
        }

        Jugador actual = partida.obtenirJugadorActual();
        if (actual == null) return;

        Circle fitxaActual = fitxes.get(actual.getIdJugador());
        if (fitxaActual != null && !fitxaActual.getStyleClass().contains("current-player")) {
            fitxaActual.getStyleClass().add("current-player");
        }
    }

    private void mostrarUltimEvent() {
        List<String> historial = partida.getHistorialAccions();
        if (historial == null || historial.isEmpty()) {
            eventos.setText("Sense esdeveniments.");
            return;
        }

        String ultim = historial.get(historial.size() - 1);
        eventos.setText(ultim);
    }

    private void comprovarGuanyador() {
        for (Jugador j : partida.getJugadors()) {
            if (j.esGuanyador(partida.getTaulell().getNumCaselles())) {
                eventos.setText("🏆 Guanyador: " + j.getNickname());
                desactivarAccions();
                break;
            }
        }
    }

    private void desactivarAccions() {
        dado.setDisable(true);
        rapido.setDisable(true);
        lento.setDisable(true);
        peces.setDisable(true);
        nieve.setDisable(true);
    }
}

