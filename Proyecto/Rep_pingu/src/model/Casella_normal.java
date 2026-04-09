package model;

public class Casella_normal extends Casella {

    public Casella_normal(int idCasella, int posicio) {
        super(idCasella, posicio);
    }

    @Override
    public void aplicarEfecte(Jugador jugador, Partida partida) {

        partida.getHistorialAccions().add(
                jugador.getNickname() + " cau en una casella normal."
        );
    }
}