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
import com.mygdx.nextlevel.TileMap;
import com.mygdx.nextlevel.actors.Item;
import com.mygdx.nextlevel.actors.Item2;
import com.mygdx.nextlevel.actors.Player;
import com.mygdx.nextlevel.actors.Player2;
import com.mygdx.nextlevel.screens.ErrorMessageScreen;
import com.mygdx.nextlevel.screens.GameScreen2;

import java.util.ArrayList;
import java.util.HashMap;

public class Hud2 {
    public Stage stage;
    private Viewport viewport;

    private TextureAtlas atlas;
    protected Skin skin;

    public float worldTimer;
    public float time;
    public long score;

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
    Label jewelLabel;
    Image jewelImg;
    String tileMapName;
    TileMap tm;
    private int condition = -1;
    ArrayList<Integer> conditionList;
    GameScreen2 screen;

    TextureRegionDrawable jewelDrawable;
    TextureRegionDrawable xDrawable;
    HashMap<String, TextureRegionDrawable> itemDrawables;


    public Hud2(GameScreen2 screen, SpriteBatch spriteBatch, Player2 player, String levelInfo) {
        atlas = new TextureAtlas("skin/uiskin.atlas");
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"), atlas);
        this.screen = screen;
        tm = screen.getTileMap();
        worldTimer = tm.getTimeLimit();
        if (worldTimer <= 0) {
            worldTimer = 300;
        }
        time = 0;
        score = 0;
//        condition = player.getCondition();
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
        stage = new Stage(viewport, spriteBatch);

        //create table inside stage
        Table table = new Table();
        table.top();
        table.setFillParent(true); //table is the size of the stage

        countdownLabel = new Label(String.format("%.00f", worldTimer), new Label.LabelStyle(new BitmapFont(), Color.BLACK));
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
        numEnemyLabel = new Label(String.format("%d/%d", player.getEnemiesKilled(), 1), new Label.LabelStyle(new BitmapFont(), Color.BLACK));

        killNoEnemy = new Label(String.format("%d", player.getEnemiesKilled()), new Label.LabelStyle(new BitmapFont(), Color.BLACK));

        jewelLabel = new Label("JEWEL", new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        jewelImg = new Image(new Texture("x.png"));
        conditionList = tm.getConditionList();

        jewelDrawable = new TextureRegionDrawable(new Texture("jewel.png"));
        xDrawable = new TextureRegionDrawable(new Texture("x.png"));

        itemDrawables = new HashMap<>();

        table.add(livesLabel).expandX().padTop(10);
        table.add(playerLabel).expandX().padTop(10);
        table.add(worldLabel).expandX().padTop(10);
        if (conditionList.contains(5)) {
            table.add(timeLabel).expandX().padTop(10);
        }
        table.add(itemLabel).expandX().padTop(10);
        if (conditionList.contains(1)) {
            table.add(coinLabel).expandX().padTop(10);
        }
        if (conditionList.contains(2) || conditionList.contains(3)) {
            table.add(enemyLabel).expandX().padTop(10);
        }
        if (conditionList.contains(4)) {
            table.add(jewelLabel).expandX().padTop(10);
        }
//        if (player.getCondition() == 1) {
//            table.add(coinLabel).expandX().padTop(10);
//        } else if (player.getCondition() == 2 || player.getCondition() == 3) {
//            table.add(enemyLabel).expandX().padTop(10);
//        } else if (player.getCondition() == 4) {
//            table.add(jewelLabel).expandX().padTop(10);
//        }
//        if (player.getCondition() == 1 && player.getCondition2() == 2) {
//            table.add(coinLabel).expandX().padTop(10);
//            table.add(enemyLabel).expandX().padTop(10);
//        }
        table.row();
        table.add(numLivesLabel).expandX();
        table.add(scoreLabel).expandX();
        table.add(levelLabel).expandX();
        if (conditionList.contains(5)) {
            table.add(countdownLabel).expandX();
        }
        table.add(itemImg).width(25).height(25).expandX();
        if (conditionList.contains(1)) {
            table.add(numCoinLabel).expandX();
        }
        if (conditionList.contains(2)) {
            table.add(numEnemyLabel).expandX();
        }
        if (conditionList.contains(3)) {
            table.add(killNoEnemy).expandX();
        }
        if (conditionList.contains(4)) {
            table.add(jewelImg).width(25).height(25).expandX();
        }

        stage.addActor(table);
    }

    public void render(Batch batch) {
        batch.end();
        stage.draw();
        batch.begin();
    }

    public void update(float delta, Player2 player, HashMap<Item2, String> map) {
        scoreLabel.setText(String.format("%d", player.getScore()));
        if (conditionList.contains(5)) {
            time += delta;
            if (time >= 1) {
                worldTimer--;
                countdownLabel.setText(String.format("%.00f", worldTimer));
                time = 0;
                if (worldTimer == 0 && !player.getWin() && player.getConditions().contains(5)) {
                    player.setFail(true);
                }
            }
        }
        numLivesLabel.setText(String.format("%d", player.getLives()));
        if (conditionList.contains(1)) {
            numCoinLabel.setText(String.format("%d/%d", player.getCoins(), tm.getCoinCount()));
        }
        if (conditionList.contains(2)) {
            numEnemyLabel.setText(String.format("%d/%d", player.getEnemiesKilled(), tm.getEnemyCount()));
        }
        if (conditionList.contains(3)) {
            killNoEnemy.setText(String.format("%d", player.getEnemiesKilled()));
        }
        if (conditionList.contains(4)) {
            if (player.getJewel()) {
                jewelImg.setDrawable(jewelDrawable);
            } else {
                jewelImg.setDrawable(xDrawable);
            }
        }
        if (player.getHeldItem() != null) {
            String itemName = map.get(player.getHeldItem());

            TextureRegionDrawable drawable = itemDrawables.get(itemName);

            if(drawable == null) {
                drawable = new TextureRegionDrawable(new Texture(map.get(player.getHeldItem())));
                itemDrawables.put(itemName, drawable);
            }

            itemImg.setDrawable(drawable);
        } else {
            itemImg.setDrawable(xDrawable);
        }
    }

    public float getTime() {
        return worldTimer;
    }

    public long getScore() {
        return score;
    }
}
