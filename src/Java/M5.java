package Java;
import java.util.Random;
public class M5 {
	

	
	    public static void main(String[] args) {
	        Random rand = new Random();

	        int[][] matriz = new int[5][5];

	        // Rellenar la matriz con valores aleatorios
	        for (int i = 0; i < 5; i++) {
	            for (int j = 0; j < 5; j++) {
	                matriz[i][j] = rand.nextInt(100) + 1; // 1 a 100
	            }
	        }

	        // Imprimir la matriz
	        System.out.println("Matriz generada:");
	        for (int i = 0; i < 5; i++) {
	            for (int j = 0; j < 5; j++) {
	                System.out.printf("%3d ", matriz[i][j]);
	            }
	            System.out.println();
	        }

	        // Buscar el mayor y el menor
	        int mayor = matriz[0][0];
	        int menor = matriz[0][0];

	        int filaMayor = 0, colMayor = 0;
	        int filaMenor = 0, colMenor = 0;

	        for (int i = 0; i < 5; i++) {
	            for (int j = 0; j < 5; j++) {
	                if (matriz[i][j] > mayor) {
	                    mayor = matriz[i][j];
	                    filaMayor = i;
	                    colMayor = j;
	                }
	                if (matriz[i][j] < menor) {
	                    menor = matriz[i][j];
	                    filaMenor = i;
	                    colMenor = j;
	                }
	            }
	        }

	        // Mostrar resultados
	        System.out.println("\nMayor valor: " + mayor +
	                           " (fila " + filaMayor + ", columna " + colMayor + ")");
	        System.out.println("Menor valor: " + menor +
	                           " (fila " + filaMenor + ", columna " + colMenor + ")");
	    }
	}


