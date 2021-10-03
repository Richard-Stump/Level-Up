package com.mygdx.nextlevel;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.nextlevel.actors.Player;
import org.graalvm.compiler.nodes.cfg.Block;

import javax.sound.midi.Receiver;



public class NextLevel extends ApplicationAdapter implements InputProcessor, ContactListener {
	SpriteBatch batch;
	Player player1;
	Texture player, enemy;
	Sprite playerSprite, enemySprite;

	World world;
	Body bodyPlayer, bodyEnemy;
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
		player = new Texture("tyson.jpg");
		playerSprite = new Sprite(player);
		enemy = new Texture("enemy.jpg");
		enemySprite = new Sprite(enemy);

		//Scale sprite
		playerSprite.setSize(64, 64);
		enemySprite.setSize(64, 64);

		playerSprite.setPosition(-playerSprite.getWidth()/2, -playerSprite.getHeight()/2 + 200);
		enemySprite.setPosition(-enemySprite.getWidth()/2 + 20, -enemySprite.getHeight()/2 -100);

		//Physics World
		world = new World(new Vector2(0, -1F), true);

		player1 = new Player(player, 0.2f, 0.5f);

		//Body Player Definition
		BodyDef bodyPlayerDef = new BodyDef();
		bodyPlayerDef.type = BodyDef.BodyType.DynamicBody;
		bodyPlayerDef.position.set((playerSprite.getX() + playerSprite.getWidth()/2)/PIXELS_TO_METERS, (playerSprite.getY() + playerSprite.getHeight()/2)/PIXELS_TO_METERS);

		//Body Enemy Definition
		BodyDef bodyEnemyDef = new BodyDef();
		bodyEnemyDef.type = BodyDef.BodyType.StaticBody;
		bodyEnemyDef.position.set((enemySprite.getX() + enemySprite.getWidth()/2)/PIXELS_TO_METERS, (enemySprite.getY() + enemySprite.getHeight()/2)/PIXELS_TO_METERS);

		//Creat Body in world
//		bodyPlayer = world.createBody(bodyPlayerDef);
		bodyPlayer = world.createBody(player1.getBodyPlayerDef());
		bodyEnemy = world.createBody(bodyEnemyDef);

		//Define dimensions of physics shape
		PolygonShape shapePlayer = new PolygonShape();
		shapePlayer.setAsBox(playerSprite.getWidth()/2/PIXELS_TO_METERS, playerSprite.getHeight()/2/PIXELS_TO_METERS);
		PolygonShape shapeEnemy = new PolygonShape();
		shapeEnemy.setAsBox(enemySprite.getWidth()/2/PIXELS_TO_METERS, enemySprite.getHeight()/2/PIXELS_TO_METERS);


		//Fixture (Density & Mass)
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.density = 0.2f;
		fixtureDef.restitution = 0.5f;
		fixtureDef.filter.categoryBits = PHYSICS_ENTITY;
		fixtureDef.filter.maskBits = WORLD_ENTITY | PHYSICS_ENTITY | BLOCK_ENTITY;

		//Player
		fixtureDef.shape = shapePlayer;
//		bodyPlayer.createFixture(fixtureDef);
		bodyPlayer.createFixture(player1.getPlayerFixtureDef());
		//Enemy
		fixtureDef.shape = shapeEnemy;
		fixtureDef.filter.categoryBits = BLOCK_ENTITY;
		fixtureDef.filter.maskBits = WORLD_ENTITY | PHYSICS_ENTITY | BLOCK_ENTITY;
		bodyEnemy.createFixture(fixtureDef);

		shapePlayer.dispose();
		shapeEnemy.dispose();

		//Bottom edge of screen
		BodyDef bodyDefEdge = new BodyDef();
		bodyDefEdge.type = BodyDef.BodyType.StaticBody;

		float w = Gdx.graphics.getWidth()/PIXELS_TO_METERS;
		float h = Gdx.graphics.getHeight()/PIXELS_TO_METERS;

		bodyDefEdge.position.set(0,0);
		FixtureDef fixtureDefEdge = new FixtureDef();
		fixtureDefEdge.filter.categoryBits = WORLD_ENTITY;
		fixtureDefEdge.filter.maskBits = PHYSICS_ENTITY | BLOCK_ENTITY | WORLD_ENTITY;

		EdgeShape edgeShape = new EdgeShape();
		edgeShape.set(-w/2, -h/2, w/2, -h/2);
		fixtureDefEdge.shape = edgeShape;
		bodyEdgeScreen = world.createBody(bodyDefEdge);
		bodyEdgeScreen.createFixture(fixtureDefEdge);

