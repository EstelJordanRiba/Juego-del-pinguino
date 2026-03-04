package Java;
import java.util.Scanner;
public class A3 {

	    public static void main(String[] args) {
	        Scanner sc = new Scanner(System.in);
	        int[] numeros = new int[10];

	        for (int i = 0; i < numeros.length; i++) {
	            System.out.print("Introduce el número " + (i + 1) + ": ");
	            numeros[i] = sc.nextInt();
	        }

	        System.out.print("Introduce un índice (0-9): ");
	        int indice = sc.nextInt();

	        if (indice >= 0 && indice < numeros.length) {
	            System.out.println("Índice: " + indice + ", Valor: " + numeros[indice]);
	        } else {
	            System.out.println("Error: índice fuera de rango.");
	        }
	    }
	}

