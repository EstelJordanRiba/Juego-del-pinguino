package modelo;

import java.io.Serializable;

public abstract class Casilla implements Serializable {
    private static final long serialVersionUID = 1L;

    protected int posicion;

    public Casilla(int posicion) {
        this.posicion = posicion;
    }

    public int getPosicion() {
        return posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    public abstract void realizarAccion();
}
