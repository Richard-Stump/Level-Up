package com.mygdx.nextlevel.screens.editor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation to mark classes a placeable in the level editor.
 *
 * This annotation should only be used in the children classes that are meant to
 * be placed, not parent classes such as 'Item' and 'Actor'
 */
@Retention(RetentionPolicy.RUNTIME) //Needed to ensure the system works
@Target(ElementType.TYPE)
public @interface Placeable {
    /**
     * Which tab will the object be put into?
     */
    public String group() default "__DEFAULT__";

    /**
     * What is the name that will show up in the object selector?
     */
    public String displayName() default "__DEFAULT__";

    /**
     * What texture to use for the object selector?
     */
    public String defaultTexture();
}
