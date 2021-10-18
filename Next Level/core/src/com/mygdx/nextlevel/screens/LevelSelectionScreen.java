//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.mygdx.nextlevel.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.nextlevel.LevelInfo;
import com.mygdx.nextlevel.NextLevel;
import com.mygdx.nextlevel.dbHandlers.CreatedLevelsDB;
import com.mygdx.nextlevel.dbHandlers.DownloadedLevelsDB;
import com.mygdx.nextlevel.enums.Difficulty;
import com.mygdx.nextlevel.enums.Tag;
import jdk.internal.org.jline.utils.DiffHelper;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

public class LevelSelectionScreen implements Screen {
    private NextLevel game;
    private Skin skin;
    private TextureAtlas atlas;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Stage stage;
    private final String titleText = "Level Select";
    private DownloadedLevelsDB dbDownloaded;
    private CreatedLevelsDB dbCreated;

    public static final int STAGE_WIDTH = 1920 / 2;
    public static final int STAGE_HEIGHT = 1080 / 2;

    public LevelSelectionScreen(NextLevel game) {
        this.game = game;
        atlas = new TextureAtlas(Gdx.files.internal("skin/neon-ui.atlas"));
        skin = new Skin(Gdx.files.internal("skin/neon-ui.json"), atlas);

        batch = game.batch;

        camera = new OrthographicCamera(STAGE_WIDTH, STAGE_HEIGHT);
        camera.position.set(camera.viewportWidth / 2.0F, camera.viewportHeight / 2.0F, 0.0F);
        camera.update();

        viewport = new StretchViewport(STAGE_WIDTH, STAGE_HEIGHT, camera);
        viewport.apply();

        stage = new Stage(viewport, batch);

        dbDownloaded = new DownloadedLevelsDB();
    }

    public void show() {
        Gdx.input.setInputProcessor(stage);
        TextButton backButton = new TextButton("Back", skin);

        backButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }
        });

        backButton.setPosition(0.0f, STAGE_HEIGHT - backButton.getHeight());
        stage.addActor(backButton);


        Table levels = new Table();

        levels.left();
        levels.setPosition(10.0f, STAGE_HEIGHT - backButton.getHeight() - 100.0f);

        //for each level (that we can fit on the screen)
        //need to implement a scrolling thing
        float rightSide = 300.0f;
        float padding = 20.0f;
        for (int i = 0; i < dbDownloaded.sortByTitle().size(); i++) {
            ArrayList<VerticalGroup> levelBox = getLevelTable(dbDownloaded.sortByTitle().get(i).getId());
            for (VerticalGroup group: levelBox) {
                group.padRight(rightSide - group.getWidth());
                levels.add(group).left().padBottom(padding);
            }
            levels.row();
        }
        stage.addActor(levels);
    }

    private ArrayList<VerticalGroup> getLevelTable(String id) {
        ArrayList<VerticalGroup> levelTable = new ArrayList<>();
        LevelInfo levelInfo;

        if (!dbDownloaded.isDBActive()) {
            System.out.println("db is not active");
            return null;
        } else {
            levelInfo = dbDownloaded.searchByID(id);
        }

        //add stuff to the table here
        //TODO: if using image preview for levels, figure out how to get image from tilemap or something
        //Image imagePreview = new Image();

        //group together title, author, difficulty, and tags
        VerticalGroup metadata = new VerticalGroup();
        metadata.columnAlign(Align.left);
        Label levelName = new Label(levelInfo.getTitle(), skin);
        Label author = new Label(levelInfo.getAuthor(), skin);
        String difficultyString = Difficulty.values()[levelInfo.getDifficulty()].getDisplayName();
        Label difficultyAndTags = new Label(difficultyString + " - " + levelInfo.getTags().toString(), skin);

        metadata.addActor(levelName);
        metadata.addActor(author);
        metadata.addActor(difficultyAndTags);
        levelTable.add(metadata);

        //rating and playcount will be on the right side of the box
        VerticalGroup ratingAndPlayCount = new VerticalGroup();
        ratingAndPlayCount.columnAlign(Align.right);
        Label rating = new Label("" + levelInfo.getRating(), skin);
        Label playCount = new Label("" + levelInfo.getPlayCount(), skin);
        ratingAndPlayCount.addActor(rating);
        ratingAndPlayCount.addActor(playCount);

        ratingAndPlayCount.right();
        levelTable.add(ratingAndPlayCount);

        return levelTable;
    }

    public void render(float delta) {
        Gdx.gl.glClearColor(0.1F, 0.12F, 0.16F, 1.0F);
        Gdx.gl.glClear(16384);
        this.stage.act();
        this.stage.draw();
    }

    public void resize(int width, int height) {
        this.viewport.update(width, height);
        this.camera.position.set(this.camera.viewportWidth / 2.0F, this.camera.viewportHeight / 2.0F, 0.0F);
        this.camera.update();
    }

    public void pause() {
    }

    public void resume() {
    }

    public void hide() {
    }

    public void dispose() {
        this.skin.dispose();
        this.atlas.dispose();
    }
}
