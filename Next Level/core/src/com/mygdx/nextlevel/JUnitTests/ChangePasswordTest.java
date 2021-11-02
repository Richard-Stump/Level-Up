package com.mygdx.nextlevel.JUnitTests;

import com.mygdx.nextlevel.Account;
import com.mygdx.nextlevel.dbHandlers.ServerDBHandler;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ChangePasswordTest {
    ServerDBHandler db;
    @Before
    public void init() {
        db = new ServerDBHandler();
        db.addUser(new Account("sam123", "Password#1", "example@gmail.com"));
    }

    @After
    public void clear() {
        TestOutputHelper.displayResult();
        TestOutputHelper.clearResult();
        db.removeUser("sam123");
        db.closeConnection();
    }

    @Test
    public void changePasswordSuccessful() {
        String username = "sam123";
        String password = "Ilovedogs123!";
        if (db.userExists(username)) {
            db.changePassword(username, password);
        }
        TestOutputHelper.setResult("changePasswordSuccessful", password, db.getPassword(username));
        Assert.assertEquals(password, db.getPassword(username));
    }

    @Test
    public void changePasswordUnsuccessful() {
        String username = "sam12";
        String password = "Ilovedogs123!";
        if (db.userExists(username)) {
            db.changePassword(username, password);
        }
        TestOutputHelper.setResult("changePasswordUnsuccessful", password, db.getPassword(username));
        TestOutputHelper.setReverse(true);
        Assert.assertNotEquals(password, db.getPassword(username));
    }
}
