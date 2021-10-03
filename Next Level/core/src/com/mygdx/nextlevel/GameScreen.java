package com.mygdx.nextlevel;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.mygdx.nextlevel.actors.Player;

import java.util.HashMap;

import static com.badlogic.gdx.Files.FileType.Internal;

public class GameScreen implements Screen {

    // Main game variables
    final NextLevel game;
    private final OrthographicCamera camera;
    private final ExtendViewport viewport;
    public SpriteBatch batch;

    // Sprites
    private final TextureAtlas textureAtlas;
    Sprite banana;
    Sprite crate;

    //float spriteXposition;
    //float spriteYposition;

    // Box2D Vars
    World world;
    Box2DDebugRenderer debugRenderer;
    Body bodyCrate;
    Body bodyBanana;

    static final float STEP_TIME = 1f / 60f;
    static final int VELOCITY_ITERATIONS = 6;
    static final int POSITION_ITERATIONS = 2;

    float accumulator = 0;

    PhysicsShapeCache physicsBodies;

    // player
    private Player player;

    //FileHandle physicsFile = (Gdx.files.internal("physics.xml"));
    //int lives = 3;

    /*
     * Constructor for main game
     */
    public GameScreen(NextLevel game) {
        this.game = game;
        Box2D.init();
        debugRenderer = new Box2DDebugRenderer();

        // direction of gravity
        world = new World(new Vector2(0, -98f), true);
        physicsBodies = new PhysicsShapeCache("physics.xml");

        // camera to follow player, view port to maintain aspect ratio
        camera = new OrthographicCamera();
        viewport = new ExtendViewport(960, 500, camera);

        // create atlas for sprites
        textureAtlas = new TextureAtlas("sprites.txt");

        // create player
        player = new Player(this);

        // class that implements ContactListener
        world.setContactListener(new WorldContactListener());


        /* banana = textureAtlas.createSprite("banana - Copy");
        crate = textureAtlas.createSprite("crate");

        batch = new SpriteBatch();

        //Body bodyPlayer = physicsBodies.createBody("banana - Copy", world, 0.05f, 0.05f);
        //body.setTransform(10, 10, 0);
        //Body bodyCrate = physicsBodies.createBody("crate", world, StaticBody,0.05f, 0.05f);

        // body and fixture for banana
        BodyDef bananaBodyDef = new BodyDef();
        bananaBodyDef.type = BodyDef.BodyType.KinematicBody;

        bananaBodyDef.position.set(banana.getX(), banana.getY());
        bodyBanana = physicsBodies.createBody("banana - Copy", world, bananaBodyDef, 0.05f, 0.05f);

        XmlReader xml = new XmlReader();
        XmlReader.Element element = xml.parse(physicsFile);
        PolygonNode bananaShape = new PolygonNode(element);

        PolygonShape bananaShape = new PolygonShape();
        bananaShape.setAsBox(banana.getWidth()/2, banana.getHeight()/2);

        FixtureDef bananaFixDef = new FixtureDef();
        bananaFixDef.shape = bananaShape;
        bananaFixDef.density = 1f;

        Fixture bananaFixture = bodyBanana.createFixture(bananaFixDef);

        // body and fixture for crate
        crate.setPosition(200, 0);

        BodyDef crateBodyDef = new BodyDef();
        crateBodyDef.type = BodyDef.BodyType.StaticBody;

        crateBodyDef.position.set(crate.getX(), crate.getY());
        body = world.createBody(bodyDef);
        bodyCrate = physicsBodies.createBody("crate", world, crateBodyDef,0.05f, 0.05f);

        PolygonShape crateShape = new PolygonShape();
        crateShape.setAsBox(crate.getWidth()/2, crate.getHeight()/2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = crateShape;
        fixtureDef.density = 3f;

        Fixture fixture = bodyCrate.createFixture(fixtureDef);

        //addSprites();
        crateShape.dispose();
        bananaShape.dispose(); */
    }

    public TextureAtlas getAtlas() {
        return this.textureAtlas;
    }

    public World getWorld() {
        return this.world;
    }

    /*private void stepWorld() {
        float delta = Gdx.graphics.getDeltaTime();

        accumulator += Math.min(delta, 0.25f);

        if (accumulator >= STEP_TIME) {
            accumulator -= STEP_TIME;

            world.step(STEP_TIME, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
        }
    }*/

    public void handleInput(float dt) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            bodyBanana.applyLinearImpulse(new Vector2(0, 3f), bodyBanana.getWorldCenter(), true);
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && bodyBanana.getLinearVelocity().x >= -2) {
            bodyBanana.applyLinearImpulse(new Vector2(-0.1f, 0), bodyBanana.getWorldCenter(), true);
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && bodyBanana.getLinearVelocity().x <= 2) {
            bodyBanana.applyLinearImpulse(new Vector2(0.1f, 0), bodyBanana.getWorldCenter(), true);
        }
    }

    public void update(float dt) {
        // handle user input first
        handleInput(dt);

        world.step(1/60f, 6, 2);

        player.update(dt);


        camera.position.x = bodyBanana.getPosition().x;
        // update game camera
        camera.update();
        // tell renderer to draw only what the camera can see of the game world
        //mapRenderer.setView(gameCam);
    }


    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        camera.update();
        //stepWorld();

        Gdx.gl.glClearColor(0.57f, 0.77f, 0.85f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        debugRenderer.render(world, camera.combined);

        batch.setProjectionMatrix(camera.combined);

        //crate.setPosition(bodyCrate.getPosition().x, bodyCrate.getPosition().y);

        batch.begin();
        player.draw(batch);
        //banana.setPosition(spriteXposition, spriteYposition);
        //banana.setPosition(bodyBanana.getPosition().x, bodyBanana.getPosition().y);
        //banana.draw(batch);
        //crate.draw(batch);
        batch.end();
        //spriteControl();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);

        //batch.setProjectionMatrix(camera.combined);
    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        //enemyImage.dispose();
        //heroImage.dispose();
        textureAtlas.dispose();
        world.dispose();
        debugRenderer.dispose();
    }
}

