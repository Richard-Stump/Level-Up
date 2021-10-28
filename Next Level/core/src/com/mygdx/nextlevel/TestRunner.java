package com.mygdx.nextlevel;

import com.mygdx.nextlevel.JUnitTests.*;
import com.mygdx.nextlevel.JUnitTests.CheckpointTest;
import com.mygdx.nextlevel.JUnitTests.CreatedLevelsDBTest;
import com.mygdx.nextlevel.JUnitTests.LevelsDBControllerTest;
import com.mygdx.nextlevel.JUnitTests.RegisterTest;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class TestRunner {
    public static void main(String[] args) {
        System.out.println();
        Result resultRegister = JUnitCore.runClasses(RegisterTest.class);
        for (Failure failure: resultRegister.getFailures()) {
            System.out.println(failure.toString());
        }
        System.out.println("RegisterTest - Passed all tests: " + resultRegister.wasSuccessful());

        Result resultCheckpoint = JUnitCore.runClasses(CheckpointTest.class);
        for (Failure failure: resultCheckpoint.getFailures()) {
            System.out.println(failure.toString());
        }
        System.out.println("CheckpointTest - Passed all tests: " + resultCheckpoint.wasSuccessful());


        Result resultLocalDB = JUnitCore.runClasses(CreatedLevelsDBTest.class, LevelsDBControllerTest.class);
        for (Failure failure: resultLocalDB.getFailures()) {
            System.out.println(failure.toString());
        }
        System.out.println("DatabaseTest - Passed all tests: " + resultLocalDB.wasSuccessful());


        Result resultServerDB = JUnitCore.runClasses(ServerDBTest.class, PostgreSQLConnectTest.class);
        for (Failure failure: resultServerDB.getFailures()) {
            System.out.println(failure.toString());
        }
        System.out.println("Server Database test - Passed all tests: " + resultServerDB.wasSuccessful());
    }
}
