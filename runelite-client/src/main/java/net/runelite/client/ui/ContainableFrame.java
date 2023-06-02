/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.annotations.VisibleForTesting
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package net.runelite.client.ui;

import com.google.common.annotations.VisibleForTesting;
import java.awt.Container;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.Arrays;
import java.util.Comparator;
import javax.swing.JFrame;
import net.runelite.client.config.ExpandResizeType;
import net.runelite.client.util.OSType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContainableFrame
extends JFrame {
    private static final Logger log = LoggerFactory.getLogger(ContainableFrame.class);
    private static final int SCREEN_EDGE_CLOSE_DISTANCE = 40;
    private static boolean jdk8231564;
    private static boolean jdk8243925;
    private ExpandResizeType expandResizeType;
    private Mode containedInScreen;
    private boolean expandedClientOppositeDirection;

    @VisibleForTesting
    static boolean jdk8231564(String javaVersion) {
        if (ContainableFrame.isVersionOrGreater(javaVersion, 15, -1, -1)) {
            return true;
        }
        if (ContainableFrame.isVersionOrGreater(javaVersion, 14, -1, -1)) {
            return false;
        }
        if (ContainableFrame.isVersionOrGreater(javaVersion, 13, 0, 4)) {
            return true;
        }
        if (ContainableFrame.isVersionOrGreater(javaVersion, 12, -1, -1)) {
            return false;
        }
        return ContainableFrame.isVersionOrGreater(javaVersion, 11, 0, 8);
    }

    @VisibleForTesting
    static boolean jdk8243925(String javaVersion) {
        if (ContainableFrame.isVersionOrGreater(javaVersion, 15, -1, -1)) {
            return true;
        }
        if (ContainableFrame.isVersionOrGreater(javaVersion, 14, -1, -1)) {
            return false;
        }
        if (ContainableFrame.isVersionOrGreater(javaVersion, 13, 0, 7)) {
            return true;
        }
        if (ContainableFrame.isVersionOrGreater(javaVersion, 12, -1, -1)) {
            return false;
        }
        return ContainableFrame.isVersionOrGreater(javaVersion, 11, 0, 9);
    }

    private static boolean isVersionOrGreater(String javaVersion, int versionMajor, int versionMinor, int versionPatch) {
        int patch;
        int minor;
        int major;
        String[] s;
        int idx = javaVersion.indexOf(95);
        if (idx != -1) {
            javaVersion = javaVersion.substring(0, idx);
        }
        if ((s = javaVersion.split("\\.")).length >= 3) {
            major = Integer.parseInt(s[0]);
            minor = Integer.parseInt(s[1]);
            patch = Integer.parseInt(s[2]);
        } else {
            major = Integer.parseInt(s[0]);
            minor = -1;
            patch = -1;
        }
        int i = Integer.compare(major, versionMajor);
        if (i != 0) {
            return i > 0;
        }
        i = Integer.compare(minor, versionMinor);
        if (i != 0) {
            return i > 0;
        }
        i = Integer.compare(patch, versionPatch);
        if (i != 0) {
            return i > 0;
        }
        return true;
    }

    public void setContainedInScreen(Mode value) {
        this.containedInScreen = value;
        if (this.containedInScreen == Mode.ALWAYS) {
            this.setLocation(this.getX(), this.getY());
            this.setBounds(this.getX(), this.getY(), this.getWidth(), this.getHeight());
        }
    }

    @Override
    public void setLocation(int x, int y) {
        if (this.containedInScreen == Mode.ALWAYS) {
            Rectangle bounds = this.getGraphicsConfiguration().getBounds();
            x = Math.max(x, (int)bounds.getX());
            x = Math.min(x, (int)(bounds.getX() + bounds.getWidth() - (double)this.getWidth()));
            y = Math.max(y, (int)bounds.getY());
            y = Math.min(y, (int)(bounds.getY() + bounds.getHeight() - (double)this.getHeight()));
        }
        super.setLocation(x, y);
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        if (this.containedInScreen == Mode.ALWAYS) {
            Rectangle bounds = this.getGraphicsConfiguration().getBounds();
            width = Math.min(width, width - (int)bounds.getX() + x);
            x = Math.max(x, (int)bounds.getX());
            height = Math.min(height, height - (int)bounds.getY() + y);
            y = Math.max(y, (int)bounds.getY());
            width = Math.min(width, (int)(bounds.getX() + bounds.getWidth()) - x);
            height = Math.min(height, (int)(bounds.getY() + bounds.getHeight()) - y);
        }
        super.setBounds(x, y, width, height);
    }

    public void expandBy(int value) {
        int currentWidth;
        int minimumWidth;
        if (this.isFullScreen()) {
            return;
        }
        int increment = value;
        boolean forcedWidthIncrease = false;
        if (this.expandResizeType == ExpandResizeType.KEEP_WINDOW_SIZE && (minimumWidth = this.getLayout().minimumLayoutSize((Container)this).width) > (currentWidth = this.getWidth())) {
            forcedWidthIncrease = true;
            increment = minimumWidth - currentWidth;
        }
        if (forcedWidthIncrease || this.expandResizeType == ExpandResizeType.KEEP_GAME_SIZE) {
            int newWindowWidth = this.getWidth() + increment;
            int newWindowX = this.getX();
            if (this.containedInScreen != Mode.NEVER) {
                boolean wouldExpandThroughEdge;
                Rectangle screenBounds = this.getGraphicsConfiguration().getBounds();
                boolean bl = wouldExpandThroughEdge = (double)(this.getX() + newWindowWidth) > screenBounds.getX() + screenBounds.getWidth();
                if (wouldExpandThroughEdge) {
                    if (!this.isFrameCloseToRightEdge() || this.isFrameCloseToLeftEdge()) {
                        newWindowX = (int)(screenBounds.getX() + screenBounds.getWidth()) - this.getWidth();
                    }
                    newWindowX -= increment;
                    this.expandedClientOppositeDirection = true;
                }
            }
            this.setBounds(newWindowX, this.getY(), newWindowWidth, this.getHeight());
        }
        this.revalidateMinimumSize();
    }

    public void contractBy(int value) {
        if (this.isFullScreen()) {
            return;
        }
        this.revalidateMinimumSize();
        Rectangle screenBounds = this.getGraphicsConfiguration().getBounds();
        boolean wasCloseToLeftEdge = Math.abs((double)this.getX() - screenBounds.getX()) <= 40.0;
        int newWindowX = this.getX();
        int newWindowWidth = this.getWidth() - value;
        if (this.isFrameCloseToRightEdge() && (this.expandedClientOppositeDirection || !wasCloseToLeftEdge)) {
            newWindowX += value;
        }
        if (this.expandResizeType == ExpandResizeType.KEEP_WINDOW_SIZE && newWindowWidth > this.getMinimumSize().width) {
            newWindowWidth = this.getWidth();
            newWindowX = this.getX();
        }
        this.setBounds(newWindowX, this.getY(), newWindowWidth, this.getHeight());
        this.expandedClientOppositeDirection = false;
    }

    @Override
    public void setMaximizedBounds(Rectangle bounds) {
        if (OSType.getOSType() == OSType.MacOS) {
            super.setMaximizedBounds(bounds);
        } else {
            super.setMaximizedBounds(this.getWindowAreaBounds());
        }
    }

    private GraphicsConfiguration getCurrentDisplayConfiguration() {
        return Arrays.stream(GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()).map(GraphicsDevice::getDefaultConfiguration).max(Comparator.comparingInt(config -> {
            Rectangle intersection = config.getBounds().intersection(this.getBounds());
            return intersection.width * intersection.height;
        })).orElseGet(this::getGraphicsConfiguration);
    }

    private Rectangle getWindowAreaBounds() {
        log.trace("Current bounds: {}", (Object)this.getBounds());
        for (GraphicsDevice device : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) {
            log.trace("Device: {} bounds {}", (Object)device, (Object)device.getDefaultConfiguration().getBounds());
        }
        GraphicsConfiguration config = this.getCurrentDisplayConfiguration();
        Rectangle bounds = config.getBounds();
        log.trace("Chosen device: {} bounds {}", (Object)config, (Object)bounds);
        if (!jdk8231564) {
            bounds = config.getDefaultTransform().createTransformedShape(bounds).getBounds();
            log.trace("Transformed bounds {}", (Object)bounds);
        }
        Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(config);
        if (!jdk8231564) {
            bounds.y = 0;
            bounds.x = 0;
            assert (!jdk8243925) : "scaled insets without scaled bounds";
        } else if (!jdk8243925) {
            double scaleX = config.getDefaultTransform().getScaleX();
            double scaleY = config.getDefaultTransform().getScaleY();
            insets.top = (int)((double)insets.top / scaleY);
            insets.bottom = (int)((double)insets.bottom / scaleY);
            insets.left = (int)((double)insets.left / scaleX);
            insets.right = (int)((double)insets.right / scaleX);
        }
        bounds.x += insets.left;
        bounds.y += insets.top;
        bounds.height -= insets.bottom + insets.top;
        bounds.width -= insets.right + insets.left;
        log.trace("Final bounds: {}", (Object)bounds);
        return bounds;
    }

    public void revalidateMinimumSize() {
        this.setMinimumSize(this.getLayout().minimumLayoutSize(this));
    }

    private boolean isFullScreen() {
        return (this.getExtendedState() & 6) == 6;
    }

    private boolean isFrameCloseToLeftEdge() {
        Rectangle screenBounds = this.getGraphicsConfiguration().getBounds();
        return Math.abs((double)this.getX() - screenBounds.getX()) <= 40.0;
    }

    private boolean isFrameCloseToRightEdge() {
        Rectangle screenBounds = this.getGraphicsConfiguration().getBounds();
        return Math.abs((double)(this.getX() + this.getWidth()) - (screenBounds.getX() + screenBounds.getWidth())) <= 40.0;
    }

    public void setExpandResizeType(ExpandResizeType expandResizeType) {
        this.expandResizeType = expandResizeType;
    }

    static {
        try {
            String javaVersion = System.getProperty("java.version");
            jdk8231564 = ContainableFrame.jdk8231564(javaVersion);
            jdk8243925 = ContainableFrame.jdk8243925(javaVersion);
        }
        catch (Exception ex) {
            log.error("error checking java version", (Throwable)ex);
        }
    }

    public static enum Mode {
        ALWAYS,
        RESIZING,
        NEVER;

    }
}

