package com.mygdx.nextlevel.dbUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String CONNECTION = "jdbc:sqlite:core/main/NextLevel/core/src/com" +
            "/mygdx/nextlevel/data/levels.sqlite";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection(CONNECTION);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
