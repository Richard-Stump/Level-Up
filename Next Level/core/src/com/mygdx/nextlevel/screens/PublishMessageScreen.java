package com.mygdx.nextlevel.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.nextlevel.LevelInfo;
import com.mygdx.nextlevel.NextLevel;
import com.mygdx.nextlevel.Util.HoverListener;
import com.mygdx.nextlevel.dbHandlers.CreatedLevelsDB;
import com.mygdx.nextlevel.dbHandlers.ServerDBHandler;
import com.mygdx.nextlevel.screens.editor.EditorLevel;

import javax.swing.text.Style;
import java.io.File;
import java.io.FileNotFoundException;

public class PublishMessageScreen implements Screen {
    private NextLevel game;
    private Skin skin;
    private TextureAtlas atlas;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Stage stage;

    private final String titleText = "Create New Level";
    public static int buttonWidth = 140;

    public PublishMessageScreen(NextLevel game, String levelId) {
        this.game = game;
        this.atlas = new TextureAtlas(Gdx.files.internal("skin/uiskin.atlas"));
        this.skin = new Skin(Gdx.files.internal("skin/uiskin.json"), atlas);
        this.batch = game.batch;

        // Create a new orthographic camera and set it to view the center of the screen.
        camera = new OrthographicCamera();
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

        //Set up the viewport
        viewport = new FitViewport(960, 500, camera);
        viewport.apply();

        stage = new Stage(viewport, batch);

        initUi();

        ServerDBHandler db = new ServerDBHandler();
        db.publishLevel(levelId);
    }

    private void initUi() {
        Table table = new Table();
        table.setFillParent(true);

        Label.LabelStyle titleStyle = skin.get("title-plain", Label.LabelStyle.class);

        table.top();
        table.add(new Label("!!! Congratulations !!!", titleStyle)).center();
        table.row();
        table.add(new Label("Your level is now available for everyone else to enjoy!", titleStyle)).center();
        table.row();
        TextButton button = new TextButton("Back to My Levels", skin);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MyLevelsScreen2(game));
            }
        });
        table.add(button).center();

        stage.addActor(table);
    }

    @Override
    public void show() {
        //Stage should control input:
        Gdx.input.setInputProcessor(stage);
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.1f, .12f, .16f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        skin.dispose();
        atlas.dispose();
    }
}
