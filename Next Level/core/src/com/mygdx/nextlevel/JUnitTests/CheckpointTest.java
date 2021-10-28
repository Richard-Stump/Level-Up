package com.mygdx.nextlevel.JUnitTests;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.nextlevel.actors.Checkpoint;
import com.mygdx.nextlevel.actors.Player;
import jdk.jfr.Description;
import org.junit.*;

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
    }

    @Test
    @Description("Test to check the player Spawn")
    public void testPlayerSpawn() {
        Vector2 spawn = new Vector2(32, 32);
        Assert.assertEquals("The original Spawnpoint of the Player is not set correctly", spawn, player.getSpawnpoint());
    }

    @Test
    @Description("Test to check if the player Spawn is updated when going over Checkpoint")
    public void testPlayerUpdateSpawn() {
        checkpoint.changeSpawn(player);
        Vector2 spawn = new Vector2(200, 32);
        Assert.assertEquals("The Spawnpoint of the Player is not updated correctly", spawn, player.getSpawnpoint());
    }

    @Test
    @Description("Test to check if the player Spawnpoint is updated when deaths reset")
    public void testPlayerResetSpawn() {
        player.setSpawnpoint(new Vector2(400, 400));
        player.setSpawnpoint(player.getWorldSpawn());
        Vector2 spawn = new Vector2(32, 32);
        Assert.assertEquals("The Spawnpoint of the Player is not updated Correctly", spawn, player.getSpawnpoint());
    }

    @Test
    @Description("Test initial lives of the player")
    public void testPlayerLives() {
        int lives = 3;
        Assert.assertEquals("The original lives of the player is not set correctly", lives, player.getLives());
    }

    @Test
    @Description("Test to check if there is a life added to the Player when going over Checkpoint")
    public void testPlayerUpdateLives() {
        int lives = 4;
        player.addLife(1);
        Assert.assertEquals("The lives of the player is not updated correctly", lives, player.getLives());
    }
}