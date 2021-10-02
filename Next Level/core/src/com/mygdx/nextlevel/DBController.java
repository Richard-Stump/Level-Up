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
            e.printStackTrace();
        }
    }
}
