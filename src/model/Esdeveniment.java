package model;

public class Esdeveniment {

    public enum TipusEsdeveniment {
        OBTENIR_PEIX,
        OBTENIR_BOLES_NEU,
        OBTENIR_DAU_RAPID,
        OBTENIR_DAU_LENT
    }

    private TipusEsdeveniment tipus;
    private String descripcio;
    private double probabilitat;

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

    public void aplicar(Jugador jugador, Partida partida) {

        switch (tipus) {

            case OBTENIR_PEIX:
                jugador.afegirPeix();
                partida.getHistorialAccions().add(
                        jugador.getNickname() + " obté un peix "
                );
                break;

            case OBTENIR_BOLES_NEU:
                int q = 1 + (int) (Math.random() * 3);
                jugador.afegirBolesNeu(q);
                partida.getHistorialAccions().add(
                        jugador.getNickname() + " obté " + q + " boles de neu "
                );
                break;

            case OBTENIR_DAU_RAPID:
                jugador.afegirDauRapid();
                partida.getHistorialAccions().add(
                        jugador.getNickname() + " obté un dau ràpid "
                );
                break;

            case OBTENIR_DAU_LENT:
                jugador.afegirDauLent();
                partida.getHistorialAccions().add(
                        jugador.getNickname() + " obté un dau lent "
                );
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