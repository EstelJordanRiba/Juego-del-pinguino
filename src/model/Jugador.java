package model;

import java.util.Objects;

public class Jugador {

    public enum EstatJugador {
        ACTIU,
        ELIMINAT,
        CONGELAT
    }

    private int idJugador;
    private String nickname;
    private int posicioActual;
    private int ordreTorn;
    private EstatJugador estat;
    private Inventari inventari;

    // 🔥 EXTRA PRO
    private int tornsCongelat = 0;

    public Jugador(int idJugador, String nickname, int ordreTorn) {
        this.idJugador = idJugador;
        this.nickname = nickname;
        this.ordreTorn = ordreTorn;
        this.posicioActual = 0;
        this.estat = EstatJugador.ACTIU;
        this.inventari = new Inventari();
    }

    public Jugador() {
        this.posicioActual = 0;
        this.estat = EstatJugador.ACTIU;
        this.inventari = new Inventari();
    }

    // =========================
    // GETTERS
    // =========================

    public int getIdJugador() { return idJugador; }
    public String getNickname() { return nickname; }
    public int getPosicioActual() { return posicioActual; }
    public int getOrdreTorn() { return ordreTorn; }
    public EstatJugador getEstat() { return estat; }
    public Inventari getInventari() { return inventari; }

  

    public void setEstat(EstatJugador estat) {
        this.estat = estat;
    }

    public void setPosicioActual(int posicioActual, int numCasellesTaulell) {
        if (posicioActual < 0) {
            this.posicioActual = 0;
        } else if (posicioActual > numCasellesTaulell) {
            this.posicioActual = numCasellesTaulell;
        } else {
            this.posicioActual = posicioActual;
        }
    }

    

    public boolean potJugar() {

        if (estat == EstatJugador.CONGELAT) {

            if (tornsCongelat > 0) {
                tornsCongelat--;
                return false;
            } else {
                estat = EstatJugador.ACTIU;
            }
        }

        return estat == EstatJugador.ACTIU;
    }

    public void congelar(int torns) {
        this.estat = EstatJugador.CONGELAT;
        this.tornsCongelat = torns;
    }

    

    public int tirarDau(Dau dau) {
        return dau.tirar();
    }

    public void moure(int passos, int numCasellesTaulell) {
        setPosicioActual(this.posicioActual + passos, numCasellesTaulell);
    }

    public void retrocedir(int passos) {
        this.posicioActual -= passos;
        if (this.posicioActual < 0) {
            this.posicioActual = 0;
        }
    }

    public void tornarInici() {
        this.posicioActual = 0;
    }

    public boolean esGuanyador(int ultimaCasella) {
        return this.posicioActual >= ultimaCasella;
    }

    public boolean utilitzarBolaNeu(Jugador objectiu) {
        if (inventari.gastarBolaNeu()) {
            objectiu.retrocedir(3);
            return true;
        }
        return false;
    }

    public boolean utilitzarPeix() {
        return inventari.gastarPeix();
    }

   

    public void afegirPeix() {
        inventari.afegirPeix();
    }

    public void afegirBolesNeu(int quantitat) {
        inventari.afegirBolesNeu(quantitat);
    }

    public void afegirDauRapid() {
        inventari.afegirDauRapid();
    }

    public void afegirDauLent() {
        inventari.afegirDauLent();
    }

   

    @Override
    public String toString() {
        return "Jugador{" +
                "id=" + idJugador +
                ", nickname='" + nickname + '\'' +
                ", posicio=" + posicioActual +
                ", estat=" + estat +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Jugador)) return false;
        Jugador jugador = (Jugador) o;
        return idJugador == jugador.idJugador;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idJugador);
    }
}