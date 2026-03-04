package Java;

public class A9_2 {
	

	    public static void main(String[] args) {

	        String texto = "Hola mundo";
	        String morse = A9.convertirMorse(texto);

	        System.out.println("Texto original: " + texto);
	        System.out.println("En morse: " + morse);

	        if (A9.verificarMorse(morse)) {
	            System.out.println("Morse válido");
	            System.out.println("De nuevo a texto: " + A9.convertirLetras(morse));
	        } else {
	            System.out.println("Morse incorrecto");
	        }
	    }
	}


