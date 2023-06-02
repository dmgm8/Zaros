/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.runelite.api.Point
 *  net.runelite.api.coords.WorldPoint
 */
package net.runelite.client.ui.overlay.worldmap;

import java.awt.image.BufferedImage;
import javax.annotation.Nullable;
import net.runelite.api.Point;
import net.runelite.api.coords.WorldPoint;

public class WorldMapPoint {
    private BufferedImage image;
    private WorldPoint worldPoint;
    @Nullable
    private WorldPoint target;
    private Point imagePoint;
    private boolean snapToEdge;
    private boolean currentlyEdgeSnapped;
    private boolean jumpOnClick;
    private String name;
    private String tooltip;

    public WorldMapPoint(WorldPoint worldPoint, BufferedImage image) {
        this.worldPoint = worldPoint;
        this.image = image;
    }

    public void onEdgeSnap() {
    }

    public void onEdgeUnsnap() {
    }

    protected WorldMapPoint(WorldMapPointBuilder<?, ?> b) {
        this.image = ((WorldMapPointBuilder)b).image;
        this.worldPoint = ((WorldMapPointBuilder)b).worldPoint;
        this.target = ((WorldMapPointBuilder)b).target;
        this.imagePoint = ((WorldMapPointBuilder)b).imagePoint;
        this.snapToEdge = ((WorldMapPointBuilder)b).snapToEdge;
        this.currentlyEdgeSnapped = ((WorldMapPointBuilder)b).currentlyEdgeSnapped;
        this.jumpOnClick = ((WorldMapPointBuilder)b).jumpOnClick;
        this.name = ((WorldMapPointBuilder)b).name;
        this.tooltip = ((WorldMapPointBuilder)b).tooltip;
    }

    public static WorldMapPointBuilder<?, ?> builder() {
        return new WorldMapPointBuilderImpl();
    }

    public BufferedImage getImage() {
        return this.image;
    }

    public WorldPoint getWorldPoint() {
        return this.worldPoint;
    }

    @Nullable
    public WorldPoint getTarget() {
        return this.target;
    }

    public Point getImagePoint() {
        return this.imagePoint;
    }

    public boolean isSnapToEdge() {
        return this.snapToEdge;
    }

    public boolean isCurrentlyEdgeSnapped() {
        return this.currentlyEdgeSnapped;
    }

    public boolean isJumpOnClick() {
        return this.jumpOnClick;
    }

    public String getName() {
        return this.name;
    }

