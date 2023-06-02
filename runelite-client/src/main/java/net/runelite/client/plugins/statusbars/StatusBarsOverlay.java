/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  net.runelite.api.Client
 *  net.runelite.api.MenuEntry
 *  net.runelite.api.Point
 *  net.runelite.api.Prayer
 *  net.runelite.api.Skill
 *  net.runelite.api.VarPlayer
 *  net.runelite.api.widgets.Widget
 *  net.runelite.api.widgets.WidgetInfo
 */
package net.runelite.client.plugins.statusbars;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.EnumMap;
import java.util.Map;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.MenuEntry;
import net.runelite.api.Point;
import net.runelite.api.Prayer;
import net.runelite.api.Skill;
import net.runelite.api.VarPlayer;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.game.AlternateSprites;
import net.runelite.client.game.SkillIconManager;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.plugins.itemstats.Effect;
import net.runelite.client.plugins.itemstats.ItemStatChangesService;
import net.runelite.client.plugins.itemstats.StatChange;
import net.runelite.client.plugins.statusbars.BarRenderer;
import net.runelite.client.plugins.statusbars.StatusBarsConfig;
import net.runelite.client.plugins.statusbars.StatusBarsPlugin;
import net.runelite.client.plugins.statusbars.Viewport;
import net.runelite.client.plugins.statusbars.config.BarMode;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.util.ImageUtil;

