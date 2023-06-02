/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  javax.annotation.Nullable
 *  net.runelite.api.widgets.WidgetInfo
 */
package net.runelite.client.ui.overlay;

import com.google.common.base.Preconditions;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayMenuEntry;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.components.LayoutableRenderableEntity;

public abstract class Overlay
implements LayoutableRenderableEntity {
    @Nullable
    private final Plugin plugin;
    private Point preferredLocation;
    private Dimension preferredSize;
    private OverlayPosition preferredPosition;
    private Rectangle bounds = new Rectangle();
    private OverlayPosition position = OverlayPosition.TOP_LEFT;
    private OverlayPriority priority = OverlayPriority.NONE;
    private OverlayLayer layer = OverlayLayer.UNDER_WIDGETS;
    private final List<Integer> drawHooks = new ArrayList<Integer>();
    private final List<OverlayMenuEntry> menuEntries = new ArrayList<OverlayMenuEntry>();
    private boolean resizable;
    private int minimumSize = 32;
    private boolean resettable = true;
    private boolean dragTargetable;
    private boolean movable = true;
    private boolean snappable = true;

    protected Overlay() {
        this.plugin = null;
    }

    protected Overlay(@Nullable Plugin plugin) {
        this.plugin = plugin;
    }

    public String getName() {
        return this.getClass().getSimpleName();
    }

    protected void drawAfterInterface(int interfaceId) {
        this.drawHooks.add(interfaceId << 16 | 0xFFFF);
    }

    protected void drawAfterLayer(int groupId, int childId) {
        Preconditions.checkArgument((groupId >= 0 && groupId <= 65535 ? 1 : 0) != 0, (Object)"groupId outside of valid range");
        Preconditions.checkArgument((childId >= 0 && childId <= 65535 ? 1 : 0) != 0, (Object)"childId outside of valid range");
        this.drawHooks.add(groupId << 16 | childId);
    }

    protected void drawAfterLayer(WidgetInfo layer) {
        this.drawHooks.add(layer.getId());
    }

    public void onMouseOver() {
    }

    public boolean onDrag(Overlay other) {
        return false;
    }

    @Nullable
    public Rectangle getParentBounds() {
        return null;
    }

    public void revalidate() {
    }

    public void setPosition(OverlayPosition position) {
        this.position = position;
        switch (position) {
            case TOOLTIP: 
            case DYNAMIC: {
                this.movable = false;
                this.snappable = false;
                break;
            }
            case DETACHED: {
                this.movable = true;
                this.snappable = false;
                break;
            }
            default: {
                this.movable = true;
                this.snappable = true;
            }
        }
    }

    @Nullable
    public Plugin getPlugin() {
        return this.plugin;
    }

    public Point getPreferredLocation() {
        return this.preferredLocation;
    }

    public Dimension getPreferredSize() {
        return this.preferredSize;
    }

    public OverlayPosition getPreferredPosition() {
        return this.preferredPosition;
    }

    @Override
    public Rectangle getBounds() {
        return this.bounds;
    }

    public OverlayPosition getPosition() {
        return this.position;
    }

    public OverlayPriority getPriority() {
        return this.priority;
    }

    public OverlayLayer getLayer() {
        return this.layer;
    }

    public List<Integer> getDrawHooks() {
        return this.drawHooks;
    }

    public List<OverlayMenuEntry> getMenuEntries() {
        return this.menuEntries;
    }

    public boolean isResizable() {
        return this.resizable;
    }

    public int getMinimumSize() {
        return this.minimumSize;
    }

    public boolean isResettable() {
        return this.resettable;
    }

    public boolean isDragTargetable() {
        return this.dragTargetable;
    }

    public boolean isMovable() {
        return this.movable;
    }

    public boolean isSnappable() {
        return this.snappable;
    }

    @Override
    public void setPreferredLocation(Point preferredLocation) {
        this.preferredLocation = preferredLocation;
    }

    @Override
    public void setPreferredSize(Dimension preferredSize) {
        this.preferredSize = preferredSize;
    }

    public void setPreferredPosition(OverlayPosition preferredPosition) {
        this.preferredPosition = preferredPosition;
    }

    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }

    public void setPriority(OverlayPriority priority) {
        this.priority = priority;
    }

    public void setLayer(OverlayLayer layer) {
        this.layer = layer;
    }

    public void setResizable(boolean resizable) {
        this.resizable = resizable;
    }

    public void setMinimumSize(int minimumSize) {
        this.minimumSize = minimumSize;
    }

    public void setResettable(boolean resettable) {
        this.resettable = resettable;
    }

    protected void setDragTargetable(boolean dragTargetable) {
        this.dragTargetable = dragTargetable;
    }

    protected void setMovable(boolean movable) {
        this.movable = movable;
    }

    protected void setSnappable(boolean snappable) {
        this.snappable = snappable;
    }
}

