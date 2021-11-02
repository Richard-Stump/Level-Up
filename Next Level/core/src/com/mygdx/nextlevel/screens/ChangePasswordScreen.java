package com.mygdx.nextlevel.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
import com.mygdx.nextlevel.dbHandlers.ServerDBHandler;

public class ChangePasswordScreen implements Screen {

    public SpriteBatch batch;
    public Stage stage;
    public Viewport viewport;
    private OrthographicCamera camera;
    private TextureAtlas atlas;
    protected Skin skin;
    private NextLevel game;

    private String oldPassword;
    private String newPassword;
    private String verifyNewPassword;

    //static vars
    public static int textBoxWidth = 320;
    public static int textBoxBottomPadding = 20;
    public static int labelWidth = 320;
    public static int labelBottomPadding = 10;
    public static int buttonWidth = 320;

    public ChangePasswordScreen(NextLevel game) {
        atlas = new TextureAtlas("skin/neon-ui.atlas");
        skin = new Skin(Gdx.files.internal("skin/neon-ui.json"), atlas);

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
        //Stage should control input:
        Gdx.input.setInputProcessor(stage);
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);

        Table table = new Table();
        //table.setDebug(true);

        Label title = new Label("Change Password", skin);
        Label oldPasswordLabel = new Label("Current Password", skin);
        Label newPasswordLabel = new Label("New Password", skin);
        Label verifyNewPasswordLabel = new Label("Re-enter Your New Password", skin);

        TextField oldPasswordField = new TextField("", skin);
        TextField newPasswordField = new TextField("", skin);
        TextField verifyNewPasswordField = new TextField("", skin);

        oldPasswordField.setPasswordMode(true);
        newPasswordField.setPasswordMode(true);
        verifyNewPasswordField.setPasswordMode(true);

        oldPasswordField.setPasswordCharacter('*');
        newPasswordField.setPasswordCharacter('*');
        verifyNewPasswordField.setPasswordCharacter('*');

        TextButton backButton = new TextButton("Back", skin);
        TextButton changePasswordButton = new TextButton("Change Password", skin);

        table.add(backButton).left().expandX();
        //table.add(new Label("", skin)).width(labelWidth).expandX();
        table.row();
        table.add(title).padBottom(labelBottomPadding + 20).expandY().bottom();
        table.row();
        table.add(oldPasswordLabel).width(labelWidth).padBottom(labelBottomPadding - 5);
        table.row();
        table.add(oldPasswordField).width(textBoxWidth).padBottom(textBoxBottomPadding);
        table.row();
        table.add(newPasswordLabel).width(labelWidth).padBottom(labelBottomPadding - 5);
        table.row();
        table.add(newPasswordField).width(textBoxWidth).padBottom(textBoxBottomPadding);
        table.row();
        table.add(verifyNewPasswordLabel).width(labelWidth).padBottom(labelBottomPadding - 5);
        table.row();
        table.add(verifyNewPasswordField).width(textBoxWidth).padBottom(textBoxBottomPadding);
        table.row();
        table.add(changePasswordButton).width(buttonWidth + 18).expandY().top();

        //event listeners
        backButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }
        });
        backButton.addListener(new HoverListener());
        changePasswordButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {

                //TODO: update database and if error send message if successful send message
                // send back to main screen or ?
                //game.setScreen(new MainMenuScreen(game));
            }
        });

        table.setFillParent(true);
        stage.addActor(table);
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
