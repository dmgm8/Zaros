/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.inject.Provides
 *  javax.inject.Inject
 *  javax.inject.Singleton
 *  net.runelite.api.GameState
 *  net.runelite.api.events.GameStateChanged
 */
package net.runelite.client.plugins.profiles;

import com.google.inject.Provides;
import java.awt.image.BufferedImage;
import java.util.concurrent.ScheduledExecutorService;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.profiles.ProfilesConfig;
import net.runelite.client.plugins.profiles.ProfilesPanel;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.util.ImageUtil;

@PluginDescriptor(name="Account Switcher", description="Allow for a allows you to easily switch between multiple Zaros accounts", tags={"profile", "account", "login", "log in"}, enabledByDefault=false)
@Singleton
public class ProfilesPlugin
extends Plugin {
    @Inject
    private ClientToolbar clientToolbar;
    @Inject
    private ProfilesConfig config;
    @Inject
    private ScheduledExecutorService executorService;
    private ProfilesPanel panel;
    private NavigationButton navButton;
    private boolean switchToPanel;
    private boolean streamerMode;
    private boolean displayEmailAddress;
    private boolean rememberPassword;

    @Provides
    ProfilesConfig getConfig(ConfigManager configManager) {
        return configManager.getConfig(ProfilesConfig.class);
    }

    @Override
    protected void startUp() {
        this.updateConfig();
        this.panel = (ProfilesPanel)this.injector.getInstance(ProfilesPanel.class);
        this.panel.init();
        BufferedImage icon = ImageUtil.getResourceStreamFromClass(this.getClass(), "profiles_icon.png");
        this.navButton = NavigationButton.builder().tooltip("Profiles").icon(icon).priority(8).panel(this.panel).onReady(() -> this.executorService.submit(() -> this.OpenPanel(true))).build();
        this.clientToolbar.addNavigation(this.navButton);
    }

    @Override
    protected void shutDown() {
        this.clientToolbar.removeNavigation(this.navButton);
    }

    @Subscribe
    private void onGameStateChanged(GameStateChanged event) {
        if (!this.switchToPanel) {
            return;
        }
        if (event.getGameState().equals((Object)GameState.LOGIN_SCREEN) && !this.navButton.isSelected()) {
            this.OpenPanel(true);
        }
    }

    @Subscribe
    private void onConfigChanged(ConfigChanged event) throws Exception {
        if (event.getGroup().equals("profiles") && event.getKey().equals("rememberPassword")) {
            this.panel = (ProfilesPanel)this.injector.getInstance(ProfilesPanel.class);
            this.shutDown();
            this.startUp();
            this.updateConfig();
        }
        if (event.getGroup().equals("profiles") && !event.getKey().equals("rememberPassword")) {
            this.updateConfig();
            this.panel = (ProfilesPanel)this.injector.getInstance(ProfilesPanel.class);
            this.panel.redrawProfiles();
        }
    }

    private void OpenPanel(boolean openPanel) {
        if (openPanel && this.switchToPanel) {
            this.navButton.getOnSelect().run();
        }
    }

    private void updateConfig() {
        this.switchToPanel = this.config.switchPanel();
        this.rememberPassword = this.config.rememberPassword();
        this.streamerMode = this.config.streamerMode();
        this.displayEmailAddress = this.config.displayEmailAddress();
    }

    boolean isSwitchToPanel() {
        return this.switchToPanel;
    }

    boolean isStreamerMode() {
        return this.streamerMode;
    }

    boolean isDisplayEmailAddress() {
        return this.displayEmailAddress;
    }

    boolean isRememberPassword() {
        return this.rememberPassword;
    }
}

