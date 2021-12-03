//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.mygdx.nextlevel.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.nextlevel.LevelInfo;
import com.mygdx.nextlevel.NextLevel;
import com.mygdx.nextlevel.Util.HoverListener;
import com.mygdx.nextlevel.dbHandlers.CreatedLevelsDB;
import com.mygdx.nextlevel.dbHandlers.DownloadedLevelsDB;
import com.mygdx.nextlevel.dbHandlers.ServerDBHandler;
import com.mygdx.nextlevel.enums.Difficulty;
import com.mygdx.nextlevel.enums.Tag;

import java.util.ArrayList;

public class LevelDownloadScreen implements Screen {
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
    private ServerDBHandler dbServer;

    public static final int STAGE_WIDTH = 1920 / 2;
    public static final int STAGE_HEIGHT = 1080 / 2;

    private String selectedId;
    private Label selectedLevel;
    private Table mainTable;
    private VerticalGroup levelVerticalGroup;

    public TextButton searchButton;
    public TextButton downloadAndPlayButton;

    //search parameters:
    private TextField searchBar;
    private SelectBox<Difficulty> difficultyDropdown;
    private ArrayList<CheckBox> tagCheckBoxes;
    private ScrollPane scrollPane;
    private SelectBox<String> sortDropdown;


    //left column
    public Label levelName;
    public Label author;
    public Label difficulty;

    //right column
    public Label rating;
    public Label playCount;


    //static vars
    public static int rightColumnWidth = 250;
    public static int topBottomPad = 30;
    public static int leftColumnWidth = 400;
    public static int labelHeight = 25;

    public LevelDownloadScreen(NextLevel game) {
        this.game = game;
        atlas = new TextureAtlas(Gdx.files.internal("skin/uiskin.atlas"));
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"), atlas);

        batch = game.batch;

        camera = new OrthographicCamera(STAGE_WIDTH, STAGE_HEIGHT);
        camera.position.set(camera.viewportWidth / 2.0F, camera.viewportHeight / 2.0F, 0.0F);
        camera.update();

        viewport = new StretchViewport(STAGE_WIDTH, STAGE_HEIGHT, camera);
        viewport.apply();

        stage = new Stage(viewport, batch);

        dbDownloaded = new DownloadedLevelsDB();
        dbCreated = new CreatedLevelsDB();
        dbServer = new ServerDBHandler();
        selectedLevel = new Label("Level Selected: none", skin);
        selectedId = "";
    }

    public void show() {
        Gdx.input.setInputProcessor(stage);
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);

        //start new layout
        mainTable = new Table();
        mainTable.setFillParent(true);
        stage.addActor(mainTable);
        //mainTable.setDebug(true);

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
        Label levelSelectLabel = new Label("Public Levels:", skin);

        //current user overview
        //HorizontalGroup userInfo = new HorizontalGroup();
        //Label usernameLabel = new Label(LoginScreen.getCurAcc(), skin);
        //userInfo.addActor(usernameLabel);
        //add stuff here for user info

