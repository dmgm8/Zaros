/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api.events;

public class VarbitChanged {
    private int varpId = -1;
    private int varbitId = -1;
    private int value;

    @Deprecated
    public int getIndex() {
        return this.varpId;
    }

    public int getVarpId() {
        return this.varpId;
    }

    public int getVarbitId() {
        return this.varbitId;
    }

    public int getValue() {
        return this.value;
    }

    public void setVarpId(int varpId) {
        this.varpId = varpId;
    }

    public void setVarbitId(int varbitId) {
        this.varbitId = varbitId;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof VarbitChanged)) {
            return false;
        }
        VarbitChanged other = (VarbitChanged)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getVarpId() != other.getVarpId()) {
            return false;
        }
        if (this.getVarbitId() != other.getVarbitId()) {
            return false;
        }
        return this.getValue() == other.getValue();
    }

    protected boolean canEqual(Object other) {
        return other instanceof VarbitChanged;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getVarpId();
        result = result * 59 + this.getVarbitId();
        result = result * 59 + this.getValue();
        return result;
    }

    public String toString() {
        return "VarbitChanged(varpId=" + this.getVarpId() + ", varbitId=" + this.getVarbitId() + ", value=" + this.getValue() + ")";
    }
}

