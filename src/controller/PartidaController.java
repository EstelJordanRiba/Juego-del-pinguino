package controller;
import model.*;

public class PartidaController {

    private Partida partida;

    public PartidaController(Partida partida) {
        this.partida = partida;
    }

    public void iniciarPartida() {
        partida.iniciarPartida();
    }

    public Jugador obtenerJugadorActual() {
        return partida.obtenirJugadorActual();
    }

    public void tirarDau(Dau dau) {
        partida.jugarTornTirarDau(dau);
    }

    public boolean usarBolaNeu(Jugador objetivo) {
        return partida.jugarTornBolaNeu(objetivo);
    }

    public void siguienteTurno() {
        partida.seguentTorn();
    }

}