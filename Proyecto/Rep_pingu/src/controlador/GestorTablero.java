package controlador;

import modelo.*;

public class GestorTablero {

    public String ejecutarCasilla(Partida partida, Pinguino jugador, Casilla casilla) {
        String mensaje = "";

        if (casilla instanceof Oso) {
            mensaje = "OSO_ATAQUE";

        } else if (casilla instanceof Agujero) {
            int destino = partida.getTablero().buscarAgujeroAnterior(jugador.getPosicion());
            jugador.setPosicion(destino);
            mensaje = jugador.getNombre() + " cae en un agujero y retrocede a la casilla " + destino + ".";

        } else if (casilla instanceof Trineo) {
            int destino = partida.getTablero().buscarSiguienteTrineo(jugador.getPosicion());
            if (destino != jugador.getPosicion()) {
                jugador.setPosicion(destino);
                mensaje = jugador.getNombre() + " usa un trineo y avanza hasta la casilla " + destino + ".";
            } else {
                mensaje = jugador.getNombre() + " cae en el último trineo y no avanza más.";
            }

        } else if (casilla instanceof Evento) {
            int evento = (int) (Math.random() * 6);

            switch (evento) {
                case 0:
                    jugador.añadirItem(new Pez("pez", 1));
                    mensaje = jugador.getNombre() + " consigue 1 pez.";
                    break;
                case 1:
                    int bolas = (int) (Math.random() * 3) + 1;
                    jugador.añadirItem(new BolaDeNieve("bola", bolas));
                    mensaje = jugador.getNombre() + " consigue " + bolas + " bolas de nieve.";
                    break;
                case 2:
                    jugador.añadirItem(new Dado("rapido", 1, 5, 10));
                    mensaje = jugador.getNombre() + " consigue un dado rápido.";
                    break;
                case 3:
                    jugador.añadirItem(new Dado("lento", 1, 1, 3));
                    mensaje = jugador.getNombre() + " consigue un dado lento.";
                    break;
                case 4:
                    jugador.perderTurno();
                    mensaje = jugador.getNombre() + " pierde un turno.";
                    break;
                default:
                    Item item = jugador.getInv().buscarPorNombre("bola");
                    if (item == null) {
                        item = jugador.getInv().buscarPorNombre("pez");
                    }
                    if (item == null) {
                        item = jugador.getInv().buscarPorNombre("rapido");
                    }
                    if (item == null) {
                        item = jugador.getInv().buscarPorNombre("lento");
                    }

                    if (item != null) {
                        item.restarCantidad(1);
                        jugador.getInv().eliminarVacios();
                        mensaje = jugador.getNombre() + " pierde 1 objeto del inventario.";
                    } else {
                        mensaje = jugador.getNombre() + " no tenía objetos que perder.";
                    }
                    break;
            }

        } else if (casilla instanceof SueloQuebradizo) {
            mensaje = ((SueloQuebradizo) casilla).aplicarEfecto(jugador);

        } else {
            mensaje = jugador.getNombre() + " cae en una casilla normal.";
        }

        partida.setUltimoEvento(mensaje);
        return mensaje;
    }
}