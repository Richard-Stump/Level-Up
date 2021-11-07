//package com.mygdx.nextlevel.screens;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.Screen;
//import com.badlogic.gdx.graphics.Cursor;
//import com.badlogic.gdx.graphics.GL20;
//import com.badlogic.gdx.graphics.OrthographicCamera;
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.graphics.g2d.Sprite;
//import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//import com.badlogic.gdx.graphics.g2d.TextureAtlas;
//import com.badlogic.gdx.scenes.scene2d.Stage;
//import com.badlogic.gdx.scenes.scene2d.ui.*;
//import com.badlogic.gdx.utils.viewport.FitViewport;
//import com.badlogic.gdx.utils.viewport.Viewport;
//import com.mygdx.nextlevel.NextLevel;
//
//public class ProfilePictureScreen implements Screen {
//    public SpriteBatch batch;
//    public Stage stage;
//    public Viewport viewport;
//    private OrthographicCamera camera;
//    private TextureAtlas atlas;
//    protected Skin skin;
//    private NextLevel game;
//
//    public static int textBoxBottomPadding = 20;
//    public static int buttonWidth = 200;
//    public static int rightMargin = 870;
//    public static int topMargin = 450;
//
//
//    public ProfilePictureScreen (NextLevel game) {
//        atlas = new TextureAtlas("skin/uiskin.atlas");
//        skin = new Skin(Gdx.files.internal("skin/uiskin.json"), atlas);
//
//        batch = game.batch;
//        camera = new OrthographicCamera();
//        viewport = new FitViewport(960, 500, camera);
//        viewport.apply();
//
//        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
//        camera.update();
//
//        stage = new Stage(viewport, batch);
//        this.game = game;
//    }
//    @Override
//    public void show() {
//        Gdx.input.setInputProcessor(stage);
//        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
//        Table mainTable = new Table();
//        Table buttonTable = new Table();
//
//        Label title = new Label("Profile Picture Selection", skin);
//        Sprite pic1 = new Sprite(new Texture(Gdx.files.internal("userIcon.png")));
//        pic1.setSize(32,32);
//        Sprite pic2 = new Sprite(new Texture(Gdx.files.internal("smash-mario.jpeg")));
//        pic2.setSize(32,32);
//        Sprite pic3 = new Sprite(new Texture(Gdx.files.internal("odyssey-mario.jpeg")));
//        pic3.setSize(32,32);
//        Sprite pic4 = new Sprite(new Texture(Gdx.files.internal("mario.jpeg")));
//        pic4.setSize(32,32);
//
//        HorizontalGroup userGroup = new HorizontalGroup();
//        userGroup.addActor(title);
//        userGroup.addActor(pic1);
//    }
//
//    @Override
//    public void render(float delta) {
//        Gdx.gl.glClearColor(.1f, .12f, .16f, 1);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//
//        stage.act();
//        stage.draw();
//    }
//
//    @Override
//    public void resize(int width, int height) {
//        viewport.update(width, height);
//        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
//        camera.update();
//    }
//
//    @Override
//    public void pause() {
//
//    }
//
//    @Override
//    public void resume() {
//
//    }
//
//    @Override
//    public void hide() {
//
//    }
//
//    @Override
//    public void dispose() {
//        skin.dispose();
//        atlas.dispose();
//    }
//}
