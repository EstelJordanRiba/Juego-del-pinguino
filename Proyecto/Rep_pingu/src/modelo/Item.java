package modelo; // Pertenece al paquete "modelo"

// Clase abstracta Item: representa un objeto del juego
// Es abstracta porque sirve como base para otros tipos (BolaDeNieve, Dado, etc.)
public abstract class Item {

    // Nombre del objeto (ej: "bola", "pez", etc.)
    private String nombre;

    // Cantidad de ese objeto que tiene el jugador
    private int cantidad;

    // Constructor: inicializa el nombre y la cantidad
    public Item(String nombre, int cantidad) {
        this.nombre = nombre;
        this.cantidad = cantidad;
    }

    // Getter del nombre
    public String getNombre() {
        return nombre;
    }

    // Setter del nombre
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // Getter de la cantidad
    public int getCantidad() {
        return cantidad;
    }

    // Setter de la cantidad
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    // Método para aumentar la cantidad de objetos
    public void sumarCantidad(int valor) {
        this.cantidad += valor;
    }

    // Método para reducir la cantidad de objetos
    public void restarCantidad(int valor) {
        this.cantidad -= valor;

        // Evita que la cantidad sea negativa
        if (this.cantidad < 0) {
            this.cantidad = 0;
        }
    }
}