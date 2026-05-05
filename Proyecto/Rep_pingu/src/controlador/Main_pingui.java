package controlador; // Indica el paquete al que pertenece esta clase

// Importaciones necesarias de JavaFX para crear la aplicación gráfica
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;

// Clase principal que hereda de Application (requisito para apps JavaFX)
public class Main_pingui extends Application {

    // Método start: es el punto de entrada de cualquier aplicación JavaFX
	@Override
	public void start(Stage primaryStage) throws Exception {
	    // Cambiamos PantallaMenu.fxml por nuestra nueva pantalla Login.fxml
	    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
	    Parent root = loader.load();
	    Scene scene = new Scene(root);
	    primaryStage.setTitle("Acceso al Juego del Pingüino");
	    primaryStage.setScene(scene);
	    primaryStage.show();
	}

    // Método main: punto de entrada estándar de Java
    public static void main(String[] args) {
    	
    	// Lanza la aplicación JavaFX (llama automáticamente a start())
        launch(args);
        
    }
}