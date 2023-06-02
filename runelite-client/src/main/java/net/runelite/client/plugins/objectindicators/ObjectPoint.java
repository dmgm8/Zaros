/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.objectindicators;

import java.awt.Color;

class ObjectPoint {
    private int id = -1;
    private String name;
    private int regionId;
    private int regionX;
    private int regionY;
    private int z;
    private Color color;

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int getRegionId() {
        return this.regionId;
    }

    public int getRegionX() {
        return this.regionX;
    }

    public int getRegionY() {
        return this.regionY;
    }

    public int getZ() {
        return this.z;
    }

    public Color getColor() {
        return this.color;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRegionId(int regionId) {
        this.regionId = regionId;
    }

    public void setRegionX(int regionX) {
        this.regionX = regionX;
    }

    public void setRegionY(int regionY) {
        this.regionY = regionY;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ObjectPoint)) {
            return false;
        }
        ObjectPoint other = (ObjectPoint)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getId() != other.getId()) {
            return false;
        }
        if (this.getRegionId() != other.getRegionId()) {
            return false;
        }
        if (this.getRegionX() != other.getRegionX()) {
            return false;
        }
        if (this.getRegionY() != other.getRegionY()) {
            return false;
        }
        if (this.getZ() != other.getZ()) {
            return false;
        }
        String this$name = this.getName();
        String other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) {
            return false;
        }
        Color this$color = this.getColor();
        Color other$color = other.getColor();
        return !(this$color == null ? other$color != null : !((Object)this$color).equals(other$color));
    }

    protected boolean canEqual(Object other) {
        return other instanceof ObjectPoint;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getId();
        result = result * 59 + this.getRegionId();
        result = result * 59 + this.getRegionX();
        result = result * 59 + this.getRegionY();
        result = result * 59 + this.getZ();
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        Color $color = this.getColor();
        result = result * 59 + ($color == null ? 43 : ((Object)$color).hashCode());
        return result;
    }

    public String toString() {
        return "ObjectPoint(id=" + this.getId() + ", name=" + this.getName() + ", regionId=" + this.getRegionId() + ", regionX=" + this.getRegionX() + ", regionY=" + this.getRegionY() + ", z=" + this.getZ() + ", color=" + this.getColor() + ")";
    }

    public ObjectPoint() {
    }

    public ObjectPoint(int id, String name, int regionId, int regionX, int regionY, int z, Color color) {
        this.id = id;
        this.name = name;
        this.regionId = regionId;
        this.regionX = regionX;
        this.regionY = regionY;
        this.z = z;
        this.color = color;
    }
}

