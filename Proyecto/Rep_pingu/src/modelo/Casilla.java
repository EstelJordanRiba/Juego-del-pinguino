package modelo;

public abstract class Casilla {

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

    public abstract void realizarAccion(Jugador jugador);
}