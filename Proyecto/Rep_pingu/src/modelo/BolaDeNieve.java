package modelo; // Esta clase pertenece al paquete "modelo"

// Clase BolaDeNieve que hereda de Item (es un tipo de objeto del juego)
public class BolaDeNieve extends Item {

    // Constructor: recibe el nombre del objeto y la cantidad disponible
    public BolaDeNieve(String nombre, int cantidad) {
        
        // Llama al constructor de la clase padre (Item)
        // para inicializar los atributos comunes (nombre y cantidad)
        super(nombre, cantidad);
    }
}