package Java;


public class Personaje {

    String nombre;
    int nivel;
    int puntosVida;
    boolean protegido;
    Inventario inventario;

    public Personaje(String nombre, int nivel, int puntosVida) {
        this.nombre = nombre;
        this.nivel = nivel;
        this.puntosVida = puntosVida;
        this.protegido = false;
        this.inventario = new Inventario();
    }

    void mostrarInfo() {
        System.out.println(nombre + " | Nivel: " + nivel + " | Vida: " + puntosVida);
    }

    void bajarVida(int cantidad) {
        if (protegido) {
            cantidad = cantidad / 2;
        }
        puntosVida = puntosVida - cantidad;
        System.out.println(nombre + " pierde " + cantidad + " de vida");
    }

    void curar(int cantidad) {
        puntosVida = puntosVida + cantidad;
    }

    Inventario getInventario() {
        return inventario;
    }

    void agregarEquipamiento(Equipamiento e) {
        inventario.agregarEquipamiento(e);
    }

    void setProtegido(boolean protegido) {
        this.protegido = protegido;
    }

    boolean estaProtegido() {
        return protegido;
    }
}