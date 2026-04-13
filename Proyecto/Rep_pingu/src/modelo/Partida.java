package modelo;

import java.io.Serializable;
import java.util.ArrayList;

public class Partida implements Serializable {
    private static final long serialVersionUID = 1L;

    private Tablero tablero;
    private ArrayList<Jugador> jugadores;
    private int turnos;
    private int jugadorActual;
    private boolean finalizada;
    private Jugador ganador;
    private String ultimoEvento;

    public Partida() {
        this.tablero = new Tablero();
        this.jugadores = new ArrayList<Jugador>();
        this.turnos = 0;
        this.jugadorActual = 0;
        this.finalizada = false;
        this.ganador = null;
        this.ultimoEvento = "La partida ha comenzado.";
        this.tablero.generarCasillasAleatorias();
    }

    public Tablero getTablero() {
        return tablero;
    }

    public void setTablero(Tablero tablero) {
        this.tablero = tablero;
    }

    public ArrayList<Jugador> getJugadores() {
        return jugadores;
    }

    public void setJugadores(ArrayList<Jugador> jugadores) {
        this.jugadores = jugadores;
    }

    public int getTurnos() {
        return turnos;
    }

    public void setTurnos(int turnos) {
        this.turnos = turnos;
    }

    public int getJugadorActualIndice() {
        return jugadorActual;
    }

    public void setJugadorActualIndice(int jugadorActual) {
        this.jugadorActual = jugadorActual;
    }

    public boolean isFinalizada() {
        return finalizada;
    }

    public void setFinalizada(boolean finalizada) {
        this.finalizada = finalizada;
    }

    public Jugador getGanador() {
        return ganador;
    }

    public void setGanador(Jugador ganador) {
        this.ganador = ganador;
    }

    public String getUltimoEvento() {
        return ultimoEvento;
    }

    public void setUltimoEvento(String ultimoEvento) {
        this.ultimoEvento = ultimoEvento;
    }

    public Jugador getJugadorActual() {
        if (jugadores == null || jugadores.isEmpty()) {
            return null;
        }
        return jugadores.get(jugadorActual);
    }

    public void siguienteTurno() {
        if (!jugadores.isEmpty()) {
            jugadorActual = (jugadorActual + 1) % jugadores.size();
            turnos++;
        }
    }

    public void comprobarGanador() {
        for (Jugador jugador : jugadores) {
            if (jugador.getPosicion() >= 49) {
                finalizada = true;
                ganador = jugador;
                ultimoEvento = jugador.getNombre() + " ha ganado la partida.";
                break;
            }
        }
    }
}