package com.mygdx.nextlevel.dbHandlers;

import com.mygdx.nextlevel.Account;
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

    /**
     * Constructor, assumes username and password that PostgreSQLConnect currently contains
     */
    public ServerDBHandler() {
        connection = PostgreSQLConnect.connect();
        if (connection == null) {
            System.out.println("There was an error with connecting to the server database");
        }
    }

    /**
     * Constructor that sets the username and password to be used to send information to the postgres server
     * Note: this is NOT the username and password of a user in the application. This is for roles in the database
     *
     * @param roleUser username to use
     * @param rolePass password to use
     */
    public ServerDBHandler(String roleUser, String rolePass) {
        PostgreSQLConnect.changeAuth(roleUser, rolePass);
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

    public void addUser(Account account) {
        String sqlQuery = "INSERT INTO api.users (username, password, email) " +
                "VALUES (?, ?, ?);";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setString(1, account.getUsername());
            statement.setString(2, account.getPassword());
            statement.setString(3, account.getEmail());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeUser(String username) {
        String sqlQuery = "DELETE FROM api.users WHERE username = ?;";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setString(1, username);

            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Most likely, you do not have the permissions to remove a user from the users table.");
            e.printStackTrace();
        }
    }

    public void clearDatabase() {
        String sqlQuery = "DELETE FROM api.users;";
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean userExists(String username) {
        ResultSet resultSet;
        String sqlQuery = "SELECT * FROM api.users WHERE username LIKE ?;";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setString(1, username);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                if (!resultSet.next()) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

//    public String getPassword(String user) {
//        ResultSet resultSet;
//        String sqlQuery = "SELECT password FROM api.users WHERE username = user;";
//        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
//            resultSet = statement.executeQuery();
//            if (resultSet.next()) {
//                return resultSet.getString("password");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return "";
//    }
    public String getPassword(String user) {
        ResultSet resultSet;
        String result = "";
        String sqlQuery = "SELECT password FROM api.users WHERE username = user;";
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                //code does not enter this conditional for some reason
                result = new String(resultSet.getString("password"));
                System.out.println("pass");
            }
            return result;

        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        }
    }
}
