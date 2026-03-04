package Java;
import java.util.Random;
public class A2 {

	    public static void main(String[] args) {
	        Random rand = new Random();
	        int[] numeros = new int[20];
	        int contadorPrimos = 0;

	        for (int i = 0; i < numeros.length; i++) {
	            numeros[i] = rand.nextInt(100);
	        }

	        for (int n : numeros) {
	            if (esPrimo(n)) {
	                contadorPrimos++;
	            }
	        }

	        int[] primos = new int[contadorPrimos];
	        int index = 0;

	        for (int n : numeros) {
	            if (esPrimo(n)) {
	                primos[index++] = n;
	            }
	        }

	        System.out.println("Array original:");
	        for (int n : numeros) {
	            System.out.print(n + " ");
	        }

	        System.out.println("\nArray de números primos:");
	        for (int p : primos) {
	            System.out.print(p + " ");
	        }
	    }

	    public static boolean esPrimo(int n) {
	        if (n < 2) return false;
	        for (int i = 2; i <= Math.sqrt(n); i++) {
	            if (n % i == 0) return false;
	        }
	        return true;
	    }
	}


