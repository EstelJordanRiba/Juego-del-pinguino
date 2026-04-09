package view;

import java.io.IOException;

import controller.PartidaController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {

    private static Stage stage;

    private SceneManager() {
    }

    public static void init(Stage primaryStage) {

    	
    	stage = primaryStage;
        stage.setTitle("El Joc del Pingüí");
        stage.setResizable(true);
    }

    public static void mostrarMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource("/PantallaMenu.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            System.err.println("Error carregant PantallaMenu.fxml");
            e.printStackTrace();
        }
    }

    public static void mostrarJuego(PartidaController gestorPartida) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource("/PantallaJuego.fxml"));
            Parent root = loader.load();

            PantallaJuego controller = loader.getController();
            controller.setPartida(gestorPartida.getPartida());

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            System.err.println("Error carregant PantallaJuego.fxml");
            e.printStackTrace();
        }
    }
}