/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.WorldType
 *  okhttp3.HttpUrl
 */
package net.runelite.client.hiscore;

import java.util.Set;
import net.runelite.api.WorldType;
import okhttp3.HttpUrl;

public enum HiscoreEndpoint {
    NORMAL("Normal", "https://zaros.io/api/hiscore/index_lite"),
    IRONMAN("Ironman", "https://zaros.io/api/hiscore/index_lite"),
    HARDCORE_IRONMAN("Hardcore Ironman", "https://zaros.io/api/hiscore/index_lite"),
    ULTIMATE_IRONMAN("Ultimate Ironman", "https://zaros.io/api/hiscore/index_lite"),
    DEADMAN("Deadman", "https://zaros.io/api/hiscore_oldschool_deadman/index_lite"),
    LEAGUE("Leagues", "https://zaros.io/api/hiscore/index_lite"),
    TOURNAMENT("Tournament", "https://zaros.io/api/hiscore/index_lite");

    private final String name;
    private final HttpUrl hiscoreURL;

    private HiscoreEndpoint(String name, String hiscoreURL) {
        this.name = name;
        this.hiscoreURL = HttpUrl.get((String)hiscoreURL);
    }

    public static HiscoreEndpoint fromWorldTypes(Set<WorldType> worldTypes) {
        if (worldTypes.contains((Object)WorldType.SEASONAL)) {
            return LEAGUE;
        }
        if (worldTypes.contains((Object)WorldType.DEADMAN)) {
            return DEADMAN;
        }
        return NORMAL;
    }

    public String getName() {
        return this.name;
    }

    public HttpUrl getHiscoreURL() {
        return this.hiscoreURL;
    }
}

