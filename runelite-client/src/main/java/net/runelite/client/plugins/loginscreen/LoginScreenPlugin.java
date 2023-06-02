/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Strings
 *  com.google.inject.Provides
 *  javax.inject.Inject
 *  net.runelite.api.Client
 *  net.runelite.api.GameState
 *  net.runelite.api.SpritePixels
 *  net.runelite.api.events.GameStateChanged
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package net.runelite.client.plugins.loginscreen;

import com.google.common.base.Strings;
import com.google.inject.Provides;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.SpritePixels;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.RuneLite;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.events.SessionOpen;
import net.runelite.client.input.KeyListener;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.loginscreen.LoginScreenConfig;
import net.runelite.client.plugins.loginscreen.LoginScreenOverride;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.util.OSType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(name="Login Screen", description="Provides various enhancements for login screen")
public class LoginScreenPlugin
extends Plugin
implements KeyListener {
    private static final Logger log = LoggerFactory.getLogger(LoginScreenPlugin.class);
    private static final int MAX_USERNAME_LENGTH = 254;
    private static final int MAX_PASSWORD_LENGTH = 44;
    private static final int MAX_PIN_LENGTH = 6;
    private static final File CUSTOM_LOGIN_SCREEN_FILE = new File(RuneLite.RUNELITE_DIR, "login.png");
    @Inject
    private Client client;
    @Inject
    private ClientThread clientThread;
    @Inject
    private LoginScreenConfig config;
    @Inject
    private KeyManager keyManager;
    private String usernameCache;

    @Override
    protected void startUp() throws Exception {
        this.applyUsername();
        this.keyManager.registerKeyListener(this);
        this.clientThread.invoke(this::overrideLoginScreen);
    }

    @Override
    protected void shutDown() throws Exception {
        if (this.config.syncUsername()) {
            this.client.getPreferences().setRememberedUsername(this.usernameCache);
        }
        this.keyManager.unregisterKeyListener(this);
        this.clientThread.invoke(() -> {
            this.restoreLoginScreen();
            this.client.setShouldRenderLoginScreenFire(true);
        });
    }

    @Provides
    LoginScreenConfig getConfig(ConfigManager configManager) {
        return configManager.getConfig(LoginScreenConfig.class);
    }

    @Subscribe
    public void onConfigChanged(ConfigChanged event) {
        if (event.getGroup().equals("loginscreen")) {
            this.clientThread.invoke(this::overrideLoginScreen);
        }
    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged event) {
        if (!this.config.syncUsername()) {
            return;
        }
        if (event.getGameState() == GameState.LOGIN_SCREEN) {
            this.applyUsername();
        } else if (event.getGameState() == GameState.LOGGED_IN) {
            String username = "";
            if (this.client.getPreferences().getRememberedUsername() != null) {
                username = this.client.getUsername();
            }
            if (this.config.username().equals(username)) {
                return;
            }
            log.debug("Saving username: {}", (Object)username);
            this.config.username(username);
        }
    }

    @Subscribe
    public void onSessionOpen(SessionOpen event) {
        this.applyUsername();
    }

    private void applyUsername() {
        if (!this.config.syncUsername()) {
            return;
        }
        GameState gameState = this.client.getGameState();
        if (gameState == GameState.LOGIN_SCREEN) {
            String username = this.config.username();
            if (Strings.isNullOrEmpty((String)username)) {
                return;
            }
            if (this.usernameCache == null) {
                this.usernameCache = this.client.getPreferences().getRememberedUsername();
            }
            this.client.getPreferences().setRememberedUsername(username);
        }
    }

    @Override
    public boolean isEnabledOnLoginScreen() {
        return true;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        boolean isModifierDown;
        if (!this.config.pasteEnabled() || this.client.getGameState() != GameState.LOGIN_SCREEN && this.client.getGameState() != GameState.LOGIN_SCREEN_AUTHENTICATOR) {
            return;
        }
        boolean bl = isModifierDown = OSType.getOSType() == OSType.MacOS ? e.isMetaDown() : e.isControlDown();
        if (e.getKeyCode() == 86 && isModifierDown) {
            try {
                String data = Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor).toString().trim();
                switch (this.client.getLoginIndex()) {
                    case 2: {
                        if (this.client.getCurrentLoginField() == 0) {
                            this.client.setUsername(data.substring(0, Math.min(data.length(), 254)));
                            break;
                        }
                        this.client.setPassword(data.substring(0, Math.min(data.length(), 44)));
                        break;
                    }
                    case 4: {
                        this.client.setOtp(data.substring(0, Math.min(data.length(), 6)));
                    }
                }
            }
            catch (UnsupportedFlavorException | IOException ex) {
                log.warn("failed to fetch clipboard data", (Throwable)ex);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void overrideLoginScreen() {
        this.client.setShouldRenderLoginScreenFire(this.config.showLoginFire());
        if (this.config.loginScreen() == LoginScreenOverride.OFF) {
            this.restoreLoginScreen();
            return;
        }
        SpritePixels pixels = null;
        if (this.config.loginScreen() == LoginScreenOverride.CUSTOM) {
            if (CUSTOM_LOGIN_SCREEN_FILE.exists()) {
                try {
                    Class<ImageIO> class_ = ImageIO.class;
                    synchronized (ImageIO.class) {
                        BufferedImage image = ImageIO.read(CUSTOM_LOGIN_SCREEN_FILE);
                        // ** MonitorExit[var3_2] (shouldn't be in output)
                        if (image.getHeight() > 503) {
                            double scalar = 503.0 / (double)image.getHeight();
                            image = ImageUtil.resizeImage(image, (int)((double)image.getWidth() * scalar), 503);
                        }
                        pixels = ImageUtil.getImageSpritePixels(image, this.client);
                    }
                }
                catch (IOException e) {
                    log.error("error loading custom login screen", (Throwable)e);
                    this.restoreLoginScreen();
                    return;
                }
            }
        } else if (this.config.loginScreen() == LoginScreenOverride.RANDOM) {
            LoginScreenOverride[] filtered = (LoginScreenOverride[])Arrays.stream(LoginScreenOverride.values()).filter(screen -> screen.getFileName() != null).toArray(LoginScreenOverride[]::new);
            LoginScreenOverride randomScreen = filtered[new Random().nextInt(filtered.length)];
            pixels = this.getFileSpritePixels(randomScreen.getFileName());
        } else {
            pixels = this.getFileSpritePixels(this.config.loginScreen().getFileName());
        }
        {
            if (pixels != null) {
                this.client.setLoginScreen(pixels);
            }
            return;
        }
    }

    private void restoreLoginScreen() {
        this.client.setLoginScreen(null);
    }

    private SpritePixels getFileSpritePixels(String file) {
        try {
            log.debug("Loading: {}", (Object)file);
            BufferedImage image = ImageUtil.loadImageResource(this.getClass(), file);
            return ImageUtil.getImageSpritePixels(image, this.client);
        }
        catch (RuntimeException ex) {
            log.debug("Unable to load image: ", (Throwable)ex);
            return null;
        }
    }
}

