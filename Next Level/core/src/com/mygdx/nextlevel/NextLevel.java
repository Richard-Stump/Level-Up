package com.mygdx.nextlevel;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.nextlevel.actors.Enemy;
import com.mygdx.nextlevel.actors.Player;

public class NextLevel extends ApplicationAdapter implements InputProcessor {
	SpriteBatch batch;
	Player player;
	Enemy enemy;

	World world;
	Body bodyEdgeScreen;

	Box2DDebugRenderer debugRenderer;
	Matrix4 debugMatrix;
	OrthographicCamera camera;

	float torque = 0.0f;
	boolean drawSprite = true;
	final float PIXELS_TO_METERS = 100f;

	final short PHYSICS_ENTITY = 0x1; //0001
	final short BLOCK_ENTITY = 0x1 << 2; //0100
	final short WORLD_ENTITY = 0x1 << 1; //0010

	@Override
	public void create () {
		batch = new SpriteBatch();

		//Physics World
		world = new World(new Vector2(0, -1F), true);

		//Create Enemy and Player
		Texture playerTexture = new Texture("tyson.jpg");
		player = new Player(playerTexture, world, 0.2f, 0.5f);
		final Texture enemyTexture = new Texture("enemy.jpg");
		enemy = new Enemy(enemyTexture, world,100f, 0.5f);

		//Bottom edge of screen
		BodyDef edgeBodyDef = new BodyDef();
		edgeBodyDef.type = BodyDef.BodyType.StaticBody;

		float w = Gdx.graphics.getWidth()/PIXELS_TO_METERS;
		float h = Gdx.graphics.getHeight()/PIXELS_TO_METERS;

		edgeBodyDef.position.set(0,0);
		FixtureDef fixtureDefEdge = new FixtureDef();
		fixtureDefEdge.filter.categoryBits = WORLD_ENTITY;
		fixtureDefEdge.filter.maskBits = PHYSICS_ENTITY | BLOCK_ENTITY | WORLD_ENTITY;

		EdgeShape edgeShape = new EdgeShape();
		edgeShape.set(-w/2, -h/2, w/2, -h/2);
		fixtureDefEdge.shape = edgeShape;
		bodyEdgeScreen = world.createBody(edgeBodyDef);
		bodyEdgeScreen.createFixture(fixtureDefEdge);
		bodyEdgeScreen.setUserData(bodyEdgeScreen);

		edgeShape.set(-w/2, -h/2, -w/2, h/2);
		fixtureDefEdge.shape = edgeShape;
		bodyEdgeScreen = world.createBody(edgeBodyDef);
		bodyEdgeScreen.createFixture(fixtureDefEdge);
		bodyEdgeScreen.setUserData(bodyEdgeScreen);

		edgeShape.set(-w/2, h/2, w/2, h/2);
		fixtureDefEdge.shape = edgeShape;
		bodyEdgeScreen = world.createBody(edgeBodyDef);
		bodyEdgeScreen.createFixture(fixtureDefEdge);
		bodyEdgeScreen.setUserData(bodyEdgeScreen);

		edgeShape.set(w/2, -h/2, w/2, h/2);
		fixtureDefEdge.shape = edgeShape;
		bodyEdgeScreen = world.createBody(edgeBodyDef);
		bodyEdgeScreen.createFixture(fixtureDefEdge);
		edgeShape.dispose();

		Gdx.input.setInputProcessor(this);

		player.getBody().setUserData(player);
		enemy.getBody().setUserData(enemy);
		bodyEdgeScreen.setUserData(bodyEdgeScreen);

		world.setContactListener(new ContactListener() {
			@Override
			public void beginContact(Contact contact) { //called when two fixuers begin contact
				if (contact.getFixtureA().getBody().getUserData().equals(player))
					System.out.println("Fixture A");
				if (contact.getFixtureB().getBody().getUserData().equals(player))
					System.out.println("Fixture B");
			}

			@Override
			public void endContact(Contact contact) { //called when two fixtures stop contact
//				contact.getFixtureA().getBody().applyForceToCenter(10f, 10f, true);
			}

			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {

			}

			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {

			}
		});

		//Create box2bug render
		debugRenderer = new Box2DDebugRenderer();
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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
		return false;
	}

	//	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Input.Keys.RIGHT)
			player.getBody().setLinearVelocity(1f,player.getBody().getLinearVelocity().y);
		if (keycode == Input.Keys.LEFT)
			player.getBody().setLinearVelocity(-1f, player.getBody().getLinearVelocity().y);

		if(keycode == Input.Keys.UP)
			player.getBody().applyForceToCenter(0f,8f,true);
		if(keycode == Input.Keys.DOWN)
			player.getBody().applyForceToCenter(0f, -10f, true);

		if(keycode == Input.Keys.RIGHT_BRACKET)
			torque += 0.1f;
		if(keycode == Input.Keys.LEFT_BRACKET)
			torque -= 0.1f;

		// Remove the torque using backslash /
		if(keycode == Input.Keys.BACKSLASH)
			torque = 0.0f;

		// If user hits spacebar, reset everything back to normal
		if(keycode == Input.Keys.SPACE) {
			player.getBody().setLinearVelocity(0f, 0f);
			torque = 0f;
			player.getSprite().setPosition(0f,0f);
			player.getBody().setTransform(0f,0f,0f);
		}

		if(keycode == Input.Keys.ESCAPE)
			drawSprite = !drawSprite;

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
