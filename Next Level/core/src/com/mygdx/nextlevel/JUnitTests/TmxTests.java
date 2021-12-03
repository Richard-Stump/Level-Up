package com.mygdx.nextlevel.JUnitTests;

import com.mygdx.nextlevel.TileMap;
import com.mygdx.nextlevel.dbHandlers.ServerDBHandler;
import org.junit.*;

import java.util.ArrayList;


public class TmxTests extends TileMap {
    TileMap tm;
    ServerDBHandler db;
    @BeforeClass
    public static void init() {
//        db = new ServerDBHandler();
//        String name = db.getLevelByID("stump_tprkjymj", true).getId() + ".tmx";
//        System.out.println(name);
//        createTMX("stump_tprkjymj.tmx");
//        tm = new TileMap("stump_tprkjymj.tmx");
        createTMX();
//        System.out.println(String.format("Gravity: %f, Time: %f", tm.getGravity(), tm.getTimeLimit()));
    }

    @AfterClass
    public static void clear() {
//        db.closeConnection();
        TestOutputHelper.displayResult();
        TestOutputHelper.clearResult();
    }

    @Test
    public void getConditionsTest() {
        ArrayList<Integer> list = getConditionListTest();
        TestOutputHelper.setResult("getConditionsTest", true, list.contains(5));
        Assert.assertTrue(list.contains(5));
    }

    @Test
    public void getConditionsTestFalse() {
        ArrayList<Integer> list = getConditionListTest();
        TestOutputHelper.setResult("getConditionsTestFalse", false, list.contains(3));
        Assert.assertFalse(list.contains(3));
    }

    @Test
    public void getTimeLimitTest() {
        float t = 50;
        float val = getTimeLimitTest1();
        TestOutputHelper.setResult("getTimeLimitTest", t, val);
        Assert.assertEquals(t, val, 1e-15);
    }

    @Test
    public void getTimeLimitTestFalse() {
        float t = 100;
        float val = getTimeLimitTest1();
        TestOutputHelper.setResult("getTimeLimitTestFalse", t, val);
        TestOutputHelper.setReverse(true);
        Assert.assertNotEquals(t, val);
    }

    @Test
    public void getGravityTest() {
        float g = 0;
        float val = getGravityTest1();
        TestOutputHelper.setResult("getGravityTest", g, val);
        Assert.assertEquals(g, val, 1e-15);
    }

    @Test
    public void getGravityTestFalse() {
        float g = 5;
        float val = getGravityTest1();
        TestOutputHelper.setResult("getGravityTest", g, val);
        TestOutputHelper.setReverse(true);
        Assert.assertNotEquals(g, val, 1e-15);
    }

}
