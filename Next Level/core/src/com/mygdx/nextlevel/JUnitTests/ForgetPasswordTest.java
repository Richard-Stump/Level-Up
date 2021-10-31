package com.mygdx.nextlevel.JUnitTests;

import com.mygdx.nextlevel.Account;
import com.mygdx.nextlevel.screens.ForgetPasswordScreen;
import org.junit.*;

public class ForgetPasswordTest extends ForgetPasswordScreen {
    Account a = new Account("nextlevel1", "testPass#1", "");
    Account b = new Account("nextlevel2", "testPass#2", "");
    @Before
    public void init() {
        accList.clear();
        accList.add(a);
        accList.add(b);
    }

    @After
    public void clear() {
        TestOutputHelper.displayResult();
        TestOutputHelper.clearResult();
    }

    @Test
    public void resetPasswordSuccessful() {
        String username = "nextlevel2";
        changePass(username);
        TestOutputHelper.setResult("resetPasswordSuccessful", "password", b.getPassword());
        Assert.assertEquals("password", b.getPassword());
    }

    @Test
    public void resetPasswordUnsuccessful() {
        String username = "nextlevel3";
        TestOutputHelper.setResult("resetPasswordUnsuccessful", false, changePass(username));
        Assert.assertEquals(false, changePass(username));
    }
}
