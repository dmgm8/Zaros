/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api.events;

public final class ScriptPostFired {
    private final int scriptId;

    public ScriptPostFired(int scriptId) {
        this.scriptId = scriptId;
    }

    public int getScriptId() {
        return this.scriptId;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ScriptPostFired)) {
            return false;
        }
        ScriptPostFired other = (ScriptPostFired)o;
        return this.getScriptId() == other.getScriptId();
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getScriptId();
        return result;
    }

    public String toString() {
        return "ScriptPostFired(scriptId=" + this.getScriptId() + ")";
    }
}

