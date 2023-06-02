/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  javax.inject.Singleton
 */
package net.runelite.client.ui;

import java.util.HashSet;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.events.NavigationButtonAdded;
import net.runelite.client.events.NavigationButtonRemoved;
import net.runelite.client.ui.NavigationButton;

@Singleton
public class ClientToolbar {
    private final EventBus eventBus;
    private final Set<NavigationButton> buttons = new HashSet<NavigationButton>();

    @Inject
    private ClientToolbar(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void addNavigation(NavigationButton button) {
        if (this.buttons.contains(button)) {
            return;
        }
        if (this.buttons.add(button)) {
            this.eventBus.post(new NavigationButtonAdded(button));
        }
    }

    public void removeNavigation(NavigationButton button) {
        if (this.buttons.remove(button)) {
            this.eventBus.post(new NavigationButtonRemoved(button));
        }
    }
}

