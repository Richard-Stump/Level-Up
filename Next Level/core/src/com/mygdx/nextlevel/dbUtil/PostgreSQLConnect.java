package com.mygdx.nextlevel.dbUtil;

import java.net.URL;
import java.net.URLClassLoader;
import java.sql.*;
import java.util.Properties;

public class PostgreSQLConnect {
    private static final String url = "jdbc:postgresql://localhost:5433/postgres";
    //start with the web_anon user
    private static String user = "authenticator";
    private static String pass = "mysecretpassword";

    /**
     * Connects to the database with the current username and password
     *
     * @return Connection to database, or null on failure
     */
    public static Connection connect() {
        Properties props = new Properties();
        props.setProperty("user", user);
        props.setProperty("password", pass);
        //props.setProperty("ssl", "true");

        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(url, props);
            if (conn != null) {
                System.out.println("Connected!");
            } else {
                System.out.println("Failed");
            }
            return conn;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Changes the credentials to use to access the postgrest database
     *
     * @param username username to change
     * @param password password to change
     */
    public static void changeAuth(String username, String password) {
        user = username;
        pass = password;
    }
}
