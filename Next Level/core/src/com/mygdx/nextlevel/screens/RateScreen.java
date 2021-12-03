package com.mygdx.nextlevel.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
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

public class RateScreen implements Screen {
    public SpriteBatch batch;
    public Stage stage;
    public Viewport viewport;
    private OrthographicCamera camera;
    private TextureAtlas atlas;
    protected Skin skin;
    private NextLevel game;
    private String levelid;

    public String lastButtonClicked = "";

    public static int buttonWidth = 50;
    public static int buttonBottomPad = 10;
    ServerDBHandler db;

    TextButton enjoymentButtons[] = new TextButton[10];
    public int enjoymentButtonSelected = -1;

    TextButton difficultyButtons[] = new TextButton[10];
    public int difficultyButtonSelected = -1;

    public RateScreen(NextLevel game) {
        db = new ServerDBHandler();
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

//        int j = 0;
//        for (double i = 0; i < 5; i = i + 0.5) {
//            int j = (int) i;
//            enjoymentButtons[j] = new TextButton("" + i + " Stars", skin);
//            enjoymentButtons[j].addListener(rateListener((float) i));
//            enjoymentButtons[j].addListener(new HoverListener());
//        }
    }

    @Override
    public void show() {
        //Stage should control input:
        Gdx.input.setInputProcessor(stage);
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);

        Label.LabelStyle titleStyle = skin.get("title-plain", Label.LabelStyle.class);
        Label titleLabel = new Label("Rate This Level", titleStyle);

//        TextButton starHalf = new TextButton("0.5 Stars", skin);
//        TextButton star1 = new TextButton("1 Star", skin);
//        TextButton star1half = new TextButton("1.5 Stars", skin);
//        TextButton star2 = new TextButton("2 Stars", skin);
//        TextButton star2half = new TextButton("2.5 Stars", skin);
//        TextButton star3 = new TextButton("3 Stars", skin);
//        TextButton star3half = new TextButton("3.5 Stars", skin);
//        TextButton star4 = new TextButton("4 Stars", skin);
//        TextButton star4half = new TextButton("4.5 Stars", skin);
//        TextButton star5 = new TextButton("5 Stars", skin);

//        starHalf.addListener(rateListener(0.5f));
//        star1.addListener(rateListener(1));
//        star1half.addListener(rateListener(1.5f));
//        star2.addListener(rateListener(2));
//        star2half.addListener(rateListener(2.5f));
//        star3.addListener(rateListener(3));
//        star3half.addListener(rateListener(3.5f));
//        star4.addListener(rateListener(4));
//        star4half.addListener(rateListener(4.5f));
//        star5.addListener(rateListener(5));
//
//        starHalf.addListener(new HoverListener());
//        star1.addListener(new HoverListener());
//        star1half.addListener(new HoverListener());
//        star2.addListener(new HoverListener());
//        star2half.addListener(new HoverListener());
//        star3.addListener(new HoverListener());
//        star3half.addListener(new HoverListener());
//        star4.addListener(new HoverListener());
//        star4half.addListener(new HoverListener());
//        star5.addListener(new HoverListener());

        //TextButton rateButton = new TextButton("Rate", skin);

        //Create Table
        Table mainTable = new Table();
        //mainTable.setDebug(true);

        mainTable.add(titleLabel).padBottom(40).colspan(4).expandY().left();
        mainTable.row();
        mainTable.add(new Label("Rate Enjoyment : ", skin)).colspan(3).padBottom(10).left();
        mainTable.row();

        int j = 0;
        for (double i = 0.5; i <= 5; i = i + 0.5) {
            //System.out.println(i);
            enjoymentButtons[j] = new TextButton("" + i , skin);
            //enjoymentButtons[j].addListener(rateListener((float) i));
            enjoymentButtons[j].addListener(enjoymentButtonListener(j));
            enjoymentButtons[j].addListener(new HoverListener());
            enjoymentButtons[j].setColor(Color.BLACK);
            mainTable.add(enjoymentButtons[j]).width(buttonWidth).padRight(5);
            j++;
        }

        mainTable.row();
        mainTable.add(new Label("Rate Difficulty : ", skin)).padTop(20).colspan(3).padBottom(10).left();
        mainTable.row();

        j = 0;
        for (double i = 0.5; i <= 5; i = i + 0.5) {
            difficultyButtons[j] = new TextButton("" + i , skin);
            //enjoymentButtons[j].addListener(rateListener((float) i));
            difficultyButtons[j].addListener(difficultyButtonListener(j));
            difficultyButtons[j].addListener(new HoverListener());
            difficultyButtons[j].setColor(Color.BLACK);
            mainTable.add(difficultyButtons[j]).width(buttonWidth).padRight(5);
            j++;
        }

