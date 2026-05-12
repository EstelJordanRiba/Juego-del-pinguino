package controlador; //El package donde esta la clase
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import modelo.*;

public class GestorBBDD { //Clase que se puede usar en todo el proyecto
	
	//Conexion con la base de datos, tema de url, el user y la contraseña
    private static final String URL  = "jdbc:oracle:thin:@//192.168.3.26:1521/XEPDB2";
    private static final String USER = "DM1_2526_GRUP06";
    private static final String PWD  = "AGRUP06";

    //Accion para conectar la base de datos
    public Connection conectarBaseDatos() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            return DriverManager.getConnection(URL, USER, PWD);
        } catch (Exception e) { //sino se puede conectar la base de dato nos da error de conexion
            System.out.println("❌ Error connexió: " + e.getMessage());
            return null;
        }
    }

    public void cerrar(Connection con) {
        // Metodo publico que no devuelve nada (void)
        // Sirve para cerrar la conexion con la base de datos

        try {
            // Try intenta ejecutar el codigo y detectar posibles errores

            if (con != null) con.close();
            // Verificamos que la conexion exista, Si no es null, cerramos la conexion con close()

        } catch (SQLException ignored) {}     // Catch captura errores SQL en caso de que falle el cierre "ignored" significa que ignoramos el error y no mostramos nada
    }

    public int login(String nickname, String contrasenya) {
        // Metodo login que devuelve un numero entero (int)
        // Recibe: - nickname del usuario - contraseña del usuario
        // Devuelve:- el id del jugador si el login es correcto - -1 si falla

        Connection con = conectarBaseDatos();
        // Creamos una conexion con la base de datos

        if (con == null) return -1;
        // Si la conexion falla y es null, devolvemos -1 automaticamente

        try {
            // Intentamos ejecutar el login
            PreparedStatement ps = con.prepareStatement(
                "SELECT jugador_id FROM Jugadors WHERE nickname = ? AND contrasenya = ?"
            );

            // PreparedStatement prepara una consulta SQL segura Los ? son valores variables que rellenaremos despues
            // Esta consulta busca un jugador cuyo:  nickname coincida y contraseña coincida

            ps.setString(1, Encriptador.encriptar(nickname));
            // Insertamos el nickname en el primer ?  Antes lo encriptamos por seguridad

            ps.setString(2, Encriptador.encriptar(contrasenya));
            // Insertamos la contraseña en el segundo ? Tambien encriptada

            ResultSet rs = ps.executeQuery();
            // Ejecutamos la consulta SQL, ResultSet guarda el resultado obtenido de la BD

            return rs.next() ? rs.getInt("jugador_id") : -1;
            // rs.next() comprueba si existe algun resultado
            // Si existe: devuelve el jugador_id
            // Si no existe: devuelve -1
            // Esto es un operador ternario: condicion ? valorTrue : valorFalse

        } catch (SQLException e) {
            // Capturamos posibles errores SQL
            System.out.println("❌ Error login: " + e.getMessage());
            // Mostramos el mensaje de error por consola
            return -1;
            // Si hay error devolvemos -1
        } finally {
            // Finally siempre se ejecuta, haya error o no

            cerrar(con);
            // Cerramos la conexion para liberar memoria y evitar conexiones abiertas
        }
    }
    
    public int registrarJugador(String nickname, String contrasenya) {
        // Metodo publico que devuelve un numero entero (int), Sirve para registrar un nuevo jugador en la base de datos, Recibe: nickname y contraseña
        // Devuelve: el id del jugador si se registra correctamente y -1 si ocurre algun error
    	
        Connection con = conectarBaseDatos();
        
        // Creamos una conexion con la base de datos
        if (con == null) return -1;
        // Si la conexion falla devolvemos -1
        try {
            // Intentamos ejecutar el registro

            PreparedStatement psCheck = con.prepareStatement(
                "SELECT jugador_id FROM Jugadors WHERE nickname = ?"
            );
            // Preparamos una consulta SQL, Esta consulta busca si ya existe un jugador, con ese nickname

            psCheck.setString(1, Encriptador.encriptar(nickname));
            // Insertamos el nickname en el primer ? Lo encriptamos antes por seguridad

            ResultSet rs = psCheck.executeQuery();
            // Ejecutamos la consulta SQL, ResultSet guarda el resultado obtenido

            if (rs.next()) return -1;
            // rs.next() comprueba si existe algun resultado
            // Si existe significa que el nickname ya esta registrado Entonces devolvemos -1

            PreparedStatement psSeq = con.prepareStatement(
                "SELECT seq_jugador.NEXTVAL FROM DUAL"
            );
            // Creamos otra consulta SQL, NEXTVAL obtiene el siguiente numero automatico, de la secuencia seq_jugador
            // DUAL es una tabla especial de Oracle

            ResultSet rsSeq = psSeq.executeQuery();
            // Ejecutamos la consulta

            rsSeq.next();
            // Nos movemos a la primera fila del resultado

            int id = rsSeq.getInt(1);
            // Guardamos el nuevo id generado automaticamente

            PreparedStatement psIns = con.prepareStatement(
                "INSERT INTO Jugadors (jugador_id, nickname, contrasenya) VALUES (?, ?, ?)"
            );
            // Preparamos la consulta INSERT, INSERT sirve para insertar datos nuevos en la tabla

            psIns.setInt(1, id);
            // Insertamos el id en el primer ?

            psIns.setString(2, Encriptador.encriptar(nickname));
            // Insertamos el nickname encriptado

            psIns.setString(3, Encriptador.encriptar(contrasenya));
            // Insertamos la contraseña encriptada

            psIns.executeUpdate();
            // Ejecutamos el INSERT en la base de datos

            return id;
            // Si todo sale bien devolvemos el id del nuevo jugador

        } catch (SQLException e) {
            // Capturamos posibles errores SQL

            System.out.println("❌ Error registre: " + e.getMessage());
            // Mostramos el error por consola
            return -1;
            // Si hay error devolvemos -1
        } finally {
            // Finally siempre se ejecuta
            cerrar(con);
            // Cerramos la conexion con la base de datos
        }
    }
    public int guardarBBDD(Partida p, ArrayList<Integer> idsJugadors) {
        // Metodo publico que devuelve un int
        // Sirve para guardar una partida completa en la base de datos
        // Recibe:la partida, los ids de los jugadores
        // Devuelve: el id de la partida si se guarda correctamente, -1 si ocurre un error

        Connection con = conectarBaseDatos();
        // Creamos la conexion con la base de datos

        if (con == null || p == null) return -1;
        // Verificamos: que exista conexion y que la partida exista

        try {
            con.setAutoCommit(false);
            // Desactivamos el autoguardado automatico
            // para controlar manualmente las transacciones SQL

            int idPartida = crearPartida(con, p, idsJugadors);
            // Creamos la partida en la base de datos
            // y obtenemos su id

            guardarTaulell(con, p, idPartida);
            // Guardamos el tablero de la partida

            guardarParticipacions(con, p, idPartida, idsJugadors);
            // Guardamos los jugadores y sus datos

            con.commit();
            // Confirmamos todos los cambios SQL

            System.out.println("✅ Partida guardada amb ID: " + idPartida);
            // Mostramos mensaje de exito

            return idPartida;
            // Devolvemos el id de la partida

        } catch (SQLException e) {

            try {
                con.rollback();
                // Si ocurre un error, cancelamos todos los cambios SQL

            } catch (SQLException ignored) {}

            System.out.println("❌ Error guardant: " + e.getMessage());
            // Mostramos el error por consola

            return -1;
            // Devolvemos error

        } finally {
            cerrar(con);
            // Cerramos la conexion siempre
        }
    }



    private int crearPartida(Connection con, Partida p, ArrayList<Integer> idsJugadors) throws SQLException {
        // Metodo privado, Sirve para crear una nueva partida en la BD

        PreparedStatement psSeq = con.prepareStatement(
            "SELECT seq_partida.NEXTVAL FROM DUAL"
        );

        // Pedimos el siguiente numero automatico, de la secuencia de partidas

        ResultSet rs = psSeq.executeQuery();
        // Ejecutamos la consulta SQL

        rs.next();
        // Nos movemos al primer resultado

        int id = rs.getInt(1);
        // Guardamos el nuevo id de partida

        Integer guanyadorId = obtenirIdGuanyador(p, idsJugadors);
        // Obtenemos el id del ganador si existe

        PreparedStatement ps = con.prepareStatement(
            "INSERT INTO Partides (partida_id, data_inici, hora_inici, estat, guanyador_id) " +
            "VALUES (?, SYSDATE, TO_CHAR(SYSDATE,'HH24:MI:SS'), ?, ?)"
        );

        // INSERT para guardar la partida

        ps.setInt(1, id);
        // Guardamos el id de la partida

        ps.setString(2, p.isFinalizada() ? "Finalitzada" : "En curs");
        // Operador ternario: si la partida esta finalizada -> "Finalitzada", si no -> "En curs"

        if (guanyadorId != null) {
            ps.setInt(3, guanyadorId);
            // Guardamos el id del ganador

        } else {
            ps.setNull(3, Types.INTEGER);
            // Si no hay ganador guardamos NULL
        }

        ps.executeUpdate();
        // Ejecutamos el INSERT

        return id;
        // Devolvemos el id de la partida
    }

    private Integer obtenirIdGuanyador(Partida p, ArrayList<Integer> idsJugadors) {
        // Metodo privado, Busca el id del jugador ganador

        if (p == null || !p.isFinalizada() || p.getGanador() == null || idsJugadors == null) {
            return null;
        }
        // Verificamos: que exista partida, que este finalizada, que exista ganador y que existan ids

        int indexPinguino = 0;
        // Variable para recorrer los pinguinos

        for (Jugador j : p.getJugadores()) {
            // Recorremos todos los jugadores

            if (j instanceof Pinguino) {
                // Verificamos que sea un pinguino

                if (j == p.getGanador()) {
                    // Comprobamos si es el ganador

                    if (indexPinguino < idsJugadors.size()) {
                        return idsJugadors.get(indexPinguino);
                        // Devolvemos el id del ganador
                    }
                }
                indexPinguino++;
            }
        }
        return null;
        // Si no encuentra ganador devolvemos null
    }

    private void guardarTaulell(Connection con, Partida p, int idPartida) throws SQLException {
        // Metodo privado, Guarda todas las casillas del tablero

        int idCasella = 1;
        // Variable para numerar casillas

        for (Casilla c : p.getTablero().getCasillas()) {
            // Recorremos todas las casillas del tablero

            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO Taulell (partida_id, casella_id, tipus) VALUES (?, ?, ?)"
            );

            // INSERT para guardar cada casilla

            ps.setInt(1, idPartida);
            // Guardamos el id de la partida

            ps.setInt(2, idCasella);
            // Guardamos el numero de casilla

            ps.setString(3, Encriptador.encriptar(convertirTipoCasilla(c)));
            // Guardamos el tipo de casilla encriptado

            ps.executeUpdate();
            // Ejecutamos el INSERT

            idCasella++;
            // Pasamos a la siguiente casilla
        }
    }

    private void guardarParticipacions(Connection con, Partida p, int idPartida, ArrayList<Integer> idsJugadors) throws SQLException {
        // Metodo privado y Guarda los jugadores participantes y sus datos

        int indiceIdJugador = 0;
        // Variable para recorrer los ids

        for (int i = 0; i < p.getJugadores().size(); i++) {
            // Recorremos todos los jugadores

            Jugador j = p.getJugadores().get(i);
            // Obtenemos el jugador actual

            if (!(j instanceof Pinguino)) {
                continue;
            }

            // Si NO es un pinguino, saltamos ese jugador

            if (idsJugadors == null || indiceIdJugador >= idsJugadors.size()) {
                throw new SQLException("Falta el ID real para el jugador: " + j.getNombre());
            }
            // Verificamos que existan ids suficientes

            Pinguino ping = (Pinguino) j;
            // Convertimos Jugador a Pinguino

            int idJugador = idsJugadors.get(indiceIdJugador);
            // Obtenemos el id del jugador

            indiceIdJugador++;
            // Pasamos al siguiente id

            int[] dados = convertirDadosParaBD(ping);
            // Convertimos los dados para guardarlos en la BD

            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO Participacions " +
                "(partida_id, jugador_id, ordre, torn_actual, dau1, dau2, dau3, peixos, boles_neu) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"
            );
            // INSERT para guardar participaciones

            ps.setInt(1, idPartida);
            // Id de partida

            ps.setInt(2, idJugador);
            // Id del jugador

            ps.setInt(3, i + 1);
            // Orden del jugador

            ps.setString(4, i == p.getJugadorActualIndice() ? "Y" : "N");
            // Marca si es el jugador actual

            ps.setInt(5, dados[0]);
            ps.setInt(6, dados[1]);
            ps.setInt(7, dados[2]);
            // Guardamos los dados

            ps.setInt(8, ping.getInv().getCantidad("pez"));
            // Cantidad de peces

            ps.setInt(9, ping.getInv().getCantidad("bola"));
            // Cantidad de bolas de nieve

            ps.executeUpdate();
            // Ejecutamos el INSERT
        }
    }
    public Partida cargarBBDD(int idPartida) {
        // Metodo publico que devuelve una Partida
        // Sirve para cargar una partida guardada en la base de datos
        //
        // Recibe:
        // - el id de la partida
        //
        // Devuelve:
        // - la partida completa si todo va bien
        // - null si ocurre un error

        Connection con = conectarBaseDatos();
        // Creamos la conexion con la base de datos

        if (con == null) return null;
        // Si la conexion falla devolvemos null

        try {

            Partida partida = new Partida();
            // Creamos una nueva partida vacia

            ArrayList<Jugador> jugadors = new ArrayList<>();
            // Creamos una lista para guardar los jugadores

            PreparedStatement psJug = con.prepareStatement(
                "SELECT j.jugador_id, j.nickname, p.torn_actual, p.ordre, " +
                "p.dau1, p.dau2, p.dau3, p.peixos, p.boles_neu " +
                "FROM Participacions p JOIN Jugadors j ON p.jugador_id = j.jugador_id " +
                "WHERE p.partida_id = ? ORDER BY p.ordre"
            );

            // Creamos una consulta SQL, JOIN une las tablas Participacions y Jugadors, Obtenemos:
            // - nickname
            // - turno actual
            // - dados
            // - peces
            // - bolas de nieve

            psJug.setInt(1, idPartida);
            // Insertamos el id de la partida en el ?

            ResultSet rsJug = psJug.executeQuery();
            // Ejecutamos la consulta SQL

            int indexActual = 0;
            // Variable para guardar el indice del jugador actual

            int i = 0;
            // Contador del bucle

            while (rsJug.next()) {
                // Mientras existan filas en el resultado SQL

                String nickname = desencriptarSeguro(rsJug.getString("nickname"));
                // Obtenemos el nickname desencriptado

                Inventario inv = new Inventario();
                // Creamos un inventario vacio

                int d1 = rsJug.getInt("dau1");
                int d2 = rsJug.getInt("dau2");
                int d3 = rsJug.getInt("dau3");
                // Obtenemos los dados guardados

                if (d1 > 0) inv.añadirOActualizar(crearDadoSegunValor(d1), 3);
                if (d2 > 0) inv.añadirOActualizar(crearDadoSegunValor(d2), 3);
                if (d3 > 0) inv.añadirOActualizar(crearDadoSegunValor(d3), 3);

                // Si existen dados, los añadimos al inventario

                int peixos = rsJug.getInt("peixos");
                int boles = rsJug.getInt("boles_neu");
                // Obtenemos peces y bolas de nieve

                if (peixos > 0) inv.añadirOActualizar(new Pez("pez", peixos), 2);
                // Añadimos peces al inventario

                if (boles > 0) inv.añadirOActualizar(new BolaDeNieve("bola", boles), 6);
                // Añadimos bolas de nieve al inventario

                jugadors.add(new Pinguino(nickname, "blanc", 0, inv));
                // Creamos un nuevo pinguino y lo añadimos a la lista de jugadores

                if ("Y".equals(rsJug.getString("torn_actual"))) {
                    indexActual = i;
                }

                // Si el jugador es el actual, guardamos su posicion

                i++;
                // Pasamos al siguiente jugador
            }

            partida.setJugadores(jugadors);
            // Guardamos todos los jugadores en la partida

            partida.setJugadorActualIndice(indexActual);
            // Guardamos el indice del jugador actual

            ArrayList<Casilla> caselles = new ArrayList<>();
            // Creamos una lista para guardar las casillas

            PreparedStatement psTab = con.prepareStatement(
                "SELECT casella_id, tipus FROM Taulell " +
                "WHERE partida_id = ? ORDER BY casella_id"
            );

            // Consulta SQL para obtener todas las casillas del tablero

            psTab.setInt(1, idPartida);
            // Insertamos el id de la partida

            ResultSet rsTab = psTab.executeQuery();
            // Ejecutamos la consulta SQL

            while (rsTab.next()) {
                // Recorremos todas las casillas obtenidas

                String tipus = desencriptarSeguro(rsTab.getString("tipus"));
                // Obtenemos el tipo de casilla desencriptado

                caselles.add(crearCasilla(tipus, rsTab.getInt("casella_id") - 1));
                // Creamos la casilla y la añadimos al tablero
            }

            partida.getTablero().setCasillas(caselles);
            // Guardamos todas las casillas en el tablero

            return partida;
            // Devolvemos la partida cargada

        } catch (SQLException e) {

            System.out.println("❌ Error carregant: " + e.getMessage());
            // Mostramos el error SQL por consola

            return null;
            // Si ocurre error devolvemos null

        } finally {
            cerrar(con);
            // Cerramos la conexion siempre
        }
    }

    public LinkedHashMap<Integer, String> obtenirPartidesPendents() {
        // Metodo publico que devuelve un LinkedHashMap, LinkedHashMap guarda datos en formato: clave -> valor
        // En este caso: Integer -> id de partida, String -> descripcion de la partida
        // Este metodo sirve para obtener todas las partidas, que siguen "En curs" (sin finalizar)

        LinkedHashMap<Integer, String> mapa = new LinkedHashMap<>();
        // Creamos un mapa vacio donde guardaremos: id de partida y descripcion de la partida

        Connection con = conectarBaseDatos();
        // Creamos la conexion con la base de datos

        if (con == null) return mapa;
        // Si falla la conexion devolvemos el mapa vacio

        try {

            PreparedStatement ps = con.prepareStatement(
                "SELECT partida_id, data_inici, hora_inici FROM Partides " +
                "WHERE estat = 'En curs' ORDER BY data_inici DESC"
            );

            // Consulta SQL que obtiene:
            // - id de partida
            // - fecha de inicio
            // - hora de inicio
            // Solo busca partidas: WHERE estat = 'En curs'
            // ORDER BY data_inici DESC, ordena las partidas desde la mas reciente

            ResultSet rs = ps.executeQuery();
            // Ejecutamos la consulta SQL

            while (rs.next()) {
                // Recorremos todas las filas obtenidas

                int id = rs.getInt("partida_id");
                // Obtenemos el id de la partida

                String desc = "Partida #" + id + "  —  " +
                              rs.getString("data_inici") + "  " +
                              rs.getString("hora_inici");

                // Creamos una descripcion en formato texto
            
                mapa.put(id, desc);
                // Guardamos en el mapa:
                // clave -> id
                // valor -> descripcion
            }

        } catch (SQLException e) {

            System.out.println("❌ Error partides pendents: " + e.getMessage());
            // Mostramos el error SQL por consola

        } finally {

            cerrar(con);
            // Cerramos la conexion siempre
        }

        return mapa;
        // Devolvemos el mapa con todas las partidas pendientes
    }

    public ArrayList<String> obtenirRanking() {
        // Metodo publico que devuelve un ArrayList de Strings, Este metodo sirve para obtener el ranking de los jugadores con mas partidas jugadas

        ArrayList<String> ranking = new ArrayList<>();
        // Creamos una lista vacia donde guardaremos, el ranking en formato texto

        Connection con = conectarBaseDatos();
        // Creamos la conexion con la base de datos

        if (con == null) return ranking;
        // Si falla la conexion devolvemos la lista vacia

        try {

            PreparedStatement ps = con.prepareStatement(
                "SELECT nickname, partides_jugades FROM Jugadors " +
                "ORDER BY partides_jugades DESC FETCH FIRST 10 ROWS ONLY"
            );

            // Consulta SQL que obtiene:
            // - nickname
            // - numero de partidas jugadas
            // ORDER BY DESC, ordena de mayor a menor
            // FETCH FIRST 10 ROWS ONLY, limita el resultado a los 10 mejores jugadores

            ResultSet rs = ps.executeQuery(); //para insertar datos
            // Ejecutamos la consulta SQL

            int pos = 1;
            // Variable para guardar la posicion del ranking

            while (rs.next()) {
                // Recorremos todas las filas obtenidas
                String nick = desencriptarSeguro(rs.getString("nickname"));
                // Obtenemos el nickname desencriptado
                ranking.add(pos + ".  " + nick + "  —  " +
                            rs.getInt("partides_jugades") + " partides");
                // Añadimos el jugador al ranking
             
                pos++;
                // Pasamos a la siguiente posicion del ranking
            }

        } catch (SQLException e) {

            System.out.println("❌ Error ranking: " + e.getMessage());
            // Mostramos el error SQL por consola

        } finally {

            cerrar(con);
            // Cerramos la conexion siempre
        }

        return ranking;
        // Devolvemos el ranking completo
    }

    public void finalitzarPartida(int idPartida) {
        // Metodo publico que no devuelve nada (void), Sirve para marcar una partida como finalizada, en la base de datos
        // Recibe: el id de la partida

        Connection con = conectarBaseDatos();
        // Creamos la conexion con la base de datos

        if (con == null) return;
        // Si falla la conexion, salimos del metodo automaticamente

        try {

            PreparedStatement ps = con.prepareStatement(
                "UPDATE Partides SET estat = 'Finalitzada' WHERE partida_id = ?"
            );

            // Consulta SQL UPDATE
            // UPDATE sirve para modificar datos existentes
            // Aqui cambiamos el estado de la partida a "Finalitzada"
            // WHERE partida_id = ? indica que solo modificamos la partida con ese id concreto

            ps.setInt(1, idPartida);
            // Insertamos el id de la partida en el ?

            ps.executeUpdate();
            // Ejecutamos la actualizacion en la base de datos

        } catch (SQLException e) {

            System.out.println("❌ Error finalitzant: " + e.getMessage());
            // Mostramos el error SQL por consola

        } finally {

            cerrar(con);
            // Cerramos la conexion siempre
        }
    }
 // PL/SQL - FUNCIONS I PROCEDIMENTS

    public int obtenirRecordGuanyadesPLSQL() {
        // Metodo publico que devuelve un numero entero (int)
        // Sirve para obtener el record de partidas ganadas mediante una funcion PL/SQL
        
        Connection con = conectarBaseDatos();
        // Creamos una conexion con la base de datos
        
        if (con == null) return 0;
        // Si la conexion falla y es null, devolvemos 0

        try {
            // Intentamos llamar a la funcion PL/SQL
            CallableStatement cs = con.prepareCall("{ ? = call fn_record_guanyades() }");
            
            // Registramos el primer parametro como salida de tipo entero (el resultado de la funcion)
            cs.registerOutParameter(1, Types.INTEGER);
            
            cs.execute();
            // Ejecutamos la llamada
            
            return cs.getInt(1);
            // Devolvemos el valor obtenido del record

        } catch (SQLException e) {
            // Capturamos posibles errores SQL
            System.out.println("❌ Error record PL/SQL: " + e.getMessage());
            return 0;
            // Si hay error devolvemos 0
        } finally {
            // Cerramos la conexion para liberar memoria
            cerrar(con);
        }
    }

    public double obtenirMitjanaGuanyadesPLSQL() {
        // Metodo publico que devuelve un numero decimal (double)
        // Sirve para obtener la media de partidas ganadas de todos los jugadores
        
        Connection con = conectarBaseDatos();
        // Creamos una conexion con la base de datos
        
        if (con == null) return 0;
        // Si no hay conexion devolvemos 0

        try {
            // Preparamos la llamada a la funcion de la base de datos
            CallableStatement cs = con.prepareCall("{ ? = call fn_mitjana_guanyades() }");
            
            // Registramos el parametro de salida como DOUBLE
            cs.registerOutParameter(1, Types.DOUBLE);
            
            cs.execute();
            // Ejecutamos la consulta
            
            return cs.getDouble(1);
            // Devolvemos el resultado de la media

        } catch (SQLException e) {
            // Capturamos el error si falla la operacion
            System.out.println("❌ Error mitjana PL/SQL: " + e.getMessage());
            return 0;
        } finally {
            // Cerramos la conexion siempre
            cerrar(con);
        }
    }

    public double obtenirPercentatgeMenysGuanyadesPLSQL(int guanyades) {
        // Metodo que recibe un entero y devuelve un double
        // Calcula el porcentaje de jugadores que tienen menos victorias que las pasadas por parametro
        
        Connection con = conectarBaseDatos();
        // Creamos la conexion
        
        if (con == null) return 0;

        try {
            // Preparamos la llamada con un parametro de salida y uno de entrada (?)
            CallableStatement cs = con.prepareCall("{ ? = call fn_percentatge_menys_guanyades(?) }");
            
            // El primer ? es el resultado
            cs.registerOutParameter(1, Types.DOUBLE);
            
            // El segundo ? es el valor que le pasamos a la funcion
            cs.setInt(2, guanyades);
            
            cs.execute();
            // Ejecutamos
            
            return cs.getDouble(1);
            // Devolvemos el porcentaje obtenido

        } catch (SQLException e) {
            System.out.println("❌ Error percentatge PL/SQL: " + e.getMessage());
            return 0;
        } finally {
            cerrar(con);
        }
    }

    public ArrayList<String> obtenirJugadorsRecordPLSQL() {
        // Metodo que devuelve una lista de Strings
        // Obtiene los nombres de los jugadores que tienen la puntuacion record
        
        ArrayList<String> resultat = new ArrayList<>();
        // Creamos la lista vacia
        
        Connection con = conectarBaseDatos();
        // Conectamos a la BD

        if (con == null) return resultat;

        try {
            // Primero obtenemos cual es la cifra record
            int record = obtenirRecordGuanyadesPLSQL();

            // Preparamos la llamada al procedimiento que usa un cursor
            CallableStatement cs = con.prepareCall("{ call pr_jugadors_record(?, ?) }");
            
            // Pasamos el record como primer parametro
            cs.setInt(1, record);
            
            // El segundo parametro es un cursor (lista de filas)
            cs.registerOutParameter(2, Types.REF_CURSOR);
            
            cs.execute();
            // Ejecutamos el procedimiento

            // Convertimos el parametro de salida en un ResultSet para leerlo
            ResultSet rs = (ResultSet) cs.getObject(2);

            while (rs.next()) {
                // Mientras haya jugadores con el record, los desencriptamos y añadimos a la lista
                String nick = desencriptarSeguro(rs.getString("nickname"));
                int guanyades = rs.getInt("partides_guanyades");
                resultat.add(nick + " — " + guanyades + " victòries");
            }

        } catch (SQLException e) {
            resultat.add("Error: " + e.getMessage());
        } finally {
            cerrar(con);
        }

        return resultat;
        // Devolvemos la lista con los mejores jugadores
    }

    public ArrayList<String> obtenirJugadorsSobreMitjanaPLSQL() {
        // Metodo que devuelve una lista de Strings con los jugadores por encima de la media
        
        ArrayList<String> resultat = new ArrayList<>();
        Connection con = conectarBaseDatos();

        if (con == null) return resultat;

        try {
            // Llamamos al procedimiento almacenado
            CallableStatement cs = con.prepareCall("{ call pr_jugadors_sobre_mitjana(?) }");
            
            // Registramos el cursor de salida
            cs.registerOutParameter(1, Types.REF_CURSOR);
            
            cs.execute();

            ResultSet rs = (ResultSet) cs.getObject(1);

            while (rs.next()) {
                // Leemos los datos, desencriptamos el nick y lo guardamos en la lista
                String nick = desencriptarSeguro(rs.getString("nickname"));
                int guanyades = rs.getInt("partides_guanyades");
                resultat.add(nick + " — " + guanyades + " victòries");
            }

            if (resultat.isEmpty()) {
                resultat.add("Encara no hi ha jugadors per sobre de la mitjana.");
            }
        } catch (SQLException e) {
            resultat.add("Error: " + e.getMessage());
        } finally {
            cerrar(con);
        }
        return resultat;
    }

    public ArrayList<String> obtenirRankingPLSQL() {
        // Metodo que obtiene el ranking completo usando procedimientos PL/SQL
        
        ArrayList<String> resultat = new ArrayList<>();
        Connection con = conectarBaseDatos();

        if (con == null) return resultat;

        try {
            // Llamamos al procedimiento de ranking
            CallableStatement cs = con.prepareCall("{ call pr_ranking_jugadors(?) }");
            
            cs.registerOutParameter(1, Types.REF_CURSOR);
            cs.execute();

            ResultSet rs = (ResultSet) cs.getObject(1);

            int pos = 1;

            while (rs.next()) {
                // Recorremos los resultados y montamos el String del ranking
                String nick = desencriptarSeguro(rs.getString("nickname"));
                int jugades = rs.getInt("partides_jugades");
                int guanyades = rs.getInt("partides_guanyades");

                resultat.add(pos + ". " + nick + " — " + jugades + " jugades / " + guanyades + " guanyades");
                pos++;
            }

            if (resultat.isEmpty()) {
                resultat.add("Encara no hi ha ranking disponible.");
            }

        } catch (SQLException e) {
            resultat.add("Error: " + e.getMessage());
        } finally {
            cerrar(con);
        }

        return resultat;
    }

    public String obtenirPosicioJugadorPLSQL(int jugadorId) {
        // Metodo que devuelve un String con la posicion de un jugador especifico
        
        Connection con = conectarBaseDatos();

        if (con == null) return "No hi ha connexió amb la base de dades.";

        try {
            // Procedimiento que recibe un ID y devuelve un entero (posicion)
            CallableStatement cs = con.prepareCall("{ call pr_posicio_jugador(?, ?) }");
            
            cs.setInt(1, jugadorId);
            // Pasamos el ID del jugador
            
            cs.registerOutParameter(2, Types.INTEGER);
            // Registramos la posicion de salida
            
            cs.execute();

            int posicio = cs.getInt(2);
            return "La teva posició al ranking és: " + posicio;

        } catch (SQLException e) {
            return "Error controlat: " + e.getMessage();
        } finally {
            cerrar(con);
        }
    }
    public int obtenirOCrearJugadorAutomatic(String nickname) {
        // Metodo para buscar un jugador por su nick o crearlo si no existe
        
        Connection con = conectarBaseDatos();
        if (con == null) return -1;

        try {
            // Encriptamos el nickname para buscarlo
            String nickEncriptat = Encriptador.encriptar(nickname);

            // Comprobamos si el jugador ya existe
            PreparedStatement psCheck = con.prepareStatement(
                "SELECT jugador_id FROM Jugadors WHERE nickname = ?"
            );
            psCheck.setString(1, nickEncriptat);

            ResultSet rs = psCheck.executeQuery();

            if (rs.next()) {
                // Si existe, devolvemos su ID
                return rs.getInt("jugador_id");
            }

            // Si no existe, pedimos un nuevo ID a la secuencia
            PreparedStatement psSeq = con.prepareStatement(
                "SELECT seq_jugador.NEXTVAL FROM DUAL"
            );

            ResultSet rsSeq = psSeq.executeQuery();
            rsSeq.next();

            int id = rsSeq.getInt(1);

            // Insertamos el nuevo jugador con una contraseña por defecto
            PreparedStatement psIns = con.prepareStatement(
                "INSERT INTO Jugadors (jugador_id, nickname, contrasenya) VALUES (?, ?, ?)"
            );

            psIns.setInt(1, id);
            psIns.setString(2, nickEncriptat);
            psIns.setString(3, Encriptador.encriptar("1234"));

            psIns.executeUpdate(); //para actualizar datos

            return id;

        } catch (SQLException e) {
            System.out.println("❌ Error obtenint/creant jugador automàtic: " + e.getMessage());
            return -1;
        } finally {
            cerrar(con);
        }
    }
    
    // AUXILIARS

    private String desencriptarSeguro(String text) {
        // Metodo privado para desencriptar texto sin que el programa se rompa si falla
        try {
            return Encriptador.desencriptar(text);
        } catch (Exception e) {
            return text;
            // Si falla devolvemos el texto original
        }
    }

    private String convertirTipoCasilla(Casilla c) {
        // Metodo que convierte el objeto Casilla a un String para guardarlo en la base de datos
        if (c instanceof Oso) return "Oso";
        if (c instanceof Agujero) return "Forat al gel";
        if (c instanceof Trineo) return "Trineu";
        if (c instanceof Evento) return "Casella interrogant";
        if (c instanceof SueloQuebradizo) return "SueloQuebradizo";
        return "Normal";
    }
    private Casilla crearCasilla(String tipus, int posicio) {
        // Metodo que crea un objeto Casilla segun el texto leido de la base de datos
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
        // Metodo que crea un objeto Dado segun el numero de caras guardado
        if (val >= 5) return new Dado("rapido", 1, 5, 10);
        if (val >= 1 && val <= 3) return new Dado("lento", 1, 1, 3);
        return new Dado("normal", 1, 1, 6);
    }

    private int[] convertirDadosParaBD(Pinguino p) {
        // Metodo que convierte el inventario de dados del pinguino en un array de numeros para la BD
        int[] dados = {0, 0, 0}; //array para la BD
        int idx = 0;

        int normals = p.getInv().getCantidad("normal"); //mirem la cantidad que te cada pinguino de dados normales
        int rapids = p.getInv().getCantidad("rapido"); //mirem la cantidad que te cada pinguino de dados rapidos
        int lents = p.getInv().getCantidad("lento");	//mirem la cantidad que te cada pinguino de dados lentos

        // Rellenamos el array con el valor maximo de cada dado
        for (int i = 0; i < normals && idx < 3; i++) dados[idx++] = 6; //dado normal maximo 6
        for (int i = 0; i < rapids && idx < 3; i++) dados[idx++] = 10; //dado rapido maximo 10
        for (int i = 0; i < lents && idx < 3; i++) dados[idx++] = 3;   //dado lents maximo 10
        return dados;
    }
}