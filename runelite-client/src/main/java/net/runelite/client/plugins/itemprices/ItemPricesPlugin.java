/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.inject.Provides
 *  javax.inject.Inject
 */
package net.runelite.client.plugins.itemprices;

import com.google.inject.Provides;
import javax.inject.Inject;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.itemprices.ItemPricesConfig;
import net.runelite.client.plugins.itemprices.ItemPricesOverlay;
import net.runelite.client.ui.overlay.OverlayManager;

@PluginDescriptor(name="Item Prices", description="Show prices on hover for items in your inventory and bank", tags={"bank", "inventory", "overlay", "high", "alchemy", "grand", "exchange", "tooltips"}, enabledByDefault=false)
public class ItemPricesPlugin
extends Plugin {
    @Inject
    private OverlayManager overlayManager;
    @Inject
    private ItemPricesOverlay overlay;

    @Provides
    ItemPricesConfig getConfig(ConfigManager configManager) {
        return configManager.getConfig(ItemPricesConfig.class);
    }

    @Override
    protected void startUp() throws Exception {
        this.overlayManager.add(this.overlay);
    }

    @Override
    protected void shutDown() throws Exception {
        this.overlayManager.remove(this.overlay);
    }
}

