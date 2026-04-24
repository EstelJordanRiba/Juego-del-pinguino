package vista;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.ArrayList;

import controlador.GestorPartida;
import modelo.*;

public class PantallaJuego {
    @FXML private MenuItem newGame, saveGame, loadGame, quitGame;
    @FXML private Button dado, rapido, lento, peces, nieve;
    @FXML private Text dadoResultText, rapido_t, lento_t, peces_t, nieve_t, eventos;
    @FXML private GridPane tablero;
    // Vinculamos los 5 posibles círculos (4 pingüinos + 1 foca)
    @FXML private Circle P1, P2, P3, P4, P5; 
    @FXML
    private void handleNewGame(ActionEvent event) {
        System.out.println("Nueva partida");
        if (gestorPartida != null) {
            gestorPartida.nuevaPartida(4);
            refrescarPantalla();
        }
    }

    @FXML
    private void handleSaveGame(ActionEvent event) {
        if (gestorPartida != null && gestorPartida.getPartida() != null) {
            gestorPartida.guardarPartida();
            eventos.setText("Partida guardada correctamente en la BD.");
        }
    }

    @FXML
    private void handleLoadGame(ActionEvent event) {
        if (gestorPartida != null) {
            gestorPartida.cargarPartida(1);
            refrescarPantalla();
            eventos.setText("Partida cargada desde la BD.");
        }
    }

    @FXML
    private void handleQuitGame(ActionEvent event) {
        System.exit(0);
    }

    private GestorPartida gestorPartida;
    private static final int COLUMNS = 5;
    private static final String TAG_CASILLA_TEXT = "CASILLA_TEXT";

    @FXML
    private void initialize() {
        gestorPartida = new GestorPartida();
        // Fallback en caso de arrancar directamente (se sobreescribirá si venimos del configurador)
        gestorPartida.nuevaPartida(4); 
        mostrarTiposDeCasillasEnTablero(gestorPartida.getPartida().getTablero());
        refrescarPantalla();
    }

    public void iniciarPartidaPersonalizada(ArrayList<Pinguino> jugadores) {
        gestorPartida.nuevaPartidaPersonalizada(jugadores);
        mostrarTiposDeCasillasEnTablero(gestorPartida.getPartida().getTablero());
        refrescarPantalla();
    }

    private void refrescarPantalla() {
        Partida p = gestorPartida.getPartida();
        eventos.setText(p.getUltimoEvento());

        // Actualizar posiciones de todos los jugadores que existan
        Circle[] fichas = {P1, P2, P3, P4, P5};
        for (int i = 0; i < p.getJugadores().size(); i++) {
            if (i < fichas.length && fichas[i] != null) {
                fichas[i].setVisible(true);
                actualizarJugadorEnTablero(fichas[i], p.getJugadores().get(i).getPosicion());
                
                String colorStr = p.getJugadores().get(i).getColor();
                if (colorStr != null) {
                    switch (colorStr.toLowerCase()) {
                        case "rojo": fichas[i].setFill(javafx.scene.paint.Color.RED); break;
                        case "azul": fichas[i].setFill(javafx.scene.paint.Color.DODGERBLUE); break;
                        case "verde": fichas[i].setFill(javafx.scene.paint.Color.LIMEGREEN); break;
                        case "amarillo": fichas[i].setFill(javafx.scene.paint.Color.GOLD); break;
                        case "gris": fichas[i].setFill(javafx.scene.paint.Color.DARKGRAY); break;
                    }
                }
            }
        }

        actualizarInventario();

        if (p.isFinalizada()) {
            abrirPantallaFinal(p.getGanador());
        }
    }

    private void actualizarInventario() {
        Jugador actual = gestorPartida.getPartida().getJugadorActual();
        // IMPORTANTE: Solo actualizamos textos de inventario si el jugador es un Pinguino
        if (actual instanceof Pinguino) {
            Pinguino p = (Pinguino) actual;
            rapido_t.setText("Dado rápido: " + p.getInv().getCantidad("rapido"));
            lento_t.setText("Dado lento: " + p.getInv().getCantidad("lento"));
            peces_t.setText("Peces: " + p.getInv().getCantidad("pez"));
            nieve_t.setText("Bolas: " + p.getInv().getCantidad("bola"));
            dado.setDisable(false); // Habilitar botones para el humano
        } else {
            // Si es la Foca (IA), desactivamos botones
            rapido_t.setText("Turno de la CPU...");
            dado.setDisable(true);
        }
    }

