/*
 * Decompiled with CFR 0.150.
 */
package mapping;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value=RetentionPolicy.RUNTIME)
@Target(value={ElementType.FIELD})
public @interface ObfuscatedGetter {
    public int intValue() default 0;

    public long longValue() default 0L;
}

