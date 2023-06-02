/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value=RetentionPolicy.SOURCE)
@Documented
@Target(value={ElementType.FIELD})
@interface ScriptArguments {
    public int integer() default 0;

    public int string() default 0;
}

