/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.base.MoreObjects
 *  com.google.inject.Inject
 *  javax.annotation.Nullable
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  net.runelite.api.Client
 */
package net.runelite.client.plugins.info;

import com.google.common.base.MoreObjects;
import com.google.inject.Inject;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.ScheduledExecutorService;
import javax.annotation.Nullable;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.HyperlinkEvent;
import net.runelite.api.Client;
import net.runelite.client.RuneLiteProperties;
import net.runelite.client.account.SessionManager;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.SessionClose;
import net.runelite.client.events.SessionOpen;
import net.runelite.client.plugins.info.JRichTextPane;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.util.LinkBrowser;

@Singleton
public class InfoPanel
extends PluginPanel {
    private static final String RUNELITE_LOGIN = "https://runelite_login/";
    private static final String TWITTER_LINK = "https://twitter.com/ZarosRSPS";
    private static final String FACEBOOK_LINK = "https://www.facebook.com/ZarosPS";
    private static final String YOUTUBE_LINK = "";
    private static final ImageIcon ARROW_RIGHT_ICON = new ImageIcon(ImageUtil.loadImageResource(InfoPanel.class, "/util/arrow_right.png"));
    private static final ImageIcon GITHUB_ICON = new ImageIcon(ImageUtil.loadImageResource(InfoPanel.class, "github_icon.png"));
    private static final ImageIcon DISCORD_ICON = new ImageIcon(ImageUtil.loadImageResource(InfoPanel.class, "discord_icon.png"));
    private static final ImageIcon PATREON_ICON = new ImageIcon(ImageUtil.loadImageResource(InfoPanel.class, "patreon_icon.png"));
    private static final ImageIcon WIKI_ICON = new ImageIcon(ImageUtil.loadImageResource(InfoPanel.class, "wiki_icon.png"));
    private static final ImageIcon TWITTER_ICON;
    private static final ImageIcon FACEBOOK_ICON;
    private static final ImageIcon YOUTUBE_ICON;
    private static final ImageIcon IMPORT_ICON;
    private final JLabel loggedLabel = new JLabel();
    private final JRichTextPane emailLabel = new JRichTextPane();
    private JPanel syncPanel;
    private JPanel actionsContainer;
    @Inject
    @Nullable
    private Client client;
    @Inject
    private RuneLiteProperties runeLiteProperties;
    @Inject
    private EventBus eventBus;
    @Inject
    private SessionManager sessionManager;
    @Inject
    private ScheduledExecutorService executor;
    @Inject
    private ConfigManager configManager;
    @Inject
    @Named(value="runelite.version")
    private String runeliteVersion;
    @Inject
    @Named(value="runelite.github.link")
    private String githubLink;
    @Inject
    @Named(value="runelite.discord.invite")
    private String discordInvite;
    @Inject
    @Named(value="runelite.patreon.link")
    private String patreonLink;
    @Inject
    @Named(value="runelite.wiki.link")
    private String wikiLink;

    void init() {
        this.setLayout(new BorderLayout());
        this.setBackground(ColorScheme.DARK_GRAY_COLOR);
        this.setBorder(new EmptyBorder(10, 10, 10, 10));
        JPanel versionPanel = new JPanel();
        versionPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        versionPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        versionPanel.setLayout(new GridLayout(0, 1));
        Font smallFont = FontManager.getRunescapeSmallFont();
        JLabel version = new JLabel(InfoPanel.htmlLabel("RuneLite version: ", this.runeliteVersion));
        version.setFont(smallFont);
        JLabel revision = new JLabel();
        revision.setFont(smallFont);
        String engineVer = "Unknown";
        if (this.client != null) {
            engineVer = String.format("Rev %d", this.client.getRevision());
        }
        revision.setText(InfoPanel.htmlLabel("Oldschool revision: ", engineVer));
        JLabel launcher = new JLabel(InfoPanel.htmlLabel("Launcher version: ", (String)MoreObjects.firstNonNull((Object)RuneLiteProperties.getLauncherVersion(), (Object)"Unknown")));
        launcher.setFont(smallFont);
        this.loggedLabel.setForeground(ColorScheme.LIGHT_GRAY_COLOR);
        this.loggedLabel.setFont(smallFont);
        this.emailLabel.setForeground(Color.WHITE);
        this.emailLabel.setFont(smallFont);
        this.emailLabel.enableAutoLinkHandler(false);
        this.emailLabel.addHyperlinkListener(e -> {
            if (HyperlinkEvent.EventType.ACTIVATED.equals(e.getEventType()) && e.getURL() != null && e.getURL().toString().equals(RUNELITE_LOGIN)) {
                this.executor.execute(this.sessionManager::login);
            }
        });
        versionPanel.add(version);
        versionPanel.add(revision);
        versionPanel.add(launcher);
        this.actionsContainer = new JPanel();
        this.actionsContainer.setBorder(new EmptyBorder(10, 0, 0, 0));
        this.actionsContainer.setLayout(new GridLayout(0, 1, 0, 10));
        this.syncPanel = InfoPanel.buildLinkPanel(IMPORT_ICON, "Import signed-out", "settings", () -> {
            int result = JOptionPane.showOptionDialog(this.syncPanel, "<html>This will overwrite your settings with settings from your local profile, which<br/>is the profile used when not signed into RuneLite with a RuneLite account.</html>", "Are you sure?", 0, 2, null, new String[]{"Yes", "No"}, "No");
            if (result == 0) {
                this.configManager.importLocal();
            }
        });
        this.actionsContainer.add(InfoPanel.buildLinkPanel(DISCORD_ICON, "Talk to us on our", "Discord server", RuneLiteProperties.getDiscordInvite()));
        this.actionsContainer.add(InfoPanel.buildLinkPanel(TWITTER_ICON, "Check us out on", "Twitter", TWITTER_LINK));
        this.actionsContainer.add(InfoPanel.buildLinkPanel(FACEBOOK_ICON, "Check us out on", "Facebook", FACEBOOK_LINK));
        this.add((Component)versionPanel, "North");
        this.add((Component)this.actionsContainer, "Center");
        this.updateLoggedIn();
        this.eventBus.register(this);
    }

    private static JPanel buildLinkPanel(ImageIcon icon, String topText, String bottomText, String url) {
        return InfoPanel.buildLinkPanel(icon, topText, bottomText, () -> LinkBrowser.browse(url));
    }

    private static JPanel buildLinkPanel(ImageIcon icon, String topText, String bottomText, final Runnable callback) {
        final JPanel container = new JPanel();
        container.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        container.setLayout(new BorderLayout());
        container.setBorder(new EmptyBorder(10, 10, 10, 10));
        final Color hoverColor = ColorScheme.DARKER_GRAY_HOVER_COLOR;
        final Color pressedColor = ColorScheme.DARKER_GRAY_COLOR.brighter();
        JLabel iconLabel = new JLabel(icon);
        container.add((Component)iconLabel, "West");
        final JPanel textContainer = new JPanel();
        textContainer.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        textContainer.setLayout(new GridLayout(2, 1));
        textContainer.setBorder(new EmptyBorder(5, 10, 5, 10));
        container.addMouseListener(new MouseAdapter(){

            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                container.setBackground(pressedColor);
                textContainer.setBackground(pressedColor);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                callback.run();
                container.setBackground(hoverColor);
                textContainer.setBackground(hoverColor);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                container.setBackground(hoverColor);
                textContainer.setBackground(hoverColor);
                container.setCursor(new Cursor(12));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                container.setBackground(ColorScheme.DARKER_GRAY_COLOR);
                textContainer.setBackground(ColorScheme.DARKER_GRAY_COLOR);
                container.setCursor(new Cursor(0));
            }
        });
        JLabel topLine = new JLabel(topText);
        topLine.setForeground(Color.WHITE);
        topLine.setFont(FontManager.getRunescapeSmallFont());
        JLabel bottomLine = new JLabel(bottomText);
        bottomLine.setForeground(Color.WHITE);
        bottomLine.setFont(FontManager.getRunescapeSmallFont());
        textContainer.add(topLine);
        textContainer.add(bottomLine);
        container.add((Component)textContainer, "Center");
        JLabel arrowLabel = new JLabel(ARROW_RIGHT_ICON);
        container.add((Component)arrowLabel, "East");
        return container;
    }

    private void updateLoggedIn() {
        String name;
        String string = name = this.sessionManager.getAccountSession() != null ? this.sessionManager.getAccountSession().getUsername() : null;
        if (name != null) {
            this.emailLabel.setContentType("text/plain");
            this.emailLabel.setText(name);
            this.loggedLabel.setText("Signed in as");
            this.actionsContainer.add((Component)this.syncPanel, 0);
        } else {
            this.emailLabel.setContentType("text/html");
            this.emailLabel.setText("<a href=\"https://runelite_login/\">Sign in</a> to sync settings to the cloud.");
            this.loggedLabel.setText("Not signed in");
            this.actionsContainer.remove(this.syncPanel);
        }
    }

    private static String htmlLabel(String key, String value) {
        return "<html><body style = 'color:#a5a5a5'>" + key + "<span style = 'color:white'>" + value + "</span></body></html>";
    }

    @Subscribe
    public void onSessionOpen(SessionOpen sessionOpen) {
        this.updateLoggedIn();
    }

    @Subscribe
    public void onSessionClose(SessionClose e) {
        this.updateLoggedIn();
    }

    static {
        IMPORT_ICON = new ImageIcon(ImageUtil.loadImageResource(InfoPanel.class, "import_icon.png"));
        TWITTER_ICON = new ImageIcon(ImageUtil.getResourceStreamFromClass(InfoPanel.class, "twitter_icon.png"));
        FACEBOOK_ICON = new ImageIcon(ImageUtil.getResourceStreamFromClass(InfoPanel.class, "facebook_icon.png"));
        YOUTUBE_ICON = new ImageIcon(ImageUtil.getResourceStreamFromClass(InfoPanel.class, "youtube_icon.png"));
    }
}

