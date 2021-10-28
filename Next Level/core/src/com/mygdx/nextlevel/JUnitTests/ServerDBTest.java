package com.mygdx.nextlevel.JUnitTests;

import com.mygdx.nextlevel.dbHandlers.CreatedLevelsDB;
import com.mygdx.nextlevel.dbHandlers.ServerDBHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ServerDBTest {
    private static ServerDBHandler db;

    @Before
    public void establishConnection() {
        db = new ServerDBHandler();
    }

    @After
    public void closeConnection() {
        db.closeConnection();
    }

    @Test
    public void testIsActive() {
        //Get actual result
        boolean actual = db.isDBActive();

        //Set expected result
        boolean expected = true;

        //test if result is what we expect
        assertEquals(expected, actual);
    }
}