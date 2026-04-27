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

    /**
     * Versión actualizada para permitir hasta 4 humanos + 1 IA.
     */
    public void nuevaPartida(int numHumanos) {
        this.partida = new Partida();
        ArrayList<Jugador> jugadores = new ArrayList<Jugador>();

        String[] colores = {"Azul", "Rojo", "Verde", "Amarillo"};
        
        // Añadir los jugadores humanos configurados
        for (int i = 0; i < numHumanos && i < 4; i++) {
            Inventario inv = new Inventario();
            inv.añadirOActualizar(new Dado("normal", 1, 1, 6), 3);
            jugadores.add(new Pinguino("Jugador " + (i + 1), colores[i], 0, inv));
        }

        // EL AÑADIDO: Añadir siempre a la IA Foca al final de la lista
        Foca cpu = new Foca("IA Foca", "Gris", 0);
        jugadores.add(cpu);

        this.partida.setJugadores(jugadores);
        this.partida.setJugadorActualIndice(0);
        this.partida.setUltimoEvento("Partida preparada. Turno de " + jugadores.get(0).getNombre());
    }

    public void nuevaPartidaPersonalizada(ArrayList<Pinguino> humanos) {
        this.partida = new Partida();
        ArrayList<Jugador> jugadores = new ArrayList<Jugador>();
        
        jugadores.addAll(humanos);

        Foca cpu = new Foca("IA Foca", "Gris", 0);
        jugadores.add(cpu);

        this.partida.setJugadores(jugadores);
        this.partida.setJugadorActualIndice(0);
        this.partida.setUltimoEvento("Partida personalizada preparada. Turno de " + jugadores.get(0).getNombre());
    }

    /**
     * Mantiene tu lógica original de tirarDado pero integrada.
     */
    public String jugarTurnoHumano(Dado dado) {
        if (partida == null || partida.isFinalizada()) return "Partida no disponible.";

        Jugador actual = partida.getJugadorActual();
        
        // Si el humano debe saltar turno
        if (actual.debeSaltarTurno()) {
            actual.consumirTurnoPerdido();
            String msj = actual.getNombre() + " pierde su turno.";
            partida.setUltimoEvento(msj);
            siguienteTurno();
            return msj;
        }

        // Tirada y movimiento (Lógica original)
        int resultado = dado.tirar(random);
        gestorJugador.jugadorSeMueve(actual, resultado, partida.getTablero());
        String mensaje = actual.getNombre() + " avanza " + resultado + " casillas.";

        // Ejecutar efectos si es Pingüino
        if (actual instanceof Pinguino) {
            Casilla casilla = partida.getTablero().getCasilla(actual.getPosicion());
            String msjCasilla = gestorTablero.ejecutarCasilla(partida, (Pinguino) actual, casilla);
            if (msjCasilla != null) mensaje += " " + msjCasilla;
            comprobarChoquesJugadores();
        }

        partida.comprobarGanador();
        if (!partida.isFinalizada()) {
            siguienteTurno();
        }
        
        return mensaje;
    }

    /**
     * El añadido para la lógica automática de la IA.
     */
    public String ejecutarTurnoIA() {
        Jugador actual = partida.getJugadorActual();
        if (!(actual instanceof Foca)) return null;

        Foca f = (Foca) actual;
        String mensaje;

        if (f.getTurnosBloqueada() > 0) {
            f.setTurnosBloqueada(f.getTurnosBloqueada() - 1);
            mensaje = "La foca está distraída comiendo pescado.";
        } else {
            int posicionAnterior = f.getPosicion();
            int pasos = random.nextInt(6) + 1;
            gestorJugador.jugadorSeMueve(f, pasos, partida.getTablero());
            int posicionNueva = f.getPosicion();
            Casilla casillaDestino = partida.getTablero().getCasilla(posicionNueva);
            if (casillaDestino instanceof Agujero) {
                int destinoA = partida.getTablero().buscarAgujeroAnterior(posicionNueva);
                f.setPosicion(destinoA);
                mensaje = "La foca (IA) cae en un agujero y retrocede.";
            } else if (casillaDestino instanceof Oso) {
                f.setPosicion(0);
                mensaje = "La foca (IA) se asusta del oso y huye al inicio.";
            } else {
                mensaje = "La foca (IA) se mueve " + pasos + " casillas.";
            }
            
            // Comprobar si pasa por encima de algún pingüino (sin contar la casilla final donde hay choque)
            for (Jugador j : partida.getJugadores()) {
                if (j instanceof Pinguino) {
                    if (j.getPosicion() > posicionAnterior && j.getPosicion() < posicionNueva) {
                        f.aplastarJugador((Pinguino) j);
                        partida.setUltimoEvento("La foca pasó por encima de " + j.getNombre() + " y perdió la mitad de su inventario.");
                    }
                }
            }
        }

        partida.setUltimoEvento(mensaje);
        siguienteTurno();
        return mensaje;
    }

    private void comprobarChoquesJugadores() {
        for (int i = 0; i < partida.getJugadores().size(); i++) {
            for (int j = i + 1; j < partida.getJugadores().size(); j++) {
                Jugador j1 = partida.getJugadores().get(i);
                Jugador j2 = partida.getJugadores().get(j);
                if (j1.getPosicion() == j2.getPosicion()) {
                    if (j1 instanceof Pinguino && j2 instanceof Pinguino) {
                        gestorJugador.pingüinoGuerra((Pinguino) j1, (Pinguino) j2);
                    } else if (j1 instanceof Pinguino && j2 instanceof Foca) {
                        gestorJugador.focaInteractua((Pinguino) j1, (Foca) j2, partida.getTablero());
                    } else if (j1 instanceof Foca && j2 instanceof Pinguino) {
                        gestorJugador.focaInteractua((Pinguino) j2, (Foca) j1, partida.getTablero());
                    }
                }
            }
        }
    }

    public void siguienteTurno() {
        partida.siguienteTurno();
    }

    // Métodos de acceso y persistencia originales
    public Partida getPartida() { return this.partida; }
    public void guardarPartida() { gestorBBDD.guardarBBDD(partida); }
    public void cargarPartida(int id) {
        Partida cargada = gestorBBDD.cargarBBDD(id);
        if (cargada != null) this.partida = cargada;
    }
}