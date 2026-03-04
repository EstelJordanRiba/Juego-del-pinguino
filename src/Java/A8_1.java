package Java;
import java.util.Random;
import java.util.Scanner;

public class A8_1{

    public static void jugar() {
        Scanner scan = new Scanner(System.in);
        Random rand = new Random();

        int secreto = rand.nextInt(25);
        System.out.print("¿Cuántos intentos quieres? ");
        int intentos = scan.nextInt();

        for (int i = 1; i <= intentos; i++) {
            System.out.print("Intento " + i + ": ");
            int num = scan.nextInt();

            if (num == secreto) {
                System.out.println("¡Correcto! Has ganado.");
                return;
            } else if (num < secreto) {
                System.out.println("El número es mayor.");
            } else {
                System.out.println("El número es menor.");
            }
        }
        System.out.println("Has perdido. El número era: " + secreto);
    }
}


