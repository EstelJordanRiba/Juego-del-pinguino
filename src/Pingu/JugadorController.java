package Pingu;

import model.*;

public class JugadorController {

    public void moverJugador(Jugador jugador, int pasos, int limite) {
        jugador.moure(pasos, limite);
    }

    public void usarPeix(Jugador jugador) {
        jugador.utilitzarPeix();
    }

    public void atacarJugador(Jugador atacante, Jugador objetivo) {
        atacante.utilitzarBolaNeu(objetivo);
    }

}