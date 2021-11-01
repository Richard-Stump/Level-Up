package com.mygdx.nextlevel.dbHandlers;

import com.mygdx.nextlevel.Account;
import com.mygdx.nextlevel.dbUtil.PostgreSQLConnect;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;
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

            if (resultSet.next()) { //if there is at least one user found
                if (!resultSet.next()) { //if there is only one user found
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String[][] getTable() {
        String[][] table;
        ResultSet resultSet;
        String sqlQuery = "SELECT * FROM api.users;";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            resultSet = statement.executeQuery();
            int numResults = 0;

            while (resultSet.next()) {
                numResults++;
            }
            resultSet.close();

            resultSet = statement.executeQuery();
            table = new String[numResults][8];

            int j = 0;
            while (resultSet.next()) {
                table[j][0] = resultSet.getString("username");
                table[j][1] = resultSet.getString("password");
                table[j][2] = resultSet.getString("email");
                //table[j][3] = resultSet.getString("levelsuploaded");
                //table[j][4] = resultSet.getString("assetsuploaded");
                //table[j][5] = resultSet.getString("profilepicture");
                //table[j][6] = resultSet.getString("levelscompleted");
                table[j][7] = ((Boolean) resultSet.getBoolean("active")).toString();
                j++;
            }

            resultSet.close();
            return table;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getPassword(String user) {
        ResultSet resultSet;
        String result = "";
        String sqlQuery = "SELECT password FROM api.users WHERE username LIKE ?;";
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            System.out.println("Here");
            statement.setString(1, user);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
//                System.out.println("Enter");
                //code does not enter this conditional for some reason
                result = resultSet.getString("password");
//                System.out.println(result);
            }
            return result;

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Exception");
            return "";
        }
    }

    public void updatePassword(String user) {
        String sqlQuery = "UPDATE api.users SET password = 'password' WHERE username LIKE ?;";
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setString(1, user);
            statement.executeQuery();
        }   catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
