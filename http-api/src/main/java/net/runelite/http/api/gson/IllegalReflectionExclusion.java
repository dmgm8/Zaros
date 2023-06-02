/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.gson.ExclusionStrategy
 *  com.google.gson.FieldAttributes
 */
package net.runelite.http.api.gson;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

public class IllegalReflectionExclusion
implements ExclusionStrategy {
    private static final Set<ClassLoader> PRIVATE_CLASSLOADERS = new HashSet<ClassLoader>();

    public boolean shouldSkipField(FieldAttributes f) {
        if (!PRIVATE_CLASSLOADERS.contains(f.getDeclaringClass().getClassLoader())) {
            return false;
        }
        assert (!Modifier.isPrivate(f.getDeclaringClass().getModifiers())) : "gsoning private class " + f.getDeclaringClass().getName();
        try {
            f.getDeclaringClass().getField(f.getName());
        }
        catch (NoSuchFieldException e) {
            throw new AssertionError((Object)("gsoning private field " + f.getDeclaringClass() + "." + f.getName()));
        }
        return false;
    }

    public boolean shouldSkipClass(Class<?> clazz) {
        return false;
    }

    static {
        for (ClassLoader cl = ClassLoader.getSystemClassLoader(); cl != null; cl = cl.getParent()) {
            PRIVATE_CLASSLOADERS.add(cl);
        }
    }
}

