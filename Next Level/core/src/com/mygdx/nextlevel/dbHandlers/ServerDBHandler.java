package com.mygdx.nextlevel.dbHandlers;

import com.mygdx.nextlevel.dbUtil.PostgreSQLConnect;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    public List<String> getStrings() {
        ResultSet resultSet;
        String sqlQuery = "SELECT * FROM api.todos ORDER BY task ASC;";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            resultSet = statement.executeQuery();
            ArrayList<String> list = new ArrayList<>();

            while (resultSet.next()) {
                String thing = resultSet.getString("task");
                list.add(thing);
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
