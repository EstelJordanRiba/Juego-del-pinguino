package model;

public class Casella_interrogatiu extends Casella {

    public Casella_interrogatiu(int idCasella, int posicio) {
        super(idCasella, posicio);
    }

    @Override
    public void aplicarEfecte(Jugador jugador, Partida partida) {

        Esdeveniment esdeveniment = partida.generarEsdevenimentAleatori();
        esdeveniment.aplicar(jugador, partida);

        partida.getHistorialAccions().add(
                jugador.getNickname() + " activa una casella sorpresa ❓"
        );
    }
}