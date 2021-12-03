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
import com.mygdx.nextlevel.Asset;
import com.mygdx.nextlevel.NextLevel;
import com.mygdx.nextlevel.Util.HoverListener;
import com.mygdx.nextlevel.dbHandlers.AssetHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AssetDownloadScreen implements Screen {
    private NextLevel game;
    private Skin skin;
    private TextureAtlas atlas;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Stage stage;
    private AssetHandler dbAssets;

    public static final int STAGE_WIDTH = 1920 / 2;
    public static final int STAGE_HEIGHT = 1080 / 2;

    private String selectedId;
    private Label selectedAsset;
    private Table mainTable;
    private VerticalGroup assetVerticalGroup;

    public TextButton searchButton;
    public TextButton downloadButton;

    //search parameters:
    private TextField searchBar;
    private ScrollPane scrollPane;

    //left column
    public Label assetName;
    public Label author;

    //static vars
    public static int rightColumnWidth = 250;
    public static int topBottomPad = 30;
    public static int leftColumnWidth = 400;
    public static int labelHeight = 25;

    public AssetDownloadScreen(NextLevel game) {
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

        dbAssets = new AssetHandler();
        selectedAsset = new Label("Asset Selected: none", skin);
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
        Label assetSelectLabel = new Label("All Assets:", skin);

        final TextButton downloadedAssetsButton = new TextButton("Delete Downloaded Assets", skin);
        downloadedAssetsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game)Gdx.app.getApplicationListener()).setScreen(new DeleteDownloadedAssetsScreen(game, false));
            }
        });
        downloadedAssetsButton.addListener(new HoverListener());

        mainTable.add(backButton).height(labelHeight +10).padTop(10).padLeft(5);
        mainTable.add(assetSelectLabel).expandX().left().padLeft(5).padTop(10);
        //mainTable.add(usernameLabel).width(200).padTop(10);
        mainTable.add(downloadedAssetsButton);
        mainTable.add(new Label("", skin)).width(backButton.getWidth());
        mainTable.row();

        //set up asset information section
        Table infoTable = getAssetTable(new ArrayList<>(dbAssets.sortAllByTitle()));
        assetVerticalGroup = new VerticalGroup();
        assetVerticalGroup.addActor(infoTable);

        assetVerticalGroup.padRight(50);
        assetVerticalGroup.padBottom(100);
        assetVerticalGroup.top();

        scrollPane = new ScrollPane(assetVerticalGroup, skin);
        scrollPane.setForceScroll(true, true);

        //make the sorting and search thing on right side
        Table searchSortGroup = getSearchSortTable();

        mainTable.add();
        mainTable.add(scrollPane).expandY();
        mainTable.add(searchSortGroup).top().padLeft(5);
        mainTable.row();

        //row 3: empty placeholder, currently selected asset, play button

        downloadButton = new TextButton("Play", skin);
        downloadButton.addListener(downloadAsset());
        downloadButton.addListener(new HoverListener());
        downloadButton.setColor(Color.LIGHT_GRAY);

        mainTable.add();
        mainTable.add(selectedAsset).left().padBottom(20).padLeft(5);
        mainTable.add(downloadButton).width(200).padBottom(20).padLeft(5);

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
        searchBar.setMessageText("By Asset Name, Author");

        searchButton = new TextButton("Search", skin);
        searchButton.addListener(searchButton());

        table.add(searchLabel).padBottom(10).height(labelHeight + 5);
        table.row();
        table.add(searchBar).padBottom(10).width(200);
        table.row();

        table.add(searchButton).padTop(20).width(200);
        return table;
    }

    private Table getAssetTable(ArrayList<Asset> assets) {
        Table infoTable = new Table();

        for (Asset asset: assets) {
            String id = asset.getAssetID();
            infoTable.add(getLeftColumn(id)).padTop(topBottomPad).padLeft(5);
            infoTable.add(getRightColumn(id)).padTop(5);
            infoTable.row();
        }

        return infoTable;
    }

    /**
     * Groups title and author into one VerticalGroup
     * @param id id of asset
     * @return VerticalGroup
     */
    private Table getLeftColumn(String id) {
        Table leftTable = new Table();
        Asset asset;

        //verify database is connected
        if (dbAssets == null) {
            System.out.println("db is not active");
            return null;
        } else {
            asset = dbAssets.searchByID(id);
        }

        //adding left column labels
        assetName = new Label(asset.name, skin);
        author = new Label(asset.author, skin);

        assetName.addListener(selectAssetListener(id));
        assetName.addListener(new HoverListener());
        author.addListener(selectAssetListener(id));
        author.addListener(new HoverListener());

        //adding to left table
        leftTable.add(assetName).width(leftColumnWidth - 10).left().height(labelHeight);
        leftTable.row();
        leftTable.add(author).width(leftColumnWidth - 10).left().height(labelHeight);

        return leftTable;
    }

    private Table getRightColumn(String id) {
        Table rightTable = new Table();
        Asset asset;
        //rightTable.setDebug(true);

        //TODO: show image?
        //verify database is connected
        if (dbAssets == null) {
            System.out.println("db is not active");
            return null;
        } else {
            asset = dbAssets.searchByID(id);
        }

        return rightTable;
    }

    private ClickListener selectAssetListener(final String id) {
        return new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //outline the selected asset
                selectedAsset.setText("Asset Selected: " + dbAssets.searchByID(id).name);
                selectedId = id;

                File file = new File(id);
                if (file.exists()) {
                    //file is already downloaded
                    downloadButton.setTouchable(Touchable.disabled);
                    downloadButton.setText("Already Downloaded");
                    downloadButton.setColor(Color.RED);
                } else {
                    //file is not downloaded
                    downloadButton.setTouchable(Touchable.enabled);
                    downloadButton.setText("Download");
                    downloadButton.setColor(Color.GREEN);
                }
            }
        };
    }

    private ClickListener downloadAsset() {
        return new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (selectedId.equals("")) {
                    return;
                }
                Asset asset = dbAssets.searchByID(selectedId);
                System.out.println("Should be downloading: " + asset.name);
                dbAssets.downloadAsset(selectedId);

                downloadButton.setTouchable(Touchable.disabled);
                downloadButton.setText("Downloaded");
                downloadButton.setColor(Color.RED);
            }
        };
    }

    private ClickListener searchButton() {
        return new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                downloadButton.setTouchable(Touchable.enabled);
                downloadButton.setText("Download");
                downloadButton.setColor(Color.LIGHT_GRAY);

                selectedId = "";
                selectedAsset.setText("Select an asset");

                //search all assets and make a list that contains this string in the title or author:
                ArrayList<Asset> list;
                if (!searchBar.getText().equals("")) {
                    ArrayList<Asset> listTitles = new ArrayList<>(dbAssets.searchByName(searchBar.getText()));
                    ArrayList<Asset> listAuthors = new ArrayList<>(dbAssets.searchByAuthor(searchBar.getText()));

                    list = new ArrayList<>(combineLists(listTitles, listAuthors));
                } else {
                    list = new ArrayList<>(dbAssets.sortAllByTitle());
                }
                System.out.println("after searching titles and authors: " + list.size());

                //redo the table
                assetVerticalGroup.clear();
                Table refreshTable;
                refreshTable = getAssetTable(list);
                assetVerticalGroup.addActor(refreshTable);
            }
        };
    }

    /**
     * Combines 2 lists, with no repeats
     * Can probably be optimized
     * Use: multiple search parameters at the same time
     *
     * @param list1 Asset list to combine
     * @param list2 Asset list
     * @return combined list
     */
    public List<Asset> combineLists(List<Asset> list1, List<Asset> list2) {
        Objects.requireNonNull(list1, "List may not be null");
        Objects.requireNonNull(list2, "List may not be null");

        ArrayList<Asset> combined = new ArrayList<>(list1);

        for (Asset assetToAdd: list2) {
            boolean isInList = false;
            for (Asset assetInList: combined) {
                if (assetToAdd.getAssetID() == assetInList.getAssetID()) {
                    isInList = true;
                }
            }
            if (!isInList) {
                combined.add(assetToAdd);
            }
        }
        return combined;
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