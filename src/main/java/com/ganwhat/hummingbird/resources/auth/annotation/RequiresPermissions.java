package com.ganwhat.hummingbird.resources.auth.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author : cvvnx1@hotmail.com
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresPermissions {

    /**
     * Determine if the user is allowed to invoke the code protected by this annotation.
     */
    String[] value();

    /**
     * The logical operation for the permission checks in case multiple roles are specified. AND is the default
     */
    Logical logical() default Logical.AND;

}
