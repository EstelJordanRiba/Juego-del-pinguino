package modelo;

/**
 * Esta clase hereda de Casilla. 
 * Se utiliza para rellenar los huecos del tablero donde no hay osos, trineos ni eventos.
 */
public class Normal extends Casilla {

    /**
     * Constructor: Crea una casilla normal en una posición específica.
     * @param posicion El número de casilla (índice) en el tablero.
     */
    public Normal(int posicion) {
        // 'super' llama al constructor de la clase padre (Casilla)
        super(posicion);
    }

    /**
     * Implementación obligatoria del método abstracto de la clase padre.
     * En este caso, al ser una casilla normal, el cuerpo está vacío 
     * porque no le ocurre nada al jugador que cae aquí.
     */
    @Override
    public void realizarAccion(Jugador jugador) {
        // No hay efecto: el jugador simplemente permanece en la posición.
    }
}