package com.mygdx.nextlevel.JUnitTests;
import com.mygdx.nextlevel.Account;

import com.mygdx.nextlevel.dbHandlers.ServerDBHandler;
import com.mygdx.nextlevel.screens.LoginScreen;
import org.junit.*;

public class LoginTest extends LoginScreen {
    ServerDBHandler db;

    @Before
    public void init() {
        db = new ServerDBHandler();
        db.addUser(new Account("nextlevel", "Password#1", "example@gmail.com"));
    }

    @After
    public void clear() {
        TestOutputHelper.displayResult();
        TestOutputHelper.clearResult();
        db.removeUser("nextlevel");
        db.closeConnection();
    }

    @Test
    public void loginSuccessful() {
        String username = "nextlevel";
        String pass = "Password#1";
        TestOutputHelper.setResult("loginSuccessful", pass, db.getPassword(username));
        Assert.assertEquals(pass, db.getPassword(username));
    }

    @Test
    public void loginUnsuccessful1() {
        String username = "nextlevel2";
        String pass = "Password#1";
        TestOutputHelper.setResult("loginUnsuccessful", pass, db.getPassword(username));
        TestOutputHelper.setReverse(true);
        Assert.assertNotEquals(pass, db.getPassword(username));
    }

    @Test
    public void loginUnsuccessful2() {
        String username = "nextlevel";
        String pass = "Password";
        TestOutputHelper.setResult("loginUnsuccessful2", pass, db.getPassword(username));
        TestOutputHelper.setReverse(true);
        Assert.assertNotEquals(pass, db.getPassword(username));
    }
}
