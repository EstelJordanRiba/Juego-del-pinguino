package Java;

public class Ma {
	

	    public static void main(String[] args) {

	        Guerrero g = new Guerrero("Thor", 3, 100);
	        Mago m = new Mago("Merlin", 5, 80, 50);
	        Ladron l = new Ladron("Sombra", 4, 70);

	        Equipamiento espada = new Equipamiento("Espada", "Arma");

	        g.agregarEquipamiento(espada);

	        g.mostrarInfo();
	        m.mostrarInfo();
	        l.mostrarInfo();

	        g.proteger(m);

	        m.bajarVida(30);
	        m.lanzarHechizo(20);

	        l.hacerseInvisible();
	        l.robar();

	        g.getInventario().mostrarInventario();
	    }
	}
