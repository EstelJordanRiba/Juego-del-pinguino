import controller.PartidaController;
import model.Partida;
import model.Taulell;

public class Main {

    public static void main(String[] args) {

        // Crear el tablero
        Taulell taulell = new Taulell(50);

        // Crear la partida
        Partida partida = new Partida(1, taulell);

        // Crear el controlador
        PartidaController controller = new PartidaController(partida);

        // Iniciar la partida
        controller.iniciarPartida();

        System.out.println("El joc d'en Pingu ha començat!");
    }
}