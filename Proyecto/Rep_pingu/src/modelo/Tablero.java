package modelo;

import java.util.ArrayList;
import java.util.Random;

public class Tablero {

    private ArrayList<Casilla> casillas;

    public Tablero() {
        this.casillas = new ArrayList<Casilla>();
    }

    public ArrayList<Casilla> getCasillas() {
        return casillas;
    }

    public void setCasillas(ArrayList<Casilla> casillas) {
        this.casillas = casillas;
    }

    public void generarCasillasAleatorias() {
        casillas.clear();
        casillas.add(new Normal(0));

        Random rand = new Random();
        for (int i = 1; i < 49; i++) {
            int tipo = rand.nextInt(6);
            Casilla c;
            switch (tipo) {
                case 0:
                    c = new Normal(i);
                    break;
                case 1:
                    c = new Oso(i);
                    break;
                case 2:
                    c = new Trineo(i);
                    break;
                case 3:
                    c = new Agujero(i);
                    break;
                case 4:
                    c = new Evento(i);
                    break;
                default:
                    c = new SueloQuebradizo(i);
                    break;
            }
            casillas.add(c);
        }

        casillas.add(new Normal(49));
    }

    public int buscarSiguienteTrineo(int posicionActual) {
        for (int i = posicionActual + 1; i < casillas.size(); i++) {
            if (casillas.get(i) instanceof Trineo) {
                return i;
            }
        }
        return posicionActual;
    }

    public int buscarAgujeroAnterior(int posicionActual) {
        for (int i = posicionActual - 1; i >= 0; i--) {
            if (casillas.get(i) instanceof Agujero) {
                return i;
            }
        }
        return 0;
    }

    public Casilla getCasilla(int posicion) {
        return casillas.get(posicion);
    }

    public void actualizarTablero() {
    }
}