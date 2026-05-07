package modelo;

/**
 * Clase que representa la casilla del Trineo.
 * Hereda de Casilla para tener una ubicación física en el tablero.
 */
public class Trineo extends Casilla {

    /**
     * Constructor: Define en qué posición se encuentra este trineo.
     * @param posicion Índice de la casilla (0-49).
     */
    public Trineo(int posicion) {
        // Llama al constructor de Casilla (Padre) para registrar la posición.
        super(posicion);
    }

    /**
     * Método para ejecutar la acción al caer en la casilla.
     * Al igual que con el Oso, la lógica de búsqueda del próximo trineo
     * se gestiona desde el 'GestorTablero' o 'GestorPartida' para mayor orden.
     */
    @Override
    public void realizarAccion(Jugador jugador) {
        // La lógica de "desplazamiento" se ejecuta en el controlador
        // al detectar que la casilla es de tipo Trineo.
    }
}