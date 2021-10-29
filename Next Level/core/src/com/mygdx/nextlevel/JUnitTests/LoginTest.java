package com.mygdx.nextlevel.JUnitTests;
import com.mygdx.nextlevel.Account;

import com.mygdx.nextlevel.screens.LoginScreen;
import org.junit.*;

public class LoginTest extends LoginScreen {

    @Before
    public void init() {
        accList.clear();
        Account a = new Account();
        Account b = new Account();
        a.setUsername("nextlevel1");
        a.setPassword("testPass#1");
        b.setUsername("nextlevel2");
        b.setPassword("testPass#2");
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
