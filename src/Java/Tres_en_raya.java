package Java;

import java.util.Scanner; // Para leer números del teclado
import java.util.Random; // Para que la IA elija posiciones al azar

public class Tres_en_raya {
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in); // Para leer lo que escribe el jugador
		Random random = new Random(); // Para los movimientos aleatorios de la IA

		// Tablero del juego: 3 filas y 3 columnas llenas de '-'
		char[][] tablero = { { '-', '-', '-' }, { '-', '-', '-' }, { '-', '-', '-' } };

		// Menú de selección de modo de juego
		System.out.println("3 EN RAYA ");
		System.out.println("1. 2 jugadores");
		System.out.println("2. Contra la computadora");
		System.out.print("Elige opción: ");

		int opcion = scan.nextInt(); // Guardamos la elección del jugador

		// Símbolos de los jugadores
		char jugador1 = 'X';
		char jugador2 = 'O'; // Este será jugador humano o IA según la opción

		boolean terminado = false; // Nos indica si el juego debe parar

		// Bucle principal: se repite hasta que alguien gana o hay empate
		while (!terminado) {

			// Mostrar el tablero actual por pantalla
			System.out.println("\nTablero actual:");
			for (int i = 0; i < 3; i++) { // Recorremos filas
				for (int j = 0; j < 3; j++) { // Recorremos columnas
					System.out.print(tablero[i][j] + " ");
				}
				System.out.println(); // Salto de línea
			}

			// TURNO DEL JUGADOR 1 (X)
			int fila = 0;
			int columna = 0;

			System.out.println("\nTurno del jugador X");

			// Pedimos fila y columna hasta que ponga una válida
			while (true) {

				System.out.print("Fila (0-2): ");
				fila = scan.nextInt(); // simplemente leemos

				System.out.print("Columna (0-2): ");
				columna = scan.nextInt();

				// Comprobamos que fila y columna están dentro del rango permitido
				if (fila < 0 || fila > 2 || columna < 0 || columna > 2) {
					System.out.println("Debes poner números entre 0 y 2.");
					continue; // Repetimos la entrada
				}

				// Si la casilla está libre, salimos del bucle
				if (tablero[fila][columna] == '-') {
					break;

				} else {
					System.out.println("Casilla ocupada. Intenta otra.");
				}
			}

			// Colocamos la ficha de jugador 1 en el tablero
			tablero[fila][columna] = jugador1;

			// COMPROBAMOS SI EL JUGADOR 1 HA GANADO

			boolean gana1 = false;

			// Revisamos filas
			for (int i = 0; i < 3; i++) {
				if (tablero[i][0] == jugador1 && tablero[i][1] == jugador1 && tablero[i][2] == jugador1)
					gana1 = true;
			}

			// Revisamos columnas
			for (int j = 0; j < 3; j++) {
				if (tablero[0][j] == jugador1 && tablero[1][j] == jugador1 && tablero[2][j] == jugador1)
					gana1 = true;
			}

			// Revisamos diagonal principal
			if (tablero[0][0] == jugador1 && tablero[1][1] == jugador1 && tablero[2][2] == jugador1)
				gana1 = true;

			// Revisamos diagonal secundaria
			if (tablero[0][2] == jugador1 && tablero[1][1] == jugador1 && tablero[2][0] == jugador1)
				gana1 = true;

			// Si gana jugador1, se acaba el juego
			if (gana1) {
				System.out.println("\nJugador X ha ganado");
				break;
			}

			// COMPROBAMOS SI HAY EMPATE

			boolean lleno = true;

			// Revisamos todo el tablero buscando casillas vacías
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					if (tablero[i][j] == '-') {
						lleno = false; // Todavía hay huecos
					}
				}
			}

			if (lleno) {
				System.out.println("\nEmpate. El tablero está lleno.");
				break;
			}
			// TURNO DEL JUGADOR 2 O DE LA COMPUTADORa
			System.out.println("\nTurno del jugador O");

			if (opcion == 1) {

				// MODO DOS JUGADORES

				while (true) {

					System.out.print("Fila (0-2): ");
					fila = scan.nextInt();

					System.out.print("Columna (0-2): ");
					columna = scan.nextInt();

					if (fila < 0 || fila > 2 || columna < 0 || columna > 2) {
						System.out.println("Números fuera de rango.");

					}

					if (tablero[fila][columna] == '-') {
						break; // casilla correcta
					} else {
						System.out.println("Casilla ocupada.");
					}
				}

			} else {

				// MODO COMPUTADORA (IA)

				System.out.println("La computadora está pensando...");

				// Elegimos una casilla al azar que esté libre
				while (true) {
					fila = random.nextInt(3);
					columna = random.nextInt(3);

					if (tablero[fila][columna] == '-')
						break;
				}
			}

			// Colocar ficha del jugador 2 o IA
			tablero[fila][columna] = jugador2;

			// COMPROBAMOS SI EL JUGADOR 2/IA HA GANADO

			boolean gana2 = false;

			// Filas
			for (int i = 0; i < 3; i++) {
				if (tablero[i][0] == jugador2 && tablero[i][1] == jugador2 && tablero[i][2] == jugador2)
					gana2 = true;
			}

			// Columnas
			for (int j = 0; j < 3; j++) {
				if (tablero[0][j] == jugador2 && tablero[1][j] == jugador2 && tablero[2][j] == jugador2)
					gana2 = true;
			}

			// Diagonales
			if (tablero[0][0] == jugador2 && tablero[1][1] == jugador2 && tablero[2][2] == jugador2)
				gana2 = true;
			if (tablero[0][2] == jugador2 && tablero[1][1] == jugador2 && tablero[2][0] == jugador2)
				gana2 = true;

			if (gana2) {
				if (opcion == 1)
					System.out.println("\n¡Jugador O ha ganado!");
				else
					System.out.println("\n¡La computadora ha ganado!");
				break;
			}
		}
		// Mostrar tablero final
		System.out.println("\nTablero final:");
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				System.out.print(tablero[i][j] + " ");
			}
			System.out.println();
		}
	}
}
