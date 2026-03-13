package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
public class Generador_esdeveniments {


    private final List<Esdeveniment> esdeveniments;
    private final Random random = new Random();

    public Generador_esdeveniments() {
        esdeveniments = new ArrayList<>();

        // Pesos/probabilitats (exemple coherent amb el document: ràpid baixa, lent alta) :contentReference[oaicite:1]{index=1}
        esdeveniments.add(new Esdeveniment(
                Esdeveniment.TipusEsdeveniment.OBTENIR_PEIX,
                "Has encontrado un pescado ",
                0.25
        ));

        esdeveniments.add(new Esdeveniment(
                Esdeveniment.TipusEsdeveniment.OBTENIR_BOLES_NEU,
                "Adquieres bolas de nieve ",
                0.45
        ));

        esdeveniments.add(new Esdeveniment(
                Esdeveniment.TipusEsdeveniment.OBTENIR_DAU_LENT,
                "Conseguiste un dado muy lentoooo ",
                0.25
        ));

        esdeveniments.add(new Esdeveniment(
                Esdeveniment.TipusEsdeveniment.OBTENIR_DAU_RAPID,
                "Conisguiste un rapido mas rapido que flash ",
                0.05
        ));
    }

    public Esdeveniment generarAleatori() {
        double total = 0.0;
        for (Esdeveniment e : esdeveniments) total += e.getProbabilitat();

        double r = random.nextDouble() * total;
        double acumulat = 0.0;

        for (Esdeveniment e : esdeveniments) {
            acumulat += e.getProbabilitat();
            if (r <= acumulat) return e;
        }

        return esdeveniments.get(esdeveniments.size() - 1);
    }
}