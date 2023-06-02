/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.inject.Provides
 *  javax.inject.Inject
 *  net.runelite.api.Client
 *  net.runelite.api.GameState
 *  net.runelite.api.HealthBar
 *  net.runelite.api.SpritePixels
 *  net.runelite.api.events.BeforeMenuRender
 *  net.runelite.api.events.BeforeRender
 *  net.runelite.api.events.GameStateChanged
 *  net.runelite.api.events.PostHealthBar
 *  net.runelite.api.events.ScriptCallbackEvent
 *  net.runelite.api.widgets.Widget
 *  net.runelite.api.widgets.WidgetInfo
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package net.runelite.client.plugins.interfacestyles;

import com.google.inject.Provides;
import java.awt.image.BufferedImage;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.HealthBar;
import net.runelite.api.SpritePixels;
import net.runelite.api.events.BeforeMenuRender;
import net.runelite.api.events.BeforeRender;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.PostHealthBar;
import net.runelite.api.events.ScriptCallbackEvent;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.interfacestyles.HealthbarOverride;
import net.runelite.client.plugins.interfacestyles.InterfaceStylesConfig;
import net.runelite.client.plugins.interfacestyles.Skin;
import net.runelite.client.plugins.interfacestyles.SpriteOverride;
import net.runelite.client.plugins.interfacestyles.WidgetOffset;
import net.runelite.client.plugins.interfacestyles.WidgetOverride;
import net.runelite.client.util.ImageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(name="Interface Styles", description="Change the interface style to the 2005/2010 interface", tags={"2005", "2010", "skin", "theme", "ui"}, enabledByDefault=false)
public class InterfaceStylesPlugin
extends Plugin {
    private static final Logger log = LoggerFactory.getLogger(InterfaceStylesPlugin.class);
    @Inject
    private Client client;
    @Inject
    private ClientThread clientThread;
    @Inject
    private InterfaceStylesConfig config;
    @Inject
    private SpriteManager spriteManager;
    private SpritePixels[] defaultCrossSprites;

    @Provides
    InterfaceStylesConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(InterfaceStylesConfig.class);
    }

    @Override
    protected void startUp() throws Exception {
        this.clientThread.invoke(this::updateAllOverrides);
    }

    @Override
    protected void shutDown() throws Exception {
        this.clientThread.invoke(() -> {
            this.restoreWidgetDimensions();
            this.removeGameframe();
            this.restoreHealthBars();
            this.restoreCrossSprites();
        });
    }

    @Subscribe
    public void onConfigChanged(ConfigChanged config) {
        if (config.getGroup().equals("interfaceStyles")) {
            this.clientThread.invoke(this::updateAllOverrides);
        }
    }

    @Subscribe
    public void onScriptCallbackEvent(ScriptCallbackEvent event) {
        if ("forceStackStones".equals(event.getEventName()) && this.config.alwaysStack()) {
            int[] intStack = this.client.getIntStack();
            int intStackSize = this.client.getIntStackSize();
            intStack[intStackSize - 1] = 1;
        }
    }

    @Subscribe
    public void onBeforeRender(BeforeRender event) {
        this.adjustWidgetDimensions();
    }

    @Subscribe
    public void onPostHealthBar(PostHealthBar postHealthBar) {
        if (!this.config.hdHealthBars()) {
            return;
        }
        HealthBar healthBar = postHealthBar.getHealthBar();
        HealthbarOverride override = HealthbarOverride.get(healthBar.getHealthBarFrontSpriteId());
        if (override != null) {
            healthBar.setPadding(override.getPadding());
        }
    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged gameStateChanged) {
        if (gameStateChanged.getGameState() != GameState.LOGIN_SCREEN) {
            return;
        }
        this.overrideCrossSprites();
    }

    private void updateAllOverrides() {
        this.removeGameframe();
        this.overrideSprites();
        this.overrideWidgetSprites();
        this.restoreWidgetDimensions();
        this.adjustWidgetDimensions();
        this.overrideHealthBars();
        this.overrideCrossSprites();
    }

    @Subscribe
    public void onBeforeMenuRender(BeforeMenuRender event) {
        if (this.config.hdMenu()) {
            this.client.draw2010Menu(this.config.menuAlpha());
            event.consume();
        } else if (this.config.menuAlpha() != 255) {
            this.client.drawOriginalMenu(this.config.menuAlpha());
            event.consume();
        }
    }

    private void overrideSprites() {
        Skin configuredSkin = this.config.skin();
        for (SpriteOverride spriteOverride : SpriteOverride.values()) {
            for (Skin skin : spriteOverride.getSkin()) {
                if (skin != configuredSkin) continue;
                String configSkin = skin.getExtendSkin() != null ? skin.getExtendSkin().toString() : skin.toString();
                String file = configSkin + "/" + spriteOverride.getSpriteID() + ".png";
                SpritePixels spritePixels = this.getFileSpritePixels(file);
                if (spriteOverride.getSpriteID() == 169) {
                    this.client.setCompass(spritePixels);
                    continue;
                }
                this.client.getSpriteOverrides().put(spriteOverride.getSpriteID(), spritePixels);
            }
        }
    }

    private void restoreSprites() {
        this.client.getWidgetSpriteCache().reset();
        for (SpriteOverride spriteOverride : SpriteOverride.values()) {
            this.client.getSpriteOverrides().remove(spriteOverride.getSpriteID());
        }
    }

    private void overrideWidgetSprites() {
        Skin configuredSkin = this.config.skin();
        for (WidgetOverride widgetOverride : WidgetOverride.values()) {
            String configSkin;
            String file;
            SpritePixels spritePixels;
            if (widgetOverride.getSkin() != configuredSkin && widgetOverride.getSkin() != configuredSkin.getExtendSkin() || (spritePixels = this.getFileSpritePixels(file = (configSkin = configuredSkin.getExtendSkin() != null ? configuredSkin.getExtendSkin().toString() : configuredSkin.toString()) + "/widget/" + widgetOverride.getName() + ".png")) == null) continue;
            for (WidgetInfo widgetInfo : widgetOverride.getWidgetInfo()) {
                this.client.getWidgetSpriteOverrides().put(widgetInfo.getPackedId(), spritePixels);
            }
        }
    }

    private void restoreWidgetSprites() {
        for (WidgetOverride widgetOverride : WidgetOverride.values()) {
            for (WidgetInfo widgetInfo : widgetOverride.getWidgetInfo()) {
                this.client.getWidgetSpriteOverrides().remove(widgetInfo.getPackedId());
            }
        }
    }

    private SpritePixels getFileSpritePixels(String file) {
        try {
            log.debug("Loading: {}", (Object)file);
            BufferedImage image = ImageUtil.loadImageResource(this.getClass(), file);
            return ImageUtil.getImageSpritePixels(image, this.client);
        }
        catch (RuntimeException ex) {
            log.debug("Unable to load image: ", (Throwable)ex);
            return null;
        }
    }

    private void adjustWidgetDimensions() {
        for (WidgetOffset widgetOffset : WidgetOffset.values()) {
            Widget widget;
            if (widgetOffset.getSkin() != this.config.skin() || (widget = this.client.getWidget(widgetOffset.getWidgetInfo())) == null) continue;
            if (widgetOffset.getOffsetX() != null) {
                widget.setRelativeX(widgetOffset.getOffsetX().intValue());
            }
            if (widgetOffset.getOffsetY() != null) {
                widget.setRelativeY(widgetOffset.getOffsetY().intValue());
            }
            if (widgetOffset.getWidth() != null) {
                widget.setWidth(widgetOffset.getWidth().intValue());
            }
            if (widgetOffset.getHeight() == null) continue;
            widget.setHeight(widgetOffset.getHeight().intValue());
        }
    }

    private void overrideHealthBars() {
        if (this.config.hdHealthBars()) {
            this.spriteManager.addSpriteOverrides(HealthbarOverride.values());
            this.clientThread.invokeLater(((Client)this.client)::resetHealthBarCaches);
        } else {
            this.restoreHealthBars();
        }
    }

    private void restoreHealthBars() {
        this.spriteManager.removeSpriteOverrides(HealthbarOverride.values());
        this.clientThread.invokeLater(((Client)this.client)::resetHealthBarCaches);
    }

    private void overrideCrossSprites() {
        if (this.config.rsCrossSprites()) {
            if (this.defaultCrossSprites != null) {
                return;
            }
            SpritePixels[] crossSprites = this.client.getCrossSprites();
            if (crossSprites == null) {
                return;
            }
            this.defaultCrossSprites = new SpritePixels[crossSprites.length];
            System.arraycopy(crossSprites, 0, this.defaultCrossSprites, 0, this.defaultCrossSprites.length);
            for (int i = 0; i < crossSprites.length; ++i) {
                SpritePixels newSprite = this.getFileSpritePixels("rs3/cross_sprites/" + i + ".png");
                if (newSprite == null) continue;
                crossSprites[i] = newSprite;
            }
        } else {
            this.restoreCrossSprites();
        }
    }

    private void restoreCrossSprites() {
        if (this.defaultCrossSprites == null) {
            return;
        }
        SpritePixels[] crossSprites = this.client.getCrossSprites();
        if (crossSprites != null && this.defaultCrossSprites.length == crossSprites.length) {
            System.arraycopy(this.defaultCrossSprites, 0, crossSprites, 0, this.defaultCrossSprites.length);
        }
        this.defaultCrossSprites = null;
    }

    private void restoreWidgetDimensions() {
        for (WidgetOffset widgetOffset : WidgetOffset.values()) {
            Widget widget = this.client.getWidget(widgetOffset.getWidgetInfo());
            if (widget == null) continue;
            widget.revalidate();
        }
    }

    private void removeGameframe() {
        this.restoreSprites();
        this.restoreWidgetSprites();
        BufferedImage compassImage = this.spriteManager.getSprite(169, 0);
        if (compassImage != null) {
            SpritePixels compass = ImageUtil.getImageSpritePixels(compassImage, this.client);
            this.client.setCompass(compass);
        }
    }
}

