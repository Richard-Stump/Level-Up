package com.mygdx.nextlevel;

/**
 * A class of groups to use in the collision detection. If this remains just the box helper
 * tag, then it should just be refactored to just the BoxCollider class.
 */
public class CollisionGroups {
    public static short BOX_HELPER = -1;  //the negative signifies that box helpers should never trigger each other
}
