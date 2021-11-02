package com.mygdx.nextlevel.JUnitTests;
import com.mygdx.nextlevel.Account;
import com.mygdx.nextlevel.NextLevel;
import com.mygdx.nextlevel.dbHandlers.ServerDBHandler;
import com.mygdx.nextlevel.screens.RegisterScreen;
import org.junit.*;

public final class RegisterTest extends RegisterScreen {
    ServerDBHandler sb;
    @Before
    public void init() {
//        accList.clear();
        sb = new ServerDBHandler();
        sb.removeUser("nextlevel");
    }

    @After
    public void clear() {
        TestOutputHelper.displayResult();
        TestOutputHelper.clearResult();
        sb.closeConnection();
    }

    @Test
    public void testMinCorrectPassLength() {
        String password = "testPass";
        TestOutputHelper.setResult("testMinCorrectPassLength", true, checkPassLength(password));
        Assert.assertTrue(checkPassLength(password));
    }

    @Test
    public void testMaxCorrectPassLength() {
        String password = "testpasstestpass";
        TestOutputHelper.setResult("testMaxCorrectPassLength", true, checkPassLength(password));
        Assert.assertTrue(checkPassLength(password));
    }

    @Test
    public void testIncorrectPassLength1() {
        String password = "test";
        TestOutputHelper.setResult("testIncorrectPassLength1", false, checkPassLength(password));
        Assert.assertFalse(checkPassLength(password));
    }

    @Test
    public void testIncorrectPassLength2() {
        String password = "testpasstestpasstestpass";
        TestOutputHelper.setResult("testIncorrectPassLength2", false, checkPassLength(password));
        Assert.assertFalse(checkPassLength(password));
    }

    @Test
    public void testCorrectCheckRegex() {
        String password = "testPass#1";
        TestOutputHelper.setResult("testCorrectCheckRegex", true, checkRegex(password));
        Assert.assertTrue(checkRegex(password));
    }

    @Test
    public void missingLowercase() {
        String password = "TESTPASS#1";
        TestOutputHelper.setResult("missingLowercase", false, checkRegex(password));
        Assert.assertFalse(checkRegex(password));
    }

    @Test
    public void missingUppercase() {
        String password = "testpass#1";
        TestOutputHelper.setResult("missingUppercase", false, checkRegex(password));
        Assert.assertFalse(checkRegex(password));
    }

    @Test
    public void missingSymbol() {
        String password = "testPasss1";
        TestOutputHelper.setResult("missingSymbol", false, checkRegex(password));
        Assert.assertFalse(checkRegex(password));
    }

    @Test
    public void missingDigit() {
        String password = "testPass##";
        TestOutputHelper.setResult("missingDigit", false, checkRegex(password));
        Assert.assertFalse(checkRegex(password));
    }

    @Test
    public void checkUsernameCorrect() {
        String username = "nextlevel";
        TestOutputHelper.setResult("checkUsernameCorrect", true, checkUsername(username));
        Assert.assertTrue(checkUsername(username));
    }

    @Test
    public void checkUsernameIncorrect1() {
        String username = "nex";
        TestOutputHelper.setResult("checkUsernameIncorrect1", false, checkUsername(username));
        Assert.assertFalse(checkUsername(username));
    }

    @Test
    public void checkUsernameIncorrect2() {
        String username = "nextnextnextnextnext";
        TestOutputHelper.setResult("checkUsernameIncorrect2", false, checkUsername(username));
        Assert.assertFalse(checkUsername(username));
    }

    @Test
    public void checkPasswords() {
        String pass = "testpass";
        String verify = "testpass";
        TestOutputHelper.setResult("checkPasswords", true, checkPasswords(pass, verify));
        Assert.assertTrue(checkPasswords(pass, verify));
    }

    @Test
    public void checkPasswordsIncorrect() {
        String pass = "testpass";
        String verify = "testpas";
        TestOutputHelper.setResult("checkPasswords", false, checkPasswords(pass, verify));
        Assert.assertFalse(checkPasswords(pass, verify));
    }

    @Test
    public void checkAccountAdded() {
        String user = "nextlevel";
        String pass = "testPass#1";
        String verify = "testPass#1";
        if (checkUsername(user) && checkPasswords(pass, verify) && checkPassLength(pass) && checkRegex(pass)) {
            Account a = new Account(user, pass, "");
//            accList.add(a);
            db.addUser(a);
        }
//        TestOutputHelper.setResult("checkAccountAdded", 1, accList.size());
        TestOutputHelper.setResult("checkAccountAdded", true, db.userExists(user));
//        Assert.assertEquals(1, accList.size());
        Assert.assertTrue(db.userExists(user));
    }

    @Test
    public void checkAccountNotAdded() {
        String user = "nextlevel2";
        String pass = "testPass#1";
        String verify = "testPass1";
        if (checkUsername(user) && checkPasswords(pass, verify) && checkPassLength(pass) && checkRegex(pass)) {
            Account a = new Account(user, pass, "");
            db.addUser(a);
        }
//        TestOutputHelper.setResult("checkAccountNotAdded", 0, accList.size());
//        Assert.assertEquals(0, accList.size());
        TestOutputHelper.setResult("checkAccountNotAdded", false, db.userExists(user));
        Assert.assertFalse(db.userExists(user));
    }
}
