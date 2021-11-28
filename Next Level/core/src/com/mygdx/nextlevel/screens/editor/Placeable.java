package com.mygdx.nextlevel.screens.editor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) //Needed to ensure the system works
@Target(ElementType.TYPE)
public @interface Placeable {
    public String group() default "__DEFAULT__";
    public String displayName() default "__DEFAULT__";
    public String defaultTexture();
}
