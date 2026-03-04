package Java;

import java.util.Scanner;

public class A8_1__2 {
	
	    public static void main(String[] args) {
	    	
	        Scanner scan = new Scanner(System.in);
	        int opcion;

	        do {
	            System.out.println("\n menu de juegos:");
	            System.out.println("1_ Adivinar número");
	            System.out.println("2_ Piedra, papel o tijeras");
	            System.out.println("3_ Tres en raya");
	            System.out.println("4_ Salir");
	            System.out.print("Elige una opción: ");
	            opcion = scan.nextInt();

	            switch (opcion) {
	                case 1:
	                	A8_1.jugar();
	                    break;
	                case 2:
	                	A8_2.jugar();
	                    break;
	                case 3:
	                	A8_3.jugar();
	                    break;
	                case 4:
	                    System.out.println("Salir");
	                    break;
	                default:
	                    System.out.println("Opción no válida. Decide un numero del menu");
	            }
	        } while (opcion != 4);
	    }
	}


