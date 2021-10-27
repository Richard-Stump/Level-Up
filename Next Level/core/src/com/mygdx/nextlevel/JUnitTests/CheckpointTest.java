package com.mygdx.nextlevel.JUnitTests;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.mygdx.nextlevel.NextLevel;
import com.mygdx.nextlevel.actors.Checkpoint;
import com.mygdx.nextlevel.actors.Player;
import com.mygdx.nextlevel.screens.CheckpointTestScreen;
import org.junit.*;

public final class CheckpointTest extends Game{
    public static SpriteBatch batch;
    public static BitmapFont font;
    public static OrthographicCamera camera;
    public static ExtendViewport viewport;
    public static Stage stage;
    public static NextLevel game;
    public static CheckpointTestScreen testScreen;

    public static Player player;
    public static Checkpoint checkpoint;

    @Override
    public void create () {
        batch = new SpriteBatch();
        font = new BitmapFont();
        camera = new OrthographicCamera();
        viewport = new ExtendViewport(960, 500, camera);
        testScreen = new CheckpointTestScreen(game);
        this.setScreen(testScreen);
    }

    @BeforeClass
    public static void setUp() {
        game = new NextLevel();

        System.out.println("Before");
        testScreen = new CheckpointTestScreen(game);
//        ((Game) Gdx.app.getApplicationListener()).setScreen(new CheckpointTestScreen(game));
        System.out.println("After");
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