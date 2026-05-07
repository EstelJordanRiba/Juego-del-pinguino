package controlador;

import java.util.Random;
import modelo.*;

public class GestorJugador {
    // Generador de números aleatorios para los eventos de la casilla interrogante
    private Random random = new Random();

    /**
     * Intenta gastar un ítem del inventario del jugador.
     * @return true si el ítem existía y fue usado, false en caso contrario.
     */
    public boolean jugadorUsaItem(Pinguino jugador, String nombreItem) {
        return jugador.getInv().gastarItem(nombreItem, 1);
    }

    /**
     * Calcula y actualiza la nueva posición del jugador en el tablero.
     * Aplica "clamping" para evitar que el índice se salga del array del tablero.
     */
    public void jugadorSeMueve(Jugador j, int pasos, Tablero t) {
        int nuevaPos = j.getPosicion() + pasos;

        // Control de límites: no puede pasar de la última casilla (49)
        if (nuevaPos > 49) {
            nuevaPos = 49;
        }
        // No puede retroceder más allá de la casilla de salida (0)
        if (nuevaPos < 0) {
            nuevaPos = 0;
        }

        j.setPosicion(nuevaPos);
    }

    /**
     * Actualiza el estado de los turnos del jugador al finalizar.
     * Si el jugador estaba penalizado, consume un turno de la penalización.
     */
    public void jugadorFinalizaTurno(Jugador j) {
        if (j.debeSaltarTurno()) {
            j.consumirTurnoPerdido();
        }
    }

    /**
     * Lógica para la casilla de evento (interrogante).
     * Elige un destino al azar entre 6 opciones (0 a 5).
     */
    public void piguinoEvento(Pinguino p) {
        int evento = random.nextInt(6);
        switch (evento) {
            case 0: // Gana un pez
                p.añadirItem(new Pez("pez", 1));
                break;
            case 1: // Gana entre 1 y 3 bolas de nieve
                p.añadirItem(new BolaDeNieve("bola", random.nextInt(3) + 1));
                break;
            case 2: // Gana un dado rápido
                p.añadirItem(new Dado("rapido", 1, 5, 10));
                break;
            case 3: // Gana un dado lento
                p.añadirItem(new Dado("lento", 1, 1, 3));
                break;
            case 4: // Penalización: Pierde el siguiente turno
                p.perderTurno();
                break;
            default: // Caso 5: Pierde un objeto (primero intenta bola, si no pez)
                Item item = p.getInv().buscarPorNombre("bola");
                if (item == null) {
                    item = p.getInv().buscarPorNombre("pez");
                }
                if (item != null) {
                    item.restarCantidad(1);
                    p.getInv().eliminarVacios(); // Limpia el inventario si la cantidad llega a 0
                }
                break;
        }
    }

    /**
     * Activa el modo de combate cuando dos pingüinos coinciden en una casilla.
     */
    public String pingüinoGuerra(Pinguino p1, Pinguino p2) {
        return p1.gestionarBatalla(p2);
    }

    /**
     * Lógica de encuentro con la Foca.
     * Si el jugador tiene peces, puede sobornarla para bloquearla.
     * Si no tiene, la foca lo castiga enviándolo al agujero anterior.
     */
    public String focaInteractua(Pinguino p, Foca f, Tablero t) {
        // Intento de soborno
        if (p.getInv().gastarItem("pez", 1)) {
            f.setTurnosBloqueada(2); // La foca no se moverá durante 2 turnos
            f.setSoborno(true);
            return "FOCA_INTERACTUA: " + p.getNombre() + " sobornó a la foca con un pez.";
        } else {
            // Castigo: retroceso al agujero más cercano por detrás
            int agujeroAnterior = t.buscarAgujeroAnterior(p.getPosicion());
            p.setPosicion(agujeroAnterior);
            return "FOCA_INTERACTUA: ¡La foca empujó a " + p.getNombre() + " a un agujero!";
        }
    }
}