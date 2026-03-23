package app;

import javafx.application.Application;
import javafx.stage.Stage;
import vista.SceneManager;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {

        try {
            // Inicialitzar gestor d'escenes
            SceneManager.init(primaryStage);

            // Mostrar menú inicial
            SceneManager.mostrarMenu();

        } catch (Exception e) {
            System.err.println("Error iniciant l'aplicació");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}