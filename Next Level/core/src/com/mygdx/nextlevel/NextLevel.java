package com.mygdx.nextlevel;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.mygdx.nextlevel.screens.MainMenuScreen;

public class NextLevel extends Game {
	public SpriteBatch batch;
	public BitmapFont font;
	public OrthographicCamera camera;
	public ExtendViewport viewport;

	public static final int V_WIDTH = 400;
	public static final int V_HEIGHT = 208;
	public static final float PPM = 100; // Scale objects

	// Box2D Collision Bits
	public static final short GROUND_BIT = 1 << 0; // 0000 0000 0001
	public static final short PLAYER_BIT = 1 << 1; // 0000 0000 0010
	public static final short PLATFORM_BIT = 1 << 2; // 0000 0000 0100
	public static final short OBJECT_BIT = 1 << 3; // 0000 0000 1000
	public static final short ENEMY_BIT = 1 << 4; // 0000 0001 0000
	public static final short ENEMY_HITBOX_BIT = 1 << 5; // 0000 0010 0000
	public static final short PLAYER_HITBOX_BIT = 1 << 6; // 0000 0100 0000
	public static final short PIT_OBJECT_BIT = 1 << 7; // 0000 1000 0000
	public static final short EXIT_BIT = 1 << 8; // 0001 0000 0000

	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();
		camera = new OrthographicCamera();
		viewport = new ExtendViewport(960, 500, camera);
		this.setScreen(new MainMenuScreen(this));
	}


	@Override
	public void render () {
		super.render();
	}

	@Override
	public void dispose () {
		font.dispose();
	}
}