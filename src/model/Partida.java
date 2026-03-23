package model;

import java.util.ArrayList;
import java.util.List;

public class Partida {

    private int idPartida;
    private Taulell taulell;
    private List<Jugador> jugadors;
    private int indexJugadorActual;

    private List<String> historialAccions;

    private GeneradorEsdeveniments generadorEsdeveniments;

    // NUEVO
    private Jugador guanyador;

    public Partida(int idPartida, Taulell taulell) {
        this.idPartida = idPartida;
        this.taulell = taulell;
        this.jugadors = new ArrayList<>();
        this.indexJugadorActual = 0;
        this.historialAccions = new ArrayList<>();
        this.generadorEsdeveniments = new GeneradorEsdeveniments();
        this.guanyador = null;
    }

    // =========================
    // GETTERS
    // =========================

    public int getIdPartida() {
        return idPartida;
    }

    public Taulell getTaulell() {
        return taulell;
    }

    public List<Jugador> getJugadors() {
        return jugadors;
    }

    public Jugador obtenirJugadorActual() {
        return jugadors.get(indexJugadorActual);
    }

    //  MÉTODO QUE TE FALTABA
    public Jugador getJugadorActual() {
        return obtenirJugadorActual();
    }

    public List<String> getHistorialAccions() {
        return historialAccions;
    }

    public String obtenirUltimMissatgeHistorial() {
        if (historialAccions == null || historialAccions.isEmpty()) {
            return "Sense esdeveniments.";
        }
        return historialAccions.get(historialAccions.size() - 1);
    }

    // =========================
    // GESTIÓ PARTIDA
    // =========================

    public void afegirJugador(Jugador j) {
        jugadors.add(j);
    }

    public void iniciarPartida() {
        indexJugadorActual = 0;
        registrar("Partida iniciada amb " + jugadors.size() + " jugadors.");
    }

    public void seguentTorn() {
        indexJugadorActual = (indexJugadorActual + 1) % jugadors.size();
    }

    //  MÉTODO QUE TE FALTABA
    public void passarTorn() {
        seguentTorn();
    }

    // =========================
    // ACCIONS DE JOC
    // =========================

    public void jugarTornTirarDau(Dau dau) {

        Jugador jugador = obtenirJugadorActual();

        int tirada = jugador.tirarDau(dau);
        int posAbans = jugador.getPosicioActual();

        jugador.moure(tirada, taulell.getNumCaselles());

        int posDespres = jugador.getPosicioActual();

        registrar(jugador.getNickname() + " ha tret " + tirada +
                " i ha passat de " + posAbans + " a " + posDespres);

        // 🔥 comprobar ganador
        if (jugador.esGuanyador(taulell.getNumCaselles())) {
            guanyador = jugador;
            registrar("🎉 " + jugador.getNickname() + " ha guanyat la partida!");
            return;
        }

        // aplicar efecte casella
        Casella casella = taulell.obtenirCasella(posDespres);
        casella.aplicarEfecte(jugador, this);

        seguentTorn();
    }

    public boolean jugarTornBolaNeu(Jugador objectiu) {

        Jugador atacant = obtenirJugadorActual();

        boolean ok = atacant.utilitzarBolaNeu(objectiu);

        if (ok) {
            registrar(atacant.getNickname() + " ha atacat " +
                    objectiu.getNickname() + " amb bola de neu.");
        } else {
            registrar(atacant.getNickname() + " no té boles de neu.");
        }

        seguentTorn();
        return ok;
    }

    // =========================
    // 🔥 ESDEVENIMENTS
    // =========================

    public Esdeveniment generarEsdevenimentAleatori() {
        return generadorEsdeveniments.generarAleatori();
    }

    public void aplicarEsdeveniment(Jugador jugador) {
        Esdeveniment e = generarEsdevenimentAleatori();
        e.aplicar(jugador, this);

        registrar("Event: " + e.getDescripcio());
    }

    // =========================
    // 🔥 MÉTODOS QUE TE FALTABAN
    // =========================

    public boolean hiHaGuanyador() {
        return guanyador != null;
    }

    public Jugador getGuanyador() {
        return guanyador;
    }

    // =========================
    // HISTORIAL
    // =========================

    private void registrar(String missatge) {
        historialAccions.add(missatge);
    }

    // =========================
    // GUARDAR / CARREGAR
    // =========================

    public void guardarEstat() {
        registrar("Partida guardada.");
    }

    public void carregarPartida(int id) {
        registrar("Partida carregada.");
    }
}