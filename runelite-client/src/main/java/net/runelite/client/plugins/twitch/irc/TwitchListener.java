/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.twitch.irc;

import java.util.Map;

public interface TwitchListener {
    public void privmsg(Map<String, String> var1, String var2);

    public void roomstate(Map<String, String> var1);

    public void usernotice(Map<String, String> var1, String var2);
}

