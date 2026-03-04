package Java;

import java.util.Random;
import java.util.Scanner;

// Clase pública principal para poder compilar el archivo
public class A7 { 
    public static void main(String[] args) {
        System.out.println("Este archivo contiene 3 juegos modulares.");
        System.out.println("Ejecuta la clase deseada para jugar: NumerosAleatorios, PiedraPapelTijeras o TresEnRaya");
    }
}

//JUEGO 1: NÚMEROS ALEATORIOS
//JUEGO 1: NÚMEROS ALEATORIOS
//JUEGO 1: NÚMEROS ALEATORIOS
// JUEGO 1: NÚMEROS ALEATORIOS

class NumerosAleatorios {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        Random rand = new Random();

        // Generamos un número aleatorio entre 0 y 24
        int numeroSecreto = rand.nextInt(25);

        // Pedimos al usuario el número de intentos
        int intentos = elegirIntentos(scan);

        // Iniciamos el juego con la función jugar
        jugar(numeroSecreto, intentos, scan);
    }

    // Función para elegir el número de intentos
    private static int elegirIntentos(Scanner scan) {
        System.out.print("¿Cuántos intentos quieres? ");
        return scan.nextInt();
    }

    // Función que controla el flujo principal del juego
    private static void jugar(int secreto, int intentos, Scanner scan) {
        for (int i = 1; i <= intentos; i++) {
            System.out.print("Intento " + i + ": ");
            int numero = scan.nextInt();

            // Comprobamos si el número es correcto, mayor o menor
            if (comprobarNumero(numero, secreto)) {
                System.out.println("Correcto. Has ganado.");
                return;
            }
        }
        System.out.println("Has perdido. El número era: " + secreto);
    }

    // Función que compara el número del usuario con el secreto
    private static boolean comprobarNumero(int usuario, int secreto) {
        if (usuario == secreto) return true;
        if (usuario < secreto) System.out.println("El número es mayor.");
        else System.out.println("El número es menor.");
        return false;
    }
}

//JUEGO 2: PIEDRA, PAPEL O TIJERAS
// JUEGO 2: PIEDRA, PAPEL O TIJERAS
//JUEGO 2: PIEDRA, PAPEL O TIJERAS
//JUEGO 2: PIEDRA, PAPEL O TIJERAS

class PiedraPapelTijeras {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        System.out.println("1. Dos jugadores");
        System.out.println("2. Contra IA");
        int opcion = scan.nextInt();
        scan.nextLine();

        if (opcion == 1) jugarDosJugadores(scan);
        else jugarContraIA(scan);
    }

    // Función para jugar entre dos jugadores humanos
    private static void jugarDosJugadores(Scanner scan) {
        System.out.print("Jugador 1: ");
        String j1 = scan.nextLine();
        System.out.print("Jugador 2: ");
        String j2 = scan.nextLine();

        if (movimientoValido(j1) && movimientoValido(j2))
            comprobarGanador(j1, j2);
        else System.out.println("Movimiento no válido.");
    }

    // Función para jugar contra la IA
    private static void jugarContraIA(Scanner scan) {
        Random rand = new Random();
        String[] opciones = {"piedra", "papel", "tisores"};

        System.out.print("Jugador: ");
        String jugador = scan.nextLine();
        String ia = opciones[rand.nextInt(3)];
        System.out.println("IA eligió: " + ia);

        if (movimientoValido(jugador))
            comprobarGanador(jugador, ia);
        else System.out.println("Movimiento no válido.");
    }

    // Función que valida que el movimiento sea piedra, papel o tijeras
    private static boolean movimientoValido(String mov) {
        return mov.equals("piedra") || mov.equals("papel") || mov.equals("tisores");
    }

    // Función que determina el ganador
    private static void comprobarGanador(String j1, String j2) {
        if (j1.equals(j2)) System.out.println("Empate");
        else if ((j1.equals("piedra") && j2.equals("tisores")) ||
                 (j1.equals("papel") && j2.equals("piedra")) ||
                 (j1.equals("tisores") && j2.equals("papel")))
            System.out.println("Gana Jugador 1");
        else System.out.println("Gana Jugador 2");
    }
}

//JUEGO 3: TRES EN RAYA
// JUEGO 3: TRES EN RAYA
//JUEGO 3: TRES EN RAYA
//JUEGO 3: TRES EN RAYA

class TresEnRaya {

    private static char[][] tablero = new char[3][3];

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        Random rand = new Random();

        // Inicializamos el tablero con '-'
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                tablero[i][j] = '-';

        System.out.println("1. Dos jugadores");
        System.out.println("2. Contra IA");
        int opcion = scan.nextInt();
        scan.nextLine();

        char jugador = 'X';

        // Bucle principal del juego
        while (true) {
            mostrarTablero();

            // Si jugador es X o modo dos jugadores, mueve humano
            if (jugador == 'X' || opcion == 1)
                movimientoJugador(scan, jugador);
            else
                movimientoIA(rand); // IA

            // Comprobamos si hay ganador
            if (hayGanador(jugador)) {
                mostrarTablero();
                System.out.println("Gana " + jugador);
                break;
            }

            // Comprobamos si el tablero está lleno
            if (tableroLleno()) {
                mostrarTablero();
                System.out.println("Empate. Tablero lleno.");
                break;
            }

            // Cambiamos de turno
            jugador = (jugador == 'X') ? 'O' : 'X';
        }
    }

    // Función para mostrar el tablero
    private static void mostrarTablero() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++)
                System.out.print(tablero[i][j] + " ");
            System.out.println();
        }
    }

    // Función para mover jugador humano
    private static void movimientoJugador(Scanner scan, char jugador) {
        int f, c;
        do {
            System.out.print("Fila (0-2): ");
            f = scan.nextInt();
            System.out.print("Columna (0-2): ");
            c = scan.nextInt();
        } while (f < 0 || f > 2 || c < 0 || c > 2 || tablero[f][c] != '-');
        tablero[f][c] = jugador;
    }

    // Función para mover la IA
    private static void movimientoIA(Random rand) {
        int f, c;
        do {
            f = rand.nextInt(3);
            c = rand.nextInt(3);
        } while (tablero[f][c] != '-');
        tablero[f][c] = 'O';
    }

    // Función que comprueba si hay ganador
    private static boolean hayGanador(char j) {
        for (int i = 0; i < 3; i++) {
            if (tablero[i][0] == j && tablero[i][1] == j && tablero[i][2] == j) return true;
            if (tablero[0][i] == j && tablero[1][i] == j && tablero[2][i] == j) return true;
        }
        return (tablero[0][0] == j && tablero[1][1] == j && tablero[2][2] == j) ||
               (tablero[0][2] == j && tablero[1][1] == j && tablero[2][0] == j);
    }

    // Función que comprueba si el tablero está lleno
    private static boolean tableroLleno() {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (tablero[i][j] == '-') return false;
        return true;
    }
}
