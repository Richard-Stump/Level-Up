package com.mygdx.nextlevel.screens.editor;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisWindow;
import com.mygdx.nextlevel.NextLevel;
import com.mygdx.nextlevel.dbHandlers.CreatedLevelsDB;
import com.mygdx.nextlevel.dbHandlers.DownloadedLevelsDB;
import com.mygdx.nextlevel.dbHandlers.ServerDBHandler;
import com.mygdx.nextlevel.enums.Difficulty;
import com.mygdx.nextlevel.enums.Tag;
import com.mygdx.nextlevel.screens.EditLevelScreen;
import com.mygdx.nextlevel.screens.LevelDownloadScreen;
import com.mygdx.nextlevel.screens.MainMenuScreen;

import java.util.ArrayList;

public class CustomAssetWindow implements Screen {
    protected AssetTextureEditTable assetTable;
    protected VisTextButton applyButton;
    protected VisTextButton cancelButton;
    protected Actor previousScrollFocus;
    private SelectBox<String> textureDropdown;
    private Skin skin;
    private TextureAtlas atlas;
    EditLevelScreen screen;
    EditorLevel level;
    NextLevel game;
    SpriteBatch batch;
    Camera camera;
    Viewport viewport;
    Stage stage;
    Label selectedTexture;
    public static final int STAGE_WIDTH = 1920 / 2;
    public static final int STAGE_HEIGHT = 1080 / 2;

    public CustomAssetWindow(NextLevel game, EditLevelScreen screen, final EditorLevel level) {
        this.screen = screen;
        this.level = level;
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

//        dbDownloaded = new DownloadedLevelsDB();
//        dbCreated = new CreatedLevelsDB();
        selectedTexture = new Label("Texture Selected: none", skin);
//        selectedId = "";
    }


    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);

        //start new layout
        mainTable = new Table();
        mainTable.setFillParent(true);
        stage.addActor(mainTable);

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
        Label.LabelStyle titleStyle = skin.get("title-plain", Label.LabelStyle.class);
        Label levelSelectLabel = new Label("Select Level", titleStyle);

        //current user overview
        //HorizontalGroup userInfo = new HorizontalGroup();
        //Label usernameLabel = new Label(LoginScreen.getCurAcc(), skin);
        //userInfo.addActor(usernameLabel);
        //add stuff here for user info

        TextButton buttonDownloadLevels = new TextButton("Download Levels", skin);
        buttonDownloadLevels.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game)Gdx.app.getApplicationListener()).setScreen(new LevelDownloadScreen(game));
            }
        });

        mainTable.add(backButton).height(labelHeight +10).padTop(10).padLeft(5);
        mainTable.add(levelSelectLabel).expandX().left().padLeft(5).padTop(10);
        mainTable.add(buttonDownloadLevels).width(200).padTop(10);
        mainTable.add(new Label("", skin)).width(backButton.getWidth());
        mainTable.row();

        //set up level information section
        Table infoTable = getLevelTable(new ArrayList<>(dbDownloaded.sortByTitle()));
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

        TextButton playButton = new TextButton("Play", skin);
        playButton.addListener(playLevel());

        mainTable.add();
        mainTable.add(selectedLevel).left().padBottom(20).padLeft(5);
        mainTable.add(playButton).width(150).padBottom(20).padLeft(5);

        //end
        mainTable.setFillParent(true);
        stage.addActor(mainTable);
    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

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

    }
}
