package Java;
import java.util.Scanner;
public class F5 {
	
	public class MenuArray {
	    public static void main(String[] args) {
	        Scanner scan = new Scanner(System.in);

	        int[] array = null; 
	        int opcion = 0;

	        // Bucle mientras la opción no sea 5
	        while (opcion != 5) {

	            System.out.println("MENÚ ");
	            System.out.println("1. Inicializar array");
	            System.out.println("2. Encontrar el número más pequeño del array");
	            System.out.println("3. Encontrar el número más grande del array");
	            System.out.println("4. Encontrar cuántos números positivos hay");
	            System.out.println("5. Salir");
	            System.out.print("Elige una opción: ");
	            opcion = sc.nextInt();

	            switch (opcion) {
	                case 1:
	                    System.out.print("¿De qué tamaño quieres el array? ");
	                    int tamaño = sc.nextInt();
	                    array = new int[tamaño];

	                    System.out.println("Introduce los valores:");
	                    for (int i = 0; i < tamaño; i++) {
	                        System.out.print("Elemento " + (i + 1) + ": ");
	                        array[i] = sc.nextInt();
	                    }
	                    System.out.println("Array inicializado correctamente.\n");
	                    break;

	                case 2:
	                    if (array == null) {
	                        System.out.println("Primero debes inicializar el array.\n");
	                    } else {
	                        int menor = array[0];
	                        for (int num : array) {
	                            if (num < menor) {
	                                menor = num;
	                            }
	                        }
	                        System.out.println("El número más pequeño es: " + menor + "\n");
	                    }
	                    break;

	                case 3:
	                    if (array == null) {
	                        System.out.println("Primero debes inicializar el array.\n");
	                    } else {
	                        int mayor = array[0];
	                        for (int num : array) {
	                            if (num > mayor) {
	                                mayor = num;
	                            }
	                        }
	                        System.out.println("El número más grande es: " + mayor + "\n");
	                    }
	                    break;

	                case 4:
	                    if (array == null) {
	                        System.out.println("Primero debes inicializar el array.\n");
	                    } else {
	                        int positivos = 0;
	                        for (int num : array) {
	                            if (num > 0) {
	                                positivos++;
	                            }
	                        }
	                        System.out.println("Cantidad de números positivos: " + positivos + "\n");
	                    }
	                    break;

	                case 5:
	                    System.out.println("Saliendo del programa...");
	                    break;

	                default:
	                    System.out.println("Opción no válida. Intenta nuevamente.\n");
	            }
	        }
	    }
	}
}



