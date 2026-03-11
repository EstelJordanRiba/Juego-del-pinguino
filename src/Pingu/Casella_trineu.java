package Pingu;
public class Casella_trineu extends Casella {
public Casella_trineu(int idCasella, int posicio) {
   super(idCasella, posicio);
}
@Override
public void aplicarEfecte(Jugador jugador, Partida partida) {
   int novaPosicio = partida.getTaulell()
           .buscarSeguentTrineu(jugador.getPosicioActual());
   jugador.setPosicioActual(novaPosicio, partida.getTaulell().getNumCaselles());
   System.out.println("El trineu t'impulsa fins a la següent casella de trineu!");
}
}
