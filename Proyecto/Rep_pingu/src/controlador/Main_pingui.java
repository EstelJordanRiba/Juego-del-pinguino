package controlador; // Indica el paquete al que pertenece esta clase

// Importaciones necesarias de JavaFX para crear la aplicación gráfica
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;

// Clase principal que hereda de Application (requisito para apps JavaFX)
public class Main_pingui extends Application {

    /**
     * Método start: es el corazón del arranque de JavaFX.
     * @param primaryStage Representa la ventana principal (el "marco" de la aplicación).
     */
	@Override
	public void start(Stage primaryStage) throws Exception {
	    // 1. Localizamos y cargamos el archivo FXML que contiene el diseño del Login
	    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
	    
	    // 2. Cargamos el diseño en un objeto Parent (la raíz de todos los elementos visuales)
	    Parent root = loader.load();
	    
	    // 3. Creamos una Escena (Scene) que contiene nuestro diseño cargado
	    Scene scene = new Scene(root);
	    
	    // 4. Configuramos la ventana (Título y Escena)
	    primaryStage.setTitle("Acceso al Juego del Pingüino");
	    primaryStage.setScene(scene);
	    
	    // 5. Hacemos que la ventana sea visible para el usuario
	    primaryStage.show();
	}

    /**
     * Método main: es lo primero que busca el sistema operativo al ejecutar el .jar o el proyecto.
     */
    public static void main(String[] args) {
    	
    	// launch(args): Es un método interno de JavaFX que prepara el sistema gráfico 
        // y después llama automáticamente al método start().
        launch(args);
        
    }
}