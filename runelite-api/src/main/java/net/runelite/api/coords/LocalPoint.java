/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package net.runelite.api.coords;

import javax.annotation.Nullable;
import net.runelite.api.Client;
import net.runelite.api.coords.WorldPoint;

public final class LocalPoint {
    private final int x;
    private final int y;

    @Nullable
    public static LocalPoint fromWorld(Client client, WorldPoint world) {
        if (client.getPlane() != world.getPlane()) {
            return null;
        }
        return LocalPoint.fromWorld(client, world.getX(), world.getY());
    }

    public static LocalPoint fromWorld(Client client, int x, int y) {
        if (!WorldPoint.isInScene(client, x, y)) {
            return null;
        }
        int baseX = client.getBaseX();
        int baseY = client.getBaseY();
        return LocalPoint.fromScene(x - baseX, y - baseY);
    }

    public int distanceTo(LocalPoint other) {
        return (int)Math.hypot(this.getX() - other.getX(), this.getY() - other.getY());
    }

    public boolean isInScene() {
        return this.x >= 0 && this.x < 13312 && this.y >= 0 && this.y < 13312;
    }

    public static LocalPoint fromScene(int x, int y) {
        return new LocalPoint((x << 7) + 64, (y << 7) + 64);
    }

    public int getSceneX() {
        return this.x >> 7;
    }

    public int getSceneY() {
        return this.y >> 7;
    }

    public LocalPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof LocalPoint)) {
            return false;
        }
        LocalPoint other = (LocalPoint)o;
        if (this.getX() != other.getX()) {
            return false;
        }
        return this.getY() == other.getY();
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getX();
        result = result * 59 + this.getY();
        return result;
    }

    public String toString() {
        return "LocalPoint(x=" + this.getX() + ", y=" + this.getY() + ")";
    }
}

