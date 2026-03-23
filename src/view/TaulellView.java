package view;

import model.Taulell;

public class TaulellView {

    public String mostrarTaulell(Taulell taulell) {

        return "Taulell amb " + taulell.getNumCaselles() + " caselles";
    }

    public String mostrarInfoCasella(int posicio) {
        return "Casella: " + posicio;
    }
}