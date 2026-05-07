package modelo;

/**
 * Representa una casilla especial que reacciona según el inventario del jugador.
 * Hereda de Casilla para integrarse en el tablero.
 */
public class SueloQuebradizo extends Casilla {

    /**
     * Constructor: Ubica la casilla en una posición del tablero.
     * @param posicion Índice de la casilla (0-49).
     */
    public SueloQuebradizo(int posicion) {
        super(posicion);
    }

    /**
     * Ejecuta la acción cuando alguien aterriza en la casilla.
     * Solo afecta a objetos de tipo Pinguino (la Foca es inmune o no tiene inventario).
     */
    @Override
    public void realizarAccion(Jugador jugador) {
        if (jugador instanceof Pinguino) {
            // Casting: Convertimos la referencia Jugador a Pinguino para usar sus métodos
            String resultado = aplicarEfecto((Pinguino) jugador);
            System.out.println(resultado);
        }
    }

    /**
     * Lógica de penalización basada en el inventario.
     * @param p El pingüino que ha caído en la casilla.
     * @return Un mensaje describiendo qué ha ocurrido.
     */
    public String aplicarEfecto(Pinguino p) {
        // Obtenemos la suma total de todos los objetos en el inventario
        int cantidad = p.getInv().totalObjetos();

        // REGLA 1: Exceso de peso (Más de 5 objetos)
        if (cantidad > 5) {
            p.setPosicion(0); // El hielo se rompe y el jugador vuelve a la salida
            return p.getNombre() + " cae por el suelo quebradizo y vuelve al inicio.";

        // REGLA 2: Peso moderado (Entre 1 y 5 objetos)
        } else if (cantidad > 0 && cantidad <= 5) {
            p.perderTurno(); // El hielo cruje y el jugador queda atrapado un turno
            return p.getNombre() + " pierde un turno por el suelo quebradizo.";

        // REGLA 3: Sin peso (Inventario vacío)
        } else {
            // El jugador pasa con seguridad
            return p.getNombre() + " pasa sin penalización por el suelo quebradizo.";
        }
    }
}