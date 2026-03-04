package Pingu;

public class FocaIA extends Jugador {

    private int tornsBloquejada = 0;

    public FocaIA(int idJugador, String nickname, int ordreTorn) {
        super(idJugador, nickname, ordreTorn);
    }

    public void decidirAccio(Partida partida) {

        if (tornsBloquejada > 0) {
            tornsBloquejada--;
            return;
        }

        // Heurística simple:
        // Si hi ha un jugador a la mateixa casella → atacar
        for (Jugador j : partida.getJugadors()) {
            if (j != this && j.getPosicioActual() == this.getPosicioActual()) {
                atacar(j, partida);
                return;
            }
        }

        // Si no, tirar dau normal
        Dau dau = Dau.crearDau(Dau.TipusDau.NORMAL);
        int passos = tirarDau(dau);
        moure(passos, partida.getTaulell().getNumCaselles());
    }

    private void atacar(Jugador jugador, Partida partida) {
        jugador.tornarInici(); // exemple simple
    }

    public void bloquejar(int torns) {
        this.tornsBloquejada = torns;
    }
}
