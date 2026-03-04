package Java;
public class Guerrero extends Personaje {

    Personaje companeroProtegido;

    public Guerrero(String nombre, int nivel, int vida) {
        super(nombre, nivel, vida);
        companeroProtegido = null;
    }

    void proteger(Personaje p) {
        companeroProtegido = p;
        p.setProtegido(true);
        System.out.println(nombre + " protege a " + p.nombre);
    }

    void dejarDeProteger() {
        if (companeroProtegido != null) {
            companeroProtegido.setProtegido(false);
            companeroProtegido = null;
        }
    }

    Personaje estaProtegiendo() {
        return companeroProtegido;
    }
}