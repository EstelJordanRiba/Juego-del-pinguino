package modelo;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.util.Base64;

public class Encriptador {

    private static final String CLAVE = "1234567890123456"; // 16 bytes
    private static final String IV = "abcdef9876543210";    // 16 bytes

    private static Cipher getCipher(int modo) throws Exception {
        SecretKeySpec key = new SecretKeySpec(CLAVE.getBytes(), "AES");
        IvParameterSpec iv = new IvParameterSpec(IV.getBytes());
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(modo, key, iv);
        return cipher;
    }

    public static String encriptar(String texto) {
        try {
            Cipher cipher = getCipher(Cipher.ENCRYPT_MODE);
            byte[] encrypted = cipher.doFinal(texto.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            e.printStackTrace();
            return texto;
        }
    }

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