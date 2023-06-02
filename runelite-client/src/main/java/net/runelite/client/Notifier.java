/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Strings
 *  com.google.common.escape.Escaper
 *  com.google.common.escape.Escapers
 *  com.google.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  net.runelite.api.ChatMessageType
 *  net.runelite.api.Client
 *  net.runelite.api.GameState
 *  net.runelite.api.Player
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package net.runelite.client;

import com.google.common.base.Strings;
import com.google.common.escape.Escaper;
import com.google.common.escape.Escapers;
import com.google.inject.Inject;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Player;
import net.runelite.client.RuneLite;
import net.runelite.client.RuneLiteProperties;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.config.FlashNotification;
import net.runelite.client.config.RuneLiteConfig;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.events.NotificationFired;
import net.runelite.client.ui.ClientUI;
import net.runelite.client.util.OSType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class Notifier {
    private static final Logger log = LoggerFactory.getLogger(Notifier.class);
    private static final String DOUBLE_QUOTE = "\"";
    private static final Escaper SHELL_ESCAPE = Escapers.builder().addEscape('\"', "'").build();
    private static final int MINIMUM_FLASH_DURATION_MILLIS = 2000;
    private static final int MINIMUM_FLASH_DURATION_TICKS = 100;
    private static final File NOTIFICATION_FILE = new File(RuneLite.RUNELITE_DIR, "notification.wav");
    private static final long CLIP_MTIME_UNLOADED = -2L;
    private static final long CLIP_MTIME_BUILTIN = -1L;
    private final Client client;
    private final RuneLiteConfig runeLiteConfig;
    private final ClientUI clientUI;
    private final ScheduledExecutorService executorService;
    private final ChatMessageManager chatMessageManager;
    private final EventBus eventBus;
    private final String appName;
    private final Path notifyIconPath;
    private boolean terminalNotifierAvailable;
    private Instant flashStart;
    private long mouseLastPressedMillis;
    private long lastClipMTime = -2L;
    private Clip clip = null;

    @Inject
    private Notifier(ClientUI clientUI, Client client, RuneLiteConfig runeliteConfig, ScheduledExecutorService executorService, ChatMessageManager chatMessageManager, EventBus eventBus, @Named(value="runelite.title") String appName) {
        this.client = client;
        this.clientUI = clientUI;
        this.runeLiteConfig = runeliteConfig;
        this.executorService = executorService;
        this.chatMessageManager = chatMessageManager;
        this.eventBus = eventBus;
        this.appName = appName;
        this.notifyIconPath = RuneLite.RUNELITE_DIR.toPath().resolve("icon.png");
        if (!Strings.isNullOrEmpty((String)RuneLiteProperties.getLauncherVersion()) && OSType.getOSType() == OSType.MacOS) {
            executorService.execute(() -> {
                this.terminalNotifierAvailable = this.isTerminalNotifierAvailable();
            });
        }
        this.storeIcon();
    }

    public void notify(String message) {
        this.notify(message, TrayIcon.MessageType.NONE);
    }

    public void notify(String message, TrayIcon.MessageType type) {
        this.eventBus.post(new NotificationFired(message, type));
        if (!this.runeLiteConfig.sendNotificationsWhenFocused() && this.clientUI.isFocused()) {
            return;
        }
        switch (this.runeLiteConfig.notificationRequestFocus()) {
            case REQUEST: {
                this.clientUI.requestFocus();
                break;
            }
            case FORCE: {
                this.clientUI.forceFocus();
            }
        }
        if (this.runeLiteConfig.enableTrayNotifications()) {
            this.sendNotification(this.buildTitle(), message, type);
        }
        switch (this.runeLiteConfig.notificationSound()) {
            case NATIVE: {
                Toolkit.getDefaultToolkit().beep();
                break;
            }
            case CUSTOM: {
                this.executorService.submit(this::playCustomSound);
            }
        }
        if (this.runeLiteConfig.enableGameMessageNotification() && this.client.getGameState() == GameState.LOGGED_IN) {
            String formattedMessage = new ChatMessageBuilder().append(ChatColorType.HIGHLIGHT).append(message).build();
            this.chatMessageManager.queue(QueuedMessage.builder().type(ChatMessageType.CONSOLE).name(this.appName).runeLiteFormattedMessage(formattedMessage).build());
        }
        if (this.runeLiteConfig.flashNotification() != FlashNotification.DISABLED) {
            this.flashStart = Instant.now();
            this.mouseLastPressedMillis = this.client.getMouseLastPressedMillis();
        }
        log.debug(message);
    }

    private String buildTitle() {
        Player player = this.client.getLocalPlayer();
        if (player == null) {
            return this.appName;
        }
        String name = player.getName();
        if (Strings.isNullOrEmpty((String)name)) {
            return this.appName;
        }
        return this.appName + " - " + name;
    }

    public void processFlash(Graphics2D graphics) {
        FlashNotification flashNotification = this.runeLiteConfig.flashNotification();
        if (this.flashStart == null || this.client.getGameState() != GameState.LOGGED_IN || flashNotification == FlashNotification.DISABLED) {
            this.flashStart = null;
            return;
        }
        if (Instant.now().minusMillis(2000L).isAfter(this.flashStart)) {
            switch (flashNotification) {
                case FLASH_TWO_SECONDS: 
                case SOLID_TWO_SECONDS: {
                    this.flashStart = null;
                    return;
                }
                case SOLID_UNTIL_CANCELLED: 
                case FLASH_UNTIL_CANCELLED: {
                    if (this.client.getMouseIdleTicks() >= 100 && this.client.getKeyboardIdleTicks() >= 100 && this.client.getMouseLastPressedMillis() <= this.mouseLastPressedMillis || !this.clientUI.isFocused()) break;
                    this.flashStart = null;
                    return;
                }
            }
        }
        if (this.client.getGameCycle() % 40 >= 20 && (flashNotification == FlashNotification.FLASH_TWO_SECONDS || flashNotification == FlashNotification.FLASH_UNTIL_CANCELLED)) {
            return;
        }
        Color color = graphics.getColor();
        graphics.setColor(this.runeLiteConfig.notificationFlashColor());
        graphics.fill(new Rectangle(this.client.getCanvas().getSize()));
        graphics.setColor(color);
    }

    private void sendNotification(String title, String message, TrayIcon.MessageType type) {
        String escapedTitle = SHELL_ESCAPE.escape(title);
        String escapedMessage = SHELL_ESCAPE.escape(message);
        switch (OSType.getOSType()) {
            case Linux: {
                this.sendLinuxNotification(escapedTitle, escapedMessage, type);
                break;
            }
            case MacOS: {
                this.sendMacNotification(escapedTitle, escapedMessage);
                break;
            }
            default: {
                this.sendTrayNotification(title, message, type);
            }
        }
    }

    private void sendTrayNotification(String title, String message, TrayIcon.MessageType type) {
        if (this.clientUI.getTrayIcon() != null) {
            this.clientUI.getTrayIcon().displayMessage(title, message, type);
        }
    }

    private void sendLinuxNotification(String title, String message, TrayIcon.MessageType type) {
        ArrayList<String> commands = new ArrayList<String>();
        commands.add("notify-send");
        commands.add(title);
        commands.add(message);
        commands.add("-a");
        commands.add(SHELL_ESCAPE.escape(this.appName));
        commands.add("-i");
        commands.add(SHELL_ESCAPE.escape(this.notifyIconPath.toAbsolutePath().toString()));
        commands.add("-u");
        commands.add(Notifier.toUrgency(type));
        if (this.runeLiteConfig.notificationTimeout() > 0) {
            commands.add("-t");
            commands.add(String.valueOf(this.runeLiteConfig.notificationTimeout()));
        }
        this.executorService.submit(() -> {
            try {
                Process notificationProcess = Notifier.sendCommand(commands);
                boolean exited = notificationProcess.waitFor(500L, TimeUnit.MILLISECONDS);
                if (exited && notificationProcess.exitValue() == 0) {
                    return;
                }
            }
            catch (IOException | InterruptedException ex) {
                log.debug("error sending notification", (Throwable)ex);
            }
            this.sendTrayNotification(title, message, type);
        });
    }

    private void sendMacNotification(String title, String message) {
        ArrayList<String> commands = new ArrayList<String>();
        if (this.terminalNotifierAvailable) {
            Collections.addAll(commands, "sh", "-lc", "\"$@\"", "--", "terminal-notifier", "-title", title, "-message", message, "-group", "net.runelite.launcher", "-sender", "net.runelite.launcher");
        } else {
            commands.add("osascript");
            commands.add("-e");
            String script = "display notification \"" + message + DOUBLE_QUOTE + " with title " + DOUBLE_QUOTE + title + DOUBLE_QUOTE;
            commands.add(script);
        }
        try {
            Notifier.sendCommand(commands);
        }
        catch (IOException ex) {
            log.warn("error sending notification", (Throwable)ex);
        }
    }

    private static Process sendCommand(List<String> commands) throws IOException {
        return new ProcessBuilder(commands).redirectErrorStream(true).start();
    }

    private void storeIcon() {
        if (OSType.getOSType() == OSType.Linux && !Files.exists(this.notifyIconPath, new LinkOption[0])) {
            try (InputStream stream = Notifier.class.getResourceAsStream("/runelite.png");){
                Files.copy(stream, this.notifyIconPath, new CopyOption[0]);
            }
            catch (IOException ex) {
                log.warn(null, (Throwable)ex);
            }
        }
    }

    private boolean isTerminalNotifierAvailable() {
        try {
            Process exec = Runtime.getRuntime().exec(new String[]{"sh", "-lc", "terminal-notifier -help"});
            if (!exec.waitFor(2L, TimeUnit.SECONDS)) {
                return false;
            }
            return exec.exitValue() == 0;
        }
        catch (IOException | InterruptedException e) {
            return false;
        }
    }

    private static String toUrgency(TrayIcon.MessageType type) {
        switch (type) {
            case WARNING: 
            case ERROR: {
                return "critical";
            }
        }
        return "normal";
    }

    private synchronized void playCustomSound() {
        long currentMTime;
        long l = currentMTime = NOTIFICATION_FILE.exists() ? NOTIFICATION_FILE.lastModified() : -1L;
        if (this.clip == null || currentMTime != this.lastClipMTime || !this.clip.isOpen()) {
            if (this.clip != null) {
                this.clip.close();
            }
            try {
                this.clip = AudioSystem.getClip();
            }
            catch (LineUnavailableException e) {
                this.lastClipMTime = -2L;
                log.warn("Unable to play notification", (Throwable)e);
                Toolkit.getDefaultToolkit().beep();
                return;
            }
            this.lastClipMTime = currentMTime;
            if (!this.tryLoadNotification()) {
                Toolkit.getDefaultToolkit().beep();
                return;
            }
        }
        this.clip.loop(1);
    }

    /*
     * Enabled aggressive exception aggregation
     */
    private boolean tryLoadNotification() {
        if (NOTIFICATION_FILE.exists()) {
            try (BufferedInputStream fileStream = new BufferedInputStream(new FileInputStream(NOTIFICATION_FILE));){
                boolean bl;
                block27: {
                    AudioInputStream sound = AudioSystem.getAudioInputStream(fileStream);
                    try {
                        this.clip.open(sound);
                        bl = true;
                        if (sound == null) break block27;
                    }
                    catch (Throwable throwable) {
                        if (sound != null) {
                            try {
                                sound.close();
                            }
                            catch (Throwable throwable2) {
                                throwable.addSuppressed(throwable2);
                            }
                        }
                        throw throwable;
                    }
                    sound.close();
                }
                return bl;
            }
            catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
                log.warn("Unable to load notification sound", (Throwable)e);
            }
        }
        try (BufferedInputStream fileStream = new BufferedInputStream(Notifier.class.getResourceAsStream("notification.wav"));){
            boolean bl;
            block28: {
                AudioInputStream sound = AudioSystem.getAudioInputStream(fileStream);
                try {
                    this.clip.open(sound);
                    bl = true;
                    if (sound == null) break block28;
                }
                catch (Throwable throwable) {
                    if (sound != null) {
                        try {
                            sound.close();
                        }
                        catch (Throwable throwable3) {
                            throwable.addSuppressed(throwable3);
                        }
                    }
                    throw throwable;
                }
                sound.close();
            }
            return bl;
        }
        catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
            log.warn("Unable to load builtin notification sound", (Throwable)e);
            return false;
        }
    }

    public static enum NativeCustomOff {
        NATIVE("Native"),
        CUSTOM("Custom"),
        OFF("Off");

        private final String name;

        public String toString() {
            return this.name;
        }

        public String getName() {
            return this.name;
        }

        private NativeCustomOff(String name) {
            this.name = name;
        }
    }
}

