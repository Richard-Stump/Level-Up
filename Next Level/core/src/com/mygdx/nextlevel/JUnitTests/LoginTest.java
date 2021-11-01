package com.mygdx.nextlevel.JUnitTests;
import com.mygdx.nextlevel.Account;

import com.mygdx.nextlevel.dbHandlers.ServerDBHandler;
import com.mygdx.nextlevel.screens.LoginScreen;
import org.junit.*;

public class LoginTest extends LoginScreen {
    static ServerDBHandler db;

    @BeforeClass
    public static void init() {
        db = new ServerDBHandler();
    }

    @AfterClass
    public static void clear() {
        TestOutputHelper.displayResult();
        TestOutputHelper.clearResult();
        db.closeConnection();
    }

    @Test
    public void loginSuccessful() {
        String username = "jchen";
        String pass = "passW0rd#1";
        TestOutputHelper.setResult("loginSuccessful", pass, db.getPassword(username));
        Assert.assertEquals(pass, db.getPassword(username));
    }

    @Test
    public void loginUnsuccessful1() {
        String username = "jchen";
        String pass = "testPass#1";
        TestOutputHelper.setResult("loginUnsuccessful1", pass, db.getPassword(username));
        Assert.assertNotEquals(pass, db.getPassword(username));
    }

    @Test
    public void loginUnsuccessful2() {
        String username = "jchen2";
        String pass = "passW0rd#1";
        TestOutputHelper.setResult("loginUnsuccessful1", pass, db.getPassword(username));
        Assert.assertNotEquals(pass, db.getPassword(username));
    }
}