    public String getTooltip() {
        return this.tooltip;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public void setWorldPoint(WorldPoint worldPoint) {
        this.worldPoint = worldPoint;
    }

    public void setTarget(@Nullable WorldPoint target) {
        this.target = target;
    }

    public void setImagePoint(Point imagePoint) {
        this.imagePoint = imagePoint;
    }

    public void setSnapToEdge(boolean snapToEdge) {
        this.snapToEdge = snapToEdge;
    }

    public void setCurrentlyEdgeSnapped(boolean currentlyEdgeSnapped) {
        this.currentlyEdgeSnapped = currentlyEdgeSnapped;
    }

    public void setJumpOnClick(boolean jumpOnClick) {
        this.jumpOnClick = jumpOnClick;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof WorldMapPoint)) {
            return false;
        }
        WorldMapPoint other = (WorldMapPoint)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.isSnapToEdge() != other.isSnapToEdge()) {
            return false;
        }
        if (this.isCurrentlyEdgeSnapped() != other.isCurrentlyEdgeSnapped()) {
            return false;
        }
        if (this.isJumpOnClick() != other.isJumpOnClick()) {
            return false;
        }
        BufferedImage this$image = this.getImage();
        BufferedImage other$image = other.getImage();
        if (this$image == null ? other$image != null : !this$image.equals(other$image)) {
            return false;
        }
        WorldPoint this$worldPoint = this.getWorldPoint();
        WorldPoint other$worldPoint = other.getWorldPoint();
        if (this$worldPoint == null ? other$worldPoint != null : !this$worldPoint.equals((Object)other$worldPoint)) {
            return false;
        }
        WorldPoint this$target = this.getTarget();
        WorldPoint other$target = other.getTarget();
        if (this$target == null ? other$target != null : !this$target.equals((Object)other$target)) {
            return false;
        }
        Point this$imagePoint = this.getImagePoint();
        Point other$imagePoint = other.getImagePoint();
        if (this$imagePoint == null ? other$imagePoint != null : !this$imagePoint.equals((Object)other$imagePoint)) {
            return false;
        }
        String this$name = this.getName();
        String other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) {
            return false;
        }
        String this$tooltip = this.getTooltip();
        String other$tooltip = other.getTooltip();
        return !(this$tooltip == null ? other$tooltip != null : !this$tooltip.equals(other$tooltip));
    }

    protected boolean canEqual(Object other) {
        return other instanceof WorldMapPoint;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + (this.isSnapToEdge() ? 79 : 97);
        result = result * 59 + (this.isCurrentlyEdgeSnapped() ? 79 : 97);
        result = result * 59 + (this.isJumpOnClick() ? 79 : 97);
        BufferedImage $image = this.getImage();
        result = result * 59 + ($image == null ? 43 : $image.hashCode());
        WorldPoint $worldPoint = this.getWorldPoint();
        result = result * 59 + ($worldPoint == null ? 43 : $worldPoint.hashCode());
        WorldPoint $target = this.getTarget();
        result = result * 59 + ($target == null ? 43 : $target.hashCode());
        Point $imagePoint = this.getImagePoint();
        result = result * 59 + ($imagePoint == null ? 43 : $imagePoint.hashCode());
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        String $tooltip = this.getTooltip();
        result = result * 59 + ($tooltip == null ? 43 : $tooltip.hashCode());
        return result;
    }

    public String toString() {
        return "WorldMapPoint(image=" + this.getImage() + ", worldPoint=" + (Object)this.getWorldPoint() + ", target=" + (Object)this.getTarget() + ", imagePoint=" + (Object)this.getImagePoint() + ", snapToEdge=" + this.isSnapToEdge() + ", currentlyEdgeSnapped=" + this.isCurrentlyEdgeSnapped() + ", jumpOnClick=" + this.isJumpOnClick() + ", name=" + this.getName() + ", tooltip=" + this.getTooltip() + ")";
    }

    private static final class WorldMapPointBuilderImpl
    extends WorldMapPointBuilder<WorldMapPoint, WorldMapPointBuilderImpl> {
        private WorldMapPointBuilderImpl() {
        }

        @Override
        protected WorldMapPointBuilderImpl self() {
            return this;
        }

        @Override
        public WorldMapPoint build() {
            return new WorldMapPoint(this);
        }
    }

    public static abstract class WorldMapPointBuilder<C extends WorldMapPoint, B extends WorldMapPointBuilder<C, B>> {
        private BufferedImage image;
        private WorldPoint worldPoint;
        private WorldPoint target;
        private Point imagePoint;
        private boolean snapToEdge;
        private boolean currentlyEdgeSnapped;
        private boolean jumpOnClick;
        private String name;
        private String tooltip;

        protected abstract B self();

        public abstract C build();

        public B image(BufferedImage image) {
            this.image = image;
            return this.self();
        }

        public B worldPoint(WorldPoint worldPoint) {
            this.worldPoint = worldPoint;
            return this.self();
        }

        public B target(@Nullable WorldPoint target) {
            this.target = target;
            return this.self();
        }

        public B imagePoint(Point imagePoint) {
            this.imagePoint = imagePoint;
            return this.self();
        }

        public B snapToEdge(boolean snapToEdge) {
            this.snapToEdge = snapToEdge;
            return this.self();
        }

        public B currentlyEdgeSnapped(boolean currentlyEdgeSnapped) {
            this.currentlyEdgeSnapped = currentlyEdgeSnapped;
            return this.self();
        }

        public B jumpOnClick(boolean jumpOnClick) {
            this.jumpOnClick = jumpOnClick;
            return this.self();
        }

        public B name(String name) {
            this.name = name;
            return this.self();
        }

        public B tooltip(String tooltip) {
            this.tooltip = tooltip;
            return this.self();
        }

        public String toString() {
            return "WorldMapPoint.WorldMapPointBuilder(image=" + this.image + ", worldPoint=" + (Object)this.worldPoint + ", target=" + (Object)this.target + ", imagePoint=" + (Object)this.imagePoint + ", snapToEdge=" + this.snapToEdge + ", currentlyEdgeSnapped=" + this.currentlyEdgeSnapped + ", jumpOnClick=" + this.jumpOnClick + ", name=" + this.name + ", tooltip=" + this.tooltip + ")";
        }
    }
}

