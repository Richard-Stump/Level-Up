package com.mygdx.nextlevel.dbHandlers;

import com.mygdx.nextlevel.dbUtil.PostgreSQLConnect;

import java.sql.Connection;
import java.sql.SQLException;

public class ServerDBHandler {
    private Connection connection;

    public ServerDBHandler() {
        connection = PostgreSQLConnect.connect();
        if (connection == null) {
            System.out.println("There was an error with connecting to the server database");
        }
    }

    /**
     * Checks if the database is active and can be accessed
     *
     * @return true if not closed, false otherwise
     */
    public boolean isDBActive() {
        try {
            return (!connection.isClosed());
        } catch (SQLException e) {
            return false;
        }
    }

    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException ignored) {
        }
    }
}
