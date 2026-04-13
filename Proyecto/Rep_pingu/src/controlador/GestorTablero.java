package controlador;

import modelo.*;

public class GestorTablero {
    public String ejecutarCasilla(Partida partida, Pinguino p, Casilla c) {
        String mensaje = p.getNombre() + " ha caído en una casilla normal.";
        Tablero tablero = partida.getTablero();

        if (c instanceof Oso) {
            if (p.getInv().gastarItem("pez", 1)) {
                mensaje = p.getNombre() + " ha usado un pez y ha evitado al oso.";
            } else {
                p.setPosicion(0);
                mensaje = p.getNombre() + " ha sido atacado por el oso y vuelve al inicio.";
            }
        } else if (c instanceof Agujero) {
            int nuevaPos = tablero.buscarAgujeroAnterior(c.getPosicion());
            p.setPosicion(nuevaPos);
            mensaje = p.getNombre() + " ha caído en un agujero y retrocede a la casilla " + nuevaPos + ".";
        } else if (c instanceof Trineo) {
            int nuevaPos = tablero.buscarSiguienteTrineo(c.getPosicion());
            p.setPosicion(nuevaPos);
            mensaje = p.getNombre() + " usa un trineo y avanza hasta la casilla " + nuevaPos + ".";
        } else if (c instanceof Evento) {
            GestorJugador gestorJugador = new GestorJugador();
            gestorJugador.piguinoEvento(p);
            mensaje = p.getNombre() + " ha activado una casilla de evento.";
        } else if (c instanceof SueloQuebradizo) {
            int total = p.getInv().totalObjetos();
            if (total > 5) {
                p.setPosicion(0);
                mensaje = p.getNombre() + " cae por llevar demasiado inventario y vuelve al inicio.";
            } else if (total > 0) {
                p.perderTurno();
                mensaje = p.getNombre() + " pisa hielo quebradizo y pierde el siguiente turno.";
            } else {
                mensaje = p.getNombre() + " cruza el hielo quebradizo sin problemas.";
            }
        }

        partida.setUltimoEvento(mensaje);
        return mensaje;
    }

    public void comprobarFinTurno(Partida partida) {
        partida.comprobarGanador();
    }
}
