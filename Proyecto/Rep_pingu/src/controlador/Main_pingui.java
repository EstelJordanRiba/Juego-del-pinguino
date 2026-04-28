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
		
		// FXMLLoader carga el archivo FXML (interfaz gráfica diseñada previamente)
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/PantallaMenu.fxml"));
	    
		// Se carga el contenido del FXML en un nodo raíz (root)
	    Parent root = loader.load();

	    // Se crea una escena con el contenido cargado
	    Scene scene = new Scene(root);
	    
	    // Se asigna la escena al escenario principal (ventana)
	    primaryStage.setScene(scene);
	    
	    // Se establece el título de la ventana
	    primaryStage.setTitle("El Juego del Pingüino");
	    
	    // Se muestra la ventana en pantalla
	    primaryStage.show();
	}

    // Método main: punto de entrada estándar de Java
    public static void main(String[] args) {
    	
    	// Lanza la aplicación JavaFX (llama automáticamente a start())
        launch(args);
        
    }
}