package com.mygdx.nextlevel.JUnitTests;

import com.mygdx.nextlevel.Account;
import com.mygdx.nextlevel.dbHandlers.ServerDBHandler;
import com.mygdx.nextlevel.screens.ForgetPasswordScreen;
import org.junit.*;

public class ForgetPasswordTest extends ForgetPasswordScreen {
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
    public void resetPasswordSuccessful() {
        String username = "nextlevel";
        String email = "example@gmail.com";
        if (db.userExists(username) && db.getEmail(username).equals(email)) {
            db.updatePassword(username);
        }
        TestOutputHelper.setResult("resetPasswordSuccessful", "password", db.getPassword(username));
        Assert.assertEquals("password", db.getPassword(username));
    }

    @Test
    public void resetPasswordUnsuccessful1() {
        String username = "jchen24";
        String email = "example@gmail.com";
        if (db.userExists(username) && db.getEmail(username).equals(email)) {
            db.updatePassword(username);
        }
        TestOutputHelper.setResult("resetPasswordUnsuccessful1", "password", db.getPassword(username));
        TestOutputHelper.setReverse(true);
        Assert.assertNotEquals("password", db.getPassword(username));
    }

    @Test
    public void resetPasswordUnsuccessful2() {
        String username = "nextlevel";
        String email = "haha@gmail.com";
        if (db.userExists(username) && db.getEmail(username).equals(email)) {
            db.updatePassword(username);
        }
        TestOutputHelper.setResult("resetPasswordUnsuccessful2", "password", db.getPassword(username));
        TestOutputHelper.setReverse(true);
        Assert.assertNotEquals("password", db.getPassword(username));
    }
}
