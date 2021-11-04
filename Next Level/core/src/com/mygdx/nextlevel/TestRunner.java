package com.mygdx.nextlevel;

import com.mygdx.nextlevel.JUnitTests.*;
import com.mygdx.nextlevel.JUnitTests.CheckpointTest;
import com.mygdx.nextlevel.JUnitTests.CreatedLevelsDBTest;
import com.mygdx.nextlevel.JUnitTests.LevelsDBControllerTest;
import com.mygdx.nextlevel.JUnitTests.RegisterTest;
import com.mygdx.nextlevel.screens.LoginScreen;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class TestRunner {


    public static void main(String[] args) {
        int totalTests;
        int totalPassed;
        int numTests;
        int numPassed;
        System.out.println();
        System.out.println("Register tests:");
        Result resultRegister = JUnitCore.runClasses(RegisterTest.class);
        for (Failure failure: resultRegister.getFailures()) {
            System.out.println(failure.toString());
        }
        numTests = resultRegister.getRunCount();
        numPassed = numTests -  resultRegister.getFailureCount();
        System.out.println("RegisterTest - Passed: " + numPassed + "/" + numTests);
        totalPassed = numPassed;
        totalTests = numTests;
        System.out.println();

        System.out.println("Login tests:");
        Result resultLogin = JUnitCore.runClasses(LoginTest.class);
        for (Failure failure: resultLogin.getFailures()) {
            System.out.println(failure.toString());
        }
        numTests = resultLogin.getRunCount();
        numPassed = numTests -  resultLogin.getFailureCount();
        System.out.println("LoginTest - Passed: " + numPassed + "/" + numTests);
        totalPassed += numPassed;
        totalTests += numTests;
        System.out.println();

        System.out.println("ForgetPassword tests:");
        Result resultFP = JUnitCore.runClasses(ForgetPasswordTest.class);
        for (Failure failure: resultFP.getFailures()) {
            System.out.println(failure.toString());
        }
        numTests = resultFP.getRunCount();
        numPassed = numTests -  resultFP.getFailureCount();
        System.out.println("ForgetPassword - Passed: " + numPassed + "/" + numTests);
        totalPassed += numPassed;
        totalTests += numTests;
        System.out.println();

        System.out.println("ChangePassword tests:");
        Result resultCP = JUnitCore.runClasses(ChangePasswordTest.class);
        for (Failure failure: resultCP.getFailures()) {
            System.out.println(failure.toString());
        }
        numTests = resultCP.getRunCount();
        numPassed = numTests -  resultCP.getFailureCount();
        System.out.println("ChangePassword - Passed: " + numPassed + "/" + numTests);
        totalPassed += numPassed;
        totalTests += numTests;
        System.out.println();

        System.out.println("Item tests:");
        Result resultItem = JUnitCore.runClasses(ItemTest.class);
//        System.out.println("Initialized resultItem");
        for (Failure failure: resultItem.getFailures()) {
            System.out.println(failure.toString());
        }
        numTests = resultItem.getRunCount();
        numPassed = numTests -  resultItem.getFailureCount();
        System.out.println("ItemTest - Passed: " + numPassed + "/" + numTests);
        totalPassed += numPassed;
        totalTests += numTests;
//        totalPassed = numPassed;
//        totalTests = numTests;
        System.out.println();

        System.out.println("Checkpoint tests:");
        Result resultCheckpoint = JUnitCore.runClasses(CheckpointTest.class);
        for (Failure failure: resultCheckpoint.getFailures()) {
            System.out.println(failure.toString());
        }
        numTests = resultCheckpoint.getRunCount();
        numPassed = numTests -  resultCheckpoint.getFailureCount();
        System.out.println("CheckpointTest - Passed: " + numPassed + "/" + numTests);
        totalPassed += numPassed;
        totalTests += numTests;
        System.out.println();

        System.out.println("Block tests:");
        Result resultBlock = JUnitCore.runClasses(BlockTest.class);
        for (Failure failure: resultBlock.getFailures()) {
            System.out.println(failure.toString());
        }
        numTests = resultBlock.getRunCount();
        numPassed = numTests -  resultBlock.getFailureCount();
        System.out.println("BlockTest - Passed: " + numPassed + "/" + numTests);
        totalPassed += numPassed;
        totalTests += numTests;
        System.out.println();

        System.out.println("Local Database tests:");
        Result resultLocalDB = JUnitCore.runClasses(CreatedLevelsDBTest.class, LevelsDBControllerTest.class);
        for (Failure failure: resultLocalDB.getFailures()) {
            System.out.println(failure.toString());
        }
        numTests = resultLocalDB.getRunCount();
        numPassed = numTests -  resultLocalDB.getFailureCount();
        System.out.println("DatabaseTest - Passed: " + numPassed + "/" + numTests);
        totalPassed += numPassed;
        totalTests += numTests;
        System.out.println();


        System.out.println("Server Database tests:");
        Result resultServerDB = JUnitCore.runClasses(ServerDBTest.class, PostgreSQLConnectTest.class);
        for (Failure failure: resultServerDB.getFailures()) {
            System.out.println(failure.toString());
        }
        numTests = resultServerDB.getRunCount();
        numPassed = numTests -  resultServerDB.getFailureCount();
        System.out.println("Server Database Test - Passed: " + numPassed + "/" + numTests);
        totalPassed += numPassed;
        totalTests += numTests;
        System.out.println();

        if (totalTests == totalPassed)
            System.out.println("All Tests have passed");
        else
            System.out.println("All Tests - Passed: " + totalPassed + "/" + totalTests);
        System.out.println();
    }
}
