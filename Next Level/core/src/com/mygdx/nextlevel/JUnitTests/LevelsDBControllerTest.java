package com.mygdx.nextlevel.JUnitTests;

import com.mygdx.nextlevel.LevelInfo;
import com.mygdx.nextlevel.dbHandlers.LevelsDBController;
import com.mygdx.nextlevel.dbHandlers.CreatedLevelsDB;
import org.junit.*;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class LevelsDBControllerTest {
    private static List<LevelInfo> priorCreatedList;
    private static final String arbitraryID = "AlphaGamma6";

    @BeforeClass
    public static void savePriorTable() throws Throwable {
        CreatedLevelsDB tablePrior = new CreatedLevelsDB();

        priorCreatedList = tablePrior.sortByTitle();
        for (LevelInfo levelInfo: tablePrior.sortByTitle()) {
            tablePrior.removeLevelInfo(levelInfo);
        }

        tablePrior.closeConnection();
    }

    @AfterClass
    public static void restorePriorTable() throws Throwable {
        CreatedLevelsDB tablePrior = new CreatedLevelsDB();

        if (tablePrior.isDBActive()) {
            //put original stuff back in
            for (LevelInfo levelInfo : priorCreatedList) {
                tablePrior.addLevelInfo(levelInfo);
            }
            tablePrior.closeConnection();
        }
    }

    @After
    public void resetTable() throws Throwable {
        CreatedLevelsDB tablePrior = new CreatedLevelsDB();
        if (tablePrior.isDBActive()) {
            //clean up
            for (LevelInfo levelInfo : tablePrior.sortByTitle()) {
                tablePrior.removeLevelInfo(levelInfo);
            }
            tablePrior.closeConnection();
        }
        TestOutputHelper.displayResult();
        TestOutputHelper.clearResult();
    }

    @Test
    public void testInvalidTableName() {
        //Need in order to test
        LevelsDBController db = null;

        //Conditions:
        String badTableName = "badTableName";

        //Should trigger the NullPointerException
        try {
            db = new LevelsDBController(badTableName);
            TestOutputHelper.setResult("testInvalidTableName", false, true);
        } catch (NullPointerException e) {
            TestOutputHelper.setResult("testInvalidTableName", false, false);
        }
    }

    @Test
    public void testIsConnected() {
        LevelsDBController db;

        //Conditions:
        String validTable = "created";
        db = new LevelsDBController(validTable);

        //Actual:
        boolean actual = db.isDBActive();

        //Expected:
        boolean expected = true;

        //Verify:
        TestOutputHelper.setResult("testIsConnected", expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsConnectedWhenClosed() throws Throwable {
        LevelsDBController db;

        //Conditions:
        String validTable = "created";
        db = new LevelsDBController(validTable);
        db.closeConnection();

        //Actual:
        boolean actual = db.isDBActive();

        //Expected:
        boolean expected = false;

        //Verify:
        TestOutputHelper.setResult("testIsConnectedWhenClosed", expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testAddLevelInfo() {
        CreatedLevelsDB tableCreated = new CreatedLevelsDB();

        //Conditions
        LevelInfo toAdd = new LevelInfo(arbitraryID, "title", "author");

        //actual
        int actual = tableCreated.addLevelInfo(toAdd);

        //expected
        int expected = 1;

        //verify
        TestOutputHelper.setResult("testAddLevelInfo", expected, actual);
        assertEquals(expected, actual); //did it return the right value?
        assertEquals(arbitraryID, tableCreated.searchByID(arbitraryID).getId()); //did it actually add it to the table?
    }

    @Test
    public void testAddLevelInfoIdentical() {
        CreatedLevelsDB tableCreated = new CreatedLevelsDB();

        //Conditions
        LevelInfo toAdd0 = new LevelInfo(arbitraryID, "title", "author");
        LevelInfo toAdd1 = new LevelInfo(arbitraryID, "title", "author");
        tableCreated.addLevelInfo(toAdd0);

        //actual
        int actual = tableCreated.addLevelInfo(toAdd1);

        //expected
        int expected = -1;

        //verify
        TestOutputHelper.setResult("testAddLevelInfoIdentical", expected, actual);
        assertEquals(expected, actual); //did it return the right value?
        assertEquals(1, tableCreated.sortByTitle().size()); //did it add the second one to the table?
    }

    @Test
    public void testRemoveLevelInfo() {
        //needed for test:
        CreatedLevelsDB tableCreated = new CreatedLevelsDB();
        tableCreated.addLevelInfo(new LevelInfo(arbitraryID, "title", "author"));

        //actual
        int actual = tableCreated.removeLevelInfo(arbitraryID);

        //expected
        int expected = 1;

        //verify
        TestOutputHelper.setResult("testRemoveLevelInfo", expected, actual);
        assertEquals(expected, actual); //did it return correct value?
        assertEquals(0, tableCreated.sortByTitle().size()); //is the list empty?
    }

    @Test
    public void testRemoveLevelInfoFromEmptyTable() {
        //needed for test:
        CreatedLevelsDB tableCreated = new CreatedLevelsDB();

        //actual
        int actual = tableCreated.removeLevelInfo(arbitraryID);

        //expected
        int expected = -1;

        //verify
        TestOutputHelper.setResult("testRemoveLevelInfoFromEmptyTable", expected, actual);
        assertEquals(expected, actual); //did it return correct value?
        assertEquals(0, tableCreated.sortByTitle().size()); //is the list empty?
    }

    @Test
    public void testRemoveLevelInfoThatDoesntExist() {
        CreatedLevelsDB tableCreated = new CreatedLevelsDB();
        int actual = tableCreated.removeLevelInfo("aaaaaaaaaaaa783aaaaa46523");
        int expected = -1;

        TestOutputHelper.setResult("testRemoveLevelInfoThatDoesntExist", expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testUpdateLevelInfo() {
        //needed for test:
        CreatedLevelsDB tableCreated = new CreatedLevelsDB();
        LevelInfo levelInfo;

        //conditions
        int originalPlayCount = 0;
        int newPlayCount = 1;
        levelInfo = new LevelInfo(arbitraryID);
        levelInfo.setPlayCount(originalPlayCount);
        tableCreated.addLevelInfo(levelInfo);

        //actual
        levelInfo.setPlayCount(newPlayCount);
        int actual = tableCreated.updateLevelInfo(levelInfo);

        //expected
        int expected = 1;

        //verify
        TestOutputHelper.setResult("testUpdateLevelInfo", expected, actual);
        assertEquals(expected, actual); //did it return correctly?
        assertEquals(newPlayCount, tableCreated.searchByID(arbitraryID).getPlayCount()); //did it change the play count?
    }

    @Test
    public void testUpdateLevelInfoNotInTable() {
        //needed for test:
        CreatedLevelsDB tableCreated = new CreatedLevelsDB();
        LevelInfo levelInfo;

        //conditions
        int newPlayCount = 69;
        levelInfo = new LevelInfo(arbitraryID);

        //actual
        levelInfo.setPlayCount(newPlayCount);
        int actual = tableCreated.updateLevelInfo(levelInfo);

        //expected
        int expected = -1;

        //verify
        TestOutputHelper.setResult("testUpdateLevelInfoNotInTable", expected, actual);
        assertEquals(expected, actual); //did it return correctly?
        assertEquals(0, tableCreated.sortByTitle().size()); //did it change the table at all?
    }

    //TODO: more tests
}
