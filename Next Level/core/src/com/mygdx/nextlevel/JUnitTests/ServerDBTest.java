package com.mygdx.nextlevel.JUnitTests;

import com.mygdx.nextlevel.Account;
import com.mygdx.nextlevel.LevelInfo;
import com.mygdx.nextlevel.dbHandlers.CreatedLevelsDB;
import com.mygdx.nextlevel.dbHandlers.ServerDBHandler;
import com.mygdx.nextlevel.dbUtil.PostgreSQLConnect;
import org.junit.*;
import org.postgresql.util.PSQLException;

import java.nio.channels.AcceptPendingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ServerDBTest {
    private static ServerDBHandler db;

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
    }

    @After
    public void cleanup() {
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
        int ret = db.addLevel(levelInfo);
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

}