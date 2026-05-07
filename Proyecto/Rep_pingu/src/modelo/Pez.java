package modelo;

/**
 * Clase que representa el objeto Pez.
 * Al extender de Item, hereda los atributos 'nombre' y 'cantidad', 
 * así como los métodos para sumar o restar unidades.
 */
public class Pez extends Item {

    /**
     * Constructor: Crea un objeto de tipo Pez.
     * @param nombre El identificador del objeto (normalmente "pez").
     * @param cantidad Cuántas unidades de pez se añaden de golpe.
     */
    public Pez(String nombre, int cantidad) {
        // Llama al constructor de la clase padre (Item)
        super(nombre, cantidad);
    }
}