/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api.events;

public final class VolumeChanged {
    private final Type type;

    public VolumeChanged(Type type) {
        this.type = type;
    }

    public Type getType() {
        return this.type;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof VolumeChanged)) {
            return false;
        }
        VolumeChanged other = (VolumeChanged)o;
        Type this$type = this.getType();
        Type other$type = other.getType();
        return !(this$type == null ? other$type != null : !((Object)((Object)this$type)).equals((Object)other$type));
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Type $type = this.getType();
        result = result * 59 + ($type == null ? 43 : ((Object)((Object)$type)).hashCode());
        return result;
    }

    public String toString() {
        return "VolumeChanged(type=" + (Object)((Object)this.getType()) + ")";
    }

    public static enum Type {
        MUSIC,
        EFFECTS,
        AREA;

    }
}

