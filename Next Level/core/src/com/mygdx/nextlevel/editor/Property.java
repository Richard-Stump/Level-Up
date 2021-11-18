package com.mygdx.nextlevel.editor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * An annotation used to mark public fields in a class so that the PropertyEditTable
 * can identify them. Optionally, a group can be specified to group properties together,
 * and a display name can be specified to display something different from the field's name.
 *
 * To use, just add the annotation before a public field in a given class:
 * @Property(group="...", displayName="...") public int ...;
 *
 * Then the PropertyEditTable will be able to automatically scan the class and display the
 * annotated properties.
 */
@Retention(RetentionPolicy.RUNTIME) //Needed to ensure the system works
@Target(ElementType.FIELD)
public @interface Property {
    String group() default "__DEFAULT__";
    String displayName() default "__DEFAULT__";
}
