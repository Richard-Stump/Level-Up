package com.mygdx.nextlevel;

/**
 * A class of groups to use in the collision detection. If this remains just the box helper
 * tag, then it should just be refactored to just the BoxCollider class.
 */
public class CollisionGroups {
    public static short BOX_HELPER = -1;  //the negative signifies that box helpers should never trigger each other
    public static short NONE = 0b0;
    public static short WORLD = 0b01;
    public static short ACTOR = 0b10;
    public static short ITEM = 0b100;
    public static short BLOCK = 0b1000;
    public static short FIRE = 0b10000;
    public static short ENEMY = 0b100000;
    public static short ALL = 0b01111111;
}
