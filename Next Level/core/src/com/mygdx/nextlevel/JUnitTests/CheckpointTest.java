package com.mygdx.nextlevel.JUnitTests;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.mygdx.nextlevel.NextLevel;
import com.mygdx.nextlevel.actors.Checkpoint;
import com.mygdx.nextlevel.actors.Player;
import com.mygdx.nextlevel.screens.CheckpointTestScreen;
import org.junit.*;

public final class CheckpointTest {
    public static SpriteBatch batch;
    public static BitmapFont font;
    public static OrthographicCamera camera;
    public static ExtendViewport viewport;
    public static NextLevel testGame;
    public static CheckpointTestScreen testScreen;

    public static Player player;
    public static Checkpoint checkpoint;

    @BeforeClass
    public static void setUp() {
        testGame = new NextLevel();
        batch = testGame.batch;
        font = new BitmapFont();
        camera = new OrthographicCamera();
        viewport = new ExtendViewport(960, 500, camera);
        viewport.apply();

        testScreen = new CheckpointTestScreen(testGame);
        player = testScreen.getPlayer();
        checkpoint = testScreen.getCheckpoint();
    }

    @Test
    public void testSpawn() {
        Vector2 spawn = new Vector2(32, 32);

        Assert.assertEquals("The spawnpoint is not at 0,0", spawn, player.getSpawnpoint());
    }

    @Test
    public void testPlayerSpawn() {
        Assert.assertEquals("Checking", new Vector2(32, 32), player.getSpawnpoint());
    }
}