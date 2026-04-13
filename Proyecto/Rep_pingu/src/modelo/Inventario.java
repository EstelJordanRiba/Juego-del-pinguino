package modelo;

import java.io.Serializable;
import java.util.ArrayList;

public class Inventario implements Serializable {
    private static final long serialVersionUID = 1L;

    private ArrayList<Item> lista;

    public Inventario() {
        this.lista = new ArrayList<Item>();
    }

    public ArrayList<Item> getLista() {
        return lista;
    }

    public void setLista(ArrayList<Item> lista) {
        this.lista = lista;
    }

    public Item buscarPorNombre(String nombre) {
        for (Item item : lista) {
            if (item.getNombre().equalsIgnoreCase(nombre)) {
                return item;
            }
        }
        return null;
    }

    public int getCantidad(String nombre) {
        Item item = buscarPorNombre(nombre);
        return item == null ? 0 : item.getCantidad();
    }

    public void añadirOActualizar(Item nuevo, int maximo) {
        Item existente = buscarPorNombre(nuevo.getNombre());
        if (existente == null) {
            int cantidad = nuevo.getCantidad();
            if (cantidad > maximo) {
                cantidad = maximo;
            }
            nuevo.setCantidad(cantidad);
            lista.add(nuevo);
        } else {
            int total = existente.getCantidad() + nuevo.getCantidad();
            if (total > maximo) {
                total = maximo;
            }
            existente.setCantidad(total);
        }
    }

    public boolean gastarItem(String nombre, int cantidad) {
        Item item = buscarPorNombre(nombre);
        if (item != null && item.getCantidad() >= cantidad) {
            item.restarCantidad(cantidad);
            return true;
        }
        return false;
    }

    public int totalObjetos() {
        int total = 0;
        for (Item item : lista) {
            total += item.getCantidad();
        }
        return total;
    }

    public void eliminarVacios() {
        lista.removeIf(item -> item.getCantidad() <= 0);
    }
}
