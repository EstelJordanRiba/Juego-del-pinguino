package controlador; // El package donde está la clase

import java.util.ArrayList;
import java.util.Random;
import modelo.*;

public class GestorPartida { // Clase encargada de controlar la lógica de la partida

    private Partida partida;
    private GestorTablero gestorTablero;
    private GestorJugador gestorJugador;
    private GestorBBDD gestorBBDD;
    private Random random;

    public GestorPartida() {
        // Constructor de la clase, Inicializa los gestores necesarios y el generador de números aleatorios
        this.partida = null;
        this.gestorTablero = new GestorTablero();
        this.gestorJugador = new GestorJugador();
        this.gestorBBDD = new GestorBBDD();
        this.random = new Random();
    }

    public void nuevaPartida(int numHumanos) {
        // Método para crear una partida estándar, Recibe: el número de jugadores humanos (máximo 4)
        this.partida = new Partida();
        ArrayList<Jugador> jugadores = new ArrayList<Jugador>();
        String[] colores = {"Azul", "Rojo", "Verde", "Amarillo"};

        for (int i = 0; i < numHumanos && i < 4; i++) {
            // Creamos cada jugador humano
            Inventario inv = new Inventario();
            // Por defecto, cada pingüino empieza con 3 dados normales
            inv.añadirOActualizar(new Dado("normal", 1, 1, 6), 3);
            jugadores.add(new Pinguino("Jugador " + (i + 1), colores[i], 0, inv));
        }
        // Añadimos siempre una Foca controlada por la IA
        Foca cpu = new Foca("IA Foca", "Gris", 0);
        jugadores.add(cpu);
        this.partida.setJugadores(jugadores);
        this.partida.setJugadorActualIndice(0);
        this.partida.setUltimoEvento("Partida preparada. Turno de " + jugadores.get(0).getNombre());
    }

    public void nuevaPartidaPersonalizada(ArrayList<Pinguino> humanos) {
        // Método para crear una partida con pingüinos ya configurados, Recibe: una lista de objetos Pinguino
        
        this.partida = new Partida();
        ArrayList<Jugador> jugadores = new ArrayList<Jugador>();

        jugadores.addAll(humanos);

        // Añadimos la Foca IA al final de la lista
        Foca cpu = new Foca("IA Foca", "Gris", 0);
        jugadores.add(cpu);

        this.partida.setJugadores(jugadores);
        this.partida.setJugadorActualIndice(0);
        this.partida.setUltimoEvento("Partida personalizada preparada. Turno de " + jugadores.get(0).getNombre());
    }

    public String jugarTurnoHumano(Dado dado) {
        // Método para ejecutar el movimiento de un jugador humano, Recibe: el dado que el jugador ha decidido usar, Devuelve: un mensaje de texto con lo ocurrido en el turno
        
        if (partida == null || partida.isFinalizada()) return "Partida no disponible.";
        Jugador actual = partida.getJugadorActual();

        // Verificamos si el jugador está bloqueado (por ejemplo, cayó en un agujero antes)
        if (actual.debeSaltarTurno()) {
            actual.consumirTurnoPerdido();
            String msj = actual.getNombre() + " pierde su turno.";
            partida.setUltimoEvento(msj);
            siguienteTurno();
            return msj;
        }

        // Lanzamos el dado y movemos al jugador en el tablero
        int resultado = dado.tirar(random);
        gestorJugador.jugadorSeMueve(actual, resultado, partida.getTablero());
        String mensaje = actual.getNombre() + " avanza " + resultado + " casillas.";

        if (actual instanceof Pinguino) {
            // Si es un pingüino, ejecutamos el efecto de la casilla donde ha caído
            Casilla casilla = partida.getTablero().getCasilla(actual.getPosicion());
            String msjCasilla = gestorTablero.ejecutarCasilla(partida, (Pinguino) actual, casilla);
            if (msjCasilla != null) mensaje += " " + msjCasilla;

            // Comprobamos si ha caído en la misma casilla que otro jugador
            String choques = comprobarChoquesJugadores();
            if (!choques.isEmpty()) {
                mensaje += " | " + choques;
            }
        }

        // Revisamos si alguien ha llegado a la meta
        partida.comprobarGanador();

        if (!partida.isFinalizada()) {
            siguienteTurno(); // Pasamos el turno al siguiente de la lista
        }

        return mensaje;
    }

