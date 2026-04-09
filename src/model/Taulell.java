package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Taulell {

    private int numCaselles;
    private List<Casella> caselles;

    private static final Random random = new Random();

 
    private static final double P_OS = 0.08;
    private static final double P_FORAT = 0.12;
    private static final double P_TRINEU = 0.10;
    private static final double P_INTERROGANT = 0.14;

    public Taulell(int numCaselles) {
        if (numCaselles < 50) {
            throw new IllegalArgumentException("El taulell ha de tenir 50 caselles o més.");
        }
        this.numCaselles = numCaselles;
        this.caselles = new ArrayList<>();
        generarTaulellAleatori();
    }

    public Taulell() {
        this(50);
    }

    public int getNumCaselles() {
        return numCaselles;
    }

    public List<Casella> getCaselles() {
        return caselles;
    }

    

    public final void generarTaulellAleatori() {
        caselles.clear();

     
        for (int pos = 0; pos <= numCaselles; pos++) {
            caselles.add(null);
        }

       
        caselles.set(0, CasellaFactory.crearNormal(0, 0));
        caselles.set(numCaselles, CasellaFactory.crearNormal(numCaselles, numCaselles));

        for (int pos = 1; pos < numCaselles; pos++) {

            boolean zonaInicial = pos < 5;

            Casella casella = CasellaFactory.crearAleatoria(pos, zonaInicial, numCaselles);
            caselles.set(pos, casella);
        }

        assegurarMinimTrineus(2);
    }



    public Casella obtenirCasella(int posicio) {
        if (posicio < 0) posicio = 0;
        if (posicio > numCaselles) posicio = numCaselles;

        // 🔥 SEGUR → evitar null
        Casella c = caselles.get(posicio);
        if (c == null) {
            return CasellaFactory.crearNormal(posicio, posicio);
        }

        return c;
    }

  
    public Casella getCasella(int posicio) {
        return obtenirCasella(posicio);
    }


    public int buscarSeguentTrineu(int posicioActual) {

        if (posicioActual < 0) posicioActual = 0;
        if (posicioActual > numCaselles) posicioActual = numCaselles;

        for (int p = posicioActual + 1; p <= numCaselles; p++) {
            Casella c = caselles.get(p);
            if (c instanceof Casella_trineu) {
                return p;
            }
        }
        return posicioActual;
    }

    private void assegurarMinimTrineus(int minim) {

        int count = 0;

        for (Casella c : caselles) {
            if (c instanceof Casella_trineu) count++;
        }

        while (count < minim) {

            int pos = 1 + random.nextInt(numCaselles - 1);

            Casella actual = caselles.get(pos);

            if (!(actual instanceof Casella_Ós) &&
                !(actual instanceof Casella_forat)) {

                caselles.set(pos, CasellaFactory.crearTrineu(pos, pos));
                count++;
            }
        }
    }

    @Override
    public String toString() {
        return "Taulell{" +
                "numCaselles=" + numCaselles +
                ", caselles=" + caselles.size() +
                '}';
    }

    public static class CasellaFactory {

        private static int idSeq = 1;

        public static Casella crearAleatoria(int pos, boolean zonaInicial, int numCaselles) {

            double r = random.nextDouble();

            double pOs = zonaInicial ? P_OS / 2 : P_OS;
            double pForat = zonaInicial ? P_FORAT / 2 : P_FORAT;

            if (r < pOs) {
                return crearOs(idSeq++, pos);
            }
            r -= pOs;

            if (r < pForat) {
                int desti = (pos <= 1) ? 0 : random.nextInt(pos);
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