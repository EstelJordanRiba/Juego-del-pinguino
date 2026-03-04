package Pingu;
public class Casella_forat extends Casella {

    private int desti;

    public Casella_forat(int idCasella, int posicio, int desti) {
        super(idCasella, posicio);
        this.desti = desti;

    @Override
    public void aplicarEfecte(Jugador jugador, Partida partida) {
        System.out.println("Has caigut en un forat! Retrocedeixes...");
        jugador.setPosicioActual(desti, partida.getTaulell().getNumCaselles());
    }
}