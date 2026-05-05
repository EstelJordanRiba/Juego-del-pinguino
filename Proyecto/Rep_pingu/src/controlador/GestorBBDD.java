package controlador;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import modelo.*;

public class GestorBBDD {

    private static final String URL  = "jdbc:oracle:thin:@//192.168.3.26:1521/XEPDB2";
    private static final String USER = "DM1_2526_GRUP06";
    private static final String PWD  = "AGRUP06";

    public Connection conectarBaseDatos() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            return DriverManager.getConnection(URL, USER, PWD);
        } catch (Exception e) {
            System.out.println("❌ Error connexió: " + e.getMessage());
            return null;
        }
    }

    public void cerrar(Connection con) {
        try {
            if (con != null) con.close();
        } catch (SQLException ignored) {}
    }

    public int login(String nickname, String contrasenya) {
        Connection con = conectarBaseDatos();
        if (con == null) return -1;

        try {
            PreparedStatement ps = con.prepareStatement(
                "SELECT jugador_id FROM Jugadors WHERE nickname = ? AND contrasenya = ?"
            );

            ps.setString(1, Encriptador.encriptar(nickname));
            ps.setString(2, Encriptador.encriptar(contrasenya));

            ResultSet rs = ps.executeQuery();

            return rs.next() ? rs.getInt("jugador_id") : -1;

        } catch (SQLException e) {
            System.out.println("❌ Error login: " + e.getMessage());
            return -1;
        } finally {
            cerrar(con);
        }
    }

    public int registrarJugador(String nickname, String contrasenya) {
        Connection con = conectarBaseDatos();
        if (con == null) return -1;

        try {
            PreparedStatement psCheck = con.prepareStatement(
                "SELECT jugador_id FROM Jugadors WHERE nickname = ?"
            );

            psCheck.setString(1, Encriptador.encriptar(nickname));

            ResultSet rs = psCheck.executeQuery();

            if (rs.next()) return -1;

            PreparedStatement psSeq = con.prepareStatement(
                "SELECT seq_jugador.NEXTVAL FROM DUAL"
            );

            ResultSet rsSeq = psSeq.executeQuery();
            rsSeq.next();

            int id = rsSeq.getInt(1);

            PreparedStatement psIns = con.prepareStatement(
                "INSERT INTO Jugadors (jugador_id, nickname, contrasenya) VALUES (?, ?, ?)"
            );

            psIns.setInt(1, id);
            psIns.setString(2, Encriptador.encriptar(nickname));
            psIns.setString(3, Encriptador.encriptar(contrasenya));

            psIns.executeUpdate();

            return id;

        } catch (SQLException e) {
            System.out.println("❌ Error registre: " + e.getMessage());
            return -1;
        } finally {
            cerrar(con);
        }
    }

    public int guardarBBDD(Partida p, ArrayList<Integer> idsJugadors) {
        Connection con = conectarBaseDatos();

        if (con == null || p == null) return -1;

        try {
            con.setAutoCommit(false);

            int idPartida = crearPartida(con, p);
            guardarTaulell(con, p, idPartida);
            guardarParticipacions(con, p, idPartida, idsJugadors);

            con.commit();

            System.out.println("✅ Partida guardada amb ID: " + idPartida);
            return idPartida;

        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException ignored) {}

            System.out.println("❌ Error guardant: " + e.getMessage());
            return -1;

        } finally {
            cerrar(con);
        }
    }

    private int crearPartida(Connection con, Partida p) throws SQLException {
        PreparedStatement psSeq = con.prepareStatement(
            "SELECT seq_partida.NEXTVAL FROM DUAL"
        );

        ResultSet rs = psSeq.executeQuery();
        rs.next();

        int id = rs.getInt(1);

        PreparedStatement ps = con.prepareStatement(
            "INSERT INTO Partides (partida_id, data_inici, hora_inici, estat) " +
            "VALUES (?, SYSDATE, TO_CHAR(SYSDATE,'HH24:MI:SS'), ?)"
        );

        ps.setInt(1, id);
        ps.setString(2, p.isFinalizada() ? "Finalitzada" : "En curs");

        ps.executeUpdate();

        return id;
    }

    private void guardarTaulell(Connection con, Partida p, int idPartida) throws SQLException {
        int idCasella = 1;

        for (Casilla c : p.getTablero().getCasillas()) {
            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO Taulell (partida_id, casella_id, tipus) VALUES (?, ?, ?)"
            );

            ps.setInt(1, idPartida);
            ps.setInt(2, idCasella);
            ps.setString(3, Encriptador.encriptar(convertirTipoCasilla(c)));

            ps.executeUpdate();

            idCasella++;
        }
    }

    private void guardarParticipacions(Connection con, Partida p, int idPartida, ArrayList<Integer> idsJugadors) throws SQLException {
        int indiceIdJugador = 0;

        for (int i = 0; i < p.getJugadores().size(); i++) {
            Jugador j = p.getJugadores().get(i);

            if (!(j instanceof Pinguino)) {
                continue;
            }

            if (idsJugadors == null || indiceIdJugador >= idsJugadors.size()) {
                throw new SQLException("Falta el ID real para el jugador: " + j.getNombre());
            }

            Pinguino ping = (Pinguino) j;
            int idJugador = idsJugadors.get(indiceIdJugador);
            indiceIdJugador++;

            int[] dados = convertirDadosParaBD(ping);

            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO Participacions " +
                "(partida_id, jugador_id, ordre, torn_actual, dau1, dau2, dau3, peixos, boles_neu) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"
            );

            ps.setInt(1, idPartida);
            ps.setInt(2, idJugador);
            ps.setInt(3, i + 1);
            ps.setString(4, i == p.getJugadorActualIndice() ? "Y" : "N");
            ps.setInt(5, dados[0]);
            ps.setInt(6, dados[1]);
            ps.setInt(7, dados[2]);
            ps.setInt(8, ping.getInv().getCantidad("pez"));
            ps.setInt(9, ping.getInv().getCantidad("bola"));

            ps.executeUpdate();
        }
    }

    public Partida cargarBBDD(int idPartida) {
        Connection con = conectarBaseDatos();

        if (con == null) return null;

        try {
            Partida partida = new Partida();

            ArrayList<Jugador> jugadors = new ArrayList<>();

            PreparedStatement psJug = con.prepareStatement(
                "SELECT j.jugador_id, j.nickname, p.torn_actual, p.ordre, " +
                "p.dau1, p.dau2, p.dau3, p.peixos, p.boles_neu " +
                "FROM Participacions p JOIN Jugadors j ON p.jugador_id = j.jugador_id " +
                "WHERE p.partida_id = ? ORDER BY p.ordre"
            );

            psJug.setInt(1, idPartida);

            ResultSet rsJug = psJug.executeQuery();

            int indexActual = 0;
            int i = 0;

            while (rsJug.next()) {
                String nickname;

                try {
                    nickname = Encriptador.desencriptar(rsJug.getString("nickname"));
                } catch (Exception e) {
                    nickname = rsJug.getString("nickname");
                }

                Inventario inv = new Inventario();

                int d1 = rsJug.getInt("dau1");
                int d2 = rsJug.getInt("dau2");
                int d3 = rsJug.getInt("dau3");

                if (d1 > 0) inv.añadirOActualizar(crearDadoSegunValor(d1), 3);
                if (d2 > 0) inv.añadirOActualizar(crearDadoSegunValor(d2), 3);
                if (d3 > 0) inv.añadirOActualizar(crearDadoSegunValor(d3), 3);

                int peixos = rsJug.getInt("peixos");
                int boles = rsJug.getInt("boles_neu");

                if (peixos > 0) inv.añadirOActualizar(new Pez("pez", peixos), 2);
                if (boles > 0) inv.añadirOActualizar(new BolaDeNieve("bola", boles), 6);

                jugadors.add(new Pinguino(nickname, "blanc", 0, inv));

                if ("Y".equals(rsJug.getString("torn_actual"))) {
                    indexActual = i;
                }

                i++;
            }

            partida.setJugadores(jugadors);
            partida.setJugadorActualIndice(indexActual);

            ArrayList<Casilla> caselles = new ArrayList<>();

            PreparedStatement psTab = con.prepareStatement(
                "SELECT casella_id, tipus FROM Taulell " +
                "WHERE partida_id = ? ORDER BY casella_id"
            );

            psTab.setInt(1, idPartida);

            ResultSet rsTab = psTab.executeQuery();

            while (rsTab.next()) {
                String tipus;

                try {
                    tipus = Encriptador.desencriptar(rsTab.getString("tipus"));
                } catch (Exception e) {
                    tipus = rsTab.getString("tipus");
                }

                caselles.add(crearCasilla(tipus, rsTab.getInt("casella_id") - 1));
            }

            partida.getTablero().setCasillas(caselles);

            return partida;

        } catch (SQLException e) {
            System.out.println("❌ Error carregant: " + e.getMessage());
            return null;
        } finally {
            cerrar(con);
        }
    }

    public LinkedHashMap<Integer, String> obtenirPartidesPendents() {
        LinkedHashMap<Integer, String> mapa = new LinkedHashMap<>();

        Connection con = conectarBaseDatos();

        if (con == null) return mapa;

        try {
            PreparedStatement ps = con.prepareStatement(
                "SELECT partida_id, data_inici, hora_inici FROM Partides " +
                "WHERE estat = 'En curs' ORDER BY data_inici DESC"
            );

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("partida_id");

                String desc = "Partida #" + id + "  —  " +
                              rs.getString("data_inici") + "  " +
                              rs.getString("hora_inici");

                mapa.put(id, desc);
            }

        } catch (SQLException e) {
            System.out.println("❌ Error partides pendents: " + e.getMessage());
        } finally {
            cerrar(con);
        }

        return mapa;
    }

    public ArrayList<String> obtenirRanking() {
        ArrayList<String> ranking = new ArrayList<>();

        Connection con = conectarBaseDatos();

        if (con == null) return ranking;

        try {
            PreparedStatement ps = con.prepareStatement(
                "SELECT nickname, partides_jugades FROM Jugadors " +
                "ORDER BY partides_jugades DESC FETCH FIRST 10 ROWS ONLY"
            );

            ResultSet rs = ps.executeQuery();

            int pos = 1;

            while (rs.next()) {
                String nick;

                try {
                    nick = Encriptador.desencriptar(rs.getString("nickname"));
                } catch (Exception e) {
                    nick = rs.getString("nickname");
                }

                ranking.add(pos + ".  " + nick + "  —  " +
                            rs.getInt("partides_jugades") + " partides");

                pos++;
            }

        } catch (SQLException e) {
            System.out.println("❌ Error ranking: " + e.getMessage());
        } finally {
            cerrar(con);
        }

        return ranking;
    }

    public void finalitzarPartida(int idPartida) {
        Connection con = conectarBaseDatos();

        if (con == null) return;

        try {
            PreparedStatement ps = con.prepareStatement(
                "UPDATE Partides SET estat = 'Finalitzada' WHERE partida_id = ?"
            );

            ps.setInt(1, idPartida);
            ps.executeUpdate();
            con.commit();

        } catch (SQLException e) {
            System.out.println("❌ Error finalitzant: " + e.getMessage());
        } finally {
            cerrar(con);
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

    private Casilla crearCasilla(String tipus, int posicio) {
        if (tipus == null) return new Normal(posicio);

        String t = tipus.toLowerCase();

        if (t.contains("oso")) return new Oso(posicio);
        if (t.contains("forat")) return new Agujero(posicio);
        if (t.contains("trineu")) return new Trineo(posicio);
        if (t.contains("interrogant")) return new Evento(posicio);
        if (t.contains("sueloquebradizo")) return new SueloQuebradizo(posicio);

        return new Normal(posicio);
    }

    private Item crearDadoSegunValor(int val) {
        if (val >= 5) return new Dado("rapido", 1, 5, 10);
        if (val >= 1 && val <= 3) return new Dado("lento", 1, 1, 3);
        return new Dado("normal", 1, 1, 6);
    }

    private int[] convertirDadosParaBD(Pinguino p) {
        int[] dados = {0, 0, 0};
        int idx = 0;

        int normals = p.getInv().getCantidad("normal");
        int rapids = p.getInv().getCantidad("rapido");
        int lents = p.getInv().getCantidad("lento");

        for (int i = 0; i < normals && idx < 3; i++) dados[idx++] = 6;
        for (int i = 0; i < rapids && idx < 3; i++) dados[idx++] = 10;
        for (int i = 0; i < lents && idx < 3; i++) dados[idx++] = 3;

        return dados;
    }
}