    public String ejecutarTurnoIA() {
        // Método que controla el movimiento automático de la Foca (IA), Devuelve: descripción de las acciones de la IA
        
        Jugador actual = partida.getJugadorActual();
        if (!(actual instanceof Foca)) return null;

        Foca f = (Foca) actual;
        String mensaje;

        // Lógica de distracción de la foca
        if (f.getTurnosBloqueada() > 0) {
            f.setTurnosBloqueada(f.getTurnosBloqueada() - 1);
            mensaje = "La foca está distraída comiendo pescado.";
        } else {
            int posicionAnterior = f.getPosicion();
            int pasos = random.nextInt(6) + 1; // La foca tira un dado invisible de 6 caras

            gestorJugador.jugadorSeMueve(f, pasos, partida.getTablero());

            int posicionNueva = f.getPosicion();
            Casilla casillaDestino = partida.getTablero().getCasilla(posicionNueva);

            // La foca también se ve afectada por osos y agujeros
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
            
            // REGLA ESPECIAL: Si la foca pasa por encima de un pingüino durante su movimiento, le quita ítems
            for (Jugador j : partida.getJugadores()) {
                if (j instanceof Pinguino) {
                    if (j.getPosicion() > posicionAnterior && j.getPosicion() < posicionNueva) {
                        f.aplastarJugador((Pinguino) j);
                        partida.setUltimoEvento("La foca pasó por encima de " + j.getNombre() + " y perdió la mitad de su inventario.");
                    }
                }
            }

            // Comprobar si la foca aterriza encima de alguien
            String choques = comprobarChoquesJugadores();
            if (!choques.isEmpty()) {
                mensaje += " | " + choques;
            }
        }
        partida.setUltimoEvento(mensaje);
        siguienteTurno();
        return mensaje;
    }
    private String comprobarChoquesJugadores() {
        // Método privado para gestionar encuentros en la misma casilla, Recorre la lista de jugadores buscando posiciones idénticas
        
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < partida.getJugadores().size(); i++) {
            for (int j = i + 1; j < partida.getJugadores().size(); j++) {
                Jugador j1 = partida.getJugadores().get(i);
                Jugador j2 = partida.getJugadores().get(j);

                // Si dos jugadores distintos están en la misma casilla (y no es la salida)
                if (j1.getPosicion() > 0 && j1.getPosicion() == j2.getPosicion()) {
                    if (j1 instanceof Pinguino && j2 instanceof Pinguino) {
                        // Dos pingüinos: se inicia una guerra de bolas de nieve
                        sb.append(gestorJugador.pingüinoGuerra((Pinguino) j1, (Pinguino) j2)).append(" ");
                    } else if (j1 instanceof Pinguino && j2 instanceof Foca) {
                        // Un pingüino y la foca interactúan
                        sb.append(gestorJugador.focaInteractua((Pinguino) j1, (Foca) j2, partida.getTablero())).append(" ");
                    } else if (j1 instanceof Foca && j2 instanceof Pinguino) {
                        sb.append(gestorJugador.focaInteractua((Pinguino) j2, (Foca) j1, partida.getTablero())).append(" ");
                    }
                }
            }
        }

        return sb.toString().trim();
    }

    public void siguienteTurno() {
        // Llama al método de la clase Partida para cambiar el índice del jugador actual
        partida.siguienteTurno();
    }
    
    public int guardarPartidaRetornantId(ArrayList<Integer> idsJugadors) {
        // Guarda la partida en la base de datos y devuelve el ID generado
        return gestorBBDD.guardarBBDD(partida, idsJugadors);
    }

    public Partida getPartida() {
        // Devuelve el objeto Partida actual
        return this.partida;
    }

    public void guardarPartida(ArrayList<Integer> idsJugadors) {
        // Procedimiento para guardar la partida en la BBDD
        gestorBBDD.guardarBBDD(partida, idsJugadors);
    }

    public void guardarPartida() {
        // Aviso de seguridad si se intenta guardar sin los IDs necesarios
        System.out.println("❌ No se puede guardar la partida sin IDs reales de jugadores.");
    }

    public void cargarPartida(int id) {
        // Busca una partida guardada por su ID y la carga en el sistema
        Partida cargada = gestorBBDD.cargarBBDD(id);
        if (cargada != null) this.partida = cargada;
    }
}