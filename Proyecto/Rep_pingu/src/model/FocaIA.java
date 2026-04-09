package model;

public class FocaIA extends Jugador {

    private int tornsBloquejada = 0;

    public FocaIA(int idJugador, String nickname, int ordreTorn) {
        super(idJugador, nickname, ordreTorn);
    }

    public void decidirAccio(Partida partida) {

        // =========================
        // BLOQUEIG
        // =========================
        if (tornsBloquejada > 0) {
            tornsBloquejada--;

            partida.getHistorialAccions().add(
                    getNickname() + " està bloquejada 🧊 (" + tornsBloquejada + " torns)"
            );

            return;
        }

        // =========================
        // PRIORITAT 1: atacar si comparteix casella
        // =========================
        for (Jugador j : partida.getJugadors()) {
            if (j != this && j.getPosicioActual() == this.getPosicioActual()) {

                atacar(j, partida);

                partida.getHistorialAccions().add(
                        getNickname() + " ataca " + j.getNickname() + " 🦭"
                );

                return;
            }
        }

        // =========================
        // PRIORITAT 2: atacar el líder
        // =========================
        Jugador lider = obtenirLider(partida);

        if (lider != null && lider != this) {

            int distancia = Math.abs(lider.getPosicioActual() - getPosicioActual());

            if (distancia <= 3) {

                atacar(lider, partida);

                partida.getHistorialAccions().add(
                        getNickname() + " ataca el líder " + lider.getNickname() + " 🦭🔥"
                );

                return;
            }
        }

        // =========================
        // SI NO → AVANÇAR
        // =========================
        Dau dau = Dau.crearDau(Dau.TipusDau.NORMAL);
        int passos = tirarDau(dau);

        moure(passos, partida.getTaulell().getNumCaselles());

        partida.getHistorialAccions().add(
                getNickname() + " es mou " + passos + " caselles"
        );
    }

    // =========================
    // ATAC
    // =========================
    private void atacar(Jugador jugador, Partida partida) {

        jugador.tornarInici();

        partida.getHistorialAccions().add(
                jugador.getNickname() + " és enviat a l'inici per la foca 🦭"
        );
    }

    // =========================
    // LÍDER
    // =========================
    private Jugador obtenirLider(Partida partida) {

        Jugador lider = null;

        for (Jugador j : partida.getJugadors()) {

            if (lider == null || j.getPosicioActual() > lider.getPosicioActual()) {
                lider = j;
            }
        }

        return lider;
    }

    // =========================
    // BLOQUEIG
    // =========================
    public void bloquejar(int torns) {
        this.tornsBloquejada = torns;
    }
}