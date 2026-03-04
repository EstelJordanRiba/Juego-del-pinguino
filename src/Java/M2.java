package Java;
import java.util.Scanner;
import java.util.Random;
public class M2 {
	

	
	    public static void main(String[] args) {
	        Scanner sc = new Scanner(System.in);
	        Random rand = new Random();

	        // Pedir número de columnas
	        System.out.print("Introduce el número de columnas: ");
	        int n = sc.nextInt();

	        // Declarar matriz de 5 filas y n columnas
	        int[][] matriz = new int[5][n];

	        // Rellenar con números aleatorios entre 0 y 10
	        for (int i = 0; i < 5; i++) {
	            for (int j = 0; j < n; j++) {
	                matriz[i][j] = rand.nextInt(11); // (0–10)
	            }
	        }

	        // Imprimir matriz
	        System.out.println("\nMatriz generada:");
	        for (int i = 0; i < 5; i++) {
	            for (int j = 0; j < n; j++) {
	                System.out.print(matriz[i][j] + " ");
	            }
	            System.out.println();
	        }

	      
	    }
	}

