package controller;

import model.Taulell;
import model.Jugador;
import model.Casella;
import model.Partida;

public class TaulellController {

    private Taulell taulell;

    public TaulellController(Taulell taulell) {
        this.taulell = taulell;
    }

    /**
     * Obté la casella on està el jugador.
     */
    public Casella obtenirCasellaJugador(Jugador jugador) {
        return taulell.obtenirCasella(jugador.getPosicioActual());
    }

    /**
     * Aplica l’efecte de la casella.
     */
    public void aplicarEfecteCasella(Jugador jugador, Partida partida) {

        Casella casella = taulell.obtenirCasella(jugador.getPosicioActual());

        casella.aplicarEfecte(jugador, partida);
    }
}