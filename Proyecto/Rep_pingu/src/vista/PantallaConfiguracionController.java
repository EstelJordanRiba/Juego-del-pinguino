package vista;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import java.util.ArrayList;

import modelo.Pinguino;
import modelo.Inventario;
import modelo.Dado;

public class PantallaConfiguracionController {

    // --- ELEMENTOS FXML (Componentes de la UI) ---
    @FXML private ComboBox<Integer> numJugadoresCombo; // Selector de 2 a 4 jugadores
    
    // Contenedores HBox para cada jugador (permiten agrupar Nombre y Color en una fila)
    @FXML private HBox rowP1; @FXML private TextField nombreP1; @FXML private ComboBox<String> colorP1;
    @FXML private HBox rowP2; @FXML private TextField nombreP2; @FXML private ComboBox<String> colorP2;
    @FXML private HBox rowP3; @FXML private TextField nombreP3; @FXML private ComboBox<String> colorP3;
    @FXML private HBox rowP4; @FXML private TextField nombreP4; @FXML private ComboBox<String> colorP4;

    @FXML private Button startButton;

    /**
     * Método initialize: Se ejecuta automáticamente al cargar la pantalla.
     * Configura los valores iniciales de los desplegables.
     */
    @FXML
    private void initialize() {
        // Añadimos opciones de cantidad de jugadores
        numJugadoresCombo.getItems().addAll(2, 3, 4);
        numJugadoresCombo.setValue(4);
        
        // Colores disponibles para los pingüinos
        String[] colores = {"Azul", "Rojo", "Verde", "Amarillo"};
        colorP1.getItems().addAll(colores); colorP1.setValue("Azul");
        colorP2.getItems().addAll(colores); colorP2.setValue("Rojo");
        colorP3.getItems().addAll(colores); colorP3.setValue("Verde");
        colorP4.getItems().addAll(colores); colorP4.setValue("Amarillo");

        // Evento: cada vez que cambie el número de jugadores, actualizamos la vista
        numJugadoresCombo.setOnAction(e -> actualizarFilas());
        actualizarFilas();
    }

    /**
     * Muestra u oculta las filas de los jugadores 3 y 4 según la selección del usuario.
     */
    private void actualizarFilas() {
        int num = numJugadoresCombo.getValue();
        // 'setVisible' controla si se ve; 'setManaged' controla si ocupa espacio en el diseño
        rowP3.setVisible(num >= 3);
        rowP3.setManaged(num >= 3);
        rowP4.setVisible(num >= 4);
        rowP4.setManaged(num >= 4);
    }

    /**
     * Recoge los datos de la interfaz, crea los objetos Pinguino y lanza la pantalla de juego.
     */
    @FXML
    private void handleStartGame(ActionEvent event) {
        ArrayList<Pinguino> jugadores = new ArrayList<>();
        int num = numJugadoresCombo.getValue();

        // Arrays auxiliares para recorrer los campos fácilmente con un bucle
        TextField[] nombres = {nombreP1, nombreP2, nombreP3, nombreP4};
        @SuppressWarnings("unchecked")
        ComboBox<String>[] colores = new ComboBox[]{colorP1, colorP2, colorP3, colorP4};

        for (int i = 0; i < num; i++) {
            String nombre = nombres[i].getText().trim();
            // Si el nombre está vacío, asignamos uno genérico
            if (nombre.isEmpty()) {
                nombre = "Jugador " + (i + 1);
            }
            String color = colores[i].getValue();
            
            // Creamos el inventario inicial para cada pingüino (3 dados normales)
            Inventario inv = new Inventario();
            inv.añadirOActualizar(new Dado("normal", 1, 1, 6), 3);
            jugadores.add(new Pinguino(nombre, color, 0, inv));
        }

        try {
            // Carga de la Pantalla de Juego
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/PantallaJuego.fxml"));
            Parent root = loader.load();
            
            // IMPORTANTE: Obtenemos el controlador de la pantalla de juego para pasarle los datos
            PantallaJuego controller = loader.getController();
            controller.iniciarPartidaPersonalizada(jugadores);

            // Cambio de escena
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("El joc d'en Pingu");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}