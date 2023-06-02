/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.HashMultimap
 *  com.google.common.collect.ImmutableMap
 *  com.google.common.collect.Multimap
 *  com.google.common.collect.Sets
 *  com.google.common.html.HtmlEscapers
 *  javax.annotation.Nullable
 *  javax.inject.Inject
 *  javax.inject.Singleton
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package net.runelite.client.plugins.config;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.common.html.HtmlEscapers;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.LayoutStyle;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import net.runelite.client.config.Config;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ExternalPluginsChanged;
import net.runelite.client.externalplugins.ExternalPluginClient;
import net.runelite.client.externalplugins.ExternalPluginManager;
import net.runelite.client.externalplugins.ExternalPluginManifest;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginManager;
import net.runelite.client.plugins.config.ConfigPanel;
import net.runelite.client.plugins.config.FixedWidthPanel;
import net.runelite.client.plugins.config.PluginListPanel;
import net.runelite.client.plugins.config.PluginSearch;
import net.runelite.client.plugins.config.SearchablePlugin;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.DynamicGridLayout;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.ui.components.IconTextField;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.util.LinkBrowser;
import net.runelite.client.util.SwingUtil;
import net.runelite.client.util.VerificationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
class PluginHubPanel
extends PluginPanel {
    private static final Logger log = LoggerFactory.getLogger(PluginHubPanel.class);
    private static final ImageIcon MISSING_ICON;
    private static final ImageIcon HELP_ICON;
    private static final ImageIcon HELP_ICON_HOVER;
    private static final ImageIcon CONFIGURE_ICON;
    private static final ImageIcon CONFIGURE_ICON_HOVER;
    private static final Pattern SPACES;
    private final PluginListPanel pluginListPanel;
    private final ExternalPluginManager externalPluginManager;
    private final PluginManager pluginManager;
    private final ExternalPluginClient externalPluginClient;
    private final ScheduledExecutorService executor;
    private final Deque<PluginIcon> iconLoadQueue = new ArrayDeque<PluginIcon>();
    private final IconTextField searchBar;
    private final JLabel refreshing;
    private final JPanel mainPanel;
    private List<PluginItem> plugins = null;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void pumpIconQueue() {
        PluginIcon pi;
        Deque<PluginIcon> deque = this.iconLoadQueue;
        synchronized (deque) {
            pi = this.iconLoadQueue.poll();
        }
        if (pi == null) {
            return;
        }
        pi.load();
        deque = this.iconLoadQueue;
        synchronized (deque) {
            if (this.iconLoadQueue.isEmpty()) {
                return;
            }
        }
        this.executor.submit(this::pumpIconQueue);
    }

    @Inject
    PluginHubPanel(PluginListPanel pluginListPanel, ExternalPluginManager externalPluginManager, PluginManager pluginManager, ExternalPluginClient externalPluginClient, ScheduledExecutorService executor) {
        super(false);
        this.pluginListPanel = pluginListPanel;
        this.externalPluginManager = externalPluginManager;
        this.pluginManager = pluginManager;
        this.externalPluginClient = externalPluginClient;
        this.executor = executor;
        String refresh = "this could just be a lambda, but no, it has to be abstracted";
        this.getInputMap(1).put(KeyStroke.getKeyStroke(116, 0), refresh);
        this.getActionMap().put(refresh, new AbstractAction(){

            @Override
            public void actionPerformed(ActionEvent e) {
                PluginHubPanel.this.reloadPluginList();
            }
        });
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        this.setBackground(ColorScheme.DARK_GRAY_COLOR);
        this.searchBar = new IconTextField();
        this.searchBar.setIcon(IconTextField.Icon.SEARCH);
        this.searchBar.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        this.searchBar.setHoverBackgroundColor(ColorScheme.DARK_GRAY_HOVER_COLOR);
        this.searchBar.getDocument().addDocumentListener(new DocumentListener(){

            @Override
            public void insertUpdate(DocumentEvent e) {
                PluginHubPanel.this.filter();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                PluginHubPanel.this.filter();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                PluginHubPanel.this.filter();
            }
        });
        JLabel externalPluginWarning = new JLabel("<html>External plugins are verified to not be malicious or rule-breaking, but are not maintained by the RuneLite developers. They may cause bugs or instability.</html>");
        externalPluginWarning.setBackground(new Color(0xFFBB33));
        externalPluginWarning.setForeground(Color.BLACK);
        externalPluginWarning.setBorder(new EmptyBorder(5, 5, 5, 2));
        externalPluginWarning.setOpaque(true);
        JLabel externalPluginWarning2 = new JLabel("Use at your own risk!");
        externalPluginWarning2.setHorizontalAlignment(0);
        externalPluginWarning2.setFont(FontManager.getRunescapeBoldFont());
        externalPluginWarning2.setBackground(externalPluginWarning.getBackground());
        externalPluginWarning2.setForeground(externalPluginWarning.getForeground());
        externalPluginWarning2.setBorder(new EmptyBorder(0, 5, 5, 5));
        externalPluginWarning2.setOpaque(true);
        JButton backButton = new JButton(ConfigPanel.BACK_ICON);
        backButton.setRolloverIcon(ConfigPanel.BACK_ICON_HOVER);
        SwingUtil.removeButtonDecorations(backButton);
        backButton.setToolTipText("Back");
        backButton.addActionListener(l -> pluginListPanel.getMuxer().popState());
        this.mainPanel = new JPanel();
        this.mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 7, 7, 7));
        this.mainPanel.setLayout(new DynamicGridLayout(0, 1, 0, 5));
        this.mainPanel.setAlignmentX(0.0f);
        this.refreshing = new JLabel("Loading...");
        this.refreshing.setHorizontalAlignment(0);
        FixedWidthPanel mainPanelWrapper = new FixedWidthPanel();
        mainPanelWrapper.setLayout(new BorderLayout());
        mainPanelWrapper.add((Component)this.mainPanel, "North");
        mainPanelWrapper.add((Component)this.refreshing, "Center");
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setHorizontalScrollBarPolicy(31);
        scrollPane.setPreferredSize(new Dimension(28672, 28672));
        scrollPane.setViewportView(mainPanelWrapper);
        layout.setVerticalGroup(layout.createSequentialGroup().addComponent(externalPluginWarning).addComponent(externalPluginWarning2).addGap(10).addGroup(layout.createParallelGroup().addComponent(backButton).addComponent(this.searchBar)).addGap(10).addComponent(scrollPane));
        layout.setHorizontalGroup(layout.createParallelGroup().addComponent(externalPluginWarning, 0, 32767, 32767).addComponent(externalPluginWarning2, 0, 32767, 32767).addGroup(layout.createSequentialGroup().addComponent(backButton).addComponent(this.searchBar).addGap(10)).addComponent(scrollPane));
        this.revalidate();
        this.refreshing.setVisible(false);
        this.reloadPluginList();
    }

    private void reloadPluginList() {
        if (this.refreshing.isVisible()) {
            return;
        }
        this.refreshing.setVisible(true);
        this.mainPanel.removeAll();
        this.executor.submit(() -> {
            List<ExternalPluginManifest> manifest;
            try {
                manifest = this.externalPluginClient.downloadManifest();
            }
            catch (IOException | VerificationException e) {
                log.error("", (Throwable)e);
                SwingUtilities.invokeLater(() -> {
                    this.refreshing.setVisible(false);
                    this.mainPanel.add(new JLabel("Downloading the plugin manifest failed"));
                    JButton retry = new JButton("Retry");
                    retry.addActionListener(l -> this.reloadPluginList());
                    this.mainPanel.add(retry);
                });
                return;
            }
            Map<String, Integer> pluginCounts = Collections.emptyMap();
            try {
                pluginCounts = this.externalPluginClient.getPluginCounts();
            }
            catch (IOException e) {
                log.warn("unable to download plugin counts", (Throwable)e);
            }
            this.reloadPluginList(manifest, pluginCounts);
        });
    }

    private void reloadPluginList(List<ExternalPluginManifest> manifest, Map<String, Integer> pluginCounts) {
        Map manifests = (Map)manifest.stream().collect(ImmutableMap.toImmutableMap(ExternalPluginManifest::getInternalName, Function.identity()));
        HashMultimap loadedPlugins = HashMultimap.create();
        for (Plugin p : this.pluginManager.getPlugins()) {
            Class<?> clazz = p.getClass();
            ExternalPluginManifest mf = ExternalPluginManager.getExternalPluginManifest(clazz);
            if (mf == null) continue;
            loadedPlugins.put((Object)mf.getInternalName(), (Object)p);
        }
        HashSet<String> installed = new HashSet<String>(this.externalPluginManager.getInstalledExternalPlugins());
        SwingUtilities.invokeLater(() -> this.lambda$reloadPluginList$5(manifests, (Multimap)loadedPlugins, pluginCounts, installed));
    }

    void filter() {
        boolean isSearching;
        if (this.refreshing.isVisible() || this.plugins == null) {
            return;
        }
        this.mainPanel.removeAll();
        Stream stream = this.plugins.stream();
        String query = this.searchBar.getText();
        boolean bl = isSearching = query != null && !query.trim().isEmpty();
        if (isSearching) {
            PluginSearch.search(this.plugins, query).forEach(this.mainPanel::add);
        } else {
            stream.sorted(Comparator.comparing(PluginItem::isInstalled).thenComparingInt(PluginItem::getUserCount).reversed().thenComparing(p -> ((PluginItem)p).manifest.getDisplayName())).forEach(this.mainPanel::add);
        }
        this.mainPanel.revalidate();
    }

    @Override
    public void onActivate() {
        this.revalidate();
        this.reloadPluginList();
        this.searchBar.setText("");
        this.searchBar.requestFocusInWindow();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void onDeactivate() {
        this.mainPanel.removeAll();
        this.refreshing.setVisible(false);
        this.plugins = null;
        Deque<PluginIcon> deque = this.iconLoadQueue;
        synchronized (deque) {
            PluginIcon pi;
            while ((pi = this.iconLoadQueue.poll()) != null) {
                pi.loadingStarted = false;
            }
        }
    }

    @Subscribe
    private void onExternalPluginsChanged(ExternalPluginsChanged ev) {
        Map<String, Integer> pluginCounts = Collections.emptyMap();
        if (this.plugins != null) {
            pluginCounts = this.plugins.stream().collect(Collectors.toMap(pi -> ((PluginItem)pi).manifest.getInternalName(), PluginItem::getUserCount));
        }
        if (!this.refreshing.isVisible()) {
            this.refreshing.setVisible(true);
            this.reloadPluginList(ev.getLoadedManifest(), pluginCounts);
        }
    }

    private /* synthetic */ void lambda$reloadPluginList$5(Map manifests, Multimap loadedPlugins, Map pluginCounts, Set installed) {
        if (!this.refreshing.isVisible()) {
            return;
        }
        this.plugins = Sets.union(manifests.keySet(), (Set)loadedPlugins.keySet()).stream().map(id -> new PluginItem((ExternalPluginManifest)manifests.get(id), loadedPlugins.get(id), pluginCounts.getOrDefault(id, -1), installed.contains(id))).collect(Collectors.toList());
        this.refreshing.setVisible(false);
        this.filter();
    }

    static {
        SPACES = Pattern.compile(" +");
        BufferedImage missingIcon = ImageUtil.loadImageResource(PluginHubPanel.class, "pluginhub_missingicon.png");
        MISSING_ICON = new ImageIcon(missingIcon);
        BufferedImage helpIcon = ImageUtil.loadImageResource(PluginHubPanel.class, "pluginhub_help.png");
        HELP_ICON = new ImageIcon(helpIcon);
        HELP_ICON_HOVER = new ImageIcon(ImageUtil.alphaOffset((Image)helpIcon, -100));
        BufferedImage configureIcon = ImageUtil.loadImageResource(PluginHubPanel.class, "pluginhub_configure.png");
        CONFIGURE_ICON = new ImageIcon(configureIcon);
        CONFIGURE_ICON_HOVER = new ImageIcon(ImageUtil.alphaOffset((Image)configureIcon, -100));
    }

    private class PluginItem
    extends JPanel
    implements SearchablePlugin {
        private static final int HEIGHT = 70;
        private static final int ICON_WIDTH = 48;
        private static final int BOTTOM_LINE_HEIGHT = 16;
        private final ExternalPluginManifest manifest;
        private final List<String> keywords = new ArrayList<String>();
        private final int userCount;
        private final boolean installed;

        PluginItem(ExternalPluginManifest newManifest, Collection<Plugin> loadedPlugins, int userCount, boolean installed) {
            ExternalPluginManifest loaded = null;
            if (!loadedPlugins.isEmpty()) {
                loaded = ExternalPluginManager.getExternalPluginManifest(loadedPlugins.iterator().next().getClass());
            }
            this.manifest = newManifest == null ? loaded : newManifest;
            this.userCount = userCount;
            this.installed = installed;
            if (this.manifest != null) {
                Collections.addAll(this.keywords, SPACES.split(this.manifest.getDisplayName().toLowerCase()));
                if (this.manifest.getDescription() != null) {
                    Collections.addAll(this.keywords, SPACES.split(this.manifest.getDescription().toLowerCase()));
                }
                Collections.addAll(this.keywords, this.manifest.getAuthor().toLowerCase());
                if (this.manifest.getTags() != null) {
                    Collections.addAll(this.keywords, this.manifest.getTags());
                }
            }
            this.setBackground(ColorScheme.DARKER_GRAY_COLOR);
            this.setOpaque(true);
            GroupLayout layout = new GroupLayout(this);
            this.setLayout(layout);
            JLabel pluginName = new JLabel(this.manifest.getDisplayName());
            pluginName.setFont(FontManager.getRunescapeBoldFont());
            pluginName.setToolTipText(this.manifest.getDisplayName());
            JLabel author = new JLabel(this.manifest.getAuthor());
            author.setFont(FontManager.getRunescapeSmallFont());
            author.setToolTipText(this.manifest.getAuthor());
            JLabel version = new JLabel(this.manifest.getVersion());
            version.setFont(FontManager.getRunescapeSmallFont());
            version.setToolTipText(this.manifest.getVersion());
            String descriptionText = this.manifest.getDescription();
            if (!descriptionText.startsWith("<html>")) {
                descriptionText = "<html>" + HtmlEscapers.htmlEscaper().escape(descriptionText) + "</html>";
            }
            JLabel description = new JLabel(descriptionText);
            description.setVerticalAlignment(1);
            description.setToolTipText(descriptionText);
            PluginIcon icon = new PluginIcon(this.manifest);
            icon.setHorizontalAlignment(0);
            JButton help = new JButton(HELP_ICON);
            help.setRolloverIcon(HELP_ICON_HOVER);
            SwingUtil.removeButtonDecorations(help);
            help.setBorder(null);
            if (this.manifest.getSupport() == null) {
                help.setVisible(false);
            } else {
                help.setToolTipText("Open help: " + this.manifest.getSupport().toString());
                help.addActionListener(ev -> LinkBrowser.browse(this.manifest.getSupport().toString()));
            }
            JButton configure = new JButton(CONFIGURE_ICON);
            configure.setRolloverIcon(CONFIGURE_ICON_HOVER);
            SwingUtil.removeButtonDecorations(configure);
            configure.setToolTipText("Configure");
            help.setBorder(null);
            if (loaded != null) {
                String search = null;
                if (loadedPlugins.size() > 1) {
                    search = loaded.getInternalName();
                } else {
                    Plugin plugin = loadedPlugins.iterator().next();
                    Config cfg = PluginHubPanel.this.pluginManager.getPluginConfigProxy(plugin);
                    if (cfg == null) {
                        search = loaded.getInternalName();
                    } else {
                        configure.addActionListener(l -> PluginHubPanel.this.pluginListPanel.openConfigurationPanel(plugin));
                    }
                }
                if (search != null) {
                    String javaIsABadLanguage = search;
                    configure.addActionListener(l -> PluginHubPanel.this.pluginListPanel.openWithFilter(javaIsABadLanguage));
                }
            } else {
                configure.setVisible(false);
            }
            boolean install = !installed;
            boolean update = loaded != null && newManifest != null && !newManifest.equals(loaded);
            boolean remove = !install && !update;
            JButton addrm = new JButton();
            if (install) {
                addrm.setText("Install");
                addrm.setBackground(new Color(2670120));
                addrm.addActionListener(l -> {
                    int result;
                    if (this.manifest.getWarning() != null && (result = JOptionPane.showConfirmDialog(this, "<html><p>" + this.manifest.getWarning() + "</p><strong>Are you sure you want to install this plugin?</strong></html>", "Installing " + this.manifest.getDisplayName(), 0, 2)) != 0) {
                        return;
                    }
                    addrm.setText("Installing");
                    addrm.setBackground(ColorScheme.MEDIUM_GRAY_COLOR);
                    PluginHubPanel.this.externalPluginManager.install(this.manifest.getInternalName());
                });
            } else if (remove) {
                addrm.setText("Remove");
                addrm.setBackground(new Color(12462120));
                addrm.addActionListener(l -> {
                    addrm.setText("Removing");
                    addrm.setBackground(ColorScheme.MEDIUM_GRAY_COLOR);
                    PluginHubPanel.this.externalPluginManager.remove(this.manifest.getInternalName());
                });
            } else {
                assert (update);
                addrm.setText("Update");
                addrm.setBackground(new Color(2056735));
                addrm.addActionListener(l -> {
                    addrm.setText("Updating");
                    addrm.setBackground(ColorScheme.MEDIUM_GRAY_COLOR);
                    PluginHubPanel.this.externalPluginManager.update();
                });
            }
            addrm.setBorder(new LineBorder(addrm.getBackground().darker()));
            addrm.setFocusPainted(false);
            layout.setHorizontalGroup(layout.createSequentialGroup().addComponent(icon, 48, 48, 48).addGap(5).addGroup(layout.createParallelGroup().addGroup(layout.createSequentialGroup().addComponent(pluginName, 0, -2, 32767).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, -2, 32767).addComponent(author, 0, -2, 32767)).addComponent(description, 0, -2, 32767).addGroup(layout.createSequentialGroup().addComponent(version, 0, -2, 32767).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, -2, 100).addComponent(help, 0, 24, 24).addComponent(configure, 0, 24, 24).addComponent(addrm, 0, 57, -2).addGap(5))));
            int lineHeight = description.getFontMetrics(description.getFont()).getHeight();
            layout.setVerticalGroup(layout.createParallelGroup().addComponent(icon, 70, -1, 70 + lineHeight).addGroup(layout.createSequentialGroup().addGap(5).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(pluginName).addComponent(author)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, -2, 32767).addComponent(description, lineHeight, -2, lineHeight * 2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, -2, 32767).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(version, 16, 16, 16).addComponent(help, 16, 16, 16).addComponent(configure, 16, 16, 16).addComponent(addrm, 16, 16, 16)).addGap(5)));
        }

        @Override
        public String getSearchableName() {
            return this.manifest.getDisplayName();
        }

        @Override
        public List<String> getKeywords() {
            return this.keywords;
        }

        public int getUserCount() {
            return this.userCount;
        }

        public boolean isInstalled() {
            return this.installed;
        }
    }

    private class PluginIcon
    extends JLabel {
        @Nullable
        private final ExternalPluginManifest manifest;
        private boolean loadingStarted;
        private boolean loaded;

        PluginIcon(ExternalPluginManifest manifest) {
            this.setIcon(MISSING_ICON);
            this.manifest = manifest.hasIcon() ? manifest : null;
            this.loaded = !manifest.hasIcon();
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void paint(Graphics g) {
            super.paint(g);
            if (!this.loaded && !this.loadingStarted) {
                this.loadingStarted = true;
                Deque deque = PluginHubPanel.this.iconLoadQueue;
                synchronized (deque) {
                    PluginHubPanel.this.iconLoadQueue.add(this);
                    if (PluginHubPanel.this.iconLoadQueue.size() == 1) {
                        PluginHubPanel.this.executor.submit(() -> PluginHubPanel.this.pumpIconQueue());
                    }
                }
            }
        }

        private void load() {
            try {
                BufferedImage img = PluginHubPanel.this.externalPluginClient.downloadIcon(this.manifest);
                this.loaded = true;
                SwingUtilities.invokeLater(() -> this.setIcon(new ImageIcon(img)));
            }
            catch (IOException e) {
                log.info("Cannot download icon for plugin \"{}\"", (Object)this.manifest.getInternalName(), (Object)e);
            }
        }
    }
}

