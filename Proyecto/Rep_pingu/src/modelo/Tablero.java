package modelo;

import java.util.ArrayList;
import java.util.Random;

public class Tablero {

    // Lista que almacena las 50 casillas del juego
    private ArrayList<Casilla> casillas;

    /**
     * Constructor: Inicializa la lista de casillas.
     */
    public Tablero() {
        this.casillas = new ArrayList<Casilla>();
    }

    /**
     * Genera el escenario de juego de forma aleatoria.
     * Asegura que la salida (0) y la meta (49) sean siempre casillas normales.
     */
    public void generarCasillasAleatorias() {
        casillas.clear();
        casillas.add(new Normal(0)); // Salida segura

        Random rand = new Random();
        // Bucle para rellenar desde la casilla 1 hasta la 48
        for (int i = 1; i < 49; i++) {
            int tipo = rand.nextInt(10); // Genera un número del 0 al 9
            Casilla c;
            
            // Probabilidad del 50% (0-4) de que sea una casilla normal
            if (tipo < 5) {
                c = new Normal(i);
            } else {
                // El otro 50% se reparte entre casillas especiales
                switch (tipo) {
                    case 5: c = new Oso(i); break;
                    case 6: c = new Trineo(i); break;
                    case 7: c = new Agujero(i); break;
                    case 8: c = new Evento(i); break;
                    default: c = new SueloQuebradizo(i); break;
                }
            }
            casillas.add(c);
        }

        casillas.add(new Normal(49)); // Meta segura
    }

    /**
     * Lógica del Trineo: Busca la siguiente casilla de tipo Trineo hacia adelante.
     * @return La posición del siguiente trineo o la posición actual si no hay más.
     */
    public int buscarSiguienteTrineo(int posicionActual) {
        for (int i = posicionActual + 1; i < casillas.size(); i++) {
            if (casillas.get(i) instanceof Trineo) {
                return i;
            }
        }
        return posicionActual;
    }

    /**
     * Lógica del Agujero: Busca la casilla de tipo Agujero más cercana hacia atrás.
     * @return La posición del agujero anterior o la casilla 0 (inicio) si no encuentra ninguno.
     */
    public int buscarAgujeroAnterior(int posicionActual) {
        for (int i = posicionActual - 1; i >= 0; i--) {
            if (casillas.get(i) instanceof Agujero) {
                return i;
            }
        }
        return 0; // Si no hay agujeros detrás, el castigo es volver al inicio
    }

    /**
     * Devuelve el objeto Casilla de una posición específica para consultar su tipo.
     */
    public Casilla getCasilla(int posicion) {
        return casillas.get(posicion);
    }

    // --- GETTERS Y SETTERS ---
    public ArrayList<Casilla> getCasillas() { return casillas; }
    public void setCasillas(ArrayList<Casilla> casillas) { this.casillas = casillas; }
    
    public void actualizarTablero() {
        // Método preparado para futuras actualizaciones visuales
    }
}