        mainTable.row();

        TextButton rateButton = new TextButton("Rate", skin);
        rateButton.addListener(rateListener());
        rateButton.addListener(new HoverListener());

        mainTable.add(rateButton).width(buttonWidth + 100).colspan(10).padBottom(100).padTop(50);


//        mainTable.add(starHalf).width(buttonWidth).padBottom(buttonBottomPad).padRight(10);
//        mainTable.add(star1).width(buttonWidth).padBottom(buttonBottomPad).padRight(10);
//        mainTable.add(star1half).width(buttonWidth).padBottom(buttonBottomPad).padRight(10);
//        mainTable.add(star2).width(buttonWidth).padBottom(buttonBottomPad).padRight(10);
//        mainTable.row();
//        mainTable.add(star2half).width(buttonWidth).padBottom(buttonBottomPad).padRight(10);
//        mainTable.add(star3).width(buttonWidth).padBottom(buttonBottomPad).padRight(10);
//        mainTable.add(star3half).width(buttonWidth).padBottom(buttonBottomPad).padRight(10);
//        mainTable.add(star4).width(buttonWidth).padBottom(buttonBottomPad).padRight(10);
//        mainTable.row();
//        mainTable.add(star4half).width(buttonWidth).padBottom(buttonBottomPad).colspan(2).right().padRight(10);
//        mainTable.add(star5).width(buttonWidth).colspan(2).padRight(10).left().padBottom(buttonBottomPad);
        //mainTable.row();
        //mainTable.add(rateButton).width(buttonWidth + 30);

        //Set table to fill stage
        mainTable.setFillParent(true);

        //Add table to stage
        stage.addActor(mainTable);
    }

    private ClickListener enjoymentButtonListener(final int clickedButton) {
        return new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //if enjoyment button is greater than 0
                if (enjoymentButtonSelected == clickedButton) {
                    for (int i = 0; i < 10; i++) {
                        if (i != clickedButton) {
                            enjoymentButtons[i].setDisabled(false);
                        } else {
                            enjoymentButtons[i].setColor(Color.BLACK);
                        }
                    }
                    enjoymentButtonSelected = -1;
                } else if (enjoymentButtonSelected < 0) {
                    //if enjoyment button selected is less than 0
                    for (int i = 0; i < 10; i++) {
                        if (i != clickedButton) {
                            enjoymentButtons[i].setDisabled(true);
                        } else {
                            enjoymentButtons[i].setColor(Color.GREEN);
                        }
                    }
                    enjoymentButtonSelected = clickedButton;
                }
            }
        };
    }

    private ClickListener difficultyButtonListener(final int clickedButton) {
        return new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //if enjoyment button is greater than 0
                if (difficultyButtonSelected == clickedButton) {
                    for (int i = 0; i < 10; i++) {
                        if (i != clickedButton) {
                            difficultyButtons[i].setDisabled(false);
                        } else {
                            difficultyButtons[i].setColor(Color.BLACK);
                        }
                    }
                    difficultyButtonSelected = -1;
                } else if (difficultyButtonSelected < 0) {
                    //if enjoyment button selected is less than 0
                    for (int i = 0; i < 10; i++) {
                        if (i != clickedButton) {
                            difficultyButtons[i].setDisabled(true);
                        } else {
                            difficultyButtons[i].setColor(Color.GREEN);
                        }
                    }
                    difficultyButtonSelected = clickedButton;
                }
            }
        };
    }

    private ClickListener rateListener() {
        return new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                float rate = 0;

                if ((enjoymentButtonSelected == -1) || (difficultyButtonSelected == -1)) {
                    ErrorDialog notEnoughInfo = new ErrorDialog(skin, "Please select ratings for level.", stage);
                } else {
                    rate = (enjoymentButtonSelected + difficultyButtonSelected) / 2f;

                    //TODO: add rating to database
                    //when level rating is updated so is # users rated
//                db.addLevelRating(levelid, rate);

                    //TODO: if successful show dialog then set screen to main menu
                    ErrorDialog dialog = new ErrorDialog("Rating Level", "MainMenuScreen", game, skin, "Thank you for rating!", stage);
                    //Dialog dialog1 = dialog.getErrorDialog();
                }

                System.out.println(rate);
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
