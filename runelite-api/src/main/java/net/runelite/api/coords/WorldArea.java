/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api.coords;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import net.runelite.api.Client;
import net.runelite.api.CollisionData;
import net.runelite.api.Point;
import net.runelite.api.Tile;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;

public class WorldArea {
    private int x;
    private int y;
    private int width;
    private int height;
    private int plane;

    public WorldArea(int x, int y, int width, int height, int plane) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.plane = plane;
    }

    public WorldArea(WorldPoint location, int width, int height) {
        this.x = location.getX();
        this.y = location.getY();
        this.plane = location.getPlane();
        this.width = width;
        this.height = height;
    }

    private Point getAxisDistances(WorldArea other) {
        Point p1 = this.getComparisonPoint(other);
        Point p2 = other.getComparisonPoint(this);
        return new Point(Math.abs(p1.getX() - p2.getX()), Math.abs(p1.getY() - p2.getY()));
    }

    public int distanceTo(WorldArea other) {
        if (this.getPlane() != other.getPlane()) {
            return Integer.MAX_VALUE;
        }
        return this.distanceTo2D(other);
    }

    public int distanceTo(WorldPoint other) {
        return this.distanceTo(new WorldArea(other, 1, 1));
    }

    public int distanceTo2D(WorldArea other) {
        Point distances = this.getAxisDistances(other);
        return Math.max(distances.getX(), distances.getY());
    }

    public int distanceTo2D(WorldPoint other) {
        return this.distanceTo2D(new WorldArea(other, 1, 1));
    }

    public boolean contains(WorldPoint worldPoint) {
        return this.distanceTo(worldPoint) == 0;
    }

    public boolean contains2D(WorldPoint worldPoint) {
        return this.distanceTo2D(worldPoint) == 0;
    }

    public boolean isInMeleeDistance(WorldArea other) {
        if (other == null || this.getPlane() != other.getPlane()) {
            return false;
        }
        Point distances = this.getAxisDistances(other);
        return distances.getX() + distances.getY() == 1;
    }

    public boolean isInMeleeDistance(WorldPoint other) {
        return this.isInMeleeDistance(new WorldArea(other, 1, 1));
    }

    public boolean intersectsWith(WorldArea other) {
        if (this.getPlane() != other.getPlane()) {
            return false;
        }
        Point distances = this.getAxisDistances(other);
        return distances.getX() + distances.getY() == 0;
    }

    public boolean canTravelInDirection(Client client, int dx, int dy) {
        return this.canTravelInDirection(client, dx, dy, x -> true);
    }

    public boolean canTravelInDirection(Client client, int dx, int dy, Predicate<? super WorldPoint> extraCondition) {
        CollisionData[] collisionData;
        dx = Integer.signum(dx);
        dy = Integer.signum(dy);
        if (dx == 0 && dy == 0) {
            return true;
        }
        LocalPoint lp = LocalPoint.fromWorld(client, this.x, this.y);
        int startX = lp.getSceneX() + dx;
        int startY = lp.getSceneY() + dy;
        int checkX = startX + (dx > 0 ? this.width - 1 : 0);
        int checkY = startY + (dy > 0 ? this.height - 1 : 0);
        int endX = startX + this.width - 1;
        int endY = startY + this.height - 1;
        int xFlags = 2359552;
        int yFlags = 2359552;
        int xyFlags = 2359552;
        int xWallFlagsSouth = 2359552;
        int xWallFlagsNorth = 2359552;
        int yWallFlagsWest = 2359552;
        int yWallFlagsEast = 2359552;
        if (dx < 0) {
            xFlags |= 8;
            xWallFlagsSouth |= 0x30;
            xWallFlagsNorth |= 6;
        }
        if (dx > 0) {
            xFlags |= 0x80;
            xWallFlagsSouth |= 0x60;
            xWallFlagsNorth |= 3;
        }
        if (dy < 0) {
            yFlags |= 2;
            yWallFlagsWest |= 0x81;
            yWallFlagsEast |= 0xC;
        }
        if (dy > 0) {
            yFlags |= 0x20;
            yWallFlagsWest |= 0xC0;
            yWallFlagsEast |= 0x18;
        }
        if (dx < 0 && dy < 0) {
            xyFlags |= 4;
        }
        if (dx < 0 && dy > 0) {
            xyFlags |= 0x10;
        }
        if (dx > 0 && dy < 0) {
            xyFlags |= 1;
        }
        if (dx > 0 && dy > 0) {
            xyFlags |= 0x40;
        }
        if ((collisionData = client.getCollisionMaps()) == null) {
            return false;
        }
        int[][] collisionDataFlags = collisionData[this.plane].getFlags();
        if (dx != 0) {
            int y;
            for (y = startY; y <= endY; ++y) {
                if ((collisionDataFlags[checkX][y] & xFlags) == 0 && extraCondition.test(WorldPoint.fromScene(client, checkX, y, this.plane))) continue;
                return false;
            }
            for (y = startY + 1; y <= endY; ++y) {
                if ((collisionDataFlags[checkX][y] & xWallFlagsSouth) == 0) continue;
                return false;
            }
            for (y = endY - 1; y >= startY; --y) {
                if ((collisionDataFlags[checkX][y] & xWallFlagsNorth) == 0) continue;
                return false;
            }
        }
        if (dy != 0) {
            int x;
            for (x = startX; x <= endX; ++x) {
                if ((collisionDataFlags[x][checkY] & yFlags) == 0 && extraCondition.test(WorldPoint.fromScene(client, x, checkY, client.getPlane()))) continue;
                return false;
            }
            for (x = startX + 1; x <= endX; ++x) {
                if ((collisionDataFlags[x][checkY] & yWallFlagsWest) == 0) continue;
                return false;
            }
            for (x = endX - 1; x >= startX; --x) {
                if ((collisionDataFlags[x][checkY] & yWallFlagsEast) == 0) continue;
                return false;
            }
        }
        if (dx != 0 && dy != 0) {
            if ((collisionDataFlags[checkX][checkY] & xyFlags) != 0 || !extraCondition.test(WorldPoint.fromScene(client, checkX, checkY, client.getPlane()))) {
                return false;
            }
            if (this.width == 1 && (collisionDataFlags[checkX][checkY - dy] & xFlags) != 0 && extraCondition.test(WorldPoint.fromScene(client, checkX, startY, client.getPlane()))) {
                return false;
            }
            if (this.height == 1 && (collisionDataFlags[checkX - dx][checkY] & yFlags) != 0 && extraCondition.test(WorldPoint.fromScene(client, startX, checkY, client.getPlane()))) {
                return false;
            }
        }
        return true;
    }

    private Point getComparisonPoint(WorldArea other) {
        int x = other.x <= this.x ? this.x : (other.x >= this.x + this.width - 1 ? this.x + this.width - 1 : other.x);
        int y = other.y <= this.y ? this.y : (other.y >= this.y + this.height - 1 ? this.y + this.height - 1 : other.y);
        return new Point(x, y);
    }

    public WorldArea calculateNextTravellingPoint(Client client, WorldArea target, boolean stopAtMeleeDistance) {
        return this.calculateNextTravellingPoint(client, target, stopAtMeleeDistance, x -> true);
    }

    public WorldArea calculateNextTravellingPoint(Client client, WorldArea target, boolean stopAtMeleeDistance, Predicate<? super WorldPoint> extraCondition) {
        if (this.plane != target.getPlane()) {
            return null;
        }
        if (this.intersectsWith(target)) {
            if (stopAtMeleeDistance) {
                return null;
            }
            return this;
        }
        int dx = target.x - this.x;
        int dy = target.y - this.y;
        Point axisDistances = this.getAxisDistances(target);
        if (stopAtMeleeDistance && axisDistances.getX() + axisDistances.getY() == 1) {
            return this;
        }
        LocalPoint lp = LocalPoint.fromWorld(client, this.x, this.y);
        if (lp == null || lp.getSceneX() + dx < 0 || lp.getSceneX() + dy >= 104 || lp.getSceneY() + dx < 0 || lp.getSceneY() + dy >= 104) {
            return null;
        }
        int dxSig = Integer.signum(dx);
        int dySig = Integer.signum(dy);
        if (stopAtMeleeDistance && axisDistances.getX() == 1 && axisDistances.getY() == 1) {
            if (this.canTravelInDirection(client, dxSig, 0, extraCondition)) {
                return new WorldArea(this.x + dxSig, this.y, this.width, this.height, this.plane);
            }
        } else {
            if (this.canTravelInDirection(client, dxSig, dySig, extraCondition)) {
                return new WorldArea(this.x + dxSig, this.y + dySig, this.width, this.height, this.plane);
            }
            if (dx != 0 && this.canTravelInDirection(client, dxSig, 0, extraCondition)) {
                return new WorldArea(this.x + dxSig, this.y, this.width, this.height, this.plane);
            }
            if (dy != 0 && Math.max(Math.abs(dx), Math.abs(dy)) > 1 && this.canTravelInDirection(client, 0, dy, extraCondition)) {
                return new WorldArea(this.x, this.y + dySig, this.width, this.height, this.plane);
            }
        }
        return this;
    }

    public boolean hasLineOfSightTo(Client client, WorldArea other) {
        if (this.plane != other.getPlane()) {
            return false;
        }
        LocalPoint sourceLp = LocalPoint.fromWorld(client, this.x, this.y);
        LocalPoint targetLp = LocalPoint.fromWorld(client, other.getX(), other.getY());
        if (sourceLp == null || targetLp == null) {
            return false;
        }
        int thisX = sourceLp.getSceneX();
        int thisY = sourceLp.getSceneY();
        int otherX = targetLp.getSceneX();
        int otherY = targetLp.getSceneY();
        int cmpThisX = otherX <= thisX ? thisX : (otherX >= thisX + this.width - 1 ? thisX + this.width - 1 : otherX);
        int cmpThisY = otherY <= thisY ? thisY : (otherY >= thisY + this.height - 1 ? thisY + this.height - 1 : otherY);
        int cmpOtherX = thisX <= otherX ? otherX : (thisX >= otherX + other.getWidth() - 1 ? otherX + other.getWidth() - 1 : thisX);
        int cmpOtherY = thisY <= otherY ? otherY : (thisY >= otherY + other.getHeight() - 1 ? otherY + other.getHeight() - 1 : thisY);
        Tile[][][] tiles = client.getScene().getTiles();
        Tile sourceTile = tiles[this.plane][cmpThisX][cmpThisY];
        Tile targetTile = tiles[other.getPlane()][cmpOtherX][cmpOtherY];
        if (sourceTile == null || targetTile == null) {
            return false;
        }
        return sourceTile.hasLineOfSightTo(targetTile);
    }

    public boolean hasLineOfSightTo(Client client, WorldPoint other) {
        return this.hasLineOfSightTo(client, new WorldArea(other, 1, 1));
    }

    public WorldPoint toWorldPoint() {
        return new WorldPoint(this.x, this.y, this.plane);
    }

    public List<WorldPoint> toWorldPointList() {
        ArrayList<WorldPoint> list = new ArrayList<WorldPoint>(this.width * this.height);
        for (int x = 0; x < this.width; ++x) {
            for (int y = 0; y < this.height; ++y) {
                list.add(new WorldPoint(this.getX() + x, this.getY() + y, this.getPlane()));
            }
        }
        return list;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getPlane() {
        return this.plane;
    }
}

