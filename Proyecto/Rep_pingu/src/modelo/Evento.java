package modelo; // Indica que pertenece al paquete "modelo"

// Clase Evento que hereda de Casilla (es un tipo especial de casilla)
public class Evento extends Casilla {

    // Constructor: recibe la posición de la casilla
    public Evento(int posicion) {
        super(posicion); // Llama al constructor de la clase padre (Casilla)
    }

    // Método sobrescrito que define qué ocurre cuando un jugador cae en esta casilla
    @Override
    public void realizarAccion(Jugador jugador) {
        
        // Aquí se debería implementar la lógica del evento
        // Por ejemplo:
        // - Ganar o perder puntos
        // - Avanzar o retroceder casillas
        // - Recibir un objeto
        // - Activar algún efecto especial del juego
        
        // Ahora mismo está vacío (pendiente de implementar)
        
    }
}