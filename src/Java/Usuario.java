package Java;

public class Usuario {
	private String nombre;
	private int edad;
	private String DNI;

	public Usuario(String nombre, int edad, String DNI) {
		this.nombre = nombre;
		this.edad = edad;
		this.DNI = DNI;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getEdad() {
		return edad;
	}

	public void setEdad(int edad) {
		this.edad = edad;
	}

	public String getDNI() {
		return DNI;
	}

	public void setDNI(String DNI) {
		this.DNI = DNI;
	}

	public String toString() {
		return "Usuario: " + nombre + "\n" + "Edad: " + edad + " años\n" + "DNI: " + DNI;
	}

	public static boolean validarDNI(String dni) {
		if (dni == null || dni.length() != 9) {
			return false;
		}

		for (int i = 0; i < 8; i++) {
			if (!Character.isDigit(dni.charAt(i))) {
				return false;
			}
		}

		char ultimaLetra = dni.charAt(8);
		if (ultimaLetra < 'A' || ultimaLetra > 'Z') {
			return false;
		}

		return true;
	}
}