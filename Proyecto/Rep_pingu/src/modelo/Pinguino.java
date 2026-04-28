package modelo;

public class Pinguino extends Jugador {

    private Inventario inv;

    public Pinguino(String nombre, String color, int posicion, Inventario inventario) {
        super(nombre, color, posicion);
        this.inv = inventario;
    }

    public Inventario getInv() {
        return inv;
    }

    public void setInv(Inventario inv) {
        this.inv = inv;
    }

    public String gestionarBatalla(Pinguino rival) {
        int bolasPropias = inv.getCantidad("bola");
        int bolasRival = rival.getInv().getCantidad("bola");

        inv.gastarItem("bola", bolasPropias);
        rival.getInv().gastarItem("bola", bolasRival);

        String msj = "GUERRA_NIEVE: " + this.getNombre() + " (" + bolasPropias + " bolas) vs " + 
                     rival.getNombre() + " (" + bolasRival + " bolas). ";

        if (bolasPropias > bolasRival) {
            rival.moverPosicion(-(bolasPropias - bolasRival));
            msj += rival.getNombre() + " retrocede " + (bolasPropias - bolasRival) + " casillas.";
        } else if (bolasRival > bolasPropias) {
            this.moverPosicion(-(bolasRival - bolasPropias));
            msj += this.getNombre() + " retrocede " + (bolasRival - bolasPropias) + " casillas.";
        } else {
            msj += "¡Empate! Nadie retrocede.";
        }
        return msj;
    }

    public boolean usarItem(Item i) {
        return inv.gastarItem(i.getNombre(), 1);
    }

    public void añadirItem(Item i) {
        String nombre = i.getNombre().toLowerCase();
        int maximo = 99;

        if (nombre.equals("normal") || nombre.equals("rapido") || nombre.equals("lento")) {
            maximo = 3;
        } else if (nombre.equals("pez")) {
            maximo = 2;
        } else if (nombre.equals("bola")) {
            maximo = 6;
        }

        inv.añadirOActualizar(i, maximo);
    }

    public void quitarItem(Item i) {
        inv.gastarItem(i.getNombre(), i.getCantidad());
        inv.eliminarVacios();
    }
}