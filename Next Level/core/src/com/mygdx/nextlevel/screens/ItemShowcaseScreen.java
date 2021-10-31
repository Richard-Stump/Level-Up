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
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.nextlevel.NextLevel;
import com.mygdx.nextlevel.Tiles.TileMapTest;
import com.mygdx.nextlevel.WorldContactListener;
import com.mygdx.nextlevel.actors.*;
import com.mygdx.nextlevel.hud.Hud;
import jdk.nashorn.internal.runtime.arrays.ArrayLikeIterator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class ItemShowcaseScreen implements Screen, InputProcessor {
    SpriteBatch batch;
    Player player;
    Enemy enemy;
    Block block, mushroomBlock, slowItemBlock, speedItemBlock, starBlock, fireflowerBlock, oneUpItemBlock, lifeStealBlock;
    Hud hud;

    //Items
    Item item, mushroom, slowItem, speedItem, star, fireflower, oneUpItem, lifeStealItem;
    int itemIndex;

    //Sprite Directions
    boolean fireballRight = true;

    float shootTimer = 0f;
    Texture fireballTexture;
    Sprite fireballSprite;
    ArrayList<Fire> fireballList;
    HashMap<Item, String> itemToName = new HashMap<>();

    World world;
    Body bodyEdgeScreen;

    Box2DDebugRenderer debugRenderer;
    Matrix4 debugMatrix;
    OrthographicCamera camera;
    ArrayList<Body> deleteList = new ArrayList<>();
    ArrayList<Item> itemList = new ArrayList<>();
    ArrayList<Block> blockList = new ArrayList<>();
    ArrayList<Object> blockListUserData = new ArrayList<>();
    ArrayList<Item> itemListGame = new ArrayList<>();


    boolean destroyItem = false;
    boolean facingRight = true;
    boolean touchedItemBlock = false;
    boolean touchedPowerUp = false;
    boolean touchedItemBlock2 = false;
    float time = 0;
    float speedTime = 0f;
    //    float invTime = 0;
    boolean itemConsumed = false;
    boolean playerKilled = false;
    //    boolean itemSpawned = false;
//    boolean killEnemy = false;
//    boolean breakBlock = false;
    float speedRight = 3f;
    float speedLeft = -3f;
    float slowTime = 0f;
    float starTime = 0f;
    float lifeStealTime = 0f;

    float torque = 0.0f;
    boolean drawSprite = true;
    final float PIXELS_TO_METERS = 100f;

    final short PHYSICS_ENTITY = 0x1; //0001
    final short BLOCK_ENTITY = 0x1 << 2; //0100
    final short WORLD_ENTITY = 0x1 << 1; //0010

    //Directional Collisions
    final short bottom = 1;
    final short left = 2;
    final short top = 3;
    final short right = 4;

    boolean landed = true;
    boolean jumped = false;

    //public TileMapTest groundT;
    public ItemShowcaseScreen() {}

    public ItemShowcaseScreen(NextLevel game){
        this.batch = game.batch;

        //Physics World
        this.world = new World(new Vector2(0.0F, -9.8F), true);

        //Create Enemy and Player
        float w = Gdx.graphics.getWidth() * 1.25f;
        float h = Gdx.graphics.getHeight();

        //groundT = new TileMapTest(this.world);

        //Player Initialization
        final Vector2 playerSpawn = new Vector2(32, 32);
        this.player = new Player(new Texture("goomba.png"), this.world, playerSpawn, 0.25f, 0f);

        //Enemy Initialization
        Vector2 enemySpawn = new Vector2(w * 0.95f, 32);
        this.enemy = new Enemy(new Texture("enemy.jpg"), this.world, enemySpawn, 100f, 0f);


        //Mushroom Block
        Vector2 mushroomBlockSpawn = new Vector2(w * 0.1f, 32 + 2*64);
        this.mushroomBlock = new Block(new Texture("item-block.png"), this.world, mushroomBlockSpawn, 10f, 0f, (short) (0x1 << (bottom - 1)), true);
        blockList.add(mushroomBlock);
        blockListUserData.add(mushroomBlock.getBody().getUserData());

        //Slow Item Block
        Vector2 slowItemBlockSpawn = new Vector2(w * 0.23f, 32 + 2*64);
        this.slowItemBlock = new Block(new Texture("item-block.png"), this.world, slowItemBlockSpawn, 10f, 0f, (short) (0x1 << (bottom - 1)), true);
        blockList.add(slowItemBlock);
        blockListUserData.add(slowItemBlock.getBody().getUserData());

        //Speed Item Block
        Vector2 speedItemBlockSpawn = new Vector2(w * 0.36f, 32 + 2*64);
        this.speedItemBlock = new Block(new Texture("item-block.png"), this.world, speedItemBlockSpawn, 10f, 0f, (short) (0x1 << (bottom - 1)), true);
        blockList.add(speedItemBlock);
        blockListUserData.add(speedItemBlock.getBody().getUserData());

        //Star Block
        Vector2 starBlockSpawn = new Vector2(w * 0.49f, 32 + 2*64);
        this.starBlock = new Block(new Texture("item-block.png"), this.world, starBlockSpawn, 10f, 0f, (short) (0x1 << (bottom - 1)), true);
        blockList.add(starBlock);
        blockListUserData.add(starBlock.getBody().getUserData());

        //Fireflower Block
//        Vector2 fireflowerBlockSpawn = new Vector2(w * 0.62f, 32 + 2*64);
//        this.fireflowerBlock = new Block(new Texture("item-block.png"), this.world, fireflowerBlockSpawn, 10f, 0f, (short) (0x1 << (bottom - 1)), true);
//        blockList.add(fireflowerBlock);
//        blockListUserData.add(fireflowerBlock.getBody().getUserData());

        Vector2 lifeStealBlockSpawn = new Vector2(w * 0.62f, 32 + 2*64);
        this.lifeStealBlock = new Block(new Texture("item-block.png"), this.world, lifeStealBlockSpawn, 10f, 0f, (short) (0x1 << (bottom - 1)), true);
        blockList.add(lifeStealBlock);
        blockListUserData.add(lifeStealBlock.getBody().getUserData());

        //One-Up Block
        Vector2 oneUpItemBlockSpawn = new Vector2(w * 0.75f, 32 + 2*64);
        this.oneUpItemBlock = new Block(new Texture("item-block.png"), this.world, oneUpItemBlockSpawn, 10f, 0f, (short) (0x1 << (bottom - 1)), true);
        blockList.add(oneUpItemBlock);
        blockListUserData.add(oneUpItemBlock.getBody().getUserData());

        //Item Initialization (Item)
        Vector2 mushroomSpawn = new Vector2(w * 0.1f, 32 + 64*3);
        this.mushroom = new Item(new Texture("mushroom.jpeg"), this.world, mushroomSpawn, 0f, 0f);
        itemToName.put(mushroom, "mushroom.jpeg");
        itemList.add(mushroom);

        //Slow Item Initialization
        Vector2 slowItemSpawn = new Vector2(w * 0.23f, -32 + 64*4);
        this.slowItem = new Item(new Texture("slow-mushroom.png"), this.world, slowItemSpawn, 0f, 0f);
        itemToName.put(slowItem, "slow-mushroom.png");
        itemList.add(slowItem);

        //Speed Item Initialization
        Vector2 speedItemSpawn = new Vector2(w * 0.36f, -32 + 64*4);
        this.speedItem = new Item(new Texture("speeditem.png"), this.world, speedItemSpawn, 0f, 0f);
        itemToName.put(speedItem, "speeditem.png");
        itemList.add(speedItem);


        //Star item initialization
        Vector2 starSpawn = new Vector2(w * 0.49f, -32 + 64*4);
        this.star = new Item(new Texture("star.jpg"), this.world, starSpawn, 0f, 0f);
        itemToName.put(star, "star.jpg");
        itemList.add(star);


        //Fireflower Intiialization
//        Vector2 fireFlowerSpawn = new Vector2(w * 0.62f, -32 + 64*4);
//        this.fireflower = new Item(new Texture("fireflower.png"), this.world, fireFlowerSpawn, 0f, 0f);
//        itemToName.put(fireflower, "fireflower.png");
//        itemList.add(fireflower);

        Vector2 lifeStealSpawn = new Vector2(w * 0.62f, -32 + 64*4);
        this.lifeStealItem = new Item(new Texture("lifesteal-mushroom.png"), this.world, lifeStealSpawn, 0f, 0f);
        itemToName.put(lifeStealItem, "lifesteal-mushroom.png");
        itemList.add(lifeStealItem);

        //One Up Initialization
        Vector2 oneUpSpawn = new Vector2(w * 0.75f, -32 + 64*4);
        this.oneUpItem = new Item(new Texture("1up-mushroom.jpeg"), this.world, oneUpSpawn, 0f, 0f);
        itemToName.put(oneUpItem, "1up-mushroom.jpeg");
        itemList.add(oneUpItem);

        mushroomBlock.setItem(mushroom);
        slowItemBlock.setItem(slowItem);
        speedItemBlock.setItem(speedItem);
        starBlock.setItem(star);
//        fireflowerBlock.setItem(fireflower);
        lifeStealBlock.setItem(lifeStealItem);
        oneUpItemBlock.setItem(oneUpItem);

        fireballList = new ArrayList<>();

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
        this.bodyEdgeScreen.setUserData(this.bodyEdgeScreen);

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
                    if (blockListUserData.contains(bodyB.getUserData())) { //Block has been hit
                        block = blockList.get(blockListUserData.indexOf(bodyB.getUserData()));
                        if (block.getCollision() != 0b0000) { //block does something on collisions
                            if (!block.isItemBlock()) { //Breakable Block
                                if (block.getCollision() == 0b0001 && bodyA.getFixtureList().get(top).equals(contact.getFixtureA())) {
                                    deleteList.add(bodyB);
                                    block.setDeleteSprite(true);
                                    blockList.remove(blockListUserData.indexOf(bodyB.getUserData()));
                                    blockListUserData.remove(bodyB.getUserData());
                                }
                            } else { //Item Block
                                if (block.getCollision() == 0b0001 && bodyA.getFixtureList().get(top).equals(contact.getFixtureA())) { //Bottom of the Block
                                    touchedItemBlock = true;
//                                    Random rand = new Random();
//                                    itemIndex = rand.nextInt(itemList.size()); //Get Random Item Block will generate
//                                    block.setItem(itemList.get(itemIndex));
                                }
                            }
                        }
                    } else if (itemList.contains(bodyB.getUserData())) {
                        item = (Item) bodyB.getUserData();
                        if (item.equals(slowItem)) {
                            player.setSlowItem(true);
                        } else if (item.equals(speedItem)) {
                            player.setSpeedItem(true);
                        } else if (item.equals(oneUpItem)) {
                            player.setOneUpItem(true);
                        } else if (item.equals(star)) {
                            player.setHeldItem(star);
                        }
//                        else if (item.equals(fireflower)) {
//                            player.setFireflower(true);
//                            player.setPowerUp(true);
//                            itemConsumed = true;
//                        }
                        else if (item.equals(lifeStealItem)) {
                            player.setHeldItem(lifeStealItem);
                        } else if (item.equals(mushroom)) {
                            touchedPowerUp = true;
                            player.setPowerUp(true);
                            player.setMushroom(true);
                        }
                        deleteList.add(bodyB);
                        item.setDeleteSprite(true);
                        itemListGame.remove(item);
                    } else if (bodyB.getUserData().equals(enemy) && !player.getsInvulnerable()) { //Enemy
                        if (player.getStar()) { //Player has star item
                            deleteList.add(bodyB);
                            enemy.setDeleteSprite(true);
                        } else if (!player.getsInvulnerable()) { //Player isn't invulnerable
                            if (bodyA.getFixtureList().get(bottom).equals(contact.getFixtureA())) { //Check if Contact on Bottom of player
                                deleteList.add(bodyB);
                                enemy.setDeleteSprite(true);
                            } else if (bodyA.getFixtureList().get(left).equals(contact.getFixtureA())) { //Check if Contact on Left of player
                                if (player.hasPowerUp()) {
                                    player.setPowerUp(false);
//                                    player.setFireflower(false);
                                    player.setLifeStealItem(false);
                                    player.setMushroom(false);
                                    invulnerableTimer();
                                } else {
                                    player.subLife();
                                    playerKilled = true;
                                }
                            } else if (bodyA.getFixtureList().get(top).equals(contact.getFixtureA())) { //Check if Contact on Top of player
                                if (player.hasPowerUp()) {
                                    player.setPowerUp(false);
//                                    player.setFireflower(false);
                                    player.setLifeStealItem(false);
                                    player.setMushroom(false);
                                    invulnerableTimer();
                                } else {
                                    player.subLife();
                                    playerKilled = true;
                                }
                            } else if (bodyA.getFixtureList().get(right).equals(contact.getFixtureA())) { //Check if Contact on Right of Player
                                if (player.hasPowerUp()) {
                                    player.setPowerUp(false);
//                                    player.setFireflower(false);
                                    player.setLifeStealItem(false);
                                    player.setMushroom(false);
                                    invulnerableTimer();
                                } else {
                                    player.subLife();
                                    playerKilled = true;
                                }
                            }
                        }
                    }
                } else if (bodyB.getUserData().equals(player)) { //If BodyB is player
                    if (bodyA.getUserData().equals(bodyEdgeScreen)) {
                        //Contact with the Sides of the Screen
                    }
                }
                //TODO need to change this
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

    public Player getPlayer() {
        return player;
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
        camera.position.y = Gdx.graphics.getHeight()/2f;
        camera.update();
//        fireball.update();

        //Set position from updated physics
        player.getSprite().setPosition((player.getBody().getPosition().x * PIXELS_TO_METERS) - player.getSprite().getWidth()/2, (player.getBody().getPosition().y * PIXELS_TO_METERS) - player.getSprite().getHeight()/2);
        enemy.getSprite().setPosition((enemy.getBody().getPosition().x * PIXELS_TO_METERS) - enemy.getSprite().getWidth()/2, (enemy.getBody().getPosition().y * PIXELS_TO_METERS) - enemy.getSprite().getHeight()/2);
        mushroomBlock.getSprite().setPosition((mushroomBlock.getBody().getPosition().x * PIXELS_TO_METERS) - mushroomBlock.getSprite().getWidth()/2, (mushroomBlock.getBody().getPosition().y * PIXELS_TO_METERS) - mushroomBlock.getSprite().getHeight()/2);
        slowItemBlock.getSprite().setPosition((slowItemBlock.getBody().getPosition().x * PIXELS_TO_METERS) - slowItemBlock.getSprite().getWidth()/2, (slowItemBlock.getBody().getPosition().y * PIXELS_TO_METERS) - slowItemBlock.getSprite().getHeight()/2);
        speedItemBlock.getSprite().setPosition((speedItemBlock.getBody().getPosition().x * PIXELS_TO_METERS) - speedItemBlock.getSprite().getWidth()/2, (speedItemBlock.getBody().getPosition().y * PIXELS_TO_METERS) - speedItemBlock.getSprite().getHeight()/2);
        starBlock.getSprite().setPosition((starBlock.getBody().getPosition().x * PIXELS_TO_METERS) - starBlock.getSprite().getWidth()/2, (starBlock.getBody().getPosition().y * PIXELS_TO_METERS) - starBlock.getSprite().getHeight()/2);
//        fireflowerBlock.getSprite().setPosition((fireflowerBlock.getBody().getPosition().x * PIXELS_TO_METERS) - fireflowerBlock.getSprite().getWidth()/2, (fireflowerBlock.getBody().getPosition().y * PIXELS_TO_METERS) - fireflowerBlock.getSprite().getHeight()/2);
        lifeStealBlock.getSprite().setPosition((lifeStealBlock.getBody().getPosition().x * PIXELS_TO_METERS) - lifeStealBlock.getSprite().getWidth()/2, (lifeStealBlock.getBody().getPosition().y * PIXELS_TO_METERS) - lifeStealBlock.getSprite().getHeight()/2);
        oneUpItemBlock.getSprite().setPosition((oneUpItemBlock.getBody().getPosition().x * PIXELS_TO_METERS) - oneUpItemBlock.getSprite().getWidth()/2, (oneUpItemBlock.getBody().getPosition().y * PIXELS_TO_METERS) - oneUpItemBlock.getSprite().getHeight()/2);

        Gdx.gl.glClearColor(1,1,1,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        debugMatrix = batch.getProjectionMatrix().cpy().scale(PIXELS_TO_METERS, PIXELS_TO_METERS, 0);

        ArrayList<Fire> fireToRemove = new ArrayList<>();
        for (Fire f : fireballList) {
            f.update();
            if (f.remove) {
                fireToRemove.add(f);
            }
        }
        fireballList.removeAll(fireToRemove);

        batch.begin();
        batch.draw(new Texture("tempBack.png"),0, 0);
        if (drawSprite) {
            //Draw Enemy
            if (!enemy.getDeleteSprite()) {
                batch.draw(enemy.getSprite(), enemy.getSprite().getX(), enemy.getSprite().getY(), enemy.getSprite().getOriginX(), enemy.getSprite().getOriginY(), enemy.getSprite().getWidth(), enemy.getSprite().getHeight(), enemy.getSprite().getScaleX(), enemy.getSprite().getScaleY(), enemy.getSprite().getRotation());
            }

            //Draw Player
            if (!player.getDeleteSprite()) {
                //If statements to change sprite Texture
                if (touchedPowerUp && player.getMushroom()) {
                    player.setTexture(new Texture("paragoomba.png"));
                } else if (player.getStar()) {
                    player.setTexture(new Texture("stargoomba.png"));
                }
//                else if (player.getFireFlower()) {
//                    player.setTexture(new Texture("firegoomba.png"));
                else if (player.getLifeStealItem()) {
                    player.setTexture(new Texture("lifesteal-goomba.png"));
                }
                else {
                    player.setTexture(new Texture("goomba.png"));
                }
                batch.draw(player.getSprite(), player.getSprite().getX(), player.getSprite().getY(), player.getSprite().getOriginX(), player.getSprite().getOriginY(), player.getSprite().getWidth(), player.getSprite().getHeight(), player.getSprite().getScaleX(), player.getSprite().getScaleY(), player.getSprite().getRotation());
            }

            //Add Item to the list
            if (touchedItemBlock && !block.isItemSpawned()) {
                createItem(block.getItem()); //Create the item
                block.setSpawned(true);
                touchedItemBlock = false;
            }

            //Draw All Blocks
            for (Block blockIteration : blockList) {
                if (blockIteration.isItemSpawned())
                    blockIteration.setTexture(new Texture("used-item-block.jpg"));

                batch.draw(blockIteration.getSprite(), blockIteration.getSprite().getX(), blockIteration.getSprite().getY(), blockIteration.getSprite().getOriginX(), blockIteration.getSprite().getOriginY(), blockIteration.getSprite().getWidth(), blockIteration.getSprite().getHeight(), blockIteration.getSprite().getScaleX(), blockIteration.getSprite().getScaleY(), blockIteration.getSprite().getRotation());
            }


            for (Fire f : fireballList) {
                f.render(batch);
            }

//        int count = 0;
//        while (count < fireballList.size()) {
//            Fire curFire = fireballList.get(count);
//            curFire.update(delta);
////            curFire.getBody().setLinearVelocity(3.0f, 0f);
////            curFire.getBody().setUserData(curFire);
////            curFire.getSprite().setSize(20.f,20.f);
//            batch.draw(curFire.getSprite(), curFire.position.x, curFire.position.y);
//            count++;
//        }

            //Draw all items in the list
            for (Item itemIteration : itemListGame) {
                getItemSprite(itemIteration); //Setting the Sprite Position
                drawItemSprite(itemIteration); //Draw the item
            }
        }
        batch.end();

        if (player.getSlowItem()) {
            slowTime += Gdx.graphics.getDeltaTime();
            if (slowTime > 3f) {
                player.setSlowItem(false);
            }
        }

        if (player.getSpeedItem()) {
            speedTime += Gdx.graphics.getDeltaTime();
            if (speedTime > 3f) {
                player.setSpeedItem(false);
            }
        }

        if (player.getStar()) {
            starTime += Gdx.graphics.getDeltaTime();
            if (starTime > 5f) {
                player.setStar(false);
            }
        }

        if (player.getLifeStealItem()) {
            lifeStealTime += Gdx.graphics.getDeltaTime();
            if (lifeStealTime > 5f) {
                player.setLifeStealItem(false);
            }

        }


        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            if (player.getSlowItem()) {
                speedLeft = -1f;
                speedRight = 1f;
            } else if (player.getSpeedItem()) {
                speedLeft = -5f;
                speedRight = 5f;
            } else {
                speedLeft = -3f;
                speedRight = 3f;
            }
            player.getBody().setLinearVelocity(speedRight, player.getBody().getLinearVelocity().y);
            if (!facingRight) {
                facingRight = true;
                player.getSprite().flip(true, false);
            }

        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            player.getBody().setLinearVelocity(speedLeft, player.getBody().getLinearVelocity().y);
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
                if (deleteList.get(i) == oneUpItem.getBody() && player.getOneUpItem()) {
                    player.addLife(1);
                    player.setOneUpItem(false);
                } else if (deleteList.get(i) == enemy.getBody() && player.getLifeStealItem()) {
                    player.addLife(1);
                }
                if (deleteList.get(i) == enemy.getBody()) {
                    System.out.println("Has Powerup: " + player.hasPowerUp());
                    System.out.println("Has lifesteal: " + player.getLifeStealItem());
                }
                world.destroyBody(deleteList.get(i));
                deleteList.remove(i);
            }
        }


        //Player killed
        if (playerKilled) {
            playerKilled = false;
        }

        world.step(Gdx.graphics.getDeltaTime(), 6, 2);
        batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
        hud.update(delta, player, itemToName);
    }

    public Vector2 getPlayerLocation() {
        return new Vector2(player.getSprite().getX(), player.getSprite().getY());
    }

    public void getItemSprite(Item b) {
        b.getSprite().setPosition((b.getBody().getPosition().x * PIXELS_TO_METERS) - b.getSprite().getWidth()/2, (b.getBody().getPosition().y * PIXELS_TO_METERS) - b.getSprite().getHeight()/2);
    }

    public void drawItemSprite(Item b) {
        batch.draw(b.getSprite(), b.getSprite().getX(), b.getSprite().getY(), b.getSprite().getOriginX(), b.getSprite().getOriginY(), b.getSprite().getWidth(), b.getSprite().getHeight(), b.getSprite().getScaleX(), b.getSprite().getScaleY(), b.getSprite().getRotation());
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

    public void createItem(Item i) {
//        i.setPosition(i.getSpawn().x, i.getSpawn().y);
        i.setPosition(block.getPosition().x, block.getPosition().y + 64f);
        i.setBody(BodyDef.BodyType.StaticBody);
        i.setShape();
        i.setFixture(0f, 0f);
        i.getBody().setUserData(i);
        itemListGame.add(i);
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

        shootTimer += Gdx.graphics.getDeltaTime();
        if (keycode == Input.Keys.Z && shootTimer >= 0.05f) {
            shootTimer = 0f;
            if (player.getFireFlower()) {
                if (facingRight) {
                    Fire fireball = new Fire(getPlayerLocation(), new Texture("fireball.png"), this.world, 4f);
//                    fireball.getBody().setUserData(fireball);
                    fireballList.add(fireball);
                    if (!fireballRight) {
                        fireball.getSprite().flip(true, false);
                    }
                } else {
                    Fire fireball = new Fire(getPlayerLocation(), new Texture("fireball.png"), this.world, -4f);
                    fireballList.add(fireball);
//                    fireball.getBody().setUserData(fireball);
                    if (fireballRight) {
                        fireball.getSprite().flip(true, false);
                    }
                }
            }
        }

        if (keycode == Input.Keys.X) {
//            System.out.println(star);
//            System.out.println(player.getHeldItem());
            if (player.getHeldItem() == star) {
                player.setHeldItem(null);
                player.setStar(true);
            } else if (player.getHeldItem() == lifeStealItem) {
                if (!player.hasPowerUp()) {
                    player.setHeldItem(null);
                    player.setLifeStealItem(true);
                    player.setPowerUp(true);
                    System.out.println("Has Powerup: " + player.hasPowerUp());
                    System.out.println("Has lifesteal: " + player.getLifeStealItem());
                }
            }
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

    public World getWorld() {
        return this.world;
    }
}


