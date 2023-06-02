/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.inject.ProvisionException
 *  javax.inject.Inject
 *  net.runelite.api.Client
 *  net.runelite.api.GameState
 *  net.runelite.api.MenuAction
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package net.runelite.client.plugins.devtools;

import com.google.inject.ProvisionException;
import java.awt.GridLayout;
import java.awt.TrayIcon;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.MenuAction;
import net.runelite.client.Notifier;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.plugins.devtools.DevToolsPlugin;
import net.runelite.client.plugins.devtools.InventoryInspector;
import net.runelite.client.plugins.devtools.ScriptInspector;
import net.runelite.client.plugins.devtools.ShellFrame;
import net.runelite.client.plugins.devtools.VarInspector;
import net.runelite.client.plugins.devtools.WidgetInspector;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.ui.overlay.OverlayMenuEntry;
import net.runelite.client.ui.overlay.infobox.Counter;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import net.runelite.client.util.ImageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class DevToolsPanel
extends PluginPanel {
    private static final Logger log = LoggerFactory.getLogger(DevToolsPanel.class);
    private final Client client;
    private final ClientThread clientThread;
    private final Notifier notifier;
    private final DevToolsPlugin plugin;
    private final WidgetInspector widgetInspector;
    private final VarInspector varInspector;
    private final ScriptInspector scriptInspector;
    private final InventoryInspector inventoryInspector;
    private final InfoBoxManager infoBoxManager;
    private final ScheduledExecutorService scheduledExecutorService;

    @Inject
    private DevToolsPanel(Client client, ClientThread clientThread, DevToolsPlugin plugin, WidgetInspector widgetInspector, VarInspector varInspector, ScriptInspector scriptInspector, InventoryInspector inventoryInspector, Notifier notifier, InfoBoxManager infoBoxManager, ScheduledExecutorService scheduledExecutorService) {
        this.client = client;
        this.clientThread = clientThread;
        this.plugin = plugin;
        this.widgetInspector = widgetInspector;
        this.varInspector = varInspector;
        this.inventoryInspector = inventoryInspector;
        this.scriptInspector = scriptInspector;
        this.notifier = notifier;
        this.infoBoxManager = infoBoxManager;
        this.scheduledExecutorService = scheduledExecutorService;
        this.setBackground(ColorScheme.DARK_GRAY_COLOR);
        this.add(this.createOptionsPanel());
    }

    private JPanel createOptionsPanel() {
        JPanel container = new JPanel();
        container.setBackground(ColorScheme.DARK_GRAY_COLOR);
        container.setLayout(new GridLayout(0, 2, 3, 3));
        container.add(this.plugin.getPlayers());
        container.add(this.plugin.getNpcs());
        container.add(this.plugin.getGroundItems());
        container.add(this.plugin.getGroundObjects());
        container.add(this.plugin.getGameObjects());
        container.add(this.plugin.getGraphicsObjects());
        container.add(this.plugin.getWalls());
        container.add(this.plugin.getDecorations());
        container.add(this.plugin.getProjectiles());
        container.add(this.plugin.getLocation());
        container.add(this.plugin.getWorldMapLocation());
        container.add(this.plugin.getTileLocation());
        container.add(this.plugin.getCameraPosition());
        container.add(this.plugin.getChunkBorders());
        container.add(this.plugin.getMapSquares());
        container.add(this.plugin.getLineOfSight());
        container.add(this.plugin.getValidMovement());
        container.add(this.plugin.getMovementFlags());
        container.add(this.plugin.getInteracting());
        container.add(this.plugin.getExamine());
        container.add(this.plugin.getDetachedCamera());
        this.plugin.getDetachedCamera().addActionListener(ev -> {
            this.client.setOculusOrbState(!this.plugin.getDetachedCamera().isActive() ? 1 : 0);
            this.client.setOculusOrbNormalSpeed(!this.plugin.getDetachedCamera().isActive() ? 36 : 12);
        });
        container.add(this.plugin.getWidgetInspector());
        this.plugin.getWidgetInspector().addFrame(this.widgetInspector);
        container.add(this.plugin.getVarInspector());
        this.plugin.getVarInspector().addFrame(this.varInspector);
        container.add(this.plugin.getSoundEffects());
        JButton notificationBtn = new JButton("Notification");
        notificationBtn.addActionListener(e -> this.scheduledExecutorService.schedule(() -> this.notifier.notify("Wow!", TrayIcon.MessageType.ERROR), 3L, TimeUnit.SECONDS));
        container.add(notificationBtn);
        container.add(this.plugin.getScriptInspector());
        this.plugin.getScriptInspector().addFrame(this.scriptInspector);
        JButton newInfoboxBtn = new JButton("Infobox");
        newInfoboxBtn.addActionListener(e -> {
            Counter counter = new Counter(ImageUtil.loadImageResource(this.getClass(), "devtools_icon.png"), this.plugin, 42){

                @Override
                public String getName() {
                    return "devtools-" + this.hashCode();
                }
            };
            counter.getMenuEntries().add(new OverlayMenuEntry(MenuAction.RUNELITE_INFOBOX, "Test", "DevTools"));
            this.infoBoxManager.addInfoBox(counter);
        });
        container.add(newInfoboxBtn);
        JButton clearInfoboxBtn = new JButton("Clear Infobox");
        clearInfoboxBtn.addActionListener(e -> this.infoBoxManager.removeIf(i -> true));
        container.add(clearInfoboxBtn);
        container.add(this.plugin.getInventoryInspector());
        this.plugin.getInventoryInspector().addFrame(this.inventoryInspector);
        JButton disconnectBtn = new JButton("Disconnect");
        disconnectBtn.addActionListener(e -> this.clientThread.invoke(() -> this.client.setGameState(GameState.CONNECTION_LOST)));
        container.add(disconnectBtn);
        container.add(this.plugin.getRoofs());
        try {
            ShellFrame sf = (ShellFrame)this.plugin.getInjector().getInstance(ShellFrame.class);
            container.add(this.plugin.getShell());
            this.plugin.getShell().addFrame(sf);
        }
        catch (ProvisionException | LinkageError e2) {
            log.debug("Shell is not supported", e2);
        }
        catch (Exception e3) {
            log.info("Shell couldn't be loaded", (Throwable)e3);
        }
        container.add(this.plugin.getInterfacePath());
        this.plugin.getInterfacePath().addActionListener(ev -> this.client.setDevelopmentInterfaceLocation(JOptionPane.showInputDialog("Interface Path (no trailing slash)")));
        container.add(this.plugin.getScriptPath());
        this.plugin.getScriptPath().addActionListener(ev -> this.client.setDevelopmentScriptLocation(JOptionPane.showInputDialog("Script Path (no trailing slash)")));
        container.add(this.plugin.getNetwork());
        return container;
    }
}

