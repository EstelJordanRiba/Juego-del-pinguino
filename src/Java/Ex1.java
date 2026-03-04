package Java;
import java.util.Scanner; //Declaramos los paquetes que utilizaremos es decir las librerias

public class Ex1 {
/* Para este ejercicio pido una frase al usuario usando la libreria scanner. 
 * Uso un bucle para tal de poder contar los caracteres que no son espacios. 
 * Para contar las palabras uso un bucle tmb pero con una negacion con el signo "!" separando por espacios.
 * Para mostrar la frase al revés uso un bucle que recorre todas las letras de el último carácter hasta el primero.
 */
	     
	    public static void main(String[] args) {

	        Scanner scan_n = new Scanner(System.in);
	        
	        //aqui activamos el codigo y creamos nuestro scaner

	        System.out.println("Introduce una frase:"); //imprimos en la consola un mensaje
	        String frase = scan_n.nextLine(); //aqui basicamente scaneamos y retenemos los datos insertados por la persona

	        // Contar caracteres sin espacios
	        int contadorCaracteres = 0; //creamos la variable contador
	        for (int i = 0; i < frase.length(); i++) { //pasa por todos los caracteres y los cuenta
	            if (frase.charAt(i) != ' ') { //Aqui negamos q se tenga q contar los espacios, por lo tanto no los contara, por eso utilizamos un for + if
	                contadorCaracteres++; //es un contador de los caracteres
	            }
	        }

	     // Contar palabras 
	        int contadorPalabras = 0; //aqui es el contador de las palabras
	        boolean dentroPalabra = false; //Utilizamos la variable boolean

	        for (int i = 0; i < frase.length(); i++) { //pasamos por todas las palabras
	            if (frase.charAt(i) != ' ' && !dentroPalabra) {
	                contadorPalabras++;
	                dentroPalabra = true;
	            } else if (frase.charAt(i) == ' ') {
	                dentroPalabra = false;
	              //cuenta todas las palabras pero igual q antes sin contar los espacios por eso utilizamos el boolean para poder generar dos casos de posbilidades
	            }
	        }

	        // Frase al revés
	        String fraseReves = ""; 
	        for (int i = frase.length() - 1; i >= 0; i--) {//esta es la formula para tal de poder girar la palabra
	            fraseReves += frase.charAt(i); //aqui con la variable de la frasealreves, hacemos q nos genere esa frase inical a la inversa por asi decirlo
	        }

	        System.out.println("Caracteres sin espacios: " + contadorCaracteres);
	        System.out.println("Numero de palabras: " + contadorPalabras);
	        System.out.println("Frase al reves: " + fraseReves);
	        	
	        //imprimimos los diferentes resultados
	        
	    }
	}