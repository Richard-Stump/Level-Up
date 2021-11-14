package com.mygdx.nextlevel.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.nextlevel.NextLevel;
import com.mygdx.nextlevel.Util.HoverListener;

public class ProfilePictureScreen implements Screen {
    public SpriteBatch batch;
    public Stage stage;
    public Viewport viewport;
    private OrthographicCamera camera;
    private TextureAtlas atlas;
    protected Skin skin;
    private NextLevel game;

    public static int textBoxBottomPadding = 20;
    public static int buttonWidth = 200;
    public static int rightMargin = 870;
    public static int topMargin = 450;


    public ProfilePictureScreen (NextLevel game) {
        atlas = new TextureAtlas("skin/uiskin.atlas");
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"), atlas);

        batch = game.batch;
        camera = new OrthographicCamera();
        viewport = new FitViewport(960, 500, camera);
        viewport.apply();

        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

        stage = new Stage(viewport, batch);
        this.game = game;
    }
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
        Table mainTable = new Table();
        Table buttonTable = new Table();

        Label title = new Label("Profile Picture Selection", skin);
//        Sprite pic1 = new Sprite(new Texture(Gdx.files.internal("userIcon.png")));
//        pic1.setSize(32,32);
//        Sprite pic2 = new Sprite(new Texture(Gdx.files.internal("smash-mario.jpeg")));
//        pic2.setSize(32,32);
//        Sprite pic3 = new Sprite(new Texture(Gdx.files.internal("odyssey-mario.jpeg")));
//        pic3.setSize(32,32);
//        Sprite pic4 = new Sprite(new Texture(Gdx.files.internal("mario.jpeg")));
//        pic4.setSize(32,32);

        Image pic1 = new Image(new Texture(Gdx.files.internal("userIcon.png")));
        Image pic2 = new Image(new Texture(Gdx.files.internal("smash-mario.jpeg")));
        Image pic3 = new Image(new Texture(Gdx.files.internal("odyssey-mario.jpeg")));
        Image pic4 = new Image(new Texture(Gdx.files.internal("mario.jpeg")));
        Image pic5 = new Image(new Texture(Gdx.files.internal("buildImg.jpg")));

        //pic1.scaleBy(-.5f);

        TextButton applyButton = new TextButton("Apply", skin);
        TextButton backButton = new TextButton("Back", skin);

        //event listeners
        backButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new ProfileMainMenu(game));
            }
        });
        backButton.addListener(new HoverListener());

        applyButton.addListener(new ClickListener() {
           //TODO: implement method to save to database
        });

        Table header = new Table();
        header.setDebug(true);
        header.add(backButton).left();
        header.add(title);

        //adding to main table
        mainTable.setDebug(true);
        mainTable.add(backButton).left().padTop(15).width(70).padLeft(15);
        mainTable.add(title).colspan(3).expandX().padTop(15);
        mainTable.add(new Label("", skin)).width(backButton.getWidth());
//        mainTable.add(header).expandX().colspan(3);
        mainTable.row();
        mainTable.add(new Label("", skin));
        mainTable.add(pic1).padBottom(10);
        mainTable.add(pic2).padBottom(10);
        mainTable.add(pic3).padBottom(10);
        mainTable.row();
        mainTable.add(new Label("", skin));
        mainTable.add(pic4).padBottom(10);
        mainTable.add(pic5).padBottom(10);
        mainTable.row();
        mainTable.add(new Label("", skin));
        mainTable.add(new Label("", skin));
        mainTable.add(new Label("", skin));
        mainTable.add(applyButton).expandY().padBottom(20).width(buttonWidth);

        mainTable.setFillParent(true);
        stage.addActor(mainTable);
//        HorizontalGroup userGroup = new HorizontalGroup();
//        userGroup.addActor(title);
//        userGroup.addActor(pic1);
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
