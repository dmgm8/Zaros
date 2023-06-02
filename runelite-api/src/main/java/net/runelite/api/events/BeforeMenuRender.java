/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api.events;

public class BeforeMenuRender {
    private boolean consumed;

    public void consume() {
        this.consumed = true;
    }

    public boolean isConsumed() {
        return this.consumed;
    }

    public void setConsumed(boolean consumed) {
        this.consumed = consumed;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof BeforeMenuRender)) {
            return false;
        }
        BeforeMenuRender other = (BeforeMenuRender)o;
        if (!other.canEqual(this)) {
            return false;
        }
        return this.isConsumed() == other.isConsumed();
    }

    protected boolean canEqual(Object other) {
        return other instanceof BeforeMenuRender;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + (this.isConsumed() ? 79 : 97);
        return result;
    }

    public String toString() {
        return "BeforeMenuRender(consumed=" + this.isConsumed() + ")";
    }
}

