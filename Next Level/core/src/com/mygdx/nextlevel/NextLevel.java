package com.mygdx.nextlevel;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.nextlevel.actors.Enemy;
import com.mygdx.nextlevel.actors.Player;
import com.mygdx.nextlevel.NextLevel.1;
import java.util.ArrayList;

public class NextLevel extends ApplicationAdapter implements InputProcessor {
	SpriteBatch batch;
	Player player;
	Enemy enemy;
	World world;
	Body bodyEdgeScreen;
	Box2DDebugRenderer debugRenderer;
	Matrix4 debugMatrix;
	OrthographicCamera camera;
	ArrayList<Body> deleteList = new ArrayList();
	ArrayList<Sprite> spriteDelList = new ArrayList();
	boolean facingRight = true;
	float torque = 0.0F;
	boolean drawSprite = true;
	final float PIXELS_TO_METERS = 100.0F;
	final short PHYSICS_ENTITY = 1;
	final short WORLD_ENTITY = 2;
	final short BLOCK_ENTITY = 4;
	boolean landed = true;
	boolean jumped = false;

	public NextLevel() {}

	@Override
	public void create () {
		this.batch = new SpriteBatch();

		//Physics World
		this.world = new World(new Vector2(0.0F, -50.0F), true);

		//Create Enemy and Player
		Texture playerTexture = new Texture("tyson.jpg");
		this.player = new Player(playerTexture, this.world, 0.2f, 0.5f);
		final Texture enemyTexture = new Texture("enemy.jpg");
		this.enemy = new Enemy(enemyTexture, this.world,100f, 0.5f);

		//Bottom edge of screen
		BodyDef edgeBodyDef = new BodyDef();
		edgeBodyDef.type = BodyDef.BodyType.StaticBody;

		float w = Gdx.graphics.getWidth()/PIXELS_TO_METERS;
		float h = Gdx.graphics.getHeight()/PIXELS_TO_METERS;

		edgeBodyDef.position.set(0.0F,0.0F);
		FixtureDef fixtureDefEdge = new FixtureDef();
		fixtureDefEdge.filter.categoryBits = WORLD_ENTITY;
		fixtureDefEdge.filter.maskBits = PHYSICS_ENTITY | BLOCK_ENTITY | WORLD_ENTITY;

		EdgeShape edgeShape = new EdgeShape();
		edgeShape.set(-w/2.0F, -h/2.0F, w/2.0F, -h/2.0F);
		fixtureDefEdge.shape = edgeShape;
		this.bodyEdgeScreen = this.world.createBody(edgeBodyDef);
		this.bodyEdgeScreen.createFixture(fixtureDefEdge);
		this.bodyEdgeScreen.setUserData(this.bodyEdgeScreen);

		edgeShape.set(-w / 2.0F, -h / 2.0F, -w / 2.0F, h / 2.0F);
		fixtureDefEdge.shape = edgeShape;
		this.bodyEdgeScreen = this.world.createBody(edgeBodyDef);
		this.bodyEdgeScreen.createFixture(fixtureDefEdge);
		this.bodyEdgeScreen.setUserData(this.bodyEdgeScreen);

		edgeShape.set(-w / 2.0F, h / 2.0F, w / 2.0F, h / 2.0F);
		fixtureDefEdge.shape = edgeShape;
		this.bodyEdgeScreen = this.world.createBody(edgeBodyDef);
		this.bodyEdgeScreen.createFixture(fixtureDefEdge);
		this.bodyEdgeScreen.setUserData(this.bodyEdgeScreen);

		edgeShape.set(w / 2.0F, -h / 2.0F, w / 2.0F, h / 2.0F);
		fixtureDefEdge.shape = edgeShape;
		this.bodyEdgeScreen = this.world.createBody(edgeBodyDef);
		this.bodyEdgeScreen.createFixture(fixtureDefEdge);
		edgeShape.dispose();

		Gdx.input.setInputProcessor(this);

		this.player.getBody().setUserData(this.player);
		this.enemy.getBody().setUserData(this.enemy);
		this.bodyEdgeScreen.setUserData(this.bodyEdgeScreen);

		this.world.setContactListener(new 1(this));

		//Create box2bug render
		this.debugRenderer = new Box2DDebugRenderer();
		this.camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}


	@Override
	public void render () {
		//Advance frame
		camera.update();
		world.step(1f/60f, 6, 2);

		//Apply Torque
		player.getBody().applyTorque(torque,true);

		//Set position from updated physics
		player.getSprite().setPosition((player.getBody().getPosition().x * PIXELS_TO_METERS) - player.getSprite().getWidth()/2, (player.getBody().getPosition().y * PIXELS_TO_METERS) - player.getSprite().getHeight()/2);
		enemy.getSprite().setPosition((enemy.getBody().getPosition().x * PIXELS_TO_METERS) - enemy.getSprite().getWidth()/2, (enemy.getBody().getPosition().y * PIXELS_TO_METERS) - enemy.getSprite().getHeight()/2);

		Gdx.gl.glClearColor(1,1,1,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(camera.combined);
		debugMatrix = batch.getProjectionMatrix().cpy().scale(PIXELS_TO_METERS, PIXELS_TO_METERS, 0);

		batch.begin();
		if (drawSprite) {
			batch.draw(player.getSprite(), player.getSprite().getX(), player.getSprite().getY(), player.getSprite().getOriginX(), player.getSprite().getOriginY(), player.getSprite().getWidth(), player.getSprite().getHeight(), player.getSprite().getScaleX(), player.getSprite().getScaleY(), player.getSprite().getRotation());
			batch.draw(enemy.getSprite(), enemy.getSprite().getX(), enemy.getSprite().getY(), enemy.getSprite().getOriginX(), enemy.getSprite().getOriginY(), enemy.getSprite().getWidth(), enemy.getSprite().getHeight(), enemy.getSprite().getScaleX(), enemy.getSprite().getScaleY(), enemy.getSprite().getRotation());
		}
		batch.end();
		debugRenderer.render(world, debugMatrix);
	}

	@Override
	public void dispose () {
		batch.dispose();
		world.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == 19 && this.landed && !this.jumped) {
			this.player.getBody().setLinearVelocity(0.0F, 100.0F);
			this.jumped = true;
			this.landed = false;
		}

		return true;
	}

	//	@Override
	public boolean keyUp(int keycode) {
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(float amountX, float amountY) {
		return false;
	}
}
