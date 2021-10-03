package com.mygdx.nextlevel;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import java.util.HashMap;

import static com.badlogic.gdx.Files.FileType.Internal;

public class GameScreen implements Screen {
    final NextLevel game;
    final HashMap<String, Sprite> sprites = new HashMap<String, Sprite>();
    //Texture heroImage;
    //Texture enemyImage;
    //Rectangle hero;
    //Rectangle enemy;
    TextureAtlas textureAtlas;
    OrthographicCamera camera;
    ExtendViewport viewport;
    public SpriteBatch batch;
    public BitmapFont font;

    float spriteXposition;
    float spriteYposition;

    World world;

    static final float STEP_TIME = 1f / 60f;
    static final int VELOCITY_ITERATIONS = 6;
    static final int POSITION_ITERATIONS = 2;

    float accumulator = 0;

    PhysicsShapeCache physicsBodies;

    Sprite banana;
    Sprite crate;

    //Body body;
    Body bodyCrate;
    Body bodyBanana;

    FileHandle physicsFile = (Gdx.files.internal("physics.xml"));

    Box2DDebugRenderer debugRenderer;
    int lives = 3;

    public GameScreen(NextLevel game) {
        this.game = game;
        Box2D.init();
        debugRenderer = new Box2DDebugRenderer();

        //direction of gravity
        world = new World(new Vector2(0, -98f), true);

        physicsBodies = new PhysicsShapeCache("physics.xml");

        //enemyImage = new Texture(Gdx.files.internal("boss-sprite.jpeg"));
        //heroImage = new Texture(Gdx.files.internal("hero.png"));
        camera = new OrthographicCamera();
        viewport = new ExtendViewport(960, 500, camera);

        textureAtlas = new TextureAtlas("sprites.txt");
        banana = textureAtlas.createSprite("banana - Copy");
        crate = textureAtlas.createSprite("crate");

        batch = new SpriteBatch();

        //Body bodyPlayer = physicsBodies.createBody("banana - Copy", world, 0.05f, 0.05f);
        //body.setTransform(10, 10, 0);
        //Body bodyCrate = physicsBodies.createBody("crate", world, StaticBody,0.05f, 0.05f);

        //body and fixture for banana
        BodyDef bananaBodyDef = new BodyDef();
        bananaBodyDef.type = BodyDef.BodyType.KinematicBody;

        bananaBodyDef.position.set(banana.getX(), banana.getY());
        bodyBanana = physicsBodies.createBody("banana - Copy", world, bananaBodyDef, 0.05f, 0.05f);

        /*XmlReader xml = new XmlReader();
        XmlReader.Element element = xml.parse(physicsFile);
        PolygonNode bananaShape = new PolygonNode(element);*/

        PolygonShape bananaShape = new PolygonShape();
        bananaShape.setAsBox(banana.getWidth()/2, banana.getHeight()/2);

        FixtureDef bananaFixDef = new FixtureDef();
        bananaFixDef.shape = bananaShape;
        bananaFixDef.density = 1f;

        Fixture bananaFixture = bodyBanana.createFixture(bananaFixDef);

        //body and fixture for crate
        crate.setPosition(200, 0);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;

        bodyDef.position.set(crate.getX(), crate.getY());
        //body = world.createBody(bodyDef);
        bodyCrate = physicsBodies.createBody("crate", world, bodyDef,0.05f, 0.05f);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(crate.getWidth()/2, crate.getHeight()/2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 3f;

        Fixture fixture = bodyCrate.createFixture(fixtureDef);

        addSprites();
        shape.dispose();
        bananaShape.dispose();
    }

    private void addSprites() {
        Array<TextureAtlas.AtlasRegion> regions = textureAtlas.getRegions();

        for (TextureAtlas.AtlasRegion region : regions) {
            Sprite sprite = textureAtlas.createSprite(region.name);

            sprites.put(region.name, sprite);
        }
    }

    private void stepWorld() {
        float delta = Gdx.graphics.getDeltaTime();

        accumulator += Math.min(delta, 0.25f);

        if (accumulator >= STEP_TIME) {
            accumulator -= STEP_TIME;

            world.step(STEP_TIME, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
        }
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        camera.update();
        stepWorld();

        Gdx.gl.glClearColor(0.57f, 0.77f, 0.85f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        crate.setPosition(bodyCrate.getPosition().x, bodyCrate.getPosition().y);
        batch.begin();
        //banana.setPosition(spriteXposition, spriteYposition);
        banana.setPosition(bodyBanana.getPosition().x, bodyBanana.getPosition().y);
        banana.draw(batch);

        crate.draw(batch);

        batch.end();

        debugRenderer.render(world, camera.combined);
        spriteControl();
    }

    public void spriteControl() {
        boolean moveRight = (Gdx.input.isKeyPressed(Input.Keys.RIGHT));
        boolean moveLeft = (Gdx.input.isKeyPressed(Input.Keys.LEFT));

        if (moveRight && bodyBanana.getLinearVelocity().x < 3.00f) {
            bodyBanana.applyLinearImpulse(new Vector2(0.15f, 0), bodyBanana.getWorldCenter(), true);
        } else if (moveLeft && bodyBanana.getLinearVelocity().x > -3.00f) {
            bodyBanana.applyLinearImpulse(new Vector2(-0.15f, 0), bodyBanana.getWorldCenter(), true);
        } else if (moveRight == moveLeft) {
            bodyBanana.setLinearVelocity(0f, bodyBanana.getLinearVelocity().y);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            bodyBanana.applyLinearImpulse(new Vector2(0, 20), bodyBanana.getPosition(), true);
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);

        batch.setProjectionMatrix(camera.combined);
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

