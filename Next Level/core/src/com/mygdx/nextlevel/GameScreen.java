package com.mygdx.nextlevel;

<<<<<<< Updated upstream
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
=======
import com.badlogic.gdx.*;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.nextlevel.actors.Hero;
//import com.mygdx.nextlevel.actors.Player;
>>>>>>> Stashed changes

import java.util.Iterator;

public class GameScreen implements Screen {
<<<<<<< Updated upstream
    final NextLevel game;
    Texture heroImage;
    Texture enemyImage;
    Rectangle hero;
    Rectangle enemy;
    OrthographicCamera camera;
    int lives = 3;

    public GameScreen(final NextLevel game) {
        this.game = game;
        enemyImage = new Texture(Gdx.files.internal("boss-sprite.jpeg"));
        heroImage = new Texture(Gdx.files.internal("hero.png"));
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        enemy = new Rectangle();
        hero = new Rectangle();
        hero.x = 200;
        hero.y = 20;
        hero.width = 64;
        hero.height = 64;
        enemy.x = 800/2 - 64/2;
        enemy.y = 20;
        enemy.width = 64;
        enemy.height = 64;
=======

    // Main game variables
    NextLevel game;
    private OrthographicCamera camera;
//    private final ExtendViewport viewport;
    public SpriteBatch batch;
    private Stage stage;

    // Sprites
//    private final TextureAtlas textureAtlas;
    Sprite banana;
    Sprite crate;

    //float spriteXposition;
    //float spriteYposition;

    // Box2D Vars
    World world;
    Box2DDebugRenderer debugRenderer;
    Body bodyCrate;
    Body bodyBanana;
    Hero one, two;

    static final float STEP_TIME = 1f / 60f;
    static final int VELOCITY_ITERATIONS = 6;
    static final int POSITION_ITERATIONS = 2;

    float accumulator = 0;

    PhysicsShapeCache physicsBodies;

    // player
//    private Player player;

    //FileHandle physicsFile = (Gdx.files.internal("physics.xml"));
    //int lives = 3;

    /*
     * Constructor for main game
     */
    public GameScreen(NextLevel game) {
        this.game = game;
        debugRenderer = new Box2DDebugRenderer();
        world = new World(new Vector2(0,0),false);
        one = new Hero(world, 0,0);
        two = new Hero(world, 50, 50);


//        Box2D.init();
//        debugRenderer = new Box2DDebugRenderer();
//
//        // direction of gravity
//        world = new World(new Vector2(0, -98f), true);
//        physicsBodies = new PhysicsShapeCache("physics.xml");
//
//        // camera to follow player, view port to maintain aspect ratio
//        camera = new OrthographicCamera();
//        viewport = new ExtendViewport(960, 500, camera);
//
//        // create atlas for sprites
//        textureAtlas = new TextureAtlas("sprites.txt");
//
//        // create player
//        player = new Player(this);
//
//        // class that implements ContactListener
//        world.setContactListener(new WorldContactListener());


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

//    public TextureAtlas getAtlas() {
//        return this.textureAtlas;
//    }

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
//        handleInput(dt);
        world.step(1 / 60f, 6, 2);
        inputUpdate(dt);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

//        world.step(1/60f, 6, 2);
//
//        player.update(dt);
//
//
//        camera.position.x = bodyBanana.getPosition().x;
        // update game camera
//        camera.update();
        // tell renderer to draw only what the camera can see of the game world
        //mapRenderer.setView(gameCam);
>>>>>>> Stashed changes
    }


    @Override
    public void render(float delta) {
<<<<<<< Updated upstream
        ScreenUtils.clear(0, 0, 0.2f, 1);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.font.draw(game.batch, "Lives: " + lives, 0, 480);
        game.batch.draw(enemyImage, enemy.x, enemy.y, enemy.width, enemy.height);
        game.batch.draw(heroImage, hero.x, hero.y, hero.width, hero.height);
        game.batch.end();

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
            hero.x -= 200 * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            hero.x += 200 * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.UP))
            hero.y += 400 * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
            hero.y -= 400 * Gdx.graphics.getDeltaTime();
        if (hero.x < 0)
            hero.x = 0;
        if (hero.x > 800 - 64)
            hero.x = 800 - 64;
        if (hero.overlaps(enemy) && hero.y > enemy.y) {
            game.batch.begin();
            hero.x = 10;
            hero.y = 20;
            hero.width = 100;
            hero.height = 100;
            enemy.x = 500;
            enemy.y = 20;
            enemy.width = 64;
            enemy.height = 64;
            game.batch.draw(heroImage, hero.x, hero.y, hero.width, hero.height);
            game.batch.draw(enemyImage, enemy.x, enemy.y, enemy.width, enemy.height);

            game.batch.end();

        } else if (hero.overlaps(enemy)) {
            lives--;
            game.batch.begin();
            hero.x = 10;
            hero.y = 20;
            hero.width = 100;
            hero.height = 100;
            game.batch.draw(heroImage, hero.x, hero.y, hero.width, hero.height);
            game.batch.end();
        }
    }
    @Override
    public void resize(int width, int height) {
=======
//        ScreenUtils.clear(0, 0, 0.2f, 1);
//        camera.update();
        //stepWorld();

        Gdx.gl.glClearColor(0.57f, 0.77f, 0.85f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        debugRenderer.render(world, camera.combined.cpy().scl(32));
//        world.step(Gdx.graphics.getDeltaTime(), 6, 2);

//        debugRenderer.render(world, camera.combined);
//
//        batch.setProjectionMatrix(camera.combined);
//
//        //crate.setPosition(bodyCrate.getPosition().x, bodyCrate.getPosition().y);
//
//        batch.begin();
//        player.draw(batch);
        //banana.setPosition(spriteXposition, spriteYposition);
        //banana.setPosition(bodyBanana.getPosition().x, bodyBanana.getPosition().y);
        //banana.draw(batch);
        //crate.draw(batch);
//        batch.end();
        //spriteControl();
    }



    @Override
    public void resize(int width, int height) {
//        viewport.update(width, height, true);
>>>>>>> Stashed changes

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
<<<<<<< Updated upstream
        enemyImage.dispose();
        heroImage.dispose();

=======
        //enemyImage.dispose();
        //heroImage.dispose();
//        textureAtlas.dispose();
//        world.dispose();
//        debugRenderer.dispose();
        debugRenderer.dispose();
        world.dispose();
    }

    private void inputUpdate(float dt) {
        float x = 0, y = 0;
        if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
            y += 1;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            y -= 1;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            x -= 1;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            x += 1;
        }
>>>>>>> Stashed changes
    }
}
