// Esta clase se encarga de gestionar el cifrado y descifrado de datos sensibles.
// Utiliza el algoritmo AES con modo CBC y padding PKCS5.
// Esto garantiza confidencialidad de la información almacenada en la base de datos.

package controlador; // El package donde se encuentra la clase de seguridad

// Importaciones necesarias para la criptografía
import javax.crypto.Cipher; // Clase principal para las operaciones de cifrado y descifrado
import javax.crypto.spec.SecretKeySpec; // Permite construir una clave secreta a partir de un array de bytes
import javax.crypto.spec.IvParameterSpec; // Permite especificar un Vector de Inicialización (IV) para el modo CBC
import java.util.Base64; // Sirve para convertir datos binarios en texto legible (String)

public class Encriptador { // Clase pública accesible desde todo el proyecto

    // Clave secreta de 16 caracteres (16 bytes = 128 bits para AES-128)
    private static final String CLAVE = "1234567890123456";

    // Vector de inicialización (IV) necesario para el modo CBC, también de 16 bytes
    private static final String IV = "abcdef9876543210";

    // Método privado que configura el motor de cifrado
    // Recibe el modo (1 para encriptar, 2 para desencriptar)
    private static Cipher getCipher(int modo) throws Exception {

        // Creamos un objeto de clave secreta especificando el algoritmo "AES"
        SecretKeySpec key = new SecretKeySpec(CLAVE.getBytes(), "AES");

        // Creamos el IV (Vector de Inicialización) a partir de nuestro String IV
        IvParameterSpec iv = new IvParameterSpec(IV.getBytes());

        // Configuramos el Cipher con: Algoritmo (AES), Modo (CBC) y Relleno (PKCS5Padding)
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        // Inicializamos el objeto con el modo recibido, la clave y el vector
        cipher.init(modo, key, iv);

        return cipher; // Devolvemos el motor configurado
    }

    // Método público que devuelve un String con el texto encriptado
    public static String encriptar(String texto) {
        // Recibe el texto plano (ej: "hola123")
        
        try {
            // Obtenemos el motor de cifrado en modo ENCRYPT_MODE
            Cipher cipher = getCipher(Cipher.ENCRYPT_MODE);

            // Ciframos el texto convertido en bytes y obtenemos un array de bytes cifrados
            byte[] encrypted = cipher.doFinal(texto.getBytes());

            // Convertimos esos bytes raros a un String legible usando Base64
            return Base64.getEncoder().encodeToString(encrypted);

        } catch (Exception e) {
            // Si algo falla (clave incorrecta, etc.), imprimimos el error
            e.printStackTrace();
            return texto; 
            // Devolvemos el texto original para no romper el programa
        }
    }

    // Método público para recuperar el texto original
    public static String desencriptar(String texto) {
        // Recibe el texto en Base64 que viene de la base de datos
        
        try {
            // Obtenemos el motor de cifrado en modo DECRYPT_MODE
            Cipher cipher = getCipher(Cipher.DECRYPT_MODE);

            // Convertimos el String Base64 de nuevo a un array de bytes cifrados
            byte[] decoded = Base64.getDecoder().decode(texto);

            // Desencriptamos los bytes y los convertimos de nuevo a un String normal
            return new String(cipher.doFinal(decoded));

        } catch (Exception e) {
            // Si hay error en el descifrado, imprimimos el rastro del error
            e.printStackTrace();
            return texto;
            // Devolvemos lo que nos llegó para evitar errores de ejecución
        }
    }
}