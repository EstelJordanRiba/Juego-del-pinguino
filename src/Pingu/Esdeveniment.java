package Pingu;

public class Esdeveniment {

    public enum TipusEsdeveniment {
        OBTENIR_PEIX,
        OBTENIR_BOLES_NEU,
        OBTENIR_DAU_RAPID,
        OBTENIR_DAU_LENT
    }

    private TipusEsdeveniment tipus;
    private String descripcio;
    private double probabilitat; // pes/prob (0..1)

    public Esdeveniment(TipusEsdeveniment tipus, String descripcio, double probabilitat) {
        this.tipus = tipus;
        this.descripcio = descripcio;
        this.probabilitat = probabilitat;
    }

    public TipusEsdeveniment getTipus() {
        return tipus;
    }

    public String getDescripcio() {
        return descripcio;
    }

    public double getProbabilitat() {
        return probabilitat;
    }

    /**
     * Aplica l'efecte al jugador (inventari controla els límits).
     */
    public void aplicar(Jugador jugador) {
        switch (tipus) {

            case OBTENIR_PEIX:
                jugador.afegirPeix();
                break;

            case OBTENIR_BOLES_NEU:
                // 1 a 3 boles de neu
                int q = 1 + (int) (Math.random() * 3);
                jugador.afegirBolesNeu(q);
                break;

            case OBTENIR_DAU_RAPID:
                jugador.afegirDauRapid();
                break;

            case OBTENIR_DAU_LENT:
                jugador.afegirDauLent();
                break;
        }
    }

    @Override
    public String toString() {
        return "Esdeveniment{" +
                "tipus=" + tipus +
                ", descripcio='" + descripcio + '\'' +
                ", probabilitat=" + probabilitat +
                '}';
    }
}