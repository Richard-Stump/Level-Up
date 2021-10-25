package com.mygdx.nextlevel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgreSQLConnect {
    //when database is created, put the CONNECTION here!
    private static final String CONNECTION = "jdbc:postgresql:localhost/mydb";
    private final String user = "postgres";
    private final String password = "root";

    public void connect() throws SQLException {
        try (Connection conn = DriverManager.getConnection(CONNECTION, user, password)){
            if (conn != null) {
                System.out.println("Connected!");
            } else {
                System.out.println("Failed");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
