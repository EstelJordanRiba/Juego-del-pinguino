package view;

import java.io.IOException;

import controlador.GestorPartida;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

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

            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            System.err.println("Error carregant el menú");
            e.printStackTrace();
        }
    }

    public static void mostrarJoc(GestorPartida gestorPartida) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    SceneManager.class.getResource("/PantallaJuego.fxml")
            );
            Parent root = loader.load();

            PantallaJuego controller = loader.getController();
            controller.setGestorPartida(gestorPartida);
            controller.prepararPartidaDemo();

            Scene scene = new Scene(root, WIDTH, HEIGHT);

            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            System.err.println("Error carregant la pantalla de joc");
            e.printStackTrace();
        }
    }

    public static Stage getStage() {
        return stage;
    }
}