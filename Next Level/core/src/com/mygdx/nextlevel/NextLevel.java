package com.mygdx.nextlevel;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import jdk.tools.jmod.Main;

public class NextLevel extends Game {
	SpriteBatch batch;
	Texture img;
	
	@Override
	public void create () {
		//batch = new SpriteBatch();
		//img = new Texture("badlogic.jpg");
		setScreen(new MainMenuScreen());
	}

	/*@Override
	public void render () {
		ScreenUtils.clear(1, 0, 0, 1);
		batch.begin();
		batch.draw(img, 0, 0);
		batch.end();
	}*/
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
