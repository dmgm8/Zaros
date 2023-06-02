/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.ApiStatus$Internal
 */
package io.sentry;

import java.lang.reflect.InvocationTargetException;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public final class OptionsContainer<T> {
    private final Class<T> clazz;

    public static <T> OptionsContainer<T> create(Class<T> clazz) {
        return new OptionsContainer<T>(clazz);
    }

    private OptionsContainer(Class<T> clazz) {
        this.clazz = clazz;
    }

    public T createInstance() throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        return this.clazz.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
    }
}

