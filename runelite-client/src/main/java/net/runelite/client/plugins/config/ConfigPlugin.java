/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  javax.inject.Provider
 *  net.runelite.api.MenuAction
 */
package net.runelite.client.plugins.config;

import java.awt.image.BufferedImage;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.swing.SwingUtilities;
import net.runelite.api.MenuAction;
import net.runelite.client.config.ChatColorConfig;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.config.RuneLiteConfig;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.OverlayMenuClicked;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.config.PluginConfigurationDescriptor;
import net.runelite.client.plugins.config.PluginListPanel;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayMenuEntry;
import net.runelite.client.util.ImageUtil;

@PluginDescriptor(name="Configuration", loadWhenOutdated=true, hidden=true)
public class ConfigPlugin
extends Plugin {
    @Inject
    private ClientToolbar clientToolbar;
    @Inject
    private Provider<PluginListPanel> pluginListPanelProvider;
    @Inject
    private ConfigManager configManager;
    @Inject
    private RuneLiteConfig runeLiteConfig;
    @Inject
    private ChatColorConfig chatColorConfig;
    private PluginListPanel pluginListPanel;
    private NavigationButton navButton;

    @Override
    protected void startUp() throws Exception {
        this.pluginListPanel = (PluginListPanel)this.pluginListPanelProvider.get();
        this.pluginListPanel.addFakePlugin(new PluginConfigurationDescriptor("RuneLite", "RuneLite client settings", new String[]{"client", "notification", "size", "position", "window", "chrome", "focus", "font", "overlay", "tooltip", "infobox"}, this.runeLiteConfig, this.configManager.getConfigDescriptor(this.runeLiteConfig)), new PluginConfigurationDescriptor("Chat Color", "Recolor chat text", new String[]{"colour", "messages"}, this.chatColorConfig, this.configManager.getConfigDescriptor(this.chatColorConfig)));
        this.pluginListPanel.rebuildPluginList();
        BufferedImage icon = ImageUtil.loadImageResource(this.getClass(), "config_icon.png");
        this.navButton = NavigationButton.builder().tooltip("Configuration").icon(icon).priority(0).panel(this.pluginListPanel.getMuxer()).build();
        this.clientToolbar.addNavigation(this.navButton);
    }

    @Override
    protected void shutDown() throws Exception {
        this.clientToolbar.removeNavigation(this.navButton);
    }

    @Subscribe
    public void onOverlayMenuClicked(OverlayMenuClicked overlayMenuClicked) {
        OverlayMenuEntry overlayMenuEntry = overlayMenuClicked.getEntry();
        if (overlayMenuEntry.getMenuAction() == MenuAction.RUNELITE_OVERLAY_CONFIG) {
            Overlay overlay = overlayMenuClicked.getOverlay();
            Plugin plugin = overlay.getPlugin();
            if (plugin == null) {
                return;
            }
            SwingUtilities.invokeLater(() -> {
                if (!this.navButton.isSelected()) {
                    this.navButton.getOnSelect().run();
                }
                this.pluginListPanel.openConfigurationPanel(plugin.getName());
            });
        }
    }
}

