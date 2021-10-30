package com.mygdx.nextlevel.JUnitTests;

import com.mygdx.nextlevel.dbUtil.PostgreSQLConnect;
import org.junit.After;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class PostgreSQLConnectTest {
    @After
    public void cleanup() {
        TestOutputHelper.displayResult();
        TestOutputHelper.clearResult();
    }

    @Test
    public void testConnect() throws SQLException {
        Connection conn = PostgreSQLConnect.connect();
        boolean result = false;
        if (conn != null) {
            result = true;
        }
        TestOutputHelper.setResult("testConnect", true, result);
        assertNotNull(conn);
        conn.close();
    }
}
