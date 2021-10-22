package com.mygdx.nextlevel.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.nextlevel.NextLevel;
import com.mygdx.nextlevel.Tiles.TileMapTest;
import com.mygdx.nextlevel.WorldContactListener;
import com.mygdx.nextlevel.actors.*;
import com.mygdx.nextlevel.hud.Hud;

import java.util.ArrayList;

public class GameScreen implements Screen, InputProcessor {
    SpriteBatch batch;
    Player player;
    Enemy enemy;
    Checkpoint checkpoint;
    Block block1, block2;
    Item item;
    Hud hud;

    World world;
    Body bodyEdgeScreen;

    Box2DDebugRenderer debugRenderer;
    Matrix4 debugMatrix;
    OrthographicCamera camera;
    ArrayList<Body> deleteList = new ArrayList<>();
    ArrayList<Sprite> spriteDelList = new ArrayList<>();
    boolean destroyItem = false;
    boolean facingRight = true;
    boolean touchedItemBlock = false;
    boolean touchedPowerUp = false;
    float time = 0;
//    float invTime = 0;
    boolean itemConsumed = false;
    boolean playerKilled = false;
    boolean itemSpawned = false;
    boolean killEnemy = false;
    boolean breakBlock = false;

    float torque = 0.0f;
    boolean drawSprite = true;
    final float PIXELS_TO_METERS = 100f;

    final short PHYSICS_ENTITY = 0x1; //0001
    final short BLOCK_ENTITY = 0x1 << 2; //0100
    final short WORLD_ENTITY = 0x1 << 1; //0010

    //Directional Collisions
    final int bottom = 1;
    final int left = 2;
    final int top = 3;
    final int right = 4;

    boolean landed = true;
    boolean jumped = false;

    //public TileMapTest groundT;

