package controlador;

import modelo.*;

public class GestorTablero {

    /**
     * Este método es el "árbitro" de las casillas. 
     * Se activa cuando un pingüino aterriza en una casilla y decide qué le pasa 
     * según el tipo de objeto (herencia) que sea la casilla.
     */
    public String ejecutarCasilla(Partida partida, Pinguino jugador, Casilla casilla) {
        String mensaje = "";

        // CASO 1: La casilla es un Oso
        if (casilla instanceof Oso) {
            mensaje = "OSO_ATAQUE"; // Solo marca el evento, la lógica de daño suele ir en el controlador superior

        // CASO 2: La casilla es un Agujero
        } else if (casilla instanceof Agujero) {
            // Busca en el tablero el agujero anterior más cercano
            int destino = partida.getTablero().buscarAgujeroAnterior(jugador.getPosicion());
            jugador.setPosicion(destino); // Teletransporta al jugador hacia atrás
            mensaje = jugador.getNombre() + " cae en un agujero y retrocede a la casilla " + destino + ".";

        // CASO 3: La casilla es un Trineo (Atajo)
        } else if (casilla instanceof Trineo) {
            // Busca el siguiente trineo para avanzar
            int destino = partida.getTablero().buscarSiguienteTrineo(jugador.getPosicion());
            if (destino != jugador.getPosicion()) {
                jugador.setPosicion(destino); // El jugador avanza posiciones
                mensaje = jugador.getNombre() + " usa un trineo y avanza hasta la casilla " + destino + ".";
            } else {
                mensaje = jugador.getNombre() + " cae en el último trineo y no avanza más.";
            }

        // CASO 4: Casilla de Evento (Interrogante)
        } else if (casilla instanceof Evento) {
            // Genera un número aleatorio del 0 al 5 para decidir qué premio o castigo dar
            int evento = (int) (Math.random() * 6);

            switch (evento) {
                case 0: // Regalo: Pez
                    jugador.añadirItem(new Pez("pez", 1));
                    mensaje = jugador.getNombre() + " consigue 1 pez.";
                    break;
                case 1: // Regalo: Bolas de nieve (de 1 a 3)
                    int bolas = (int) (Math.random() * 3) + 1;
                    jugador.añadirItem(new BolaDeNieve("bola", bolas));
                    mensaje = jugador.getNombre() + " consigue " + bolas + " bolas de nieve.";
                    break;
                case 2: // Regalo: Dado de alta velocidad
                    jugador.añadirItem(new Dado("rapido", 1, 5, 10));
                    mensaje = jugador.getNombre() + " consigue un dado rápido.";
                    break;
                case 3: // Regalo: Dado de baja velocidad
                    jugador.añadirItem(new Dado("lento", 1, 1, 3));
                    mensaje = jugador.getNombre() + " consigue un dado lento.";
                    break;
                case 4: // Castigo: Pierde el turno
                    jugador.perderTurno();
                    mensaje = jugador.getNombre() + " pierde un turno.";
                    break;
                default: // Caso 5: Castigo - Perder un ítem aleatorio
                    // Busca por prioridad qué quitarle
                    Item item = jugador.getInv().buscarPorNombre("bola");
                    if (item == null) item = jugador.getInv().buscarPorNombre("pez");
                    if (item == null) item = jugador.getInv().buscarPorNombre("rapido");
                    if (item == null) item = jugador.getInv().buscarPorNombre("lento");

                    if (item != null) { //Solo funciona si item no es null
                        item.restarCantidad(1);
                        jugador.getInv().eliminarVacios(); // Limpia el inventario si el objeto llegó a 0
                        mensaje = jugador.getNombre() + " pierde 1 objeto del inventario.";
                    } else {
                        mensaje = jugador.getNombre() + " no tenía objetos que perder.";
                    }
                    break;
            }

        // CASO 5: Suelo Quebradizo (Hielo que se rompe)
        } else if (casilla instanceof SueloQuebradizo) {
            // Llama al método propio de la clase SueloQuebradizo para gestionar su rotura
            mensaje = ((SueloQuebradizo) casilla).aplicarEfecto(jugador);

        // CASO 6: Casilla blanca o normal
        } else {
            mensaje = jugador.getNombre() + " cae en una casilla normal.";
        }

        // Actualiza el estado de la partida con el texto de lo ocurrido
        partida.setUltimoEvento(mensaje);
        return mensaje;
    }
}