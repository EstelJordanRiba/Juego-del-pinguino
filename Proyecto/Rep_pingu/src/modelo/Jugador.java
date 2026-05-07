package modelo;

/**
 * La palabra 'abstract' significa que esta clase es una idea general.
 * Define el comportamiento común para pingüinos y la foca.
 */
public abstract class Jugador {

    // Atributos privados: Encapsulamiento para proteger los datos
    private int posicion;        // Casilla actual (0-49)
    private String nombre;      // Nombre del jugador o IA
    private String color;       // Color representativo
    private int turnosPerdidos; // Contador de turnos que debe estar sin jugar
    private Inventario inventario; // El saco de objetos del jugador

    /**
     * Constructor: Inicializa los valores básicos al crear cualquier tipo de jugador.
     */
    public Jugador(String nombre, String color, int posicion) {
        this.posicion = posicion;
        this.nombre = nombre;
        this.color = color;
        this.turnosPerdidos = 0; // Por defecto empieza con todos sus turnos
        this.inventario = new Inventario();
    }

    // --- MÉTODOS DE ESTADO DE TURNOS ---

    /**
     * Incrementa la penalización del jugador.
     */
    public void perderTurno() {
        this.turnosPerdidos++;
    }

    /**
     * Indica si el jugador tiene prohibido mover en este turno.
     */
    public boolean debeSaltarTurno() {
        return turnosPerdidos > 0;
    }

    /**
     * Resta uno a la penalización cuando el jugador ya ha "cumplido" su espera.
     */
    public void consumirTurnoPerdido() {
        if (turnosPerdidos > 0) {
            turnosPerdidos--;
        }
    }

    // --- MÉTODOS DE MOVIMIENTO ---

    /**
     * Cambia la posición relativa del jugador y valida los límites del tablero.
     */
    public void moverPosicion(int pasos) {
        this.posicion += pasos;

        // Evita que el jugador retroceda por debajo de la salida
        if (this.posicion < 0) {
            this.posicion = 0;
        }

        // Evita que el jugador sobrepase la meta (casilla 49)
        if (this.posicion > 49) {
            this.posicion = 49;
        }
    }

    // --- GETTERS Y SETTERS (Acceso controlado a los datos) ---

    public int getPosicion() { return posicion; }
    public void setPosicion(int posicion) { this.posicion = posicion; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public int getTurnosPerdidos() { return turnosPerdidos; }
    public void setTurnosPerdidos(int turnosPerdidos) { this.turnosPerdidos = turnosPerdidos; }

    public Inventario getInventario() { return inventario; }
    public void setInventario(Inventario inventario) { this.inventario = inventario; }
}