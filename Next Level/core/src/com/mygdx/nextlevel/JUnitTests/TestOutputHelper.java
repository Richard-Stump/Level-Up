package com.mygdx.nextlevel.JUnitTests;

public class TestOutputHelper {
    private static String test = "";
    private static Object expectedObj;
    private static Object actualObj;
    private static boolean reverse;

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";

    public static void displayResult() {
        String str;
        if ((expectedObj == null) && (actualObj == null)) {
            expectedObj = "_null";
            actualObj = "_null";
        }
        if (actualObj != null && actualObj.equals(expectedObj) || actualObj != null && reverse && !actualObj.equals(expectedObj)) {
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
        reverse = false;
    }

    public static void setResult(String testName, Object expected, Object actual) {
        test = testName;
        actualObj = actual;
        expectedObj = expected;
        reverse = false;
    }

    public static void setReverse(boolean set) {
        reverse = set;
    }
}
