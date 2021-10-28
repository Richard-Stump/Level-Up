package com.mygdx.nextlevel.JUnitTests;
import com.mygdx.nextlevel.NextLevel;
import com.mygdx.nextlevel.screens.RegisterScreen;
import org.junit.*;

public final class RegisterTest extends RegisterScreen {


    @Test
    public void testMinCorrectPassLength() {
        String password = "testPass";
        Assert.assertTrue(checkPassLength(password));
    }

    @Test
    public void testMaxCorrectPassLength() {
        String password = "testpasstestpass";
        Assert.assertTrue(checkPassLength(password));
    }

    @Test
    public void testIncorrectPassLength1() {
        String password = "test";
        Assert.assertFalse(checkPassLength(password));
    }

    @Test
    public void testIncorrectPassLength2() {
        String password = "testpasstestpasstestpass";
        Assert.assertFalse(checkPassLength(password));
    }

    @Test
    public void testCorrectCheckRegex() {
        String password = "testPass#1";
        Assert.assertTrue(checkRegex(password));
    }

    @Test
    public void missingLowercase() {
        String password = "TESTPASS#1";
        Assert.assertFalse(checkRegex(password));
    }

    @Test
    public void missingUppercase() {
        String password = "testpass#1";
        Assert.assertFalse(checkRegex(password));
    }

    @Test
    public void missingSymbol() {
        String password = "testPasss1";
        Assert.assertFalse(checkRegex(password));
    }

    @Test
    public void missingDigit() {
        String password = "testPass##";
        Assert.assertFalse(checkRegex(password));
    }
}
