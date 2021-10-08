package com.mygdx.nextlevel;

import com.mygdx.nextlevel.dbUtil.DBConnection;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LevelsDBController {
    private Connection connection;
    private final String tableName;

    /**
     * Constructor - should only be called by the children
     */
    public LevelsDBController(String tableName) {
        //connect to the database
        try {
            connection = DBConnection.getConnection();
        } catch (SQLException e) {
            //handle database errors (doesn't exist, etc)
            e.printStackTrace();
        }
        if (isDBConnected()) {
            this.tableName = tableName;
        } else {
            this.tableName = null;
        }
    }

    /**
     *
     * @return true if connected, false otherwise
     */
    public boolean isDBConnected() {
        return (connection != null);
    }

    /*
    -------------- Functions to modify the database:
     */

    /**
     *  This will add a level into the local levels database
     *
     * @param levelInfo LevelInfo object associated with the level
     * @return 1 if successfully added, -1 otherwise
     */
    public int addLevelInfo(LevelInfo levelInfo) {
        int rowsChanged;

        String sqlQuery = "INSERT INTO " + tableName + " (id, title, author, bestTime, rating, " +
                "difficulty, playCount, dateDownloaded, tags, dateCreated)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setString(1, levelInfo.getId());
            statement.setString(2, levelInfo.getTitle());
            statement.setString(3, levelInfo.getAuthor());
            statement.setFloat(4, levelInfo.getBestTime());
            statement.setFloat(5, levelInfo.getRating());
            statement.setInt(6, levelInfo.getDifficulty());
            statement.setInt(7, levelInfo.getPlayCount());
            statement.setDate(8, levelInfo.getDateDownloaded());
            statement.setString(9, levelInfo.getTags().toString());
            statement.setDate(10, levelInfo.getDateCreated());

            //will return the number of rows affected
            rowsChanged = statement.executeUpdate();
            System.out.println(rowsChanged);

            if (rowsChanged > 0) {
                statement.close();
                return 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     *  When a level retrieves new data about play count, overall rating, and best time, this function will update local
     *      data
     *  We don't want to update anything else: any modifications that a user makes to a level will be counted as a new
     *      level
     *
     * @param levelInfo level information associated with level with updated information
     * @return 1 on success, -1 on failure
     */
    public int updateLevelInfo(LevelInfo levelInfo) {
        int rowsChanged;

        String sqlQuery = "UPDATE ? " +
                "SET bestTime = ? " +
                "AND rating = ? " +
                "AND playCount = ? " +
                "WHERE id LIKE ?;";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setString(1, tableName);

            statement.setFloat(2, levelInfo.getBestTime());
            statement.setFloat(3, levelInfo.getRating());
            statement.setInt(4, levelInfo.getPlayCount());
            statement.setString(5, levelInfo.getId());

            rowsChanged = statement.executeUpdate();
            if (rowsChanged == 1) {
                statement.close();
                return 1;
            }
            return -1;
        } catch (SQLException e) {
            return -1;
        }
    }

    /**
     *  This function should be used when the user deletes a level from their local machine
     *
     * @param levelInfo the level information associated with the level the user is deleting
     * @return 1 on success, -1 on failure
     */
    public int removeLevelInfo(LevelInfo levelInfo) {
        int rowsChanged;

        String sqlQuery = "DELETE FROM ? " +
                "WHERE id LIKE ?";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setString(1, tableName);
            statement.setString(2, levelInfo.getId());

            rowsChanged = statement.executeUpdate();
            if (rowsChanged == 1) {
                return 1;
            }
            return -1;
        } catch (SQLException e) {
            return -1;
        }
    }

    /*
    -------------- Functions to retrieve and sort levels:
     */

    /**
     *  This function sorts the table alphabetically
     *
     * @return a list of LevelInfo objects that are sorted alphabetically by title
     */
    public List<LevelInfo> sortByTitle() {
        ResultSet resultSet;

        String sqlQuery = "SELECT * FROM " + tableName +
                " ORDER BY title ASC;";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            resultSet = statement.executeQuery();

            return resultAsList(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Sorts the list by difficulty
     * @return list of LevelInfo objects sorted from lowest to highest difficulty
     */
    public List<LevelInfo> sortByDifficulty() {
        ResultSet resultSet;

        String sqlQuery = "SELECT * FROM " + tableName +
                " ORDER BY difficulty ASC;";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            resultSet = statement.executeQuery();

            return resultAsList(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Sorts the table from highest to lowest rating
     *
     * @return list with HIGHEST RATED levels FIRST
     */
    public List<LevelInfo> sortByRating() {
        ResultSet resultSet;

        String sqlQuery = "SELECT * FROM " + tableName +
                " ORDER BY rating DESC;";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            resultSet = statement.executeQuery();

            return resultAsList(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Sorts the table from highest playCount to lowest playCount
     * @return list with most popular levels FIRST
     */
    public List<LevelInfo> sortByPlayCount() {
        ResultSet resultSet;

        String sqlQuery = "SELECT * FROM " + tableName +
                " ORDER BY playCount DESC;";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            resultSet = statement.executeQuery();

            return resultAsList(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Sorts table by the date it was downloaded
     * Uses: displaying users recently downloaded levels without any search or sort params
     *
     * @return list of LevelInfo objects sorted by date downloaded
     */
    public List<LevelInfo> sortByDateDownloaded() {
        ResultSet resultSet;

        String sqlQuery = "SELECT * FROM " + tableName +
                " ORDER BY dateDownloaded DESC;";

        //TODO: figure out how DATEs are stored!

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            resultSet = statement.executeQuery();
            return resultAsList(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Sorts table by the date a level was created
     *
     * @return list of LevelInfo objects sorted by date created
     */
    public List<LevelInfo> sortByDateCreated() {
        ResultSet resultSet;

        String sqlQuery = "SELECT * FROM " + tableName +
                " ORDER BY dateCreated DESC;";

        //TODO: figure out how DATEs are stored!

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            resultSet = statement.executeQuery();
            return resultAsList(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
    -------------- Functions to search the database for specific values:
     */

    /**
     * Searches a table for a LevelInfo object by the title
     *
     * @param title The string to search for in the title
     * @return list of LevelInfo objects that match the search
     */
    public List<LevelInfo> searchByTitle(String title) {
        return searchByString("title", title);
    }

    /**
     * Searches a table for a LevelInfo object by the author
     *
     * @param author The string to search for in the author's username
     * @return list of LevelInfo objects that match the search
     */
    public List<LevelInfo> searchByAuthor(String author) {
        return searchByString("author", author);
    }

    /**
     * Searches the table for all levels with a specific difficulty
     *
     * @param difficulty difficulty to search for
     * @return list of LevelInfo's that have the specified difficulty
     */
    public List<LevelInfo> searchByDifficulty(int difficulty) {
        ResultSet resultSet;
        String sqlQuery = "SELECT * FROM " + tableName +
                " WHERE difficulty LIKE ?" +
                " ORDER BY title ASC;";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            //add the argument
            statement.setInt(1, difficulty);

            //execute the statement
            resultSet = statement.executeQuery();

            return resultAsList(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Searches table based on the rating
     *
     * @param rating rating of the level
     * @return list of LevelInfo objects that contain the rating
     */
    public List<LevelInfo> searchByRating(float rating) {
        ResultSet resultSet;
        String sqlQuery = "SELECT * FROM " + tableName +
                " WHERE rating LIKE ?;";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            //add the argument
            statement.setFloat(1, rating);

            //execute the statement
            resultSet = statement.executeQuery();

            return resultAsList(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
    public List<LevelInfo> searchByTags(List<String> tags) {

    }

     */




    /*
    -------------- Helper functions:
     */

    /**
     * Searches a table for a LevelInfo object by the column and value.
     * Returns it sorted by title
     *
     * @param column the column to search in the table
     * @param value The value to search for
     * @return list of LevelInfo objects that match the search
     */
    private List<LevelInfo> searchByString(String column, String value) {
        ResultSet resultSet;
        String sqlQuery = "SELECT * FROM " + tableName +
                " WHERE " + column + " LIKE ?" +
                " ORDER BY title ASC;";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            //add the argument
            statement.setString(1, "%" + value + "%");

            //execute the statement
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
            LevelInfo levelInfo = new LevelInfo(resultSet.getString("id"));
            levelInfo.setTitle(resultSet.getString("title"));
            levelInfo.setAuthor(resultSet.getString("author"));
            levelInfo.setPlayCount(resultSet.getInt("playCount"));
            levelInfo.setDifficulty(resultSet.getInt("difficulty"));
            levelInfo.setRating(resultSet.getFloat("rating"));
            levelInfo.setBestTime(resultSet.getFloat("bestTime"));
            levelInfo.setDateDownloaded(resultSet.getDate("dateDownloaded"));
            list.add(levelInfo);
        }
        return list;
    }
}