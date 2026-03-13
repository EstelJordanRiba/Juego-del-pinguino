package model;
import java.util.Random;

public class Dau {

    public enum TipusDau {
        NORMAL,
        RAPID,
        LENT
    }

    private int valorMinim;
    private int valorMaxim;
    private double probabilitat; // 0.0 a 1.0
    private TipusDau tipus;

    private static final Random random = new Random();

   
    // CONSTRUCTOR PRIVAT

    private Dau(int valorMinim, int valorMaxim, double probabilitat, TipusDau tipus) {
        this.valorMinim = valorMinim;
        this.valorMaxim = valorMaxim;
        this.probabilitat = probabilitat;
        this.tipus = tipus;
    }

   
    // FACTORY METHOD

    public static Dau crearDau(TipusDau tipus) {
        switch (tipus) {
            case RAPID:
                return new Dau(5, 10, 0.25, TipusDau.RAPID); // baixa probabilitat
            case LENT:
                return new Dau(1, 3, 0.80, TipusDau.LENT); // alta probabilitat
            case NORMAL:
            default:
                return new Dau(1, 6, 1.0, TipusDau.NORMAL);
        }
    }

   
    // LÒGICA PRINCIPAL

    public int tirar() {

        // Comprovem probabilitat (si no passa → 0 moviment)
        if (random.nextDouble() > probabilitat) {
            return 0;
        }

        return random.nextInt(valorMaxim - valorMinim + 1) + valorMinim;
    }

    // GETTERS
 
    public TipusDau getTipus() {
        return tipus;
    }

    public int getValorMinim() {
        return valorMinim;
    }

    public int getValorMaxim() {
        return valorMaxim;
    }

    public double getProbabilitat() {
        return probabilitat;
    }

    @Override
    public String toString() {
        return "Dau{" +
                "tipus=" + tipus +
                ", rang=" + valorMinim + "-" + valorMaxim +
                ", probabilitat=" + probabilitat +
                '}';
    }
}