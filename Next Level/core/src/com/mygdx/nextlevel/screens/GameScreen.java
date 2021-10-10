package com.mygdx.nextlevel.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.nextlevel.NextLevel;
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
    boolean facingRight = true;
    boolean touchedItemBlock = false;
    boolean touchedPowerUp = false;

    float torque = 0.0f;
    boolean drawSprite = true;
    final float PIXELS_TO_METERS = 100f;

    final short PHYSICS_ENTITY = 0x1; //0001
    final short BLOCK_ENTITY = 0x1 << 2; //0100
    final short WORLD_ENTITY = 0x1 << 1; //0010

    boolean landed = true;
    boolean jumped = false;

    public GameScreen(NextLevel game){
        this.batch = game.batch;

        //Physics World
        this.world = new World(new Vector2(0.0F, -40.0F), true);

        //Create Enemy and Player
        Vector2 playerSpawn = new Vector2(-300.0f, 0f);
        Texture playerTexture = new Texture("goomba.png");
        this.player = new Player(playerTexture, this.world, playerSpawn, 0.2f, 0.5f);
        Vector2 enemySpawn = new Vector2(0f, -100.0f);
        final Texture enemyTexture = new Texture("enemy.jpg");
        this.enemy = new Enemy(enemyTexture, this.world, enemySpawn, 100f, 0.5f);
        Vector2 checkpointSpawn = new Vector2(0f, -200.0f);
        final Texture checkpointTexture = new Texture("checkpoint2.jpg");
        this.checkpoint = new Checkpoint(checkpointTexture, this.world, checkpointSpawn,0f, 0f, this.player);
        final Texture blockTexture1 = new Texture("block.png");
        final Texture blockTexture2 = new Texture("item-block.png");
        Vector2 blockSpawn = new Vector2(100f, -100f);
        Vector2 blockSpawn2 = new Vector2(200f, -100f);
        this.block1 = new Block(blockTexture1, this.world, blockSpawn, 100f, 0.5f, true, false);
        this.block2 = new Block(blockTexture2, this.world, blockSpawn2, 100f, 0.5f, false, true);
        final Texture itemTexture = new Texture("mushroom.jpeg");
        Vector2 itemSpawn = new Vector2(200f, 0f);
        this.item = new Item(itemTexture, this.world, itemSpawn, 0f, 0f);

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
        this.checkpoint.getBody().setUserData(this.checkpoint);
        this.block1.getBody().setUserData(this.block1);
        this.block2.getBody().setUserData(this.block2);
        this.item.getBody().setUserData(this.item);

        //Hud
        hud = new Hud(game.batch);

        world.setContactListener(new ContactListener() {
                //(new WorldContactListener(this));
                //(new ContactListener() {
            @Override
            public void beginContact(Contact contact) { //called when two fixtures begin contact
                if (contact.getFixtureA().getBody().getUserData().equals(player)) {
//					System.out.println("Touching object");
                    if (contact.getFixtureB().getBody().getUserData().equals(checkpoint) && !checkpoint.isTriggered()) {
                        checkpoint.setTriggered(true);
                        checkpoint.changeSpawn(player);
                        checkpoint.setTexture(new Texture("checkpoint.png"));
                        player.addLife();
                        System.out.println(player.getLives());
                        return;
                    } else if (contact.getFixtureB().getBody().getUserData().equals(block2) && !touchedItemBlock) {
                        touchedItemBlock = true;
                    } else if (contact.getFixtureB().getBody().getUserData().equals(item) && !touchedPowerUp) {
                        touchedPowerUp = true;
                        player.setPowerUp(true);
                    } else if (contact.getFixtureB().getBody().getUserData().equals(enemy) && player.hasPowerUp()) {
                        System.out.println("Touching enemy and has pwoer up");
                        player.setPowerUp(false);
                    }
//					System.out.println("Touching enemy");
//					System.out.println("Killed enemy");
                }
                if (contact.getFixtureB().getBody().getUserData().equals(player)) {
//					System.out.println("Touching ground");
                    player.getBody().setLinearVelocity(player.getBody().getLinearVelocity().x, 0);
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
                if (contact.getFixtureA().getBody().getUserData().equals(player)) {
                    if (contact.getFixtureB().getBody().getUserData().equals(enemy) || contact.getFixtureB().getBody().getUserData().equals(block1) || contact.getFixtureB().getBody().getUserData().equals(item)) {
                        deleteList.add(contact.getFixtureB().getBody());
//						spriteDelList.add(enemy.getSprite());
                    }
                }
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
//Advance frame
        camera.update();
        world.step(1f/60.0f, 6, 2);

        //Apply Torque
//		player.getBody().applyTorque(torque,true);

        //Set position from updated physics
        player.getSprite().setPosition((player.getBody().getPosition().x * PIXELS_TO_METERS) - player.getSprite().getWidth()/2, (player.getBody().getPosition().y * PIXELS_TO_METERS) - player.getSprite().getHeight()/2);
        enemy.getSprite().setPosition((enemy.getBody().getPosition().x * PIXELS_TO_METERS) - enemy.getSprite().getWidth()/2, (enemy.getBody().getPosition().y * PIXELS_TO_METERS) - enemy.getSprite().getHeight()/2);
        checkpoint.getSprite().setPosition((checkpoint.getBody().getPosition().x * PIXELS_TO_METERS) - checkpoint.getSprite().getWidth()/2, (checkpoint.getBody().getPosition().y * PIXELS_TO_METERS) - checkpoint.getSprite().getHeight()/2);
//		checkpoint.getSprite().setPosition((checkpoint.getBody().getPosition().x * PIXELS_TO_METERS) - checkpoint.getSprite().getWidth()/2, (checkpoint.getBody().getPosition().y * PIXELS_TO_METERS) - checkpoint.getSprite().getHeight()/2);
        block1.getSprite().setPosition((block1.getBody().getPosition().x * PIXELS_TO_METERS) - block1.getSprite().getWidth()/2, (block1.getBody().getPosition().y * PIXELS_TO_METERS) - block1.getSprite().getHeight()/2);
        block2.getSprite().setPosition((block2.getBody().getPosition().x * PIXELS_TO_METERS) - block2.getSprite().getWidth()/2, (block2.getBody().getPosition().y * PIXELS_TO_METERS) - block2.getSprite().getHeight()/2);
        item.getSprite().setPosition((item.getBody().getPosition().x * PIXELS_TO_METERS) - item.getSprite().getWidth()/2, (item.getBody().getPosition().y * PIXELS_TO_METERS) - item.getSprite().getHeight()/2);

        Gdx.gl.glClearColor(1,1,1,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        debugMatrix = batch.getProjectionMatrix().cpy().scale(PIXELS_TO_METERS, PIXELS_TO_METERS, 0);

        batch.begin();
        if (drawSprite) {
            batch.draw(enemy.getSprite(), enemy.getSprite().getX(), enemy.getSprite().getY(), enemy.getSprite().getOriginX(), enemy.getSprite().getOriginY(), enemy.getSprite().getWidth(), enemy.getSprite().getHeight(), enemy.getSprite().getScaleX(), enemy.getSprite().getScaleY(), enemy.getSprite().getRotation());
            batch.draw(checkpoint.getSprite(), checkpoint.getSprite().getX(), checkpoint.getSprite().getY(), checkpoint.getSprite().getOriginX(), checkpoint.getSprite().getOriginY(), checkpoint.getSprite().getWidth(), checkpoint.getSprite().getHeight(), checkpoint.getSprite().getScaleX(), checkpoint.getSprite().getScaleY(), checkpoint.getSprite().getRotation());
            if (touchedPowerUp && player.hasPowerUp()) {
                player.setTexture(new Texture("paragoomba.png"));
                batch.draw(player.getSprite(), player.getSprite().getX(), player.getSprite().getY(), player.getSprite().getOriginX(), player.getSprite().getOriginY(), player.getSprite().getWidth(), player.getSprite().getHeight(), player.getSprite().getScaleX(), player.getSprite().getScaleY(), player.getSprite().getRotation());
            } else {
                player.setTexture(new Texture("goomba.png"));
                batch.draw(player.getSprite(), player.getSprite().getX(), player.getSprite().getY(), player.getSprite().getOriginX(), player.getSprite().getOriginY(), player.getSprite().getWidth(), player.getSprite().getHeight(), player.getSprite().getScaleX(), player.getSprite().getScaleY(), player.getSprite().getRotation());
            }
            batch.draw(block1.getSprite(), block1.getSprite().getX(), block1.getSprite().getY(), block1.getSprite().getOriginX(), block1.getSprite().getOriginY(), block1.getSprite().getWidth(), block1.getSprite().getHeight(), block1.getSprite().getScaleX(), block1.getSprite().getScaleY(), block1.getSprite().getRotation());
            batch.draw(block2.getSprite(), block2.getSprite().getX(), block2.getSprite().getY(), block2.getSprite().getOriginX(), block2.getSprite().getOriginY(), block2.getSprite().getWidth(), block2.getSprite().getHeight(), block2.getSprite().getScaleX(), block2.getSprite().getScaleY(), block2.getSprite().getRotation());
            if (touchedItemBlock) {
                batch.draw(item.getSprite(), item.getSprite().getX(), item.getSprite().getY(), item.getSprite().getOriginX(), item.getSprite().getOriginY(), item.getSprite().getWidth(), item.getSprite().getHeight(), item.getSprite().getScaleX(), item.getSprite().getScaleY(), item.getSprite().getRotation());
                block2.setSpawned(true);
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
        enemy.movement();

        debugRenderer.render(world, debugMatrix);
        if (deleteList.size() > 0) {
            for (int i = 0; i < deleteList.size(); i++) {
                world.destroyBody(deleteList.get(i));
                deleteList.remove(i);
            }
        }

        batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
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

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.UP && this.landed && !this.jumped) {
            Vector2 force = new Vector2(0f, 65f);
            this.player.getBody().applyForceToCenter(force, true);
            this.jumped = true;
            this.landed = false;
        }

        if (keycode == Input.Keys.ESCAPE) {
            drawSprite = !drawSprite;
        }

        //Simulate Player Death
        if (keycode == Input.Keys.SPACE) {
            player.subLife();
            System.out.println(player.getLives());
            if (this.player.getLives() < 1) {
                player.addLife();
                player.addLife();
                player.addLife();

                player.getBody().setTransform(this.player.getWorldSpawn().x/PIXELS_TO_METERS, this.player.getWorldSpawn().y/PIXELS_TO_METERS, 0);
                player.setSpawnpoint(player.getWorldSpawn());
                checkpoint.setTexture(new Texture("checkpoint2.jpg"));
                checkpoint.setTriggered(false);
            } else {
                player.getBody().setTransform(this.player.getSpawnpoint().x/PIXELS_TO_METERS, this.player.getSpawnpoint().y/PIXELS_TO_METERS, 0);

            }
            player.getBody().setLinearVelocity(0, 0);
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


