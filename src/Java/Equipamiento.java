package Java;

public class Equipamiento {

    String nombre;
    String tipo;

    public Equipamiento(String nombre, String tipo) {
        this.nombre = nombre;
        this.tipo = tipo;
    }

    String getNombre() {
        return nombre;
    }

    String getTipo() {
        return tipo;
    }
}