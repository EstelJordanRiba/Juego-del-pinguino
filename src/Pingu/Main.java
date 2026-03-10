package Pingu;

                                                                                                     
import controller.PartidaController;
import model.Partida;
import model.Taulell;

public class Main {

    public static void main(String[] args) {

        Taulell taulell = new Taulell(50);
        Partida partida = new Partida(1, taulell);

        PartidaController controller = new PartidaController(partida);

        controller.iniciarPartida();

    }

}