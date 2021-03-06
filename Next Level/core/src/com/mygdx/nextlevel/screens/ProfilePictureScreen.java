package com.mygdx.nextlevel.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.nextlevel.NextLevel;
import com.mygdx.nextlevel.Util.ErrorDialog;
import com.mygdx.nextlevel.Util.HoverListener;
import com.mygdx.nextlevel.dbHandlers.ServerDBHandler;

public class ProfilePictureScreen extends LoginScreen implements Screen {
    public SpriteBatch batch;
    public Stage stage;
    public Viewport viewport;
    private OrthographicCamera camera;
    private TextureAtlas atlas;
    protected Skin skin;
    private NextLevel game;

    public String username;

    public static int textBoxBottomPadding = 20;
    public static int buttonWidth = 200;
    public static int rightMargin = 870;
    public static int topMargin = 450;

    public String selectedPic = "";
    ServerDBHandler db;

    public Label selectedPicLabel;


    public ProfilePictureScreen (NextLevel game) {
        atlas = new TextureAtlas("skin/uiskin.atlas");
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"), atlas);

        db = new ServerDBHandler();

        batch = game.batch;
        camera = new OrthographicCamera();
        viewport = new FitViewport(960, 500, camera);
        viewport.apply();

        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

        stage = new Stage(viewport, batch);
        this.game = game;
        this.username = LoginScreen.getCurAcc();
    }
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
        Table mainTable = new Table();

        Label.LabelStyle titleStyle = skin.get("title-plain", Label.LabelStyle.class);
        Label title = new Label("Profile Picture Selection", titleStyle);

        Image pic1 = new Image(new Texture(Gdx.files.internal("userIcon.png")));
//        pic1.setSize(1f, 1f);
        Image pic2 = new Image(new Texture(Gdx.files.internal("hero.png")));
//        pic2.setSize(1f, 1f);
        Image pic3 = new Image(new Texture(Gdx.files.internal("fire-hero.png")));
//        pic3.setSize(1f, 1f);
        Image pic4 = new Image(new Texture(Gdx.files.internal("life-steal-hero.png")));
//        pic4.setSize(1f, 1f);
        Image pic5 = new Image(new Texture(Gdx.files.internal("star-hero.png")));
//        pic5.setSize(1f, 1f);
        Image pic6 = new Image(new Texture(Gdx.files.internal("enemy.png")));
//        pic6.setSize(1f, 1f);
        Image pic7 = new Image(new Texture(Gdx.files.internal("enemy_jump.png")));
//        pic7.setSize(1f, 1f);
        Image pic8 = new Image(new Texture(Gdx.files.internal("enemy_shoot.png")));
//        pic8.setSize(1f, 1f);

        //pic1.scaleBy(-.5f);

        pic1.scaleBy(-.2f);
        pic2.scaleBy(-.2f);
        pic3.scaleBy(-.2f);
        pic4.scaleBy(-.2f);
        pic5.scaleBy(-.2f);
        pic6.scaleBy(-.2f);
        pic7.scaleBy(-.2f);
        pic8.scaleBy(-.2f);

        TextButton applyButton = new TextButton("Apply", skin);
        TextButton backButton = new TextButton("Back", skin);

        selectedPicLabel = new Label("Selected Profile Pic: ----", skin);

        //event listeners
        backButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new ProfileMainMenu(game));
            }
        });
        backButton.addListener(new HoverListener());

        pic1.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                selectedPic = "userIcon.png";
                selectedPicLabel.setText("Selected Profile Pic: Basic User Icon");
            }
        });
        pic1.addListener(new HoverListener());

        pic2.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                selectedPic = "hero.png";
                selectedPicLabel.setText("Selected Profile Pic: Hero");
            }
        });
        pic2.addListener(new HoverListener());

        pic3.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                selectedPic = "fire-hero.png";
                selectedPicLabel.setText("Selected Profile Pic: Fire Hero");
            }
        });
        pic3.addListener(new HoverListener());

        pic4.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                selectedPic = "life-steal-hero.png";
                selectedPicLabel.setText("Selected Profile Pic: Life Steal Hero");
            }
        });
        pic4.addListener(new HoverListener());

        pic5.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                selectedPic = "star-hero.png";
                selectedPicLabel.setText("Selected Profile Pic: Star Hero");
            }
        });
        pic5.addListener(new HoverListener());

        pic6.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                selectedPic = "enemy.png";
                selectedPicLabel.setText("Selected Profile Pic: Enemy");
            }
        });
        pic6.addListener(new HoverListener());

        pic7.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                selectedPic = "enemy_jump.png";
                selectedPicLabel.setText("Selected Profile Pic: Jumping Enemy");
            }
        });
        pic7.addListener(new HoverListener());

        pic8.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                selectedPic = "enemy_shoot.png";
                selectedPicLabel.setText("Selected Profile Pic: Fire Enemy");
            }
        });
        pic8.addListener(new HoverListener());

        applyButton.addListener(setProfilePicListener());
        applyButton.addListener(new HoverListener());

        Table header = new Table();
        //header.setDebug(true);
        header.add(backButton).left();
        header.add(title);

        //adding to main table
        //mainTable.setDebug(true);
        mainTable.add(backButton).left().padTop(15).width(70).padLeft(15);
        mainTable.add(title).colspan(3).expandX().padTop(15);
        mainTable.add(new Label("", skin)).width(backButton.getWidth());
//        mainTable.add(header).expandX().colspan(3);
        mainTable.row();
        mainTable.add(new Label("", skin));
        mainTable.add(pic1).padBottom(10);
        mainTable.add(pic2).padBottom(10);
        mainTable.add(pic3).padBottom(10);
        //mainTable.add(new Label("", skin));
        mainTable.row();
        mainTable.add(new Label("", skin));
        mainTable.add(pic4).padBottom(10);
        mainTable.add(pic5).padBottom(10);
        mainTable.add(pic6).padBottom(10);
        mainTable.row();
        mainTable.add(new Label("", skin));
        mainTable.add(pic7).padBottom(10);
        mainTable.add(pic8).padBottom(10);
        mainTable.row();
        mainTable.add(new Label("", skin));
        mainTable.add(selectedPicLabel).width(200);
        mainTable.add(new Label("", skin));
        mainTable.add(applyButton).expandY().padBottom(20).width(buttonWidth);

        mainTable.setFillParent(true);
        stage.addActor(mainTable);
    }

    private ClickListener setProfilePicListener() {
        return  new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                db.setProfilePic(username, selectedPic);
                ErrorDialog message = new ErrorDialog("Profile Picture",
                        "ProfileMainMenu", game, skin,
                        "Profile picture successfully changed!", stage);
                //game.setScreen(new ProfileMainMenu(game));
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
}
