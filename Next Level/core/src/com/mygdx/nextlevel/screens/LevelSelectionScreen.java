//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.mygdx.nextlevel.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
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
import com.sun.java.accessibility.util.internal.ButtonTranslator;
import com.sun.tools.javac.comp.Check;
import jdk.internal.org.jline.utils.DiffHelper;
import org.w3c.dom.Text;

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

    private String selectedId;
    private Label selectedLevel;

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
        selectedLevel = new Label("Select a level", skin);
        selectedId = "";
    }

    public void show() {
        Gdx.input.setInputProcessor(stage);

        //start new layout
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);
        table.setDebug(true);

        //row 1: back button, screen title, current user overview
        //back button
        TextButton backButton = new TextButton("Back", skin);
        backButton.left();
        backButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }
        });

        //screen title
        Label levelSelectLabel = new Label("Select Level", skin);

        //current user overview
        HorizontalGroup userInfo = new HorizontalGroup();
        //add stuff here for user info

        table.add(backButton);
        table.add(levelSelectLabel);
        table.add(userInfo);
        table.row();


        //row 2: empty placeholder, scrollable table with levels, sorting box

        //populate a table full of information
        VerticalGroup levelTable = new VerticalGroup();
        //columns: {image}, {title, author, difficulty, tags}, empty space, {rating, play count}
        for (LevelInfo levelInfo: dbDownloaded.sortByTitle()) {
            String id = levelInfo.getId();
            HorizontalGroup levelGroup = new HorizontalGroup();

            levelGroup.addListener(selectLevel(levelGroup, id));

            //Image levelPreview = new Image();

            //levelGroup.addActor(levelPreview);
            levelGroup.addActor(getTitleGroup(id));
            levelGroup.addActor(getRatingGroup(id));
            levelGroup.padBottom(20);
            levelTable.addActor(levelGroup);
        }

        levelTable.top();
        levelTable.padRight(20);

        ScrollPane scrollPane = new ScrollPane(levelTable, skin);
        scrollPane.setForceScroll(true, true);

        //make the sorting and search thing on right side
        Table searchSortGroup = getSearchSortTable();

        table.add();
        table.add(scrollPane);
        table.add(searchSortGroup).top();
        table.row();

        //row 3: empty placeholder, currently selected level, play button

        TextButton playButton = new TextButton("Play", skin);
        playButton.addListener(playLevel());

        table.add();
        table.add(selectedLevel).left();
        table.add(playButton);

        //end

        stage.addActor(table);
    }

    private Table getSearchSortTable() {
        Table table = new Table();
        Label searchLabel = new Label("Search:", skin);
        TextField searchBar = new TextField("Search", skin);

        Label diffLabel = new Label("Difficulty:", skin);
        SelectBox<Difficulty> difficultyDropdown = new SelectBox<>(skin);
        difficultyDropdown.setItems(Difficulty.class.getEnumConstants());

        Label tagLabel = new Label("Tags:", skin);
        ArrayList<CheckBox> tagCheckBoxes = new ArrayList<>();
        for (Tag t: Tag.values()) {
            tagCheckBoxes.add(new CheckBox(t.toString(), skin));
        }

        TextButton searchButton = new TextButton("Search", skin);
        searchButton.addListener(new ClickListener() {
            //apply search and update table
        });

        table.add(searchLabel);
        table.row();
        table.add(searchBar);
        table.row();
        table.add(diffLabel);
        table.row();
        table.add(difficultyDropdown);
        table.row();
        table.add(tagLabel);
        table.row();

        for (CheckBox cb: tagCheckBoxes) {
            table.add(cb);
            table.row();
        }

        table.add(searchButton);
        table.row();
        return table;
    }

    /**
     * Groups title, author, difficulty, and tags into one VerticalGroup
     * @param id id of level
     * @return VerticalGroup
     */
    private VerticalGroup getTitleGroup(String id) {
        LevelInfo levelInfo;

        //verify database is connected
        if (!dbDownloaded.isDBActive()) {
            System.out.println("db is not active");
            return null;
        } else {
            levelInfo = dbDownloaded.searchByID(id);
        }

        VerticalGroup titleGroup = new VerticalGroup();

        //definitely not the best way to do click listeners

        Label levelName = new Label(levelInfo.getTitle(), skin);
        Label author = new Label(levelInfo.getAuthor(), skin);
        String difficultyString = Difficulty.values()[levelInfo.getDifficulty()].getDisplayName();
        Label difficultyAndTags = new Label(difficultyString + " - " + levelInfo.getTags().toString(), skin);

        titleGroup.addActor(levelName);
        titleGroup.addActor(author);
        titleGroup.addActor(difficultyAndTags);
        titleGroup.left();
        return titleGroup;
    }

    private VerticalGroup getRatingGroup(String id) {
        LevelInfo levelInfo;

        //verify database is connected
        if (!dbDownloaded.isDBActive()) {
            System.out.println("db is not active");
            return null;
        } else {
            levelInfo = dbDownloaded.searchByID(id);
        }

        VerticalGroup ratingGroup = new VerticalGroup();

        Label rating = new Label("" + levelInfo.getRating(), skin);
        Label playCount = new Label("" + levelInfo.getPlayCount(), skin);
        ratingGroup.addActor(rating);
        ratingGroup.addActor(playCount);

        //make it go to the right side of the parent
        ratingGroup.right();

        return ratingGroup;
    }

    private ClickListener selectLevel(final HorizontalGroup level, final String id) {
        return new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //outline the selected level
                selectedLevel.setText("Level Selected: " + dbDownloaded.searchByID(id).getTitle());
                selectedId = id;
            }
        };
    }

    private ClickListener playLevel() {
        return new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Should be playing: " + dbDownloaded.searchByID(selectedId).getTitle());
                //TODO: open the game screen with the level that is selected

            }
        };
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
