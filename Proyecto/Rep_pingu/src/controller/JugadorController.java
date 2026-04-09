package controller;

import model.*;

public class JugadorController {

    // =========================
    // MOVIMENT
    // =========================

    public void moureJugador(Jugador jugador, int passos, Partida partida) {

        if (!jugador.potJugar()) {
            partida.getHistorialAccions().add(
                    jugador.getNickname() + " està congelat ❄ i no es pot moure"
            );
            return;
        }

        int posAbans = jugador.getPosicioActual();

        jugador.moure(passos, partida.getTaulell().getNumCaselles());

        int posDespres = jugador.getPosicioActual();

        partida.getHistorialAccions().add(
                jugador.getNickname() + " es mou de " + posAbans + " a " + posDespres
        );
    }

    // =========================
    // PEIX
    // =========================

    public void usarPeix(Jugador jugador, Partida partida) {

        boolean ok = jugador.utilitzarPeix();

        if (ok) {
            partida.getHistorialAccions().add(
                    jugador.getNickname() + " utilitza un peix 🐟"
            );
        } else {
            partida.getHistorialAccions().add(
                    jugador.getNickname() + " no té peixos"
            );
        }
    }

    // =========================
    // ATAC
    // =========================

    public void atacarJugador(Jugador atacante, Jugador objetivo, Partida partida) {

        if (!atacante.potJugar()) {
            partida.getHistorialAccions().add(
                    atacante.getNickname() + " està congelat ❄ i no pot atacar"
            );
            return;
        }

        boolean ok = atacante.utilitzarBolaNeu(objetivo);

        if (ok) {
            partida.getHistorialAccions().add(
                    atacante.getNickname() + " ataca " +
                    objetivo.getNickname() + " ❄"
            );
        } else {
            partida.getHistorialAccions().add(
                    atacante.getNickname() + " no té boles de neu"
            );
        }
    }
}