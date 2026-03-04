package Java;

import java.util.Scanner;

public class Main_ {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		String nombre;
		int edad;
		String dni;

		System.out.println(" BIENVENIDO AL SISTEMA BANCARIO \n");
		System.out.println("Por favor, introduzca sus datos:\n");

		System.out.print("Nombre: ");
		nombre = scanner.nextLine();

		System.out.print("Edad: ");
		while (!scanner.hasNextInt()) {
			System.out.println("Error: Debe introducir un número válido.");
			System.out.print("Edad: ");
			scanner.next();
		}
		edad = scanner.nextInt();
		scanner.nextLine();

		do {
			System.out.print("DNI (formato: 12345678A): ");
			dni = scanner.nextLine().toUpperCase();

			if (!Usuario.validarDNI(dni)) {
				System.out.println("Error: DNI incorrecto.");
				System.out.println("El DNI debe tener 8 números seguidos de una letra (A-Z).");
				System.out.println("Ejemplo: 12345678A\n");
			}
		} while (!Usuario.validarDNI(dni));

		Usuario usuario = new Usuario(nombre, edad, dni);
		Cuenta cuenta = new Cuenta(dni);

		System.out.println("\n Usuario creado correctamente\n");
		System.out.println(usuario.toString());
		System.out.println();

		int opcion;
		boolean continuar = true;

		while (continuar) {
			mostrarMenu();

			while (!scanner.hasNextInt()) {
				System.out.println("Error: Debe introducir un número válido.");
				System.out.print("Seleccione una opción: ");
				scanner.next();
			}
			opcion = scanner.nextInt();
			scanner.nextLine();

			System.out.println();

			switch (opcion) {
			case 1:
				System.out.print("Introduce la cantidad a retirar: ");
				if (scanner.hasNextDouble()) {
					double cantidadRetirar = scanner.nextDouble();
					scanner.nextLine();
					cuenta.getSaldo(cantidadRetirar);
					System.out.println("Saldo actual: " + cuenta.consultarSaldo() + "€");
				} else {
					System.out.println("Error: Cantidad inválida.");
					scanner.nextLine();
				}
				break;

			case 2:
				System.out.print("Introduce la cantidad a ingresar: ");
				if (scanner.hasNextDouble()) {
					double cantidadIngresar = scanner.nextDouble();
					scanner.nextLine();
					cuenta.setSaldo(cantidadIngresar);
					System.out.println("Saldo actual: " + cuenta.consultarSaldo() + "€");
				} else {
					System.out.println("Error: Cantidad inválida.");
					scanner.nextLine();
				}
				break;

			case 3:
				cuenta.getGastos();
				break;

			case 4:
				cuenta.getIngresos();
				break;

			case 5:
				System.out.println(cuenta.toString());
				System.out.println();
				break;

			case 6:
				continuar = false;
				break;

			default:
				System.out.println("ERROR: Opción no válida. Seleccione una opción del 1 al 6.\n");
			}

			if (continuar && opcion >= 1 && opcion <= 5) {
				System.out.println("\nPresione ENTER para continuar...");
				scanner.nextLine();
			}
		}

		System.out.println("Fin del programa.");
		System.out.println("Gracias por utilizar la aplicación.");

		scanner.close();
	}

	private static void mostrarMenu() {

		System.out.println("    Realizar una nueva acción:         ");
		System.out.println("1. Retirar dinero");
		System.out.println("2. Ingresar dinero");
		System.out.println("3. Mostrar gastos");
		System.out.println("4. Mostrar ingresos");
		System.out.println("5. Mostrar saldo");
		System.out.println("6. Salir");
		System.out.print("\nSeleccione una opción: ");
	}
}
