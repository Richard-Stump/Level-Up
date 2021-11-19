package com.mygdx.nextlevel.JUnitTests;

import com.mygdx.nextlevel.BoxCollider;
import com.mygdx.nextlevel.actors.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FinishingTest {
    Player2 player;
    CoinStatic coin;
    Jewel jewel;
    @Before
    public void init() {
        player = new Player2();
        coin = new CoinStatic();
        jewel = new Jewel();
    }

    @After
    public void clear() {
        player = null;
        coin = null;
        jewel = null;
        TestOutputHelper.displayResult();
        TestOutputHelper.clearResult();
    }

    @Test
    public void collectCoinTest() {
        player.onCollision(coin, BoxCollider.Side.LEFT);
        TestOutputHelper.setResult("collectCoinTest", 1, player.getCoins());
        Assert.assertEquals(1, player.getCoins());
    }
}
