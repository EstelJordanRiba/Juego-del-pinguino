package controller;

import model.*;

public class TaulellController {

    private Taulell taulell;

    public TaulellController(Taulell taulell) {
        this.taulell = taulell;
    }

    // =========================
    // CONSULTA
    // =========================

    public Casella obtenirCasellaJugador(Jugador jugador) {
        return taulell.obtenirCasella(jugador.getPosicioActual());
    }

    // =========================
    // INFO PER UI (OPCIONAL)
    // =========================

    public int getPosicioJugador(Jugador jugador) {
        return jugador.getPosicioActual();
    }

    public int getTotalCaselles() {
        return taulell.getNumCaselles();
    }
}