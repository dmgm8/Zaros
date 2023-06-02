/*
 * Decompiled with CFR 0.150.
 */
package mapping;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value=RetentionPolicy.RUNTIME)
@Target(value={ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.FIELD})
public @interface ObfuscatedSignature {
    public String signature();

    public String garbageValue() default "";
}

