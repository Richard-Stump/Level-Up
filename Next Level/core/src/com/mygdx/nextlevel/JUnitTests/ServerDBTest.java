package com.mygdx.nextlevel.JUnitTests;

import com.badlogic.gdx.Gdx;
import com.mygdx.nextlevel.Account;
import com.mygdx.nextlevel.LevelInfo;
import com.mygdx.nextlevel.dbHandlers.CreatedLevelsDB;
import com.mygdx.nextlevel.dbHandlers.ServerDBHandler;
import com.mygdx.nextlevel.dbUtil.PostgreSQLConnect;
import org.junit.*;
import org.postgresql.util.PSQLException;

import java.io.File;
import java.nio.channels.AcceptPendingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ServerDBTest {
    private static ServerDBHandler db;
    private static final double DELTA = 1e-15;

    @BeforeClass
    public static void populateDatabase() {
        //TODO: save old database, and put in a sample database
        //also probably not a good idea to run when application is in production...

        //put in temporary account
        Account steveAccount = new Account("steve", "abcd1234!", "steve@example.com");
//        steveAccount.setUsername("steve");
//        steveAccount.setEmail("steve@example.com");
//        steveAccount.setPassword("abcd1234!");
        db = new ServerDBHandler();
        db.addUser(steveAccount);

        LevelInfo levelInfo = new LevelInfo("id123456789", "Test Level 1", "reeves34");
        db.addLevel(levelInfo);

        db.closeConnection();
    }

    @AfterClass
    public static void restoreDatabase() {
        ServerDBHandler adminDB = new ServerDBHandler();
        adminDB.removeUser("steve");
        adminDB.removeLevel("id12adsfg");
        adminDB.closeConnection();
    }

    @Before
    public void establishConnection() {
        db = new ServerDBHandler();
        LevelInfo info = new LevelInfo("idtestgetrecordtime", "TestGetRecordTime", "jchen");
        Account a = new Account("testuser", "password", "testuser@example.com", "default");
//        a.setProfilePic("default");
        db.addLevel(info);
        db.addUser(a);
    }

    @After
    public void cleanup() {
        db.removeLevel("idtestgetrecordtime");
        db.removeUser("testuser");
        db.closeConnection();
        TestOutputHelper.displayResult();
        TestOutputHelper.clearResult();
    }

    @Test
    public void testIsActive() {
        //Get actual result
        boolean actual = db.isDBActive();

        //Set expected result
        boolean expected = true;

        //test if result is what we expect
        TestOutputHelper.setResult("testIsActive", expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testUserExistsFalse() {
        boolean actual = db.userExists("john");
        boolean expected = false;

        TestOutputHelper.setResult("testUserExistsFalse", expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testUserExistsTrue() {
        boolean actual = db.userExists("steve");
        boolean expected = true;

        TestOutputHelper.setResult("testUserExistsTrue", expected, actual);
        assertEquals(expected, actual);
    }

    /*
    @Test
    public void testGetTable() {
        //TODO: make this into an actual test
        //System.out.println("get table: ");
        String[][] table = db.getTable();

        for (String[] strings : table) {
            for (int j = 0; j < table[0].length; j++) {
                //System.out.print(strings[j] + ", ");
            }
            //System.out.println();
        }
        //System.out.println();
        assertTrue(true);
    }

     */

    @Test
    public void testAddUser() {
        Account testAccount = new Account("erwin", "puppyDogs5000", "erwin@example.com");
        String username = "erwin";
//        testAccount.setUsername(username);
//        testAccount.setPassword("puppyDogs5000");
//        testAccount.setEmail("erwin@example.com");

        db.addUser(testAccount);

        boolean actual = db.userExists(username);
        boolean expected = true;

        //TODO: for security reasons, we probably want to change how the switch to admin occurs
        //cleanup with admin privileges
        ServerDBHandler adminDB = new ServerDBHandler("admin", "CQNK2Ih8H8aikg6M");
        adminDB.removeUser(username);
        adminDB.closeConnection();
        PostgreSQLConnect.changeAuth("anon", "anonpassword");

        TestOutputHelper.setResult("testAddUser", expected, actual);
        assertEquals(actual, expected);
    }


    @Test
    public void testAddLevel() {
        LevelInfo levelInfo = new LevelInfo("id12adsfg", "reevesLevel", "reeves34");
        LevelInfo levelInfo2 = new LevelInfo("reeves34_dsfafdsa", "reevesLevel", "reeves34");
        //TODO: figure out how to attach tmx, tsx, and pngs to the levelInfos
        //File fTmx = new File(Gdx.files.internal("test2.tmx").path());
        //File fTsx = new File(Gdx.files.internal("test2.tsx").path());
        //File fPng = new File(Gdx.files.internal("test2.png").path());

        //levelInfo.setTmx(fTmx);
        //levelInfo.setTsx(fTsx);
        //levelInfo.setPng(fPng);
        //levelInfo2.setPng(fPng);
        //levelInfo2.setTmx(fTmx);
        //levelInfo2.setTsx(fTsx);
        int ret = db.addLevel(levelInfo);
        //db.addLevel(levelInfo2);
        TestOutputHelper.clearResult();
        TestOutputHelper.setResult("testAddLevel", 1, ret);
        assertEquals(1, ret);
    }

    @Test
    public void testRemoveLevel() {
        int ret = db.removeLevel("id123456789");
        TestOutputHelper.clearResult();
        TestOutputHelper.setResult("testRemoveLevel", 1, ret);
        assertEquals(1, ret);
    }

    @Test
    public void testGetRecordTime() {
        double time = db.getRecordTime("idtestgetrecordtime");
        TestOutputHelper.clearResult();
        TestOutputHelper.setResult("testGetRecordTime", 10000.00, time);
        Assert.assertEquals(10000.00, time, DELTA);
    }

    @Test
    public void testUpdateRecordTime() {
        double time = db.getRecordTime("idtestgetrecordtime");
        double newTime = 13.57;
        if (time > newTime) {
            db.updateRecordTime("idtestgetrecordtime", newTime);
        }
        TestOutputHelper.clearResult();
        TestOutputHelper.setResult("updateRecordTime", 13.57, db.getRecordTime("idtestgetrecordtime"));
        Assert.assertEquals(13.57, db.getRecordTime("idtestgetrecordtime"), DELTA);
    }

    @Test
    public void testUpdateRecordTime2() {
        double time = db.getRecordTime("idtestgetrecordtime");
        double newTime = 100001.00;
        if (time > newTime) {
            db.updateRecordTime("idtestgetrecordtime", newTime);
        }
        TestOutputHelper.clearResult();
        TestOutputHelper.setResult("updateRecordTime2", 10000.00, db.getRecordTime("idtestgetrecordtime"));
        Assert.assertEquals(10000.00, db.getRecordTime("idtestgetrecordtime"), DELTA);
    }

    @Test
    public void testGetProfilePic() {
        String pp = db.getProfilePic("testuser");
        TestOutputHelper.clearResult();
        TestOutputHelper.setResult("getProfilePic", "default", pp);
        Assert.assertEquals("default", pp);
    }

    @Test
    public void testUpdateProfilePic() {
        db.setProfilePic("testuser", "penguin.png");
        String pp = db.getProfilePic("testuser");
        TestOutputHelper.clearResult();
        TestOutputHelper.setResult("updateProfilePic", "penguin.png", pp);
        Assert.assertEquals("penguin.png", pp);
    }

}