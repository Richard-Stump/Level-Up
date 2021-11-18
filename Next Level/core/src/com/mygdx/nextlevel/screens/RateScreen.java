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
import com.mygdx.nextlevel.Util.ErrorDialog;
import com.mygdx.nextlevel.Util.HoverListener;
import com.mygdx.nextlevel.dbHandlers.ServerDBHandler;

public class RateScreen implements Screen {
    public SpriteBatch batch;
    public Stage stage;
    public Viewport viewport;
    private OrthographicCamera camera;
    private TextureAtlas atlas;
    protected Skin skin;
    private NextLevel game;
    private String levelid;

    public String lastButtonClicked = "";

    public static int buttonWidth = 200;
    public static int buttonBottomPad = 10;
    ServerDBHandler db;

    public RateScreen(NextLevel game) {
        db = new ServerDBHandler();
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
        Label titleLabel = new Label("Rate This Level", titleStyle);

        TextButton star1 = new TextButton("One Star", skin);
        TextButton star2 = new TextButton("Two Stars", skin);
        TextButton star3 = new TextButton("Three Stars", skin);
        TextButton star4 = new TextButton("Four Stars", skin);
        TextButton star5 = new TextButton("Five Stars", skin);

        star1.addListener(rateListener(1));
        star2.addListener(rateListener(2));
        star3.addListener(rateListener(3));
        star4.addListener(rateListener(4));
        star5.addListener(rateListener(5));

        star1.addListener(new HoverListener());
        star2.addListener(new HoverListener());
        star3.addListener(new HoverListener());
        star4.addListener(new HoverListener());
        star5.addListener(new HoverListener());

        //TextButton rateButton = new TextButton("Rate", skin);

        //Create Table
        Table mainTable = new Table();
        //mainTable.setDebug(true);

        mainTable.add(titleLabel).padBottom(40);
        mainTable.row();
        mainTable.add(star1).width(buttonWidth).padBottom(buttonBottomPad);
        mainTable.row();
        mainTable.add(star2).width(buttonWidth).padBottom(buttonBottomPad);
        mainTable.row();
        mainTable.add(star3).width(buttonWidth).padBottom(buttonBottomPad);
        mainTable.row();
        mainTable.add(star4).width(buttonWidth).padBottom(buttonBottomPad);
        mainTable.row();
        mainTable.add(star5).width(buttonWidth).padBottom(35);
        //mainTable.row();
        //mainTable.add(rateButton).width(buttonWidth + 30);

        //Set table to fill stage
        mainTable.setFillParent(true);

        //Add table to stage
        stage.addActor(mainTable);
    }

    private ClickListener rateListener(final int rate) {
        return new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //TODO: add rating to database
//                db.addLevelRating(levelid, rate);

                //TODO: if successful show dialog then set screen to main menu
                ErrorDialog dialog = new ErrorDialog("Rating Level", "MainMenuScreen", game, skin, "Thank you for rating!", stage);
                //Dialog dialog1 = dialog.getErrorDialog();
            }
        };
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
