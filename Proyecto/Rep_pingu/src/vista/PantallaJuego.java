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
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tooltip;
import javafx.scene.control.ButtonBar.ButtonData;
import java.util.Optional;
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
        configurarTooltips();
        refrescarPantalla();
    }

    private void configurarTooltips() {
        Tooltip.install(dado, new Tooltip("Dado Normal: Avanza de 1 a 6 casillas."));
        Tooltip.install(rapido, new Tooltip("Dado Rápido: Avanza de 5 a 10 casillas."));
        Tooltip.install(lento, new Tooltip("Dado Lento: Avanza de 1 a 3 casillas."));
        Tooltip.install(peces, new Tooltip("Pez: Evita que el Oso te devuelva al inicio."));
        Tooltip.install(nieve, new Tooltip("Bola de Nieve: Pasa el turno rápido o minimiza pérdidas si te pisa la Foca."));
    }

    public void iniciarPartidaPersonalizada(ArrayList<Pinguino> jugadores) {
        gestorPartida.nuevaPartidaPersonalizada(jugadores);
        mostrarTiposDeCasillasEnTablero(gestorPartida.getPartida().getTablero());
        refrescarPantalla();
    }

    private void refrescarPantalla() {
        Partida p = gestorPartida.getPartida();
        eventos.setText(p.getUltimoEvento());

        // Ocultar todas las fichas y quitarles el brillo primero
        Circle[] fichas = {P1, P2, P3, P4, P5};
        for (Circle ficha : fichas) {
            if (ficha != null) {
                ficha.setVisible(false);
                ficha.setEffect(null);
            }
        }

        int jugadorActualIndex = p.getJugadorActualIndice();

        // Actualizar posiciones de todos los jugadores que existan
        for (int i = 0; i < p.getJugadores().size(); i++) {
            if (i < fichas.length && fichas[i] != null) {
                fichas[i].setVisible(true);
                actualizarJugadorEnTablero(fichas[i], p.getJugadores().get(i).getPosicion());
                
                String colorStr = p.getJugadores().get(i).getColor();
                if (colorStr != null) {
                    switch (colorStr.toLowerCase()) {
                        case "rojo": fichas[i].setFill(Color.RED); break;
                        case "azul": fichas[i].setFill(Color.DODGERBLUE); break;
                        case "verde": fichas[i].setFill(Color.LIMEGREEN); break;
                        case "amarillo": fichas[i].setFill(Color.GOLD); break;
                        case "gris": fichas[i].setFill(Color.DARKGRAY); break;
                    }
                }

                // Añadir brillo al jugador cuyo turno esté activo
                if (i == jugadorActualIndex) {
                    DropShadow glow = new DropShadow();
                    glow.setColor((Color) fichas[i].getFill());
                    glow.setRadius(25);
                    glow.setSpread(0.7);
                    fichas[i].setEffect(glow);
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

    private void procesarTurnoPinguino(Pinguino p, Dado dado, String tipoDado) {
        int origen = p.getPosicion();
        String mensaje = gestorPartida.jugarTurnoHumano(dado);
        
        if (!tipoDado.equals("normal")) {
            p.getInv().gastarItem(tipoDado, 1);
        }
        
        if (mensaje.contains("OSO_ATAQUE")) {
            manejarAtaqueOso(p);
            dadoResultText.setText("¡Atacado por el oso!");
        } else {
            animarMovimiento(getFicha(p), origen, p.getPosicion());
            dadoResultText.setText(mensaje);
        }
        
        mostrarAlertasEventos(mensaje);
        
        refrescarPantalla();
        checkTurnoIA();
    }

    private void manejarAtaqueOso(Pinguino p) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("¡Ataque del Oso!");
        alert.setHeaderText("¡Un oso salvaje ha aparecido en tu casilla!");
        alert.setContentText("¿Qué deseas hacer?");
        
        if (imgOso != null) {
            ImageView imageView = new ImageView(imgOso);
            imageView.setFitHeight(100);
            imageView.setFitWidth(100);
            alert.setGraphic(imageView);
        }
        
        ButtonType btnPez = new ButtonType("Lanzar Pez", ButtonData.OK_DONE);
        ButtonType btnHuir = new ButtonType("Aceptar Destino (Huir)", ButtonData.CANCEL_CLOSE);
        
        if (p.getInv().getCantidad("pez") > 0) {
            alert.getButtonTypes().setAll(btnPez, btnHuir);
        } else {
            alert.setContentText("No tienes peces. ¡El oso te atrapa!");
            alert.getButtonTypes().setAll(btnHuir);
        }
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == btnPez) {
            p.getInv().gastarItem("pez", 1);
            gestorPartida.getPartida().setUltimoEvento(p.getNombre() + " usó un pez para escapar del oso.");
        } else {
            p.setPosicion(0);
            gestorPartida.getPartida().setUltimoEvento(p.getNombre() + " fue atrapado por el oso y vuelve al inicio.");
        }
    }

    @FXML
    private void handleDado(ActionEvent event) {
        Jugador actual = gestorPartida.getPartida().getJugadorActual();
        if (!(actual instanceof Pinguino)) return;

        Pinguino p = (Pinguino) actual;
        Item item = p.getInv().buscarPorNombre("normal");

        if (item instanceof Dado) {
            procesarTurnoPinguino(p, (Dado) item, "normal");
        }
    }

    @FXML
    private void handleRapido(ActionEvent event) {
        Jugador actual = gestorPartida.getPartida().getJugadorActual();
        if (!(actual instanceof Pinguino)) return;

        Pinguino p = (Pinguino) actual;
        Item item = p.getInv().buscarPorNombre("rapido");

        if (item instanceof Dado && item.getCantidad() > 0) {
            procesarTurnoPinguino(p, (Dado) item, "rapido");
        } else {
            eventos.setText("No tienes dados rápidos.");
        }
    }

    @FXML
    private void handleLento(ActionEvent event) {
        Jugador actual = gestorPartida.getPartida().getJugadorActual();
        if (!(actual instanceof Pinguino)) return;

        Pinguino p = (Pinguino) actual;
        Item item = p.getInv().buscarPorNombre("lento");

        if (item instanceof Dado && item.getCantidad() > 0) {
            procesarTurnoPinguino(p, (Dado) item, "lento");
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
            
            mostrarAlertasEventos(msjIA);
            
            refrescarPantalla();
        }
    }

    private void mostrarAlertasEventos(String mensaje) {
        if (mensaje == null) return;
        
        if (mensaje.contains("GUERRA_NIEVE") || mensaje.contains("FOCA_INTERACTUA")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("¡Evento Especial en la Casilla!");
            alert.setHeaderText("¡Choque de jugadores!");
            
            // Extraer la parte relevante del mensaje para mostrarla
            StringBuilder contenido = new StringBuilder();
            String[] partes = mensaje.split("\\|");
            for (String parte : partes) {
                if (parte.contains("GUERRA_NIEVE") || parte.contains("FOCA_INTERACTUA")) {
                    contenido.append(parte.trim()).append("\n\n");
                }
            }
            
            alert.setContentText(contenido.toString().trim());
            alert.showAndWait();
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
                Tooltip.install(pane, new Tooltip("Oso: Te ataca y vuelves a la salida a menos que uses un pez."));
            } else if (c instanceof Agujero) {
                iv.setImage(imgAgujero);
                Tooltip.install(pane, new Tooltip("Agujero: Caes y retrocedes hasta el agujero anterior."));
            } else if (c instanceof Trineo) {
                iv.setImage(imgTrineo);
                Tooltip.install(pane, new Tooltip("Trineo: Te deslizas hacia adelante hasta el siguiente trineo."));
            } else if (c instanceof Evento) {
                iv.setImage(imgEvento);
                Tooltip.install(pane, new Tooltip("Casilla Evento: Te da objetos (peces, dados) o te quita el turno."));
            } else if (c instanceof SueloQuebradizo) {
                iv.setImage(imgQuebradizo);
                Tooltip.install(pane, new Tooltip("Suelo Quebradizo: Al pisarlo 2 veces se rompe y te caes al agujero."));
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