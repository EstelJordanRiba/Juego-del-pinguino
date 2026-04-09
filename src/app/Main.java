package app;
	import controller.PartidaController;
	import model.Partida;
	import model.Taulell;
	import model.Jugador;

	public class Main {

	    public static void main(String[] args) {

	        // Crear el tablero
	        Taulell taulell = new Taulell(50);

	        // Crear la partida
	        Partida partida = new Partida(1, taulell);

	        // 🔥 AÑADIR JUGADORES
	        partida.afegirJugador(new Jugador(1, "Pingu1", 0));
	        partida.afegirJugador(new Jugador(2, "Pingu2", 1));

	        // Crear el controlador
	        PartidaController controller = new PartidaController(partida);

	        // Iniciar la partida
	        controller.iniciarPartida();

	        System.out.println("El joc d'en Pingu ha començat!");
	    }
	}
