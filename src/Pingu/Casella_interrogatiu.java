package Pingu;


public class Casella_interrogatiu extends Casella {

    public Casella_interrogatiu(int idCasella, int posicio) {
        super(idCasella, posicio);
    }

    @Override
    public void aplicarEfecte(Jugador jugador, Partida partida) {

        Esdeveniment esdeveniment = partida.generarEsdevenimentAleatori();
        esdeveniment.aplicar(jugador);

        System.out.println("Casella sorpresa activada!");
    }
}