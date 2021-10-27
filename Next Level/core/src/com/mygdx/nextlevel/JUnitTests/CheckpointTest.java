package com.mygdx.nextlevel.JUnitTests;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.nextlevel.actors.Checkpoint;
import com.mygdx.nextlevel.actors.Player;
import org.junit.*;

public final class CheckpointTest {
    public static World world;
    public static Player player;
    public static Checkpoint checkpoint;

    @BeforeClass
    public static void setUp() {
        player = new Player(new Vector2(32, 32), 0.25f, 0f);
        checkpoint = new Checkpoint(new Vector2(200, 32), player);
    }

    @Test
    public void testPlayerSpawn() {
        Vector2 spawn = new Vector2(32, 32);
        Assert.assertEquals("The spawnpoint is not at 0,0", spawn, player.getSpawnpoint());
    }
}