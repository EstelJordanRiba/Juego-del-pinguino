package Java;


public class Mago extends Personaje {

    int mana;

    public Mago(String nombre, int nivel, int vida, int mana) {
        super(nombre, nivel, vida);
        this.mana = mana;
    }

    void lanzarHechizo(int coste) {
        if (mana >= coste) {
            mana = mana - coste;
            System.out.println(nombre + " lanza un hechizo");
        } else {
            System.out.println("No hay suficiente mana");
        }
    }

    void recargarMana(int cantidad) {
        mana = mana + cantidad;
    }

    int getMana() {
        return mana;
    }
}