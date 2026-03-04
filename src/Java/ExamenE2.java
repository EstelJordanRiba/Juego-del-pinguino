// Paquete donde se encuentra la clase
package Java;

// Importamos las clases necesarias para leer/escribir archivos y usar Scanner
import java.io.*;
import java.util.Scanner;

// Declaración de la clase ExamenE2
public class ExamenE2 {

    /*
     * Método principal (main)
     * Es el punto de entrada del programa
     */
    public static void main(String[] args) {

        // Creamos un Scanner para leer datos introducidos por el usuario
        Scanner scan_n = new Scanner(System.in);

        // PARTE 1
     
        System.out.println("guardar personas\n");

        try {
            // Creamos el fichero datos.txt para escribir en él
            FileWriter fw = new FileWriter("datos.txt");

            // Bucle for para pedir los datos de 3 personas
            // La variable i actúa como contador
            for (int i = 1; i <= 3; i++) {

                // Mostramos qué persona estamos introduciendo
                System.out.println("Persona " + i + ":");

                // Pedimos el nombre de la persona
                System.out.print("  Nombre: ");
                String nombre = scan_n.nextLine();

                // Pedimos la edad de la persona
                System.out.print("  Edad: ");
                int edad = scan_n.nextInt();
                scan_n.nextLine(); // Limpiamos el salto de línea del Scanner

                // Escribimos el nombre y la edad en el fichero separados por una coma
                fw.write(nombre + "," + edad + "\n");
            }

            // Cerramos el fichero para guardar correctamente los datos
            fw.close();

            // Mensaje indicando que los datos se han guardado bien
            System.out.println("\nDatos correctamente guardados en datos.txt\n");

        } catch (IOException e) {
            // Mensaje de error si ocurre algún problema al guardar
            System.out.println("Error al guardar: " + e.getMessage());
        }

        
        // PARTE 2
        
        System.out.println("Nombre y edad de todas las personas:\n");

        try {
            // Abrimos el fichero para leer su contenido
            FileReader fr = new FileReader("datos.txt");
            BufferedReader br = new BufferedReader(fr);

            String linea;

            // Leemos el fichero línea por línea
            while ((linea = br.readLine()) != null) {
                // Mostramos cada línea por pantalla
                System.out.println(linea);
            }

            // Cerramos el fichero
            br.close();

        } catch (IOException e) {
            // Mensaje de error si ocurre algún problema al leer
            System.out.println("Error al leer: " + e.getMessage());
        }

        
        // PARTE 3
        
        System.out.println("\nPersonas mayores de edad:\n");

        try {
            // Abrimos de nuevo el fichero para volver a leerlo
            FileReader fr = new FileReader("datos.txt");
            BufferedReader br = new BufferedReader(fr);

            String linea;

            // Leemos el fichero línea por línea
            while ((linea = br.readLine()) != null) {

                // Separamos el nombre y la edad usando la coma
                String[] partes = linea.split(",");
                String nombre = partes[0];
                int edad = Integer.parseInt(partes[1]);

                // Comprobamos si la persona es mayor de edad
                if (edad >= 18) {
                    // Mostramos solo el nombre de las personas mayores de edad
                    System.out.println(nombre);
                }
            }

            // Cerramos el fichero
            br.close();

        } catch (IOException e) {
            // Mensaje de error si ocurre algún problema al leer
            System.out.println("Error al leer: " + e.getMessage());
        }
    }
}

