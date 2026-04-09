package controller;

import model.*;

public class PartidaController {

    private Partida partida;

    public PartidaController(Partida partida) {
        this.partida = partida;
    }

    // =========================
    // INICI PARTIDA
    // =========================

    public void iniciarPartida() {
        partida.iniciarPartida();
    }

    // =========================
    // TORN: TIRAR DAU
    // =========================

    public void jugarTornDau() {

        if (partida.hiHaGuanyador()) return;

        Dau dau = Dau.crearDau(Dau.TipusDau.NORMAL);

        partida.jugarTornTirarDau(dau);
    }

    // =========================
    // TORN: ATAC
    // =========================

    public void atacarSeguentJugador() {

        if (partida.hiHaGuanyador()) return;

        Jugador actual = partida.getJugadorActual();

        for (Jugador j : partida.getJugadors()) {
            if (j != actual) {
                partida.jugarTornBolaNeu(j);
                break;
            }
        }
    }

    // =========================
    // GETTERS PER UI
    // =========================

    public Partida getPartida() {
        return partida;
    }

    public Jugador getJugadorActual() {
        return partida.getJugadorActual();
    }

    public String getUltimMissatge() {
        return partida.obtenirUltimMissatgeHistorial();
    }

    public boolean hiHaGuanyador() {
        return partida.hiHaGuanyador();
    }

    public Jugador getGuanyador() {
        return partida.getGuanyador();
    }
}