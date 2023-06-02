/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.annotations.VisibleForTesting
 *  com.google.common.collect.HashBasedTable
 *  com.google.common.collect.Table
 *  com.google.inject.Provides
 *  javax.annotation.Nullable
 *  javax.inject.Inject
 *  net.runelite.api.Client
 *  net.runelite.api.GameState
 *  net.runelite.api.Skill
 *  net.runelite.api.VarPlayer
 *  net.runelite.api.events.GameStateChanged
 *  net.runelite.api.events.ScriptPostFired
 *  net.runelite.api.events.VarbitChanged
 *  net.runelite.api.widgets.Widget
 *  net.runelite.api.widgets.WidgetInfo
 */
package net.runelite.client.plugins.attackstyles;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.inject.Provides;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.Nullable;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Skill;
import net.runelite.api.VarPlayer;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.ScriptPostFired;
import net.runelite.api.events.VarbitChanged;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.attackstyles.AttackStyle;
import net.runelite.client.plugins.attackstyles.AttackStylesConfig;
import net.runelite.client.plugins.attackstyles.AttackStylesOverlay;
import net.runelite.client.plugins.attackstyles.WeaponType;
import net.runelite.client.ui.overlay.OverlayManager;

@PluginDescriptor(name="Attack Styles", description="Show your current attack style as an overlay", tags={"combat", "defence", "magic", "overlay", "ranged", "strength", "warn", "pure"})
public class AttackStylesPlugin
extends Plugin {
    private int equippedWeaponTypeVarbit = -1;
    private AttackStyle attackStyle;
    private final Set<Skill> warnedSkills = new HashSet<Skill>();
    private boolean warnedSkillSelected = false;
    private final Table<WeaponType, WidgetInfo, Boolean> widgetsToHide = HashBasedTable.create();
    @Inject
    private Client client;
    @Inject
    private ClientThread clientThread;
    @Inject
    private AttackStylesConfig config;
    @Inject
    private OverlayManager overlayManager;
    @Inject
    private AttackStylesOverlay overlay;

    @Provides
    AttackStylesConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(AttackStylesConfig.class);
    }

    @Override
    protected void startUp() throws Exception {
        this.overlayManager.add(this.overlay);
        if (this.client.getGameState() == GameState.LOGGED_IN) {
            this.clientThread.invoke(this::start);
        }
    }

    private void start() {
        this.resetWarnings();
        int attackStyleVarbit = this.client.getVarpValue(VarPlayer.ATTACK_STYLE);
        this.equippedWeaponTypeVarbit = this.client.getVarbitValue(357);
        int castingModeVarbit = this.client.getVarbitValue(2668);
        this.updateAttackStyle(this.equippedWeaponTypeVarbit, attackStyleVarbit, castingModeVarbit);
        this.updateWarning(false);
        this.processWidgets();
    }

    @Override
    protected void shutDown() {
        this.overlayManager.remove(this.overlay);
        this.hideWarnedStyles(false);
        this.processWidgets();
        this.hideWidget(this.client.getWidget(WidgetInfo.COMBAT_AUTO_RETALIATE), false);
    }

    @Nullable
    public AttackStyle getAttackStyle() {
        return this.attackStyle;
    }

    public boolean isWarnedSkillSelected() {
        return this.warnedSkillSelected;
    }

    @Subscribe
    public void onScriptPostFired(ScriptPostFired scriptPostFired) {
        if (scriptPostFired.getScriptId() == 420) {
            this.processWidgets();
        }
    }

    private void processWidgets() {
        WeaponType equippedWeaponType = WeaponType.getWeaponType(this.equippedWeaponTypeVarbit);
        if (this.widgetsToHide.containsRow((Object)equippedWeaponType)) {
            for (WidgetInfo widgetKey : this.widgetsToHide.row((Object)equippedWeaponType).keySet()) {
                this.hideWidget(this.client.getWidget(widgetKey), (Boolean)this.widgetsToHide.get((Object)equippedWeaponType, (Object)widgetKey));
            }
        }
        this.hideWidget(this.client.getWidget(WidgetInfo.COMBAT_AUTO_RETALIATE), this.config.hideAutoRetaliate());
    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged event) {
        if (event.getGameState() == GameState.LOGGED_IN) {
            this.resetWarnings();
        }
    }

    @Subscribe
    public void onVarbitChanged(VarbitChanged event) {
        if (event.getVarpId() == VarPlayer.ATTACK_STYLE.getId() || event.getVarbitId() == 357 || event.getVarbitId() == 2668) {
            int currentAttackStyleVarbit = this.client.getVarpValue(VarPlayer.ATTACK_STYLE);
            int currentEquippedWeaponTypeVarbit = this.client.getVarbitValue(357);
            int currentCastingModeVarbit = this.client.getVarbitValue(2668);
            boolean weaponSwitch = currentEquippedWeaponTypeVarbit != this.equippedWeaponTypeVarbit;
            this.equippedWeaponTypeVarbit = currentEquippedWeaponTypeVarbit;
            this.updateAttackStyle(this.equippedWeaponTypeVarbit, currentAttackStyleVarbit, currentCastingModeVarbit);
            this.updateWarning(weaponSwitch);
            if (weaponSwitch) {
                this.processWidgets();
            }
        }
    }

    @Subscribe
    public void onConfigChanged(ConfigChanged event) {
        if (event.getGroup().equals("attackIndicator")) {
            boolean enabled = Boolean.TRUE.toString().equals(event.getNewValue());
            switch (event.getKey()) {
                case "warnForDefensive": {
                    this.updateWarnedSkills(enabled, Skill.DEFENCE);
                    break;
                }
                case "warnForAttack": {
                    this.updateWarnedSkills(enabled, Skill.ATTACK);
                    break;
                }
                case "warnForStrength": {
                    this.updateWarnedSkills(enabled, Skill.STRENGTH);
                    break;
                }
                case "warnForRanged": {
                    this.updateWarnedSkills(enabled, Skill.RANGED);
                    break;
                }
                case "warnForMagic": {
                    this.updateWarnedSkills(enabled, Skill.MAGIC);
                    break;
                }
                case "removeWarnedStyles": {
                    this.hideWarnedStyles(enabled);
                }
            }
            this.processWidgets();
        }
    }

    private void resetWarnings() {
        this.updateWarnedSkills(this.config.warnForAttack(), Skill.ATTACK);
        this.updateWarnedSkills(this.config.warnForStrength(), Skill.STRENGTH);
        this.updateWarnedSkills(this.config.warnForDefence(), Skill.DEFENCE);
        this.updateWarnedSkills(this.config.warnForRanged(), Skill.RANGED);
        this.updateWarnedSkills(this.config.warnForMagic(), Skill.MAGIC);
    }

    private void updateAttackStyle(int equippedWeaponType, int attackStyleIndex, int castingMode) {
        AttackStyle[] attackStyles = WeaponType.getWeaponType(equippedWeaponType).getAttackStyles();
        if (attackStyleIndex < attackStyles.length) {
            this.attackStyle = attackStyles[attackStyleIndex];
            if (this.attackStyle == null) {
                this.attackStyle = AttackStyle.OTHER;
            } else if (this.attackStyle == AttackStyle.CASTING && castingMode == 1) {
                this.attackStyle = AttackStyle.DEFENSIVE_CASTING;
            }
        }
    }

    private void updateWarnedSkills(boolean enabled, Skill skill) {
        if (enabled) {
            this.warnedSkills.add(skill);
        } else {
            this.warnedSkills.remove((Object)skill);
        }
        this.updateWarning(false);
    }

    private void updateWarning(boolean weaponSwitch) {
        this.warnedSkillSelected = false;
        if (this.attackStyle != null) {
            for (Skill skill : this.attackStyle.getSkills()) {
                if (!this.warnedSkills.contains((Object)skill)) continue;
                if (weaponSwitch) {
                    // empty if block
                }
                this.warnedSkillSelected = true;
                break;
            }
        }
        this.hideWarnedStyles(this.config.removeWarnedStyles());
    }

    private void hideWarnedStyles(boolean enabled) {
        WeaponType equippedWeaponType = WeaponType.getWeaponType(this.equippedWeaponTypeVarbit);
        if (equippedWeaponType == null) {
            return;
        }
        AttackStyle[] attackStyles = equippedWeaponType.getAttackStyles();
        block7: for (int i = 0; i < attackStyles.length; ++i) {
            AttackStyle attackStyle = attackStyles[i];
            if (attackStyle == null) continue;
            boolean warnedSkill = false;
            for (Skill skill : attackStyle.getSkills()) {
                if (!this.warnedSkills.contains((Object)skill)) continue;
                warnedSkill = true;
                break;
            }
            if (attackStyle == AttackStyle.DEFENSIVE_CASTING || !enabled) {
                this.widgetsToHide.put((Object)equippedWeaponType, (Object)WidgetInfo.COMBAT_DEFENSIVE_SPELL_BOX, (Object)(enabled && warnedSkill ? 1 : 0));
                this.widgetsToHide.put((Object)equippedWeaponType, (Object)WidgetInfo.COMBAT_DEFENSIVE_SPELL_ICON, (Object)(enabled && warnedSkill ? 1 : 0));
                this.widgetsToHide.put((Object)equippedWeaponType, (Object)WidgetInfo.COMBAT_DEFENSIVE_SPELL_SHIELD, (Object)(enabled && warnedSkill ? 1 : 0));
                this.widgetsToHide.put((Object)equippedWeaponType, (Object)WidgetInfo.COMBAT_DEFENSIVE_SPELL_TEXT, (Object)(enabled && warnedSkill ? 1 : 0));
            }
            switch (i) {
                case 0: {
                    this.widgetsToHide.put((Object)equippedWeaponType, (Object)WidgetInfo.COMBAT_STYLE_ONE, (Object)(enabled && warnedSkill ? 1 : 0));
                    continue block7;
                }
                case 1: {
                    this.widgetsToHide.put((Object)equippedWeaponType, (Object)WidgetInfo.COMBAT_STYLE_TWO, (Object)(enabled && warnedSkill ? 1 : 0));
                    continue block7;
                }
                case 2: {
                    this.widgetsToHide.put((Object)equippedWeaponType, (Object)WidgetInfo.COMBAT_STYLE_THREE, (Object)(enabled && warnedSkill ? 1 : 0));
                    continue block7;
                }
                case 3: {
                    this.widgetsToHide.put((Object)equippedWeaponType, (Object)WidgetInfo.COMBAT_STYLE_FOUR, (Object)(enabled && warnedSkill ? 1 : 0));
                    continue block7;
                }
                case 4: {
                    this.widgetsToHide.put((Object)equippedWeaponType, (Object)WidgetInfo.COMBAT_SPELLS, (Object)(enabled && warnedSkill ? 1 : 0));
                    continue block7;
                }
            }
        }
    }

    private void hideWidget(Widget widget, boolean hidden) {
        if (widget != null) {
            widget.setHidden(hidden);
        }
    }

    @VisibleForTesting
    Set<Skill> getWarnedSkills() {
        return this.warnedSkills;
    }

    @VisibleForTesting
    Table<WeaponType, WidgetInfo, Boolean> getHiddenWidgets() {
        return this.widgetsToHide;
    }
}

