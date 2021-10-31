package com.mygdx.nextlevel.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
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
import com.mygdx.nextlevel.Account;
import com.mygdx.nextlevel.AccountList;
import com.mygdx.nextlevel.LevelInfo;
import com.mygdx.nextlevel.NextLevel;
import com.mygdx.nextlevel.dbHandlers.CreatedLevelsDB;
import com.mygdx.nextlevel.dbHandlers.DownloadedLevelsDB;
import com.mygdx.nextlevel.enums.Difficulty;

import java.util.ArrayList;

public class TestScreen extends AccountList implements Screen {

    public SpriteBatch batch;
    public Stage stage;
    public Viewport viewport;
    private OrthographicCamera camera;
    private TextureAtlas atlas;
    protected Skin skin;
    private NextLevel game;
    private DownloadedLevelsDB dbDownloaded;
    private CreatedLevelsDB dbCreated;
    private String selectedId;
    private Label selectedLevel;

    //left column
    public Label levelName;
    public Label author;
    public Label difficulty;

    //right column
    public Label rating;
    public Label playCount;

    private ScrollPane scrollPane;

    //static vars
    public static int rightColumnWidth = 100;
    public static int topBottomPad = 30;
    public static int leftColumnWidth = 200;
    public static int textPad = 10;


    public TestScreen() {}
    public TestScreen(NextLevel game) {
        atlas = new TextureAtlas("skin/neon-ui.atlas");
        skin = new Skin(Gdx.files.internal("skin/neon-ui.json"), atlas);

        batch = game.batch;
        camera = new OrthographicCamera();
        viewport = new FitViewport(960, 500, camera);
        viewport.apply();

        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

        dbDownloaded = new DownloadedLevelsDB();
        selectedLevel = new Label("Level Selected: none", skin);
        selectedId = "";
        stage = new Stage(viewport, batch);
        this.game = game;
    }

    @Override
    public void show() {
        //Stage should control input:
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();

        table.setDebug(true);

        VerticalGroup infoVertical;

        Table infoTable = getLevelTable(new ArrayList<>(dbDownloaded.sortByTitle()));
        infoVertical = new VerticalGroup();
        infoVertical.addActor(infoTable);

        scrollPane = new ScrollPane(infoVertical, skin);
        scrollPane.setForceScroll(true, true);
        table.add(new Label("title", skin));
        table.row();
        table.add(scrollPane).setActorHeight(300);
        table.row();
        table.add(selectedLevel).left();

        table.setFillParent(true);
        stage.addActor(table);
    }

    private Table getLevelTable(ArrayList<LevelInfo> levels) {
        Table infoTable = new Table();
        infoTable.setDebug(true);

        for (LevelInfo levelInfo: levels) {
            String id = levelInfo.getId();
            infoTable.add(getLeftColumn(id)).padTop(topBottomPad);
            infoTable.add(getRightColumn(id)).padTop(10);
            infoTable.row();

            //getDiffTags(id);
            //infoTable.add(difficulty);
        }

        return infoTable;
    }

/*    public void getDiffTags(String id) {
        LevelInfo levelInfo;

        //verify database is connected
        if (!dbDownloaded.isDBActive()) {
            System.out.println("db is not active");
            return;
        } else {
            levelInfo = dbDownloaded.searchByID(id);
        }

        String difficultyString = Difficulty.values()[levelInfo.getDifficulty()].getDisplayName();
        difficulty = new Label(difficultyString + " - " + levelInfo.getTags().toString(), skin);

        difficulty.addListener(selectLevelListener(id));
    }*/

    private Table getLeftColumn(String id) {
        Table leftTable = new Table();
        LevelInfo levelInfo;
        leftTable.setDebug(true);

        //verify database is connected
        if (!dbDownloaded.isDBActive()) {
            System.out.println("db is not active");
            return null;
        } else {
            levelInfo = dbDownloaded.searchByID(id);
        }

        //adding left column labels
        levelName = new Label(levelInfo.getTitle(), skin);
        author = new Label(levelInfo.getAuthor(), skin);

        String difficultyString = Difficulty.values()[levelInfo.getDifficulty()].getDisplayName();
        difficulty = new Label(difficultyString + " - " + levelInfo.getTags().toString(), skin);

        difficulty.addListener(selectLevelListener(id));
        levelName.addListener(selectLevelListener(id));
        author.addListener(selectLevelListener(id));

        //adding to left table
        leftTable.add(levelName).width(leftColumnWidth).left();
        leftTable.row();
        leftTable.add(author).width(leftColumnWidth).left();
        leftTable.row();
        leftTable.add(difficulty).width(leftColumnWidth).left();

        return leftTable;
    }

    private Table getRightColumn(String id) {
        Table rightTable = new Table();
        LevelInfo levelInfo;
        rightTable.setDebug(true);

        //verify database is connected
        if (!dbDownloaded.isDBActive()) {
            System.out.println("db is not active");
            return null;
        } else {
            levelInfo = dbDownloaded.searchByID(id);
        }

        //right column labels
        rating = new Label("" + levelInfo.getRating() + "/5", skin);
        playCount = new Label("" + levelInfo.getPlayCount(), skin);

        rating.addListener(selectLevelListener(id));
        playCount.addListener(selectLevelListener(id));

        //add to right table
        rightTable.add(rating).width(rightColumnWidth).left();
        rightTable.row();
        rightTable.add(playCount).width(rightColumnWidth).left();

        return rightTable;
    }

    private ClickListener selectLevelListener(final String id) {
        return new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //outline the selected level
                selectedLevel.setText("Level Selected: " + dbDownloaded.searchByID(id).getTitle());
                selectedId = id;
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

    public boolean changePass(String username) {
        for (Account a : accList) {
            if (a.getUsername().equals(username)) {
                a.setPassword("password");
                return true;
            }
        }
        return false;
    }
}
