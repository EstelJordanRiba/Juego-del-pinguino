package Java;
import java.util.Scanner;
import java.util.Random;

	public class F1 {
		
		    // Función que busca el número en el array
	public static int buscar(int numero, int[] array) {
		        	for (int i = 0; i < array.length; i++) {
		            if (array[i] == numero) {
		                return i; // devuelve la primera posición encontrada
		            }
		        }
		        		return -1; // no encontrado
		    }
	
	public static void main(String[] args) {
		   Scanner scan = new Scanner(System.in);
		   Random rand = new Random();

		        // Definir tamaño del array
		        int tamaño = 10; 
		        int[] array = new int[tamaño];

		        // Inicializar array con números aleatorios del 0 al 99
		        	for (int i = 0; i < tamaño; i++) {
		        			array[i] = rand.nextInt(100);
		        }

		        // Mostrar el array generado
		        		System.out.print("Array generado: ");
		        	for (int num : array) {
		            	System.out.print(num + " ");
		        }
		        		System.out.println();

		        // Pedir al usuario un número a buscar
		        		System.out.print("Introduce el número a buscar: ");
		        int numero = sc.nextInt();

		        // Llamar a la función buscar
		        int posicion = buscar(numero, array);

		        // Mostrar resultado
		        	if (posicion != -1) {
		        		System.out.println("El número " + numero + " se encuentra en la posición " + posicion);
		        	} else {
		        		System.out.println("El número " + numero + " no se encuentra en el array.");
		        }	
		    }
		}
