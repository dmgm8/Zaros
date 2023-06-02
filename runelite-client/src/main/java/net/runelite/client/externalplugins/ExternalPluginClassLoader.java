/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.externalplugins;

import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.net.URLClassLoader;
import net.runelite.client.externalplugins.ExternalPluginManifest;
import net.runelite.client.util.ReflectUtil;

class ExternalPluginClassLoader
extends URLClassLoader
implements ReflectUtil.PrivateLookupableClassLoader {
    private final ExternalPluginManifest manifest;
    private MethodHandles.Lookup lookup;

    ExternalPluginClassLoader(ExternalPluginManifest manifest, URL[] urls) {
        super(urls, ExternalPluginClassLoader.class.getClassLoader());
        this.manifest = manifest;
        ReflectUtil.installLookupHelper(this);
    }

    @Override
    public Class<?> defineClass0(String name, byte[] b, int off, int len) throws ClassFormatError {
        return super.defineClass(name, b, off, len);
    }

    public ExternalPluginManifest getManifest() {
        return this.manifest;
    }

    @Override
    public MethodHandles.Lookup getLookup() {
        return this.lookup;
    }

    @Override
    public void setLookup(MethodHandles.Lookup lookup) {
        this.lookup = lookup;
    }
}

