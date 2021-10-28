package com.mygdx.nextlevel.JUnitTests;

import com.mygdx.nextlevel.dbHandlers.ServerDBHandler;
import com.mygdx.nextlevel.dbUtil.PostgreSQLConnect;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class PostgreSQLConnectTest {
    @Test
    public void testConnect() throws SQLException {
        Connection conn = PostgreSQLConnect.connect();
        assertNotNull(conn);
        conn.close();
    }
}
