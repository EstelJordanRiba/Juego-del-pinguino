package modelo;

/**
 * Clase que representa la casilla del Oso.
 * Hereda de Casilla, lo que le permite tener una posición y ser parte del tablero.
 */
public class Oso extends Casilla {

    /**
     * Constructor: Ubica la casilla del oso en el tablero.
     * @param posicion Índice de la casilla (0-49).
     */
    public Oso(int posicion) {
        // Llama al constructor de Casilla para guardar la ubicación
        super(posicion);
    }

    /**
     * Método para ejecutar la lógica al caer en la casilla.
     * * Nota: En tu arquitectura, la lógica compleja (daño, retroceso, etc.) 
     * se ha movido al 'GestorTablero' para mantener el modelo limpio.
     */
    @Override
    public void realizarAccion(Jugador jugador) {
        // El comportamiento específico se gestiona desde el controlador (GestorTablero)
        // detectando si la casilla 'instanceof Oso'.
    }
}