    @FXML
    private void handleDado(ActionEvent event) {
        Jugador actual = gestorPartida.getPartida().getJugadorActual();
        if (!(actual instanceof Pinguino)) return;

        int origen = actual.getPosicion();
        Pinguino p = (Pinguino) actual;
        Item item = p.getInv().buscarPorNombre("normal");

        if (item instanceof Dado) {
            // 1. Turno del Humano
            String mensaje = gestorPartida.jugarTurnoHumano((Dado) item);
            animarMovimiento(getFicha(actual), origen, actual.getPosicion());
            dadoResultText.setText(mensaje);
            refrescarPantalla();

            // 2. TURNO DE LA IA (Si después del humano le toca a la foca)
            checkTurnoIA();
        }
    }

    @FXML
    private void handleRapido(ActionEvent event) {
        Jugador actual = gestorPartida.getPartida().getJugadorActual();
        if (!(actual instanceof Pinguino)) return;

        int origen = actual.getPosicion();
        Pinguino p = (Pinguino) actual;
        Item item = p.getInv().buscarPorNombre("rapido");

        if (item instanceof Dado && item.getCantidad() > 0) {
            String mensaje = gestorPartida.jugarTurnoHumano((Dado) item);
            p.getInv().gastarItem("rapido", 1);
            animarMovimiento(getFicha(actual), origen, actual.getPosicion());
            dadoResultText.setText(mensaje);
            refrescarPantalla();
            checkTurnoIA();
        } else {
            eventos.setText("No tienes dados rápidos.");
        }
    }

    @FXML
    private void handleLento(ActionEvent event) {
        Jugador actual = gestorPartida.getPartida().getJugadorActual();
        if (!(actual instanceof Pinguino)) return;

        int origen = actual.getPosicion();
        Pinguino p = (Pinguino) actual;
        Item item = p.getInv().buscarPorNombre("lento");

        if (item instanceof Dado && item.getCantidad() > 0) {
            String mensaje = gestorPartida.jugarTurnoHumano((Dado) item);
            p.getInv().gastarItem("lento", 1);
            animarMovimiento(getFicha(actual), origen, actual.getPosicion());
            dadoResultText.setText(mensaje);
            refrescarPantalla();
            checkTurnoIA();
        } else {
            eventos.setText("No tienes dados lentos.");
        }
    }

    @FXML
    private void handlePeces(ActionEvent event) {
        Jugador actual = gestorPartida.getPartida().getJugadorActual();
        if (!(actual instanceof Pinguino)) return;
        Pinguino p = (Pinguino) actual;
        
        if (p.getInv().gastarItem("pez", 1)) {
            eventos.setText(p.getNombre() + " se come un pez.");
            refrescarPantalla();
        } else {
            eventos.setText("No tienes peces.");
        }
    }

    @FXML
    private void handleNieve(ActionEvent event) {
        Jugador actual = gestorPartida.getPartida().getJugadorActual();
        if (!(actual instanceof Pinguino)) return;
        Pinguino p = (Pinguino) actual;
        
        if (p.getInv().gastarItem("bola", 1)) {
            eventos.setText(p.getNombre() + " lanza una bola de nieve.");
            refrescarPantalla();
            gestorPartida.siguienteTurno();
            checkTurnoIA();
        } else {
            eventos.setText("No tienes bolas de nieve.");
        }
    }

    private void checkTurnoIA() {
        Jugador siguiente = gestorPartida.getPartida().getJugadorActual();
        if (siguiente instanceof Foca) {
            int origenFoca = siguiente.getPosicion();
            String msjIA = gestorPartida.ejecutarTurnoIA();
            
            // Animamos la ficha de la foca (P5)
            animarMovimiento(P5, origenFoca, siguiente.getPosicion());
            eventos.setText(msjIA);
            refrescarPantalla();
        }
    }

    // Método auxiliar para saber qué círculo corresponde a cada jugador
    private Circle getFicha(Jugador j) {
        int indice = gestorPartida.getPartida().getJugadores().indexOf(j);
        Circle[] fichas = {P1, P2, P3, P4, P5};
        return (indice >= 0 && indice < fichas.length) ? fichas[indice] : P1;
    }

    private void actualizarJugadorEnTablero(Circle pieza, int posicion) {
        GridPane.setRowIndex(pieza, posicion / COLUMNS);
        GridPane.setColumnIndex(pieza, posicion % COLUMNS);
    }

