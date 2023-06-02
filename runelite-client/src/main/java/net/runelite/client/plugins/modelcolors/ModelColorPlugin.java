/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.inject.Provider
 *  javax.inject.Inject
 *  net.runelite.api.Client
 */
package net.runelite.client.plugins.modelcolors;

import com.google.inject.Provider;
import java.awt.image.BufferedImage;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.devtools.DevToolsPlugin;
import net.runelite.client.plugins.modelcolors.ModelColorPanel;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.util.ImageUtil;

@PluginDescriptor(name="Model Color Changer", description="Enable the model color changer", tags={"panel", "model color"}, developerPlugin=true)
public class ModelColorPlugin
extends Plugin {
    @Inject
    private Client client;
    @Inject
    private ClientToolbar clientToolbar;
    @Inject
    private Provider<ModelColorPanel> uiPanel;
    private NavigationButton uiNavigationButton;

    @Override
    protected void startUp() throws Exception {
        BufferedImage icon = ImageUtil.loadImageResource(DevToolsPlugin.class, "devtools_icon.png");
        this.uiNavigationButton = NavigationButton.builder().tooltip("Model Colors").icon(icon).priority(2).panel((PluginPanel)this.uiPanel.get()).build();
        this.clientToolbar.addNavigation(this.uiNavigationButton);
    }

    @Override
    protected void shutDown() throws Exception {
        this.clientToolbar.removeNavigation(this.uiNavigationButton);
    }
}

