package view;

import model.Jugador;
import model.Inventari;

public class JugadorView {

    public String mostrarInfoJugador(Jugador jugador) {

        return "👤 " + jugador.getNickname() +
               " Posició: " + jugador.getPosicioActual() +
               " Estat: " + jugador.getEstat();
    }

    public String mostrarInventari(Jugador jugador) {

        Inventari inv = jugador.getInventari();

        return " Peixos: " + inv.getPeixos() +
               "  Boles: " + inv.getBolesNeu() +
               "  Daus: " + inv.getTotalDaus();
    }
}