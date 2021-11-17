package com.mygdx.nextlevel.JUnitTests;
import com.mygdx.nextlevel.BoxCollider;
import com.mygdx.nextlevel.NextLevel;
import com.mygdx.nextlevel.actors.*;
import com.mygdx.nextlevel.screens.GameScreen2;
import org.junit.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ItemTest {
//    GameScreen2 screen;
    Player2 player;
    MushroomItem2 mushroom;
    LifeStealItem2 lifeSteal;
    SlowItem2 slow;
    SpeedItem2 speed;
    StarItem2 star;
    LifeItem2 oneUp;
//    BoxCollider.Side side = BoxCollider.Side.RIGHT;
    @Before
    public void init() {
//        System.out.println("Here");
//        screen = new GameScreen2();
//        System.out.println("Here2");
        player = new Player2();
//        System.out.println("Here3");
        mushroom = new MushroomItem2();
        lifeSteal = new LifeStealItem2();
        slow = new SlowItem2();
        speed = new SpeedItem2();
        star = new StarItem2();
        oneUp = new LifeItem2();
    }

    @After
    public void clear() {
//        System.out.println("Clear");
//        screen = null;
        player = null;
        mushroom = null;
        lifeSteal = null;
        slow = null;
        speed = null;
        star = null;
        oneUp = null;
        TestOutputHelper.displayResult();
        TestOutputHelper.clearResult();
    }

    @Test
    public void mushroomTest() {
        System.out.println("Here");
//        Player2 player = new Player2(screen, 32, 32);
//        MushroomItem2 mushroom = new MushroomItem2(screen, 32, 32);
        System.out.println(player);
        player.onCollision(mushroom, BoxCollider.Side.RIGHT);
        System.out.println("Here2");
        TestOutputHelper.setResult("mushroomTest", true, player.getMushroom());
        Assert.assertTrue(player.getMushroom());
    }

    @Test
    public void mushroomPUTest() {
        player.onCollision(mushroom, BoxCollider.Side.RIGHT);
        TestOutputHelper.setResult("mushroomPUTest", true, player.hasPowerUp());
        Assert.assertTrue(player.hasPowerUp());
    }

    @Test
    public void lifeStealTest() {
        player.onCollision(lifeSteal, BoxCollider.Side.RIGHT);
        TestOutputHelper.setResult("lifeStealTest", (Item2) lifeSteal, player.getHeldItem());
        Assert.assertEquals((Item2) lifeSteal, player.getHeldItem());
    }


    @Test
    public void slowItemTest() {
        player.onCollision(slow, BoxCollider.Side.RIGHT);
        TestOutputHelper.setResult("slowItemTest", true, player.getSlowItem());
        Assert.assertTrue(player.getSlowItem());
    }


    @Test
    public void speedItemTest() {
        player.onCollision(speed, BoxCollider.Side.RIGHT);
        TestOutputHelper.setResult("speedItemTest", true, player.getSpeedItem());
        Assert.assertTrue(player.getSpeedItem());
    }

    @Test
    public void starItemTest() {
        player.onCollision(star, BoxCollider.Side.RIGHT);
        TestOutputHelper.setResult("starItemTest", (Item2) star, player.getHeldItem());
        Assert.assertEquals((Item2) star, player.getHeldItem());
    }

    @Test
    public void lifeItemTest() {
        player.onCollision(oneUp, BoxCollider.Side.RIGHT);
        TestOutputHelper.setResult("lifeItemTest", 4, player.getLives());
        Assert.assertEquals(4, player.getLives());
    }
}
