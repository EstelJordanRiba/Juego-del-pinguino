package controlador; // Pertenece al paquete "modelo"

// Importaciones necesarias para trabajar con cifrado AES
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.util.Base64;

// Clase encargada de encriptar y desencriptar texto
public class Encriptador {

    // Clave secreta (debe tener 16 bytes para AES-128)
    private static final String CLAVE = "1234567890123456";

    // Vector de inicialización (IV), también de 16 bytes
    private static final String IV = "abcdef9876543210";

    // Método privado que configura el cifrador
    private static Cipher getCipher(int modo) throws Exception {
        
        // Se crea la clave AES a partir del String
        SecretKeySpec key = new SecretKeySpec(CLAVE.getBytes(), "AES");
        
        // Se crea el vector de inicialización (IV)
        IvParameterSpec iv = new IvParameterSpec(IV.getBytes());
        
        // Se especifica el tipo de cifrado: AES en modo CBC con padding
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        
        // Se inicializa el cifrador en modo encriptar o desencriptar
        cipher.init(modo, key, iv);
        
        return cipher;
    }

    // Método para encriptar texto
    public static String encriptar(String texto) {
        try {
            Cipher cipher = getCipher(Cipher.ENCRYPT_MODE);
            
            // Se convierte el texto en bytes y se encripta
            byte[] encrypted = cipher.doFinal(texto.getBytes());
            
            // Se codifica en Base64 para poder mostrarlo como texto
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            e.printStackTrace();
            
            // En caso de error, devuelve el texto original
            return texto;
        }
    }

    // Método para desencriptar texto
    public static String desencriptar(String texto) {
        try {
            Cipher cipher = getCipher(Cipher.DECRYPT_MODE);
            
            // Se decodifica el Base64 a bytes
            byte[] decoded = Base64.getDecoder().decode(texto);
            
            // Se desencripta y se convierte a String
            return new String(cipher.doFinal(decoded));
        } catch (Exception e) {
            e.printStackTrace();
            
            // En caso de error, devuelve el texto original
            return texto;
        }
    }
}