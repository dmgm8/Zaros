/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.inject.Provides
 *  javax.inject.Inject
 *  net.runelite.api.Client
 *  net.runelite.api.Experience
 *  net.runelite.api.GameState
 *  net.runelite.api.Skill
 *  net.runelite.api.WorldType
 *  net.runelite.api.events.GameTick
 *  net.runelite.api.events.ScriptPostFired
 *  net.runelite.api.widgets.Widget
 *  net.runelite.api.widgets.WidgetInfo
 */
package net.runelite.client.plugins.combatlevel;

import com.google.inject.Provides;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Experience;
import net.runelite.api.GameState;
import net.runelite.api.Skill;
import net.runelite.api.WorldType;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.ScriptPostFired;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.combatlevel.CombatLevelConfig;
import net.runelite.client.plugins.combatlevel.CombatLevelOverlay;
import net.runelite.client.ui.overlay.OverlayManager;

@PluginDescriptor(name="Combat Level", description="Show a more accurate combat level in Combat Options panel and other combat level functions", tags={"wilderness", "attack", "range"})
public class CombatLevelPlugin
extends Plugin {
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.###");
    private static final String CONFIG_GROUP = "combatlevel";
    private static final String ATTACK_RANGE_CONFIG_KEY = "wildernessAttackLevelRange";
    private static final Pattern WILDERNESS_LEVEL_PATTERN = Pattern.compile("^Level: (\\d+)$");
    private static final int MIN_COMBAT_LEVEL = 3;
    @Inject
    private Client client;
    @Inject
    private CombatLevelConfig config;
    @Inject
    private CombatLevelOverlay overlay;
    @Inject
    private OverlayManager overlayManager;

    @Provides
    CombatLevelConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(CombatLevelConfig.class);
    }

    @Override
    protected void startUp() throws Exception {
        this.overlayManager.add(this.overlay);
        if (this.config.wildernessAttackLevelRange()) {
            this.appendAttackLevelRangeText();
        }
    }

    @Override
    protected void shutDown() throws Exception {
        String widgetText;
        this.overlayManager.remove(this.overlay);
        Widget combatLevelWidget = this.client.getWidget(WidgetInfo.COMBAT_LEVEL);
        if (combatLevelWidget != null && (widgetText = combatLevelWidget.getText()).contains(".")) {
            combatLevelWidget.setText(widgetText.substring(0, widgetText.indexOf(".")));
        }
        this.shutDownAttackLevelRange();
    }

    @Subscribe
    public void onGameTick(GameTick event) {
        if (this.client.getGameState() != GameState.LOGGED_IN) {
            return;
        }
        Widget combatLevelWidget = this.client.getWidget(WidgetInfo.COMBAT_LEVEL);
        if (combatLevelWidget == null || !this.config.showPreciseCombatLevel()) {
            return;
        }
        double combatLevelPrecise = Experience.getCombatLevelPrecise((int)this.client.getRealSkillLevel(Skill.ATTACK), (int)this.client.getRealSkillLevel(Skill.STRENGTH), (int)this.client.getRealSkillLevel(Skill.DEFENCE), (int)this.client.getRealSkillLevel(Skill.HITPOINTS), (int)this.client.getRealSkillLevel(Skill.MAGIC), (int)this.client.getRealSkillLevel(Skill.RANGED), (int)this.client.getRealSkillLevel(Skill.PRAYER));
        combatLevelWidget.setText("Combat Lvl: " + DECIMAL_FORMAT.format(combatLevelPrecise));
    }

    @Subscribe
    public void onConfigChanged(ConfigChanged event) {
        if (!CONFIG_GROUP.equals(event.getGroup()) || !ATTACK_RANGE_CONFIG_KEY.equals(event.getKey())) {
            return;
        }
        if (this.config.wildernessAttackLevelRange()) {
            this.appendAttackLevelRangeText();
        } else {
            this.shutDownAttackLevelRange();
        }
    }

    @Subscribe
    public void onScriptPostFired(ScriptPostFired scriptPostFired) {
        if (scriptPostFired.getScriptId() == 388 && this.config.wildernessAttackLevelRange()) {
            this.appendAttackLevelRangeText();
        }
    }

    private void appendAttackLevelRangeText() {
        Widget wildernessLevelWidget = this.client.getWidget(WidgetInfo.PVP_WILDERNESS_LEVEL);
        if (wildernessLevelWidget == null) {
            return;
        }
        String wildernessLevelText = wildernessLevelWidget.getText();
        Matcher m = WILDERNESS_LEVEL_PATTERN.matcher(wildernessLevelText);
        if (!m.matches() || WorldType.isPvpWorld((Collection)this.client.getWorldType())) {
            return;
        }
        int wildernessLevel = Integer.parseInt(m.group(1));
        int combatLevel = this.client.getLocalPlayer().getCombatLevel();
        wildernessLevelWidget.setText(wildernessLevelText + "<br>" + CombatLevelPlugin.combatAttackRange(combatLevel, wildernessLevel));
    }

    private void shutDownAttackLevelRange() {
        String wildernessLevelText;
        if (WorldType.isPvpWorld((Collection)this.client.getWorldType())) {
            return;
        }
        Widget wildernessLevelWidget = this.client.getWidget(WidgetInfo.PVP_WILDERNESS_LEVEL);
        if (wildernessLevelWidget != null && (wildernessLevelText = wildernessLevelWidget.getText()).contains("<br>")) {
            wildernessLevelWidget.setText(wildernessLevelText.substring(0, wildernessLevelText.indexOf("<br>")));
        }
    }

    private static String combatAttackRange(int combatLevel, int wildernessLevel) {
        return Math.max(3, combatLevel - wildernessLevel) + "-" + Math.min(126, combatLevel + wildernessLevel);
    }
}

