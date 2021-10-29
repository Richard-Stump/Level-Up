package com.mygdx.nextlevel.dbUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    //private static final String CONNECTION = ;
    private static String url = "jdbc:sqlite:" + System.getProperty("user.dir") + "/Next Level/core/src/com" +
            "/mygdx/nextlevel/data/";

    public static Connection getConnection(String databaseName) throws SQLException {
        String connAll = url;
        connAll = connAll.concat(databaseName);
        connAll = connAll.concat(".sqlite");

        try {
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection(connAll);
        } catch (ClassNotFoundException e) {
            url = "jdbc:sqlite:" + System.getProperty("user.dir") + "/core/src/com" +
                    "/mygdx/nextlevel/data/";
            connAll = connAll.concat(databaseName);
            connAll = connAll.concat(".sqlite");
            try {
                Class.forName("org.sqlite.JDBC");
                return DriverManager.getConnection(connAll);
            } catch (ClassNotFoundException r) {
                r.printStackTrace();
            }
            return null;
        }
    }
}
