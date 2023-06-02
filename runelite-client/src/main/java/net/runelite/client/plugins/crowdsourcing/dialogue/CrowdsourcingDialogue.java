/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  net.runelite.api.Client
 *  net.runelite.api.events.GameTick
 *  net.runelite.api.widgets.Widget
 *  net.runelite.api.widgets.WidgetInfo
 */
package net.runelite.client.plugins.crowdsourcing.dialogue;

import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.crowdsourcing.CrowdsourcingManager;
import net.runelite.client.plugins.crowdsourcing.dialogue.DialogueOptionsData;
import net.runelite.client.plugins.crowdsourcing.dialogue.NpcDialogueData;
import net.runelite.client.plugins.crowdsourcing.dialogue.PlayerDialogueData;
import net.runelite.client.plugins.crowdsourcing.dialogue.StartEndData;

public class CrowdsourcingDialogue {
    private static final String USERNAME_TOKEN = "%USERNAME%";
    @Inject
    private Client client;
    @Inject
    private CrowdsourcingManager manager;
    private boolean inDialogue = false;
    private String lastNpcDialogueText = null;
    private String lastPlayerDialogueText = null;
    private Widget[] dialogueOptions;

    private String sanitize(String dialogue) {
        String username = this.client.getLocalPlayer().getName();
        return dialogue.replaceAll(username, USERNAME_TOKEN);
    }

    @Subscribe
    public void onGameTick(GameTick tick) {
        Widget npcDialogueTextWidget = this.client.getWidget(WidgetInfo.DIALOG_NPC_TEXT);
        Widget playerDialogueTextWidget = this.client.getWidget(WidgetInfo.DIALOG_PLAYER_TEXT);
        Widget playerDialogueOptionsWidget = this.client.getWidget(WidgetInfo.DIALOG_OPTION_OPTIONS);
        if (!(this.inDialogue || npcDialogueTextWidget == null && playerDialogueTextWidget == null && playerDialogueOptionsWidget == null)) {
            this.inDialogue = true;
            this.manager.storeEvent(new StartEndData(true));
        } else if (this.inDialogue && npcDialogueTextWidget == null && playerDialogueTextWidget == null && playerDialogueOptionsWidget == null) {
            this.inDialogue = false;
            this.manager.storeEvent(new StartEndData(false));
        }
        if (npcDialogueTextWidget != null && !npcDialogueTextWidget.getText().equals(this.lastNpcDialogueText)) {
            this.lastNpcDialogueText = npcDialogueTextWidget.getText();
            String npcName = this.client.getWidget(WidgetInfo.DIALOG_NPC_NAME).getText();
            NpcDialogueData data = new NpcDialogueData(this.sanitize(this.lastNpcDialogueText), npcName);
            this.manager.storeEvent(data);
        }
        if (playerDialogueTextWidget != null && !playerDialogueTextWidget.getText().equals(this.lastPlayerDialogueText)) {
            this.lastPlayerDialogueText = playerDialogueTextWidget.getText();
            PlayerDialogueData data = new PlayerDialogueData(this.sanitize(this.lastPlayerDialogueText));
            this.manager.storeEvent(data);
        }
        if (playerDialogueOptionsWidget != null && playerDialogueOptionsWidget.getChildren() != this.dialogueOptions) {
            this.dialogueOptions = playerDialogueOptionsWidget.getChildren();
            String[] optionsText = new String[this.dialogueOptions.length];
            for (int i = 0; i < this.dialogueOptions.length; ++i) {
                optionsText[i] = this.sanitize(this.dialogueOptions[i].getText());
            }
            DialogueOptionsData data = new DialogueOptionsData(optionsText);
            this.manager.storeEvent(data);
        }
    }
}

