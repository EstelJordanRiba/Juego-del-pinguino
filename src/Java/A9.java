package Java;

public class A9 {

    // Letras del abecedario
    private static final char[] LETRAS = {
        'A','B','C','D','E','F','G','H','I','J',
        'K','L','M','N','O','P','Q','R','S','T',
        'U','V','W','X','Y','Z'
    };

    // Código morse correspondiente
    private static final String[] MORSE = {
        ".-","-...","-.-.","-..",".","..-.","--.","....","..",".---",
        "-.-",".-..","--","-.","---",".--.","--.-",".-.","...","-",
        "..-","...-",".--","-..-","-.--","--.."
    };

    // Convierte texto a morse
    public static String convertirMorse(String texto) {

        texto = texto.toUpperCase();
        String resultado = "";

        for (int i = 0; i < texto.length(); i++) {
            char letra = texto.charAt(i);

            if (letra == ' ') {
                resultado += "/ ";
            } else {
                for (int j = 0; j < LETRAS.length; j++) {
                    if (letra == LETRAS[j]) {
                        resultado += MORSE[j] + " ";
                        break;
                    }
                }
            }
        }

        return resultado.trim();
    }

    // Convierte morse a texto
    public static String convertirLetras(String codigo) {

        String resultado = "";
        String[] palabras = codigo.split(" / ");

        for (String palabra : palabras) {
            String[] letrasMorse = palabra.split(" ");

            for (String l : letrasMorse) {
                for (int i = 0; i < MORSE.length; i++) {
                    if (l.equals(MORSE[i])) {
                        resultado += LETRAS[i];
                        break;
                    }
                }
            }
            resultado += " ";
        }

        return resultado.trim();
    }

    // Verifica si el morse es correcto
    public static boolean verificarMorse(String codigo) {

        String[] partes = codigo.split(" ");

        for (String p : partes) {
            if (p.equals("/") || p.equals("")) continue;

            boolean encontrado = false;

            for (String m : MORSE) {
                if (p.equals(m)) {
                    encontrado = true;
                    break;
                }
            }

            if (!encontrado) {
                return false;
            }
        }
        return true;
    }
}

