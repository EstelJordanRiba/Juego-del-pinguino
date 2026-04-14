package controlador;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedHashMap;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import modelo.Partida;

public class GestorBBDD {

    // ── Connexió Oracle ──────────────────────────────────────────
    private static final String ENTORNO = "fuera"; // "centro" o "fuera"
    private static final String URL_CENTRO = "jdbc:oracle:thin:@//192.168.3.26:1521/XEPDB2";
    private static final String URL_FUERA  = "jdbc:oracle:thin:@//oracle.ilerna.com:1521/XEPDB2";
    private static final String DB_USER    = "DM1_2526_GRUP06";
    private static final String DB_PWD     = "AGRUP06";

    // ── Fitxer local ─────────────────────────────────────────────
    private static final String RUTA_GUARDADO = "partida_pingu.dat";
    private static final String CLAVE         = "DAM1-PINGU-2026";

    private String urlBBDD;
    private String username;
    private String password;

    // ── Getters / Setters ─────────────────────────────────────────
    public String getUrlBBDD()              { return urlBBDD; }
    public void   setUrlBBDD(String u)      { this.urlBBDD = u; }
    public String getUsername()             { return username; }
    public void   setUsername(String u)     { this.username = u; }
    public String getPassword()             { return password; }
    public void   setPassword(String p)     { this.password = p; }

    // ── Connexió Oracle ───────────────────────────────────────────
    public Connection conectarBaseDatos() {
        System.out.println("Intentando conectarse a la base de datos...");
        String url = ENTORNO.equalsIgnoreCase("centro") ? URL_CENTRO : URL_FUERA;

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection(url, DB_USER, DB_PWD);
            if (con.isValid(5)) {
                System.out.println("Conectados a la base de datos (" + ENTORNO.toUpperCase() + ").");
            } else {
                System.out.println("Conexión creada, pero no parece válida.");
            }
            return con;
        } catch (ClassNotFoundException e) {
            System.out.println("No se ha encontrado el driver de Oracle.");
        } catch (SQLException e) {
            System.out.println("No se pudo conectar: " + e.getMessage());
        }
        return null;
    }

    public void cerrar(Connection con) {
        if (con != null) {
            try { con.close(); } catch (SQLException ignored) {}
        }
    }

    public int insert(Connection con, String sql) {
        return executeInsUpDel(con, sql, "Insert");
    }

    public int update(Connection con, String sql) {
        return executeInsUpDel(con, sql, "Update");
    }

    public int delete(Connection con, String sql) {
        return executeInsUpDel(con, sql, "Delete");
    }

    public ArrayList<LinkedHashMap<String, String>> select(Connection con, String sql) {
        ArrayList<LinkedHashMap<String, String>> resultados = new ArrayList<>();
        if (con == null) {
            System.out.println("No hay conexión.");
            return resultados;
        }
        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            ResultSetMetaData meta = rs.getMetaData();
            int numColumnas = meta.getColumnCount();
            while (rs.next()) {
                LinkedHashMap<String, String> fila = new LinkedHashMap<>();
                for (int i = 1; i <= numColumnas; i++) {
                    fila.put(meta.getColumnLabel(i), rs.getString(i));
                }
                resultados.add(fila);
            }
        } catch (SQLException e) {
            System.out.println("Error en SELECT: " + e.getMessage());
        }
        return resultados;
    }

    public void print(Connection con, String sql, String[] columnes) {
        if (con == null) { System.out.println("No hay conexión."); return; }
        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            int fila = 0;
            boolean hayResultados = false;
            while (rs.next()) {
                hayResultados = true;
                System.out.println("---- Fila " + (++fila) + " ----");
                for (String col : columnes) {
                    System.out.println(col + ": " + rs.getString(col));
                }
            }
            if (!hayResultados) System.out.println("No se ha encontrado nada.");
        } catch (SQLException e) {
            System.out.println("Error en SELECT: " + e.getMessage());
        }
    }

    public int executeInsUpDel(Connection con, String sql, String etiqueta) {
        if (con == null) { System.out.println("No hay conexión."); return 0; }
        try (Statement st = con.createStatement()) {
            int filas = st.executeUpdate(sql);
            System.out.println(etiqueta + " hecho correctamente. Filas afectadas: " + filas);
            return filas;
        } catch (SQLException e) {
            System.out.println("Error en " + etiqueta + ": " + e.getMessage());
            return 0;
        }
    }

    // ── Guardar / Carregar partida (fitxer local xifrat) ──────────
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
            if (!archivo.exists()) return null;
            byte[] contenido  = leerTodo(archivo);
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

    // ── Mètodes privats de xifrat ─────────────────────────────────
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
