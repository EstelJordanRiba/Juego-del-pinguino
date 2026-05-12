package modelo;

import java.util.ArrayList;

public class Partida {

    // --- ATRIBUTOS (El estado de la partida) ---
    private Tablero tablero;            // El tablero con sus 50 casillas
    private ArrayList<Jugador> jugadores; // Lista de humanos e IA
    private int turnos;                 // Contador total de turnos transcurridos
    private int jugadorActual;          // Índice (0, 1, 2...) del jugador que debe mover
    private boolean finalizada;         // Indica si alguien ya ha llegado a la meta
    private Jugador ganador;            // Referencia al jugador que ha ganado
    private String ultimoEvento;        // Texto descriptivo de la última acción (para la interfaz)

    /**
     * Constructor: Inicializa una partida desde cero.
     */
    public Partida() {
        this.tablero = new Tablero();
        this.jugadores = new ArrayList<Jugador>();
        this.turnos = 0;
        this.jugadorActual = 0;
        this.finalizada = false;
        this.ganador = null;
        this.ultimoEvento = "La partida ha comenzado.";
        
        // Al crear la partida, se rellena el tablero con casillas especiales
        this.tablero.generarCasillasAleatorias();
    }

    // --- LÓGICA DE FLUJO ---

    /**
     * Devuelve el objeto Jugador al que le toca tirar.
     * Incluye validaciones de seguridad por si la lista está vacía o el índice es erróneo.
     */
    public Jugador getJugadorActual() {
        if (jugadores == null || jugadores.isEmpty()) {
            return null;
        }

        if (jugadorActual < 0 || jugadorActual >= jugadores.size()) {
            jugadorActual = 0; // Reset de seguridad
        }

        return jugadores.get(jugadorActual);
    }

    /**
     * Pasa el turno al siguiente jugador de la lista.
     * Usa el operador módulo (%) para que, al llegar al último, vuelva al primero (0).
     */
    public void siguienteTurno() {
        if (!jugadores.isEmpty()) {
            // Ejemplo: si hay 3 jugadores, el índice hará 0 -> 1 -> 2 -> 0...
            jugadorActual = (jugadorActual + 1) % jugadores.size();
            turnos++;
        }
    }

    /**
     * Revisa si algún jugador ha alcanzado o superado la casilla 49 (la meta).
     * Si lo encuentra, marca la partida como terminada.
     */
    public void comprobarGanador() {
        for (Jugador jugador : jugadores) {
            if (jugador.getPosicion() >= 49) {
                finalizada = true;
                ganador = jugador;
                ultimoEvento = jugador.getNombre() + " ha ganado la partida.";
                break; // Paramos de buscar, ya hay un ganador
            }
        }
    }

    // --- GETTERS Y SETTERS (Acceso a los datos) ---

 // Devuelve el tablero
    public Tablero getTablero() { return tablero; }
    // Cambia el valor del tablero
    public void setTablero(Tablero tablero) { this.tablero = tablero; }
    // Devuelve la lista de jugadores
    public ArrayList<Jugador> getJugadores() { return jugadores; }
    // Cambia la lista de jugadores
    public void setJugadores(ArrayList<Jugador> jugadores) { this.jugadores = jugadores; }
    // Devuelve el número de turnos
    public int getTurnos() { return turnos; }
    // Cambia el número de turnos
    public void setTurnos(int turnos) { this.turnos = turnos; }
    // Devuelve el índice del jugador actual
    public int getJugadorActualIndice() { return jugadorActual; }
    // Cambia el índice del jugador actual
    public void setJugadorActualIndice(int jugadorActual) { this.jugadorActual = jugadorActual; }
    // Devuelve si la partida ha finalizado
    public boolean isFinalizada() { return finalizada; }
    // Cambia el estado de finalizada
    public void setFinalizada(boolean finalizada) { this.finalizada = finalizada; }
    // Devuelve el ganador
    public Jugador getGanador() { return ganador; }
    // Cambia el ganador
    public void setGanador(Jugador ganador) { this.ganador = ganador; }
    // Devuelve el último evento
    public String getUltimoEvento() { return ultimoEvento; }
    // Cambia el último evento
    public void setUltimoEvento(String ultimoEvento) { this.ultimoEvento = ultimoEvento; 
    
    }
    }