        final TextButton downloadedLevelsButton = new TextButton("Delete Downloaded Levels", skin);
        downloadedLevelsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game)Gdx.app.getApplicationListener()).setScreen(new DeleteDownloadedLevelsScreen(game));
            }
        });
        downloadedLevelsButton.addListener(new HoverListener());

        mainTable.add(backButton).height(labelHeight +10).padTop(10).padLeft(5);
        mainTable.add(levelSelectLabel).expandX().left().padLeft(5).padTop(10);
        //mainTable.add(usernameLabel).width(200).padTop(10);
        //TODO: add tab to downloaded level deletion screen here
        mainTable.add(downloadedLevelsButton);
        mainTable.add(new Label("", skin)).width(backButton.getWidth());
        mainTable.row();

        //set up level information section
        Table infoTable = getLevelTable(new ArrayList<>(dbServer.sortPublicByTitle()));
        levelVerticalGroup = new VerticalGroup();
        levelVerticalGroup.addActor(infoTable);

        scrollPane = new ScrollPane(levelVerticalGroup, skin);
        scrollPane.setForceScroll(true, true);

        //make the sorting and search thing on right side
        Table searchSortGroup = getSearchSortTable();

        mainTable.add();
        mainTable.add(scrollPane).expandY();
        mainTable.add(searchSortGroup).top().padLeft(5);
        mainTable.row();

        //row 3: empty placeholder, currently selected level, play button

        downloadAndPlayButton = new TextButton("Play", skin);
        downloadAndPlayButton.addListener(downloadLevel());
        downloadAndPlayButton.addListener(new HoverListener());
        downloadAndPlayButton.setColor(Color.LIGHT_GRAY);

        mainTable.add();
        mainTable.add(selectedLevel).left().padBottom(20).padLeft(5);
        mainTable.add(downloadAndPlayButton).width(200).padBottom(20).padLeft(5);

        //end
        mainTable.setFillParent(true);
        stage.addActor(mainTable);
    }

    /**
     * Gets the search table that will hold all the search parameters that the user can use
     * @return
     */
    private Table getSearchSortTable() {
        final Table table = new Table();
        Label searchLabel = new Label("Search:", skin);
        searchBar = new TextField("", skin);
        searchBar.setMessageText("By Level Name, Author");

        HorizontalGroup sortByGroup = new HorizontalGroup();
        Label labelSortBy = new Label("Sort by:", skin);
        //labelSortBy.setHeight(labelHeight + 10);
        sortDropdown = new SelectBox<>(skin);
        sortDropdown.setItems("Title", "Rating");
        sortByGroup.addActor(labelSortBy);
        sortByGroup.addActor(sortDropdown);

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
        //table.setDebug(true);

        searchButton = new TextButton("Search", skin);
        searchButton.addListener(searchButton());

        table.add(searchLabel).padBottom(10).height(labelHeight + 5);
        table.row();
        table.add(searchBar).padBottom(10).width(200);
        table.row();
        table.add(sortByGroup).height(labelHeight + 5).padBottom(10);
        table.row();
        table.add(diffLabel);
        table.row();
        table.add(difficultyDropdown).padBottom(10).width(150);
        table.row();
        table.add(tagLabel).height(labelHeight);
        table.row();

        for (CheckBox cb: tagCheckBoxes) {
            table.add(cb).height(labelHeight);
            table.row();
        }

        table.add(searchButton).padTop(20).width(200);
        //table.row();
        return table;
    }

    private Table getLevelTable(ArrayList<LevelInfo> levels) {
        Table infoTable = new Table();
        //infoTable.setDebug(true);

        for (LevelInfo levelInfo: levels) {
            String id = levelInfo.getId();
            infoTable.add(getLeftColumn(id)).padTop(topBottomPad).padLeft(5);
            infoTable.add(getRightColumn(id)).padTop(5);
            infoTable.row();
        }

        return infoTable;
    }

    /**
     * Groups title, author, difficulty, and tags into one VerticalGroup
     * @param id id of level
     * @return VerticalGroup
     */
    private Table getLeftColumn(String id) {
        Table leftTable = new Table();
        LevelInfo levelInfo;
        //leftTable.setDebug(true);

        //verify database is connected
        if (!dbServer.isDBActive()) {
            System.out.println("db is not active");
            return null;
        } else {
            levelInfo = dbServer.getLevelByID(id, false);
        }

        //adding left column labels
        levelName = new Label(levelInfo.getTitle(), skin);
        author = new Label(levelInfo.getAuthor(), skin);

        String difficultyString = Difficulty.values()[levelInfo.getDifficulty()].getDisplayName();
        difficulty = new Label(difficultyString + " - " + levelInfo.getTags().toString(), skin);

        difficulty.addListener(selectLevelListener(id));
        difficulty.addListener(new HoverListener());
        levelName.addListener(selectLevelListener(id));
        levelName.addListener(new HoverListener());
        author.addListener(selectLevelListener(id));
        author.addListener(new HoverListener());

        //adding to left table
        leftTable.add(levelName).width(leftColumnWidth - 10).left().height(labelHeight);
        leftTable.row();
        leftTable.add(author).width(leftColumnWidth - 10).left().height(labelHeight);
        leftTable.row();
        leftTable.add(difficulty).width(leftColumnWidth - 10).left().height(labelHeight);

        return leftTable;
    }

    private Table getRightColumn(String id) {
        Table rightTable = new Table();
        LevelInfo levelInfo;
        //rightTable.setDebug(true);

        int numRaters = 0;

        //verify database is connected
        if (!dbServer.isDBActive()) {
            System.out.println("db is not active");
            return null;
        } else {
            levelInfo = dbServer.getLevelByID(id, false);
            numRaters = dbServer.getRatingCount(id);
        }

        float rateInt = levelInfo.getRating();
        if (rateInt < 0) {
            rating = new Label("Rating: NA/5  [#" + numRaters + "]", skin);
        } else {
            //right column labels
            rating = new Label("Rating: " + levelInfo.getRating() + "/5  [#" + numRaters + "]", skin);
        }
        playCount = new Label("Play Count: " + levelInfo.getPlayCount(), skin);

        rating.addListener(selectLevelListener(id));
        rating.addListener(new HoverListener());
        playCount.addListener(selectLevelListener(id));
        playCount.addListener(new HoverListener());

        //add to right table
        rightTable.add(rating).width(rightColumnWidth).left().height(labelHeight);
        rightTable.row();
        rightTable.add(playCount).width(rightColumnWidth).left().height(labelHeight);

        return rightTable;
    }

    private ClickListener selectLevelListener(final String id) {
        return new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //outline the selected level
                selectedLevel.setText("Level Selected: " + dbServer.getLevelByID(id, false).getTitle());
                selectedId = id;

                if ((dbDownloaded.searchByID(id) != null) || (dbCreated.searchByID(id) != null)) {
                    downloadAndPlayButton.setTouchable(Touchable.enabled);
                    downloadAndPlayButton.setText("Play");
                    downloadAndPlayButton.setColor(Color.RED);
                } else {
                    downloadAndPlayButton.setTouchable(Touchable.enabled);
                    downloadAndPlayButton.setText("Download and Play");
                    downloadAndPlayButton.setColor(Color.GREEN);
                }
            }
        };
    }

    private ClickListener downloadLevel() {
        return new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //TODO: if the level is already downloaded directly go to play

                if (selectedId.equals("")) {
                    return;
                }
                LevelInfo levelInfo = dbServer.getLevelByID(selectedId, true);
                System.out.println("Should be downloading: " + levelInfo.getTitle());
                dbDownloaded.addLevelInfo(levelInfo);

                downloadAndPlayButton.setTouchable(Touchable.disabled);
                downloadAndPlayButton.setText("Play");
                downloadAndPlayButton.setColor(Color.RED);

                //TODO: after downloading set screen to level to play pass the level id
                ((Game)Gdx.app.getApplicationListener()).setScreen(new GameScreen2(game, dbCreated.searchByID(selectedId).getId()));
            }
        };
    }

    private ClickListener searchButton() {
        return new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                downloadAndPlayButton.setTouchable(Touchable.enabled);
                downloadAndPlayButton.setText("Play");
                downloadAndPlayButton.setColor(Color.LIGHT_GRAY);

                selectedId = "";
                selectedLevel.setText("Select a level");

                //search all levels and make a list that contains this string in the title or author:
                ArrayList<LevelInfo> ongoingList;
                if (!searchBar.getText().equals("")) {
                    ArrayList<LevelInfo> listTitles = new ArrayList<>(dbServer.searchByTitle(searchBar.getText(), false));
                    ArrayList<LevelInfo> listAuthors = new ArrayList<>(dbServer.searchByAuthor(searchBar.getText(), false));

                    ongoingList = new ArrayList<>(dbDownloaded.combineLists(listTitles, listAuthors));
                } else {
                    ongoingList = new ArrayList<>(dbServer.sortPublicByTitle());
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

                ArrayList<LevelInfo> sortedList = new ArrayList<>();

                //resort the list (could be optimized)
                if (sortDropdown.getSelected().equals("Rating")) {
                    int count = finalList.size();
                    for (int i = 0; i < count; i++) {
                        LevelInfo highestRatingLevel = finalList.get(0);
                        for (LevelInfo levelInfo: finalList) {
                            if (levelInfo.getRating() > highestRatingLevel.getRating()) {
                                highestRatingLevel = levelInfo;
                            }
                        }
                        sortedList.add(highestRatingLevel);
                        finalList.remove(highestRatingLevel);
                    }
                } else {
                    sortedList = finalList;
                }

                //redo the table
                levelVerticalGroup.clear();
                Table refreshTable;
                refreshTable = getLevelTable(sortedList);
                levelVerticalGroup.addActor(refreshTable);
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