package com.mygdx.nextlevel.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
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
import com.mygdx.nextlevel.NextLevel;
import org.w3c.dom.Text;

public class ForgetPasswordScreen implements Screen {

    public SpriteBatch batch;
    public Stage stage;
    public Viewport viewport;
    private OrthographicCamera camera;
    private TextureAtlas atlas;
    protected Skin skin;
    private NextLevel game;

    public String username;

    //static vars
    public static int textBoxWidth = 320;
    public static int textBoxBottomPadding = 20;
    public static int buttonWidth = 170;

    public ForgetPasswordScreen(NextLevel game) {
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

        Table table = new Table();

        //table.setDebug(true);

        Label title = new Label("Next Level", skin);
        Label forgotPassText = new Label("Forgot Password", skin);

        //textfield
        TextField usernameText = new TextField("", skin);
        usernameText.setMessageText("Username");

        TextButton enterButton = new TextButton("Request", skin);
        TextButton backButton = new TextButton("Back", skin);

        //stacking widgets for layout
        Stack mainStack = new Stack();
        mainStack.add(new Image(new Texture(Gdx.files.internal("rect.png"))));

        Table overlay = new Table();
        //overlay.setDebug(true);
        overlay.add(forgotPassText).left().padLeft(16);
        overlay.row();
        overlay.add(new Label("", skin)).width(350).height(25);
        overlay.row();
        overlay.add(usernameText).width(textBoxWidth).left().padLeft(13).padBottom(16);
        mainStack.add(overlay);

        table.add(title).padBottom(textBoxBottomPadding).colspan(2);
        table.row();

        table.add(mainStack).padBottom(textBoxBottomPadding).colspan(2);
        table.row();
        table.add(backButton).width(buttonWidth);
        table.add(enterButton).width(buttonWidth);

        //click listeners
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game)Gdx.app.getApplicationListener()).setScreen(new LoginScreen(game));
            }
        });
        enterButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //TODO: change password if a valid username else show error message
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