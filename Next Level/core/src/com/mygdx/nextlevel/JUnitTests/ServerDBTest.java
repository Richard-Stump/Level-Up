package com.mygdx.nextlevel.JUnitTests;

import com.mygdx.nextlevel.dbHandlers.CreatedLevelsDB;
import com.mygdx.nextlevel.dbHandlers.ServerDBHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

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

    //change this to a useful test once established success
    @Test
    public void testGetTasks() {
        ArrayList<String> tasks = new ArrayList<>(db.getStrings());

        for (String s: tasks) {
            System.out.println(s);
        }
    }
}