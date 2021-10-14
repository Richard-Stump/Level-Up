//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.mygdx.nextlevel.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.nextlevel.LevelInfo;
import com.mygdx.nextlevel.NextLevel;
import com.mygdx.nextlevel.dbHandlers.CreatedLevelsDB;
import com.mygdx.nextlevel.dbHandlers.DownloadedLevelsDB;
import com.mygdx.nextlevel.enums.Difficulty;
import com.mygdx.nextlevel.enums.Tag;

import java.sql.Date;
import java.sql.SQLException;

public class PostLevelCreationMenuScreen implements Screen {
    private NextLevel game;
    private Skin skin;
    private TextureAtlas atlas;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Stage stage;
    private final String titleText = "Level Information";
    private DownloadedLevelsDB dbDownloaded;
    private CreatedLevelsDB dbCreated;

    public PostLevelCreationMenuScreen(NextLevel game) {
        this.game = game;
        atlas = new TextureAtlas(Gdx.files.internal("skin/neon-ui.atlas"));
        skin = new Skin(Gdx.files.internal("skin/neon-ui.json"), atlas);

        batch = game.batch;
        camera = new OrthographicCamera();
        camera.position.set(camera.viewportWidth / 2.0F, camera.viewportHeight / 2.0F, 0.0F);
        camera.update();

        viewport = new FitViewport(960.0F, 500.0F, camera);
        viewport.apply();

        stage = new Stage(viewport, batch);
    }

    public void show() {
        Gdx.input.setInputProcessor(stage);
        TextButton backButton = new TextButton("Back", skin);

        backButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new EditLevelScreen(game));
            }
        });

        Label title = new Label(titleText, skin);
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.top();
        mainTable.add(backButton).width(75.0F);
        mainTable.add(title).center().expandX();
        TextField titleField = new TextField("", skin);
        Label tagLabel1 = new Label("Tag 1: ", skin);

        final Label tagLabel2 = new Label("Tag 2: ", skin);
        final SelectBox<Tag> tagSelectBox1 = new SelectBox<>(skin);
        final SelectBox<Tag> tagSelectBox2 = new SelectBox<>(skin);

        tagSelectBox1.setItems(Tag.class.getEnumConstants());
        tagSelectBox2.setItems(Tag.class.getEnumConstants());
        tagLabel2.setVisible(false);
        tagSelectBox2.setVisible(false);

        tagSelectBox1.addListener(new EventListener() {
            public boolean handle(Event event) {
                if (tagSelectBox1.getSelected().equals(Tag.NONE)) {
                    tagLabel2.setVisible(false);
                    tagSelectBox2.setVisible(false);
                } else {
                    tagLabel2.setVisible(true);
                    tagSelectBox2.setVisible(true);
                }

                return false;
            }
        });
        Table settingsTable = new Table();
        settingsTable.top();
        settingsTable.add(new Label("Level Title: ", skin));
        settingsTable.add(titleField);
        settingsTable.row();

        settingsTable.add(tagLabel1);
        settingsTable.add(tagSelectBox1);
        settingsTable.row();

        settingsTable.add(tagLabel2);
        settingsTable.add(tagSelectBox2);
        settingsTable.row();

        mainTable.row();
        mainTable.add(settingsTable).colspan(2).center().expand();

        TextButton finishButton = new TextButton("Finish", skin);
        finishButton.addListener(finishButtonListener(titleField, tagSelectBox1, tagSelectBox2));
        mainTable.row();

        mainTable.add(finishButton).colspan(2).center().expandX();
        stage.addActor(mainTable);
    }

    private ClickListener finishButtonListener(final TextField titleField, final SelectBox<Tag> tagSelect1, final SelectBox<Tag> tagSelect2) {
        return new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dbCreated = new CreatedLevelsDB();
                dbDownloaded = new DownloadedLevelsDB();

                //TODO: when users are implemented, replace these fields with the correct fields
                LevelInfo newLevelInfo = new LevelInfo(dbCreated.generateUniqueID("TestUser"), titleField.getText(), "TestUser");
                newLevelInfo.setDateCreated(new Date(System.currentTimeMillis()));
                newLevelInfo.setDateDownloaded(new Date(System.currentTimeMillis()));

                if (!tagSelect1.getSelected().equals(Tag.NONE)) {
                    newLevelInfo.addTag(tagSelect1.getSelected());
                    if (!tagSelect2.getSelected().equals(Tag.NONE)) {
                        newLevelInfo.addTag(tagSelect2.getSelected());
                    }
                }

                dbCreated.addLevelInfo(newLevelInfo);
                dbDownloaded.addLevelInfo(newLevelInfo);

                LevelInfo itemInDatabase = dbCreated.searchByID(newLevelInfo.getId());
                System.out.printf("Success, level %s was created with ID %s\n", itemInDatabase.getTitle(), itemInDatabase.getId());

                try {
                    dbCreated.close();
                    dbDownloaded.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    System.out.println("Could not save data to database, try again");
                    return;
                }

                //TODO: maybe display a popup, or bring the user to the level information page

                game.setScreen(new MainMenuScreen(game));
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
