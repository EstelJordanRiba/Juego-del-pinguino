package Java;

public class A9_1 {

	public class Main {
	    public static void main(String[] args) {

	        String texto = "Hola";
	        String morse = A9.convertirMorse(texto);
	        System.out.println("Texto a morse: " + morse);

	        String letras = A9.convertirLetras(morse);
	        System.out.println("Morse a texto: " + letras);

	        System.out.println("Morse correcto: " +
	        		A9.verificarMorse(morse));
	    }
	}
}

