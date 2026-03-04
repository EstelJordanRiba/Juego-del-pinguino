package Java;

public class M3 {

	    public static void main(String[] args) {

	        int[][] matriz = {
	            {1, 2, 3, 4},
	            {5, 6, 7, 8},
	            {9, 10, 11, 12},
	            {13, 14, 15, 16}
	        };

	        int filas = 4;
	        int columnas = 4;

	        // Suma de cada fila
	        System.out.println("Suma de cada fila:");
	        for (int i = 0; i < filas; i++) {
	            int sumaFila = 0;
	            for (int j = 0; j < columnas; j++) {
	                sumaFila += matriz[i][j];
	            }
	            System.out.println("Fila " + i + ": " + sumaFila);
	        }

	        // Suma de cada columna
	        System.out.println("\nSuma de cada columna:");
	        for (int j = 0; j < columnas; j++) {
	            int sumaColumna = 0;
	            for (int i = 0; i < filas; i++) {
	                sumaColumna += matriz[i][j];
	            }
	            System.out.println("Columna " + j + ": " + sumaColumna);
	        }
	    }
	}


