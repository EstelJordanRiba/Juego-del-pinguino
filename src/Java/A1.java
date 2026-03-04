package Java;
import java.util.Scanner;

public class A1 {


	    public static void main(String[] args) {
	        Scanner sc = new Scanner(System.in);
	        int[] numeros = new int[10];

	        for (int i = 0; i < numeros.length; i++) {
	            System.out.print("Introduce el número " + (i + 1) + ": ");
	            numeros[i] = sc.nextInt();
	        }

	        int max = numeros[0];
	        int min = numeros[0];
	        int posMax = 0;
	        int posMin = 0;

	        for (int i = 1; i < numeros.length; i++) {
	            if (numeros[i] > max) {
	                max = numeros[i];
	                posMax = i;
	            }
	            if (numeros[i] < min) {
	                min = numeros[i];
	                posMin = i;
	            }
	        }

	        System.out.println("Valor máximo: " + max + " en la posición " + posMax);
	        System.out.println("Valor mínimo: " + min + " en la posición " + posMin);
	    }
	}

