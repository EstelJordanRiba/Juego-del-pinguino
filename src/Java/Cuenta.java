package Java;

import java.util.ArrayList;

public class Cuenta {
	private double saldo;
	private String DNI_usuario;
	private ArrayList<Double> gastos;
	private ArrayList<Double> ingresos;

	public Cuenta(String DNI_usuario) {
		this.DNI_usuario = DNI_usuario;
		this.saldo = 0.0;
		this.gastos = new ArrayList<>();
		this.ingresos = new ArrayList<>();
	}

	public double getSaldo(double cantidad) {
		if (cantidad <= 0) {
			System.out.println("Error: La cantidad debe ser mayor que 0.");
			return saldo;
		}

		if (cantidad > saldo) {
			System.out.println("ERROR: No dispone de saldo suficiente.");
			System.out.println("Saldo disponible: " + saldo + "€");
			System.out.println("Cantidad solicitada: " + cantidad + "€");
			return saldo;
		}

		saldo -= cantidad;
		gastos.add(cantidad);
		System.out.println(" Retirada realizada correctamente: " + cantidad + "€");
		return saldo;
	}

	public double setSaldo(double cantidad) {
		if (cantidad <= 0) {
			System.out.println("Error: La cantidad debe ser mayor que 0.");
			return saldo;
		}

		saldo += cantidad;
		ingresos.add(cantidad);
		System.out.println(" Ingreso realizado correctamente: " + cantidad + "€");
		return saldo;
	}

	public double consultarSaldo() {
		return saldo;
	}

	public void getIngresos() {
		System.out.println("LISTA DE INGRESOS ");
		if (ingresos.isEmpty()) {
			System.out.println("No hay ingresos registrados.");
		} else {
			double total = 0;
			for (int i = 0; i < ingresos.size(); i++) {
				System.out.println((i + 1) + ". " + ingresos.get(i) + "€");
				total += ingresos.get(i);
			}

			System.out.println("Total ingresado: " + total + "€");
		}
		System.out.println();
	}

	public void getGastos() {
		System.out.println("LISTA DE GASTOS ");
		if (gastos.isEmpty()) {
			System.out.println("No hay gastos registrados.");
		} else {
			double total = 0;
			for (int i = 0; i < gastos.size(); i++) {
				System.out.println((i + 1) + ". " + gastos.get(i) + "€");
				total += gastos.get(i);
			}

			System.out.println("Total gastado: " + total + "€");
		}
		System.out.println();
	}

	public String toString() {
		return "DNI Usuario: " + DNI_usuario + "\n" + "Saldo disponible: " + saldo + "€";
	}
}