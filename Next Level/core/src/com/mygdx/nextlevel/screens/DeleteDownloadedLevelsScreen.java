package com.mygdx.nextlevel.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
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
import com.mygdx.nextlevel.screens.LevelDownloadScreen;
import com.mygdx.nextlevel.screens.ProfileMainMenu;

import java.util.ArrayList;

public class DeleteDownloadedLevelsScreen implements Screen {
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

    private ButtonGroup<TextButton> buttonGroup;
    private String activeDB;
    private Label activeDBLabel;

    //left column
    public Label levelName;
    public Label author;
    public Label difficulty;

    //right column
    public Label rating;
    public Label playCount;

    //static vars
    public static int rightColumnWidth = 150;
    public static int topBottomPad = 30;
    public static int leftColumnWidth = 350;
    public static int labelHeight = 25;

    public DeleteDownloadedLevelsScreen(NextLevel game) {
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

        dbCreated = new CreatedLevelsDB();
        dbCreated.updateCreatedDatabase();
        dbDownloaded = new DownloadedLevelsDB();
        selectedLevel = new Label("Level Selected: none", skin);
        selectedId = "";
        activeDB = "downloaded";
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
                game.setScreen(new LevelDownloadScreen(game));
            }
        });

        //screen title
        activeDBLabel = new Label("Your downloaded levels:", skin);
        activeDB = "downloaded";

