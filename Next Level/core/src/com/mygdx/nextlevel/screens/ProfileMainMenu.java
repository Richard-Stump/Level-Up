package com.mygdx.nextlevel.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.nextlevel.Asset;
import com.mygdx.nextlevel.NextLevel;
import com.mygdx.nextlevel.Util.HoverListener;
import com.mygdx.nextlevel.dbHandlers.AssetHandler;
import com.mygdx.nextlevel.dbHandlers.ServerDBHandler;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class ProfileMainMenu extends LoginScreen implements Screen {
    public SpriteBatch batch;
    public Stage stage;
    public Viewport viewport;
    private OrthographicCamera camera;
    private TextureAtlas atlas;
    protected Skin skin;
    private NextLevel game;

    //player information
    public String username;
    public String profilePic;

    //static vars
    public static int bottomPadding = 20;
    public static int buttonWidth = 300;
    public static int rightMargin = 870;
    public static int topMargin = 450;
    ServerDBHandler db = new ServerDBHandler();

    public ProfileMainMenu (NextLevel game) {
        atlas = new TextureAtlas("skin/uiskin.atlas");
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"), atlas);

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
        //Stage should control input:
        Gdx.input.setInputProcessor(stage);
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);

        Table mainTable = new Table();

        Label.LabelStyle titleStyle = skin.get("title-plain", Label.LabelStyle.class);

        Label title = new Label("User Settings", titleStyle);
        TextButton backButton = new TextButton("Back", skin);

        profilePic = db.getProfilePic(username);
        Image playerPic = new Image(new Texture(Gdx.files.internal(profilePic)));
        //TODO: database crashes, string is not being saved.
//        Image playerPic = new Image(new Texture(Gdx.files.internal(profilePic)));

        System.out.println("current pic: " + profilePic);

        playerPic.scaleBy(.02f);

        Label usernameLabel = new Label(username, skin);

        TextButton changeProfilePicButton = new TextButton("Change Profile Picture", skin);
        TextButton changePasswordButton = new TextButton("Change Password", skin);
        TextButton deleteLevelsButton = new TextButton("Delete Levels", skin);
        TextButton uploadAssetButton = new TextButton("Upload Asset", skin);
        TextButton removeAssetServerButton = new TextButton("Remove Asset from Server", skin);

        //TODO: rewire the buttons
        backButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }
        });
        backButton.addListener(new HoverListener());
        changePasswordButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game)Gdx.app.getApplicationListener()).setScreen(new ChangePasswordScreen(game));
            }
        });
        changePasswordButton.addListener(new HoverListener());
        deleteLevelsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game)Gdx.app.getApplicationListener()).setScreen(new UserAccountScreen(game));
            }
        });
        deleteLevelsButton.addListener(new HoverListener());
        changeProfilePicButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game)Gdx.app.getApplicationListener()).setScreen(new ProfilePictureScreen(game));
            }
        });
        changeProfilePicButton.addListener(new HoverListener());
        uploadAssetButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                FileDialog fd = new FileDialog(new JFrame(), "Choose an asset", FileDialog.LOAD);
                fd.setDirectory("C:\\");
                //TODO: figure out how to filter the correct files
                fd.setFilenameFilter(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        String ext = name.substring(name.indexOf('.'));
                        if (ext.equals(".jpg") || ext.equals(".jpeg") || ext.equals(".png")) {
                            return true;
                        }
                        return false;
                    }
                });
                fd.setVisible(true);
                String filename = fd.getFile();
                String dir = fd.getDirectory();
                if (filename == null) {
                    System.out.println("Canceled");
                    return;
                } else {
                    System.out.printf("File '%s' was selected from directory %s\n", filename, dir);
                }

                TextField nameTF = new TextField("", skin);

                Dialog dialog = new Dialog("Name", skin, "dialog") {
                    @Override
                    protected void result(Object object) {
                        System.out.println("result: " + object);

                        if ((Boolean) object) {
                            AssetHandler assetHandler = new AssetHandler();
                            Asset asset = new Asset(nameTF.getText(), LoginScreen.getCurAcc());
                            asset.setAssetID(assetHandler.generateAssetID(asset));

                            //add the extension to the assetID
                            String extension = filename.substring(filename.indexOf('.'));
                            asset.setAssetID(asset.getAssetID() + extension);

                            //old asset file
                            File fAsset = new File(dir + filename);

                            try {
                                FileInputStream fis = new FileInputStream(fAsset);

                                //copy the asset to a new file in the assets folder using its assetid
                                FileHandle file = Gdx.files.local(asset.getAssetID());

                                file.write(fis, false);
                                fis.close();
                                System.out.printf("asset ID: %s\n", asset.getAssetID());

                                assetHandler.uploadAsset(asset);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                };

                Table table = dialog.getContentTable();
                table.row();
                table.add(new Label("What do you want to name this asset?", skin));
                table.row();
                nameTF.setMessageText("Name the Asset");
                table.add(nameTF);

                dialog.button("Confirm", true);
                dialog.button("Cancel", false);
                dialog.key(Input.Keys.ENTER, true);
                dialog.show(stage);
            }
        });
        uploadAssetButton.addListener(new HoverListener());

        removeAssetServerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game)Gdx.app.getApplicationListener()).setScreen(new DeleteDownloadedAssetsScreen(game, true));
            }
        });
        removeAssetServerButton.addListener(new HoverListener());


        //vertical groups
