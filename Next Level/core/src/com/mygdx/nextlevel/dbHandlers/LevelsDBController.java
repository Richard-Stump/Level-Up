package com.mygdx.nextlevel.dbHandlers;

import com.mygdx.nextlevel.LevelInfo;
import com.mygdx.nextlevel.dbUtil.DBConnection;
import com.mygdx.nextlevel.enums.Tag;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LevelsDBController {
    private Connection connection;
    private final String tableName;

    /**
     * Constructor - should only be called by the children
     */
    public LevelsDBController(String tableName) {
        if (!tableName.equals("created") && !tableName.equals("downloaded")) {
            throw new NullPointerException();
        }
        //connect to the database
        try {
            connection = DBConnection.getConnection("levels");
        } catch (SQLException e) {
            //handle database errors (doesn't exist, etc)
            e.printStackTrace();
        }
        if (isDBActive()) {
            this.tableName = tableName;
        } else {
            this.tableName = null;
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

        if (searchByID(levelInfo.getId()) != null) {
            return -1;
        }

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

            //statement.setBinaryStream(9, new FileInputStream(levelInfo.getTmx()), (int) levelInfo.getTmx().length());
            //statement.setBinaryStream(10, new FileInputStream(levelInfo.getTsx()), (int) levelInfo.getTsx().length());
            //statement.setBinaryStream(11, new FileInputStream(levelInfo.getPng()), (int) levelInfo.getPng().length());


            //statement.setString(9, levelInfo.getTags().toString());
            String tagString = "";
            if (levelInfo.getTags().size() > 0) {
                for (Tag tag : levelInfo.getTags()) {
                    tagString = tagString.concat(tag.toString());
                    tagString += ",";
                }
                tagString = tagString.substring(0, tagString.length() - 1);
            }
            statement.setString(9, tagString);

            statement.setDate(10, levelInfo.getDateCreated());

            //will return the number of rows affected
            rowsChanged = statement.executeUpdate();

            if (rowsChanged > 0) {
                statement.close();
                return 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } /*catch (FileNotFoundException e) {
            System.out.println("No file was found that is associated with this levelInfo object");
            e.printStackTrace();
        }
        */
        return -1;
    }

    /**
     *  When a level retrieves new data about play count, overall rating, and best time, this function will update local
     *      data
     *  We don't want to update anything else: any modifications that a user makes to a level will be counted as a new
     *      level
     *
     * @param levelInfo level information associated with level with updated information
     * @return number of entries changed, -1 on failure
     */
    public int updateLevelInfo(LevelInfo levelInfo) {
        int rowsChanged;

        LevelInfo curr = searchByID(levelInfo.getId());

        if (curr == null) {
            return -1;
        }

        if ((curr.getPlayCount() == levelInfo.getPlayCount()) &&
                (curr.getRating() == levelInfo.getRating()) &&
                (curr.getBestTime() == levelInfo.getBestTime()) &&
                (curr.getDifficulty() == levelInfo.getDifficulty())) {
            return 0;
        }

        String sqlQuery = "UPDATE " + tableName +
                " SET bestTime = ? , " +
                "rating = ? , " +
                "playCount = ? , " +
                "difficulty = ? " +
                "WHERE id = ?;";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setFloat(1, levelInfo.getBestTime());
            statement.setFloat(2, levelInfo.getRating());
            statement.setInt(3, levelInfo.getPlayCount());
            statement.setInt(4, levelInfo.getDifficulty());
            statement.setString(5, levelInfo.getId());

            rowsChanged = statement.executeUpdate();
            statement.close();
            return rowsChanged;
        } catch (SQLException e) {
            return -1;
        }
    }

    /**
     * This function should be used when the user deletes a level from their local machine
     *
     * @param id the id to remove from the table
     * @return 1 on success, -1 on failure
     */
    public int removeLevelInfo(String id) {
        int rowsChanged;

        String sqlQuery = "DELETE FROM " + tableName +
                " WHERE id = ?;";

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
     *  This function should be used when the user deletes a level from their local machine
     *
     * @param levelInfo the level information associated with the level the user is deleting
     * @return 1 on success, -1 on failure
     */
    public int removeLevelInfo(LevelInfo levelInfo) {
        return removeLevelInfo(levelInfo.getId());
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
     * Search database by a unique ID
     *
     * @param id ID to search for
     * @return matching LevelInfo, or null if none exists
     */
    public LevelInfo searchByID(String id) {
        ResultSet resultSet;
        String sqlQuery = "SELECT * FROM " + tableName +
                " WHERE id LIKE ?;";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            //add the argument
            statement.setString(1, id);

            //execute the statement
            resultSet = statement.executeQuery();

            List<LevelInfo> resultList = resultAsList(resultSet);
            resultSet.close();

            if (resultList.size() == 1) {
                return resultList.get(0);
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
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

    /**
     *
     *
     * @param tags tags that each level is required to have
     * @return list of LevelInfo objects that contain all tags specified
     */
    public List<LevelInfo> searchByTags(List<Tag> tags) {
        ResultSet resultSet;
        String sqlQuery = "SELECT * FROM " + tableName +
                " WHERE tags LIKE ?";

        for (int i = 0; i < tags.size() - 1; i++) {
            sqlQuery = sqlQuery.concat(" AND tags LIKE ?");
        }

        sqlQuery += " ORDER BY title ASC;";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            //add the argument
            for (int i = 1; i <= tags.size(); i++) {
                statement.setString(i, "%" + tags.get(i - 1) + "%");
            }

            //execute the statement
            resultSet = statement.executeQuery();

            return resultAsList(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
    -------------- Helper functions:
     */

    /**
     * Combines 2 lists, with no repeats
     * Can probably be optimized
     * Use: multiple search parameters at the same time
     *
     * @param list1 LevelInfo list to combine
     * @param list2 LevelInfo list
     * @return combined list
     */
    public List<LevelInfo> combineLists(List<LevelInfo> list1, List<LevelInfo> list2) {
        Objects.requireNonNull(list1, "List may not be null");
        Objects.requireNonNull(list2, "List may not be null");

        ArrayList<LevelInfo> combined = new ArrayList<>(list1);

        for (LevelInfo levelToAdd: list2) {
            boolean isInList = false;
            for (LevelInfo levelInList: combined) {
                if (levelToAdd.equals(levelInList)) {
                    isInList = true;
                }
            }
            if (!isInList) {
                combined.add(levelToAdd);
            }
        }
        return combined;
    }

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
            levelInfo.setDateCreated(resultSet.getDate("dateCreated"));

            ArrayList<Tag> tags = new ArrayList<>();
            String tagString = resultSet.getString("tags");
            while (tagString.contains(",")) {
                tags.add(Tag.valueOf(tagString.substring(0, tagString.indexOf(','))));
                tagString = tagString.substring(tagString.indexOf(',') + 1);
            }
            if (!tagString.equals("")) {
                tags.add(Tag.valueOf(tagString));
                levelInfo.setTags(tags);
            }

            list.add(levelInfo);
        }
        return list;
    }

    public void close() throws SQLException {
        connection.close();
    }
}