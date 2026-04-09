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

    
   

    public void jugarTornTirarDau(Dau dau) {

      
        if (hiHaGuanyador()) return;

        Jugador jugador = obtenirJugadorActual();

       
        if (!jugador.potJugar()) {
            registrar(jugador.getNickname() + " està congelat  i perd el torn");
            seguentTorn();
            return;
        }

        int tirada = jugador.tirarDau(dau);
        int posAbans = jugador.getPosicioActual();

        jugador.moure(tirada, taulell.getNumCaselles());

        int posDespres = jugador.getPosicioActual();

        registrar(jugador.getNickname() + " ha tret " + tirada +
                " i ha passat de " + posAbans + " a " + posDespres);

      
        if (jugador.esGuanyador(taulell.getNumCaselles())) {
            guanyador = jugador;
            registrar("🎉 " + jugador.getNickname() + " ha guanyat la partida!");
            return;
        }

    
        Casella casella = taulell.obtenirCasella(posDespres);
        casella.aplicarEfecte(jugador, this);

        seguentTorn();
    }

    public boolean jugarTornBolaNeu(Jugador objectiu) {

        if (hiHaGuanyador()) return false;

        Jugador atacant = obtenirJugadorActual();

        if (!atacant.potJugar()) {
            registrar(atacant.getNickname() + " està congelat  i no pot atacar");
            seguentTorn();
            return false;
        }

        boolean ok = atacant.utilitzarBolaNeu(objectiu);

        if (ok) {
            registrar(atacant.getNickname() + " ha atacat " +
                    objectiu.getNickname() + " amb bola de neu ");
        } else {
            registrar(atacant.getNickname() + " no té boles de neu.");
        }

        seguentTorn();
        return ok;
    }

   

    public Esdeveniment generarEsdevenimentAleatori() {
        return generadorEsdeveniments.generarAleatori();
    }

    public void aplicarEsdeveniment(Jugador jugador) {
        Esdeveniment e = generarEsdevenimentAleatori();
        e.aplicar(jugador, this);

        registrar("Event: " + e.getDescripcio());
    }

    

    public boolean hiHaGuanyador() {
        return guanyador != null;
    }

    public Jugador getGuanyador() {
        return guanyador;
    }

   
    private void registrar(String missatge) {
        historialAccions.add(missatge);
    }

 

    public void guardarEstat() {
        registrar("Partida guardada.");
    }

    public void carregarPartida(int id) {
        registrar("Partida carregada.");
    }
}