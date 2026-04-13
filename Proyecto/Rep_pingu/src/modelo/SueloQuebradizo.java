package modelo;

public class SueloQuebradizo extends Casilla {
    private static final long serialVersionUID = 1L;

    public SueloQuebradizo(int posicion) {
        super(posicion);
    }

    @Override
    public void realizarAccion() {
    }

    public void realizarAccion(Jugador jugador) {
        int cantidad = jugador.getInventario().totalObjetos();

        if (cantidad > 5) {
            jugador.setPosicion(0);
            System.out.println(jugador.getNombre() + " cau pel terra trencadís i torna a l'inici!");

        } else if (cantidad >= 1) {
            jugador.perderTurno();
            System.out.println(jugador.getNombre() + " perd un torn pel terra trencadís.");

        } else {
            System.out.println(jugador.getNombre() + " passa sense penalització.");
        }
    }
}