package com.mygdx.nextlevel;

import com.mygdx.nextlevel.JUnitTests.CheckpointTest;
import com.mygdx.nextlevel.JUnitTests.CreatedLevelsDBTest;
import com.mygdx.nextlevel.JUnitTests.LevelsDBControllerTest;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class TestRunner {
    public static void main(String[] args) {
        System.out.println();
        Result resultCheckpoint = JUnitCore.runClasses(CheckpointTest.class);
        for (Failure failure: resultCheckpoint.getFailures()) {
            System.out.println(failure.toString());
        }

        Result resultDB = JUnitCore.runClasses(CreatedLevelsDBTest.class, LevelsDBControllerTest.class);
        for (Failure failure: resultDB.getFailures()) {
            System.out.println(failure.toString());
        }
        System.out.println("Passed all tests: " + resultCheckpoint.wasSuccessful());
        System.out.println("Passed all tests: " + resultDB.wasSuccessful());
    }
}
