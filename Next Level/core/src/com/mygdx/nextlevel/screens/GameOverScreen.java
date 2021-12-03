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
import com.mygdx.nextlevel.TileMap;
import com.mygdx.nextlevel.Util.HoverListener;
import com.mygdx.nextlevel.actors.Player2;
import com.mygdx.nextlevel.dbHandlers.ServerDBHandler;
import com.mygdx.nextlevel.hud.Hud2;

public class GameOverScreen implements Screen {
    public SpriteBatch batch;
    public Stage stage;
    public Viewport viewport;
    private OrthographicCamera camera;
    private TextureAtlas atlas;
    protected Skin skin;
    private NextLevel game;
    private Hud2 hud;
    private Player2 player;
    private String levelInfo;

    public String title;

    public Label titleLabel;
    public Label scoreLabel;
    public Label timeLabel;
    public Label finishConditionLabel;
    public Label timerLabel;
    public Label recordLabel;

    public TextButton mainMenuButton;
    public TextButton tryAgainButton;
    public TextButton okButton;

    //static vars
    public static int labelWidth = 300;
    public static int labelBottomPadding = 20;
    public static int buttonWidth = 190;

    public boolean showRating = false;
    public String levelid;
    public double time;
    ServerDBHandler db;

    public GameOverScreen (NextLevel game, Hud2 hud2, String title, Player2 player2, String levelInfo) {
        atlas = new TextureAtlas("skin/uiskin.atlas");
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"), atlas);
        this.levelInfo = levelInfo;
        db = new ServerDBHandler();
        batch = game.batch;
        camera = new OrthographicCamera();
        viewport = new FitViewport(960, 500, camera);
        viewport.apply();

        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

        stage = new Stage(viewport, batch);
        this.game = game;
        this.hud = hud2;
        this.title = title;
        this.player = player2;
        this.levelid = levelInfo;
        this.time = time;

        if (title.equals("VICTORY")) {
            showRating = true;
        } else {
            showRating = false;
        }
    }
    public GameOverScreen (NextLevel game, Hud2 hud2, String title, Player2 player2, String levelInfo, double time) {
        atlas = new TextureAtlas("skin/uiskin.atlas");
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"), atlas);
        this.levelInfo = levelInfo;

        batch = game.batch;
        camera = new OrthographicCamera();
        viewport = new FitViewport(960, 500, camera);
        viewport.apply();

        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

        stage = new Stage(viewport, batch);
        this.game = game;
        this.hud = hud2;
        this.title = title;
        this.player = player2;
        this.levelid = levelInfo;
        this.time = time;

        if (title.equals("VICTORY")) {
            showRating = true;
        } else {
            showRating = false;
        }
    }

    public GameOverScreen() {
        atlas = new TextureAtlas("skin/uiskin.atlas");
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"), atlas);

        batch = game.batch;
        camera = new OrthographicCamera();
        viewport = new FitViewport(960, 500, camera);
        viewport.apply();

        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

        stage = new Stage(viewport, batch);
    }

    @Override
    public void show() {
        //Stage should control input:
        Gdx.input.setInputProcessor(stage);
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);

        Label.LabelStyle titleStyle = skin.get("title-plain", Label.LabelStyle.class);
        //Label.LabelStyle textStyle = skin.get("subtitle", Label.LabelStyle.class);

        titleLabel = new Label(title, titleStyle);
        System.out.println(player.getScore());
        scoreLabel = new Label("Score : " + player.getScore(), titleStyle);
        //scoreLabel.scaleBy(.9f);
        timeLabel = new Label("Time Remaining : " + hud.getTime(), titleStyle);
        timerLabel = new Label("Timer : " + time, titleStyle);
        if (this.time < db.getRecordTime(levelInfo)) {
            db.updateRecordTime(levelInfo, this.time);
        }
        recordLabel = new Label("Record : " + db.getRecordTime(levelInfo), titleStyle);

        finishConditionLabel = new Label("Finishing Conditions : ", titleStyle);
        mainMenuButton = new TextButton("Main Menu", skin);
        tryAgainButton = new TextButton("Try Again", skin);
        okButton = new TextButton("Ok", skin);

        mainMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
                ((Game)Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen(game));
            }
        });
        mainMenuButton.addListener(new HoverListener());

        tryAgainButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
                //TODO: set screen to restart current level
                ((Game)Gdx.app.getApplicationListener()).setScreen(new GameScreen2(game, levelInfo, GameScreen2.Mode.PLAY, null));
            }
        });
        tryAgainButton.addListener(new HoverListener());

        okButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
                //TODO: set screen to restart current level
                ((Game)Gdx.app.getApplicationListener()).setScreen(new RateScreen(game, levelid));
            }
        });
        okButton.addListener(new HoverListener());

        Table mainTable = new Table();

        mainTable.setSkin(skin);
        //mainTable.setDebug(true);
        mainTable.add(title).colspan(2).width(labelWidth + 100).padBottom(labelBottomPadding + 20);
        mainTable.row();
        mainTable.add(scoreLabel).left().padRight(10).colspan(2).width(labelWidth).padBottom(labelBottomPadding);
        mainTable.row();
        mainTable.add(timerLabel).left().padRight(10).colspan(2).width(labelWidth).padBottom(labelBottomPadding);
        mainTable.row();
        mainTable.add(timeLabel).left().padRight(10).colspan(2).width(labelWidth).padBottom(labelBottomPadding + 20);
        mainTable.row();
        mainTable.add(recordLabel).left().padRight(10).colspan(2).width(labelWidth).padBottom(labelBottomPadding);
        mainTable.row();
//        mainTable.add(finishConditionLabel).left().padRight(10).colspan(2).width(labelWidth).padBottom(labelBottomPadding + 20);
//        mainTable.row();
        if (showRating) {
            mainTable.add(okButton).width(buttonWidth);
        } else {
            mainTable.add(mainMenuButton).width(buttonWidth).right().padRight(5);
            mainTable.add(tryAgainButton).width(buttonWidth).left().padLeft(5);
        }
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
