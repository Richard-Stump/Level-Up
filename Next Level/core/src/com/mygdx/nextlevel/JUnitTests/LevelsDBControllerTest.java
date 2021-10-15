package com.mygdx.nextlevel.JUnitTests;

import com.mygdx.nextlevel.LevelInfo;
import com.mygdx.nextlevel.LevelsDBController;
import com.mygdx.nextlevel.dbHandlers.CreatedLevelsDB;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class LevelsDBControllerTest {
    private static List<LevelInfo> priorCreatedList;

    @BeforeClass
    public static void savePriorTable() throws Throwable {
        CreatedLevelsDB tablePrior = new CreatedLevelsDB();

        priorCreatedList = tablePrior.sortByTitle();
        for (LevelInfo levelInfo: tablePrior.sortByTitle()) {
            tablePrior.removeLevelInfo(levelInfo);
        }

        tablePrior.close();
    }

    @AfterClass
    public static void restorePriorTable() throws Throwable {
        CreatedLevelsDB tablePrior = new CreatedLevelsDB();

        if (tablePrior.isDBActive()) {
            //clean up
            for (LevelInfo levelInfo : tablePrior.sortByTitle()) {
                tablePrior.removeLevelInfo(levelInfo);
            }

            //put original stuff back in
            for (LevelInfo levelInfo : priorCreatedList) {
                tablePrior.addLevelInfo(levelInfo);
            }
            tablePrior.close();
        }
    }

    @Test(expected = NullPointerException.class)
    public void testInvalidTableName() {
        //Need in order to test
        LevelsDBController db;

        //Conditions:
        String badTableName = "badTableName";

        //Should trigger the NullPointerException
        db = new LevelsDBController(badTableName);
    }

    @Test
    public void testIsConnected0() {
        LevelsDBController db;

        //Conditions:
        String validTable = "created";
        db = new LevelsDBController(validTable);

        //Actual:
        boolean actual = db.isDBActive();

        //Expected:
        boolean expected = true;

        //Verify:
        assertEquals(expected, actual);
    }

    @Test
    public void testIsConnected1() throws Throwable {
        LevelsDBController db;

        //Conditions:
        String validTable = "created";
        db = new LevelsDBController(validTable);
        db.close();

        //Actual:
        boolean actual = db.isDBActive();

        //Expected:
        boolean expected = false;

        //Verify:
        assertEquals(expected, actual);
    }

    @Test
    public void testAddLevelInfo() {
        CreatedLevelsDB tableCreated = new CreatedLevelsDB();

        //Conditions
        String id = "legitID";
        LevelInfo toAdd = new LevelInfo(id);

        //actual
        int actual = tableCreated.addLevelInfo(toAdd);

        //expected
        int expected = 1;

        //verify
        assertEquals(expected, actual); //did it return the right value?
        assertEquals(id, tableCreated.searchByID(id).getId()); //did it actually add it to the table?
    }

    /*
    @Test
    public void testRemoveLevelInfo() {
        //needed for test:
        CreatedLevelsDB tableCreated = new CreatedLevelsDB();
        String id = "AlphaGamma6";
        tableCreated.addLevelInfo(new LevelInfo(id));
    }

    @Test
    public void testUpdateLevelInfo() {

    }

     */
}
