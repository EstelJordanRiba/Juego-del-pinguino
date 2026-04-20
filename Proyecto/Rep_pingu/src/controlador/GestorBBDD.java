package controlador;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import modelo.*;

public class GestorBBDD {

    // ── Connexió Oracle ──────────────────────────────────────────
    private static final String ENTORNO = "fuera"; // "centro" o "fuera"
    private static final String URL_CENTRO = "jdbc:oracle:thin:@//192.168.3.26:1521/XEPDB2";
    private static final String URL_FUERA  = "jdbc:oracle:thin:@//oracle.ilerna.com:1521/XEPDB2";
    private static final String DB_USER    = "DM1_2526_GRUP06";
    private static final String DB_PWD     = "AGRUP06";

    private static final int ID_PARTIDA_FIJA = 1;

    private String urlBBDD;
    private String username;
    private String password;

    public String getUrlBBDD() { return urlBBDD; }
    public void setUrlBBDD(String u) { this.urlBBDD = u; }
    public String getUsername() { return username; }
    public void setUsername(String u) { this.username = u; }
    public String getPassword() { return password; }
    public void setPassword(String p) { this.password = p; }

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
            try {
                con.close();
            } catch (SQLException ignored) {
            }
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
        if (con == null) {
            System.out.println("No hay conexión.");
            return;
        }

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

