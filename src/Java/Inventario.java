package Java;
import java.util.ArrayList;

public class Inventario {

    ArrayList<Equipamiento> lista;

    public Inventario() {
        lista = new ArrayList<Equipamiento>();
    }

    void agregarEquipamiento(Equipamiento e) {
        lista.add(e);
    }

    void mostrarInventario() {
        for (int i = 0; i < lista.size(); i++) {
            System.out.println(lista.get(i).getNombre());
        }
    }
}