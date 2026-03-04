package Pingu;

public class Inventari {

    // CONSTANTS DE LÍMIT
  

    private static final int MAX_DAUS_TOTAL = 3;
    private static final int MAX_PEIXOS = 2;
    private static final int MAX_BOLES_NEU = 6;

    // ATRIBUTS
    

    private int dausNormals;
    private int dausRapids;
    private int dausLents;
    private int peixos;
    private int bolesNeu;

    // CONSTRUCTOR
   

    public Inventari() {
        this.dausNormals = 0;
        this.dausRapids = 0;
        this.dausLents = 0;
        this.peixos = 0;
        this.bolesNeu = 0;
    }

    // GETTERS

    public int getDausNormals() {
        return dausNormals;
    }

    public int getDausRapids() {
        return dausRapids;
    }

    public int getDausLents() {
        return dausLents;
    }

    public int getPeixos() {
        return peixos;
    }

    public int getBolesNeu() {
        return bolesNeu;
    }

    public int getTotalDaus() {
        return dausNormals + dausRapids + dausLents;
    }


    // AFEGIR ITEMS

    public boolean afegirPeix() {
        if (peixos < MAX_PEIXOS) {
            peixos++;
            return true;
        }
        return false;
    }

    public boolean afegirBolesNeu(int quantitat) {
        if (quantitat <= 0) return false;

        int novaQuantitat = bolesNeu + quantitat;
        if (novaQuantitat > MAX_BOLES_NEU) {
            bolesNeu = MAX_BOLES_NEU;
        } else {
            bolesNeu = novaQuantitat;
        }
        return true;
    }

    public boolean afegirDauNormal() {
        if (getTotalDaus() < MAX_DAUS_TOTAL) {
            dausNormals++;
            return true;
        }
        return false;
    }

    public boolean afegirDauRapid() {
        if (getTotalDaus() < MAX_DAUS_TOTAL) {
            dausRapids++;
            return true;
        }
        return false;
    }

    public boolean afegirDauLent() {
        if (getTotalDaus() < MAX_DAUS_TOTAL) {
            dausLents++;
            return true;
        }
        return false;
    }

    
    // GASTAR ITEMS
   
    public boolean gastarPeix() {
        if (peixos > 0) {
            peixos--;
            return true;
        }
        return false;
    }

    public boolean gastarBolaNeu() {
        if (bolesNeu > 0) {
            bolesNeu--;
            return true;
        }
        return false;
    }

    public boolean gastarDauNormal() {
        if (dausNormals > 0) {
            dausNormals--;
            return true;
        }
        return false;
    }

    public boolean gastarDauRapid() {
        if (dausRapids > 0) {
            dausRapids--;
            return true;
        }
        return false;
    }

    public boolean gastarDauLent() {
        if (dausLents > 0) {
            dausLents--;
            return true;
        }
        return false;
    }

    // UTILITATS

    public boolean teBolesNeu() {
        return bolesNeu > 0;
    }

    public boolean tePeixos() {
        return peixos > 0;
    }

    public boolean teDaus() {
        return getTotalDaus() > 0;
    }

    @Override
    public String toString() {
        return "Inventari{" +
                "dausNormals=" + dausNormals +
                ", dausRapids=" + dausRapids +
                ", dausLents=" + dausLents +
                ", peixos=" + peixos +
                ", bolesNeu=" + bolesNeu +
                '}';
    }
}