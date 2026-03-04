package Java;
import java.util.Scanner;
public class M4 {


	    public static void main(String[] args) {
	        Scanner sc = new Scanner(System.in);

	        char[][] sala = new char[6][6];

	        // Inicializar la matriz con '-'
	        for (int i = 0; i < 6; i++) {
	            for (int j = 0; j < 6; j++) {
	                sala[i][j] = '-';
	            }
	        }

	        // Pedir 3 asientos ocupados
	        System.out.println("Introduce 3 asientos ocupados (fila y columna entre 0 y 5):");

	        for (int k = 0; k < 3; k++) {
	            System.out.println("Asiento " + (k + 1) + ":");
	            System.out.print("Fila: ");
	            int fila = sc.nextInt();
	            System.out.print("Columna: ");
	            int columna = sc.nextInt();

	            // Validar rango
	            if (fila >= 0 && fila < 6 && columna >= 0 && columna < 6) {
	                sala[fila][columna] = 'O';
	            } else {
	                System.out.println("Coordenadas fuera de rango. No se marcará.");
	            }
	        }

	        // Imprimir matriz
	        System.out.println("\nRepresentación de la sala:");
	        for (int i = 0; i < 6; i++) {
	            for (int j = 0; j < 6; j++) {
	                System.out.print(sala[i][j] + " ");
	            }
	            System.out.println();
	        }

	        sc.close();
	    }
	}

