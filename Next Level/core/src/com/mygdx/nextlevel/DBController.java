package com.mygdx.nextlevel;

import com.mygdx.nextlevel.dbUtil.DBConnection;


import java.sql.Connection;
import java.sql.SQLException;

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

    public boolean addLevel(LevelInfo levelInfo) {
        return false;
    }
}
