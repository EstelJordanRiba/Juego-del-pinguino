package modelo;

import java.io.Serializable;

public abstract class Jugador implements Serializable {
    private static final long serialVersionUID = 1L;

    private int posicion;
    private String nombre;
    private String color;
    private int turnosPerdidos;
    private Inventario inventario;

    public Jugador(String nombre, String color, int posicion) {
        this.posicion = posicion;
        this.nombre = nombre;
        this.color = color;
        this.turnosPerdidos = 0;
        this.inventario = new Inventario();
    }

    public int getPosicion() {
        return posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getTurnosPerdidos() {
        return turnosPerdidos;
    }

    public void setTurnosPerdidos(int turnosPerdidos) {
        this.turnosPerdidos = turnosPerdidos;
    }

    public void perderTurno() {
        this.turnosPerdidos++;
    }

    public boolean debeSaltarTurno() {
        return turnosPerdidos > 0;
    }

    public void consumirTurnoPerdido() {
        if (turnosPerdidos > 0) {
            turnosPerdidos--;
        }
    }

    public void moverPosicion(int pasos) {
        this.posicion += pasos;
        if (this.posicion < 0) {
            this.posicion = 0;
        }
        if (this.posicion > 49) {
            this.posicion = 49;
        }
    }

    public Inventario getInventario() {
        return inventario;
    }

    public void setInventario(Inventario inventario) {
        this.inventario = inventario;
    }
}