package Java;
import java.io.*;

public class Procesadorventas_ {


	    public static void main(String[] args) {
	        
	        try {
	            // Crear carpeta reportes
	            File carpeta = new File("reportes");
	            carpeta.mkdir();
	            
	            // Mostrar inicio
	            System.out.println("=== PROCESANDO VENTAS ===\n");
	            
	            // Variable para el total
	            double total = 0;
	            
	            // Abrir carpeta Data
	            File data = new File("Data");
	            File[] archivos = data.listFiles();
	            
	            // Revisar cada archivo
	            for (int i = 0; i < archivos.length; i++) {
	                
	                if (archivos[i].getName().endsWith(".txt")) {
	                    
	                    // Abrir archivo para leer
	                    BufferedReader br = new BufferedReader(new FileReader(archivos[i]));
	                    
	                    String linea;
	                    int numLinea = 1;
	                    
	                    // Leer línea por línea
	                    while ((linea = br.readLine()) != null) {
	                        
	                        try {
	                            // Separar por |
	                            String[] partes = linea.split("\\|");
	                            
	                            String producto = partes[1];
	                            String categoria = partes[2];
	                            int unidades = Integer.parseInt(partes[3]);
	                            double precio = Double.parseDouble(partes[4]);
	                            
	                            // Calcular
	                            double totalLinea = unidades * precio;
	                            total = total + totalLinea;
	                            
	                            // Mostrar en consola
	                            System.out.println(categoria + " - " + producto + ": " + totalLinea + " euros");
	                            
	                        } catch (Exception e) {
	                            System.out.println("ERROR: " + archivos[i].getName() + " linea " + numLinea + ": " + linea);
	                        }
	                        
	                        numLinea++;
	                    }
	                    
	                    br.close();
	                }
	            }
	            
	            // Mostrar total
	            System.out.println("\n=== TOTAL: " + total + " euros ===");
	            
	            System.out.println("\nProceso completado!");
	            
	        } catch (Exception e) {
	            System.out.println("Error: " + e.getMessage());
	        }
	    }
	}