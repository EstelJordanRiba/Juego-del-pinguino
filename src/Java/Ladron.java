package Java;
public class Ladron extends Personaje {

    boolean invisible;

    public Ladron(String nombre, int nivel, int vida) {
        super(nombre, nivel, vida);
        invisible = false;
    }

    void robar() {
        if (invisible) {
            System.out.println(nombre + " roba sin ser visto");
        } else {
            System.out.println(nombre + " roba y hace ruido");
        }
    }

    void hacerseInvisible() {
        invisible = true;
    }

    boolean estaInvisible() {
        return invisible;
    }
}