    public GameScreen(NextLevel game){
        this.batch = game.batch;

        //Physics World
        this.world = new World(new Vector2(0.0F, -9.8F), true);

        //Create Enemy and Player
//        float w = Gdx.graphics.getWidth();
        float w = Gdx.graphics.getWidth() * 1.25f;
        float h = Gdx.graphics.getHeight();

        //groundT = new TileMapTest(this.world);
        
        //Player Initialization
        final Vector2 playerSpawn = new Vector2(32, 32);
        this.player = new Player(new Texture("goomba.png"), this.world, playerSpawn, 0.25f, 0f);

        //Enemy Initialization
        Vector2 enemySpawn = new Vector2(w * 0.95f, 32);
        this.enemy = new Enemy(new Texture("enemy.jpg"), this.world, enemySpawn, 100f, 0f);

        //Checkpoint Initialization
        Vector2 checkpointSpawn = new Vector2(w/2, 32);
        this.checkpoint = new Checkpoint(new Texture("checkpoint2.jpg"), this.world, checkpointSpawn,0f, 0f, this.player);

        //Block1 Initialization (Brick Block)
        Vector2 blockSpawn = new Vector2(w * 0.625f, 32 + 2*64);
        this.block1 = new Block(new Texture("block.png"), this.world, blockSpawn, 100f, 0f, true, false);

        //Block2 Initialization (Item Block)
        Vector2 blockSpawn2 = new Vector2(w * 0.75f, 32 + 2*64);
        this.block2 = new Block(new Texture("item-block.png"), this.world, blockSpawn2, 100f, 0f, false, true);

        //Item Initialization (Item)
        Vector2 itemSpawn = new Vector2(w * 0.75f, 32 + 64*3);
        this.item = new Item(new Texture("mushroom.jpeg"), this.world, itemSpawn, 0f, 0f);


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
        Gdx.input.setInputProcessor(this);
        this.player.getBody().setUserData(this.player);
        this.enemy.getBody().setUserData(this.enemy);
        this.checkpoint.getBody().setUserData(this.checkpoint);
        this.block1.getBody().setUserData(this.block1);
        this.block2.getBody().setUserData(this.block2);
        this.item.getBody().setUserData(this.item);
        this.bodyEdgeScreen.setUserData(this.bodyEdgeScreen);

        //TODO Causes an Error if item loaded after block hit
//        if (touchedItemBlock) {
//            final Texture itemTexture = new Texture("mushroom.jpeg");
//            Vector2 itemSpawn = new Vector2((w * PIXELS_TO_METERS/2) * 0.5f, -h * PIXELS_TO_METERS/2 + 250f);
//            this.item = new Item(itemTexture, this.world, itemSpawn, 0f, 0f);
//            this.item.getBody().setUserData(this.item);
//        }

        //Hud
        hud = new Hud(game.batch, player);

        //Collisions between bodies
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
                    } else if (bodyB.getUserData().equals(block2) && !touchedItemBlock) { //Item Block
                        if (bodyA.getFixtureList().get(top).equals(contact.getFixtureA())) { //Check if Contact on Top Side of player
                            touchedItemBlock = true;
                        }
                    } else if (bodyB.getUserData().equals(item) && !touchedPowerUp) { //Item
                        touchedPowerUp = true;
                        player.setPowerUp(true);
                        item.setDeleteSprite(true);
                        itemConsumed = true;
                    } else if (bodyB.getUserData().equals(enemy) && !player.getsInvulnerable()) { //Enemy
                        if (bodyA.getFixtureList().get(bottom).equals(contact.getFixtureA())) { //Check if Contact on Bottom of player
                            deleteList.add(bodyB);
                            enemy.setDeleteSprite(true);
                        } else if (bodyA.getFixtureList().get(left).equals(contact.getFixtureA())) { //Check if Contact on Left of player
                            if (player.hasPowerUp()) {
                                player.setPowerUp(false);
                                invulnerableTimer();
                            } else {
                                player.subLife();
                                playerKilled = true;
                            }
                        } else if (bodyA.getFixtureList().get(top).equals(contact.getFixtureA())) { //Check if Contact on Top of player
                            if (player.hasPowerUp()) {
                                player.setPowerUp(false);
                                invulnerableTimer();
                            } else {
                                player.subLife();
                                playerKilled = true;
                            }
                        } else if (bodyA.getFixtureList().get(right).equals(contact.getFixtureA())) { //Check if Contact on Right of Player
                            if (player.hasPowerUp()) {
                                player.setPowerUp(false);
                                invulnerableTimer();
                            } else {
                                player.subLife();
                                playerKilled = true;
                            }
                        }
                    } else if (bodyB.getUserData().equals(block1)) { //Breakable block
                        if (bodyA.getFixtureList().get(top).equals(contact.getFixtureA())) {
                            deleteList.add(bodyB);
                            block1.setDeleteSprite(true);
                        }
                    }
                } else if (bodyB.getUserData().equals(player)) { //If BodyB is player
                    if (bodyA.getUserData().equals(bodyEdgeScreen)) {
                        //Contact with the Sides of the Screen
                    }
                }
                landed = true;
                jumped = false;
            }

            @Override
            public void endContact(Contact contact) { //called when two fixtures stop contact
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

    public void killEnemy(Contact contact) {
        if (contact.getFixtureB().getBody().getUserData().equals(enemy) && player.getBody().getFixtureList().get(1).equals(contact.getFixtureA())) {
            deleteList.add(contact.getFixtureB().getBody());
        } else {
            deleteList.add(contact.getFixtureA().getBody());
        }
    }

    public Player getPlayer() {
        return player;
    }

    public Checkpoint getCheckpoint() {
        return checkpoint;
    }

    public Block getBlock2() {
        return block2;
    }

    public Block getBlock1() {
        return block1;
    }

    public Item getItem() {
        return item;
    }

    public Enemy getEnemy() {
        return enemy;
    }

    public ArrayList<Body> getDeleteList() {
        return deleteList;
    }

    public boolean isTouchedItemBlock() {
        return touchedItemBlock;
    }

    public boolean isLanded() {
        return landed;
    }

    public boolean isJumped() {
        return jumped;
    }

    public boolean isTouchedPowerUp() {
        return touchedPowerUp;
    }

    public void show() {
        Gdx.input.setInputProcessor(this);
    }

    public void hide() {

    }



    public void render(float delta) {
        //Camera Setup
        float width = Gdx.graphics.getWidth() * 1.25f;
        if (player.getSprite().getX() < Gdx.graphics.getWidth()/2f) {
            camera.position.x = Gdx.graphics.getWidth()/2f;
        } else if (width - player.getSprite().getX() < Gdx.graphics.getWidth()/2f) {
            camera.position.x = width - Gdx.graphics.getWidth()/2f;
        } else {
            camera.position.x = player.getSprite().getX();
        }
//        camera.position.x = player.getSprite().getX();
        camera.position.y = Gdx.graphics.getHeight()/2f;
        camera.update();

        //Set position from updated physics
        player.getSprite().setPosition((player.getBody().getPosition().x * PIXELS_TO_METERS) - player.getSprite().getWidth()/2, (player.getBody().getPosition().y * PIXELS_TO_METERS) - player.getSprite().getHeight()/2);
        enemy.getSprite().setPosition((enemy.getBody().getPosition().x * PIXELS_TO_METERS) - enemy.getSprite().getWidth()/2, (enemy.getBody().getPosition().y * PIXELS_TO_METERS) - enemy.getSprite().getHeight()/2);

        //testing new tile code
        //groundT.groundTile.setPosition(groundT.groundTileWorldBody.getPosition().x, groundT.groundTileWorldBody.getPosition().y);

        checkpoint.getSprite().setPosition((checkpoint.getBody().getPosition().x * PIXELS_TO_METERS) - checkpoint.getSprite().getWidth()/2, (checkpoint.getBody().getPosition().y * PIXELS_TO_METERS) - checkpoint.getSprite().getHeight()/2);
        block1.getSprite().setPosition((block1.getBody().getPosition().x * PIXELS_TO_METERS) - block1.getSprite().getWidth()/2, (block1.getBody().getPosition().y * PIXELS_TO_METERS) - block1.getSprite().getHeight()/2);
        block2.getSprite().setPosition((block2.getBody().getPosition().x * PIXELS_TO_METERS) - block2.getSprite().getWidth()/2, (block2.getBody().getPosition().y * PIXELS_TO_METERS) - block2.getSprite().getHeight()/2);

//        if (touchedItemBlock && !itemConsumed) {
//            final Texture itemTexture = new Texture("mushroom.jpeg");
//            Vector2 itemSpawn = new Vector2(200f, 0f);
//            this.item = new Item(itemTexture, this.world, itemSpawn, 0f, 0f);
//            this.item.getBody().setUserData(this.item);
//            item.getSprite().setPosition((item.getBody().getPosition().x * PIXELS_TO_METERS) - item.getSprite().getWidth() / 2, (item.getBody().getPosition().y * PIXELS_TO_METERS) - item.getSprite().getHeight() / 2);
//        }
        if (touchedItemBlock && !itemConsumed) {
            item.getSprite().setPosition((item.getBody().getPosition().x * PIXELS_TO_METERS) - item.getSprite().getWidth() / 2, (item.getBody().getPosition().y * PIXELS_TO_METERS) - item.getSprite().getHeight() / 2);
        }

        Gdx.gl.glClearColor(1,1,1,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        debugMatrix = batch.getProjectionMatrix().cpy().scale(PIXELS_TO_METERS, PIXELS_TO_METERS, 0);

        batch.begin();
        batch.draw(new Texture("tempBack.png"),0, 0);
        if (drawSprite) {
            if (!enemy.getDeleteSprite()) {
                batch.draw(enemy.getSprite(), enemy.getSprite().getX(), enemy.getSprite().getY(), enemy.getSprite().getOriginX(), enemy.getSprite().getOriginY(), enemy.getSprite().getWidth(), enemy.getSprite().getHeight(), enemy.getSprite().getScaleX(), enemy.getSprite().getScaleY(), enemy.getSprite().getRotation());
            }

            batch.draw(checkpoint.getSprite(), checkpoint.getSprite().getX(), checkpoint.getSprite().getY(), checkpoint.getSprite().getOriginX(), checkpoint.getSprite().getOriginY(), checkpoint.getSprite().getWidth(), checkpoint.getSprite().getHeight(), checkpoint.getSprite().getScaleX(), checkpoint.getSprite().getScaleY(), checkpoint.getSprite().getRotation());
            if (!player.getDeleteSprite()) {
                if (touchedPowerUp && player.hasPowerUp()) {
                    player.setTexture(new Texture("paragoomba.png"));
                    batch.draw(player.getSprite(), player.getSprite().getX(), player.getSprite().getY(), player.getSprite().getOriginX(), player.getSprite().getOriginY(), player.getSprite().getWidth(), player.getSprite().getHeight(), player.getSprite().getScaleX(), player.getSprite().getScaleY(), player.getSprite().getRotation());
                } else {
                    player.setTexture(new Texture("goomba.png"));
                    batch.draw(player.getSprite(), player.getSprite().getX(), player.getSprite().getY(), player.getSprite().getOriginX(), player.getSprite().getOriginY(), player.getSprite().getWidth(), player.getSprite().getHeight(), player.getSprite().getScaleX(), player.getSprite().getScaleY(), player.getSprite().getRotation());
                }
            }
            if (!block1.getDeleteSprite()) {
                batch.draw(block1.getSprite(), block1.getSprite().getX(), block1.getSprite().getY(), block1.getSprite().getOriginX(), block1.getSprite().getOriginY(), block1.getSprite().getWidth(), block1.getSprite().getHeight(), block1.getSprite().getScaleX(), block1.getSprite().getScaleY(), block1.getSprite().getRotation());
            }
            batch.draw(block2.getSprite(), block2.getSprite().getX(), block2.getSprite().getY(), block2.getSprite().getOriginX(), block2.getSprite().getOriginY(), block2.getSprite().getWidth(), block2.getSprite().getHeight(), block2.getSprite().getScaleX(), block2.getSprite().getScaleY(), block2.getSprite().getRotation());
            if (touchedItemBlock) {
                if (!item.getDeleteSprite()) {
//                    this.item.setBody(BodyDef.BodyType.StaticBody);
//                    this.item.setShape();
//                    this.item.setFixture(0f,0f);
//                    this.item.getBody().setUserData(this.item);
//                    if (touchedItemBlock && !itemConsumed) {
//                        item.getSprite().setPosition((item.getBody().getPosition().x * PIXELS_TO_METERS) - item.getSprite().getWidth() / 2, (item.getBody().getPosition().y * PIXELS_TO_METERS) - item.getSprite().getHeight() / 2);
//                    }
                    batch.draw(item.getSprite(), item.getSprite().getX(), item.getSprite().getY(), item.getSprite().getOriginX(), item.getSprite().getOriginY(), item.getSprite().getWidth(), item.getSprite().getHeight(), item.getSprite().getScaleX(), item.getSprite().getScaleY(), item.getSprite().getRotation());
                    block2.setSpawned(true);
                }
            }
        }

        batch.end();

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            player.getBody().setLinearVelocity(3f, player.getBody().getLinearVelocity().y);
            if (!facingRight) {
                facingRight = true;
                player.getSprite().flip(true, false);
            }

        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            player.getBody().setLinearVelocity(-3f, player.getBody().getLinearVelocity().y);
            if (facingRight) {
                facingRight = false;
                player.getSprite().flip(true, false);
            }
        }
        time += Gdx.graphics.getDeltaTime();
        if (time % 10 < 5) {
            enemy.moveLeft();
        } else if (time % 10 >= 5) {
            enemy.moveRight();
        }

        debugRenderer.render(world, debugMatrix);
        if (deleteList.size() > 0) {
            for (int i = 0; i < deleteList.size(); i++) {
                world.destroyBody(deleteList.get(i));
                deleteList.remove(i);
            }
        }

        //Player killed
        if (playerKilled) {
            player.death(checkpoint);
            playerKilled = false;
        }

        world.step(Gdx.graphics.getDeltaTime(), 6, 2);
        batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
        hud.update(delta, player);
    }

    public void resize(int width, int height) {

    }

    public void pause() {

    }

    public void resume() {

    }

    public void dispose() {
        batch.dispose();
        world.dispose();
    }

    private void invulnerableTimer() {
        float invTime = 0;
        player.setInvulnerable(true);
        while (invTime < 5) {
            invTime += Gdx.graphics.getDeltaTime();
        }
        player.setInvulnerable(false);

    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.UP && this.landed && !this.jumped) {
            float impulse = this.player.getBody().getMass() * 6.5f;
            this.player.getBody().applyLinearImpulse(new Vector2(0, impulse), this.player.getBody().getWorldCenter(), true);
            this.jumped = true;
            this.landed = false;
        }

        if (keycode == Input.Keys.ESCAPE) {
            drawSprite = !drawSprite;
        }
        return true;
    }

    @Override
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


