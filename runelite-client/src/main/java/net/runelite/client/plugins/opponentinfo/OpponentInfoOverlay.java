/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Strings
 *  javax.inject.Inject
 *  net.runelite.api.Actor
 *  net.runelite.api.Client
 *  net.runelite.api.MenuAction
 *  net.runelite.api.NPC
 *  net.runelite.api.NPCComposition
 *  net.runelite.api.VarPlayer
 */
package net.runelite.client.plugins.opponentinfo;

import com.google.common.base.Strings;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import javax.inject.Inject;
import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.NPC;
import net.runelite.api.NPCComposition;
import net.runelite.api.VarPlayer;
import net.runelite.client.game.NPCManager;
import net.runelite.client.hiscore.HiscoreManager;
import net.runelite.client.plugins.opponentinfo.HitpointsDisplayStyle;
import net.runelite.client.plugins.opponentinfo.OpponentInfoConfig;
import net.runelite.client.plugins.opponentinfo.OpponentInfoPlugin;
import net.runelite.client.ui.overlay.OverlayMenuEntry;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.components.ProgressBarComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;
import net.runelite.client.util.Text;

class OpponentInfoOverlay
extends OverlayPanel {
    private static final Color HP_GREEN = new Color(0, 146, 54, 230);
    private static final Color HP_RED = new Color(102, 15, 16, 230);
    private final Client client;
    private final OpponentInfoPlugin opponentInfoPlugin;
    private final OpponentInfoConfig opponentInfoConfig;
    private final HiscoreManager hiscoreManager;
    private final NPCManager npcManager;
    private Integer lastCurrentHealth;
    private Integer lastMaxHealth;
    private int lastRatio = 0;
    private int lastHealthScale = 0;
    private String opponentName;

    @Inject
    private OpponentInfoOverlay(Client client, OpponentInfoPlugin opponentInfoPlugin, OpponentInfoConfig opponentInfoConfig, HiscoreManager hiscoreManager, NPCManager npcManager) {
        super(opponentInfoPlugin);
        this.client = client;
        this.opponentInfoPlugin = opponentInfoPlugin;
        this.opponentInfoConfig = opponentInfoConfig;
        this.hiscoreManager = hiscoreManager;
        this.npcManager = npcManager;
        this.setPosition(OverlayPosition.TOP_LEFT);
        this.setPriority(OverlayPriority.HIGH);
        this.panelComponent.setBorder(new Rectangle(2, 2, 2, 2));
        this.panelComponent.setGap(new Point(0, 2));
        this.getMenuEntries().add(new OverlayMenuEntry(MenuAction.RUNELITE_OVERLAY_CONFIG, "Configure", "Opponent info overlay"));
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        Actor opponent = this.opponentInfoPlugin.getLastOpponent();
        if (opponent == null) {
            this.opponentName = null;
            return null;
        }
        if (opponent.getName() != null && opponent.getHealthScale() > 0) {
            int hpHudMaxHitpoints;
            String longName;
            NPCComposition composition;
            this.lastRatio = opponent.getHealthRatio();
            this.lastHealthScale = opponent.getHealthScale();
            this.opponentName = Text.removeTags(opponent.getName());
            this.lastMaxHealth = null;
            this.lastCurrentHealth = null;
            if (opponent instanceof NPC && (composition = ((NPC)opponent).getTransformedComposition()) != null && !Strings.isNullOrEmpty((String)(longName = composition.getStringValue(510)))) {
                this.opponentName = longName;
            }
            if ((hpHudMaxHitpoints = this.client.getVar(15129)) > 0) {
                this.lastCurrentHealth = this.client.getVar(15128);
                this.lastMaxHealth = hpHudMaxHitpoints;
            }
        }
        if (this.opponentName == null || this.hasHpHud(opponent)) {
            return null;
        }
        FontMetrics fontMetrics = graphics.getFontMetrics();
        int panelWidth = Math.max(129, fontMetrics.stringWidth(this.opponentName) + 4 + 4);
        this.panelComponent.setPreferredSize(new Dimension(panelWidth, 0));
        this.panelComponent.getChildren().add(TitleComponent.builder().text(this.opponentName).build());
        if (this.lastRatio >= 0 && this.lastHealthScale > 0) {
            ProgressBarComponent progressBarComponent = new ProgressBarComponent();
            progressBarComponent.setBackgroundColor(HP_RED);
            progressBarComponent.setForegroundColor(HP_GREEN);
            HitpointsDisplayStyle displayStyle = this.opponentInfoConfig.hitpointsDisplayStyle();
            if ((displayStyle == HitpointsDisplayStyle.HITPOINTS || displayStyle == HitpointsDisplayStyle.BOTH) && this.lastMaxHealth != null) {
                int health = 0;
                if (this.lastCurrentHealth != null) {
                    health = this.lastCurrentHealth;
                } else if (this.lastRatio > 0) {
                    int maxHealth;
                    int minHealth = 1;
                    if (this.lastHealthScale > 1) {
                        if (this.lastRatio > 1) {
                            minHealth = (this.lastMaxHealth * (this.lastRatio - 1) + this.lastHealthScale - 2) / (this.lastHealthScale - 1);
                        }
                        if ((maxHealth = (this.lastMaxHealth * this.lastRatio - 1) / (this.lastHealthScale - 1)) > this.lastMaxHealth) {
                            maxHealth = this.lastMaxHealth;
                        }
                    } else {
                        maxHealth = this.lastMaxHealth;
                    }
                    health = (minHealth + maxHealth + 1) / 2;
                }
                ProgressBarComponent.LabelDisplayMode progressBarDisplayMode = displayStyle == HitpointsDisplayStyle.BOTH ? ProgressBarComponent.LabelDisplayMode.BOTH : ProgressBarComponent.LabelDisplayMode.FULL;
                progressBarComponent.setLabelDisplayMode(progressBarDisplayMode);
                progressBarComponent.setMaximum(this.lastMaxHealth.intValue());
                progressBarComponent.setValue(health);
            } else {
                float floatRatio = (float)this.lastRatio / (float)this.lastHealthScale;
                progressBarComponent.setValue((double)floatRatio * 100.0);
            }
            this.panelComponent.getChildren().add(progressBarComponent);
        }
        return super.render(graphics);
    }

    private boolean hasHpHud(Actor opponent) {
        boolean settingEnabled;
        boolean bl = settingEnabled = this.client.getVarbitValue(12389) == 0;
        if (settingEnabled && opponent instanceof NPC) {
            int opponentId = this.client.getVarpValue(VarPlayer.HP_HUD_NPC_ID);
            int opponentUid = this.client.getVar(VarPlayer.HP_HUD_UID);
            return opponentId != -1 && opponentId == ((NPC)opponent).getId() || opponentUid != 0 && opponentUid == ((NPC)opponent).getIndex();
        }
        return false;
    }
}

