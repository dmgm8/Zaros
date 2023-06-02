/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.WorldType
 *  net.runelite.http.api.worlds.WorldType
 */
package net.runelite.client.util;

import java.util.EnumSet;
import net.runelite.api.WorldType;

public class WorldUtil {
    public static EnumSet<WorldType> toWorldTypes(EnumSet<net.runelite.http.api.worlds.WorldType> apiTypes) {
        EnumSet<WorldType> types = EnumSet.noneOf(WorldType.class);
        for (net.runelite.http.api.worlds.WorldType apiType : apiTypes) {
            types.add(WorldType.valueOf((String)apiType.name()));
        }
        return types;
    }
}