    private void animarMovimiento(Circle pieza, int origen, int destino) {
        if (pieza == null) return;
        int oldRow = origen / COLUMNS;
        int oldCol = origen % COLUMNS;
        int newRow = destino / COLUMNS;
        int newCol = destino % COLUMNS;
        
        double cellWidth = tablero.getWidth() / COLUMNS;
        double cellHeight = tablero.getHeight() / 10;
        
        TranslateTransition slide = new TranslateTransition(Duration.millis(500), pieza);
        slide.setByX((newCol - oldCol) * cellWidth);
        slide.setByY((newRow - oldRow) * cellHeight);
        slide.setOnFinished(e -> {
            pieza.setTranslateX(0);
            pieza.setTranslateY(0);
            actualizarJugadorEnTablero(pieza, destino);
        });
        slide.play();
    }

    // --- El resto de métodos (abrirPantallaFinal, handleQuit, etc.) se mantienen igual ---
    private void abrirPantallaFinal(Jugador ganador) { 
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/PantallaFinal.fxml"));
            Parent root = loader.load();
            
            PantallaFinalController controller = loader.getController();
            controller.setGanador(ganador);
            
            Scene scene = new Scene(root);
            Stage stage = (Stage) tablero.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Fin del Juego");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("¡" + ganador.getNombre() + " ha ganado la partida!");
        }
    }

    private Image imgOso;
    private Image imgAgujero;
    private Image imgTrineo;
    private Image imgEvento;
    private Image imgQuebradizo;
    private Image imgNormal;

    private void cargarImagenes() {
        if (imgOso == null) {
            try {
                imgOso = new Image(getClass().getResourceAsStream("/img/oso.png"));
                imgAgujero = new Image(getClass().getResourceAsStream("/img/agujero.png"));
                imgTrineo = new Image(getClass().getResourceAsStream("/img/trineo.png"));
                imgEvento = new Image(getClass().getResourceAsStream("/img/evento.png"));
                imgQuebradizo = new Image(getClass().getResourceAsStream("/img/quebradizo.png"));
                imgNormal = new Image(getClass().getResourceAsStream("/img/normal.png"));
            } catch (Exception e) {
                System.out.println("Error cargando imágenes: " + e.getMessage());
            }
        }
    }

    private void mostrarTiposDeCasillasEnTablero(Tablero t) {
        cargarImagenes();
        
        // Limpiamos los hijos anteriores del tablero excepto los círculos de los jugadores
        ArrayList<javafx.scene.Node> toRemove = new ArrayList<>();
        for (javafx.scene.Node node : tablero.getChildren()) {
            if (!(node instanceof Circle)) {
                toRemove.add(node);
            }
        }
        tablero.getChildren().removeAll(toRemove);

        for (int i = 0; i < t.getCasillas().size(); i++) {
            Casilla c = t.getCasillas().get(i);
            int row = i / COLUMNS;
            int col = i % COLUMNS;

            javafx.scene.layout.StackPane pane = new javafx.scene.layout.StackPane();
            pane.setStyle("-fx-border-color: #ddd; -fx-border-width: 1px; -fx-background-color: white;");

            ImageView iv = new ImageView();
            // Tamaño ajustado para encajar bien en la casilla
            iv.setFitWidth(50);
            iv.setFitHeight(50);
            iv.setPreserveRatio(true);

            if (c instanceof Oso) {
                iv.setImage(imgOso);
            } else if (c instanceof Agujero) {
                iv.setImage(imgAgujero);
            } else if (c instanceof Trineo) {
                iv.setImage(imgTrineo);
            } else if (c instanceof Evento) {
                iv.setImage(imgEvento);
            } else if (c instanceof SueloQuebradizo) {
                iv.setImage(imgQuebradizo);
            } else {
                iv.setImage(imgNormal);
            }

            pane.getChildren().add(iv);

            if (i == 0 || i == 49) {
                Text text = new Text(i == 0 ? "🏁" : "🏆");
                text.setStyle("-fx-font-size: 18px;");
                javafx.scene.layout.StackPane.setAlignment(text, javafx.geometry.Pos.BOTTOM_RIGHT);
                pane.getChildren().add(text);
            }

            tablero.add(pane, col, row);
        }
        
        // Ensure players are on top
        ArrayList<javafx.scene.Node> circles = new ArrayList<>();
        for (javafx.scene.Node node : tablero.getChildren()) {
            if (node instanceof Circle) {
                circles.add(node);
            }
        }
        for (javafx.scene.Node node : circles) {
            node.toFront();
        }
    }
}