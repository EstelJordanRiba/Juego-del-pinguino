package Pingu;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Taulell {

    private int numCaselles;               // 50 o més (document) :contentReference[oaicite:1]{index=1}
    private List<Casella> llistaCaselles;  // index = posicio

    private static final Random random = new Random();

    // Percentatges orientatius (ajustables)
    private static final double P_OS = 0.08;
    private static final double P_FORAT = 0.12;
    private static final double P_TRINEU = 0.10;
    private static final double P_INTERROGANT = 0.14;
    // La resta -> normal

    public Taulell(int numCaselles) {
        if (numCaselles < 50) {
            throw new IllegalArgumentException("El taulell ha de tenir 50 caselles o més.");
        }
        this.numCaselles = numCaselles;
        this.llistaCaselles = new ArrayList<>(numCaselles + 1);
        generarTaulellAleatori();
    }

    public Taulell() {
        // per defecte
        this(50);
    }

    public int getNumCaselles() {
        return numCaselles;
    }

    public List<Casella> getLlistaCaselles() {
        return llistaCaselles;
    }

    /**
     * Genera un taulell aleatori de 0..numCaselles (incloent 0).
     * - posició 0: normal (inici)
     * - última posició: normal (final)
     */
    public final void generarTaulellAleatori() {
        llistaCaselles.clear();

        // Reservem posicions 0..numCaselles
        for (int pos = 0; pos <= numCaselles; pos++) {
            llistaCaselles.add(null);
        }

        // Inici i final sempre normals
        llistaCaselles.set(0, CasellaFactory.crearNormal(0, 0));
        llistaCaselles.set(numCaselles, CasellaFactory.crearNormal(numCaselles, numCaselles));

        // Omplir la resta
        for (int pos = 1; pos < numCaselles; pos++) {

            // Evitem que hi hagi forats massa “besties” a l’inici
            boolean zonaInicial = pos < 5;

            Casella casella = CasellaFactory.crearAleatoria(pos, zonaInicial, numCaselles);
            llistaCaselles.set(pos, casella);
        }

        // Petita garantia: mínim 2 trineus (si no, buscarSeguentTrineu no té gràcia)
        assegurarMinimTrineus(2);
    }

    /**
     * Retorna la casella de la posició.
     */
    public Casella obtenirCasella(int posicio) {
        if (posicio < 0) posicio = 0;
        if (posicio > numCaselles) posicio = numCaselles;
        return llistaCaselles.get(posicio);
    }

    /**
     * Busca el següent trineu des de la posicióActual.
     * Si no n’hi ha, retorna la mateixa posició (no es mou).
     */
    public int buscarSeguentTrineu(int posicioActual) {
        if (posicioActual < 0) posicioActual = 0;
        if (posicioActual > numCaselles) posicioActual = numCaselles;

        for (int p = posicioActual + 1; p <= numCaselles; p++) {
            Casella c = llistaCaselles.get(p);
            if (c instanceof Casella_trineu) {
                return p;
            }
        }
        return posicioActual;
    }

    private void assegurarMinimTrineus(int minim) {
        int count = 0;
        for (Casella c : llistaCaselles) {
            if (c instanceof Casella_trineu) count++;
        }

        while (count < minim) {
            int pos = 1 + random.nextInt(numCaselles - 1); // evita 0 i final
            // no sobrescrivim forats “crítics” si no vols; aquí ho fem simple:
            llistaCaselles.set(pos, CasellaFactory.crearTrineu(pos, pos));
            count++;
        }
    }

    @Override
    public String toString() {
        return "Taulell{" +
                "numCaselles=" + numCaselles +
                ", caselles=" + (llistaCaselles.size()) +
                '}';
    }

    // ==========================
    // FACTORY (nota alta)
    // ==========================
    public static class CasellaFactory {

        private static int idSeq = 1; // id incremental simple (després BD)

        public static Casella crearAleatoria(int pos, boolean zonaInicial, int numCaselles) {

            double r = random.nextDouble();

            // A la zona inicial, reduïm forats/ós
            double pOs = zonaInicial ? P_OS / 2 : P_OS;
            double pForat = zonaInicial ? P_FORAT / 2 : P_FORAT;

            if (r < pOs) {
                return crearOs(idSeq++, pos);
            }
            r -= pOs;

            if (r < pForat) {
                // destí: "forat d’abans" (document) :contentReference[oaicite:2]{index=2}
                // Triem un destí anterior raonable: entre 0 i pos-1
                int desti = (pos <= 1) ? 0 : random.nextInt(pos); // [0..pos-1]
                return crearForat(idSeq++, pos, desti);
            }
            r -= pForat;

            if (r < P_TRINEU) {
                return crearTrineu(idSeq++, pos);
            }
            r -= P_TRINEU;

            if (r < P_INTERROGANT) {
                return crearInterrogant(idSeq++, pos);
            }

            return crearNormal(idSeq++, pos);
        }

        public static Casella crearOs(int id, int pos) {
            return new Casella_Ós(id, pos);
        }

        public static Casella crearForat(int id, int pos, int desti) {
            return new Casella_forat(id, pos, desti);
        }

        public static Casella crearTrineu(int id, int pos) {
            return new Casella_trineu(id, pos);
        }

        public static Casella crearInterrogant(int id, int pos) {
            return new Casella_interrogatiu(id, pos);
        }

        public static Casella crearNormal(int id, int pos) {
            return new Casella_normal(id, pos);
        }
    }
}