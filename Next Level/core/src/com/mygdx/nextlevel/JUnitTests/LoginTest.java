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
        db.removeUser("jchen");
        db.addUser(new Account("jchen", "Password#1", "example@gmail.com"));
    }

    @After
    public void clear() {
        TestOutputHelper.displayResult();
        TestOutputHelper.clearResult();
        db.closeConnection();
    }

    @Test
    public void loginSuccessful() {
        String username = "jchen";
        String pass = "Password#1";
        TestOutputHelper.setResult("loginSuccessful", pass, db.getPassword(username));
        Assert.assertEquals(pass, db.getPassword(username));
    }

    @Test
    public void loginUnsuccessful() {
        String username = "jchen2";
        String pass = "Password#1";
        TestOutputHelper.setResult("loginUnsuccessful", pass, db.getPassword(username));
        Assert.assertNotEquals(pass, db.getPassword(username));
    }
}
