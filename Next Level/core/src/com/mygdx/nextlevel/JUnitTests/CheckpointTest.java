package com.mygdx.nextlevel.JUnitTests;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.nextlevel.actors.Checkpoint;
import com.mygdx.nextlevel.actors.Player;
import jdk.jfr.Description;
import org.junit.*;

import static org.junit.Assert.assertEquals;

public class CheckpointTest {
    private static Player player;
    private static Checkpoint checkpoint;

    @Before
    public void init() {
        player = new Player(new Vector2(32, 32), 0.25f, 0f);
        checkpoint = new Checkpoint(new Vector2(200, 32), player);
    }

    @After
    public void clear() {
        player = null;
        checkpoint = null;

        TestOutputHelper.displayResult();
        TestOutputHelper.clearResult();
    }

    @Test
    @Description("Test to check the player Spawn")
    public void testPlayerSpawn() {
        Vector2 expected = new Vector2(32, 32);
        Vector2 actual = player.getSpawnpoint();
        TestOutputHelper.setResult("testPlayerSpawn", expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    @Description("Test to check if the player Spawn is updated when going over Checkpoint")
    public void testPlayerUpdateSpawn() {
        checkpoint.changeSpawn();
        Vector2 expected = new Vector2(200, 32);
        Vector2 actual = player.getSpawnpoint();
        TestOutputHelper.setResult("testPlayerUpdateSpawn", expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    @Description("Test to check if the player Spawnpoint is updated when deaths reset")
    public void testPlayerResetSpawn() {
        player.setSpawnpoint(new Vector2(400, 400));
        player.setSpawnpoint(player.getWorldSpawn());
        Vector2 expected = new Vector2(32, 32);
        Vector2 actual = player.getSpawnpoint();
        TestOutputHelper.setResult("testPlayerResetSpawn", expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    @Description("Test initial lives of the player")
    public void testPlayerLives() {
        int expected = 3;
        int actual = player.getLives();
        TestOutputHelper.setResult("testPlayerLives", expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    @Description("Test to check if there is a life added to the Player when going over Checkpoint")
    public void testPlayerUpdateLives() {
        int expected = 4;
        player.addLife(1);
        int actual = player.getLives();
        TestOutputHelper.setResult("testPlayerUpdateLives", expected, actual);
        assertEquals(expected, actual);
    }
}