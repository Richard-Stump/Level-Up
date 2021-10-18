package com.mygdx.nextlevel.hud;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.nextlevel.NextLevel;
import com.mygdx.nextlevel.actors.Player;
import com.mygdx.nextlevel.screens.MainMenuScreen;

public class Hud {

    public Stage stage;
    private Viewport viewport;

    private TextureAtlas atlas;
    protected Skin skin;

    public Integer worldTimer;
    public float time;
    private Integer score;

    Label countdownLabel;
    Label scoreLabel;
    Label timeLabel;
    Label levelLabel;
    Label worldLabel;
    Label playerLabel;
    Label livesLabel;
    Label numLivesLabel;

    TextButton backButton;

    public Hud( SpriteBatch spriteBatch, Player player) {
        atlas = new TextureAtlas("skin/neon-ui.atlas");
        skin = new Skin(Gdx.files.internal("skin/neon-ui.json"), atlas);

        worldTimer = 300;
        time = 0;
        score = 0;

        viewport = new FitViewport(NextLevel.V_WIDTH, NextLevel.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, spriteBatch);

        //create table inside stage
        Table table = new Table();
        table.top();
        table.setFillParent(true); //table is the size of the stage

        countdownLabel = new Label(String.format("%03d", worldTimer), new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        scoreLabel = new Label(String.format("%06d", score), new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        timeLabel = new Label("TIME", new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        levelLabel = new Label("1-1", new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        worldLabel = new Label("WORLD", new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        playerLabel = new Label("PLAYER", new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        livesLabel = new Label("LIVES", new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        numLivesLabel = new Label(String.format("%d", player.getLives()), new Label.LabelStyle(new BitmapFont(), Color.BLACK));

//        backButton = new TextButton("Back", skin);
//        //back button listener
//        backButton.addListener(new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                ((Game)Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen(game));
//            }
//        });
//
//        table.add(backButton).top().left();
//        table.row();

        table.add(livesLabel).expandX().padTop(10);
        table.add(playerLabel).expandX().padTop(10);
        table.add(worldLabel).expandX().padTop(10);
        table.add(timeLabel).expandX().padTop(10);
        table.row();
        table.add(numLivesLabel).expandX();
        table.add(scoreLabel).expandX();
        table.add(levelLabel).expandX();
        table.add(countdownLabel).expandX();


        stage.addActor(table);
    }
    public void update(float delta, Player player) {
        numLivesLabel.setText(String.format("%d", player.getLives()));
    }
}
