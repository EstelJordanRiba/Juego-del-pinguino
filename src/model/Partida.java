package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Partida {

    public enum EstatPartida {
        NO_INICIADA,
        EN_CURS,
        FINALITZADA
    }

    // =====================
    // ATRIBUTS
    // =====================

    private int idPartida;
    private List<Jugador> jugadors;
    private Taulell taulell;

    private int indexTornActual;
    private EstatPartida estat;

    private Generador_esdeveniments generadorEsdeveniments;

    private List<String> historialAccions;

    // =====================
    // CONSTRUCTORS
    // =====================

    public Partida(int idPartida, Taulell taulell) {

        this.idPartida = idPartida;
        this.taulell = taulell;

        this.jugadors = new ArrayList<>();
        this.indexTornActual = 0;

        this.estat = EstatPartida.NO_INICIADA;

        this.generadorEsdeveniments = new Generador_esdeveniments();
        this.historialAccions = new ArrayList<>();
    }

    public Partida() {

        this.jugadors = new ArrayList<>();
        this.historialAccions = new ArrayList<>();
        this.generadorEsdeveniments = new Generador_esdeveniments();

        this.estat = EstatPartida.NO_INICIADA;
    }

    // =====================
    // GETTERS
    // =====================

    public int getIdPartida() {
        return idPartida;
    }

    public List<Jugador> getJugadors() {
        return jugadors;
    }

    public Taulell getTaulell() {
        return taulell;
    }

    public int getIndexTornActual() {
        return indexTornActual;
    }

    public EstatPartida getEstat() {
        return estat;
    }

    public Generador_esdeveniments getGeneradorEsdeveniments() {
        return generadorEsdeveniments;
    }

    public List<String> getHistorialAccions() {
        return historialAccions;
    }

    // =====================
    // GESTIÓ DE PARTIDA
    // =====================

    public void afegirJugador(Jugador jugador) {

        if (estat != EstatPartida.NO_INICIADA) {
            throw new IllegalStateException("No pots afegir jugadors amb la partida en curs.");
        }

        if (jugador == null) {
            throw new IllegalArgumentException("Jugador null.");
        }

        jugadors.add(jugador);
    }

    public void iniciarPartida() {

        if (jugadors.isEmpty()) {
            throw new IllegalStateException("No hi ha jugadors a la partida.");
        }

        this.indexTornActual = 0;
        this.estat = EstatPartida.EN_CURS;

        registrar("Partida iniciada amb " + jugadors.size() + " jugadors.");
    }

    // =====================
    // TORN ACTUAL
    // =====================

    public Jugador obtenirJugadorActual() {

        if (jugadors.isEmpty()) {
            return null;
        }

        return jugadors.get(indexTornActual);
    }

    public void seguentTorn() {

        if (estat != EstatPartida.EN_CURS) {
            return;
        }

        int intents = 0;

        do {

            indexTornActual = (indexTornActual + 1) % jugadors.size();

            intents++;

            if (intents > jugadors.size()) {
                break;
            }

        } while (obtenirJugadorActual().getEstat() != Jugador.EstatJugador.ACTIU);

        registrar("Següent torn -> " + obtenirJugadorActual().getNickname());
    }

    public void executarTornActual() {

        Jugador jugador = obtenirJugadorActual();

        if (jugador instanceof FocaIA) {

            ((FocaIA) jugador).decidirAccio(this);

        } else {

            // Espera acció del jugador humà
        }
    }

    // =====================
    // FLUX DEL TORN
    // =====================

    public void jugarTornTirarDau(Dau dau) {

        validarEnCurs();

        Jugador jugador = obtenirJugadorActual();

        int passos = jugador.tirarDau(dau);

        registrar(jugador.getNickname() + " tira dau " + dau.getTipus() + " -> " + passos);

        if (passos <= 0) {

            seguentTorn();
            return;
        }

        jugador.moure(passos, taulell.getNumCaselles());

        registrar(jugador.getNickname() + " es mou fins la posició " + jugador.getPosicioActual());

        aplicarCasellaActual(jugador);

        if (jugador.esGuanyador(taulell.getNumCaselles())) {

            estat = EstatPartida.FINALITZADA;

            registrar("🏆 Guanyador: " + jugador.getNickname());

            return;
        }

        seguentTorn();
    }

    public boolean jugarTornBolaNeu(Jugador objectiu) {

        validarEnCurs();

        Jugador atacant = obtenirJugadorActual();

        if (objectiu == null) return false;

        if (Objects.equals(atacant, objectiu)) return false;

        boolean ok = atacant.utilitzarBolaNeu(objectiu);

        if (ok) {

            registrar(atacant.getNickname() + " llança bola de neu a " + objectiu.getNickname());

            seguentTorn();

        } else {

            registrar(atacant.getNickname() + " intenta bola de neu però no en té.");
        }

        return ok;
    }

    private void aplicarCasellaActual(Jugador jugador) {

        Casella casella = taulell.obtenirCasella(jugador.getPosicioActual());

        if (casella == null) {

            registrar("No hi ha casella definida a la posició " + jugador.getPosicioActual());
            return;
        }

        registrar("Cau a casella: " + casella.getClass().getSimpleName());

        casella.aplicarEfecte(jugador, this);

        registrar("Després d'efecte -> posició " + jugador.getPosicioActual());
    }

    // =====================
    // MÈTODES PER CONTROLLERS
    // =====================

    public Jugador getJugadorActual() {
        return obtenirJugadorActual();
    }

    public void passarTorn() {
        seguentTorn();
    }

    public int tirarDau() {
        return (int) (Math.random() * 6) + 1;
    }

    public boolean hiHaGuanyador() {

        for (Jugador j : jugadors) {

            if (j.esGuanyador(taulell.getNumCaselles())) {
                return true;
            }
        }

        return false;
    }

    public Jugador getGuanyador() {

        for (Jugador j : jugadors) {

            if (j.esGuanyador(taulell.getNumCaselles())) {
                return j;
            }
        }

        return null;
    }

    // =====================
    // PERSISTÈNCIA (DAO)
    // =====================

    public void guardarEstat() {

        registrar("guardarEstat() pendent d'implementar amb BD/DAO.");
    }

    public void carregarPartida(int idPartida) {

        registrar("carregarPartida(" + idPartida + ") pendent d'implementar amb BD/DAO.");
    }

    // =====================
    // HISTORIAL
    // =====================

    private void registrar(String missatge) {

        historialAccions.add(LocalDateTime.now() + " - " + missatge);
    }

    private void validarEnCurs() {

        if (estat != EstatPartida.EN_CURS) {

            throw new IllegalStateException("La partida no està en curs.");
        }
    }
}