//        downloadedLevelsButton.addListener(new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                downloadedLevelsButton.setColor(Color.GREEN);
//                createdLevelsButton.setColor(Color.BLUE);
//                activeDB = "downloaded";
//
//                activeDBLabel.setText("Your downloaded levels:");
//
//                selectedId = "";
//                selectedLevel.setText("Select a level");
//                levelVerticalGroup.clear();
//                levelVerticalGroup.addActor(getRefreshedLevelList(activeDB));
//            }
//        });


        mainTable.add(backButton).height(labelHeight +10).padTop(10).padLeft(5);
        mainTable.add(activeDBLabel).padTop(10).expandX().left().padLeft(5).height(labelHeight);
        //mainTable.add(hgButtons).width(200);
        mainTable.add(new Label("", skin)).width(backButton.getWidth());
        mainTable.add(new Label("", skin)).width(backButton.getWidth());
        mainTable.row();

        //set up level information section
        Table infoTable = getLevelTable(new ArrayList<>(dbCreated.sortByTitle()));
        levelVerticalGroup = new VerticalGroup();
        levelVerticalGroup.addActor(infoTable);
        levelVerticalGroup.padRight(50);

        levelVerticalGroup.padBottom(30);
        levelVerticalGroup.top();

        scrollPane = new ScrollPane(levelVerticalGroup, skin);
        scrollPane.setForceScroll(false, true);

        //make the sorting and search thing on right side
        Table searchSortGroup = getSearchSortTable();

        mainTable.add();
        mainTable.add(scrollPane).expandY();
        mainTable.add(searchSortGroup).top().padLeft(5);
        mainTable.row();

        //row 3: empty placeholder, currently selected level, play button

        TextButton playButton = new TextButton("Play", skin);
        playButton.addListener(playLevel());

        mainTable.add();
        mainTable.add(selectedLevel).left().padBottom(20).padLeft(5);
        mainTable.add(playButton).width(150).padBottom(20).padLeft(5);

        selectedId = "";
        selectedLevel.setText("Select a level");
        levelVerticalGroup.clear();
        levelVerticalGroup.addActor(getRefreshedLevelList(activeDB));

        //end
        mainTable.setFillParent(true);
        stage.addActor(mainTable);

        //System.out.println(activeDB);
    }

    private Table getSearchSortTable() {
        final Table table = new Table();
        Label searchLabel = new Label("Search:", skin);
        searchBar = new TextField("", skin);
        searchBar.setMessageText("By Level Name");

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

        table.add(searchLabel).padBottom(10).height(labelHeight + 5);
        table.row();
        table.add(searchBar).padBottom(10).width(200);
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

        table.add(searchButton).padTop(20).width(150);
        table.row();
        return table;
    }

    private Table getLevelTable(ArrayList<LevelInfo> levels) {
        Table infoTable = new Table();
        //infoTable.setDebug(true);

        for (LevelInfo levelInfo: levels) {
            String id = levelInfo.getId();
            infoTable.add(getLeftColumn(id)).padTop(topBottomPad).padLeft(5);
            infoTable.add(getRightColumn(id)).padTop(5);

            TextButton deleteButton = new TextButton("Delete", skin);
            deleteButton.addListener(deleteLevelListener(id));
            infoTable.add(deleteButton).width(80).padBottom(15);

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
        if (activeDB.equals("downloaded")) {
            if (!dbDownloaded.isDBActive()) {
                System.out.println("db is not active");
                return null;
            } else {
                levelInfo = dbDownloaded.searchByID(id);
            }
        } else {
            return null;
        }

        //adding left column labels
        if (levelInfo  != null) {
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
            leftTable.add(difficulty).width(leftColumnWidth - 10).left().height(labelHeight);

            return leftTable;
        }
        return new Table();
    }

    private Table getRightColumn(String id) {
        Table rightTable = new Table();
        LevelInfo levelInfo;
        //rightTable.setDebug(true);

        //verify database is connected
        if (activeDB.equals("downloaded")) {
            if (!dbDownloaded.isDBActive()) {
                System.out.println("db is not active");
                return null;
            } else {
                levelInfo = dbDownloaded.searchByID(id);
            }
        } else {
            return null;
        }

        //right column labels
        if (levelInfo != null) {
            rating = new Label("" + levelInfo.getRating() + "/5", skin);
            playCount = new Label("" + levelInfo.getPlayCount(), skin);

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
        return new Table();
    }

    private ClickListener selectLevelListener(final String id) {
        return new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //outline the selected level
                String levelSelectedStr;
                if (activeDB.equals("downloaded")){
                    levelSelectedStr = dbDownloaded.searchByID(id).getTitle();
                } else {
                    levelSelectedStr = "error";
                }
                selectedLevel.setText("Level Selected: " + levelSelectedStr);
                selectedId = id;
            }
        };
    }

    private Stage delStage;
    private boolean isDeleting = false;

    private ClickListener deleteLevelListener(final String id) {
        return new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //outline the selected level
                if (activeDB.equals("downloaded")) {
                    selectedLevel.setText("Level Selected: " + dbDownloaded.searchByID(id).getTitle());
                }
                selectedId = id;
                isDeleting = true;

                //make the user confirm their decision:
                Dialog dialog = new Dialog("Delete Level", skin) {
                    @Override
                    protected void result(Object object) {
                        System.out.println("result: " + object);
                        Gdx.input.setInputProcessor(stage);

                        if ((Boolean) object) {
                            if (activeDB.equals("downloaded")) {
                                dbDownloaded.removeLevelInfo(id);
                            }
                            levelVerticalGroup.clear();
                            levelVerticalGroup.addActor(getRefreshedLevelList(activeDB));
                        }
                    }
                };

                if (activeDB.equals("downloaded")) {
                    dialog.text("Are you sure you want to delete " + dbDownloaded.searchByID(id).getTitle() + " locally?");
                }

                dialog.button("Delete", true);
                dialog.button("Cancel", false);

                OrthographicCamera delCamera = new OrthographicCamera(500, 500);
                delCamera.position.set(delCamera.viewportWidth / 2.0F, delCamera.viewportHeight / 2.0F, 0.0F);
                delCamera.update();

                Viewport delViewport = new StretchViewport(500, 500, delCamera);
                delViewport.apply();

                delStage = new Stage(viewport, batch);

                Gdx.input.setInputProcessor(delStage);

                //delStage.addActor(dialog);

                dialog.show(delStage);
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

                levelVerticalGroup.clear();
                levelVerticalGroup.addActor(getRefreshedLevelList(activeDB));
            }
        };
    }

    private Table getRefreshedLevelList(String table) {
        //search all levels and make a list that contains this string in the title or author:
        ArrayList<LevelInfo> ongoingList;

//        if (table.equals("created")) {
//            if (!searchBar.getText().equals("")) {
//                ArrayList<LevelInfo> listTitles = new ArrayList<>(dbCreated.searchByTitle(searchBar.getText()));
//                ArrayList<LevelInfo> listAuthors = new ArrayList<>(dbCreated.searchByAuthor(searchBar.getText()));
//
//                ongoingList = new ArrayList<>(dbCreated.combineLists(listTitles, listAuthors));
//            } else {
//                ongoingList = new ArrayList<>(dbCreated.sortByTitle());
//            }
        if (table.equals("downloaded")) {
            if (!searchBar.getText().equals("")) {
                ArrayList<LevelInfo> listTitles = new ArrayList<>(dbDownloaded.searchByTitle(searchBar.getText()));
                ArrayList<LevelInfo> listAuthors = new ArrayList<>(dbDownloaded.searchByAuthor(searchBar.getText()));

                ongoingList = new ArrayList<>(dbDownloaded.combineLists(listTitles, listAuthors));
            } else {
                ongoingList = new ArrayList<>(dbDownloaded.sortByTitle());
            }
        } else {
            return null;
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

        Table refreshTable;
        refreshTable = getLevelTable(finalList);
        return refreshTable;
    }


    public void render(float delta) {
        Gdx.gl.glClearColor(0.1F, 0.12F, 0.16F, 1.0F);
        Gdx.gl.glClear(16384);
        this.stage.act();
        this.stage.draw();

        if (isDeleting) {
            delStage.act();
            delStage.draw();
        }
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
