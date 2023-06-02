/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api.clan;

public final class ClanTitle {
    private final int id;
    private final String name;

    public ClanTitle(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ClanTitle)) {
            return false;
        }
        ClanTitle other = (ClanTitle)o;
        if (this.getId() != other.getId()) {
            return false;
        }
        String this$name = this.getName();
        String other$name = other.getName();
        return !(this$name == null ? other$name != null : !this$name.equals(other$name));
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getId();
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        return result;
    }

    public String toString() {
        return "ClanTitle(id=" + this.getId() + ", name=" + this.getName() + ")";
    }
}

