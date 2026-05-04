package controlador;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import modelo.*;

public class GestorBBDD {

    private static final String ENTORNO = "fuera";
    private static final String URL_CENTRO = "jdbc:oracle:thin:@//192.168.3.26:1521/XEPDB2";
    private static final String URL_FUERA  = "jdbc:oracle:thin:@//oracle.ilerna.com:1521/XEPDB2";
    private static final String DB_USER    = "DM1_2526_GRUP06";
    private static final String DB_PWD     = "AGRUP06";

    private static final int ID_PARTIDA_FIJA = 1;

    public Connection conectarBaseDatos() {
        try {
            String url = ENTORNO.equalsIgnoreCase("centro") ? URL_CENTRO : URL_FUERA;
            Class.forName("oracle.jdbc.driver.OracleDriver");
            return DriverManager.getConnection(url, DB_USER, DB_PWD);
        } catch (Exception e) {
            System.out.println("Error conexión: " + e.getMessage());
            return null;
        }
    }

    public void cerrar(Connection con) {
        try { if (con != null) con.close(); } catch (SQLException ignored) {}
    }

    // =========================
    // GUARDAR
    // =========================

    public void guardarBBDD(Partida p) {
        Connection con = conectarBaseDatos();
        if (con == null || p == null) return;

        try {
            con.setAutoCommit(false);

            borrarDatosPartida(con, ID_PARTIDA_FIJA);
            guardarPartida(con, p);
            guardarJugadores(con, p);
            guardarTurnos(con, p);
            guardarTablero(con, p);

            con.commit();
            System.out.println("Partida guardada correctamente.");

        } catch (SQLException e) {
            try { con.rollback(); } catch (SQLException ignored) {}
            System.out.println("Error guardando: " + e.getMessage());
        } finally {
            cerrar(con);
        }
    }

