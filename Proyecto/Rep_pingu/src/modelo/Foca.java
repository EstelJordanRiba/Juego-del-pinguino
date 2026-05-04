package modelo;

public class Foca extends Jugador {

    private boolean soborno;
    private int turnosBloqueada;

    public Foca(String nombre, String color, int posicion) {
        super(nombre, color, posicion);
        this.soborno = false;
        this.turnosBloqueada = 0;
    }

    public boolean isSoborno() {
        return soborno;
    }

    public void setSoborno(boolean soborno) {
        this.soborno = soborno;
    }

    public int getTurnosBloqueada() {
        return turnosBloqueada;
    }

    public void setTurnosBloqueada(int turnosBloqueada) {
        this.turnosBloqueada = turnosBloqueada;
    }

    public void aplastarJugador(Pinguino p) {
        int mitad = p.getInv().totalObjetos() / 2;

        while (mitad > 0) {
            if (p.getInv().gastarItem("bola", 1)) {
                mitad--;
            } else if (p.getInv().gastarItem("pez", 1)) {
                mitad--;
            } else if (p.getInv().gastarItem("rapido", 1)) {
                mitad--;
            } else if (p.getInv().gastarItem("lento", 1)) {
                mitad--;
            } else if (p.getInv().gastarItem("normal", 1)) {
                mitad--;
            } else {
                mitad = 0;
            }
        }

        p.getInv().eliminarVacios();
    }

    public boolean esSobornado() {
        return soborno;
    }
}