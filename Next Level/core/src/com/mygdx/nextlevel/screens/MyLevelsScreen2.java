package com.mygdx.nextlevel.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.nextlevel.LevelInfo;
import com.mygdx.nextlevel.NextLevel;
import com.mygdx.nextlevel.Util.ErrorDialog;
import com.mygdx.nextlevel.Util.HoverListener;
import com.mygdx.nextlevel.dbHandlers.CreatedLevelsDB;
import com.mygdx.nextlevel.dbHandlers.DownloadedLevelsDB;
import com.mygdx.nextlevel.dbHandlers.ServerDBHandler;
import com.mygdx.nextlevel.enums.Difficulty;
import com.mygdx.nextlevel.enums.Tag;
import org.w3c.dom.Text;
import java.util.List;

import java.util.ArrayList;

public class MyLevelsScreen2 implements Screen {
    private NextLevel game;
    private Skin skin;
    private TextureAtlas atlas;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Stage stage;
    private final String titleText = "Level Select";
    private CreatedLevelsDB dbCreated;
    private ServerDBHandler dbServer;

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

    public String username;

    //static vars
    public static int rightColumnWidth = 120;
    public static int topBottomPad = 30;
    public static int leftColumnWidth = 200;
    public static int labelHeight = 25;

    public MyLevelsScreen2(NextLevel game) {
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
        dbServer = new ServerDBHandler();
        dbCreated.updateCreatedDatabase();
        selectedLevel = new Label("Level Selected: none", skin);
        selectedId = "";
        activeDB = "created";

        username = LoginScreen.curAcc;
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
        activeDBLabel = new Label("Your created levels:", skin);

        mainTable.add(backButton).height(labelHeight +10).padTop(10).padLeft(5);
        mainTable.add(activeDBLabel).padTop(10).expandX().left().padLeft(5).height(labelHeight);
        mainTable.add(new Label("", skin)).width(backButton.getWidth());
        mainTable.add(new Label("", skin)).width(backButton.getWidth());
        mainTable.row();

        //set up level information section
        List<LevelInfo> levelInfoList = dbCreated.sortByTitle();
        ArrayList<LevelInfo> levelInfoArrayList = new ArrayList<>(levelInfoList);

        Table infoTable = getLevelTable(levelInfoArrayList);
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
            infoTable.add(getLeftColumn(id)).padLeft(5);
            infoTable.add(getRightColumn(id));

            TextButton deleteButton = new TextButton("Delete", skin);
            deleteButton.addListener(deleteLevelListener(id));
            deleteButton.addListener(new HoverListener());
            infoTable.add(deleteButton).padBottom(10);

            TextButton editButton = new TextButton("Edit", skin);
            //TODO: create listener for edit
            editButton.addListener(editLevelListener(id));
            editButton.addListener(new HoverListener());
            infoTable.add(editButton).padBottom(10).padLeft(5);

            TextButton.TextButtonStyle toggleStyle = skin.get("toggle", TextButton.TextButtonStyle.class);

            TextButton publishButton = new TextButton("Publish", skin);
            if (levelInfo.isPublic()) {
                publishButton.setText("Unpublish");
            }
            //TODO: create listener for publish
            publishButton.addListener(publishLevelListener(levelInfo, id, publishButton));
            publishButton.addListener(new HoverListener());
            infoTable.add(publishButton).padBottom(10).padLeft(5).width(90);

            infoTable.row();

            Image line = new Image(new Texture(Gdx.files.internal("horzline.png")));
            infoTable.add(line).colspan(5);
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
        if (activeDB.equals("created")) {
            if (!dbCreated.isDBActive()) {
                System.out.println("db is not active");
                return null;
            } else {
                levelInfo = dbCreated.searchByID(id);
            }
        } else {
            return null;
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
        leftTable.add(levelName).width(leftColumnWidth - 20).left().height(labelHeight);
        leftTable.row();
        leftTable.add(difficulty).width(leftColumnWidth - 20).left().height(labelHeight);

        return leftTable;
    }

    private Table getRightColumn(String id) {
        Table rightTable = new Table();
        LevelInfo levelInfo;
        //rightTable.setDebug(true);

        int numRaters = 0;

        //verify database is connected
        if (activeDB.equals("created")) {
            if (!dbCreated.isDBActive()) {
                System.out.println("db is not active");
                return null;
            } else {
                levelInfo = dbCreated.searchByID(id);
            }
        } else {
            return null;
        }

        if (!dbServer.isDBActive()) {
            System.out.println("db is not active");
            return null;
        } else {
            levelInfo = dbServer.getLevelByID(id, false);
            numRaters = dbServer.getRatingCount(id);
        }

        //right column labels
        float rate = levelInfo.getRating();
        if (rate < 0) {
            rating = new Label("Rating: NA/5  [#" + numRaters + "]", skin);
        } else {
            //right column labels
            rating = new Label("Rating: " + levelInfo.getRating() + "/5  [#" + numRaters + "]", skin);
        }
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

    private ClickListener selectLevelListener(final String id) {
        return new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //outline the selected level
                String levelSelectedStr;
                if (activeDB.equals("created")) {
                    levelSelectedStr = dbCreated.searchByID(id).getTitle();
                } else {
                    levelSelectedStr = "error";
                }
                selectedLevel.setText("Level Selected: " + levelSelectedStr);
                selectedId = id;
            }
        };
    }

