package controller;

import model.Partida;
import model.Jugador;
import model.Taulell;
import model.Casella;

public class PartidaController {

    private Partida partida;
    private TaulellController taulellController;

    public PartidaController(Partida partida) {
        this.partida = partida;
        this.taulellController = new TaulellController(partida.getTaulell());
    }

    /**
     * Inicia la partida.
     */
    public void iniciarPartida() {

        System.out.println("La partida ha començat!");

        while (!partida.hiHaGuanyador()) {

            Jugador jugadorActual = partida.getJugadorActual();

            System.out.println("Torn de: " + jugadorActual.getNickname());

            executarTorn(jugadorActual);

            partida.passarTorn();
        }

        System.out.println("Guanyador: " + partida.getGuanyador().getNickname());
    }

    /**
     * Executa el torn d’un jugador.
     */
    private void executarTorn(Jugador jugador) {

        int resultatDau = partida.tirarDau();

        System.out.println(jugador.getNickname() + " ha tret: " + resultatDau);

        jugador.moure(resultatDau, partida.getTaulell().getNumCaselles());

        taulellController.aplicarEfecteCasella(jugador, partida);
    }
}