		edgeShape.set(-w/2, -h/2, -w/2, h/2);
		fixtureDefEdge.shape = edgeShape;
		bodyEdgeScreen = world.createBody(bodyDefEdge);
		bodyEdgeScreen.createFixture(fixtureDefEdge);

		edgeShape.set(-w/2, h/2, w/2, h/2);
		fixtureDefEdge.shape = edgeShape;
		bodyEdgeScreen = world.createBody(bodyDefEdge);
		bodyEdgeScreen.createFixture(fixtureDefEdge);

		edgeShape.set(w/2, -h/2, w/2, h/2);
		fixtureDefEdge.shape = edgeShape;
		bodyEdgeScreen = world.createBody(bodyDefEdge);
		bodyEdgeScreen.createFixture(fixtureDefEdge);
		edgeShape.dispose();

		Gdx.input.setInputProcessor(this);

		world.setContactListener(new ContactListener() {
			@Override
			public void beginContact(Contact contact) {
				endContact(contact);
			}

			@Override
			public void endContact(Contact contact) {
//				contact.getFixtureB().getBody().applyForce(1f, 1f, 1f, 1f, true);
//				if (contact.getFixtureA().getBody().getInertia() > 0)
//					System.out.println("Test");
				contact.getFixtureA().getBody().applyForceToCenter(100f, 1f, true);
			}

			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {

			}

			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {

			}
		});
		//Create box2dbug render
		debugRenderer = new Box2DDebugRenderer();
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}


	@Override
	public void render () {
		//Advance frame
		camera.update();
		world.step(1f/60f, 6, 2);

		//Apply Torque
//		bodyPlayer.applyTorque(torque,true);

		//Set position from updated physics
		player1.getPlayerSprite().setPosition((bodyPlayer.getPosition().x * PIXELS_TO_METERS) - player1.getPlayerSprite().getWidth()/2, (bodyPlayer.getPosition().y * PIXELS_TO_METERS) - player1.getPlayerSprite().getHeight()/2);
		enemySprite.setPosition((bodyEnemy.getPosition().x * PIXELS_TO_METERS) - enemySprite.getWidth()/2, (bodyEnemy.getPosition().y * PIXELS_TO_METERS) - enemySprite.getHeight()/2);

		Gdx.gl.glClearColor(1,1,1,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(camera.combined);
		debugMatrix = batch.getProjectionMatrix().cpy().scale(PIXELS_TO_METERS, PIXELS_TO_METERS, 0);

		batch.begin();
		if (drawSprite) {
			batch.draw(player1.getPlayerSprite(), player1.getPlayerSprite().getX(), player1.getPlayerSprite().getY(), player1.getPlayerSprite().getOriginX(), player1.getPlayerSprite().getOriginY(), player1.getPlayerSprite().getWidth(), player1.getPlayerSprite().getHeight(), player1.getPlayerSprite().getScaleX(), player1.getPlayerSprite().getScaleY(), player1.getPlayerSprite().getRotation());
			batch.draw(enemySprite, enemySprite.getX(), enemySprite.getY(), enemySprite.getOriginX(), enemySprite.getOriginY(), enemySprite.getWidth(), enemySprite.getHeight(), enemySprite.getScaleX(), enemySprite.getScaleY(), enemySprite.getRotation());
		}
		batch.end();
		debugRenderer.render(world, debugMatrix);
	}

	@Override
	public void dispose () {
		batch.dispose();
		world.dispose();
		player.dispose();
		enemy.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	//	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Input.Keys.RIGHT)
			bodyPlayer.setLinearVelocity(1f,bodyPlayer.getLinearVelocity().y);
		if (keycode == Input.Keys.LEFT)
			bodyPlayer.setLinearVelocity(-1f, bodyPlayer.getLinearVelocity().y);

		if(keycode == Input.Keys.UP)
			bodyPlayer.applyForceToCenter(0f,8f,true);
		if(keycode == Input.Keys.DOWN)
			bodyPlayer.applyForceToCenter(0f, -10f, true);

//		if(keycode == Input.Keys.RIGHT_BRACKET)
//			torque += 0.1f;
//		if(keycode == Input.Keys.LEFT_BRACKET)
//			torque -= 0.1f;
//
//		// Remove the torque using backslash /
//		if(keycode == Input.Keys.BACKSLASH)
//			torque = 0.0f;

		// If user hits spacebar, reset everything back to normal
		if(keycode == Input.Keys.SPACE) {
			bodyPlayer.setLinearVelocity(0f, 0f);
			torque = 0f;
			playerSprite.setPosition(0f,0f);
			bodyPlayer.setTransform(0f,0f,0f);
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

	@Override
	public void beginContact(Contact contact) {
	}

	@Override
	public void endContact(Contact contact) {

	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {

	}
}
