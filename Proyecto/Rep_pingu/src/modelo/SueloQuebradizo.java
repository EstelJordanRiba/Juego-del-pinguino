package modelo;

public class SueloQuebradizo extends Casilla {
    private static final long serialVersionUID = 1L;

    public SueloQuebradizo(int posicion) {
        super(posicion);
    }

    @Override
    public void realizarAccion(Jugador jugador) {
        if (jugador instanceof Pinguino) {
            aplicarEfecto((Pinguino) jugador);
        }
    }

    public String aplicarEfecto(Pinguino p) {
        int cantidad = p.getInv().totalObjetos();
        
        
        if (cantidad > 5) {
            p.setPosicion(0);
            return p.getNombre() + " cae por el suelo quebradizo y vuelve al inicio.";
        } else if (cantidad > 1 && cantidad <= 5) {
            p.perderTurno();
            return p.getNombre() + " pierde un turno por el suelo quebradizo.";
        } else {
            return p.getNombre() + " pasa sin penalización por el suelo quebradizo.";
        }
    }
}