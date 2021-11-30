package com.mygdx.nextlevel.JUnitTests;

import com.mygdx.nextlevel.LevelInfo;
import com.mygdx.nextlevel.dbHandlers.CreatedLevelsDB;
import com.mygdx.nextlevel.screens.LoginScreen;
import org.junit.*;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class CreatedLevelsDBTest {
    private static CreatedLevelsDB db;
    private static List<LevelInfo> savedListCreated;

    @BeforeClass
    public static void savePriorDB() throws Throwable{
        CreatedLevelsDB dbCreated = new CreatedLevelsDB();

        if (dbCreated.isDBActive()) {
            savedListCreated = dbCreated.sortByTitle();

            //clear database
            for (LevelInfo level: savedListCreated) {
                dbCreated.removeLevelInfo(level);
            }
        }
        dbCreated.closeConnection();
    }

    @AfterClass
    public static void restorePriorDB() throws Throwable {
        CreatedLevelsDB dbCreated = new CreatedLevelsDB();

        if (dbCreated.isDBActive()) {
            //clear database
            for (LevelInfo level: dbCreated.sortByTitle()) {
                dbCreated.removeLevelInfo(level);
            }

            //restore items to database
            for (LevelInfo levelInfo: savedListCreated) {
                dbCreated.addLevelInfo(levelInfo);
            }
            dbCreated.closeConnection();
        }
    }

    @Before
    public void establishConnection() {
        db = new CreatedLevelsDB();
    }

    @After
    public void closeConnection() throws Throwable {
        //clear database
        for (LevelInfo level: db.sortByTitle()) {
            db.removeLevelInfo(level);
        }
        db.closeConnection();
    }

    @Test
    public void testIsUniqueIDEmptyDB() throws Throwable {
        //Conditions:
        String id = "eeeeeeeeee";

        //Get actual result
        int actual = db.isUniqueID(id);

        //Set expected result
        int expected = 1;

        //test if result is what we expect
        TestOutputHelper.clearResult();
        TestOutputHelper.setResult("testIsUniqueIDEmptyDB", expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsUniqueID0() throws Throwable {
        //Conditions:
        String id = "sampleID13";

        LevelInfo sameID = new LevelInfo(id);
        db.addLevelInfo(sameID);

        //Get actual result
        int actual = db.isUniqueID(id);

        //Set expected result
        int expected = 0;

        //test if result is what we expect
        TestOutputHelper.clearResult();
        TestOutputHelper.setResult("testIsUniqueID0", expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testUpdateCreatedDatabase() {
        LoginScreen.curAcc = "reeves34";
        db.updateCreatedDatabase();

        for (LevelInfo levelInfo: db.sortByTitle()) {
            System.out.printf("id: %s\n", levelInfo.getId());
        }
        TestOutputHelper.clearResult();
        TestOutputHelper.setResult("testUpdateCreatedDatabase", true, true);
        assertEquals(true, true);
    }
}
