package modelo;

/**
 * Clase que representa al Pingüino. 
 * Hereda de Jugador (posicion, nombre, color) y añade la gestión de ítems.
 */
public class Pinguino extends Jugador {

    private Inventario inv; // Almacén de objetos (peces, bolas, dados)

    /**
     * Constructor: Crea un pingüino y le asigna un inventario inicial.
     */
    public Pinguino(String nombre, String color, int posicion, Inventario inventario) {
        // Llama al constructor de Jugador (Padre)
        super(nombre, color, posicion);
        this.inv = inventario;
    }

    // --- GESTIÓN DE BATALLAS ---

    /**
     * Lógica de la Guerra de Nieve. 
     * Se activa cuando un pingüino cae en la misma casilla que otro.
     * @param rival El otro pingüino con el que se choca.
     * @return Un mensaje de texto resumiendo el resultado del combate.
     */
    public String gestionarBatalla(Pinguino rival) {
        // 1. Miramos cuántas bolas tiene cada uno
        int bolasPropias = inv.getCantidad("bola");
        int bolasRival = rival.getInv().getCantidad("bola");

        // 2. Ambos gastan todas sus bolas en la pelea
        inv.gastarItem("bola", bolasPropias);
        rival.getInv().gastarItem("bola", bolasRival);

        String msj = "GUERRA_NIEVE: " + this.getNombre() + " (" + bolasPropias + " bolas) vs " + 
                     rival.getNombre() + " (" + bolasRival + " bolas). ";

        // 3. El que tiene menos bolas retrocede la diferencia de fuerza
        if (bolasPropias > bolasRival) {
            rival.moverPosicion(-(bolasPropias - bolasRival));
            msj += rival.getNombre() + " retrocede " + (bolasPropias - bolasRival) + " casillas.";
        } else if (bolasRival > bolasPropias) {
            this.moverPosicion(-(bolasRival - bolasPropias));
            msj += this.getNombre() + " retrocede " + (bolasRival - bolasPropias) + " casillas.";
        } else {
            msj += "¡Empate! Nadie retrocede.";
        }
        return msj;
    }

    // --- GESTIÓN DE INVENTARIO ---

    /**
     * Añade un ítem al inventario aplicando las reglas de capacidad máxima del juego.
     */
    public void añadirItem(Item i) {
        String nombre = i.getNombre().toLowerCase();
        int maximo = 99; // Capacidad por defecto

        // Reglas de negocio según el tipo de objeto
        if (nombre.equals("normal") || nombre.equals("rapido") || nombre.equals("lento")) {
            maximo = 3; // Máximo 3 dados de cada tipo
        } else if (nombre.equals("pez")) {
            maximo = 2; // Máximo 2 peces
        } else if (nombre.equals("bola")) {
            maximo = 6; // Máximo 6 bolas de nieve
        }

        // Se delega la inserción real a la clase Inventario
        inv.añadirOActualizar(i, maximo);
    }

    /**
     * Elimina una cantidad específica de un ítem y limpia el inventario si llega a cero.
     */
    public void quitarItem(Item i) {
        inv.gastarItem(i.getNombre(), i.getCantidad());
        inv.eliminarVacios();
    }

    // --- GETTERS Y SETTERS ---

    public Inventario getInv() { return inv; }
    public void setInv(Inventario inv) { this.inv = inv; }

    public boolean usarItem(Item i) {
        return inv.gastarItem(i.getNombre(), 1);
    }
}