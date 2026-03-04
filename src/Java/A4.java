package Java;
import java.util.Scanner;
import java.util.Random;
public class A4 {

	    public static void main(String[] args) {
	        Scanner sc = new Scanner(System.in);
	        Random rand = new Random();

	        System.out.print("Introduce el tamaño del array: ");
	        int tamaño = sc.nextInt();

	        int[] numeros = new int[tamaño];
	        int suma = 0;

	        for (int i = 0; i < tamaño; i++) {
	            numeros[i] = rand.nextInt(10);
	            suma += numeros[i];
	        }

	        for (int i = 0; i < tamaño; i++) {
	            System.out.println("Posición " + i + ": " + numeros[i]);
	        }

	        System.out.println("Suma total: " + suma);
	    }
	}

