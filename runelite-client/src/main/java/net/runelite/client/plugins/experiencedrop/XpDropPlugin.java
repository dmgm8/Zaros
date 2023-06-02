/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.inject.Provides
 *  javax.inject.Inject
 *  net.runelite.api.Client
 *  net.runelite.api.EnumComposition
 *  net.runelite.api.Skill
 *  net.runelite.api.events.GameTick
 *  net.runelite.api.events.ScriptPreFired
 *  net.runelite.api.events.StatChanged
 *  net.runelite.api.widgets.Widget
 */
package net.runelite.client.plugins.experiencedrop;

import com.google.inject.Provides;
import java.awt.Color;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.IntStream;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.EnumComposition;
import net.runelite.api.Skill;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.ScriptPreFired;
import net.runelite.api.events.StatChanged;
import net.runelite.api.widgets.Widget;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.experiencedrop.PrayerType;
import net.runelite.client.plugins.experiencedrop.XpDropConfig;
import net.runelite.client.plugins.experiencedrop.XpPrayer;

@PluginDescriptor(name="XP Drop", description="Enable customization of the way XP drops are displayed", tags={"experience", "levels", "tick", "prayer", "xpdrop"})
public class XpDropPlugin
extends Plugin {
    @Inject
    private Client client;
    @Inject
    private XpDropConfig config;
    private int tickCounter = 0;
    private int previousExpGained;
    private boolean hasDropped = false;
    private boolean correctPrayer;
    private Skill lastSkill = null;
    private final Map<Skill, Integer> previousSkillExpTable = new EnumMap<Skill, Integer>(Skill.class);

    @Provides
    XpDropConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(XpDropConfig.class);
    }

    @Subscribe
    public void onScriptPreFired(ScriptPreFired scriptPreFired) {
        if (scriptPreFired.getScriptId() == 996) {
            int[] intStack = this.client.getIntStack();
            int intStackSize = this.client.getIntStackSize();
            int widgetId = intStack[intStackSize - 4];
            this.processXpDrop(widgetId);
        }
    }

    private void processXpDrop(int widgetId) {
        Widget xpdrop = this.client.getWidget(widgetId);
        Widget[] children = xpdrop.getChildren();
        Widget text = children[0];
        PrayerType prayer = this.getActivePrayerType();
        if (prayer == null) {
            this.hideSkillIcons(xpdrop);
            this.resetTextColor(text);
            return;
        }
        IntStream spriteIDs = Arrays.stream(children).skip(1L).filter(Objects::nonNull).mapToInt(Widget::getSpriteId);
        int color = 0;
        switch (prayer) {
            case MELEE: {
                if (!this.correctPrayer && !spriteIDs.anyMatch(id -> id == 197 || id == 198 || id == 199)) break;
                color = this.config.getMeleePrayerColor().getRGB();
                this.correctPrayer = true;
                break;
            }
            case RANGE: {
                if (!this.correctPrayer && !spriteIDs.anyMatch(id -> id == 200)) break;
                color = this.config.getRangePrayerColor().getRGB();
                this.correctPrayer = true;
                break;
            }
            case MAGIC: {
                if (!this.correctPrayer && !spriteIDs.anyMatch(id -> id == 202)) break;
                color = this.config.getMagePrayerColor().getRGB();
                this.correctPrayer = true;
            }
        }
        if (color != 0) {
            text.setTextColor(color);
        } else {
            this.resetTextColor(text);
        }
        this.hideSkillIcons(xpdrop);
    }

    private void resetTextColor(Widget widget) {
        Color standardColor = this.config.standardColor();
        if (standardColor != null) {
            int color = standardColor.getRGB();
            widget.setTextColor(color);
        } else {
            EnumComposition colorEnum = this.client.getEnum(1169);
            int defaultColorId = this.client.getVarbitValue(4695);
            int color = colorEnum.getIntValue(defaultColorId);
            widget.setTextColor(color);
        }
    }

    private void hideSkillIcons(Widget xpdrop) {
        if (this.config.hideSkillIcons()) {
            Object[] children = xpdrop.getChildren();
            Arrays.fill(children, 1, children.length, null);
        }
    }

    private PrayerType getActivePrayerType() {
        for (XpPrayer prayer : XpPrayer.values()) {
            if (this.client.getServerVarbitValue(prayer.getPrayer().getVarbit()) != 1) continue;
            return prayer.getType();
        }
        return null;
    }

    @Subscribe
    public void onGameTick(GameTick tick) {
        this.correctPrayer = false;
        int fakeTickDelay = this.config.fakeXpDropDelay();
        if (fakeTickDelay == 0 || this.lastSkill == null) {
            return;
        }
        if (this.hasDropped) {
            this.hasDropped = false;
            this.tickCounter = 0;
            return;
        }
        if (++this.tickCounter % fakeTickDelay != 0) {
            return;
        }
        this.client.runScript(new Object[]{2091, this.lastSkill.ordinal(), this.previousExpGained});
    }

    @Subscribe
    public void onStatChanged(StatChanged statChanged) {
        Skill skill = statChanged.getSkill();
        int xp = statChanged.getXp();
        this.lastSkill = skill;
        Integer previous = this.previousSkillExpTable.put(skill, xp);
        if (previous != null) {
            this.previousExpGained = xp - previous;
            this.hasDropped = true;
        }
    }
}

