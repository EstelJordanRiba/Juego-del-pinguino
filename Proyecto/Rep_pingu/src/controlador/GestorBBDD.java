package controlador;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import modelo.Partida;

public class GestorBBDD {
    private String urlBBDD;
    private String username;
    private String password;
    private static final String RUTA_GUARDADO = "partida_pingu.dat";
    private static final String CLAVE = "DAM1-PINGU-2026";

    public String getUrlBBDD() {
        return urlBBDD;
    }

    public void setUrlBBDD(String urlBBDD) {
        this.urlBBDD = urlBBDD;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void guardarBBDD(Partida p) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(p);
            oos.close();

            byte[] cifrado = cifrar(baos.toByteArray());
            FileOutputStream fos = new FileOutputStream(RUTA_GUARDADO);
            fos.write(Base64.getEncoder().encode(cifrado));
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Partida cargarBBDD(int id) {
        try {
            File archivo = new File(RUTA_GUARDADO);
            if (!archivo.exists()) {
                return null;
            }

            byte[] contenido = leerTodo(archivo);
            byte[] descifrado = descifrar(Base64.getDecoder().decode(contenido));

            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(descifrado));
            Partida partida = (Partida) ois.readObject();
            ois.close();
            return partida;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private byte[] leerTodo(File archivo) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FileInputStream fis = new FileInputStream(archivo);
        byte[] buffer = new byte[1024];
        int leidos;
        while ((leidos = fis.read(buffer)) != -1) {
            baos.write(buffer, 0, leidos);
        }
        fis.close();
        return baos.toByteArray();
    }

    private byte[] cifrar(byte[] datos) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, crearClave());
        return cipher.doFinal(datos);
    }

    private byte[] descifrar(byte[] datos) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, crearClave());
        return cipher.doFinal(datos);
    }

    private SecretKeySpec crearClave() throws Exception {
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        byte[] clave = sha.digest(CLAVE.getBytes(StandardCharsets.UTF_8));
        byte[] clave16 = new byte[16];
        System.arraycopy(clave, 0, clave16, 0, 16);
        return new SecretKeySpec(clave16, "AES");
    }
}
