/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.inject.Provides
 *  javax.inject.Inject
 *  net.runelite.api.Client
 *  net.runelite.api.GameState
 *  net.runelite.api.events.FocusChanged
 *  net.runelite.api.events.ScriptPostFired
 *  net.runelite.api.events.WidgetLoaded
 *  net.runelite.api.widgets.Widget
 *  net.runelite.api.widgets.WidgetInfo
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package net.runelite.client.plugins.antidrag;

import com.google.inject.Provides;
import java.awt.event.KeyEvent;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.FocusChanged;
import net.runelite.api.events.ScriptPostFired;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.input.KeyListener;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.antidrag.AntiDragConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(name="Anti Drag", description="Prevent dragging an item for a specified delay", tags={"antidrag", "delay", "inventory", "items"}, enabledByDefault=false)
public class AntiDragPlugin
extends Plugin
implements KeyListener {
    private static final Logger log = LoggerFactory.getLogger(AntiDragPlugin.class);
    static final String CONFIG_GROUP = "antiDrag";
    private static final int DEFAULT_DELAY = 5;
    @Inject
    private Client client;
    @Inject
    private ClientThread clientThread;
    @Inject
    private AntiDragConfig config;
    @Inject
    private KeyManager keyManager;
    private boolean shiftHeld;
    private boolean ctrlHeld;

    @Provides
    AntiDragConfig getConfig(ConfigManager configManager) {
        return configManager.getConfig(AntiDragConfig.class);
    }

    @Override
    protected void startUp() throws Exception {
        if (this.client.getGameState() == GameState.LOGGED_IN) {
            this.clientThread.invokeLater(() -> {
                if (!this.config.onShiftOnly()) {
                    this.setDragDelay();
                }
            });
        }
        this.keyManager.registerKeyListener(this);
    }

    @Override
    protected void shutDown() throws Exception {
        this.clientThread.invoke(this::resetDragDelay);
        this.keyManager.unregisterKeyListener(this);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == 17 && this.config.disableOnCtrl() && !this.config.onShiftOnly()) {
            this.resetDragDelay();
            this.ctrlHeld = true;
        } else if (e.getKeyCode() == 16 && this.config.onShiftOnly()) {
            this.setDragDelay();
            this.shiftHeld = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == 17 && this.config.disableOnCtrl() && !this.config.onShiftOnly()) {
            this.setDragDelay();
            this.ctrlHeld = false;
        } else if (e.getKeyCode() == 16 && this.config.onShiftOnly()) {
            this.resetDragDelay();
            this.shiftHeld = false;
        }
    }

    private boolean isOverriding() {
        return (!this.config.onShiftOnly() || this.shiftHeld) && !this.ctrlHeld;
    }

    @Subscribe
    public void onConfigChanged(ConfigChanged event) {
        if (event.getGroup().equals(CONFIG_GROUP)) {
            if (!this.config.disableOnCtrl()) {
                this.ctrlHeld = false;
            }
            if (this.config.onShiftOnly()) {
                this.shiftHeld = false;
                this.clientThread.invoke(this::resetDragDelay);
            } else {
                this.clientThread.invoke(this::setDragDelay);
            }
        }
    }

    @Subscribe
    public void onFocusChanged(FocusChanged focusChanged) {
        if (!focusChanged.isFocused()) {
            this.shiftHeld = false;
            this.ctrlHeld = false;
            this.clientThread.invoke(this::resetDragDelay);
        } else if (!this.config.onShiftOnly()) {
            this.clientThread.invoke(this::setDragDelay);
        }
    }

    @Subscribe
    public void onWidgetLoaded(WidgetLoaded widgetLoaded) {
        if (!this.isOverriding()) {
            return;
        }
        if (widgetLoaded.getGroupId() == 12 || widgetLoaded.getGroupId() == 15 || widgetLoaded.getGroupId() == 192) {
            this.setBankDragDelay(this.config.dragDelay());
        } else if (widgetLoaded.getGroupId() == 149) {
            this.setInvDragDelay(this.config.dragDelay());
        }
    }

    @Subscribe
    private void onScriptPostFired(ScriptPostFired ev) {
        if (ev.getScriptId() == 6011) {
            Widget inv = this.client.getWidget(WidgetInfo.INVENTORY);
            int delay = this.config.dragDelay();
            boolean overriding = this.isOverriding();
            for (Widget child : inv.getDynamicChildren()) {
                child.setOnMouseRepeatListener((Object[])null);
                if (!overriding) continue;
                child.setDragDeadTime(delay);
            }
        } else if (ev.getScriptId() == 1607) {
            this.setCoxDragDelay(this.config.dragDelay());
        }
    }

    private static void applyDragDelay(Widget widget, int delay) {
        if (widget != null) {
            for (Widget item : widget.getDynamicChildren()) {
                item.setDragDeadTime(delay);
            }
        }
    }

    private void setBankDragDelay(int delay) {
        Widget bankItemContainer = this.client.getWidget(WidgetInfo.BANK_ITEM_CONTAINER);
        Widget bankInventoryItemsContainer = this.client.getWidget(WidgetInfo.BANK_INVENTORY_ITEMS_CONTAINER);
        Widget bankDepositContainer = this.client.getWidget(WidgetInfo.DEPOSIT_BOX_INVENTORY_ITEMS_CONTAINER);
        Widget coxPrivateChest = this.client.getWidget(WidgetInfo.RAIDS_PRIVATE_STORAGE_ITEM_CONTAINER);
        AntiDragPlugin.applyDragDelay(bankItemContainer, delay);
        AntiDragPlugin.applyDragDelay(bankInventoryItemsContainer, delay);
        AntiDragPlugin.applyDragDelay(bankDepositContainer, delay);
        AntiDragPlugin.applyDragDelay(coxPrivateChest, delay);
    }

    private void setInvDragDelay(int delay) {
        Widget inventory = this.client.getWidget(WidgetInfo.INVENTORY);
        AntiDragPlugin.applyDragDelay(inventory, delay);
    }

    private void setCoxDragDelay(int delay) {
        Widget coxChest = this.client.getWidget(WidgetInfo.RAIDS_PRIVATE_STORAGE_ITEM_CONTAINER);
        AntiDragPlugin.applyDragDelay(coxChest, delay);
    }

    private void setDragDelay() {
        int delay = this.config.dragDelay();
        log.debug("Set delay to {}", (Object)delay);
        this.client.setInventoryDragDelay(delay);
        this.setInvDragDelay(delay);
        this.setBankDragDelay(delay);
        this.setCoxDragDelay(delay);
    }

    private void resetDragDelay() {
        log.debug("Reset delay to {}", (Object)5);
        this.client.setInventoryDragDelay(5);
        this.setInvDragDelay(5);
        this.setBankDragDelay(5);
        this.setCoxDragDelay(5);
    }
}

