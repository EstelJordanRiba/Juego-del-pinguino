
package Java;
public class ExamenE1 {
	// Declaración de la clase principal llamada ExamenE1 y del paquete
    /*
     * Método que calcula la media de las notas de un alumno
     * @param notas Array de enteros que contiene las notas del alumno
     * @return Devuelve la media de las notas o 0 si el array está vacío o es null
     */
    public static double calcularMedia(int[] notas) {

        // Comprobamos si el array es nulo o no tiene elementos
        if (notas == null || notas.length == 0) {
            return 0; // Si no hay notas, la media es 0
        }

        // Variable para almacenar la suma de todas las notas
        int suma = 0;

        // Bucle for-each que recorre cada nota del array
        for (int nota : notas) {
            suma += nota; // Se van sumando las notas una a una
        }

        // Se devuelve la media (suma total dividido entre el número de notas)
        return (double) suma / notas.length;
    }

    /*
     * Método que indica si el alumno está aprobado
     * @param media Media de las notas del alumno
     * @return true si la media es mayor o igual a 5, false si es menor
     */
    public static boolean estaAprobado(double media) {

        // Devuelve true si la media es 5 o más, si no devuelve false
        return media >= 5;
    }

    /*
     * Método principal (main)
     * Es el punto de entrada del programa
     */
    public static void main(String[] args) {

        // Creamos un array con 5 notas del alumno
        int[] notas = {10, 10, 5, 5, 6};

        // Llamamos al método calcularMedia para obtener la media
        double media = calcularMedia(notas);

        // Llamamos al método estaAprobado para saber si ha aprobado
        boolean aprobado = estaAprobado(media);

        // Mostramos por pantalla la media de las notas
        System.out.println("Media de todos los examenes: " + media);

        // Mostramos por pantalla si el alumno está aprobado o no
        System.out.println("El alumno ha sido aprobado: " + aprobado);
    }
}
	

