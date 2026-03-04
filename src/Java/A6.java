package Java;
import java.util.Scanner;
public class A6 {
	
	    public static void main(String[] args) {
	        Scanner sc = new Scanner(System.in);
	        int[] array = new int[10];
	        int[] resultado = new int[10];

	        for (int i = 0; i < array.length; i++) {
	            System.out.print("Introduce el número " + (i + 1) + ": ");
	            array[i] = sc.nextInt();
	        }

	        System.out.print("Introduce cuántas posiciones desplazar a la derecha: ");
	        int desplazamiento = sc.nextInt();
	        desplazamiento = desplazamiento % array.length;

	        for (int i = 0; i < array.length; i++) {
	            int nuevaPos = (i + desplazamiento) % array.length;
	            resultado[nuevaPos] = array[i];
	        }

	        System.out.println("Array original:");
	        for (int n : array) {
	            System.out.print(n + " ");
	        }

	        System.out.println("\nArray desplazado:");
	        for (int n : resultado) {
	            System.out.print(n + " ");
	        }
	    }
	}
