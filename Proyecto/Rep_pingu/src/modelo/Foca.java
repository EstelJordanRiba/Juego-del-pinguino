package modelo; // Pertenece al paquete "modelo"

// Clase Foca que hereda de Jugador (es un tipo especial de jugador)
public class Foca extends Jugador {

    // Indica si la foca ha sido sobornada (true = no ataca)
    private boolean soborno;

    // Número de turnos que la foca está bloqueada (no puede actuar)
    private int turnosBloqueada;

    // Constructor: inicializa los datos básicos del jugador
    public Foca(String nombre, String color, int posicion) {
        super(nombre, color, posicion); // Llama al constructor de Jugador
        this.soborno = false; // Por defecto no está sobornada
        this.turnosBloqueada = 0; // No está bloqueada al inicio
    }

    // Getter del estado de soborno
    public boolean isSoborno() {
        return soborno;
    }

    // Setter del estado de soborno
    public void setSoborno(boolean soborno) {
        this.soborno = soborno;
    }

    // Getter de los turnos bloqueada
    public int getTurnosBloqueada() {
        return turnosBloqueada;
    }

    // Setter de los turnos bloqueada
    public void setTurnosBloqueada(int turnosBloqueada) {
        this.turnosBloqueada = turnosBloqueada;
    }

    // Método especial: la foca "aplasta" a un pingüino
    public void aplastarJugador(Pinguino p) {
        
        // Calcula la mitad de los objetos del pingüino
        int mitad = p.getInv().totalObjetos() / 2;

        // Mientras queden objetos por quitar
        while (mitad > 0) {
            
            // Intenta quitar distintos tipos de objetos del inventario
            if (p.getInv().gastarItem("bola", 1)) {
                mitad--;
            } else if (p.getInv().gastarItem("pez", 1)) {
                mitad--;
            } else if (p.getInv().gastarItem("rapido", 1)) {
                mitad--;
            } else if (p.getInv().gastarItem("lento", 1)) {
                mitad--;
            } else if (p.getInv().gastarItem("normal", 1)) {
                mitad--;
            } else {
                // Si no quedan objetos, termina el bucle
                mitad = 0;
            }
        }

        // Limpia el inventario eliminando objetos con cantidad 0
        p.getInv().eliminarVacios();
    }

    // Método que devuelve si la foca está sobornada (igual que isSoborno)
    public boolean esSobornado() {
        return soborno;
    }
}