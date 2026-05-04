package vista;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;

public class PantallaMenu {
    @FXML private MenuItem newGame;
    @FXML private MenuItem saveGame;
    @FXML private MenuItem loadGame;
    @FXML private MenuItem quitGame;
    
    @FXML private Button btnJugar;
    @FXML private Button btnPersonalizar;
    @FXML private Button btnCargar;

    @FXML
    private void initialize() {
        System.out.println("Pantalla menú iniciada");
    }

    @FXML
    private void handleNewGame() {
        System.out.println("Nuevo juego");
    }

    @FXML
    private void handleSaveGame() {
        System.out.println("Guardar desde menú no implementado");
    }

    @FXML
    private void handleLoadGame() {
        System.out.println("Cargar desde menú no implementado");
    }

    @FXML
    private void handleQuitGame() {
        System.exit(0);
    }

    @FXML
    private void handleJugar(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/PantallaJuego.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("El Juego del Pingüino");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handlePersonalizar(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/PantallaConfiguracion.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Configuración de Partida");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCargar(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/PantallaJuego.fxml"));
            Parent root = loader.load();
            
            PantallaJuego controller = loader.getController();
            controller.cargarPartidaExistente();
            
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("El Juego del Pingüino - Partida Cargada");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}