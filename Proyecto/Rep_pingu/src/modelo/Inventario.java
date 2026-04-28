package modelo; // Pertenece al paquete "modelo"

import java.util.ArrayList; // Importa la estructura de lista dinámica

// Clase Inventario: gestiona los objetos (Items) de un jugador
public class Inventario {

    // Lista que guarda todos los items del inventario
    private ArrayList<Item> lista;

    // Constructor: inicializa la lista vacía
    public Inventario() {
        this.lista = new ArrayList<Item>();
    }

    // Getter de la lista
    public ArrayList<Item> getLista() {
        return lista;
    }

    // Setter de la lista
    public void setLista(ArrayList<Item> lista) {
        this.lista = lista;
    }

    // Busca un item por su nombre (sin importar mayúsculas/minúsculas)
    public Item buscarPorNombre(String nombre) {
        for (Item item : lista) {
            if (item.getNombre().equalsIgnoreCase(nombre)) {
                return item; // Devuelve el item si lo encuentra
            }
        }
        return null; // Si no lo encuentra, devuelve null
    }

    // Devuelve la cantidad de un item concreto
    public int getCantidad(String nombre) {
        Item item = buscarPorNombre(nombre);
        return item == null ? 0 : item.getCantidad();
        // Si no existe → 0, si existe → su cantidad
    }

    // Añade un item o actualiza su cantidad si ya existe
    public void añadirOActualizar(Item nuevo, int maximo) {
        Item existente = buscarPorNombre(nuevo.getNombre());

        if (existente == null) {
            // Si no existe, lo añade
            int cantidad = nuevo.getCantidad();

            // Controla que no supere el máximo permitido
            if (cantidad > maximo) {
                cantidad = maximo;
            }

            nuevo.setCantidad(cantidad);
            lista.add(nuevo);

        } else {
            // Si ya existe, suma las cantidades
            int total = existente.getCantidad() + nuevo.getCantidad();

            // Controla el máximo permitido
            if (total > maximo) {
                total = maximo;
            }

            existente.setCantidad(total);
        }
    }

    // Gasta (resta) una cantidad de un item
    public boolean gastarItem(String nombre, int cantidad) {
        Item item = buscarPorNombre(nombre);

        // Comprueba que exista y haya suficiente cantidad
        if (item != null && item.getCantidad() >= cantidad) {
            item.restarCantidad(cantidad); // Resta la cantidad
            return true; // Operación correcta
        }

        return false; // No se pudo gastar
    }

    // Devuelve el total de objetos en el inventario
    public int totalObjetos() {
        int total = 0;

        // Suma todas las cantidades de los items
        for (Item item : lista) {
            total += item.getCantidad();
        }

        return total;
    }

    // Elimina los items que tienen cantidad 0 o menor
    public void eliminarVacios() {
        lista.removeIf(item -> item.getCantidad() <= 0);
    }
}