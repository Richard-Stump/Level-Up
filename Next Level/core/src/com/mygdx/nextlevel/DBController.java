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
     * @return true if successfully added, false otherwise
     */
    public boolean addDownloadedLevel(LevelInfo levelInfo) throws SQLException {
        int rowsChanged;

        String sqlQuery = "INSERT INTO downloaded (title, author, bestTime, rating, difficulty, playCount, dateDownloaded)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {

            statement.setString(1, levelInfo.getTitle());
            statement.setString(2, levelInfo.getAuthor());
            statement.setFloat(3, levelInfo.getBestTime());
            statement.setFloat(4, levelInfo.getRating());
            statement.setInt(5, levelInfo.getDifficulty());
            statement.setInt(6, levelInfo.getPlayCount());
            statement.setDate(7, levelInfo.getDateDownloaded());

            //will return the number of rows affected
            rowsChanged = statement.executeUpdate();
            System.out.println(rowsChanged);

            if (rowsChanged > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}