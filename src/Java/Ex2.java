package Java;
import java.util.Random;
//utilizamos la libreria random q sirve para generar x cosa aleatori, en este caso numeros.

public class Ex2 {


	    /*
	     * En este ejercicio creo una matriz de 5*5.
	     * Uso la libreria Random para generar números aleatorios entre 1 y 9
	     * y  luego poder rellenar la matriz.
	     * Recorro la matriz con dos bucles "for" para mostrarla por pantalla.
	     * Sumo la diagonal secundaria comprobando que fila + columna es 4.
	     * Busco el número mayor recorriendo toda la matriz y guardo su posición.
	     * Para girar por asi decirlo la matriz creo otra matriz cambiando filas por columnas.
	     */

	    public static void main(String[] args) {
	    	//proceso inical para tal de procerder con el programa

	        int[][] matriz = new int[5][5]; //Aqui creariamos la variable matriz q seria nuestra pabla q sera 5*5
	        Random random = new Random(); //Tmb generamos el random.

	        // Rellenar la matriz con valores aleatorios entre 1 y 9
	        for (int fila = 0; fila < 5; fila++) { //hace q pase por cada fila la computadora para poner un numero "x"
	            for (int columna = 0; columna < 5; columna++) { //hace q pase por cada columna la computadora para poner un numero "x"
	                matriz[fila][columna] = random.nextInt(9) + 1;  //Rellena toda la matriz
	            }
	        }

	        // Mostrar la matriz
	        System.out.println("Matriz original:"); //Aqui imprime el mensaje entre:" ", a la consola 
	        for (int fila = 0; fila < 5; fila++) {
	            for (int columna = 0; columna < 5; columna++) {
	                System.out.print(matriz[fila][columna] + " "); //Pasa por fila y columna, y pone la casilla vacia
	            }
	            System.out.println(); //Imprime todo lo anterior
	        }

	        // a) Suma de la diagonal secundaria
	        int sumaDiagonalSecundaria = 0;
	        for (int fila = 0; fila < 5; fila++) {
	            for (int columna = 0; columna < 5; columna++) {
	                if (fila + columna == 4) {
	                    sumaDiagonalSecundaria += matriz[fila][columna];
	                }
	            }
	        }
	        System.out.println("Suma de la diagonal secundaria: " + sumaDiagonalSecundaria); //Imprimimos los resultados de la suma de la diagonal

	        // b) Número mayor y su posición
	        int mayor = matriz[0][0];
	        int filaMayor = 0;
	        int columnaMayor = 0;

	        for (int fila = 0; fila < 5; fila++) {
	            for (int columna = 0; columna < 5; columna++) {
	                if (matriz[fila][columna] > mayor) {
	                    mayor = matriz[fila][columna];
	                    filaMayor = fila;
	                    columnaMayor = columna;
	                }
	            }
	        }

	        System.out.println("Numero mayor: " + mayor);
	        System.out.println("Posicion: fila " + filaMayor + ", columna " + columnaMayor); //Aqui imprimimos los resultados anteriores

	        // c) Transponer la matriz
	        int[][] matrizTranspuesta = new int[5][5]; //creamos una nueva variable

	        for (int fila = 0; fila < 5; fila++) {
	            for (int columna = 0; columna < 5; columna++) { //pasamos por columnas y filas
	                matrizTranspuesta[columna][fila] = matriz[fila][columna]; //Giramos los numeros de las columnas con las filas y las filas como columnas
	            }
	        }

	        System.out.println("Matriz transpuesta:"); 
	        for (int fila = 0; fila < 5; fila++) {
	            for (int columna = 0; columna < 5; columna++) {
	                System.out.print(matrizTranspuesta[fila][columna] + " ");
	            }
	            System.out.println();
	            //Aqui enviamos la matriz transpuesta
	        }
	    }
	}

