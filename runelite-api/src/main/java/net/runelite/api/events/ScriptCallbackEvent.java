/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api.events;

import net.runelite.api.Script;

public class ScriptCallbackEvent {
    private Script script;
    private String eventName;

    public Script getScript() {
        return this.script;
    }

    public String getEventName() {
        return this.eventName;
    }

    public void setScript(Script script) {
        this.script = script;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ScriptCallbackEvent)) {
            return false;
        }
        ScriptCallbackEvent other = (ScriptCallbackEvent)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Script this$script = this.getScript();
        Script other$script = other.getScript();
        if (this$script == null ? other$script != null : !this$script.equals(other$script)) {
            return false;
        }
        String this$eventName = this.getEventName();
        String other$eventName = other.getEventName();
        return !(this$eventName == null ? other$eventName != null : !this$eventName.equals(other$eventName));
    }

    protected boolean canEqual(Object other) {
        return other instanceof ScriptCallbackEvent;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Script $script = this.getScript();
        result = result * 59 + ($script == null ? 43 : $script.hashCode());
        String $eventName = this.getEventName();
        result = result * 59 + ($eventName == null ? 43 : $eventName.hashCode());
        return result;
    }

    public String toString() {
        return "ScriptCallbackEvent(script=" + this.getScript() + ", eventName=" + this.getEventName() + ")";
    }
}

