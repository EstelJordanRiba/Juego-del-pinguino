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

    public void passarTorn() {
        seguentTorn();
    }

    // =========================
    // ACCIONS DE JOC
    // =========================

    public void jugarTornTirarDau(Dau dau) {

        // 🔥 1. Si ja hi ha guanyador → parar
        if (hiHaGuanyador()) return;

        Jugador jugador = obtenirJugadorActual();

        // 🔥 2. Control jugador congelat
        if (!jugador.potJugar()) {
            registrar(jugador.getNickname() + " està congelat ❄ i perd el torn");
            seguentTorn();
            return;
        }

        int tirada = jugador.tirarDau(dau);
        int posAbans = jugador.getPosicioActual();

        jugador.moure(tirada, taulell.getNumCaselles());

        int posDespres = jugador.getPosicioActual();

        registrar(jugador.getNickname() + " ha tret " + tirada +
                " i ha passat de " + posAbans + " a " + posDespres);

        // 🔥 3. Comprovar guanyador
        if (jugador.esGuanyador(taulell.getNumCaselles())) {
            guanyador = jugador;
            registrar("🎉 " + jugador.getNickname() + " ha guanyat la partida!");
            return;
        }

        // 🔥 4. Aplicar efecte casella
        Casella casella = taulell.obtenirCasella(posDespres);
        casella.aplicarEfecte(jugador, this);

        seguentTorn();
    }

    public boolean jugarTornBolaNeu(Jugador objectiu) {

        if (hiHaGuanyador()) return false;

        Jugador atacant = obtenirJugadorActual();

        // 🔥 control congelat
        if (!atacant.potJugar()) {
            registrar(atacant.getNickname() + " està congelat ❄ i no pot atacar");
            seguentTorn();
            return false;
        }

        boolean ok = atacant.utilitzarBolaNeu(objectiu);

        if (ok) {
            registrar(atacant.getNickname() + " ha atacat " +
                    objectiu.getNickname() + " amb bola de neu ❄");
        } else {
            registrar(atacant.getNickname() + " no té boles de neu.");
        }

        seguentTorn();
        return ok;
    }

    // =========================
    // ESDEVENIMENTS
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
    // GUANYADOR
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