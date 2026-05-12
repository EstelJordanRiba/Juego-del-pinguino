package vista; // Define el paquete donde se encuentra esta clase

import javafx.fxml.FXML; // Permite usar anotaciones @FXML para conectar con el archivo FXML
import javafx.scene.control.Button; // Importa la clase Button de JavaFX
import javafx.scene.control.MenuItem; // Importa la clase MenuItem para los menús
import javafx.fxml.FXMLLoader; // Permite cargar archivos FXML
import javafx.scene.Parent; // Clase base para todos los nodos que pueden tener hijos
import javafx.scene.Scene; // Representa el contenido de una ventana
import javafx.stage.Stage; // Representa la ventana principal
import javafx.event.ActionEvent; // Evento que se genera al interactuar con botones, etc.
import javafx.scene.Node; // Clase base para todos los elementos visuales

public class PantallaMenu { // Declaración de la clase del menú principal

    @FXML private MenuItem newGame; // Referencia al menú "Nuevo juego" del FXML
    @FXML private MenuItem saveGame; // Referencia al menú "Guardar juego"
    @FXML private MenuItem loadGame; // Referencia al menú "Cargar juego"
    @FXML private MenuItem quitGame; // Referencia al menú "Salir"

    @FXML private Button btnJugar; // Botón para iniciar partida
    @FXML private Button btnPersonalizar; // Botón para personalizar partida
    @FXML private Button btnCargar; // Botón para cargar partida

    @FXML
    private void initialize() { // Método que se ejecuta automáticamente al cargar la vista
        System.out.println("Pantalla menú iniciada"); // Mensaje en consola para comprobar que se cargó
    }

    @FXML
    private void handleNewGame() { // Método que se ejecuta al pulsar "Nuevo juego"
        System.out.println("Nuevo juego"); // Muestra mensaje en consola
    }

    @FXML
    private void handleSaveGame() { // Método para guardar partida desde el menú
        System.out.println("Guardar desde menú no implementado"); // Indica que aún no está implementado
    }

    @FXML
    private void handleLoadGame() { // Método para cargar partida desde el menú
        System.out.println("Cargar desde menú no implementado"); // Indica que aún no está implementado
    }

    @FXML
    private void handleQuitGame() { // Método para salir del juego
        System.exit(0); // Cierra completamente la aplicación
    }

    @FXML
    private void handleJugar(ActionEvent event) { // Método que se ejecuta al pulsar el botón "Jugar"
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/PantallaJuego.fxml")); 
            // Carga el archivo FXML de la pantalla de juego

            Parent root = loader.load(); 
            // Carga el contenido del FXML en un objeto raíz

            Scene scene = new Scene(root); 
            // Crea una nueva escena con ese contenido

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow(); 
            // Obtiene la ventana actual desde el botón que generó el evento

            stage.setScene(scene); 
            // Cambia la escena actual a la nueva

            stage.setTitle("El Juego del Pingüino"); 
            // Cambia el título de la ventana
        } catch (Exception e) {
            e.printStackTrace(); // Imprime el error si ocurre algún problema
        }
    }

    @FXML
    private void handlePersonalizar(ActionEvent event) { // Método al pulsar "Personalizar"
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/PantallaConfiguracion.fxml")); 
            // Carga la pantalla de configuración

            Parent root = loader.load(); 
            // Carga el contenido del FXML

            Scene scene = new Scene(root); 
            // Crea la nueva escena

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow(); 
            // Obtiene la ventana actual

            stage.setScene(scene); 
            // Cambia la escena

            stage.setTitle("Configuración de Partida"); 
            // Cambia el título de la ventana
        } catch (Exception e) {
            e.printStackTrace(); // Muestra errores en consola
        }
    }

    @FXML
    private void handleCargar(ActionEvent event) { // Método al pulsar "Cargar"
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/PantallaJuego.fxml")); 
            // Carga la pantalla de juego

            Parent root = loader.load(); 
            // Carga el contenido del FXML

            PantallaJuego controller = loader.getController(); 
            // Obtiene el controlador de la pantalla de juego

            controller.cargarPartidaExistente(); 
            // Llama a un método del controlador para cargar una partida guardada

            Scene scene = new Scene(root); 
            // Crea una nueva escena

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow(); 
            // Obtiene la ventana actual

            stage.setScene(scene); 
            // Cambia la escena

            stage.setTitle("El Juego del Pingüino - Partida Cargada"); 
            // Cambia el título indicando que se ha cargado una partida
        } catch (Exception e) {
            e.printStackTrace(); // Muestra errores en consola
        }
    }
}