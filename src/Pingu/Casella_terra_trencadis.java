package Pingu;

public class Casella_terra_trencadis  extends Casella {
    @Override
    public void aplicarEfecte(Jugador jugador, Partida partida) {
        int totalObjectes = jugador.getInventari().getTotalDaus()
                + jugador.getInventari().getPeixos()
                + jugador.getInventari().getBolesNeu();

        if (totalObjectes > 5) {
            jugador.tornarInici();
        } else if (totalObjectes > 0) {
            jugador.setEstat(Jugador.EstatJugador.CONGELAT);
        }
    }
}