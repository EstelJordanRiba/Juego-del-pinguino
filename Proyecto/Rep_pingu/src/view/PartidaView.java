package view;

import model.Partida;
import model.Jugador;

public class PartidaView {

    public String mostrarEstadoPartida(Partida partida) {

        Jugador actual = partida.getJugadorActual();

        return "Torn de: " + actual.getNickname() +
               " | Posició: " + actual.getPosicioActual();
    }

    public String mostrarGanador(Partida partida) {

        if (partida.hiHaGuanyador()) {
            return "🏆 Guanyador: " + partida.getGuanyador().getNickname();
        }

        return "";
    }

    public String mostrarTurnoJugador(Partida partida) {

        return "Torn de: " + partida.getJugadorActual().getNickname();
    }
}