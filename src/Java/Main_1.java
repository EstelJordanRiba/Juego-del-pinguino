package Java;

public class Main_1 {
	import java.util.Scanner;
	import java.util.ArrayList;

	class Usuarios {
	    private String nombre;
	    private int edad;
	    private String DNI;

	    public Usuarios(String nombre, int edad, String DNI) {
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
	        return "Usuario: " + nombre + "\n" +
	               "Edad: " + edad + " años\n" +
	               "DNI: " + DNI;
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

	class Cuenta_1 {
	    private double saldo;
	    private String DNI_usuario;
	    private ArrayList<Double> gastos;
	    private ArrayList<Double> ingresos;

	    public Cuenta_1(String DNI_usuario) {
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
	        return "DNI Usuario: " + DNI_usuario + "\n" +
	               "Saldo disponible: " + saldo + "€";
	    }
	}

	public class Main_1 {
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

