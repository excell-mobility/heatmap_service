package service;

import model.Cell;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Sebastian Urbanek on 05.10.16.
 */
@Component
public class DatabaseService {

    public static final boolean LOCAL_MODE = false;
    private static String JDBC_DRIVER;

    private String sqlQuery;

    public DatabaseService() {
        if (LOCAL_MODE) {
            JDBC_DRIVER = "org.postgresql.Driver";
            this.sqlQuery = "SELECT * FROM heatmap_data_10 WHERE " +
                    "time = '2014-12-24 20:00:00';";
        } else {
            JDBC_DRIVER = "com.cloudera.impala.jdbc4.Driver";
            this.sqlQuery = "SELECT * FROM heatmap_data_10 WHERE time='2014-12-24 18:00:00';";
        }
    }

    public DatabaseService(String sqlQuery) {
        this.sqlQuery = sqlQuery;
    }

    public void setSQLQuery(String sqlQuery) {
        this.sqlQuery = sqlQuery;
    }

    public String getSqlQuery() {
        return this.sqlQuery;
    }

    public Cell[] startRequest() {
        if (LOCAL_MODE) {
            return transferRequestToPostgres();
        } else {
            return transferRequestToImpala();
        }
    }

    private Cell[] transferRequestToPostgres() {
        try {
            // Datenbank-Treiber laden
            Class.forName(JDBC_DRIVER);
            // Verbindung zur Datenbank herstellen und Datenbank auswählen
            Connection connection = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/heatmap","excell_user", "heatmap");
            // Anfrage an Datenbank stellen und Antwort-Objekt erhalten
            ResultSet rs = connection.createStatement().executeQuery(sqlQuery);

            // Auswerten des SQL-Ergebnisses
            Cell[] grid = parseResult(rs);

            // Verbindung beenden
            connection.close();
            // Liste als Array von Zellen zurücksenden.
            return grid;
        } catch (ClassNotFoundException e) {
            System.err.println("Fehler beim Laden des Datenbank-Treibers.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Fehler beim Ausführen der Datenbank-Anfrage (SQL)");
            e.printStackTrace();
        }
        return null;
    }

    private Cell[] transferRequestToImpala() {
        try {
            System.out.println("Anfrage an Impala gestartet.");
            System.out.println("DRIVER: " + JDBC_DRIVER);
            System.out.println("SQL: " + this.sqlQuery);
            // Datenbank-Treiber laden
            Class.forName(JDBC_DRIVER);
            // Verbindung zur Datenbank herstellen
            java.sql.Connection connection = DriverManager
                    .getConnection("jdbc:hive2://141.64.5.201:21050/;auth=noSasl");
            // Datenbank "taxidata" nutzen
            connection.createStatement().execute("use heatmap;");

            // Anfrage an Datenbank stellen und Antwort-Objekt erhalten
            ResultSet rs = connection.createStatement().executeQuery(sqlQuery);

            // Auswerten des SQL-Ergebnisses
            Cell[] grid = parseResult(rs);

            // Verbindung beenden
            connection.close();
            // Liste als Array von Zellen zurücksenden.
            return grid;
        } catch (ClassNotFoundException e) {
            System.err.println("Fehler beim Laden des Datenbank-Treibers.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Fehler beim Ausführen der Datenbank-Anfrage (SQL)");
            e.printStackTrace();
        }
        return null;
    }

    private Cell[] parseResult(ResultSet rs) {
        // Auswerten des SQL-Ergebnisses
        ArrayList<Cell> cellsList = new ArrayList<Cell>();
        try {
            while (rs.next()) {
                float lat1 = rs.getFloat("lat1");
                float lng1 = rs.getFloat("lng1");
                float lat2 = rs.getFloat("lat2");
                float lng2 = rs.getFloat("lng2");
                int verhicleCount = rs.getInt("vehicle_count");
                int averageVelocity = rs.getInt("average_velocity");
                Cell cell = new Cell(lat1, lng1, lat2, lng2, verhicleCount, averageVelocity);
                cellsList.add(cell);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cellsList.toArray(new Cell[cellsList.size()]);
    }
}