class StatusBarsOverlay
extends Overlay {
    private static final Color PRAYER_COLOR = new Color(50, 200, 200, 175);
    private static final Color ACTIVE_PRAYER_COLOR = new Color(57, 255, 186, 225);
    private static final Color HEALTH_COLOR = new Color(225, 35, 0, 125);
    private static final Color POISONED_COLOR = new Color(0, 145, 0, 150);
    private static final Color VENOMED_COLOR = new Color(0, 65, 0, 150);
    private static final Color HEAL_COLOR = new Color(255, 112, 6, 150);
    private static final Color PRAYER_HEAL_COLOR = new Color(57, 255, 186, 75);
    private static final Color ENERGY_HEAL_COLOR = new Color(199, 118, 0, 218);
    private static final Color RUN_STAMINA_COLOR = new Color(160, 124, 72, 255);
    private static final Color SPECIAL_ATTACK_COLOR = new Color(3, 153, 0, 195);
    private static final Color ENERGY_COLOR = new Color(199, 174, 0, 220);
    private static final Color DISEASE_COLOR = new Color(255, 193, 75, 181);
    private static final Color PARASITE_COLOR = new Color(196, 62, 109, 181);
    private static final int HEIGHT = 252;
    private static final int RESIZED_BOTTOM_HEIGHT = 272;
    private static final int IMAGE_SIZE = 17;
    private static final Dimension ICON_DIMENSIONS = new Dimension(26, 25);
    private static final int RESIZED_BOTTOM_OFFSET_Y = 12;
    private static final int RESIZED_BOTTOM_OFFSET_X = 10;
    private static final int MAX_SPECIAL_ATTACK_VALUE = 100;
    private static final int MAX_RUN_ENERGY_VALUE = 100;
    private final Client client;
    private final StatusBarsPlugin plugin;
    private final StatusBarsConfig config;
    private final ItemStatChangesService itemStatService;
    private final SpriteManager spriteManager;
    private final Image prayerIcon;
    private final Image heartDisease;
    private final Image heartPoison;
    private final Image heartVenom;
    private Image heartIcon;
    private Image specialIcon;
    private Image energyIcon;
    private final Map<BarMode, BarRenderer> barRenderers = new EnumMap<BarMode, BarRenderer>(BarMode.class);

    @Inject
    private StatusBarsOverlay(Client client, StatusBarsPlugin plugin, StatusBarsConfig config, SkillIconManager skillIconManager, ItemStatChangesService itemstatservice, SpriteManager spriteManager) {
        this.setPosition(OverlayPosition.DYNAMIC);
        this.setLayer(OverlayLayer.ABOVE_WIDGETS);
        this.client = client;
        this.plugin = plugin;
        this.config = config;
        this.itemStatService = itemstatservice;
        this.spriteManager = spriteManager;
        this.prayerIcon = ImageUtil.resizeCanvas(ImageUtil.resizeImage(skillIconManager.getSkillImage(Skill.PRAYER, true), 17, 17), StatusBarsOverlay.ICON_DIMENSIONS.width, StatusBarsOverlay.ICON_DIMENSIONS.height);
        this.heartDisease = ImageUtil.resizeCanvas(ImageUtil.loadImageResource(AlternateSprites.class, "1067-DISEASE.png"), StatusBarsOverlay.ICON_DIMENSIONS.width, StatusBarsOverlay.ICON_DIMENSIONS.height);
        this.heartPoison = ImageUtil.resizeCanvas(ImageUtil.loadImageResource(AlternateSprites.class, "1067-POISON.png"), StatusBarsOverlay.ICON_DIMENSIONS.width, StatusBarsOverlay.ICON_DIMENSIONS.height);
        this.heartVenom = ImageUtil.resizeCanvas(ImageUtil.loadImageResource(AlternateSprites.class, "1067-VENOM.png"), StatusBarsOverlay.ICON_DIMENSIONS.width, StatusBarsOverlay.ICON_DIMENSIONS.height);
        this.initRenderers();
    }

    private void initRenderers() {
        this.barRenderers.put(BarMode.DISABLED, null);
        this.barRenderers.put(BarMode.HITPOINTS, new BarRenderer(() -> this.inLms() ? 99 : this.client.getRealSkillLevel(Skill.HITPOINTS), () -> this.client.getBoostedSkillLevel(Skill.HITPOINTS), () -> this.getRestoreValue(Skill.HITPOINTS.getName()), () -> {
            int poisonState = this.client.getVarpValue(VarPlayer.IS_POISONED);
            if (poisonState >= 1000000) {
                return VENOMED_COLOR;
            }
            if (poisonState > 0) {
                return POISONED_COLOR;
            }
            if (this.client.getVarpValue(VarPlayer.DISEASE_VALUE) > 0) {
                return DISEASE_COLOR;
            }
            if (this.client.getVarbitValue(10151) >= 1) {
                return PARASITE_COLOR;
            }
            return HEALTH_COLOR;
        }, () -> HEAL_COLOR, () -> {
            int poisonState = this.client.getVarpValue(VarPlayer.IS_POISONED);
            if (poisonState > 0 && poisonState < 50) {
                return this.heartPoison;
            }
            if (poisonState >= 1000000) {
                return this.heartVenom;
            }
            if (this.client.getVarpValue(VarPlayer.DISEASE_VALUE) > 0) {
                return this.heartDisease;
            }
            return this.heartIcon;
        }));
        this.barRenderers.put(BarMode.PRAYER, new BarRenderer(() -> this.inLms() ? 99 : this.client.getRealSkillLevel(Skill.PRAYER), () -> this.client.getBoostedSkillLevel(Skill.PRAYER), () -> this.getRestoreValue(Skill.PRAYER.getName()), () -> {
            Color prayerColor = PRAYER_COLOR;
            for (Prayer pray : Prayer.values()) {
                if (!this.client.isPrayerActive(pray)) continue;
                prayerColor = ACTIVE_PRAYER_COLOR;
                break;
            }
            return prayerColor;
        }, () -> PRAYER_HEAL_COLOR, () -> this.prayerIcon));
        this.barRenderers.put(BarMode.RUN_ENERGY, new BarRenderer(() -> 100, ((Client)this.client)::getEnergy, () -> this.getRestoreValue("Run Energy"), () -> {
            if (this.client.getVarbitValue(25) != 0) {
                return RUN_STAMINA_COLOR;
            }
            return ENERGY_COLOR;
        }, () -> ENERGY_HEAL_COLOR, () -> this.energyIcon));
        this.barRenderers.put(BarMode.SPECIAL_ATTACK, new BarRenderer(() -> 100, () -> this.client.getVarpValue(VarPlayer.SPECIAL_ATTACK_PERCENT) / 10, () -> 0, () -> SPECIAL_ATTACK_COLOR, () -> SPECIAL_ATTACK_COLOR, () -> this.specialIcon));
    }

    @Override
    public Dimension render(Graphics2D g) {
        int offsetRightBarY;
        int offsetRightBarX;
        int offsetLeftBarY;
        int offsetLeftBarX;
        int height;
        int width;
        if (!this.plugin.isBarsDisplayed()) {
            return null;
        }
        Viewport curViewport = null;
        Widget curWidget = null;
        for (Viewport viewport : Viewport.values()) {
            Widget viewportWidget = this.client.getWidget(viewport.getViewport());
            if (viewportWidget == null || viewportWidget.isHidden()) continue;
            curViewport = viewport;
            curWidget = viewportWidget;
            break;
        }
        if (curViewport == null) {
            return null;
        }
        Point offsetLeft = curViewport.getOffsetLeft();
        Point offsetRight = curViewport.getOffsetRight();
        Point location = curWidget.getCanvasLocation();
        if (curViewport == Viewport.RESIZED_BOTTOM) {
            width = this.config.barWidth();
            height = 272;
            int barWidthOffset = width - 20;
            offsetLeftBarX = location.getX() + 10 - offsetLeft.getX() - 2 * barWidthOffset;
            offsetLeftBarY = location.getY() - 12 - offsetLeft.getY();
            offsetRightBarX = location.getX() + 10 - offsetRight.getX() - barWidthOffset;
            offsetRightBarY = location.getY() - 12 - offsetRight.getY();
        } else {
            width = 20;
            height = 252;
            offsetLeftBarX = location.getX() - offsetLeft.getX();
            offsetLeftBarY = location.getY() - offsetLeft.getY();
            offsetRightBarX = location.getX() - offsetRight.getX() + curWidget.getWidth();
            offsetRightBarY = location.getY() - offsetRight.getY();
        }
        this.buildIcons();
        BarRenderer left = this.barRenderers.get((Object)this.config.leftBarMode());
        BarRenderer right = this.barRenderers.get((Object)this.config.rightBarMode());
        if (left != null) {
            left.renderBar(this.config, g, offsetLeftBarX, offsetLeftBarY, width, height);
        }
        if (right != null) {
            right.renderBar(this.config, g, offsetRightBarX, offsetRightBarY, width, height);
        }
        return null;
    }

    private int getRestoreValue(String skill) {
        Effect change;
        MenuEntry[] menu = this.client.getMenuEntries();
        int menuSize = menu.length;
        if (menuSize == 0) {
            return 0;
        }
        MenuEntry entry = menu[menuSize - 1];
        Widget widget = entry.getWidget();
        int restoreValue = 0;
        if (widget != null && widget.getId() == WidgetInfo.INVENTORY.getId() && (change = this.itemStatService.getItemStatChanges(widget.getItemId())) != null) {
            for (StatChange c : change.calculate(this.client).getStatChanges()) {
                int value = c.getTheoretical();
                if (value == 0 || !c.getStat().getName().equals(skill)) continue;
                restoreValue = value;
            }
        }
        return restoreValue;
    }

    private void buildIcons() {
        if (this.heartIcon == null) {
            this.heartIcon = this.loadAndResize(1067);
        }
        if (this.energyIcon == null) {
            this.energyIcon = this.loadAndResize(1069);
        }
        if (this.specialIcon == null) {
            this.specialIcon = this.loadAndResize(1610);
        }
    }

    private BufferedImage loadAndResize(int spriteId) {
        BufferedImage image = this.spriteManager.getSprite(spriteId, 0);
        if (image == null) {
            return null;
        }
        return ImageUtil.resizeCanvas(image, StatusBarsOverlay.ICON_DIMENSIONS.width, StatusBarsOverlay.ICON_DIMENSIONS.height);
    }

    private boolean inLms() {
        return this.client.getWidget(WidgetInfo.LMS_KDA) != null;
    }
}

