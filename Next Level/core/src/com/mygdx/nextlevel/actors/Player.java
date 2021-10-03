//package com.mygdx.nextlevel.actors;
//
//import com.badlogic.gdx.graphics.g2d.Animation;
//import com.badlogic.gdx.graphics.g2d.Sprite;
//import com.badlogic.gdx.graphics.g2d.TextureRegion;
//import com.badlogic.gdx.physics.box2d.*;
//import com.mygdx.nextlevel.GameScreen;
//import com.badlogic.gdx.utils.Array;
//import com.mygdx.nextlevel.NextLevel;
//
//public class Player extends Sprite {
//
//    public enum State {FALLING, JUMPING, IDLE, RUNNING}
//
//    ;
//    public State currentState;
//    public State previousState;
//
//    public World world;
//    public Body b2body;
//
//    private TextureRegion stand;
//    private float stateTimer;
//
//    private Animation<TextureRegion> idle;
//    //private Animation<TextureRegion> run;
//
//    GameScreen screen;
//
//    public Player(GameScreen screen) {
//        this.screen = screen;
//        this.world = screen.getWorld();
//
//        currentState = State.IDLE;
//        previousState = State.IDLE;
//        stateTimer = 0;
//
//        Array<TextureRegion> frames = new Array<TextureRegion>();
//
//        for (int i = 0; i < 4; i++) {
//            frames.add(new TextureRegion(screen.getAtlas().findRegion("Biker_idle"), i * 50, 0, 50, 37));
//        }
//        idle = new Animation<TextureRegion>(0.3f, frames);
//
//        frames.clear();
//
//        definePlayer();
//
//        // set initial values for location, etc.
//        setBounds(0,0, 33 / NextLevel.PPM, 23 / NextLevel.PPM);
//        setRegion(idle.getKeyFrame(stateTimer, true));
//    }
//
//    public void update (float dt) {
//        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 3);
//        setRegion(getFrame(dt));
//    }
//
//    public TextureRegion getFrame(float dt) {
//        currentState = getState();
//        TextureRegion region;
//
//        // add later when we have more states
//        switch (currentState){
//            default:
//                region = idle.getKeyFrame(stateTimer, true);
//        }
//
//        // if the current state is the same as the previous state increase the state timer.
//        // otherwise, the state has changed, and we need to reset timer.
//        stateTimer = currentState == previousState ? stateTimer + dt : 0;
//        //update previous state
//        previousState = currentState;
//        // return our final adjusted frame
//        return region;
//    }
//
//    public State getState() {
//        // currently, only have one state
//        return State.IDLE;
//    }
//
//    public float getStateTimer() {
//        return stateTimer;
//    }
//
//
//    public void definePlayer() {
//        BodyDef bodyDef = new BodyDef();
//        // todo: temp position, update using defined spawn location on map
//        bodyDef.position.set(32 / NextLevel.PPM, 32 / NextLevel.PPM);
//        bodyDef.type = BodyDef.BodyType.DynamicBody;
//        b2body = world.createBody(bodyDef);
//
//        FixtureDef fixtureDef = new FixtureDef();
//        CircleShape cShape = new CircleShape();
//        cShape.setRadius(8 / NextLevel.PPM);
//        fixtureDef.filter.categoryBits = NextLevel.PLAYER_BIT;
//        fixtureDef.filter.maskBits = NextLevel.GROUND_BIT | NextLevel.PLATFORM_BIT | NextLevel.OBJECT_BIT | NextLevel.ENEMY_BIT;
//        fixtureDef.shape = cShape;
//        b2body.createFixture(fixtureDef).setUserData(this);
//
//    }
//}
