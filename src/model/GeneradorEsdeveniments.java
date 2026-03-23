package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeneradorEsdeveniments {

    private final List<Esdeveniment> esdeveniments;
    private final Random random = new Random();

    public GeneradorEsdeveniments() {

        esdeveniments = new ArrayList<>();

        // 🎯 Probabilitats equilibrades

        esdeveniments.add(new Esdeveniment(
                Esdeveniment.TipusEsdeveniment.OBTENIR_PEIX,
                "🐟 Has trobat un peix",
                0.25
        ));

        esdeveniments.add(new Esdeveniment(
                Esdeveniment.TipusEsdeveniment.OBTENIR_BOLES_NEU,
                "❄ Aconsegueixes boles de neu",
                0.45
        ));

        esdeveniments.add(new Esdeveniment(
                Esdeveniment.TipusEsdeveniment.OBTENIR_DAU_LENT,
                "🐢 Has obtingut un dau lent",
                0.25
        ));

        esdeveniments.add(new Esdeveniment(
                Esdeveniment.TipusEsdeveniment.OBTENIR_DAU_RAPID,
                "⚡ Has obtingut un dau ràpid",
                0.05
        ));
    }

    // 🎲 GENERADOR ALEATORI AMB PESOS
    public Esdeveniment generarAleatori() {

        if (esdeveniments.isEmpty()) {
            throw new IllegalStateException("No hi ha esdeveniments definits.");
        }

        double total = 0.0;
        for (Esdeveniment e : esdeveniments) {
            total += e.getProbabilitat();
        }

        double r = random.nextDouble() * total;
        double acumulat = 0.0;

        for (Esdeveniment e : esdeveniments) {
            acumulat += e.getProbabilitat();
            if (r <= acumulat) {
                return e;
            }
        }

        // fallback (no hauria de passar)
        return esdeveniments.get(esdeveniments.size() - 1);
    }
}