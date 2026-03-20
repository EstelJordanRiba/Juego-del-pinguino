package app;

import javafx.application.Application;
import javafx.stage.Stage;
import vista.SceneManager;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        SceneManager.init(primaryStage);
        SceneManager.mostrarMenu();
    }

    public static void main(String[] args) {
        launch(args);
    }
}