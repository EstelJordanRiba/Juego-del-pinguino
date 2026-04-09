package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MenuController {

    @FXML
    private void onIniciarPartida() {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/game.fxml"));
            Parent root = loader.load();

            GameController controller = loader.getController();
            controller.inicialitzarPartida();

            Stage stage = (Stage) root.getScene().getWindow();
            stage.setScene(new Scene(root, 400, 400));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onSortir() {
        System.exit(0);
    }
}