package com.mygdx.nextlevel.JUnitTests;

import com.badlogic.gdx.Gdx;
import com.mygdx.nextlevel.Account;
import com.mygdx.nextlevel.Level;
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
import java.util.Random;

import static org.junit.Assert.*;

public class ServerDBTest {
    private static ServerDBHandler db;
    private static final double DELTA = 1e-15;

    @BeforeClass
    public static void populateDatabase() {
        //TODO: save old database, and put in a sample database
        //also probably not a good idea to run when application is in production...

        //put in temporary account
        Account steveAccount = new Account("steve", "abcd1234!", "steve@example.com");
        db = new ServerDBHandler();
        db.addUser(steveAccount);

        db.closeConnection();
    }

    @AfterClass
    public static void restoreDatabase() {
        ServerDBHandler db = new ServerDBHandler();
        db.removeUser("steve");
        db.closeConnection();
    }

    @Before
    public void establishConnection() {
        //connect
        db = new ServerDBHandler();
        //add some sample data
        LevelInfo info = new LevelInfo("idtestgetrecordtime", "TestGetRecordTime", "jchen");
        Account a = new Account("testuser", "password", "testuser@example.com", "default");

        db.addLevel(info);
        db.addUser(a);
        db.setProfilePic("testuser", "default");
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





    //--------------------- User table tests ---------------------//

    @Test
    public void testAddUser() {
        String username = "erwin";
        Account testAccount = new Account(username, "puppyDogs5000", "erwin@example.com");

        db.addUser(testAccount);

        boolean actual = db.userExists(username);
        boolean expected = true;

        db.removeUser(username);

        TestOutputHelper.setResult("testAddUser", expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testRemoveUser() {
        String username = "testRemoveUser";
        Account testAccount = new Account(username, "Password1!", username + "@xmail.com");
        db.addUser(testAccount);

        db.removeUser(username);
        boolean actual = db.userExists(username);
        boolean expected = false;

        TestOutputHelper.clearResult();
        TestOutputHelper.setResult(username, expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testUserExistsFalse() {
        String username = "testUserExistsFalse";
        boolean actual = db.userExists(username);
        boolean expected = false;

        TestOutputHelper.setResult(username, expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testUserExistsTrue() {
        boolean actual = db.userExists("steve");
        boolean expected = true;

        TestOutputHelper.setResult("testUserExistsTrue", expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testEmailExistsTrue() {
        String email = "testEmailExistsTrue";
        Account testAccount = new Account("username", "Password1!", email + "@snails.com");
        db.addUser(testAccount);

        int actual = db.emailExists(email + "@snails.com");
        int expected = 1;

        db.removeUser("username");

        TestOutputHelper.clearResult();
        TestOutputHelper.setResult(email, expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testEmailExistsFalse() {
        String email = "testEmailExistsFalse";

        int actual = db.emailExists(email);
        int expected = 0;

        TestOutputHelper.clearResult();
        TestOutputHelper.setResult(email, expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetPasswordUserExists() {
        String password = "testGetPasswordUserExists";
        Account testAccount = new Account("username", password + "1!", "email@example.com");
        db.addUser(testAccount);

        String actual = db.getPassword("username");
        String expected = password + "1!";

        db.removeUser("username");

        TestOutputHelper.clearResult();
        TestOutputHelper.setResult(password, expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetPasswordUserDoesntExist() {
        String username = "testGetPasswordUserDoesntExist";

        String actual = db.getPassword(username);
        String expected = "";

        TestOutputHelper.clearResult();
        TestOutputHelper.setResult(username, expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testChangePasswordUserExists() {
        String username = "testGetPasswordUserDoesntExist";
        String oldPassword = "pear";
        String newPassword = "pineapple";

        Account testAccount = new Account(username, oldPassword, "email@fruits.com");
        db.addUser(testAccount);

        int actual = db.changePassword(username, newPassword);
        int expected = 1;

        String pass = db.getPassword(username);

        db.removeUser(username);

        TestOutputHelper.clearResult();
        TestOutputHelper.setResult(username, expected, actual);
        assertEquals(expected, actual);
        assertEquals(newPassword, pass);
        assertNotEquals(oldPassword, pass);
    }

    @Test
    public void testChangePasswordUserDoesntExist() {
        String username = "testGetPasswordUserDoesntExist";
        String newPassword = "pineapple";

        int actual = db.changePassword(username, newPassword);
        int expected = 0;

        TestOutputHelper.clearResult();
        TestOutputHelper.setResult(username, expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testUpdatePasswordUserExists() {
        String username = "testUpdatePasswordUserExists";
        String password = "ant";

        Account testAccount = new Account(username, password, "email@ant.com");
        db.addUser(testAccount);

        int actual = db.updatePassword(username);
        int expected = 1;

        String pass = db.getPassword(username);

        db.removeUser(username);

        TestOutputHelper.clearResult();
        TestOutputHelper.setResult(username, expected, actual);
        assertEquals(expected, actual);
        assertEquals("password", pass);
        assertNotEquals(password, pass);
    }

    @Test
    public void testUpdatePasswordUserDoesntExist() {
        String username = "testUpdatePasswordUserDoesntExist";

        int actual = db.updatePassword(username);
        int expected = 0;

        TestOutputHelper.clearResult();
        TestOutputHelper.setResult(username, expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetProfilePic() {
        System.out.println(db.getPassword("testuser"));
        System.out.println(db.getEmail("testuser"));
        System.out.println(db.getProfilePic("testuser"));
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







    //--------------------- Level table tests ---------------------//

    @Test
    public void testAddLevel1() {
        String id = "testAddLevel1";
        LevelInfo levelInfo = new LevelInfo(id, "title", "testuser");
        //TODO: figure out how to attach tmx, tsx, and pngs to the levelInfos
        //File fTmx = new File(Gdx.files.internal("test2.tmx").path());
        //File fTsx = new File(Gdx.files.internal("test2.tsx").path());
        //File fPng = new File(Gdx.files.internal("test2.png").path());

        //levelInfo.setTmx(fTmx);
        //levelInfo.setTsx(fTsx);
        //levelInfo.setPng(fPng);
        int actual = db.addLevel(levelInfo);
        int expected = 1;

        LevelInfo levelInfoServer = db.getLevelByID(id);
        db.removeLevel(id);

        TestOutputHelper.clearResult();
        TestOutputHelper.setResult(id, expected, actual);
        assertEquals(expected, actual);
        assertNotNull(levelInfoServer);

    }

    @Test
    public void testAddLevel2() {
        String id = "testAddLevel2";
        LevelInfo existingLevel = new LevelInfo(id, "titleOriginal", "testuser");
        db.addLevel(existingLevel);

        LevelInfo levelWithSameID = new LevelInfo(id, "titleNew", "testuser");

        //File fTmx = new File(Gdx.files.internal("test2.tmx").path());
        //File fTsx = new File(Gdx.files.internal("test2.tsx").path());
        //File fPng = new File(Gdx.files.internal("test2.png").path());

        //levelInfo.setTmx(fTmx);
        //levelInfo.setTsx(fTsx);
        //levelInfo.setPng(fPng);
        int actual = db.addLevel(levelWithSameID);
        int expected = 0;
        LevelInfo levelOnServer = db.getLevelByID(id);
        db.removeLevel(id);

        TestOutputHelper.clearResult();
        TestOutputHelper.setResult(id, expected, actual);
        assertEquals(expected, actual);
        assertEquals(levelOnServer.getTitle(), "titleOriginal");
    }

    @Test
    public void testRemoveLevel1() {
        String id = "testRemoveLevel1";
        LevelInfo levelInfo = new LevelInfo(id, "title", "testuser");
        db.addLevel(levelInfo);

        int actual = db.removeLevel(id);
        int expected = 1;
        TestOutputHelper.clearResult();
        TestOutputHelper.setResult(id, expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testRemoveLevel2() {
        String id = "testRemoveLevel2";

        int actual = db.removeLevel(id);
        int expected = -1;

        TestOutputHelper.clearResult();
        TestOutputHelper.setResult(id, expected, actual);
        assertEquals(expected, actual);
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
    public void testGetLevelRatingsEmpty() {
        String id = "id_noRatings";
        LevelInfo toAdd = new LevelInfo(id, "title", "reeves34");
        db.addLevel(toAdd);

        ArrayList<Double> ratings = db.getLevelRatings(id);

        db.removeLevel(id);

        TestOutputHelper.clearResult();
        TestOutputHelper.setResult("testGetLevelRatingsEmpty", null, ratings);
        assertNull(ratings);
    }

    @Test
    public void testAddLevelRating() {
        String id = "id_addRatings";
        Double ratingToAdd = 4.5;

        LevelInfo toAdd = new LevelInfo(id, "title", "reeves34");

        db.addLevel(toAdd);
        db.addLevelRating(id, ratingToAdd);
        ArrayList<Double> ratings = db.getLevelRatings(id);
        db.removeLevel(id);

        ArrayList<Double> expected = new ArrayList<>();
        expected.add(ratingToAdd);

        TestOutputHelper.clearResult();
        TestOutputHelper.setResult("testAddLevelRating", expected.get(0), ratings.get(0));
        assertEquals(expected.get(0), ratings.get(0));
    }

    @Test
    public void testIncreaseLevelPlayCount() {
        String id = "testIncreaseLevelPlayCount";
        LevelInfo toAdd = new LevelInfo(id, "title", "asdf");
        db.addLevel(toAdd);
        db.increaseLevelPlayCount(id);
        db.increaseLevelPlayCount(id);

        int actual = db.getLevelByID(id).getPlayCount();
        db.removeLevel(id);

        int expected = 2;

        TestOutputHelper.clearResult();
        TestOutputHelper.setResult(id, expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetLevelAverageRating() {
        String id = "testGetLevelAverageRating";
        LevelInfo levelInfo = new LevelInfo(id, "title", "asdf");
        db.addLevel(levelInfo);

        float sum = 0;
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            //generate a random number between 0 and 10, then divide by 2 to simulate having a rating out of 5 stars
            double rating = ((double) random.nextInt(11)) / 2;
            db.addLevelRating(id, rating);
            sum += rating;
        }
        float expected = sum / 100f;
        float actual = db.getLevelAverageRating(id);
        db.removeLevel(id);

        TestOutputHelper.clearResult();
        TestOutputHelper.setResult(id, expected, actual);
        assertEquals(expected, actual, 0);
    }

    @Test
    public void testGetLevelAverageRatingNoRatings() {
        String id = "testGetLevelAverageRatingNoRatings";
        LevelInfo levelInfo = new LevelInfo(id, "title", "asdf");
        db.addLevel(levelInfo);

        float actual = db.getLevelAverageRating(id);
        db.removeLevel(id);
        float expected = -1;

        TestOutputHelper.clearResult();
        TestOutputHelper.setResult(id, expected, actual);
        assertEquals(expected, actual, 0);
    }

}