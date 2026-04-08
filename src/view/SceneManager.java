package view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Partida;

import java.io.IOException;

public class SceneManager {

    private static Stage stage;
    private static final double WIDTH = 1100;
    private static final double HEIGHT = 760;

    private SceneManager() {}

    public static void init(Stage primaryStage) {
        stage = primaryStage;
        stage.setTitle("El Joc del Pingüí");
        stage.setResizable(true);
    }

    public static void mostrarMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    SceneManager.class.getResource("/PantallaMenu.fxml")
            );
            Parent root = loader.load();

            Scene scene = new Scene(root, WIDTH, HEIGHT);

            scene.getStylesheets().add(
                    SceneManager.class.getResource("/PantallaMenu.css").toExternalForm()
            );

            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            System.err.println("Error carregant menú");
            e.printStackTrace();
        }
    }

    public static void mostrarJoc(Partida partida) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    SceneManager.class.getResource("/PantallaJuego.fxml")
            );
            Parent root = loader.load();

            PantallaJuego controller = loader.getController();
            controller.setPartida(partida);

            Scene scene = new Scene(root, WIDTH, HEIGHT);

            scene.getStylesheets().add(
                    SceneManager.class.getResource("/PantallaJuego.css").toExternalForm()
            );

            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            System.err.println("Error carregant joc");
            e.printStackTrace();
        }
    }

    public static Stage getStage() {
        return stage;
    }
}