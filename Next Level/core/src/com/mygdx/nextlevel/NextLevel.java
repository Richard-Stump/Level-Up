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
<<<<<<< Updated upstream
		gameScreen = new GameScreen();
		setScreen(gameScreen);
=======
		font = new BitmapFont();
		camera = new OrthographicCamera();
		viewport = new ExtendViewport(960, 500, camera);
//		this.setScreen(new MainMenuScreen(this));
		this.setScreen(new GameScreen(this));

>>>>>>> Stashed changes
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
