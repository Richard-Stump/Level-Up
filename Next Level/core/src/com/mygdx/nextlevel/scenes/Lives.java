//package com.mygdx.nextlevel.scenes;
//
//import com.badlogic.gdx.graphics.Color;
//import com.badlogic.gdx.graphics.OrthographicCamera;
//import com.badlogic.gdx.graphics.g2d.BitmapFont;
//import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//import com.badlogic.gdx.scenes.scene2d.Stage;
//import com.badlogic.gdx.scenes.scene2d.ui.Label;
//import com.badlogic.gdx.scenes.scene2d.ui.Table;
//import com.badlogic.gdx.utils.viewport.FitViewport;
//import com.badlogic.gdx.utils.viewport.Viewport;
//import com.mygdx.nextlevel.NextLevel;
//
//
//
//public class Lives {
//    public Stage stage;
//    private Viewport viewport;
//
//    private int lives;
//     Label livesLabel;
//
//    public Lives(SpriteBatch spriteBatch) {
//        lives = 3;
//        viewport = new FitViewport(NextLevel.WIDTH, NextLevel.HEIGHT, new OrthographicCamera());
//        stage = new Stage(viewport, spriteBatch);
//
//        Table table = new Table();
//        table.top();
//        table.setFillParent(true);
//        livesLabel = new Label(String.format("Lives: %d", lives), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
//        table.add(livesLabel).expandX().padTop(10);
//        stage.addActor(table);
//    }
//}
