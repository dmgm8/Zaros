/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Strings
 *  com.google.inject.Provides
 *  javax.annotation.Nullable
 *  javax.inject.Inject
 *  net.runelite.api.Client
 *  net.runelite.api.Friend
 *  net.runelite.api.GameState
 *  net.runelite.api.Ignore
 *  net.runelite.api.IndexedSprite
 *  net.runelite.api.MenuAction
 *  net.runelite.api.Nameable
 *  net.runelite.api.events.MenuEntryAdded
 *  net.runelite.api.events.NameableNameChanged
 *  net.runelite.api.events.RemovedFriend
 *  net.runelite.api.events.ScriptCallbackEvent
 *  net.runelite.api.widgets.WidgetInfo
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package net.runelite.client.plugins.friendnotes;

import com.google.common.base.Strings;
import com.google.inject.Provides;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import javax.annotation.Nullable;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Friend;
import net.runelite.api.GameState;
import net.runelite.api.Ignore;
import net.runelite.api.IndexedSprite;
import net.runelite.api.MenuAction;
import net.runelite.api.Nameable;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.NameableNameChanged;
import net.runelite.api.events.RemovedFriend;
import net.runelite.api.events.ScriptCallbackEvent;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.chatbox.ChatboxPanelManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.friendnotes.FriendNoteOverlay;
import net.runelite.client.plugins.friendnotes.FriendNotesConfig;
import net.runelite.client.plugins.friendnotes.HoveredFriend;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.ColorUtil;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.util.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(name="Friend Notes", description="Store notes about your friends")
public class FriendNotesPlugin
extends Plugin {
    private static final Logger log = LoggerFactory.getLogger(FriendNotesPlugin.class);
    static final String CONFIG_GROUP = "friendNotes";
    private static final int CHARACTER_LIMIT = 128;
    private static final String KEY_PREFIX = "note_";
    private static final String ADD_NOTE = "Add Note";
    private static final String EDIT_NOTE = "Edit Note";
    private static final String NOTE_PROMPT_FORMAT = "%s's Notes<br>" + ColorUtil.prependColorTag("(Limit %s Characters)", new Color(0, 0, 170));
    private static final int ICON_WIDTH = 14;
    private static final int ICON_HEIGHT = 12;
    @Inject
    private Client client;
    @Inject
    private ConfigManager configManager;
    @Inject
    private OverlayManager overlayManager;
    @Inject
    private FriendNoteOverlay overlay;
    @Inject
    private ChatboxPanelManager chatboxPanelManager;
    @Inject
    private ClientThread clientThread;
    @Inject
    private FriendNotesConfig config;
    private HoveredFriend hoveredFriend = null;
    private int iconIdx = -1;
    private String currentlyLayouting;

    @Provides
    private FriendNotesConfig getConfig(ConfigManager configManager) {
        return configManager.getConfig(FriendNotesConfig.class);
    }

    @Override
    protected void startUp() throws Exception {
        this.overlayManager.add(this.overlay);
        this.clientThread.invoke(() -> {
            if (this.client.getModIcons() == null) {
                return false;
            }
            this.loadIcon();
            return true;
        });
        if (this.client.getGameState() == GameState.LOGGED_IN) {
            this.rebuildFriendsList();
            this.rebuildIgnoreList();
        }
    }

    @Override
    protected void shutDown() throws Exception {
        this.overlayManager.remove(this.overlay);
        if (this.client.getGameState() == GameState.LOGGED_IN) {
            this.rebuildFriendsList();
            this.rebuildIgnoreList();
        }
    }

    @Subscribe
    public void onConfigChanged(ConfigChanged event) {
        if (!event.getGroup().equals(CONFIG_GROUP)) {
            return;
        }
        switch (event.getKey()) {
            case "showIcons": {
                if (this.client.getGameState() != GameState.LOGGED_IN) break;
                this.rebuildFriendsList();
                this.rebuildIgnoreList();
            }
        }
    }

    private void setFriendNote(String displayName, String note) {
        if (Strings.isNullOrEmpty((String)note)) {
            this.configManager.unsetConfiguration(CONFIG_GROUP, KEY_PREFIX + displayName);
        } else {
            this.configManager.setConfiguration(CONFIG_GROUP, KEY_PREFIX + displayName, note);
        }
        if (this.client.getGameState() == GameState.LOGGED_IN) {
            this.rebuildFriendsList();
            this.rebuildIgnoreList();
        }
    }

    @Nullable
    private String getFriendNote(String displayName) {
        return this.configManager.getConfiguration(CONFIG_GROUP, KEY_PREFIX + displayName);
    }

    private void migrateFriendNote(String currentDisplayName, String prevDisplayName) {
        String prevNote;
        String currentNote = this.getFriendNote(currentDisplayName);
        if (currentNote == null && (prevNote = this.getFriendNote(prevDisplayName)) != null) {
            log.debug("Update friend's username: '{}' -> '{}'", (Object)prevDisplayName, (Object)currentDisplayName);
            this.setFriendNote(prevDisplayName, null);
            this.setFriendNote(currentDisplayName, prevNote);
        }
    }

    private void setHoveredFriend(String displayName) {
        String note;
        this.hoveredFriend = null;
        if (!Strings.isNullOrEmpty((String)displayName) && (note = this.getFriendNote(displayName)) != null) {
            this.hoveredFriend = new HoveredFriend(displayName, note);
        }
    }

    @Subscribe
    public void onMenuEntryAdded(MenuEntryAdded event) {
        int groupId = WidgetInfo.TO_GROUP((int)event.getActionParam1());
        if (groupId == WidgetInfo.FRIENDS_LIST.getGroupId() && event.getOption().equals("Message") || groupId == WidgetInfo.IGNORE_LIST.getGroupId() && event.getOption().equals("Delete")) {
            this.setHoveredFriend(Text.toJagexName(Text.removeTags(event.getTarget())));
            this.client.createMenuEntry(-1).setOption(this.hoveredFriend == null || this.hoveredFriend.getNote() == null ? ADD_NOTE : EDIT_NOTE).setType(MenuAction.RUNELITE).setTarget(event.getTarget()).onClick(e -> {
                String sanitizedTarget = Text.toJagexName(Text.removeTags(e.getTarget()));
                String note = this.getFriendNote(sanitizedTarget);
                this.chatboxPanelManager.openTextInput(String.format(NOTE_PROMPT_FORMAT, sanitizedTarget, 128)).value(Strings.nullToEmpty((String)note)).onDone(content -> {
                    if (content == null) {
                        return;
                    }
                    content = Text.removeTags(content).trim();
                    log.debug("Set note for '{}': '{}'", (Object)sanitizedTarget, content);
                    this.setFriendNote(sanitizedTarget, (String)content);
                }).build();
            });
        } else if (this.hoveredFriend != null) {
            this.hoveredFriend = null;
        }
    }

    @Subscribe
    public void onNameableNameChanged(NameableNameChanged event) {
        Nameable nameable = event.getNameable();
        if (nameable instanceof Friend || nameable instanceof Ignore) {
            String name = nameable.getName();
            String prevName = nameable.getPrevName();
            if (prevName != null) {
                this.migrateFriendNote(Text.toJagexName(name), Text.toJagexName(prevName));
            }
        }
    }

    @Subscribe
    public void onRemovedFriend(RemovedFriend event) {
        String displayName = Text.toJagexName(event.getNameable().getName());
        log.debug("Remove friend: '{}'", (Object)displayName);
        this.setFriendNote(displayName, null);
    }

    @Subscribe
    public void onScriptCallbackEvent(ScriptCallbackEvent event) {
        if (!this.config.showIcons() || this.iconIdx == -1) {
            return;
        }
        switch (event.getEventName()) {
            case "friendsChatSetText": {
                String sanitized;
                String[] stringStack = this.client.getStringStack();
                int stringStackSize = this.client.getStringStackSize();
                String rsn = stringStack[stringStackSize - 1];
                this.currentlyLayouting = sanitized = Text.toJagexName(Text.removeTags(rsn));
                if (this.getFriendNote(sanitized) == null) break;
                stringStack[stringStackSize - 1] = rsn + " <img=" + this.iconIdx + ">";
                break;
            }
            case "friendsChatSetPosition": {
                if (this.currentlyLayouting == null || this.getFriendNote(this.currentlyLayouting) == null) {
                    return;
                }
                int[] intStack = this.client.getIntStack();
                int intStackSize = this.client.getIntStackSize();
                int xpos = intStack[intStackSize - 4];
                intStack[intStackSize - 4] = xpos += 15;
            }
        }
    }

    private void rebuildFriendsList() {
        this.clientThread.invokeLater(() -> {
            log.debug("Rebuilding friends list");
            this.client.runScript(new Object[]{631, WidgetInfo.FRIEND_LIST_FULL_CONTAINER.getPackedId(), WidgetInfo.FRIEND_LIST_SORT_BY_NAME_BUTTON.getPackedId(), WidgetInfo.FRIEND_LIST_SORT_BY_LAST_WORLD_CHANGE_BUTTON.getPackedId(), WidgetInfo.FRIEND_LIST_SORT_BY_WORLD_BUTTON.getPackedId(), WidgetInfo.FRIEND_LIST_LEGACY_SORT_BUTTON.getPackedId(), WidgetInfo.FRIEND_LIST_NAMES_CONTAINER.getPackedId(), WidgetInfo.FRIEND_LIST_SCROLL_BAR.getPackedId(), WidgetInfo.FRIEND_LIST_LOADING_TEXT.getPackedId(), WidgetInfo.FRIEND_LIST_PREVIOUS_NAME_HOLDER.getPackedId()});
        });
    }

    private void rebuildIgnoreList() {
        this.clientThread.invokeLater(() -> {
            log.debug("Rebuilding ignore list");
            this.client.runScript(new Object[]{630, WidgetInfo.IGNORE_FULL_CONTAINER.getPackedId(), WidgetInfo.IGNORE_SORT_BY_NAME_BUTTON.getPackedId(), WidgetInfo.IGNORE_LEGACY_SORT_BUTTON.getPackedId(), WidgetInfo.IGNORE_NAMES_CONTAINER.getPackedId(), WidgetInfo.IGNORE_SCROLL_BAR.getPackedId(), WidgetInfo.IGNORE_LOADING_TEXT.getPackedId(), WidgetInfo.IGNORE_PREVIOUS_NAME_HOLDER.getPackedId()});
        });
    }

    private void loadIcon() {
        if (this.iconIdx != -1) {
            return;
        }
        BufferedImage iconImg = ImageUtil.loadImageResource(this.getClass(), "note_icon.png");
        if (iconImg == null) {
            return;
        }
        BufferedImage resized = ImageUtil.resizeImage(iconImg, 14, 12);
        IndexedSprite[] modIcons = this.client.getModIcons();
        assert (modIcons != null);
        IndexedSprite[] newIcons = Arrays.copyOf(modIcons, modIcons.length + 1);
        newIcons[newIcons.length - 1] = ImageUtil.getImageIndexedSprite(resized, this.client);
        this.iconIdx = newIcons.length - 1;
        this.client.setModIcons(newIcons);
    }

    public HoveredFriend getHoveredFriend() {
        return this.hoveredFriend;
    }
}

