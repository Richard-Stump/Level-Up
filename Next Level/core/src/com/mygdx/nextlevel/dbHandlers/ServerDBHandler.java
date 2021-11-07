package com.mygdx.nextlevel.dbHandlers;

import com.mygdx.nextlevel.Account;
import com.mygdx.nextlevel.LevelInfo;
import com.mygdx.nextlevel.dbUtil.PostgreSQLConnect;
import com.mygdx.nextlevel.enums.Tag;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
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

    //--------------------- User table functions ---------------------//


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

    public void clearUserTable() {
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
                //these aren't strings
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
//            System.out.println("Here");
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

    public int emailExists(String mail) {
        ResultSet resultSet;
        String sqlQuery = "SELECT * FROM api.users WHERE email LIKE ?;";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            System.out.println(mail);
            statement.setString(1, mail);
            resultSet = statement.executeQuery();

            int count = 0;
            while (resultSet.next()) {
                count++;
            }
            return count;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void changePassword(String user, String pass) {
        String sqlQuery = "UPDATE api.users SET password = ? WHERE username LIKE ?;";
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setString(1, pass);
            statement.setString(2, user);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatePassword(String user) {
        String sqlQuery = "UPDATE api.users SET password = 'password' WHERE username LIKE ?;";
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
//            System.out.println("Here");
            statement.setString(1, user);
//            System.out.println("Here2");
            statement.executeUpdate();
//            System.out.println("Here3");

        }   catch (SQLException e) {
//            System.out.println("Here4");
            e.printStackTrace();
        }
    }





    //--------------------- Level table functions ---------------------//

    /**
     * Add a level in the server database
     *
     * @param levelInfo LevelInfo of the item to add
     * @return 1 on success, 0 of failure
     */
    public int addLevel(LevelInfo levelInfo) {
        String sqlQuery = "INSERT INTO api.levels (levelid, title, author, tags, besttime, besttimeuser, datecreated) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?);";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setString(1, levelInfo.getId());
            statement.setString(2, levelInfo.getTitle());
            statement.setString(3, levelInfo.getAuthor());
            statement.setArray(4, connection.createArrayOf("text", levelInfo.getTags().toArray()));
            statement.setFloat(5, levelInfo.getBestTime());
            statement.setString(6, levelInfo.getAuthor());
            statement.setDate(7, levelInfo.getDateCreated());
            //need to get
            //statement.setBinaryStream(8, new FileInputStream(levelInfo.getTmx()), (int) levelInfo.getTmx().length());
            //statement.setBinaryStream(9, new FileInputStream(levelInfo.getTsx()), (int) levelInfo.getTsx().length());
            //statement.setBinaryStream(10, new FileInputStream(levelInfo.getPng()), (int) levelInfo.getPng().length());

            return statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * This function should be used when the user deletes a level from their local machine
     *
     * @param id the id to remove from the table
     * @return 1 on success, -1 on failure
     */
    public int removeLevel(String id) {
        int rowsChanged;

        String sqlQuery = "DELETE FROM api.levels WHERE levelid = ?;";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setString(1, id);

            rowsChanged = statement.executeUpdate();
            if (rowsChanged == 1) {
                return 1;
            }
            return -1;
        } catch (SQLException e) {
            return -1;
        }
    }

    /**
     *  This function sorts the table alphabetically
     *
     * @return a list of LevelInfo objects that are sorted alphabetically by title
     */
    public List<LevelInfo> sortByTitle() {
        ResultSet resultSet;

        String sqlQuery = "SELECT * FROM api.levels ORDER BY title ASC;";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            resultSet = statement.executeQuery();

            return resultAsList(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Takes a ResultSet and makes a List of LevelInfo's out of it
     *
     * @param resultSet raw resultset
     * @return a list of LevelInfo objects that match the search
     * @throws SQLException if there's an SQL exception
     */
    private List<LevelInfo> resultAsList(ResultSet resultSet) throws SQLException {
        List<LevelInfo> list = new ArrayList<>();

        //cycle through results and add it to the list
        while (resultSet.next()) {
            LevelInfo levelInfo = new LevelInfo(resultSet.getString("levelid"));
            levelInfo.setTitle(resultSet.getString("title"));
            levelInfo.setAuthor(resultSet.getString("author"));
            levelInfo.setPlayCount(resultSet.getInt("playcount"));
            levelInfo.setDifficulty(resultSet.getInt("difficulty"));
            levelInfo.setRating(resultSet.getFloat("rating"));
            levelInfo.setBestTime(resultSet.getFloat("besttime"));
            levelInfo.setDateDownloaded(resultSet.getDate("datecreated"));
            levelInfo.setDateCreated(resultSet.getDate("datecreated"));

            ArrayList<Tag> tags = new ArrayList<>();
            Array a = resultSet.getArray("tags");
            String[] tagStrings = (String[]) a.getArray();

            for (String tagStr: tagStrings) {
                tags.add(Tag.valueOf(tagStr));
            }
            levelInfo.setTags(tags);

            list.add(levelInfo);
        }
        return list;
    }
}
