package vista;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import controlador.GestorPartida;
import modelo.Dado;
import modelo.Item;
import modelo.Jugador;
import modelo.Pinguino;
import modelo.Tablero;

public class PantallaJuego {
    @FXML private MenuItem newGame;
    @FXML private MenuItem saveGame;
    @FXML private MenuItem loadGame;
    @FXML private MenuItem quitGame;
    @FXML private Button dado;
    @FXML private Button rapido;
    @FXML private Button lento;
    @FXML private Button peces;
    @FXML private Button nieve;
    @FXML private Text dadoResultText;
    @FXML private Text rapido_t;
    @FXML private Text lento_t;
    @FXML private Text peces_t;
    @FXML private Text nieve_t;
    @FXML private Text eventos;
    @FXML private GridPane tablero;
    @FXML private Circle P1;
    @FXML private Circle P2;
    @FXML private Circle P3;
    @FXML private Circle P4;

    private GestorPartida gestorPartida;
    private static final int COLUMNS = 5;
    private static final String TAG_CASILLA_TEXT = "CASILLA_TEXT";

    @FXML
    private void initialize() {
        gestorPartida = new GestorPartida();
        gestorPartida.nuevaPartida();
        mostrarTiposDeCasillasEnTablero(gestorPartida.getPartida().getTablero());
        refrescarPantalla();
    }

    private void mostrarTiposDeCasillasEnTablero(Tablero t) {
        tablero.getChildren().removeIf(node -> TAG_CASILLA_TEXT.equals(node.getUserData()));
        for (int i = 0; i < t.getCasillas().size(); i++) {
            if (i > 0 && i < 49) {
                Text texto = new Text(t.getCasillas().get(i).getClass().getSimpleName());
                texto.setUserData(TAG_CASILLA_TEXT);
                texto.getStyleClass().add("cell-type");
                int row = i / COLUMNS;
                int col = i % COLUMNS;
                GridPane.setRowIndex(texto, row);
                GridPane.setColumnIndex(texto, col);
                tablero.getChildren().add(texto);
            }
        }
    }

    private void refrescarPantalla() {
        eventos.setText(gestorPartida.getPartida().getUltimoEvento());
        actualizarJugadorEnTablero(P1, gestorPartida.getPartida().getJugadores().get(0).getPosicion());
        actualizarJugadorEnTablero(P2, gestorPartida.getPartida().getJugadores().get(1).getPosicion());
        actualizarInventario();
    }

    private void actualizarInventario() {
        Pinguino actual = (Pinguino) gestorPartida.getPartida().getJugadorActual();
        rapido_t.setText("Dado rápido: " + actual.getInv().getCantidad("rapido"));
        lento_t.setText("Dado lento: " + actual.getInv().getCantidad("lento"));
        peces_t.setText("Peces: " + actual.getInv().getCantidad("pez"));
        nieve_t.setText("Bolas de nieve: " + actual.getInv().getCantidad("bola"));
    }

    private void actualizarJugadorEnTablero(Circle pieza, int posicion) {
        int row = posicion / COLUMNS;
        int col = posicion % COLUMNS;
        GridPane.setRowIndex(pieza, row);
        GridPane.setColumnIndex(pieza, col);
    }

    private void animarMovimiento(Circle pieza, int origen, int destino) {
        int oldRow = origen / COLUMNS;
        int oldCol = origen % COLUMNS;
        int newRow = destino / COLUMNS;
        int newCol = destino % COLUMNS;
        double cellWidth = tablero.getWidth() <= 0 ? 120 : tablero.getWidth() / COLUMNS;
        double cellHeight = tablero.getHeight() <= 0 ? 52 : tablero.getHeight() / 10;
        double dx = (newCol - oldCol) * cellWidth;
        double dy = (newRow - oldRow) * cellHeight;
        TranslateTransition slide = new TranslateTransition(Duration.millis(350), pieza);
        slide.setByX(dx);
        slide.setByY(dy);
        slide.setOnFinished(e -> {
            pieza.setTranslateX(0);
            pieza.setTranslateY(0);
            GridPane.setRowIndex(pieza, newRow);
            GridPane.setColumnIndex(pieza, newCol);
            dado.setDisable(false);
        });
        slide.play();
    }

    @FXML
    private void handleNewGame() {
        gestorPartida.nuevaPartida();
        mostrarTiposDeCasillasEnTablero(gestorPartida.getPartida().getTablero());
        refrescarPantalla();
        dadoResultText.setText("Ha salido: -");
    }

    @FXML
    private void handleSaveGame() {
        gestorPartida.guardarPartida();
        eventos.setText("Partida guardada correctamente.");
    }

    @FXML
    private void handleLoadGame() {
        gestorPartida.cargarPartida(1);
        mostrarTiposDeCasillasEnTablero(gestorPartida.getPartida().getTablero());
        refrescarPantalla();
        eventos.setText("Partida cargada correctamente.");
    }

    @FXML
    private void handleQuitGame() {
        System.exit(0);
    }

    @FXML
    private void handleDado(ActionEvent event) {
        Jugador actual = gestorPartida.getPartida().getJugadorActual();
        int origen = actual.getPosicion();
        Item item = ((Pinguino) actual).getInv().buscarPorNombre("normal");

        if (item instanceof Dado) {
            String mensaje = gestorPartida.jugarTurnoConDado((Dado) item);
            int destino = actual.getPosicion();

            dadoResultText.setText(mensaje);
            refrescarPantalla();

            if (actual == gestorPartida.getPartida().getJugadores().get(0)) {
                animarMovimiento(P1, origen, destino);
            } else {
                animarMovimiento(P2, origen, destino);
            }
        }
    }
    
    @FXML
    private void handleRapido() {
        usarDadoEspecial("rapido");
    }

    @FXML
    private void handleLento() {
        usarDadoEspecial("lento");
    }

    private void usarDadoEspecial(String nombre) {
        Jugador actual = gestorPartida.getPartida().getJugadorActual();
        if (!(actual instanceof Pinguino)) {
            return;
        }

        Pinguino p = (Pinguino) actual;
        Item item = p.getInv().buscarPorNombre(nombre);

        if (item instanceof Dado && item.getCantidad() > 0) {
            int origen = actual.getPosicion();
            p.getInv().gastarItem(nombre, 1);

            String mensaje = gestorPartida.jugarTurnoConDado((Dado) item);
            int destino = actual.getPosicion();

            dadoResultText.setText(mensaje);
            refrescarPantalla();

            if (actual == gestorPartida.getPartida().getJugadores().get(0)) {
                animarMovimiento(P1, origen, destino);
            } else {
                animarMovimiento(P2, origen, destino);
            }
        } else {
            eventos.setText("No tienes ese dado disponible.");
        }
    }
    @FXML
    private void handlePeces() {
        eventos.setText("Los peces se usan automáticamente al caer en el oso.");
    }

    @FXML
    private void handleNieve() {
        eventos.setText("Las bolas de nieve se usan automáticamente cuando dos jugadores coinciden.");
    }

    public void setGestorPartida(GestorPartida gestorPartida) {
        this.gestorPartida = gestorPartida;
    }
}
