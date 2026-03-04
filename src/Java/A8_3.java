package Java;
import java.util.Random;
import java.util.Scanner;

public class A8_3 {

    private static char[][] tablero = new char[3][3];

    public static void jugar() {
        Scanner scan = new Scanner(System.in);
        Random rand = new Random();

        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                tablero[i][j] = '-';

        System.out.println("1. Dos jugadores");
        System.out.println("2. Contra IA");
        int opcion = scan.nextInt();

        char jugador = 'X';

        while (true) {
            mostrar();

            if (jugador == 'X' || opcion == 1)
                moverJugador(scan, jugador);
            else
                moverIA(rand);

            if (ganador(jugador)) {
                mostrar();
                System.out.println("Gana " + jugador);
                break;
            }

            if (lleno()) {
                mostrar();
                System.out.println("Empate.");
                break;
            }

            jugador = (jugador == 'X') ? 'O' : 'X';
        }
    }

    private static void mostrar() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++)
                System.out.print(tablero[i][j] + " ");
            System.out.println();
        }
    }

    private static void moverJugador(Scanner scan, char j) {
        int f, c;
        do {
            System.out.print("Fila (0-2): ");
            f = scan.nextInt();
            System.out.print("Columna (0-2): ");
            c = scan.nextInt();
        } while (f < 0 || f > 2 || c < 0 || c > 2 || tablero[f][c] != '-');

        tablero[f][c] = j;
    }

    private static void moverIA(Random rand) {
        int f, c;
        do {
            f = rand.nextInt(3);
            c = rand.nextInt(3);
        } while (tablero[f][c] != '-');

        tablero[f][c] = 'O';
    }

    private static boolean ganador(char j) {
        for (int i = 0; i < 3; i++) {
            if (tablero[i][0] == j && tablero[i][1] == j && tablero[i][2] == j) return true;
            if (tablero[0][i] == j && tablero[1][i] == j && tablero[2][i] == j) return true;
        }
        return (tablero[0][0] == j && tablero[1][1] == j && tablero[2][2] == j) ||
               (tablero[0][2] == j && tablero[1][1] == j && tablero[2][0] == j);
    }

    private static boolean lleno() {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (tablero[i][j] == '-') return false;
        return true;
    }
}

