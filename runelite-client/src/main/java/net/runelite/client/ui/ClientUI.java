/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Strings
 *  com.google.inject.Inject
 *  javax.annotation.Nullable
 *  javax.inject.Named
 *  javax.inject.Provider
 *  javax.inject.Singleton
 *  net.runelite.api.Client
 *  net.runelite.api.GameState
 *  net.runelite.api.Player
 *  net.runelite.api.Point
 *  net.runelite.api.events.GameStateChanged
 *  net.runelite.api.widgets.Widget
 *  net.runelite.api.widgets.WidgetInfo
 *  org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities
 *  org.pushingpixels.substance.internal.utils.SubstanceTitlePaneUtilities$ExtraComponentKind
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package net.runelite.client.ui;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import java.applet.Applet;
import java.awt.Canvas;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.lang.reflect.Type;
import java.time.Duration;
import javax.annotation.Nullable;
import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.event.HyperlinkEvent;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Player;
import net.runelite.api.Point;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.RuneLiteProperties;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.config.ExpandResizeType;
import net.runelite.client.config.RuneLiteConfig;
import net.runelite.client.config.WarningOnExit;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ClientShutdown;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.events.NavigationButtonAdded;
import net.runelite.client.events.NavigationButtonRemoved;
import net.runelite.client.input.KeyManager;
import net.runelite.client.input.MouseAdapter;
import net.runelite.client.input.MouseManager;
import net.runelite.client.ui.ClientPanel;
import net.runelite.client.ui.ClientPluginToolbar;
import net.runelite.client.ui.ClientTitleToolbar;
import net.runelite.client.ui.ContainableFrame;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.MacOSQuitStrategy;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.ui.skin.SubstanceRuneLiteLookAndFeel;
import net.runelite.client.util.HotkeyListener;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.util.LinkBrowser;
import net.runelite.client.util.OSType;
import net.runelite.client.util.OSXUtil;
import net.runelite.client.util.SwingUtil;
import net.runelite.client.util.WinUtil;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceTitlePaneUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class ClientUI {
    private static final Logger log = LoggerFactory.getLogger(ClientUI.class);
    private static final String CONFIG_GROUP = "runelite";
    private static final String CONFIG_CLIENT_BOUNDS = "clientBounds";
    private static final String CONFIG_CLIENT_MAXIMIZED = "clientMaximized";
    private static final String CONFIG_CLIENT_SIDEBAR_CLOSED = "clientSidebarClosed";
    public static final BufferedImage ICON = ImageUtil.loadImageResource(ClientUI.class, "/runelite.png");
    private TrayIcon trayIcon;
    private final RuneLiteConfig config;
    private final KeyManager keyManager;
    private final MouseManager mouseManager;
    private final Applet client;
    private final ConfigManager configManager;
    private final Provider<ClientThread> clientThreadProvider;
    private final EventBus eventBus;
    private final boolean safeMode;
    private final String title;
    private final boolean vanilla;
    private final CardLayout cardLayout = new CardLayout();
    private final Rectangle sidebarButtonPosition = new Rectangle();
    private boolean withTitleBar;
    private BufferedImage sidebarOpenIcon;
    private BufferedImage sidebarClosedIcon;
    private ContainableFrame frame;
    private JPanel navContainer;
    private PluginPanel pluginPanel;
    private ClientPluginToolbar pluginToolbar;
    private ClientTitleToolbar titleToolbar;
    private JButton currentButton;
    private NavigationButton currentNavButton;
    private boolean sidebarOpen;
    private JPanel container;
    private NavigationButton sidebarNavigationButton;
    private JButton sidebarNavigationJButton;
    private Dimension lastClientSize;
    private Cursor defaultCursor;
    @Inject(optional=true)
    @Named(value="minMemoryLimit")
    private int minMemoryLimit = 400;
    @Inject(optional=true)
    @Named(value="recommendedMemoryLimit")
    private int recommendedMemoryLimit = 512;

    @Inject
    private ClientUI(RuneLiteConfig config, KeyManager keyManager, MouseManager mouseManager, @Nullable Applet client, ConfigManager configManager, Provider<ClientThread> clientThreadProvider, EventBus eventBus, @Named(value="safeMode") boolean safeMode, @Named(value="runelite.title") String title, @Named(value="vanilla") boolean vanilla) {
        this.config = config;
        this.keyManager = keyManager;
        this.mouseManager = mouseManager;
        this.client = client;
        this.configManager = configManager;
        this.clientThreadProvider = clientThreadProvider;
        this.eventBus = eventBus;
        this.safeMode = safeMode;
        this.title = title + (safeMode ? " (safe mode)" : "");
        this.vanilla = vanilla;
    }

    @Subscribe
    public void onConfigChanged(ConfigChanged event) {
        if (!event.getGroup().equals(CONFIG_GROUP) || event.getKey().equals(CONFIG_CLIENT_MAXIMIZED) || event.getKey().equals(CONFIG_CLIENT_BOUNDS)) {
            return;
        }
        SwingUtilities.invokeLater(() -> this.updateFrameConfig(event.getKey().equals("lockWindowSize")));
    }

    @Subscribe
    public void onNavigationButtonAdded(NavigationButtonAdded event) {
        SwingUtilities.invokeLater(() -> {
            NavigationButton navigationButton = event.getButton();
            PluginPanel pluginPanel = navigationButton.getPanel();
            boolean inTitle = !event.getButton().isTab() && this.withTitleBar;
            int iconSize = 16;
            if (pluginPanel != null) {
                this.navContainer.add((Component)pluginPanel.getWrappedPanel(), navigationButton.getTooltip());
            }
            JButton button = SwingUtil.createSwingButton(navigationButton, 16, (navButton, jButton) -> {
                boolean doClose;
                PluginPanel panel = navButton.getPanel();
                if (panel == null) {
                    return;
                }
                boolean bl = doClose = this.currentButton != null && this.currentButton == jButton && this.currentButton.isSelected();
                if (doClose) {
                    this.contract();
                    this.currentButton.setSelected(false);
                    this.currentNavButton.setSelected(false);
                    this.currentButton = null;
                    this.currentNavButton = null;
                } else {
                    if (this.currentButton != null) {
                        this.currentButton.setSelected(false);
                    }
                    if (this.currentNavButton != null) {
                        this.currentNavButton.setSelected(false);
                    }
                    this.currentButton = jButton;
                    this.currentNavButton = navButton;
                    this.currentButton.setSelected(true);
                    this.currentNavButton.setSelected(true);
                    this.expand((NavigationButton)navButton);
                }
            });
            if (inTitle) {
                this.titleToolbar.addComponent(event.getButton(), button);
                this.titleToolbar.revalidate();
            } else {
                this.pluginToolbar.addComponent(event.getButton(), button);
                this.pluginToolbar.revalidate();
            }
            if (navigationButton.getOnReady() != null) {
                navigationButton.getOnReady().run();
            }
        });
    }

    @Subscribe
    public void onNavigationButtonRemoved(NavigationButtonRemoved event) {
        SwingUtilities.invokeLater(() -> {
            this.pluginToolbar.removeComponent(event.getButton());
            this.pluginToolbar.revalidate();
            this.titleToolbar.removeComponent(event.getButton());
            this.titleToolbar.revalidate();
            PluginPanel pluginPanel = event.getButton().getPanel();
            if (pluginPanel != null) {
                this.navContainer.remove(pluginPanel.getWrappedPanel());
            }
        });
    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged event) {
        if (event.getGameState() != GameState.LOGGED_IN || !(this.client instanceof Client) || !this.config.usernameInTitle()) {
            return;
        }
        Client client = (Client)this.client;
        ClientThread clientThread = (ClientThread)this.clientThreadProvider.get();
        clientThread.invokeLater(() -> {
            if (client.getGameState() != GameState.LOGGED_IN) {
                return true;
            }
            Player player = client.getLocalPlayer();
            if (player == null) {
                return false;
            }
            String name = player.getName();
            if (Strings.isNullOrEmpty((String)name)) {
                return false;
            }
            this.frame.setTitle(this.title + " - " + name);
            return true;
        });
    }

    public void init() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            SwingUtil.setupDefaults();
            SwingUtil.setTheme((LookAndFeel)((Object)new SubstanceRuneLiteLookAndFeel()));
            SwingUtil.setFont(FontManager.getRunescapeFont());
            this.frame = new ContainableFrame();
            OSXUtil.tryEnableFullscreen(this.frame);
            this.frame.setTitle(this.title);
            this.frame.setIconImage(ICON);
            this.frame.getLayeredPane().setCursor(Cursor.getDefaultCursor());
            this.frame.setLocationRelativeTo(this.frame.getOwner());
            this.frame.setResizable(true);
            this.frame.setDefaultCloseOperation(0);
            if (OSType.getOSType() == OSType.MacOS) {
                MacOSQuitStrategy.setup();
            }
            this.frame.addWindowListener(new WindowAdapter(){

                @Override
                public void windowClosing(WindowEvent event) {
                    int result = 0;
                    if (ClientUI.this.showWarningOnExit()) {
                        try {
                            result = JOptionPane.showConfirmDialog(ClientUI.this.frame, "Are you sure you want to exit?", "Exit", 2, 3);
                        }
                        catch (Exception e) {
                            log.warn("Unexpected exception occurred while check for confirm required", (Throwable)e);
                        }
                    }
                    if (result == 0) {
                        ClientUI.this.shutdownClient();
                    }
                }
            });
            this.frame.addWindowStateListener(l -> {
                if (l.getNewState() == 0) {
                    SwingUtilities.invokeLater(this.frame::revalidateMinimumSize);
                }
            });
            this.container = new JPanel();
            this.container.setLayout(new BoxLayout(this.container, 0));
            this.container.add(new ClientPanel(this.client));
            this.navContainer = new JPanel();
            this.navContainer.setLayout(this.cardLayout);
            this.navContainer.setMinimumSize(new Dimension(0, 0));
            this.navContainer.setMaximumSize(new Dimension(0, 0));
            this.navContainer.setPreferredSize(new Dimension(0, 0));
            this.navContainer.putClientProperty("substancelaf.internal.colorizationFactor", 1.0);
            this.container.add(this.navContainer);
            this.pluginToolbar = new ClientPluginToolbar();
            this.titleToolbar = new ClientTitleToolbar();
            this.frame.add(this.container);
            HotkeyListener sidebarListener = new HotkeyListener(this.config::sidebarToggleKey){

                @Override
                public void hotkeyPressed() {
                    ClientUI.this.toggleSidebar();
                }
            };
            sidebarListener.setEnabledOnLoginScreen(true);
            this.keyManager.registerKeyListener(sidebarListener);
            HotkeyListener pluginPanelListener = new HotkeyListener(this.config::panelToggleKey){

                @Override
                public void hotkeyPressed() {
                    ClientUI.this.togglePluginPanel();
                }
            };
            pluginPanelListener.setEnabledOnLoginScreen(true);
            this.keyManager.registerKeyListener(pluginPanelListener);
            MouseAdapter mouseListener = new MouseAdapter(){

                @Override
                public MouseEvent mousePressed(MouseEvent mouseEvent) {
                    if (SwingUtilities.isLeftMouseButton(mouseEvent) && ClientUI.this.sidebarButtonPosition.contains(mouseEvent.getPoint())) {
                        SwingUtilities.invokeLater(() -> ClientUI.this.toggleSidebar());
                        mouseEvent.consume();
                    }
                    return mouseEvent;
                }
            };
            this.mouseManager.registerMouseListener(mouseListener);
            this.withTitleBar = !this.vanilla && this.config.enableCustomChrome();
            this.frame.setUndecorated(this.withTitleBar);
            if (this.withTitleBar) {
                this.frame.getRootPane().setWindowDecorationStyle(1);
                final JComponent titleBar = SubstanceCoreUtilities.getTitlePaneComponent((Window)this.frame);
                this.titleToolbar.putClientProperty("substancelaf.internal.titlePane.extraComponentKind", (Object)SubstanceTitlePaneUtilities.ExtraComponentKind.TRAILING);
                titleBar.add(this.titleToolbar);
                final LayoutManager delegate = titleBar.getLayout();
                titleBar.setLayout(new LayoutManager(){

                    @Override
                    public void addLayoutComponent(String name, Component comp) {
                        delegate.addLayoutComponent(name, comp);
                    }

                    @Override
                    public void removeLayoutComponent(Component comp) {
                        delegate.removeLayoutComponent(comp);
                    }

                    @Override
                    public Dimension preferredLayoutSize(Container parent) {
                        return delegate.preferredLayoutSize(parent);
                    }

                    @Override
                    public Dimension minimumLayoutSize(Container parent) {
                        return delegate.minimumLayoutSize(parent);
                    }

                    @Override
                    public void layoutContainer(Container parent) {
                        delegate.layoutContainer(parent);
                        int width = ((ClientUI)ClientUI.this).titleToolbar.getPreferredSize().width;
                        ClientUI.this.titleToolbar.setBounds(titleBar.getWidth() - 75 - width, 0, width, titleBar.getHeight());
                    }
                });
            }
            this.updateFrameConfig(false);
            this.sidebarOpenIcon = ImageUtil.loadImageResource(ClientUI.class, this.withTitleBar ? "open.png" : "open_rs.png");
            this.sidebarClosedIcon = ImageUtil.flipImage(this.sidebarOpenIcon, true, false);
            this.sidebarNavigationButton = NavigationButton.builder().priority(100).icon(this.sidebarOpenIcon).tooltip("Open SideBar").onClick(this::toggleSidebar).build();
            this.sidebarNavigationJButton = SwingUtil.createSwingButton(this.sidebarNavigationButton, 0, null);
            this.titleToolbar.addComponent(this.sidebarNavigationButton, this.sidebarNavigationJButton);
            if (this.configManager.getConfiguration(CONFIG_GROUP, CONFIG_CLIENT_SIDEBAR_CLOSED) == null && !this.vanilla) {
                this.toggleSidebar();
            }
        });
    }

    public void show() {
        int maxMemory;
        SwingUtilities.invokeLater(() -> {
            this.frame.pack();
            this.frame.revalidateMinimumSize();
            if (this.config.enableTrayIcon()) {
                this.trayIcon = SwingUtil.createTrayIcon(ICON, this.title, this.frame);
            }
            if (this.config.rememberScreenBounds() && !this.safeMode) {
                try {
                    Rectangle clientBounds = (Rectangle)this.configManager.getConfiguration(CONFIG_GROUP, CONFIG_CLIENT_BOUNDS, (Type)((Object)Rectangle.class));
                    if (clientBounds != null) {
                        this.frame.setBounds(clientBounds);
                        GraphicsConfiguration gc = this.findDisplayFromBounds(clientBounds);
                        if (gc != null) {
                            double scale = gc.getDefaultTransform().getScaleX();
                            if (scale != 1.0 && OSType.getOSType() != OSType.MacOS) {
                                clientBounds.setRect(clientBounds.getX() / scale, clientBounds.getY() / scale, clientBounds.getWidth() / scale, clientBounds.getHeight() / scale);
                                this.frame.setMinimumSize(clientBounds.getSize());
                                this.frame.setBounds(clientBounds);
                            }
                        } else {
                            this.frame.setLocationRelativeTo(this.frame.getOwner());
                        }
                    } else {
                        this.frame.setLocationRelativeTo(this.frame.getOwner());
                    }
                    if (this.configManager.getConfiguration(CONFIG_GROUP, CONFIG_CLIENT_MAXIMIZED) != null) {
                        this.frame.setExtendedState(6);
                    }
                }
                catch (Exception ex) {
                    log.warn("Failed to set window bounds", (Throwable)ex);
                    this.frame.setLocationRelativeTo(this.frame.getOwner());
                }
            } else {
                this.frame.setLocationRelativeTo(this.frame.getOwner());
            }
            this.frame.setVisible(true);
            this.frame.setResizable(!this.config.lockWindowSize());
            this.frame.toFront();
            this.requestFocus();
            log.info("Showing frame {}", (Object)this.frame);
            this.frame.revalidateMinimumSize();
        });
        if (this.client != null && !(this.client instanceof Client)) {
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this.frame, "RuneLite has not yet been updated to work with the latest\ngame update, it will work with reduced functionality until then.", "RuneLite is outdated", 1));
        }
        if ((maxMemory = (int)(Runtime.getRuntime().maxMemory() / 1024L / 1024L)) < this.minMemoryLimit) {
            SwingUtilities.invokeLater(() -> {
                JEditorPane ep = new JEditorPane("text/html", "Your Java memory limit is " + maxMemory + "mb, which is lower than the recommended " + this.recommendedMemoryLimit + "mb.<br>This can cause instability, and it is recommended you remove or increase this limit.<br>Join <a href=\"" + RuneLiteProperties.getDiscordInvite() + "\">Discord</a> for assistance.");
                ep.addHyperlinkListener(e -> {
                    if (e.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED)) {
                        LinkBrowser.browse(e.getURL().toString());
                    }
                });
                ep.setEditable(false);
                ep.setOpaque(false);
                JOptionPane.showMessageDialog(this.frame, ep, "Max memory limit low", 2);
            });
        }
    }

    private GraphicsConfiguration findDisplayFromBounds(Rectangle bounds) {
        GraphicsDevice[] gds;
        for (GraphicsDevice gd : gds = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) {
            GraphicsConfiguration gc = gd.getDefaultConfiguration();
            Rectangle displayBounds = gc.getBounds();
            if (!displayBounds.contains(bounds)) continue;
            return gc;
        }
        return null;
    }

    private boolean showWarningOnExit() {
        if (this.config.warningOnExit() == WarningOnExit.ALWAYS) {
            return true;
        }
        if (this.config.warningOnExit() == WarningOnExit.LOGGED_IN && this.client instanceof Client) {
            return ((Client)this.client).getGameState() != GameState.LOGIN_SCREEN;
        }
        return false;
    }

    private void shutdownClient() {
        this.saveClientBoundsConfig();
        ClientShutdown csev = new ClientShutdown();
        this.eventBus.post(csev);
        new Thread(() -> {
            csev.waitForAllConsumers(Duration.ofSeconds(10L));
            if (this.client != null) {
                int clientShutdownWaitMS;
                if (this.client instanceof Client) {
                    ((Client)this.client).stopNow();
                    clientShutdownWaitMS = 1000;
                } else {
                    this.client.stop();
                    this.frame.setVisible(false);
                    clientShutdownWaitMS = 6000;
                }
                try {
                    Thread.sleep(clientShutdownWaitMS);
                }
                catch (InterruptedException interruptedException) {
                    // empty catch block
                }
            }
            System.exit(0);
        }, "RuneLite Shutdown").start();
    }

    public void paint(Graphics graphics) {
        assert (SwingUtilities.isEventDispatchThread()) : "paint must be called on EDT";
        this.frame.paint(graphics);
    }

    public int getWidth() {
        return this.frame.getWidth();
    }

    public int getHeight() {
        return this.frame.getHeight();
    }

    public boolean isFocused() {
        return this.frame.isFocused();
    }

    public void requestFocus() {
        switch (OSType.getOSType()) {
            case MacOS: {
                OSXUtil.requestUserAttention();
                break;
            }
            default: {
                this.frame.requestFocus();
            }
        }
        this.giveClientFocus();
    }

    public void forceFocus() {
        switch (OSType.getOSType()) {
            case MacOS: {
                OSXUtil.requestForeground();
                break;
            }
            case Windows: {
                WinUtil.requestForeground(this.frame);
                break;
            }
            default: {
                this.frame.requestFocus();
            }
        }
        this.giveClientFocus();
    }

    public Cursor getCurrentCursor() {
        return this.container.getCursor();
    }

    public Cursor getDefaultCursor() {
        return this.defaultCursor != null ? this.defaultCursor : Cursor.getDefaultCursor();
    }

    public void setCursor(BufferedImage image, String name) {
        Cursor cursorAwt;
        if (this.container == null) {
            return;
        }
        java.awt.Point hotspot = new java.awt.Point(0, 0);
        this.defaultCursor = cursorAwt = Toolkit.getDefaultToolkit().createCustomCursor(image, hotspot, name);
        this.setCursor(cursorAwt);
    }

    public void setCursor(Cursor cursor) {
        this.container.setCursor(cursor);
    }

    public void resetCursor() {
        if (this.container == null) {
            return;
        }
        this.defaultCursor = null;
        this.container.setCursor(Cursor.getDefaultCursor());
    }

    public Point getCanvasOffset() {
        Canvas canvas;
        if (this.client instanceof Client && (canvas = ((Client)this.client).getCanvas()) != null) {
            java.awt.Point point = SwingUtilities.convertPoint(canvas, 0, 0, this.frame);
            return new Point(point.x, point.y);
        }
        return new Point(0, 0);
    }

    public void paintOverlays(Graphics2D graphics) {
        Point mousePosition;
        if (!(this.client instanceof Client) || this.withTitleBar || this.vanilla) {
            return;
        }
        Client client = (Client)this.client;
        int x = client.getRealDimensions().width - this.sidebarOpenIcon.getWidth() - 5;
        Widget logoutButton = client.getWidget(WidgetInfo.RESIZABLE_VIEWPORT_BOTTOM_LINE_LOGOUT_BUTTON);
        int y = logoutButton != null && !logoutButton.isHidden() && logoutButton.getParent() != null ? logoutButton.getHeight() + logoutButton.getRelativeY() : 5;
        BufferedImage image = this.sidebarOpen ? this.sidebarClosedIcon : this.sidebarOpenIcon;
        Rectangle sidebarButtonRange = new Rectangle(x - 15, 0, image.getWidth() + 25, client.getRealDimensions().height);
        if (sidebarButtonRange.contains((mousePosition = new Point(client.getMouseCanvasPosition().getX() + client.getViewportXOffset(), client.getMouseCanvasPosition().getY() + client.getViewportYOffset())).getX(), mousePosition.getY())) {
            graphics.drawImage((Image)image, x, y, null);
        }
        this.sidebarButtonPosition.setBounds(x, y, image.getWidth(), image.getHeight());
    }

    public GraphicsConfiguration getGraphicsConfiguration() {
        return this.frame.getGraphicsConfiguration();
    }

    private void toggleSidebar() {
        if (this.vanilla) {
            return;
        }
        boolean isSidebarOpen = this.sidebarOpen;
        boolean bl = this.sidebarOpen = !this.sidebarOpen;
        if (this.currentButton != null) {
            this.currentButton.setSelected(this.sidebarOpen);
        }
        if (this.currentNavButton != null) {
            this.currentNavButton.setSelected(this.sidebarOpen);
        }
        if (isSidebarOpen) {
            this.sidebarNavigationJButton.setIcon(new ImageIcon(this.sidebarOpenIcon));
            this.sidebarNavigationJButton.setToolTipText("Open SideBar");
            this.configManager.setConfiguration(CONFIG_GROUP, CONFIG_CLIENT_SIDEBAR_CLOSED, true);
            this.contract();
            this.container.remove(this.pluginToolbar);
        } else {
            this.sidebarNavigationJButton.setIcon(new ImageIcon(this.sidebarClosedIcon));
            this.sidebarNavigationJButton.setToolTipText("Close SideBar");
            this.configManager.unsetConfiguration(CONFIG_GROUP, CONFIG_CLIENT_SIDEBAR_CLOSED);
            this.expand(this.currentNavButton);
            this.container.add(this.pluginToolbar);
        }
        this.container.revalidate();
        this.giveClientFocus();
        if (this.sidebarOpen) {
            this.frame.expandBy(this.pluginToolbar.getWidth());
        } else {
            this.frame.contractBy(this.pluginToolbar.getWidth());
        }
    }

    private void togglePluginPanel() {
        boolean pluginPanelOpen;
        boolean bl = pluginPanelOpen = this.pluginPanel != null;
        if (this.currentButton != null) {
            this.currentButton.setSelected(!pluginPanelOpen);
        }
        if (pluginPanelOpen) {
            this.contract();
        } else {
            this.expand(this.currentNavButton);
        }
    }

    private void expand(@Nullable NavigationButton button) {
        int expandBy;
        if (button == null) {
            return;
        }
        PluginPanel panel = button.getPanel();
        if (panel == null) {
            return;
        }
        if (!this.sidebarOpen) {
            this.toggleSidebar();
        }
        int width = panel.getWrappedPanel().getPreferredSize().width;
        int n = expandBy = this.pluginPanel != null ? this.pluginPanel.getWrappedPanel().getPreferredSize().width - width : width;
        if (this.pluginPanel != null) {
            this.pluginPanel.onDeactivate();
        }
        this.pluginPanel = panel;
        this.navContainer.setMinimumSize(new Dimension(width, 0));
        this.navContainer.setMaximumSize(new Dimension(width, Integer.MAX_VALUE));
        this.navContainer.setPreferredSize(new Dimension(width, 0));
        this.navContainer.revalidate();
        this.cardLayout.show(this.navContainer, button.getTooltip());
        this.giveClientFocus();
        panel.onActivate();
        if (expandBy > 0) {
            this.frame.expandBy(expandBy);
        } else if (expandBy < 0) {
            this.frame.contractBy(expandBy);
        }
    }

    private void contract() {
        if (this.pluginPanel == null) {
            return;
        }
        this.pluginPanel.onDeactivate();
        this.navContainer.setMinimumSize(new Dimension(0, 0));
        this.navContainer.setMaximumSize(new Dimension(0, 0));
        this.navContainer.setPreferredSize(new Dimension(0, 0));
        this.navContainer.revalidate();
        this.giveClientFocus();
        this.frame.contractBy(this.pluginPanel.getWrappedPanel().getPreferredSize().width);
        this.pluginPanel = null;
    }

    private void giveClientFocus() {
        if (this.client instanceof Client) {
            Canvas c = ((Client)this.client).getCanvas();
            if (c != null) {
                c.requestFocusInWindow();
            }
        } else if (this.client != null) {
            this.client.requestFocusInWindow();
        }
    }

    private void updateFrameConfig(boolean updateResizable) {
        int height;
        if (this.frame == null) {
            return;
        }
        if (this.frame.isUndecorated() && this.frame.getGraphicsConfiguration().isTranslucencyCapable() && this.frame.getGraphicsConfiguration().getDevice().getFullScreenWindow() == null) {
            this.frame.setOpacity((float)this.config.windowOpacity() / 100.0f);
        }
        if (this.config.usernameInTitle() && this.client instanceof Client) {
            Player player = ((Client)this.client).getLocalPlayer();
            if (player != null && player.getName() != null) {
                this.frame.setTitle(this.title + " - " + player.getName());
            }
        } else {
            this.frame.setTitle(this.title);
        }
        if (this.frame.isAlwaysOnTopSupported()) {
            this.frame.setAlwaysOnTop(this.config.gameAlwaysOnTop());
        }
        if (updateResizable) {
            this.frame.setResizable(!this.config.lockWindowSize());
        }
        this.frame.setExpandResizeType(this.config.automaticResizeType());
        ContainableFrame.Mode containMode = this.config.containInScreen();
        if (containMode == ContainableFrame.Mode.ALWAYS && !this.withTitleBar) {
            containMode = ContainableFrame.Mode.RESIZING;
        }
        this.frame.setContainedInScreen(containMode);
        if (!this.config.rememberScreenBounds()) {
            this.configManager.unsetConfiguration(CONFIG_GROUP, CONFIG_CLIENT_MAXIMIZED);
            this.configManager.unsetConfiguration(CONFIG_GROUP, CONFIG_CLIENT_BOUNDS);
        }
        if (this.client == null) {
            return;
        }
        int width = Math.max(Math.min(this.config.gameSize().width, 7680), 765);
        Dimension size = new Dimension(width, height = Math.max(Math.min(this.config.gameSize().height, 2160), 503));
        if (!size.equals(this.lastClientSize)) {
            this.lastClientSize = size;
            this.client.setSize(size);
            this.client.setPreferredSize(size);
            this.client.getParent().setPreferredSize(size);
            this.client.getParent().setSize(size);
            if (this.frame.isVisible()) {
                this.frame.pack();
            }
        }
    }

    private void saveClientBoundsConfig() {
        Rectangle bounds = this.frame.getBounds();
        if ((this.frame.getExtendedState() & 6) != 0) {
            this.configManager.setConfiguration(CONFIG_GROUP, CONFIG_CLIENT_BOUNDS, bounds);
            this.configManager.setConfiguration(CONFIG_GROUP, CONFIG_CLIENT_MAXIMIZED, true);
        } else {
            if (this.config.automaticResizeType() == ExpandResizeType.KEEP_GAME_SIZE && this.pluginPanel != null) {
                bounds.width -= this.pluginPanel.getWrappedPanel().getPreferredSize().width;
            }
            this.configManager.unsetConfiguration(CONFIG_GROUP, CONFIG_CLIENT_MAXIMIZED);
            this.configManager.setConfiguration(CONFIG_GROUP, CONFIG_CLIENT_BOUNDS, bounds);
        }
    }

    public TrayIcon getTrayIcon() {
        return this.trayIcon;
    }
}

