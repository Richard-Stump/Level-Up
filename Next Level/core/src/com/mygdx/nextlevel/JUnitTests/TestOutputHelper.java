package com.mygdx.nextlevel.JUnitTests;

public class TestOutputHelper {
    private static String test = "";
    private static Object expectedObj;
    private static Object actualObj;

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";

    public static void displayResult() {
        String str;
        if (((actualObj == null) && (expectedObj == null)) || ((actualObj != null) && (actualObj.equals(expectedObj)))) {
            str = String.format(ANSI_GREEN +"passed" + ANSI_RESET + " %s", test);
        } else {
            str = String.format(ANSI_RED + "failed" + ANSI_RESET + " %s", test);
        }

        str = String.format("\t%s\n\t\tExpected: %s\n\t\tActual: %s\n", str, expectedObj.toString(), actualObj.toString());

        System.out.println(str);
    }

    public static void clearResult() {
        expectedObj = null;
        actualObj = null;
        test = "";
    }

    public static void setResult(String testName, Object expected, Object actual) {
        test = testName;
        actualObj = actual;
        expectedObj = expected;
    }
}