//        VerticalGroup profileGroup = new VerticalGroup();
//        profileGroup.addActor(playerPic);
//        profileGroup.addActor(usernameLabel);

        Stack mainStack = new Stack();
        mainStack.add(new Image(new Texture(Gdx.files.internal("rect.png"))));

        Table profileTable = new Table();
        //profileTable.setDebug(true);
        profileTable.add(playerPic).padBottom(bottomPadding).padTop(bottomPadding);
        profileTable.row();
        profileTable.add(usernameLabel).padBottom(bottomPadding);
        mainStack.add(profileTable);

//        VerticalGroup buttonGroup = new VerticalGroup();
//        buttonGroup.addActor(changeProfilePicButton);
//        buttonGroup.addActor(changePasswordButton);
//        buttonGroup.addActor(deleteLevelsButton);

        Table buttonTable = new Table();
        buttonTable.add(changeProfilePicButton).width(buttonWidth).padBottom(bottomPadding);
        buttonTable.row();
        buttonTable.add(changePasswordButton).width(buttonWidth).padBottom(bottomPadding);
        buttonTable.row();
        buttonTable.add(deleteLevelsButton).width(buttonWidth).padBottom(bottomPadding);
        buttonTable.row();
        buttonTable.add(uploadAssetButton).width(buttonWidth).padBottom(bottomPadding);
        buttonTable.row();
        buttonTable.add(removeAssetServerButton).width(buttonWidth).padBottom(bottomPadding);

//        Table headerTable = new Table();
//        //headerTable.setDebug(true);
//        headerTable.add(backButton).left().padRight(20);
//        headerTable.add(title).width(830);

        //adding to main table
        //mainTable.setDebug(true);
        mainTable.add(backButton).left().padLeft(15).padTop(15).width(70);
        mainTable.add(title).padTop(15).left().padLeft(10);
        //mainTable.add(headerTable).padTop(15).colspan(2).expandX();
        mainTable.row();
        mainTable.add(new Label("", skin));
        mainTable.add(mainStack).expandY().width(350);
        mainTable.add(buttonTable).expandY().expandX().padRight(100).padBottom(50);

//        mainTable.add(playerPic).expandY();
//        mainTable.add(usernameLabel);
//        mainTable.row();
//        mainTable.add(changeProfilePicButton);
//        mainTable.row();
//        mainTable.add(changePasswordButton);
//        mainTable.row();
//        mainTable.add(deleteLevelsButton);

        //Set table to fill stage
        mainTable.setFillParent(true);

        //Add table to stage
        stage.addActor(mainTable);
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
