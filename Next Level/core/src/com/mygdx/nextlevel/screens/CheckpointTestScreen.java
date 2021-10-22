package com.mygdx.nextlevel.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.nextlevel.NextLevel;
import com.mygdx.nextlevel.actors.*;
import com.mygdx.nextlevel.hud.Hud;

public class CheckpointTestScreen implements Screen {
    SpriteBatch batch;
    Player player;
    Checkpoint checkpoint;
    Hud hud;

    World world;
    Body bodyEdgeScreen;

    Box2DDebugRenderer debugRenderer;
    Matrix4 debugMatrix;
    OrthographicCamera camera;

    final float PIXELS_TO_METERS = 100f;

    final short PHYSICS_ENTITY = 0x1; //0001
    final short BLOCK_ENTITY = 0x1 << 2; //0100
    final short WORLD_ENTITY = 0x1 << 1; //0010

    //Directional Collisions
    final int bottom = 1;
    final int left = 2;
    final int top = 3;
    final int right = 4;

    public CheckpointTestScreen(NextLevel game) {
        this.batch = game.batch;

        //Physics World
        this.world = new World(new Vector2(0.0F, -9.8F), true);
        float w = Gdx.graphics.getWidth() * 1.25f;
        float h = Gdx.graphics.getHeight();

        //Player Initialization
        final Vector2 playerSpawn = new Vector2(32, 32);
        this.player = new Player(new Texture("goomba.png"), this.world, playerSpawn, 0.25f, 0f);

        //Checkpoint Initialization
        Vector2 checkpointSpawn = new Vector2(32 + 2 * 64, 32);
        this.checkpoint = new Checkpoint(new Texture("checkpoint2.jpg"), this.world, checkpointSpawn,0f, 0f, this.player);

        //Update to screen parameters
        w /= PIXELS_TO_METERS;
        h /= PIXELS_TO_METERS;

        //Screen Border
        BodyDef edgeBodyDef = new BodyDef();
        edgeBodyDef.type = BodyDef.BodyType.KinematicBody;
        edgeBodyDef.position.set(0.0F,0.0F);
        this.bodyEdgeScreen = this.world.createBody(edgeBodyDef);

        //Fixture Setup for Border
        FixtureDef fixtureDefEdgeLeftRightTop = new FixtureDef();
        FixtureDef fixtureDefEdgeBottom = new FixtureDef();
        fixtureDefEdgeLeftRightTop.filter.categoryBits = WORLD_ENTITY;
        fixtureDefEdgeBottom.filter.categoryBits = WORLD_ENTITY;
        fixtureDefEdgeLeftRightTop.filter.maskBits = PHYSICS_ENTITY | BLOCK_ENTITY | WORLD_ENTITY;
        fixtureDefEdgeBottom.filter.maskBits = PHYSICS_ENTITY | BLOCK_ENTITY | WORLD_ENTITY;

        //Bottom of the world
        EdgeShape edgeShape = new EdgeShape();
        edgeShape.set(0,0,w,0);
        fixtureDefEdgeBottom.shape = edgeShape;
        fixtureDefEdgeBottom.density = 100.0f;
        fixtureDefEdgeBottom.restitution = 0f;
        fixtureDefEdgeBottom.friction = 1f;
        this.bodyEdgeScreen.createFixture(fixtureDefEdgeBottom);

        //Left Side of the world
        edgeShape.set(0,0,0,h);
        fixtureDefEdgeLeftRightTop.shape = edgeShape;
        this.bodyEdgeScreen.createFixture(fixtureDefEdgeLeftRightTop);

        //Top of the world
        edgeShape.set(0,h,w,h);
        fixtureDefEdgeLeftRightTop.shape = edgeShape;
        this.bodyEdgeScreen.createFixture(fixtureDefEdgeLeftRightTop);

        //Right side of the world
        edgeShape.set(w,0,w,h);
        fixtureDefEdgeLeftRightTop.shape = edgeShape;
        this.bodyEdgeScreen.createFixture(fixtureDefEdgeLeftRightTop);
        edgeShape.dispose();

        //Setup User Data for Collision detection
        this.player.getBody().setUserData(this.player);
        this.checkpoint.getBody().setUserData(this.checkpoint);

        //Hud
        hud = new Hud(game.batch, player);

        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                //Two different bodies in the contact
                Body bodyA = contact.getFixtureA().getBody();
                Body bodyB = contact.getFixtureB().getBody();

                if (bodyA.getUserData().equals(player)) { //If BodyA is player
                    //Check to see what is in contact with BodyA
                    if (bodyB.getUserData().equals(checkpoint)) { //Checkpoint
                        if (!checkpoint.isTriggered()) {
                            checkpoint.setTriggered(true);
                            checkpoint.changeSpawn(player);
                            checkpoint.setTexture(new Texture("checkpoint.png"));
                            player.addLife(1);
                        }
                    }
                }
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
        });

        //Create box2bug render
        this.debugRenderer = new Box2DDebugRenderer();
        this.camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public Player getPlayer() {
        return player;
    }

    public Checkpoint getCheckpoint() {
        return checkpoint;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        //Set position from updated physics
        player.getSprite().setPosition((player.getBody().getPosition().x * PIXELS_TO_METERS) - player.getSprite().getWidth()/2, (player.getBody().getPosition().y * PIXELS_TO_METERS) - player.getSprite().getHeight()/2);
        checkpoint.getSprite().setPosition((checkpoint.getBody().getPosition().x * PIXELS_TO_METERS) - checkpoint.getSprite().getWidth()/2, (checkpoint.getBody().getPosition().y * PIXELS_TO_METERS) - checkpoint.getSprite().getHeight()/2);

        Gdx.gl.glClearColor(1,1,1,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        debugMatrix = batch.getProjectionMatrix().cpy().scale(PIXELS_TO_METERS, PIXELS_TO_METERS, 0);

        batch.begin();
        batch.draw(checkpoint.getSprite(), checkpoint.getSprite().getX(), checkpoint.getSprite().getY(), checkpoint.getSprite().getOriginX(), checkpoint.getSprite().getOriginY(), checkpoint.getSprite().getWidth(), checkpoint.getSprite().getHeight(), checkpoint.getSprite().getScaleX(), checkpoint.getSprite().getScaleY(), checkpoint.getSprite().getRotation());
        batch.draw(player.getSprite(), player.getSprite().getX(), player.getSprite().getY(), player.getSprite().getOriginX(), player.getSprite().getOriginY(), player.getSprite().getWidth(), player.getSprite().getHeight(), player.getSprite().getScaleX(), player.getSprite().getScaleY(), player.getSprite().getRotation());
        batch.end();

        debugRenderer.render(world, debugMatrix);
        world.step(Gdx.graphics.getDeltaTime(), 6, 2);
        batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
        hud.update(delta, player);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        world.dispose();
    }
}
