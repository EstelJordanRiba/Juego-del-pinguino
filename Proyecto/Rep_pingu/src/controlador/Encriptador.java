// Esta clase se encarga de gestionar el cifrado y descifrado de datos sensibles.
// Utiliza el algoritmo AES con modo CBC y padding PKCS5.
// Esto garantiza confidencialidad de la información almacenada en la base de datos.

package controlador;

// Importaciones necesarias
import javax.crypto.Cipher; // Permite cifrar y descifrar
import javax.crypto.spec.SecretKeySpec; // Representa la clave secreta
import javax.crypto.spec.IvParameterSpec; // Vector de inicialización
import java.util.Base64; // Codificación a texto

public class Encriptador {

    // Clave secreta de 16 bytes (AES-128)
    private static final String CLAVE = "1234567890123456";

    // Vector de inicialización (también 16 bytes)
    private static final String IV = "abcdef9876543210";

    // Método que construye el objeto Cipher
    private static Cipher getCipher(int modo) throws Exception {

        // Se crea la clave a partir del String
        SecretKeySpec key = new SecretKeySpec(CLAVE.getBytes(), "AES");

        // Se crea el IV
        IvParameterSpec iv = new IvParameterSpec(IV.getBytes());

        // Se define el algoritmo
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        // Inicializa el modo (encrypt/decrypt)
        cipher.init(modo, key, iv);

        return cipher;
    }

    // Método para encriptar texto
    public static String encriptar(String texto) {
        try {
            Cipher cipher = getCipher(Cipher.ENCRYPT_MODE);

            // Convierte texto a bytes y lo cifra
            byte[] encrypted = cipher.doFinal(texto.getBytes());

            // Lo convierte a Base64
            return Base64.getEncoder().encodeToString(encrypted);

        } catch (Exception e) {
            e.printStackTrace();
            return texto;
        }
    }

    // Método para desencriptar
    public static String desencriptar(String texto) {
        try {
            Cipher cipher = getCipher(Cipher.DECRYPT_MODE);

            byte[] decoded = Base64.getDecoder().decode(texto);

            return new String(cipher.doFinal(decoded));

        } catch (Exception e) {
            e.printStackTrace();
            return texto;
        }
    }
}

