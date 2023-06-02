/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  javax.inject.Singleton
 *  net.runelite.api.Client
 *  net.runelite.api.events.GameTick
 *  net.runelite.api.widgets.Widget
 *  net.runelite.api.widgets.WidgetInfo
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package net.runelite.client.plugins.timetracking.farming;

import java.lang.reflect.Type;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.Client;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.timetracking.farming.FarmingPatch;
import net.runelite.client.plugins.timetracking.farming.FarmingRegion;
import net.runelite.client.plugins.timetracking.farming.FarmingWorld;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class PaymentTracker {
    private static final Logger log = LoggerFactory.getLogger(PaymentTracker.class);
    private static final String PAYMENT_MALE = "That'll do nicely, sir. Leave it with me - I'll make sure<br>that patch grows for you.";
    private static final String PAYMENT_FEMALE = "That'll do nicely, madam. Leave it with me - I'll make<br>sure that patch grows for you.";
    private final Client client;
    private final ConfigManager configManager;
    private final FarmingWorld farmingWorld;

    @Subscribe
    public void onGameTick(GameTick gameTick) {
        Widget text = this.client.getWidget(WidgetInfo.DIALOG_NPC_TEXT);
        if (text == null || !PAYMENT_MALE.equals(text.getText()) && !PAYMENT_FEMALE.equals(text.getText())) {
            return;
        }
        Widget name = this.client.getWidget(WidgetInfo.DIALOG_NPC_NAME);
        Widget head = this.client.getWidget(WidgetInfo.DIALOG_NPC_HEAD_MODEL);
        if (name == null || head == null || head.getModelType() != 2) {
            return;
        }
        int npcId = head.getModelId();
        FarmingPatch patch = this.findPatchForNpc(npcId);
        if (patch == null) {
            return;
        }
        if (this.getProtectedState(patch)) {
            return;
        }
        log.debug("Detected patch payment for {} ({})", (Object)name.getText(), (Object)npcId);
        this.setProtectedState(patch, true);
    }

    private static String configKey(FarmingPatch fp) {
        return fp.configKey() + "." + "protected";
    }

    public void setProtectedState(FarmingPatch fp, boolean state) {
        if (!state) {
            this.configManager.unsetRSProfileConfiguration("timetracking", PaymentTracker.configKey(fp));
        } else {
            this.configManager.setRSProfileConfiguration("timetracking", PaymentTracker.configKey(fp), state);
        }
    }

    public boolean getProtectedState(FarmingPatch fp) {
        return Boolean.TRUE.equals(this.configManager.getRSProfileConfiguration("timetracking", PaymentTracker.configKey(fp), (Type)((Object)Boolean.class)));
    }

    private FarmingPatch findPatchForNpc(int npcId) {
        FarmingPatch p = null;
        for (FarmingRegion region : this.farmingWorld.getRegionsForLocation(this.client.getLocalPlayer().getWorldLocation())) {
            for (FarmingPatch patch : region.getPatches()) {
                if (patch.getFarmer() != npcId) continue;
                if (p != null) {
                    log.debug("Ambiguous payment to {} between {} and {}", new Object[]{npcId, p, patch});
                    return null;
                }
                p = patch;
            }
        }
        return p;
    }

    @Inject
    private PaymentTracker(Client client, ConfigManager configManager, FarmingWorld farmingWorld) {
        this.client = client;
        this.configManager = configManager;
        this.farmingWorld = farmingWorld;
    }
}

