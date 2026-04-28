package modelo; // Indica que la clase pertenece al paquete "modelo"

import java.util.Random; // Importa la clase Random para generar números aleatorios

// Clase Dado que hereda de Item (es un tipo de objeto del juego)
public class Dado extends Item {

    // Atributos privados: valor mínimo y máximo del dado
    private int max;
    private int min;

    // Constructor: inicializa el nombre, cantidad y rango del dado
    public Dado(String nombre, int cantidad, int min, int max) {
        super(nombre, cantidad); // Llama al constructor de Item
        this.min = min;
        this.max = max;
    }

    // Getter del valor máximo
    public int getMax() {
        return max;
    }

    // Setter del valor máximo
    public void setMax(int max) {
        this.max = max;
    }

    // Getter del valor mínimo
    public int getMin() {
        return min;
    }

    // Setter del valor mínimo
    public void setMin(int min) {
        this.min = min;
    }

    // Método para "tirar" el dado
    // Recibe un objeto Random y devuelve un número aleatorio entre min y max (incluidos)
    public int tirar(Random r) {
        return r.nextInt((max - min) + 1) + min;
    }
}