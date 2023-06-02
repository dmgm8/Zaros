/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  javax.inject.Singleton
 *  net.runelite.http.api.feed.FeedItem
 *  net.runelite.http.api.feed.FeedItemType
 *  net.runelite.http.api.feed.FeedResult
 *  okhttp3.Call
 *  okhttp3.Callback
 *  okhttp3.OkHttpClient
 *  okhttp3.Request
 *  okhttp3.Request$Builder
 *  okhttp3.Response
 *  okhttp3.ResponseBody
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package net.runelite.client.plugins.feed;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Comparator;
import java.util.function.Supplier;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import net.runelite.client.plugins.feed.FeedConfig;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.util.LinkBrowser;
import net.runelite.http.api.feed.FeedItem;
import net.runelite.http.api.feed.FeedItemType;
import net.runelite.http.api.feed.FeedResult;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
class FeedPanel
extends PluginPanel {
    private static final Logger log = LoggerFactory.getLogger(FeedPanel.class);
    private static final ImageIcon RUNELITE_ICON;
    private static final ImageIcon OSRS_ICON;
    private static final Color TWEET_BACKGROUND;
    private static final Color OSRS_NEWS_BACKGROUND;
    private static final Color BLOG_POST_BACKGROUND;
    private static final int MAX_CONTENT_LINES = 3;
    private static final int CONTENT_WIDTH = 148;
    private static final int TIME_WIDTH = 20;
    private static final Comparator<FeedItem> FEED_ITEM_COMPARATOR;
    private final FeedConfig config;
    private final Supplier<FeedResult> feedSupplier;
    private final OkHttpClient okHttpClient;

    @Inject
    FeedPanel(FeedConfig config, Supplier<FeedResult> feedSupplier, OkHttpClient okHttpClient) {
        super(true);
        this.config = config;
        this.feedSupplier = feedSupplier;
        this.okHttpClient = okHttpClient;
        this.setBorder(new EmptyBorder(10, 10, 10, 10));
        this.setBackground(ColorScheme.DARK_GRAY_COLOR);
        this.setLayout(new GridLayout(0, 1, 0, 4));
    }

    void rebuildFeed() {
        FeedResult feed = this.feedSupplier.get();
        if (feed == null) {
            return;
        }
        SwingUtilities.invokeLater(() -> {
            this.removeAll();
            feed.getItems().stream().filter(f -> f.getType() != FeedItemType.BLOG_POST || this.config.includeBlogPosts()).filter(f -> f.getType() != FeedItemType.TWEET || this.config.includeTweets()).filter(f -> f.getType() != FeedItemType.OSRS_NEWS || this.config.includeOsrsNews()).sorted(FEED_ITEM_COMPARATOR).forEach(this::addItemToPanel);
        });
    }

    private void addItemToPanel(final FeedItem item) {
        final JPanel avatarAndRight = new JPanel(new BorderLayout());
        avatarAndRight.setPreferredSize(new Dimension(0, 56));
        final JLabel avatar = new JLabel();
        avatar.setPreferredSize(new Dimension(52, 48));
        avatar.setBorder(new EmptyBorder(0, 4, 0, 0));
        switch (item.getType()) {
            case TWEET: {
                try {
                    Request request = new Request.Builder().url(item.getAvatar()).build();
                    this.okHttpClient.newCall(request).enqueue(new Callback(){

                        public void onFailure(Call call, IOException e) {
                            log.warn(null, (Throwable)e);
                        }

                        /*
                         * WARNING - Removed try catching itself - possible behaviour change.
                         */
                        public void onResponse(Call call, Response response) throws IOException {
                            try (ResponseBody responseBody = response.body();){
                                if (!response.isSuccessful()) {
                                    log.warn("Failed to download image " + item.getAvatar());
                                    return;
                                }
                                Class<ImageIO> class_ = ImageIO.class;
                                synchronized (ImageIO.class) {
                                    BufferedImage icon = ImageIO.read(responseBody.byteStream());
                                    // ** MonitorExit[var5_4] (shouldn't be in output)
                                    avatar.setIcon(new ImageIcon(icon));
                                }
                            }
                            {
                                return;
                            }
                        }
                    });
                }
                catch (IllegalArgumentException | NullPointerException e) {
                    log.warn(null, (Throwable)e);
                }
                avatarAndRight.setBackground(TWEET_BACKGROUND);
                break;
            }
            case OSRS_NEWS: {
                if (OSRS_ICON != null) {
                    avatar.setIcon(OSRS_ICON);
                }
                avatarAndRight.setBackground(OSRS_NEWS_BACKGROUND);
                break;
            }
            default: {
                if (RUNELITE_ICON != null) {
                    avatar.setIcon(RUNELITE_ICON);
                }
                avatarAndRight.setBackground(BLOG_POST_BACKGROUND);
            }
        }
        JPanel upAndContent = new JPanel();
        upAndContent.setLayout(new BoxLayout(upAndContent, 1));
        upAndContent.setBorder(new EmptyBorder(4, 8, 4, 4));
        upAndContent.setBackground(null);
        JPanel titleAndTime = new JPanel();
        titleAndTime.setLayout(new BorderLayout());
        titleAndTime.setBackground(null);
        Color darkerForeground = UIManager.getColor("Label.foreground").darker();
        JLabel titleLabel = new JLabel(item.getTitle());
        titleLabel.setFont(FontManager.getRunescapeSmallFont());
        titleLabel.setBackground(null);
        titleLabel.setForeground(darkerForeground);
        titleLabel.setPreferredSize(new Dimension(128, 0));
        Duration duration = Duration.between(Instant.ofEpochMilli(item.getTimestamp()), Instant.now());
        JLabel timeLabel = new JLabel(this.durationToString(duration));
        timeLabel.setFont(FontManager.getRunescapeSmallFont());
        timeLabel.setForeground(darkerForeground);
        titleAndTime.add((Component)titleLabel, "West");
        titleAndTime.add((Component)timeLabel, "East");
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(null);
        JLabel contentLabel = new JLabel(this.lineBreakText(item.getContent(), FontManager.getRunescapeSmallFont()));
        contentLabel.setBorder(new EmptyBorder(2, 0, 0, 0));
        contentLabel.setFont(FontManager.getRunescapeSmallFont());
        contentLabel.setForeground(darkerForeground);
        content.add((Component)contentLabel, "Center");
        upAndContent.add(titleAndTime);
        upAndContent.add(content);
        upAndContent.add(new Box.Filler(new Dimension(0, 0), new Dimension(0, 32767), new Dimension(0, 32767)));
        avatarAndRight.add((Component)avatar, "West");
        avatarAndRight.add((Component)upAndContent, "Center");
        final Color backgroundColor = avatarAndRight.getBackground();
        final Color hoverColor = backgroundColor.brighter().brighter();
        final Color pressedColor = hoverColor.brighter();
        avatarAndRight.addMouseListener(new MouseAdapter(){

            @Override
            public void mouseEntered(MouseEvent e) {
                avatarAndRight.setBackground(hoverColor);
                avatarAndRight.setCursor(new Cursor(12));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                avatarAndRight.setBackground(backgroundColor);
                avatarAndRight.setCursor(new Cursor(0));
            }

            @Override
            public void mousePressed(MouseEvent e) {
                avatarAndRight.setBackground(pressedColor);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                avatarAndRight.setBackground(hoverColor);
                LinkBrowser.browse(item.getUrl());
            }
        });
        this.add(avatarAndRight);
    }

    private String durationToString(Duration duration) {
        if (duration.getSeconds() >= 86400L) {
            return (int)(duration.getSeconds() / 86400L) + "d";
        }
        if (duration.getSeconds() >= 3600L) {
            return (int)(duration.getSeconds() / 3600L) + "h";
        }
        return (int)(duration.getSeconds() / 60L) + "m";
    }

    private String lineBreakText(String text, Font font) {
        StringBuilder newText = new StringBuilder("<html>");
        FontRenderContext fontRenderContext = new FontRenderContext(font.getTransform(), true, true);
        int lines = 0;
        int pos = 0;
        String[] words = text.split(" ");
        String line = "";
        while (lines < 3 && pos < words.length) {
            String newLine = pos > 0 ? line + " " + words[pos] : words[pos];
            double width = font.getStringBounds(newLine, fontRenderContext).getWidth();
            if (width >= 148.0) {
                newText.append(line);
                newText.append("<br>");
                line = "";
                ++lines;
                continue;
            }
            line = newLine;
            ++pos;
        }
        newText.append(line);
        newText.append("</html>");
        return newText.toString();
    }

    static {
        TWEET_BACKGROUND = new Color(15, 15, 15);
        OSRS_NEWS_BACKGROUND = new Color(36, 30, 19);
        BLOG_POST_BACKGROUND = new Color(11, 30, 41);
        FEED_ITEM_COMPARATOR = (o1, o2) -> {
            if (o1.getType() != o2.getType()) {
                if (o1.getType() == FeedItemType.BLOG_POST) {
                    return -1;
                }
                if (o2.getType() == FeedItemType.BLOG_POST) {
                    return 1;
                }
            }
            return -Long.compare(o1.getTimestamp(), o2.getTimestamp());
        };
        RUNELITE_ICON = new ImageIcon(ImageUtil.loadImageResource(FeedPanel.class, "runelite.png"));
        OSRS_ICON = new ImageIcon(ImageUtil.loadImageResource(FeedPanel.class, "osrs.png"));
    }
}

