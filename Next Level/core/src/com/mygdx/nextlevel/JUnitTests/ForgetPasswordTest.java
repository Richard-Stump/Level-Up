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
    }

    @After
    public void clear() {
        TestOutputHelper.displayResult();
        TestOutputHelper.clearResult();
        db.closeConnection();
    }

    @Test
    public void resetPasswordSuccessful() {
        String username = "jchen";
        if (db.userExists(username)) {
            db.updatePassword(username);
        }
        TestOutputHelper.setResult("resetPasswordSuccessful", "password", db.getPassword(username));
        Assert.assertEquals("password", db.getPassword(username));
    }

    @Test
    public void resetPasswordUnsuccessful() {
        String username = "jchen2";
        if (db.userExists(username)) {
            db.updatePassword(username);
        }
        TestOutputHelper.setResult("resetPasswordUnsuccessful", "password", db.getPassword(username));
        Assert.assertNotEquals("password", db.getPassword(username));
    }
}
