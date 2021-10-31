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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kotcrab.vis.ui.widget.tabbedpane.Tab;
import com.mygdx.nextlevel.Level;
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
import java.util.Objects;

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
    private Table mainTable;
    private VerticalGroup levelVerticalGroup;

    public TextButton searchButton;

    //search parameters:
    private TextField searchBar;
    private SelectBox<Difficulty> difficultyDropdown;
    private ArrayList<CheckBox> tagCheckBoxes;
    private ScrollPane scrollPane;

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
        selectedLevel = new Label("Level Selected: none", skin);
        selectedId = "";
    }

    public void show() {
        Gdx.input.setInputProcessor(stage);

        //start new layout
        mainTable = new Table();
        mainTable.setFillParent(true);
        stage.addActor(mainTable);
        //mainTable.setDebug(true);

        //TODO: put button somewhere that takes the user to a download page to download more levels ("Manage Levels" button)

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

        mainTable.add(backButton);
        mainTable.add(levelSelectLabel).expandX();
        mainTable.add(userInfo).width(200);
        mainTable.add(new Label("", skin)).width(backButton.getWidth());
        mainTable.row();


        //row 2: empty placeholder, scrollable table with levels, sorting box

        //populate a table full of information
        Table infoTable = new Table();
        levelVerticalGroup = getLevelVerticalGroup(new ArrayList<>(dbDownloaded.sortByTitle()));
        System.out.println(levelVerticalGroup.getPrefWidth());
        levelVerticalGroup.expand();

        //levelVerticalGroup.setWidth(500);
        //levelVerticalGroup.top();
        //levelVerticalGroup.padRight(20);
        //infoTable.setDebug(true);
        //infoTable.add(levelVerticalGroup).expandX();

        scrollPane = new ScrollPane(levelVerticalGroup, skin);
        scrollPane.setForceScroll(true, true);

        //make the sorting and search thing on right side
        Table searchSortGroup = getSearchSortTable();

        mainTable.add();
        mainTable.add(scrollPane);
        mainTable.add(searchSortGroup).top();
        mainTable.row();

        //row 3: empty placeholder, currently selected level, play button

        TextButton playButton = new TextButton("Play", skin);
        playButton.addListener(playLevel());

        mainTable.add();
        mainTable.add(selectedLevel).left().padBottom(20);
        mainTable.add(playButton).width(150).padBottom(20);

        //end
        mainTable.setFillParent(true);
        stage.addActor(mainTable);
    }

    private Table getSearchSortTable() {
        final Table table = new Table();
        Label searchLabel = new Label("Search:", skin);
        searchBar = new TextField("", skin);
        searchBar.setMessageText("By Level Name, Author");

        Label diffLabel = new Label("Difficulty:", skin);
        difficultyDropdown = new SelectBox<>(skin);
        difficultyDropdown.setItems(Difficulty.class.getEnumConstants());

        Label tagLabel = new Label("Tags:", skin);
        tagCheckBoxes = new ArrayList<>();
        for (Tag t: Tag.values()) {
            if (!t.equals(Tag.NONE)) {
                tagCheckBoxes.add(new CheckBox(t.toString(), skin));
            }
        }

        //TODO: add search by (star) rating

        searchButton = new TextButton("Search", skin);
        searchButton.addListener(searchButton());

        table.add(searchLabel).padBottom(10);
        table.row();
        table.add(searchBar).padBottom(20).width(200);
        table.row();
        table.add(diffLabel);
        table.row();
        table.add(difficultyDropdown).padBottom(10).width(150);
        table.row();
        table.add(tagLabel);
        table.row();

        for (CheckBox cb: tagCheckBoxes) {
            table.add(cb);
            table.row();
        }

        table.add(searchButton).padTop(20).width(150);
        table.row();
        return table;
    }

    private VerticalGroup getLevelVerticalGroup(ArrayList<LevelInfo> levels) {
        VerticalGroup vGroup = new VerticalGroup();
        //vGroup.setWidth(500);
        //columns: {image}, {title, author, difficulty, tags}, empty space, {rating, play count}
        for (LevelInfo levelInfo: levels) {
            String id = levelInfo.getId();
            HorizontalGroup levelGroup = new HorizontalGroup();
            //levelGroup.setWidth(400);

            levelGroup.addListener(selectLevel(levelGroup, id));

            //Image levelPreview = new Image();

            //levelGroup.addActor(levelPreview);
            levelGroup.addActor(getTitleGroup(id));
            levelGroup.addActor(getRatingGroup(id));
            levelGroup.padBottom(20);
            vGroup.addActor(levelGroup);
        }
        return vGroup;
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
                if (selectedId.equals("")) {
                    return;
                }
                System.out.println("Should be playing: " + dbDownloaded.searchByID(selectedId).getTitle());
                //TODO: open the game screen with the level that is selected

            }
        };
    }

    private ClickListener searchButton() {
        return new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                selectedId = "";
                selectedLevel.setText("Select a level");

                //search all levels and make a list that contains this string in the title or author:
                ArrayList<LevelInfo> ongoingList;
                if (!searchBar.getText().equals("")) {
                    ArrayList<LevelInfo> listTitles = new ArrayList<>(dbDownloaded.searchByTitle(searchBar.getText()));
                    ArrayList<LevelInfo> listAuthors = new ArrayList<>(dbDownloaded.searchByAuthor(searchBar.getText()));

                    ongoingList = new ArrayList<>(dbDownloaded.combineLists(listTitles, listAuthors));
                } else {
                    ongoingList = new ArrayList<>(dbDownloaded.sortByTitle());
                }
                System.out.println("after searching titles and authors: " + ongoingList.size());

                ArrayList<Tag> searchTags = new ArrayList<>();
                for (CheckBox cb: tagCheckBoxes) {
                    if (cb.isChecked()) {
                        searchTags.add(Tag.valueOf(cb.getLabel().getText().toString()));
                    }
                }

                ArrayList<LevelInfo> newList = new ArrayList<>();
                //filter out the ones that don't have at least one of these tags
                if (searchTags.size() != 0) {
                    for (LevelInfo level : ongoingList) {
                        ArrayList<Tag> tags = new ArrayList<>(level.getTags());
                        boolean hasAtLeastOne = false;
                        for (Tag levelTag : tags) {
                            for (Tag sTag : searchTags) {
                                //for each tag that the level has, if it matches one of the tags the user searched for, add it
                                if (levelTag.equals(sTag)) {
                                    hasAtLeastOne = true;
                                    break;
                                }
                            }
                        }
                        if (hasAtLeastOne) {
                            newList.add(level);
                        }
                    }
                    ongoingList.clear();
                    ongoingList = new ArrayList<>(newList);
                }
                System.out.println("after filtering tags: " + ongoingList.size());


                //search the list and only keep those that have this difficulty:
                ArrayList<LevelInfo> finalList = new ArrayList<>();
                if (!difficultyDropdown.getSelected().equals(Difficulty.NONE)) {
                    for (LevelInfo level : ongoingList) {
                        if (level.getDifficulty() == difficultyDropdown.getSelected().ordinal()) {
                            finalList.add(level);
                        }
                    }
                } else {
                    finalList = new ArrayList<>(ongoingList);
                }

                System.out.println("after filtering difficulty: " + finalList.size());

                //redo the table
                levelVerticalGroup.clear();
                levelVerticalGroup = getLevelVerticalGroup(finalList);
                levelVerticalGroup.top();
                levelVerticalGroup.padRight(20);
                scrollPane.setActor(levelVerticalGroup);
                scrollPane.setForceScroll(true, true);
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
