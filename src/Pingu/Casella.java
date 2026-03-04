package Pingu;
public abstract class Casella {

    protected int idCasella;
    protected int posicio;

    public Casella(int idCasella, int posicio) {
        this.idCasella = idCasella;
        this.posicio = posicio;
    }

    public int getIdCasella() {
        return idCasella;
    }

    public int getPosicio() {
        return posicio;
    }

    /**
     * Cada tipus de casella implementa el seu comportament.
     */
    public abstract void aplicarEfecte(Jugador jugador, Partida partida);

    @Override
    public String toString() {
        return "Casella{" +
                "id=" + idCasella +
                ", posicio=" + posicio +
                '}';
    }
}