package com.mygdx.nextlevel;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.nextlevel.screens.GameScreen;

public class NextLevel extends Game {
	public SpriteBatch batch;
//	public static final int WIDTH = 800;
//	public static final int HEIGHT = 480;
	public Screen gameScreen;

	@Override
	public void create () {
		gameScreen = new GameScreen();
		setScreen(gameScreen);
	}

//	@Override
//	public void render () {
//		super.render();
//	}
//
//	@Override
//	public void dispose () {
//		batch.dispose();
//	}

}
