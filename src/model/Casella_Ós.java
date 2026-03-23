package model;

public class Casella_Ós extends Casella {

    public Casella_Ós(int idCasella, int posicio) {
        super(idCasella, posicio);
    }

    @Override
    public void aplicarEfecte(Jugador jugador, Partida partida) {

        if (jugador.getInventari().tePeixos()) {

            boolean usat = jugador.utilitzarPeix();

            if (usat) {
                partida.getHistorialAccions().add(
                        jugador.getNickname() + " suborna l'ós amb un peix 🐟"
                );
                return;
            }
        }

        jugador.tornarInici();

        partida.getHistorialAccions().add(
                jugador.getNickname() + " és atacat per l'ós 🐻 i torna a l'inici"
        );
    }
}