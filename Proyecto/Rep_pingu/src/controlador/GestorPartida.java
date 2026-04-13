package controlador;

import java.util.ArrayList;
import java.util.Random;

import modelo.*;

public class GestorPartida {
    private Partida partida;
    private GestorTablero gestorTablero;
    private GestorJugador gestorJugador;
    private GestorBBDD gestorBBDD;
    private Random random;

    public GestorPartida() {
        this.partida = null;
        this.gestorTablero = new GestorTablero();
        this.gestorJugador = new GestorJugador();
        this.gestorBBDD = new GestorBBDD();
        this.random = new Random();
    }

    public void nuevaPartida() {
        this.partida = new Partida();
        ArrayList<Jugador> jugadores = new ArrayList<Jugador>();

        Inventario inv1 = new Inventario();
        inv1.añadirOActualizar(new Dado("normal", 1, 1, 6), 3);
        jugadores.add(new Pinguino("Jugador 1", "Azul", 0, inv1));

        Inventario inv2 = new Inventario();
        inv2.añadirOActualizar(new Dado("normal", 1, 1, 6), 3);
        jugadores.add(new Pinguino("Jugador 2", "Rojo", 0, inv2));

        this.partida.setJugadores(jugadores);
        this.partida.setJugadorActualIndice(0);
        this.partida.setUltimoEvento("Nueva partida preparada.");
    }

    public int tirarDado(Jugador j, Dado dadoOpcional) {
        int resultado = dadoOpcional.tirar(random);
        gestorJugador.jugadorSeMueve(j, resultado, this.partida.getTablero());
        return resultado;
    }

    public String jugarTurnoConDado(Dado dado) {
        if (partida == null || partida.isFinalizada()) {
            return "La partida no está disponible.";
        }

        Jugador actual = partida.getJugadorActual();
        if (actual == null) {
            return "No hay jugadores.";
        }

        if (actual.debeSaltarTurno()) {
            actual.consumirTurnoPerdido();
            String mensaje = actual.getNombre() + " pierde este turno.";
            partida.setUltimoEvento(mensaje);
            siguienteTurno();
            return mensaje;
        }

        int resultado = tirarDado(actual, dado);
        String mensaje = actual.getNombre() + " avanza " + resultado + " casillas.";

        if (actual instanceof Pinguino) {
            Casilla casilla = partida.getTablero().getCasillas().get(actual.getPosicion());
            mensaje = gestorTablero.ejecutarCasilla(partida, (Pinguino) actual, casilla);
            comprobarChoquesJugadores();
        }

        partida.comprobarGanador();
        if (!partida.isFinalizada()) {
            siguienteTurno();
        }
        return mensaje;
    }

    private void comprobarChoquesJugadores() {
        for (int i = 0; i < partida.getJugadores().size(); i++) {
            for (int j = i + 1; j < partida.getJugadores().size(); j++) {
                Jugador j1 = partida.getJugadores().get(i);
                Jugador j2 = partida.getJugadores().get(j);
                if (j1.getPosicion() == j2.getPosicion() && j1 instanceof Pinguino && j2 instanceof Pinguino) {
                    gestorJugador.pingüinoGuerra((Pinguino) j1, (Pinguino) j2);
                    partida.setUltimoEvento("Dos jugadores han chocado y han librado una guerra de bolas de nieve.");
                }
            }
        }
    }

    public void ejecutarTurnoCompleto() {
        Jugador actual = partida.getJugadorActual();
        if (actual instanceof Pinguino) {
            Pinguino p = (Pinguino) actual;
            Item item = p.getInv().buscarPorNombre("normal");
            if (item instanceof Dado) {
                jugarTurnoConDado((Dado) item);
            }
        }
    }

    public void procesarTurnoJugador(Jugador j) {
        partida.setUltimoEvento("Turno de " + j.getNombre());
    }

    public void actualizarEstadoTablero() {
        partida.getTablero().actualizarTablero();
    }

    public void siguienteTurno() {
        partida.siguienteTurno();
    }

    public Partida getPartida() {
        return this.partida;
    }

    public void guardarPartida() {
        gestorBBDD.guardarBBDD(partida);
    }

    public void cargarPartida(int id) {
        Partida cargada = gestorBBDD.cargarBBDD(id);
        if (cargada != null) {
            this.partida = cargada;
        }
    }
}