            if (!hayResultados) {
                System.out.println("No se ha encontrado nada.");
            }

        } catch (SQLException e) {
            System.out.println("Error en SELECT: " + e.getMessage());
        }
    }

    public int executeInsUpDel(Connection con, String sql, String etiqueta) {
        if (con == null) {
            System.out.println("No hay conexión.");
            return 0;
        }

        try (Statement st = con.createStatement()) {
            int filas = st.executeUpdate(sql);
            System.out.println(etiqueta + " hecho correctamente. Filas afectadas: " + filas);
            return filas;
        } catch (SQLException e) {
            System.out.println("Error en " + etiqueta + ": " + e.getMessage());
            return 0;
        }
    }

    // ── GUARDAR PARTIDA EN ORACLE ────────────────────────────────
    public void guardarBBDD(Partida p) {
        Connection con = conectarBaseDatos();
        if (con == null || p == null) {
            return;
        }

        try {
            con.setAutoCommit(false);

            borrarDatosPartida(con, ID_PARTIDA_FIJA);
            guardarPartida(con, p);
            guardarJugadores(con, p);
            guardarTurnos(con, p);
            guardarTablero(con, p);

            con.commit();
            System.out.println("Partida guardada correctamente en Oracle.");

        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException ignored) {
            }
            System.out.println("Error guardando partida: " + e.getMessage());
        } finally {
            cerrar(con);
        }
    }

    // ── CARGAR PARTIDA DESDE ORACLE ──────────────────────────────
    public Partida cargarBBDD(int id) {
        Connection con = conectarBaseDatos();
        if (con == null) {
            return null;
        }

        try {
            Partida partida = new Partida();

            // 1. Cargar jugadores desde Jugadors
            ArrayList<Jugador> jugadores = new ArrayList<>();

            String sqlJugadores = "SELECT jugador_id, nom, color_fitxa, dau1, dau2, dau3, peixos, boles_neu " +
                                  "FROM Jugadors WHERE partida_id = ? ORDER BY jugador_id";

            PreparedStatement psJug = con.prepareStatement(sqlJugadores);
            psJug.setInt(1, id);
            ResultSet rsJug = psJug.executeQuery();

            while (rsJug.next()) {
                String nombre = rsJug.getString("nom");
                String color = rsJug.getString("color_fitxa");

                Inventario inv = new Inventario();

                int dau1 = rsJug.getInt("dau1");
                int dau2 = rsJug.getInt("dau2");
                int dau3 = rsJug.getInt("dau3");
                int peixos = rsJug.getInt("peixos");
                int boles = rsJug.getInt("boles_neu");

                // Convertimos los 3 dados guardados a inventario
                if (dau1 > 0) {
                    inv.añadirOActualizar(crearDadoSegunValor(dau1), 3);
                }
                if (dau2 > 0) {
                    inv.añadirOActualizar(crearDadoSegunValor(dau2), 3);
                }
                if (dau3 > 0) {
                    inv.añadirOActualizar(crearDadoSegunValor(dau3), 3);
                }
                if (peixos > 0) {
                    inv.añadirOActualizar(new Pez("pez", peixos), 2);
                }
                if (boles > 0) {
                    inv.añadirOActualizar(new BolaDeNieve("bola", boles), 6);
                }

                Pinguino ping = new Pinguino(nombre, color, 0, inv);
                jugadores.add(ping);
            }

            rsJug.close();
            psJug.close();

            partida.setJugadores(jugadores);

            // 2. Cargar turno actual desde Torns
            String sqlTurno = "SELECT jugador_id FROM Torns WHERE partida_id = ? AND torn_actual = 'Y'";
            PreparedStatement psTurno = con.prepareStatement(sqlTurno);
            psTurno.setInt(1, id);
            ResultSet rsTurno = psTurno.executeQuery();

            if (rsTurno.next()) {
                int jugadorActualId = rsTurno.getInt("jugador_id");
                partida.setJugadorActualIndice(jugadorActualId - 1);
            } else {
                partida.setJugadorActualIndice(0);
            }

            rsTurno.close();
            psTurno.close();

            // 3. Cargar tablero desde Taulell
            ArrayList<Casilla> casillas = new ArrayList<>();

            String sqlTablero = "SELECT casella_id, tipus FROM Taulell WHERE taulell_id = ? ORDER BY casella_id";
            PreparedStatement psTab = con.prepareStatement(sqlTablero);
            psTab.setInt(1, id);
            ResultSet rsTab = psTab.executeQuery();

            while (rsTab.next()) {
                int posicion = rsTab.getInt("casella_id") - 1;
                String tipo = rsTab.getString("tipus");

                Casilla c = crearCasilla(tipo, posicion);
                if (c != null) {
                    casillas.add(c);
                }
            }

            rsTab.close();
            psTab.close();

            partida.getTablero().setCasillas(casillas);

            // 4. Cargar posiciones actuales desde PARTICIPACIÓN simulada con Torns/Jugadors
            // Como vuestra tabla Jugadors no tiene posicion_actual, usamos el orden inicial 0
            // Si quieres guardar posición real, tendrás que añadir una columna POSICIO_ACTUAL a Jugadors
            // o usar la tabla Participacion real que tenéis creada en Oracle.

            partida.setUltimoEvento("Partida cargada desde Oracle.");

            cerrar(con);
            System.out.println("Partida cargada correctamente desde Oracle.");
            return partida;

        } catch (SQLException e) {
            System.out.println("Error cargando partida: " + e.getMessage());
            cerrar(con);
            return null;
        }
    }

    // ── MÉTODOS PRIVADOS ─────────────────────────────────────────
    private void borrarDatosPartida(Connection con, int idPartida) throws SQLException {
        PreparedStatement ps1 = con.prepareStatement("DELETE FROM Torns WHERE partida_id = ?");
        ps1.setInt(1, idPartida);
        ps1.executeUpdate();
        ps1.close();

        PreparedStatement ps2 = con.prepareStatement("DELETE FROM Taulell WHERE taulell_id = ?");
        ps2.setInt(1, idPartida);
        ps2.executeUpdate();
        ps2.close();

        PreparedStatement ps3 = con.prepareStatement("DELETE FROM Partides WHERE partida_id = ?");
        ps3.setInt(1, idPartida);
        ps3.executeUpdate();
        ps3.close();

        PreparedStatement ps4 = con.prepareStatement("DELETE FROM Jugadors WHERE partida_id = ?");
        ps4.setInt(1, idPartida);
        ps4.executeUpdate();
        ps4.close();
    }

    private void guardarPartida(Connection con, Partida p) throws SQLException {
        String sql = "INSERT INTO Partides (partida_id, estat, data_inici, data_fi, jugador1_id, jugador2_id, jugador3_id, jugador4_id) " +
                     "VALUES (?, ?, SYSDATE, NULL, ?, ?, ?, ?)";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, ID_PARTIDA_FIJA);
        ps.setString(2, p.isFinalizada() ? "Finalitzada" : "En curs");

        ps.setInt(3, p.getJugadores().size() > 0 ? 1 : 0);
        ps.setInt(4, p.getJugadores().size() > 1 ? 2 : 0);
        ps.setInt(5, p.getJugadores().size() > 2 ? 3 : 0);
        ps.setInt(6, p.getJugadores().size() > 3 ? 4 : 0);

        ps.executeUpdate();
        ps.close();
    }

    private void guardarJugadores(Connection con, Partida p) throws SQLException {
        int idJugador = 1;

        for (Jugador j : p.getJugadores()) {
            if (j instanceof Pinguino) {
                Pinguino ping = (Pinguino) j;

                int[] dados = convertirDadosParaBD(ping);

                String sql = "INSERT INTO Jugadors (jugador_id, nom, color_fitxa, partida_id, dau1, dau2, dau3, peixos, boles_neu) " +
                             "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

                PreparedStatement ps = con.prepareStatement(sql);
                ps.setInt(1, idJugador);
                ps.setString(2, ping.getNombre());
                ps.setString(3, ping.getColor());
                ps.setInt(4, ID_PARTIDA_FIJA);
                ps.setInt(5, dados[0]);
                ps.setInt(6, dados[1]);
                ps.setInt(7, dados[2]);
                ps.setInt(8, ping.getInv().getCantidad("pez"));
                ps.setInt(9, ping.getInv().getCantidad("bola"));
                ps.executeUpdate();
                ps.close();

                idJugador++;
            }
        }
    }

    private void guardarTurnos(Connection con, Partida p) throws SQLException {
        for (int i = 0; i < p.getJugadores().size(); i++) {
            String sql = "INSERT INTO Torns (torn_id, partida_id, jugador_id, torn_actual, ordre) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, i + 1);
            ps.setInt(2, ID_PARTIDA_FIJA);
            ps.setInt(3, i + 1);
            ps.setString(4, i == p.getJugadorActualIndice() ? "Y" : "N");
            ps.setInt(5, i + 1);
            ps.executeUpdate();
            ps.close();
        }
    }

    private void guardarTablero(Connection con, Partida p) throws SQLException {
        int idCasilla = 1;

        for (Casilla c : p.getTablero().getCasillas()) {
            String sql = "INSERT INTO Taulell (taulell_id, casella_id, tipus, posicio_x, posicio_y) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, ID_PARTIDA_FIJA);
            ps.setInt(2, idCasilla);
            ps.setString(3, convertirTipoCasilla(c));
            ps.setInt(4, c.getPosicion() % 5);
            ps.setInt(5, c.getPosicion() / 5);
            ps.executeUpdate();
            ps.close();

            idCasilla++;
        }
    }

    private String convertirTipoCasilla(Casilla c) {
        if (c instanceof Oso) return "Oso";
        if (c instanceof Agujero) return "Forat al gel";
        if (c instanceof Trineo) return "Trineu";
        if (c instanceof Evento) return "Casella interrogant";
        if (c instanceof SueloQuebradizo) return "SueloQuebradizo";
        return "Normal";
    }

    private Casilla crearCasilla(String tipo, int posicion) {
        if (tipo == null) return new Normal(posicion);

        String t = tipo.trim().toLowerCase();

        if (t.contains("oso") || t.contains("os")) {
            return new Oso(posicion);
        } else if (t.contains("forat")) {
            return new Agujero(posicion);
        } else if (t.contains("trineu")) {
            return new Trineo(posicion);
        } else if (t.contains("interrogant")) {
            return new Evento(posicion);
        } else if (t.contains("sueloquebradizo")) {
            return new SueloQuebradizo(posicion);
        } else {
            return new Normal(posicion);
        }
    }

    private Item crearDadoSegunValor(int valorBD) {
        if (valorBD >= 5) {
            return new Dado("rapido", 1, 5, 10);
        } else if (valorBD >= 1 && valorBD <= 3) {
            return new Dado("lento", 1, 1, 3);
        } else {
            return new Dado("normal", 1, 1, 6);
        }
    }

    private int[] convertirDadosParaBD(Pinguino p) {
        int[] dados = new int[] {0, 0, 0};
        int index = 0;

        int normales = p.getInv().getCantidad("normal");
        int rapidos = p.getInv().getCantidad("rapido");
        int lentos = p.getInv().getCantidad("lento");

        for (int i = 0; i < normales && index < 3; i++) {
            dados[index++] = 6;
        }
        for (int i = 0; i < rapidos && index < 3; i++) {
            dados[index++] = 10;
        }
        for (int i = 0; i < lentos && index < 3; i++) {
            dados[index++] = 3;
        }

        return dados;
    }
}