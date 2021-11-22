package com.mygdx.nextlevel.dbHandlers;

import com.mygdx.nextlevel.Account;
import com.mygdx.nextlevel.Level;
import com.mygdx.nextlevel.LevelInfo;
import com.mygdx.nextlevel.dbUtil.PostgreSQLConnect;
import com.mygdx.nextlevel.enums.Tag;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
        } catch (SQLException | NullPointerException e) {
            return false;
        }
    }

    /**
     * Close connection to the server database
     */
    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException ignored) {
        }
    }

    //--------------------- User table functions ---------------------//


    /**
     * Add a user into the server database so they are able to log in and use the application
     *
     * @param account account to add
     */
    public void addUser(Account account) {
        String sqlQuery = "INSERT INTO api.users (username, password, email, profilepicture) " +
                "VALUES (?, ?, ?, ?);";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setString(1, account.getUsername());
            statement.setString(2, account.getPassword());
            statement.setString(3, account.getEmail());
            statement.setString(4, account.getProfilePic());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Remove a user from the database
     * TODO: also remove all the user's levels
     * If you just want to deactivate a user (and keep all the levels they uploaded), see deactivate below
     *
     * @param username username to remove
     */
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

    /**
     * Clear the entire user table
     * Note: this is not to be called unless you know what you're doing
     */
    public void clearUserTable() {
        String sqlQuery = "DELETE FROM api.users;";
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if a user exists in the server database by using the username
     *
     * @param username username to check for
     * @return true if a user exists with that username, false otherwise
     */
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

    /**
     * Activate a user in the database
     *
     * @param user
     * @return
     */
    public int activateUser(String user) {
        String sqlQuery = "UPDATE api.users SET active = true WHERE username LIKE " + user + ";";
        return 1;
    }

    /**
     * Deactivate a user in the database. Should be used when deleting an account.
     * Levels created by the user should remain in the server database.
     *
     * @param user
     * @return
     */
    public int deactivateUser(String user) {
        String sqlQuery = "UPDATE api.users SET active = false WHERE username LIKE " + user + ";";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            return statement.executeUpdate();
        } catch (SQLException e) {
            return -1;
        }
    }

    /**
     * Get a user's email address by using their username
     *
     * @param user username to search for
     * @return email address of the user, or an empty string if none found
     */
    public String getEmail(String user) {
        ResultSet resultSet;
        String result = "";
        String sqlQuery = "SELECT email FROM api.users WHERE username LIKE ?;";
        try(PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setString(1, user);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                result = resultSet.getString("email");
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Check to see if there is a user in the server database with the specified email
     *
     * @param email email to search by
     * @return the number of user's with that email (2+ indicates an error)
     */
    public int emailExists(String email) {
        ResultSet resultSet;
        String sqlQuery = "SELECT * FROM api.users WHERE email LIKE ?;";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setString(1, email);
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

    /**
     * Get a user's password from the server
     *
     * @param user username to get the password of
     * @return password of the user, empty if user doesn't exist
     */
    public String getPassword(String user) {
        ResultSet resultSet;
        String result = "";
        String sqlQuery = "SELECT password FROM api.users WHERE username LIKE ?;";
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setString(1, user);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                result = resultSet.getString("password");
            }
            return result;

        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Change a user's password
     *
     * @param user username to change the password of
     * @param pass the user's new password
     * @return 1 on success, failure otherwise
     */
    public int changePassword(String user, String pass) {
        String sqlQuery = "UPDATE api.users SET password = ? WHERE username LIKE ?;";
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setString(1, pass);
            statement.setString(2, user);
            return statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Reset the user's password to 'password'
     *
     * @param user the username to reset the password of
     * @return 1 on success, failure otherwise
     */
    public int updatePassword(String user) {
        String sqlQuery = "UPDATE api.users SET password = 'password' WHERE username LIKE ?;";
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setString(1, user);
            return statement.executeUpdate();
        }   catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public String getProfilePic(String user) {
        ResultSet resultSet;
        String result = "";
        String sqlQuery = "SELECT profilepicture FROM api.users WHERE username LIKE ?;";
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setString(1, user);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                result = resultSet.getString("profilepicture");
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        }
    }

    public void setProfilePic(String user, String picture) {
        String sqlQuery = "UPDATE api.users SET profilepicture = ? WHERE username LIKE ?;";
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setString(1, picture);
            statement.setString(2, user);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets a multidimensional array of what the user table looks like in the server database
     *
     * @return multidimensional array containing information about the users
     */
    public String[][] getUsersTable() {
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
                Array uploadedLevels = resultSet.getArray("levelsuploaded");
                table[j][3] = Arrays.toString((String[]) uploadedLevels.getArray());

                Array uploadedAssets = resultSet.getArray("assetsuploaded");
                table[j][4] = Arrays.toString((String[]) uploadedAssets.getArray());

                Array levelsCompleted = resultSet.getArray("levelscompleted");
                table[j][5] = Arrays.toString((String[]) levelsCompleted.getArray());

                table[j][6] = ((Boolean) resultSet.getBoolean("active")).toString();
                table[j][7] = resultSet.getString("profilepicture");
                j++;
            }

            resultSet.close();
            return table;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }





    //--------------------- Level table functions ---------------------//

    /**
     * Add a level in the server database
     *
     * @param levelInfo LevelInfo of the item to add
     * @return 1 on success, 0 on failure
     */
    public int addLevel(LevelInfo levelInfo) {
        String sqlQuery = "INSERT INTO api.levels (levelid, title, author, tags, besttime, besttimeuser, datecreated, tmx) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
        String sqlQuery2 = "UPDATE api.users SET levelsuploaded = array_append(levelsuploaded, ?) WHERE username=?;";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            PreparedStatement statement2 = connection.prepareStatement(sqlQuery2);

            statement.setString(1, levelInfo.getId());
            statement.setString(2, levelInfo.getTitle());
            statement.setString(3, levelInfo.getAuthor());
            statement.setArray(4, connection.createArrayOf("text", levelInfo.getTags().toArray()));
            statement.setDouble(5, levelInfo.getBestTime());
            statement.setString(6, levelInfo.getAuthor());
            statement.setDate(7, levelInfo.getDateCreated());

            //need to get
            File tmxFile = new File(levelInfo.getId() + ".tmx");
            statement.setBinaryStream(8, new FileInputStream(tmxFile), (int) tmxFile.length());
            //statement.setBinaryStream(9, new FileInputStream(levelInfo.getTsx()), (int) levelInfo.getTsx().length());
            //statement.setBinaryStream(10, new FileInputStream(levelInfo.getPng()), (int) levelInfo.getPng().length());

            statement2.setString(1, levelInfo.getId());
            statement2.setString(2, levelInfo.getAuthor());

            if (statement.executeUpdate() != 1) {
                return 0;
            }

            return statement2.executeUpdate();
        } catch (Exception e) {
            //e.printStackTrace();
            return 0;
        }
    }

    /**
     * Change a level's status from private to public
     *
     * @param id id of the level
     * @return 1 on success, 0 on failure
     */
    public int publishLevel(String id) {
        String sqlQuery = "UPDATE api.levels SET public=true WHERE levelid=?;";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setString(1, id);
            if (statement.executeUpdate() == 1) {
                return 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Change a level's status from public to private
     *
     * @param id id of the level
     * @return 1 on success, 0 on failure
     */
    public int unpublishLevel(String id) {
        String sqlQuery = "UPDATE api.levels SET public=false WHERE levelid=?;";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setString(1, id);
            if (statement.executeUpdate() == 1) {
                return 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Gets all the level ids created by a user
     *
     * @param username author to search for
     * @return Array of the level ids that the user has created
     */
    public String[] getUsersCreatedLevelsIDs(String username) {
        ResultSet resultSet;
        String sqlQuery = "SELECT levelsuploaded FROM api.users WHERE username LIKE ?;";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setString(1, username);
            resultSet = statement.executeQuery();
            resultSet.next();

            String[] toRet = (String[]) resultSet.getArray("levelsuploaded").getArray();

            resultSet.close();
            return toRet;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Gets all the LevelInfo objects of levels that a user has created
     *
     * @param username author to search for
     * @return ArrayList of LevelInfo objects describing the levels the user has created
     */
    public ArrayList<LevelInfo> getUsersCreatedLevels(String username) {
        ArrayList<LevelInfo> list = new ArrayList<>();
        for (String levelID: getUsersCreatedLevelsIDs(username)) {
            list.add(getLevelByID(levelID));
        }
        return list;
    }

    /**
     * This function should be used when the user deletes a level from their local machine
     *
     * @param id the id to remove from the table
     * @return 1 on success, -1 on failure
     */
    public int removeLevel(String id) {
        int rowsChanged;

        //get information about the level we will delete
        LevelInfo levelInfo = getLevelByID(id);

        if (levelInfo == null) {
            return -1;
        }

        String sqlQuery = "DELETE FROM api.levels WHERE levelid = ?;";
        String sqlQuery2 = "UPDATE api.users SET levelsuploaded = array_remove(levelsuploaded, ?) WHERE username=?;";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            PreparedStatement statement2 = connection.prepareStatement(sqlQuery2);
            statement.setString(1, id);
            statement2.setString(1, id);
            statement2.setString(2, levelInfo.getAuthor());

            rowsChanged = statement.executeUpdate();
            if (rowsChanged == 1) {
                statement2.executeUpdate();
                return 1;
            }
            return -1;
        } catch (SQLException e) {
            return -1;
        }
    }

    /**
     *  This function sorts all public levels in the server alphabetically
     *
     * @return a list of LevelInfo objects that are sorted alphabetically by title
     */
    public List<LevelInfo> sortPublicByTitle() {
        ResultSet resultSet;

        String sqlQuery = "SELECT * FROM api.levels ORDER BY title ASC;";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            resultSet = statement.executeQuery();

            return resultAsList(resultSet, false);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     *  This function sorts all levels in the server database, both public and private, alphabetically
     *
     * @return a list of LevelInfo objects that are sorted alphabetically by title
     */
    public List<LevelInfo> sortAllByTitle() {
        ResultSet resultSet;

        String sqlQuery = "SELECT * FROM api.levels ORDER BY title ASC;";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            resultSet = statement.executeQuery();

            return resultAsList(resultSet, true);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Gets all LevelInfo objects from an author
     *
     * @param author The string to search for in the author's username
     * @param includePrivate determines whether levels listed as private are searched for
     * @return list of LevelInfo objects that match the search
     */
    public List<LevelInfo> searchByAuthor(String author, boolean includePrivate) {
        return searchByString("levels", "author", author, includePrivate);
    }

    /**
     * Searches for all LevelInfo objects in the server database by the title
     *
     * @param title The string to search for in the title
     * @param includePrivate determines whether levels listed as private are searched for
     * @return list of LevelInfo objects that match the search
     */
    public List<LevelInfo> searchByTitle(String title, boolean includePrivate) {
        return searchByString("levels", "title", title, includePrivate);
    }

    /**
     * Searches the server database for a LevelInfo object by the column and value.
     * Returns it sorted by title
     *
     * @param column the column to search in the table
     * @param value The value to search for
     * @param includePrivate determines whether levels listed as private are searched for
     * @return list of LevelInfo objects that match the search
     */
    private List<LevelInfo> searchByString(String table, String column, String value, boolean includePrivate) {
        ResultSet resultSet;
        String sqlQuery = "SELECT * FROM api." + table +
                " WHERE " + column + " LIKE ?" +
                " ORDER BY title ASC;";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            //add the argument
            statement.setString(1, "%" + value + "%");

            //execute the statement
            resultSet = statement.executeQuery();

            return resultAsList(resultSet, includePrivate);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Retrieves a level from the database using it's id
     *
     * @param id id of the level to retrieve
     * @return level with the matching id
     */
    public LevelInfo getLevelByID(String id) {
        ResultSet resultSet;

        String sqlQuery = "SELECT * FROM api.levels WHERE levelid=?;";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setString(1, id);
            resultSet = statement.executeQuery();

            List<LevelInfo> list = resultAsList(resultSet, true);
            return list.get(0);
        } catch (Exception e) {
            //e.printStackTrace();
            return null;
        }
    }

    /**
     * Takes a ResultSet and makes a List of LevelInfo's out of it
     *
     * @param resultSet raw resultset
     * @param includePrivate determines whether levels listed as private are searched for
     * @return a list of LevelInfo objects that match the search
     * @throws SQLException if there's an SQL exception
     */
    public List<LevelInfo> resultAsList(ResultSet resultSet, boolean includePrivate) throws SQLException {
        List<LevelInfo> list = new ArrayList<>();

        //cycle through results and add it to the list
        while (resultSet.next()) {
            LevelInfo levelInfo = new LevelInfo(resultSet.getString("levelid"));
            levelInfo.setTitle(resultSet.getString("title"));
            levelInfo.setAuthor(resultSet.getString("author"));
            levelInfo.setPlayCount(resultSet.getInt("playcount"));
            levelInfo.setDifficulty(resultSet.getInt("difficulty"));
            levelInfo.setRating(getLevelAverageRating(levelInfo.getId()));
            levelInfo.setBestTime(resultSet.getFloat("besttime"));
            levelInfo.setDateDownloaded(resultSet.getDate("datecreated"));
            levelInfo.setDateCreated(resultSet.getDate("datecreated"));
            levelInfo.setPublic(resultSet.getBoolean("public"));


            String filename = levelInfo.getId();
            byte[] tmxBytes = resultSet.getBytes("tmx");
            try {
                FileOutputStream fosTmx = new FileOutputStream(filename + ".tmx");
                fosTmx.write(tmxBytes);
                fosTmx.close();
            } catch (Exception e) {
                System.out.println("Couldn't save the tmx from the server");
                e.printStackTrace();
            }

            /*
            //levelInfo.setTmx(resultSet.getBinaryStream());
            //levelInfo.setTsx();
            //levelInfo.setPng();
             */

            ArrayList<Tag> tags = new ArrayList<>();
            Array a = resultSet.getArray("tags");
            String[] tagStrings = (String[]) a.getArray();

            for (String tagStr: tagStrings) {
                tags.add(Tag.valueOf(tagStr));
            }
            levelInfo.setTags(tags);

            if (levelInfo.isPublic()) {
                list.add(levelInfo);
            } else {
                if (includePrivate) {
                    list.add(levelInfo);
                }
            }
        }
        return list;
    }

    /**
     * Gets all the ratings that a level has
     *
     * @param id id of the level to search for
     * @return list containing all the ratings
     */
    public ArrayList<Double> getLevelRatings(String id) {
        ResultSet resultSet;
        ArrayList<Double> list = new ArrayList<>();
        String sqlQuery = "SELECT rating FROM api.levels WHERE levelid=?;";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setString(1, id);
            resultSet = statement.executeQuery();
            resultSet.next();
            Array array = resultSet.getArray("rating");
            Object[] dArray = (Object[]) array.getArray();

            for(Object doubObj: dArray) {
                list.add(Double.parseDouble(doubObj.toString()));
            }

            return list;
        } catch (Exception e) {
            //e.printStackTrace();
            return null;
        }
    }

    /**
     * Get the number of people who have rated the level
     * @param id level id
     * @return number of people that rated the level
     */
    public int getRatingCount(String id) {
        return getLevelRatings(id).size();
    }

    /**
     * Get the average level rating
     *
     * @param id level to get average rating
     * @return average rating, or -1 if there are no ratings on the level
     */
    public float getLevelAverageRating(String id) {
        ArrayList<Double> ratings = getLevelRatings(id);
        if (ratings == null) {
            return -1;
        }
        float sum = 0;

        for (Double rating: ratings) {
            sum += rating;
        }

        return sum / ratings.size();
    }

    public double getRecordTime(String id) {
        ResultSet resultSet;
        double ret = -1.0;

        String sqlQuery = "SELECT besttime FROM api.levels WHERE levelid=?;";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setString(1, id);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                ret = resultSet.getDouble("besttime");
            }
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
            return ret;
        }
    }

    public void updateRecordTime(String id, double time) {
        String sqlQuery = "UPDATE api.levels SET besttime = ? WHERE levelid = ?;";
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setDouble(1, time);
            statement.setString(2, id);
            statement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Appends a rating to the levels list of ratings
     *
     * @param id
     * @param toAdd
     * @return
     */
    public int addLevelRating(String id, double toAdd) {
        String sqlQuery = "UPDATE api.levels SET rating = array_append(rating, ?) WHERE levelid=?;";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setDouble(1, toAdd);
            statement.setString(2, id);
            return statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Increase the level's play count by 1 in the server database
     *
     * @param id id of the level
     * @return 1 on success, -1 on failure
     */
    public int increaseLevelPlayCount(String id) {
        String sqlQuery = "UPDATE api.levels SET playcount = playcount+1 WHERE levelid=?;";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setString(1, id);
            return statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
