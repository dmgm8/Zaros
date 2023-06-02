/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  net.runelite.api.Client
 *  net.runelite.api.GameState
 *  net.runelite.api.events.GameStateChanged
 *  net.runelite.http.api.xtea.XteaKey
 *  net.runelite.http.api.xtea.XteaRequest
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package net.runelite.client.plugins.xtea;

import java.util.HashSet;
import java.util.Set;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.xtea.XteaClient;
import net.runelite.http.api.xtea.XteaKey;
import net.runelite.http.api.xtea.XteaRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(name="Xtea", hidden=true, forceDisabled=true)
public class XteaPlugin
extends Plugin {
    private static final Logger log = LoggerFactory.getLogger(XteaPlugin.class);
    private final Set<Integer> sentRegions = new HashSet<Integer>();
    @Inject
    private Client client;
    @Inject
    private XteaClient xteaClient;

    @Subscribe
    public void onGameStateChanged(GameStateChanged gameStateChanged) {
        if (gameStateChanged.getGameState() != GameState.LOGGED_IN) {
            return;
        }
        int revision = this.client.getRevision();
        int[] regions = this.client.getMapRegions();
        int[][] xteaKeys = this.client.getXteaKeys();
        XteaRequest xteaRequest = new XteaRequest();
        xteaRequest.setRevision(revision);
        for (int idx = 0; idx < regions.length; ++idx) {
            int region = regions[idx];
            int[] keys = xteaKeys[idx];
            if (this.sentRegions.contains(region)) continue;
            this.sentRegions.add(region);
            log.debug("Region {} keys {}, {}, {}, {}", new Object[]{region, keys[0], keys[1], keys[2], keys[3]});
            XteaKey xteaKey = new XteaKey();
            xteaKey.setRegion(region);
            xteaKey.setKeys(keys);
            xteaRequest.addKey(xteaKey);
        }
        if (xteaRequest.getKeys().isEmpty()) {
            return;
        }
        this.xteaClient.submit(xteaRequest);
    }
}

