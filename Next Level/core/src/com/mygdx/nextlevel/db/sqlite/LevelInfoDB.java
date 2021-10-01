package com.mygdx.nextlevel.db.sqlite;

import java.sql.*;

/**
 *
 */
public class LevelInfoDB {
    /**
     * Connect to database
     */
    public static void connect(String filename) {
        Connection connection = null;

        try {
            String url = "jdbc:sqlite:" + filename;

            connection = DriverManager.getConnection(url);
            System.out.println("Connected");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                    System.out.println("Closed");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void createDatabase(String filename) {
        String url = "jdbc:sqlite:" + filename;

        try {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection(url);

            if (conn != null) {
                DatabaseMetaData metaData = conn.getMetaData();
                System.out.println("Driver name: " + metaData.getDriverName());
                System.out.println("created");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        createDatabase("example.db");
        connect("example.db");
    }
}
