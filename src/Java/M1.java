package Java;
import java.util.Scanner;
public class M1 {
	

	    public static void main(String[] args) {
	        Scanner sc = new Scanner(System.in);

	        int[][] matriz = new int[3][3];
	        int suma = 0;

	        // Rellenar la matriz
	        System.out.println("Introduce los elementos de la matriz 3x3:");
	        for (int i = 0; i < 3; i++) {
	            for (int j = 0; j < 3; j++) {
	                System.out.print("Elemento [" + i + "][" + j + "]: ");
	                matriz[i][j] = sc.nextInt();
	                suma += matriz[i][j];  // Acumular suma
	            }
	        }

	        // Mostrar la suma
	        System.out.println("La suma de todos los elementos es: " + suma);

	       
	}

}
