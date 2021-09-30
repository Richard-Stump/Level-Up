package com.mygdx.nextlevel;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.nextlevel.screens.GameScreen;

public class NextLevel extends Game {
	public SpriteBatch batch;
	public static final int WIDTH = 800;
	public static final int HEIGHT = 480;

	@Override
	public void create () {
		batch = new SpriteBatch();
		setScreen(new GameScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void dispose () {
		batch.dispose();
	}

}
