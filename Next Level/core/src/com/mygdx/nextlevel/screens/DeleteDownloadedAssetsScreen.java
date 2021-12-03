package com.mygdx.nextlevel.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
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

public class DeleteDownloadedAssetsScreen implements Screen {
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

    //search parameters:
    private TextField searchBar;
    private ScrollPane scrollPane;

    //left column
    public Label assetName;
    public Label assetAuthor;

    public Label assetsLabel;

    //static vars
    public static int rightColumnWidth = 150;
    public static int topBottomPad = 30;
    public static int leftColumnWidth = 350;
    public static int labelHeight = 25;

    private boolean serverDelete;
    private ArrayList<String> downloadedAssets;
    private ArrayList<String> imagePreviews;

    public DeleteDownloadedAssetsScreen(NextLevel game, boolean serverDelete) {
        this.serverDelete = serverDelete;
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

        downloadedAssets = new ArrayList<>();
        imagePreviews = new ArrayList<>();
        for(Asset asset: dbAssets.sortAllByTitle()) {
            if (serverDelete) {
                if (asset.author.equals(LoginScreen.getCurAcc())) {
                    if (dbAssets.existsLocally(asset.getAssetID())) {
                        downloadedAssets.add(asset.getAssetID());
                    }
                    imagePreviews.add(asset.getAssetID());
                    dbAssets.downloadAsset(asset.getAssetID());
                }
            }
        }
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
                cleanImages();
                if (serverDelete) {
                    game.setScreen(new ProfileMainMenu(game));
                } else {
                    game.setScreen(new AssetDownloadScreen(game));
                }
            }
        });

        //screen title
        if (serverDelete) {
            assetsLabel = new Label("Your assets:", skin);
        } else {
            assetsLabel = new Label("Your downloaded assets:", skin);
        }

        mainTable.add(backButton).height(labelHeight +10).padTop(10).padLeft(5);
        mainTable.add(assetsLabel).padTop(10).expandX().left().padLeft(5).height(labelHeight);
        //mainTable.add(hgButtons).width(200);
        mainTable.add(new Label("", skin)).width(backButton.getWidth());
        mainTable.add(new Label("", skin)).width(backButton.getWidth());
        mainTable.row();

        ArrayList<Asset> assetList = new ArrayList<>(dbAssets.sortAllByTitle());
        ArrayList<Asset> copyList = new ArrayList<>(assetList);
        for (Asset asset: copyList) {
            if (!downloadedAssets.contains(asset.getAssetID())) {
                assetList.remove(asset);
            }
        }

        //set up asset information section
        Table infoTable = getAssetTable(assetList);
        assetVerticalGroup = new VerticalGroup();
        assetVerticalGroup.addActor(infoTable);
        assetVerticalGroup.padRight(50);

        assetVerticalGroup.padBottom(100);
        assetVerticalGroup.top();

        scrollPane = new ScrollPane(assetVerticalGroup, skin);
        scrollPane.setForceScroll(false, true);

        //make the sorting and search thing on right side
        Table searchSortGroup = getSearchSortTable();

        mainTable.add();
        mainTable.add(scrollPane).expandY();
        mainTable.add(searchSortGroup).top().padLeft(5);
        mainTable.row();

        //row 3: empty placeholder, currently selected asset, play button

        mainTable.add();
        mainTable.add(selectedAsset).left().padBottom(20).padLeft(5);

        selectedId = "";
        selectedAsset.setText("Select an asset");
        assetVerticalGroup.clear();
        assetVerticalGroup.addActor(getRefreshedAssetList());

        //end
        mainTable.setFillParent(true);
        stage.addActor(mainTable);

        //System.out.println(activeDB);
    }

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

            TextButton deleteButton = new TextButton("Delete", skin);
            deleteButton.addListener(deleteAssetListener(id));
            deleteButton.addListener(new HoverListener());
            infoTable.add(deleteButton).width(80).padBottom(15);

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

        //verify database is connected
        if (dbAssets == null) {
            System.out.println("db is not active");
            return null;
        }

        //have an image preview
        FileHandle fileHandle = new FileHandle(id);
        Texture texture = new Texture(fileHandle);
        Image preview = new Image(texture);
        preview.addListener(selectAssetListener(id));

        leftTable.add(preview).width(64).height(64).padRight(25);

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

        //adding left column labels
        assetName = new Label(asset.name, skin);
        assetAuthor = new Label(asset.author, skin);

        assetName.addListener(selectAssetListener(id));
        assetName.addListener(new HoverListener());
        assetAuthor.addListener(selectAssetListener(id));
        assetAuthor.addListener(new HoverListener());

        //adding to left table
        rightTable.add(assetName).width(leftColumnWidth - 10).left().height(labelHeight);
        rightTable.row();
        rightTable.add(assetAuthor).width(leftColumnWidth - 10).left().height(labelHeight);

        return rightTable;
    }

    private void cleanImages() {
        for (String id: imagePreviews) {
            if (!downloadedAssets.contains(id)) {
                dbAssets.removeAssetLocal(id);
            }
        }
        imagePreviews = new ArrayList<>();
    }

    private ClickListener selectAssetListener(final String id) {
        return new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //outline the selected asset
                String assetSelectedStr = dbAssets.searchByID(id).name;
                selectedAsset.setText("Asset Selected: " + assetSelectedStr);
                selectedId = id;
            }
        };
    }

    private Stage delStage;
    private boolean isDeleting = false;

    private ClickListener deleteAssetListener(final String id) {
        return new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //outline the selected asset
                selectedAsset.setText("Asset Selected: " + dbAssets.searchByID(id).name);

                selectedId = id;
                isDeleting = true;

                //make the user confirm their decision:
                Dialog dialog = new Dialog("Delete Asset", skin) {
                    @Override
                    protected void result(Object object) {
                        System.out.println("result: " + object);
                        Gdx.input.setInputProcessor(stage);

                        if ((Boolean) object) {
                            if (serverDelete) {
                                dbAssets.removeAssetServer(id);
                                downloadedAssets.remove(id);
                                imagePreviews.remove(id);
                            } else {
                                dbAssets.removeAssetLocal(id);
                                downloadedAssets.remove(id);
                            }
                            assetVerticalGroup.clear();
                            assetVerticalGroup.addActor(getRefreshedAssetList());
                        }
                    }
                };

                if (serverDelete) {
                    dialog.text("Are you sure you want to delete " + dbAssets.searchByID(id).name + " from the server?");
                } else {
                    dialog.text("Are you sure you want to delete " + dbAssets.searchByID(id).name + " locally?");
                }

                dialog.button("Delete", true);
                dialog.button("Cancel", false);

                //OrthographicCamera delCamera = new OrthographicCamera(500, 500);
                //delCamera.position.set(delCamera.viewportWidth / 2.0F, delCamera.viewportHeight / 2.0F, 0.0F);
                //delCamera.update();

                //Viewport delViewport = new StretchViewport(500, 500, delCamera);
                //delViewport.apply();

                //delStage = new Stage(viewport, batch);

                //Gdx.input.setInputProcessor(delStage);

                //delStage.addActor(dialog);

                dialog.show(stage);
            }
        };
    }

    private ClickListener searchButton() {
        return new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                selectedId = "";
                selectedAsset.setText("Select an asset");

                assetVerticalGroup.clear();
                assetVerticalGroup.addActor(getRefreshedAssetList());
            }
        };
    }

    private Table getRefreshedAssetList() {
        //search all assets and make a list that contains this string in the title or author:
        ArrayList<Asset> ongoingList;

        if (!searchBar.getText().equals("")) {
            ArrayList<Asset> listTitles = new ArrayList<>(dbAssets.searchByName(searchBar.getText()));
            ArrayList<Asset> listAuthors = new ArrayList<>(dbAssets.searchByAuthor(searchBar.getText()));

            ongoingList = new ArrayList<>(combineLists(listTitles, listAuthors));
        } else {
            ongoingList = new ArrayList<>(dbAssets.sortAllByTitle());
        }

        //check if we have the file already
        ArrayList<Asset> finalList = new ArrayList<>();
        for (Asset asset: ongoingList) {
            File f = new File(asset.getAssetID());
            if (f.exists()) {
                finalList.add(asset);
            }
        }

        System.out.println("after searching titles and authors: " + finalList.size());

        Table refreshTable;
        refreshTable = getAssetTable(finalList);
        return refreshTable;
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
                if (assetToAdd.getAssetID().equals(assetInList.getAssetID())) {
                    isInList = true;
                    break;
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

        /*
        if (isDeleting) {
            delStage.act();
            delStage.draw();
        }

         */
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
