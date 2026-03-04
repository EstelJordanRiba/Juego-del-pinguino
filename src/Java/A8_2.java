package Java;
import java.util.Random;
import java.util.Scanner;

public class A8_2 {

    public static void jugar() {
        Scanner scan = new Scanner(System.in);
        Random rand = new Random();

        System.out.println("1. Dos jugadores");
        System.out.println("2. Contra IA");
        int opcion = scan.nextInt();
        scan.nextLine();

        if (opcion == 1) {
            System.out.print("Jugador 1: ");
            String j1 = scan.nextLine();
            System.out.print("Jugador 2: ");
            String j2 = scan.nextLine();
            comprobar(j1, j2);
        } else {
            String[] opciones = {"piedra", "papel", "tisores"};
            System.out.print("Jugador: ");
            String jugador = scan.nextLine();
            String ia = opciones[rand.nextInt(3)];

            System.out.println("IA eligió: " + ia);
            comprobar(jugador, ia);
        }
    }

    private static void comprobar(String j1, String j2) {
        if (!valido(j1) || !valido(j2)) {
            System.out.println("Movimiento no válido.");
            return;
        }

        if (j1.equals(j2))
            System.out.println("Empate");
        else if ((j1.equals("piedra") && j2.equals("tisores")) ||
                 (j1.equals("papel") && j2.equals("piedra")) ||
                 (j1.equals("tisores") && j2.equals("papel")))
            System.out.println("Gana Jugador 1");
        else
            System.out.println("Gana Jugador 2");
    }

    private static boolean valido(String m) {
        return m.equals("piedra") || m.equals("papel") || m.equals("tisores");
    }
}
