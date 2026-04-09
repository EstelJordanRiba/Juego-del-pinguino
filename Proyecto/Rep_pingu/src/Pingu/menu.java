package Pingu;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Scanner;

import model.Jugador;

public class menu {
    public static Connection con;

    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);
        con = BBDD.conectarBaseDatos(scan);

        // PRINT JUGADORES
        System.out.println("=== JUGADORES ===");

        String[] colsJugador = { "ID_JUGADOR", "NICKNAME", "PARTIDAS_JUGADAS" };

        BBDD.print(con, "SELECT * FROM JUGADOR", colsJugador);

        // INSERT JUGADOR
        System.out.println("=== INSERT JUGADOR ===");

        BBDD.insert(con,
                "INSERT INTO JUGADOR VALUES (seq_jugador.NEXTVAL, 'Pingu1', '1234', 0)");

        // SELECT NORMAL
        ArrayList<String> cols = new ArrayList<>();
        cols.add("ID_JUGADOR");
        cols.add("NICKNAME");
        cols.add("PARTIDAS_JUGADAS");

        procesamientoSelect(con, "SELECT * FROM JUGADOR", cols);

        // SELECT JOIN (PRO)
        System.out.println("=== JUGADORES EN PARTIDA ===");

        ArrayList<String> colsJoin = new ArrayList<>();
        colsJoin.add("NICKNAME");
        colsJoin.add("POSICION");

        procesamientoSelect(con,
                "SELECT j.NICKNAME, p.POSICION " +
                        "FROM JUGADOR j " +
                        "JOIN PARTICIPACION p ON j.ID_JUGADOR = p.ID_JUGADOR " +
                        "WHERE p.ID_PARTIDA = 1",
                colsJoin);

        BBDD.cerrar(con);
    }

    
    // SELECT ADAPTADO

    public static void procesamientoSelect(Connection con, String sql, ArrayList<String> columnas) {

        ArrayList<LinkedHashMap<String, String>> filas = BBDD.select(con, sql);

        if (filas.isEmpty()) {
            System.out.println("No se ha encontrado nada");
        } else {

            for (LinkedHashMap<String, String> fila : filas) {

                for (String col : columnas) {

                    String valor = fila.get(col);

                    if (valor == null) {
                        System.out.println("Columna no encontrada: " + col);
                    } else {
                        procesarValor(col, valor);
                    }
                }

                System.out.println("----");
            }
        }
    }

    //  AQUÍ PUEDES CREAR OBJETOS DEL JUEGO (IMPORTANTE)
    public static void procesarValor(String col, String valor) {

        switch (col) {

            case "ID_JUGADOR":
                int id = Integer.parseInt(valor);
                System.out.println("ID: " + id);
                break;

            case "NICKNAME":
                System.out.println("Jugador: " + valor);
                break;

            case "PARTIDAS_JUGADAS":
                int partidas = Integer.parseInt(valor);
                System.out.println("Partidas jugadas: " + partidas);
                break;

            case "POSICION":
                int pos = Integer.parseInt(valor);
                System.out.println("Posición: " + pos);
                break;

            default:
                System.out.println(col + ": " + valor);
        }
    }
}