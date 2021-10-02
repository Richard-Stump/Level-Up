package com.mygdx.nextlevel;

import com.mygdx.nextlevel.dbUtil.DBConnection;


import java.sql.*;

public class DBController {
    private Connection connection;

    public DBController() {
        //connect to the database
        try {
            connection = DBConnection.getConnection();
        } catch (SQLException e) {
            //handle database errors (doesn't exist, etc)
            e.printStackTrace();
        }
    }

    /**
     *
     * @return true if connected, false otherwise
     */
    public boolean isDBConnected() {
        return (connection != null);
    }

    /**
     *  This will add a level into the local database containing information about downloaded levels
     *
     * @param levelInfo LevelInfo object associated with the level
     * @return 1 if successfully added, -1 otherwise
     */
    public int addDownloadedLevel(LevelInfo levelInfo) {
        int rowsChanged;

        String sqlQuery = "INSERT INTO downloaded (id, title, author, bestTime, rating, difficulty, playCount, dateDownloaded)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {

            statement.setString(1, levelInfo.getId());
            statement.setString(2, levelInfo.getTitle());
            statement.setString(3, levelInfo.getAuthor());
            statement.setFloat(4, levelInfo.getBestTime());
            statement.setFloat(5, levelInfo.getRating());
            statement.setInt(6, levelInfo.getDifficulty());
            statement.setInt(7, levelInfo.getPlayCount());
            statement.setDate(8, levelInfo.getDateDownloaded());

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
    public int updateDownloadedLevel(LevelInfo levelInfo) {
        int rowsChanged;

        String sqlQuery = "UPDATE downloaded " +
                "SET bestTime = ? " +
                "AND rating = ? " +
                "AND playCount = ? " +
                "WHERE id LIKE ?";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setFloat(1, levelInfo.getBestTime());
            statement.setFloat(2, levelInfo.getRating());
            statement.setInt(3, levelInfo.getPlayCount());
            statement.setString(4, levelInfo.getId());

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
    public int removeDownloadedLevel(LevelInfo levelInfo) {
        int rowsChanged;

        String sqlQuery = "DELETE FROM downloaded " +
                "WHERE id LIKE ?";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setString(1, levelInfo.getId());

            rowsChanged = statement.executeUpdate();
            if (rowsChanged == 1) {
                return 1;
            }
            return -1;
        } catch (SQLException e) {
            return -1;
        }
    }
}