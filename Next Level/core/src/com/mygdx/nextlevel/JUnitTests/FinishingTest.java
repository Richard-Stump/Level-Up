package com.mygdx.nextlevel.JUnitTests;

import com.mygdx.nextlevel.BoxCollider;
import com.mygdx.nextlevel.actors.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FinishingTest {
    Player2 player;

    @Before
    public void init() {
        player = new Player2();
    }

    @After
    public void clear() {
        player = null;
        TestOutputHelper.displayResult();
        TestOutputHelper.clearResult();
    }

    @Test
    public void collectCoinTest() {
        player.incCoins();
        TestOutputHelper.setResult("collectCoinTest", 1, player.getCoins());
        Assert.assertEquals(1, player.getCoins());
    }

    @Test
    public void getJewelTest() {
        player.setJewel(true);
        TestOutputHelper.setResult("getJewelTest", true, player.getJewel());
        Assert.assertTrue(player.getJewel());
    }

    @Test
    public void killingEnemyTest() {
        player.incEnemiesKilled();
        TestOutputHelper.setResult("killingEnemyTest", 1, player.getEnemiesKilled());
        Assert.assertEquals(1, player.getEnemiesKilled());
    }

    @Test
    public void passCoinCondition() {
        int num = 5;
        player.incCoins();
        player.incCoins();
        player.incCoins();
        player.incCoins();
        player.incCoins();
        if (player.getCoins() == num) {
            player.setWin(true);
        }
        TestOutputHelper.setResult("passCoinCondition", true, player.getWin());
        Assert.assertTrue(player.getWin());
    }

    @Test
    public void passCoinConditionFalse() {
        int num = 5;
        player.incCoins();
        player.incCoins();
        if (player.getCoins() == num) {
            player.setWin(true);
        }
        TestOutputHelper.setResult("passCoinConditionFalse", false, player.getWin());
        Assert.assertFalse(player.getWin());
    }

    @Test
    public void passKillEnemyCondition() {
        int num = 5;
        player.incEnemiesKilled();
        player.incEnemiesKilled();
        player.incEnemiesKilled();
        player.incEnemiesKilled();
        player.incEnemiesKilled();
        if (player.getEnemiesKilled() == num) {
            player.setWin(true);
        }
        TestOutputHelper.setResult("passKillEnemyCondition", true, player.getWin());
        Assert.assertTrue(player.getWin());
    }

    @Test
    public void passKillEnemyConditionFalse() {
        int num = 5;
        player.incEnemiesKilled();
        player.incEnemiesKilled();
        if (player.getEnemiesKilled() == num) {
            player.setWin(true);
        }
        TestOutputHelper.setResult("passKillEnemyConditionFalse", false, player.getWin());
        Assert.assertFalse(player.getWin());
    }

    @Test
    public void passNoKillEnemyCondition() {
        int num = 0;
        if (player.getEnemiesKilled() == num) {
            player.setWin(true);
        }
        TestOutputHelper.setResult("passNoKillEnemyCondition", true, player.getWin());
        Assert.assertTrue(player.getWin());
    }

    @Test
    public void passNoKillEnemyConditionFalse() {
        int num = 0;
        player.incEnemiesKilled();
        if (player.getEnemiesKilled() == num) {
            player.setWin(true);
        }
        TestOutputHelper.setResult("passNoKillEnemyConditionFalse", false, player.getWin());
        Assert.assertFalse(player.getWin());
    }

    @Test
    public void passJewelCondition() {
        player.setJewel(true);
        if (player.getJewel()) {
            player.setWin(true);
        }
        TestOutputHelper.setResult("passJewelCondition", true, player.getWin());
        Assert.assertTrue(player.getWin());
    }

    @Test
    public void passJewelConditionFalse() {
        if (player.getJewel()) {
            player.setWin(true);
        }
        TestOutputHelper.setResult("passJewelConditionFalse", false, player.getWin());
        Assert.assertFalse(player.getWin());
    }

    @Test
    public void passTimeCondition() {
        double time = 10.0;
        double finish = 9.0;
        if (finish < time) {
            player.setWin(true);
        }
        TestOutputHelper.setResult("passTimeCondition", true, player.getWin());
        Assert.assertTrue(player.getWin());
    }

    @Test
    public void passTimeConditionFalse() {
        double time = 10.0;
        double finish = 10.0;
        if (finish < time) {
            player.setWin(true);
        }
        TestOutputHelper.setResult("passTimeConditionFalse", false, player.getWin());
        Assert.assertFalse(player.getWin());
    }
}
