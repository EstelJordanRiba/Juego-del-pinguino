package Java;
import java.util.Scanner;
public class A5 {

	    public static void main(String[] args) {
	        Scanner sc = new Scanner(System.in);
	        int[] numeros = new int[15];

	        for (int i = 0; i < numeros.length; i++) {
	            System.out.print("Introduce el número " + (i + 1) + ": ");
	            numeros[i] = sc.nextInt();
	        }

	        System.out.print("Introduce el número de referencia: ");
	        int ref = sc.nextInt();

	        int mayores = 0, menores = 0, iguales = 0;

	        for (int n : numeros) {
	            if (n > ref) mayores++;
	            else if (n < ref) menores++;
	            else iguales++;
	        }

	        System.out.println("Mayores: " + mayores);
	        System.out.println("Menores: " + menores);
	        System.out.println("Iguales: " + iguales);
	    }
	}
