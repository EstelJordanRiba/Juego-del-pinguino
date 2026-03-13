package model;

public class Casella_forat extends Casella {
private int desti;
public Casella_forat(int idCasella, int posicio, int desti) {
   super(idCasella, posicio);
   this.desti = desti;
}
@Override
public void aplicarEfecte(Jugador jugador, Partida partida) {
   // El jugador cae en el forat, es teletransportado a la casella de destino
   jugador.setPosicioActual(desti, partida.getTaulell().getNumCaselles());
   System.out.println("El jugador cau en un forat i és teletransportat a la casella " + desti);
	}
}
