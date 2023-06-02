/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Suppliers
 *  com.google.inject.Binder
 *  com.google.inject.Provides
 *  com.google.inject.TypeLiteral
 *  javax.inject.Inject
 *  net.runelite.http.api.feed.FeedResult
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package net.runelite.client.plugins.feed;

import com.google.common.base.Suppliers;
import com.google.inject.Binder;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import javax.inject.Inject;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.feed.FeedClient;
import net.runelite.client.plugins.feed.FeedConfig;
import net.runelite.client.plugins.feed.FeedPanel;
import net.runelite.client.task.Schedule;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.util.ImageUtil;
import net.runelite.http.api.feed.FeedResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(name="News Feed", description="Show the latest RuneLite blog posts, OSRS news, and JMod Twitter posts", tags={"external", "integration", "panel", "twitter"}, loadWhenOutdated=true, forceDisabled=true)
public class FeedPlugin
extends Plugin {
    private static final Logger log = LoggerFactory.getLogger(FeedPlugin.class);
    @Inject
    private ClientToolbar clientToolbar;
    @Inject
    private ScheduledExecutorService executorService;
    @Inject
    private FeedClient feedClient;
    private FeedPanel feedPanel;
    private NavigationButton navButton;
    private final Supplier<FeedResult> feedSupplier = Suppliers.memoizeWithExpiration(() -> {
        try {
            return this.feedClient.lookupFeed();
        }
        catch (IOException e) {
            log.warn(null, (Throwable)e);
            return null;
        }
    }, (long)10L, (TimeUnit)TimeUnit.MINUTES);

    @Override
    public void configure(Binder binder) {
        binder.bind((TypeLiteral)new TypeLiteral<Supplier<FeedResult>>(){}).toInstance(this.feedSupplier);
    }

    @Override
    protected void startUp() throws Exception {
        this.feedPanel = (FeedPanel)this.injector.getInstance(FeedPanel.class);
        BufferedImage icon = ImageUtil.loadImageResource(this.getClass(), "icon.png");
        this.navButton = NavigationButton.builder().tooltip("News Feed").icon(icon).priority(8).panel(this.feedPanel).build();
        this.clientToolbar.addNavigation(this.navButton);
        this.executorService.submit(this::updateFeed);
    }

    @Override
    protected void shutDown() throws Exception {
        this.clientToolbar.removeNavigation(this.navButton);
    }

    private void updateFeed() {
        this.feedPanel.rebuildFeed();
    }

    @Subscribe
    public void onConfigChanged(ConfigChanged event) {
        if (event.getGroup().equals("feed")) {
            this.executorService.submit(this::updateFeed);
        }
    }

    @Schedule(period=10L, unit=ChronoUnit.MINUTES, asynchronous=true)
    public void updateFeedTask() {
        this.updateFeed();
    }

    @Provides
    FeedConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(FeedConfig.class);
    }
}

