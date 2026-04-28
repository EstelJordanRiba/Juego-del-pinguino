package modelo; // Indica que esta clase pertenece al paquete "modelo"

// Clase abstracta: no se puede crear directamente (no puedes hacer new Casilla())
// Sirve como base para otros tipos de casillas (como Agujero, Meta, etc.)
public abstract class Casilla {

    // Atributo protegido: posición de la casilla en el tablero
    // "protected" permite que las clases hijas accedan directamente
    protected int posicion;

    // Constructor: inicializa la posición de la casilla
    public Casilla(int posicion) {
        this.posicion = posicion;
    }

    // Getter: devuelve la posición de la casilla
    public int getPosicion() {
        return posicion;
    }

    // Setter: permite modificar la posición
    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    // Método abstracto: NO tiene implementación aquí
    // Obliga a las clases hijas a definir qué ocurre cuando un jugador cae en la casilla
    public abstract void realizarAccion(Jugador jugador);
}