    private ClickListener editLevelListener(final String id) {
        return new ClickListener() {
          @Override
          public void clicked(InputEvent event, float x, float y) {
              ServerDBHandler handler = new ServerDBHandler();

              LevelInfo info = handler.getLevelByID(id, true);
              if (info.isPublic()) {
                  ErrorDialog errorDialog = new ErrorDialog(skin, "Unable to edit public level, please delete.", stage);
              } else {
                  game.setScreen(new EditLevelScreen(game, info));
              }
          }
        };
    }

    private ClickListener publishLevelListener(final LevelInfo level, final String id, final TextButton publishButton) {
        return new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Im in publish level listener");
                selectedLevel.setText("Level Selected: " + dbCreated.searchByID(id).getTitle());
                //check if level is published
                //if yes, show dialog that states that it is already published
                if (level.getPlayCount() > 0) {
                    if (level.isPublic()) {
//                        ErrorDialog unpublishLevelDialog = new ErrorDialog(skin, "Level is already published. Are you " +
//                                "sure you want to unpublish " + dbCreated.searchByID(id).getTitle() + "?", stage, "Cancel",
//                                "Unpublish", id, publishButton, level,game);
                        ErrorDialog dialog = new ErrorDialog(skin, "Please delete level if you want to unpublish.", stage);

                    } else {
                        //if no, are you sure you want to publish, then success or fail dialog
                        ErrorDialog publishLevelDialog = new ErrorDialog(skin, "Are you sure you want to publish "
                                + dbCreated.searchByID(id).getTitle() + "?", stage, "Cancel",
                                "Publish", id, publishButton, level,game);
                    }
                } else {
                    ErrorDialog dialog = new ErrorDialog(skin, "You must complete the level in order to publish!", stage);
                    dbServer.updateLevel(level);
                }
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
                if (activeDB.equals("created")) {
                    selectedLevel.setText("Level Selected: " + dbCreated.searchByID(id).getTitle());
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
                            if (activeDB.equals("created")) {
                                dbCreated.removeLevelInfo(id);
                                ServerDBHandler serverDB = new ServerDBHandler();
                                serverDB.removeLevel(id);
                                serverDB.closeConnection();
                            }
                            levelVerticalGroup.clear();
                            levelVerticalGroup.addActor(getRefreshedLevelList(activeDB));
                        }
                    }
                };

                if (activeDB.equals("created")) {
                    dialog.text("Are you sure you want to delete " + dbCreated.searchByID(id).getTitle() + " from your account?\nThis cannot be undone");
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
                if (activeDB.equals("created")) {
                    System.out.println("Should be playing: " + dbCreated.searchByID(selectedId).getTitle());
                }
                LevelInfo levelInfo = dbServer.getLevelByID(selectedId, true);
                System.out.println("Should be downloading: " + levelInfo.getTitle());


                ((Game)Gdx.app.getApplicationListener()).setScreen(new GameScreen2(game, dbCreated.searchByID(selectedId).getId()));
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

        if (table.equals("created")) {
            if (!searchBar.getText().equals("")) {
                ArrayList<LevelInfo> listTitles = new ArrayList<>(dbCreated.searchByTitle(searchBar.getText()));
                ArrayList<LevelInfo> listAuthors = new ArrayList<>(dbCreated.searchByAuthor(searchBar.getText()));

                ongoingList = new ArrayList<>(dbCreated.combineLists(listTitles, listAuthors));
            } else {
                ongoingList = new ArrayList<>(dbCreated.sortByTitle());
                //ongoingList = new ArrayList<>(dbHandler.getUsersCreatedLevels(username));
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
