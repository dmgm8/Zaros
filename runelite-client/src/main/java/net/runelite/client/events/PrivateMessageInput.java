/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.events;

import net.runelite.client.events.ChatInput;

public abstract class PrivateMessageInput
extends ChatInput {
    private final String target;
    private final String message;

    public PrivateMessageInput(String target, String message) {
        this.target = target;
        this.message = message;
    }

    public String getTarget() {
        return this.target;
    }

    public String getMessage() {
        return this.message;
    }

    public String toString() {
        return "PrivateMessageInput(target=" + this.getTarget() + ", message=" + this.getMessage() + ")";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PrivateMessageInput)) {
            return false;
        }
        PrivateMessageInput other = (PrivateMessageInput)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        String this$target = this.getTarget();
        String other$target = other.getTarget();
        if (this$target == null ? other$target != null : !this$target.equals(other$target)) {
            return false;
        }
        String this$message = this.getMessage();
        String other$message = other.getMessage();
        return !(this$message == null ? other$message != null : !this$message.equals(other$message));
    }

    protected boolean canEqual(Object other) {
        return other instanceof PrivateMessageInput;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = super.hashCode();
        String $target = this.getTarget();
        result = result * 59 + ($target == null ? 43 : $target.hashCode());
        String $message = this.getMessage();
        result = result * 59 + ($message == null ? 43 : $message.hashCode());
        return result;
    }
}

