package com.mygdx.nextlevel.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.nextlevel.NextLevel;

public class ErrorMessageScreen implements Screen {

    public SpriteBatch batch;
    public Stage stage;
    public Viewport viewport;
    private OrthographicCamera camera;
    private TextureAtlas atlas;
    protected Skin skin;
    private NextLevel game;

    public String message;
    public float countdown;
    public String nextScreen;

    public ErrorMessageScreen (NextLevel game, String message, String nextScreen) {
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
        this.message = message;
        this.nextScreen = nextScreen;

        Table table = new Table();
        table.setFillParent(true);

        Label errorLabel = new Label(this.message, skin);
        Label disposeLabel = new Label("This screen will automatically close", skin);

        table.center();
        table.add(errorLabel);
        table.row();
        table.add(disposeLabel);

        stage.addActor(table);

        countdown = 2.5f;
    }

    @Override
    public void show() {

    }

    public void update(float delta) {
        countdown -= delta;

        if (countdown < 0.0f) {
            switch (nextScreen) {
                case("RegisterScreen"):
                    game.setScreen(new RegisterScreen(game));
                    break;
                case("LoginScreen"):
                    game.setScreen(new LoginScreen(game));
                    break;
                case("ForgetPasswordScreen"):
                    game.setScreen(new ForgetPasswordScreen(game));
                    break;
                default:
                    break;
            }
            dispose();
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.1f, .12f, .16f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
        update(delta);
    }

    @Override
    public void resize(int width, int height) {

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
        //stage.dispose();
    }
}
