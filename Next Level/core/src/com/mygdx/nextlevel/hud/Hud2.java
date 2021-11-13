package com.mygdx.nextlevel.hud;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.nextlevel.NextLevel;
import com.mygdx.nextlevel.actors.Item;
import com.mygdx.nextlevel.actors.Item2;
import com.mygdx.nextlevel.actors.Player;
import com.mygdx.nextlevel.actors.Player2;
import com.mygdx.nextlevel.screens.ErrorMessageScreen;

import java.util.HashMap;

public class Hud2 {
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
    Label itemLabel;
    Label coinLabel;
    Label numCoinLabel;
    Label enemyLabel;
    Label numEnemyLabel;
    Label killNoEnemy;
    Image itemImg;
    private int condition = -1;

    public Hud2(SpriteBatch spriteBatch, Player2 player) {
        atlas = new TextureAtlas("skin/uiskin.atlas");
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"), atlas);

        worldTimer = 300;
        time = 0;
        score = 0;
        condition = player.getCondition();
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
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
        itemLabel = new Label("ITEM", new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        itemImg = new Image(new Texture("x.png"));

        coinLabel = new Label("COINS", new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        numCoinLabel = new Label(String.format("%d/%d", player.getCoins(), 4), new Label.LabelStyle(new BitmapFont(), Color.BLACK));

        enemyLabel = new Label("ENEMIES", new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        numEnemyLabel = new Label(String.format("%d/%d", player.getEnemiesKilled(), 2), new Label.LabelStyle(new BitmapFont(), Color.BLACK));

        killNoEnemy = new Label(String.format("%d", player.getEnemiesKilled()), new Label.LabelStyle(new BitmapFont(), Color.BLACK));



        table.add(livesLabel).expandX().padTop(10);
        table.add(playerLabel).expandX().padTop(10);
        table.add(worldLabel).expandX().padTop(10);
        table.add(timeLabel).expandX().padTop(10);
        table.add(itemLabel).expandX().padTop(10);
        if (player.getCondition() == 1) {
            table.add(coinLabel).expandX().padTop(10);
        } else if (player.getCondition() == 2 || player.getCondition() == 3) {
            table.add(enemyLabel).expandX().padTop(10);
        }
        table.row();
        table.add(numLivesLabel).expandX();
        table.add(scoreLabel).expandX();
        table.add(levelLabel).expandX();
        table.add(countdownLabel).expandX();
        table.add(itemImg).width(25).height(25).expandX();
        if (player.getCondition() == 1) {
            table.add(numCoinLabel).expandX();
        } else if (player.getCondition() == 2) {
            table.add(numEnemyLabel).expandX();
        } else if (player.getCondition() == 3) {
            table.add(killNoEnemy).expandX();
        }

        stage.addActor(table);
    }

    public void render(Batch batch) {
        batch.end();
        stage.draw();
        batch.begin();
    }

    public void update(float delta, Player2 player, HashMap<Item2, String> map) {
        time += delta;
        if (time >= 1) {
            worldTimer--;
            countdownLabel.setText(String.format("%03d", worldTimer));
            time = 0;
            if (worldTimer == 0 && !player.getWin()) {
                player.setFail(true);
            }
        }
        numLivesLabel.setText(String.format("%d", player.getLives()));
        if (player.getCondition() == 1) {
            numCoinLabel.setText(String.format("%d/%d", player.getCoins(), 5));
        } else if (player.getCondition() == 2) {
            numEnemyLabel.setText(String.format("%d/%d", player.getEnemiesKilled(), 2));
        } else if (player.getCondition() == 3) {
            killNoEnemy.setText(String.format("%d", player.getEnemiesKilled()));
        }
        if (player.getHeldItem() != null) {
            itemImg.setDrawable(new TextureRegionDrawable(new Texture(map.get(player.getHeldItem()))));
        } else {
            itemImg.setDrawable(new TextureRegionDrawable(new Texture("x.png")));
        }
    }
}
