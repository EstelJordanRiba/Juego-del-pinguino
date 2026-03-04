package Pingu;

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

    private int indexTornActual; // índex dins jugadors
    private EstatPartida estat;

    private Generador_esdeveniments generadorEsdeveniments;

    // Historial bàsic (després ho persistim a BD)
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
        if (jugador == null) throw new IllegalArgumentException("Jugador null.");
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

    public Jugador obtenirJugadorActual() {
        if (jugadors.isEmpty()) return null;
        return jugadors.get(indexTornActual);
    }

    public void seguentTorn() {
        if (estat != EstatPartida.EN_CURS) return;

        // passa al següent jugador ACTIU (evita congelats/eliminats)
        int intents = 0;
        do {
            indexTornActual = (indexTornActual + 1) % jugadors.size();
            intents++;
            if (intents > jugadors.size()) break; // evita bucle infinit
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
    // FLUX D'UN TORN (document) :contentReference[oaicite:1]{index=1}
    // =====================

    /**
     * Acció: tirar un dau (normal/rapid/lent) i aplicar efecte de casella.
     * Nota: el consum del dau (inventari) el pots gestionar al controller si vols.
     */
    public void jugarTornTirarDau(Dau dau) {
        validarEnCurs();

        Jugador jugador = obtenirJugadorActual();

        int passos = jugador.tirarDau(dau);
        registrar(jugador.getNickname() + " tira dau " + dau.getTipus() + " -> " + passos);

        if (passos <= 0) {
            // si el dau falla per probabilitat, no es mou
            seguentTorn();
            return;
        }

        jugador.moure(passos, taulell.getNumCaselles());
        registrar(jugador.getNickname() + " es mou fins la posició " + jugador.getPosicioActual());

        aplicarCasellaActual(jugador);

        // comprovar guanyador
        if (jugador.esGuanyador(taulell.getNumCaselles())) {
            estat = EstatPartida.FINALITZADA;
            registrar("🏆 Guanyador: " + jugador.getNickname());
            return;
        }

        seguentTorn();
    }

    /**
     * Acció: utilitzar bola de neu contra un objectiu.
     * (No mou el jugador; només aplica efecte sobre rival)
     */
    public boolean jugarTornBolaNeu(Jugador objectiu) {
        validarEnCurs();

        Jugador atacant = obtenirJugadorActual();

        if (objectiu == null) return false;
        if (Objects.equals(atacant, objectiu)) return false;

        boolean ok = atacant.utilitzarBolaNeu(objectiu);
        if (ok) {
            registrar(atacant.getNickname() + " llança bola de neu a " + objectiu.getNickname() +
                    " -> " + objectiu.getPosicioActual());
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

        registrar("Cau a casella: " + casella.getClass().getSimpleName() + " (pos " + casella.getPosicio() + ")");
        casella.aplicarEfecte(jugador, this);
        registrar("Després d'efecte -> posició " + jugador.getPosicioActual());
    }

    // =====================
    // PERSISTÈNCIA (HOOKS)
    // =====================

    /**
     * Aquí cridaràs PartidaDAO, JugadorDAO, InventariDAO, TaulellDAO...
     * (al document diu guardar estat en BD i xifrat) :contentReference[oaicite:2]{index=2}
     */
    public void guardarEstat() {
        // TODO: implementar amb DAOs
        registrar("guardarEstat() pendent d'implementar amb BD/DAO.");
    }

    public void carregarPartida(int idPartida) {
        // TODO: implementar amb DAOs
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