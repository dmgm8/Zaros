/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 */
package net.runelite.client.plugins.info;

import java.awt.image.BufferedImage;
import javax.inject.Inject;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.info.InfoPanel;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.util.ImageUtil;

@PluginDescriptor(name="Info Panel", description="Enable the Info panel", loadWhenOutdated=true)
public class InfoPlugin
extends Plugin {
    @Inject
    private ClientToolbar clientToolbar;
    private NavigationButton navButton;

    @Override
    protected void startUp() throws Exception {
        InfoPanel panel = (InfoPanel)this.injector.getInstance(InfoPanel.class);
        panel.init();
        BufferedImage icon = ImageUtil.loadImageResource(this.getClass(), "info_icon.png");
        this.navButton = NavigationButton.builder().tooltip("Info").icon(icon).priority(10).panel(panel).build();
        this.clientToolbar.addNavigation(this.navButton);
    }

    @Override
    protected void shutDown() {
        this.clientToolbar.removeNavigation(this.navButton);
    }
}

