package com.mygdx.nextlevel.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.nextlevel.NextLevel;
import com.mygdx.nextlevel.Util.HoverListener;
import com.mygdx.nextlevel.hud.Hud2;

public class GameOverScreen implements Screen {
    public SpriteBatch batch;
    public Stage stage;
    public Viewport viewport;
    private OrthographicCamera camera;
    private TextureAtlas atlas;
    protected Skin skin;
    private NextLevel game;
    private Hud2 hud2;

    public Label title;
    public Label scoreLabel;
    public Label timeLabel;
    public Label finishConditionLabel;

    public TextButton mainMenuButton;
    public TextButton tryAgainButton;

    //static vars
    public static int labelWidth = 300;
    public static int labelBottomPadding = 20;
    public static int buttonWidth = 190;

    public GameOverScreen (NextLevel game, Hud2 hud2) {
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
        //Stage should control input:
        Gdx.input.setInputProcessor(stage);
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);

        Label.LabelStyle titleStyle = skin.get("title-plain", Label.LabelStyle.class);
        //TextButton button = new TextButton("Click me!", titleStyle);
        //Label.LabelStyle textStyle = skin.get("subtitle", Label.LabelStyle.class);

        title = new Label("Game Over...", titleStyle);
        scoreLabel = new Label("Score : " + hud2.getScore(), titleStyle);
        //scoreLabel.scaleBy(.9f);

        timeLabel = new Label("Time Remaining : " + hud2.getTime(), titleStyle);
        finishConditionLabel = new Label("Finishing Conditions : ", titleStyle);
        mainMenuButton = new TextButton("Main Menu", skin);
        tryAgainButton = new TextButton("Try Again", skin);

        mainMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
                ((Game)Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen(game));
            }
        });
        mainMenuButton.addListener(new HoverListener());

        Table mainTable = new Table();
        //mainTable.setDebug(true);
        mainTable.add(title).colspan(2).width(labelWidth + 100).padBottom(labelBottomPadding + 20);
        mainTable.row();
        mainTable.add(scoreLabel).colspan(2).width(labelWidth).padBottom(labelBottomPadding);
        mainTable.row();
        mainTable.add(timeLabel).colspan(2).width(labelWidth).padBottom(labelBottomPadding);
        mainTable.row();
        mainTable.add(finishConditionLabel).colspan(2).width(labelWidth).padBottom(labelBottomPadding + 20);
        mainTable.row();
        mainTable.add(mainMenuButton).width(buttonWidth).right().padRight(5);
        mainTable.add(tryAgainButton).width(buttonWidth).left().padLeft(5);

        //Set table to fill stage
        mainTable.setFillParent(true);

        //Add table to stage
        stage.addActor(mainTable);
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
