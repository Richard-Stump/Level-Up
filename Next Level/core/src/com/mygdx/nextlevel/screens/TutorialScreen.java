package com.mygdx.nextlevel.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.nextlevel.NextLevel;

public class TutorialScreen implements Screen {

    public Stage stage;
    public SpriteBatch batch;
    public Viewport viewport;
    private OrthographicCamera camera;
    private TextureAtlas atlas;
    protected Skin skin;
    private NextLevel game;

    public TutorialScreen (NextLevel game) {
        this.game = game;
        atlas = new TextureAtlas("skin/neon-ui.atlas");
        skin = new Skin(Gdx.files.internal("skin/neon-ui.json"), atlas);

        batch = game.batch;
        camera = new OrthographicCamera();
        viewport = new FitViewport(960, 500, camera);
        viewport.apply();

        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

        stage = new Stage(viewport, batch);
    }

    @Override
    public void show() {
        //Stage should control input:
        Gdx.input.setInputProcessor(stage);

        //Create Table
        //Table container = new Table();
        Table table = new Table();
        //Set table to fill stage
        table.setFillParent(true);
        //Set alignment of contents in the table.
        table.top();

        //ScrollPane scrollPane = new ScrollPane(table, skin);
        //container.add(scrollPane).expandY();

        // Back button          Tutorial
        //
        //        /  ↑  /
        // /  ←  /  ↓  /  →  /        Move by using arrow keys
        //
        //

        //Label for top of page
        Label title = new Label("Tutorial", skin);

        //back button
        TextButton backButton = new TextButton("Back", skin);

        //back button listener
        backButton.addListener(new ClickListener() {
           @Override
           public void clicked(InputEvent event, float x, float y) {
               ((Game)Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen(game, MainMenuScreen.username));
           }
        });

        //movement information
        Image arrowImage = new Image(new Texture(Gdx.files.internal("arrowkeys.png")));
        Label movement = new Label("Move the character using LEFT and Right\n arrow keys and UP to jump.", skin);

        //killing enemies information
        Image enemy = new Image(new Texture(Gdx.files.internal("goomba.png")));
        Label killEnemy = new Label("Kill enemies by jumping on top of them.", skin);

        //powerups information
        Image powerup = new Image(new Texture(Gdx.files.internal("mushroom.jpeg")));
        Label powerupGain = new Label("Collect items for a power up.", skin);

        //finishing a level
        Image flag = new Image(new Texture(Gdx.files.internal("flag.png")));
        Label finishInfo = new Label("Complete a level by reaching the finish flag.", skin);

        //table organization
        table.add(backButton).top().width(75);
        table.add(title).center().expandX().padRight(50);

        //2nd row
        table.row();
        table.add(arrowImage).size(100, 60).padLeft(100).padTop(50);
        table.add(movement).center().expandX().padTop(50);

        //3rd row
        table.row();
        table.add(enemy).size(50, 50).padLeft(100).padTop(50);
        table.add(killEnemy).center().expandX().padTop(50);

        //4th row
        table.row();
        table.add(powerup).size(50, 50).padLeft(100).padTop(50);
        table.add(powerupGain).center().expandX().padTop(50);

        //5th row
        table.row();
        table.add(flag).size(30, 70).padLeft(100).padTop(50);
        table.add(finishInfo).center().expandX().padTop(50);


        //Add table to stage
        stage.addActor(table);
        //stage.addActor(container);
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

    }
}
