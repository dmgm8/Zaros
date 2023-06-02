/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api.events;

import net.runelite.api.ScriptEvent;

public class ScriptPreFired {
    private final int scriptId;
    private ScriptEvent scriptEvent;

    public ScriptPreFired(int scriptId) {
        this.scriptId = scriptId;
    }

    public int getScriptId() {
        return this.scriptId;
    }

    public ScriptEvent getScriptEvent() {
        return this.scriptEvent;
    }

    public void setScriptEvent(ScriptEvent scriptEvent) {
        this.scriptEvent = scriptEvent;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ScriptPreFired)) {
            return false;
        }
        ScriptPreFired other = (ScriptPreFired)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getScriptId() != other.getScriptId()) {
            return false;
        }
        ScriptEvent this$scriptEvent = this.getScriptEvent();
        ScriptEvent other$scriptEvent = other.getScriptEvent();
        return !(this$scriptEvent == null ? other$scriptEvent != null : !this$scriptEvent.equals(other$scriptEvent));
    }

    protected boolean canEqual(Object other) {
        return other instanceof ScriptPreFired;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getScriptId();
        ScriptEvent $scriptEvent = this.getScriptEvent();
        result = result * 59 + ($scriptEvent == null ? 43 : $scriptEvent.hashCode());
        return result;
    }

    public String toString() {
        return "ScriptPreFired(scriptId=" + this.getScriptId() + ", scriptEvent=" + this.getScriptEvent() + ")";
    }
}

