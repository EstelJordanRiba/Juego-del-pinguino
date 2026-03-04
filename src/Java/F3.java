package Java;
import java.util.Random;
public class F3 {

	    public static void main(String[] args) {

	        int[] array = new int[10]; // tamaño del array
	        Random rand = new Random();

	        // Rellenar con números aleatorios entre 0 y 99
	        for (int i = 0; i < array.length; i++) {
	            array[i] = rand.nextInt(100);
	        }

	        // Imprimir el array
	        imprimirArray(array);

	        // Comprobar si es creciente
	        if (esCreciente(array)) {
	            System.out.println("La secuencia es creciente.");
	        } else {
	            System.out.println("La secuencia NO es creciente.");
	        }
	    }

	    // Función que comprueba si el array es creciente
	    static boolean esCreciente(int[] array) {
	        for (int i = 0; i < array.length - 1; i++) {
	            if (array[i] > array[i + 1]) {
	                return false; // Si encuentra un elemento mayor que el siguiente, no es creciente
	            }
	        }
	        return true; // Si nunca se incumple la condición, es creciente
	    }

	    // Función que imprime el contenido del array
	    static void imprimirArray(int[] array) {
	        System.out.print("Array: ");
	        for (int num : array) {
	            System.out.print(num + " ");
	        }
	        System.out.println();
	    }
	}


