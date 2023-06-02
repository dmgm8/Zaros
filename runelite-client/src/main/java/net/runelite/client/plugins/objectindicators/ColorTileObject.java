/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.ObjectComposition
 *  net.runelite.api.TileObject
 */
package net.runelite.client.plugins.objectindicators;

import java.awt.Color;
import net.runelite.api.ObjectComposition;
import net.runelite.api.TileObject;

final class ColorTileObject {
    private final TileObject tileObject;
    private final ObjectComposition composition;
    private final String name;
    private final Color color;

    public TileObject getTileObject() {
        return this.tileObject;
    }

    public ObjectComposition getComposition() {
        return this.composition;
    }

    public String getName() {
        return this.name;
    }

    public Color getColor() {
        return this.color;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ColorTileObject)) {
            return false;
        }
        ColorTileObject other = (ColorTileObject)o;
        TileObject this$tileObject = this.getTileObject();
        TileObject other$tileObject = other.getTileObject();
        if (this$tileObject == null ? other$tileObject != null : !this$tileObject.equals((Object)other$tileObject)) {
            return false;
        }
        ObjectComposition this$composition = this.getComposition();
        ObjectComposition other$composition = other.getComposition();
        if (this$composition == null ? other$composition != null : !this$composition.equals((Object)other$composition)) {
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

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        TileObject $tileObject = this.getTileObject();
        result = result * 59 + ($tileObject == null ? 43 : $tileObject.hashCode());
        ObjectComposition $composition = this.getComposition();
        result = result * 59 + ($composition == null ? 43 : $composition.hashCode());
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        Color $color = this.getColor();
        result = result * 59 + ($color == null ? 43 : ((Object)$color).hashCode());
        return result;
    }

    public String toString() {
        return "ColorTileObject(tileObject=" + (Object)this.getTileObject() + ", composition=" + (Object)this.getComposition() + ", name=" + this.getName() + ", color=" + this.getColor() + ")";
    }

    public ColorTileObject(TileObject tileObject, ObjectComposition composition, String name, Color color) {
        this.tileObject = tileObject;
        this.composition = composition;
        this.name = name;
        this.color = color;
    }
}

