/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api.hooks;

import net.runelite.api.Model;
import net.runelite.api.Renderable;
import net.runelite.api.SceneTileModel;
import net.runelite.api.SceneTilePaint;
import net.runelite.api.Texture;

public interface DrawCallbacks {
    public void draw(Renderable var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, long var10);

    public void drawScenePaint(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, SceneTilePaint var9, int var10, int var11, int var12, int var13, int var14, int var15);

    public void drawSceneModel(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, SceneTileModel var9, int var10, int var11, int var12, int var13, int var14, int var15);

    public void draw(int var1);

    public boolean drawFace(Model var1, int var2);

    public void drawScene(int var1, int var2, int var3, int var4, int var5, int var6);

    public void postDrawScene();

    public void animate(Texture var1, int var2);
}

