package com.mygdx.nextlevel.JUnitTests;

import com.mygdx.nextlevel.TileMap;
import com.mygdx.nextlevel.dbHandlers.ServerDBHandler;
import org.junit.*;

import java.util.ArrayList;


public class TmxTests {
    TileMap tm;
    ServerDBHandler db;
    @Before
    public void init() {
        db = new ServerDBHandler();
        tm = new TileMap(db.getLevelByID("jchen3_vcq", true).getId() + ".tmx");
        System.out.println(String.format("Gravity: %f, Time: %f", tm.getGravity(), tm.getTimeLimit()));
    }

    @After
    public void clear() {
        tm = null;
        db.closeConnection();
        TestOutputHelper.displayResult();
        TestOutputHelper.clearResult();
    }

    @Test
    public void getConditionsTest() {
        ArrayList<Integer> list = tm.getConditionList();
        TestOutputHelper.setResult("getConditionsTest", true, list.contains(5));
        Assert.assertTrue(list.contains(5));
    }

    @Test
    public void getConditionsTestFalse() {
        ArrayList<Integer> list = tm.getConditionList();
        TestOutputHelper.setResult("getConditionsTestFalse", false, list.contains(3));
        Assert.assertFalse(list.contains(3));
    }

    @Test
    public void getTimeLimitTest() {
        float t = 50;
        TestOutputHelper.setResult("getTimeLimitTest", t, tm.getTimeLimit());
        Assert.assertEquals(t, tm.getTimeLimit(), 1e-15);
    }

    @Test
    public void getTimeLimitTestFalse() {
        float t = 100;
        TestOutputHelper.setResult("getTimeLimitTestFalse", t, tm.getTimeLimit());
        TestOutputHelper.setReverse(true);
        Assert.assertNotEquals(t, tm.getTimeLimit());
    }

    @Test
    public void getGravityTest() {
        float g = 0;
        TestOutputHelper.setResult("getGravityTest", g, tm.getGravity());
        Assert.assertEquals(g, tm.getGravity(), 1e-15);
    }

    @Test
    public void getGravityTestFalse() {
        float g = 5;
        TestOutputHelper.setResult("getGravityTest", g, tm.getGravity());
        TestOutputHelper.setReverse(true);
        Assert.assertNotEquals(g, tm.getGravity(), 1e-15);
    }

}
