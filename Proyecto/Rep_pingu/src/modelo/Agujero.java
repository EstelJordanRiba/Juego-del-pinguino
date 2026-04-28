package modelo; // Indica que esta clase pertenece al paquete "modelo"

// Clase Agujero que hereda de Casilla (es un tipo específico de casilla)
public class Agujero extends Casilla {
   
    // Constructor: recibe la posición de la casilla en el tablero
    public Agujero(int posicion) {
        super(posicion); // Llama al constructor de la clase padre (Casilla)
    }

    // Método sobrescrito de la clase Casilla
    // Se ejecuta cuando un jugador cae en esta casilla
    @Override
    public void realizarAccion(Jugador jugador) {
        
        // Aquí deberías definir qué ocurre cuando el jugador cae en un agujero
        // Por ejemplo:
        // - Perder turno
        // - Volver al inicio
        // - Retroceder casillas
        // Ahora mismo está vacío, así que no hace nada
        
    }
}