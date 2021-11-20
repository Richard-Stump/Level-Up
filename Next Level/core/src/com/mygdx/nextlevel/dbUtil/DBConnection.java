package com.mygdx.nextlevel.dbUtil;

import java.io.File;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    /*
    current:
    private static String url = "jdbc:sqlite:" + System.getProperty("user.dir") + "/Next Level/core/src/com" +
            "/mygdx/nextlevel/data/";

     */

    //new:
    private static String url = "jdbc:sqlite:";

    public static Connection getConnection(String databaseName) throws SQLException {
        String connAll = url;
        connAll = connAll.concat(databaseName);
        connAll = connAll.concat(".sqlite");
        //connAll = connAll.concat("?createDatabaseIfNotExist=true");

        //create data directory for it


        try {
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection(connAll);
        } catch (Exception e) {
            /*
            url = "jdbc:sqlite:" + System.getProperty("user.dir") + "/com/mygdx/nextlevel/data/";
            connAll = url;
            connAll = connAll.concat(databaseName);
            connAll = connAll.concat(".sqlite");
            try {
                Class.forName("org.sqlite.JDBC");
                return DriverManager.getConnection(connAll);
            } catch (ClassNotFoundException r) {
                r.printStackTrace();
            }

             */
            return null;
        }
    }
}