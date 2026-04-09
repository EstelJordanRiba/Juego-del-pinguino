package model;

public class Casella_forat extends Casella {

    private int desti;

    public Casella_forat(int idCasella, int posicio, int desti) {
        super(idCasella, posicio);
        this.desti = desti;
    }

    @Override
    public void aplicarEfecte(Jugador jugador, Partida partida) {

        jugador.setPosicioActual(desti, partida.getTaulell().getNumCaselles());

        partida.getHistorialAccions().add(
                jugador.getNickname() + " cau en un forat i va a la casella " + desti
        );
    }
}