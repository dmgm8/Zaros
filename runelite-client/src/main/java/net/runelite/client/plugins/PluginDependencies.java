/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import net.runelite.client.plugins.PluginDependency;

@Retention(value=RetentionPolicy.RUNTIME)
@Target(value={ElementType.TYPE})
@Documented
public @interface PluginDependencies {
    public PluginDependency[] value();
}

