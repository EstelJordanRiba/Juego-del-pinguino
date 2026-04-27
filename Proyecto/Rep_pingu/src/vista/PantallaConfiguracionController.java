package vista;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
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

    @FXML private ComboBox<Integer> numJugadoresCombo;
    
    @FXML private HBox rowP1;
    @FXML private TextField nombreP1;
    @FXML private ComboBox<String> colorP1;

    @FXML private HBox rowP2;
    @FXML private TextField nombreP2;
    @FXML private ComboBox<String> colorP2;

    @FXML private HBox rowP3;
    @FXML private TextField nombreP3;
    @FXML private ComboBox<String> colorP3;

    @FXML private HBox rowP4;
    @FXML private TextField nombreP4;
    @FXML private ComboBox<String> colorP4;

    @FXML private Button startButton;

    @FXML
    private void initialize() {
        numJugadoresCombo.getItems().addAll(2, 3, 4);
        numJugadoresCombo.setValue(4);
        
        String[] colores = {"Azul", "Rojo", "Verde", "Amarillo"};
        colorP1.getItems().addAll(colores); colorP1.setValue("Azul");
        colorP2.getItems().addAll(colores); colorP2.setValue("Rojo");
        colorP3.getItems().addAll(colores); colorP3.setValue("Verde");
        colorP4.getItems().addAll(colores); colorP4.setValue("Amarillo");

        numJugadoresCombo.setOnAction(e -> actualizarFilas());
        actualizarFilas();
    }

    private void actualizarFilas() {
        int num = numJugadoresCombo.getValue();
        rowP3.setVisible(num >= 3);
        rowP3.setManaged(num >= 3);
        rowP4.setVisible(num >= 4);
        rowP4.setManaged(num >= 4);
    }

    @FXML
    private void handleStartGame(ActionEvent event) {
        ArrayList<Pinguino> jugadores = new ArrayList<>();
        int num = numJugadoresCombo.getValue();

        TextField[] nombres = {nombreP1, nombreP2, nombreP3, nombreP4};
        @SuppressWarnings("unchecked")
        ComboBox<String>[] colores = new ComboBox[]{colorP1, colorP2, colorP3, colorP4};

        for (int i = 0; i < num; i++) {
            String nombre = nombres[i].getText().trim();
            if (nombre.isEmpty()) {
                nombre = "Jugador " + (i + 1);
            }
            String color = colores[i].getValue();
            
            Inventario inv = new Inventario();
            inv.añadirOActualizar(new Dado("normal", 1, 1, 6), 3);
            jugadores.add(new Pinguino(nombre, color, 0, inv));
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/PantallaJuego.fxml"));
            Parent root = loader.load();
            
            PantallaJuego controller = loader.getController();
            controller.iniciarPartidaPersonalizada(jugadores);

            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("El joc d'en Pingu");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
