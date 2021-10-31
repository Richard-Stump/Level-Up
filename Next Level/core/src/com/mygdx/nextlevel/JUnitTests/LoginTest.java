package com.mygdx.nextlevel.JUnitTests;
import com.mygdx.nextlevel.Account;

import com.mygdx.nextlevel.screens.LoginScreen;
import org.junit.*;

public class LoginTest extends LoginScreen {

    @Before
    public void init() {
        accList.clear();
        Account a = new Account("nextlevel1", "testPass#1", "");
        Account b = new Account("nextlevel2", "testPass#2", "");
        accList.add(a);
        accList.add(b);
    }

    @After
    public void clear() {
        TestOutputHelper.displayResult();
        TestOutputHelper.clearResult();
    }

    @Test
    public void loginSuccessful() {
        String username = "nextlevel2";
        String pass = "testPass#2";
        TestOutputHelper.setResult("loginSuccessful", true, login(username, pass));
        Assert.assertTrue(login(username, pass));
    }

    @Test
    public void loginUnsuccessful() {
        String username = "nextlevel2";
        String pass = "testPass#1";
        TestOutputHelper.setResult("loginUnsuccessful", false, login(username, pass));
        Assert.assertFalse(login(username, pass));
    }
}
