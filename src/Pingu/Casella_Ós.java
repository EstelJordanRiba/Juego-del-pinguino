package Pingu;
public class Casella_Ós extends Casella {

    public CasellaOs(int idCasella, int posicio) {
        super(idCasella, posicio);
    }

    @Override
    public void aplicarEfecte(Jugador jugador, Partida partida) {

        if (jugador.getInventari().tePeixos()) {
            boolean usat = jugador.utilitzarPeix();
            if (usat) {
                System.out.println("El jugador ha subornat l'Ós amb un peix!");
                return;
            }
        }

        System.out.println("L'Ós ataca! Tornes a l'inici.");
        jugador.tornarInici();
    }
}