    private void guardarPartida(Connection con, Partida p) throws SQLException {
        String sql = "INSERT INTO Partides (partida_id, estat, data_inici, data_fi, jugador1_id, jugador2_id, jugador3_id, jugador4_id) VALUES (?, ?, SYSDATE, NULL, ?, ?, ?, ?)";
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

                String sql = "INSERT INTO Jugadors (jugador_id, nom, color_fitxa, partida_id, dau1, dau2, dau3, peixos, boles_neu) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement ps = con.prepareStatement(sql);

                ps.setInt(1, idJugador);

                // 🔒 ENCRIPTACIÓN
                ps.setString(2, Encriptador.encriptar(ping.getNombre()));
                ps.setString(3, Encriptador.encriptar(ping.getColor()));

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

            // 🔒 ENCRIPTACIÓN
            ps.setString(3, Encriptador.encriptar(convertirTipoCasilla(c)));

            ps.setInt(4, c.getPosicion() % 5);
            ps.setInt(5, c.getPosicion() / 5);

            ps.executeUpdate();
            ps.close();

            idCasilla++;
        }
    }

    // =========================
    // CARGAR
    // =========================

    public Partida cargarBBDD(int id) {
        Connection con = conectarBaseDatos();
        if (con == null) return null;

        try {
            Partida partida = new Partida();

            ArrayList<Jugador> jugadores = new ArrayList<>();

            String sqlJugadores = "SELECT jugador_id, nom, color_fitxa, dau1, dau2, dau3, peixos, boles_neu FROM Jugadors WHERE partida_id = ? ORDER BY jugador_id";
            PreparedStatement psJug = con.prepareStatement(sqlJugadores);
            psJug.setInt(1, id);
            ResultSet rsJug = psJug.executeQuery();

            while (rsJug.next()) {

                // 🔓 DESENCRIPTAR (con fallback)
                String nombre;
                String color;

                try {
                    nombre = Encriptador.desencriptar(rsJug.getString("nom"));
                } catch (Exception e) {
                    nombre = rsJug.getString("nom");
                }

                try {
                    color = Encriptador.desencriptar(rsJug.getString("color_fitxa"));
                } catch (Exception e) {
                    color = rsJug.getString("color_fitxa");
                }

                Inventario inv = new Inventario();

                int dau1 = rsJug.getInt("dau1");
                int dau2 = rsJug.getInt("dau2");
                int dau3 = rsJug.getInt("dau3");

                if (dau1 > 0) inv.añadirOActualizar(crearDadoSegunValor(dau1), 3);
                if (dau2 > 0) inv.añadirOActualizar(crearDadoSegunValor(dau2), 3);
                if (dau3 > 0) inv.añadirOActualizar(crearDadoSegunValor(dau3), 3);

                if (rsJug.getInt("peixos") > 0)
                    inv.añadirOActualizar(new Pez("pez", rsJug.getInt("peixos")), 2);

                if (rsJug.getInt("boles_neu") > 0)
                    inv.añadirOActualizar(new BolaDeNieve("bola", rsJug.getInt("boles_neu")), 6);

                jugadores.add(new Pinguino(nombre, color, 0, inv));
            }

            partida.setJugadores(jugadores);

            // TABLERO
            ArrayList<Casilla> casillas = new ArrayList<>();

            String sqlTab = "SELECT casella_id, tipus FROM Taulell WHERE taulell_id = ? ORDER BY casella_id";
            PreparedStatement psTab = con.prepareStatement(sqlTab);
            psTab.setInt(1, id);
            ResultSet rsTab = psTab.executeQuery();

            while (rsTab.next()) {
                int posicion = rsTab.getInt("casella_id") - 1;

                String tipo;

                try {
                    tipo = Encriptador.desencriptar(rsTab.getString("tipus"));
                } catch (Exception e) {
                    tipo = rsTab.getString("tipus");
                }

                Casilla c = crearCasilla(tipo, posicion);
                if (c != null) casillas.add(c);
            }

            partida.getTablero().setCasillas(casillas);

            cerrar(con);
            return partida;

        } catch (SQLException e) {
            System.out.println("Error cargando: " + e.getMessage());
            cerrar(con);
            return null;
        }
    }

    // =========================
    // AUXILIARES (SIN CAMBIOS)
    // =========================

    private void borrarDatosPartida(Connection con, int idPartida) throws SQLException {
        con.prepareStatement("DELETE FROM Torns WHERE partida_id = " + idPartida).executeUpdate();
        con.prepareStatement("DELETE FROM Taulell WHERE taulell_id = " + idPartida).executeUpdate();
        con.prepareStatement("DELETE FROM Partides WHERE partida_id = " + idPartida).executeUpdate();
        con.prepareStatement("DELETE FROM Jugadors WHERE partida_id = " + idPartida).executeUpdate();
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

        String t = tipo.toLowerCase();

        if (t.contains("oso")) return new Oso(posicion);
        if (t.contains("forat")) return new Agujero(posicion);
        if (t.contains("trineu")) return new Trineo(posicion);
        if (t.contains("interrogant")) return new Evento(posicion);
        if (t.contains("sueloquebradizo")) return new SueloQuebradizo(posicion);

        return new Normal(posicion);
    }

    private Item crearDadoSegunValor(int valorBD) {
        if (valorBD >= 5) return new Dado("rapido", 1, 5, 10);
        if (valorBD >= 1 && valorBD <= 3) return new Dado("lento", 1, 1, 3);
        return new Dado("normal", 1, 1, 6);
    }

    private int[] convertirDadosParaBD(Pinguino p) {
        int[] dados = new int[]{0, 0, 0};
        int index = 0;

        int normales = p.getInv().getCantidad("normal");
        int rapidos = p.getInv().getCantidad("rapido");
        int lentos = p.getInv().getCantidad("lento");

        for (int i = 0; i < normales && index < 3; i++) dados[index++] = 6;
        for (int i = 0; i < rapidos && index < 3; i++) dados[index++] = 10;
        for (int i = 0; i < lentos && index < 3; i++) dados[index++] = 3;

        return dados;
    }
}