/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.events;

import net.runelite.client.events.ChatInput;

public abstract class ChatboxInput
extends ChatInput {
    private final String value;
    private final int chatType;

    public ChatboxInput(String value, int chatType) {
        this.value = value;
        this.chatType = chatType;
    }

    public String getValue() {
        return this.value;
    }

    public int getChatType() {
        return this.chatType;
    }

    public String toString() {
        return "ChatboxInput(value=" + this.getValue() + ", chatType=" + this.getChatType() + ")";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ChatboxInput)) {
            return false;
        }
        ChatboxInput other = (ChatboxInput)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        if (this.getChatType() != other.getChatType()) {
            return false;
        }
        String this$value = this.getValue();
        String other$value = other.getValue();
        return !(this$value == null ? other$value != null : !this$value.equals(other$value));
    }

    protected boolean canEqual(Object other) {
        return other instanceof ChatboxInput;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = super.hashCode();
        result = result * 59 + this.getChatType();
        String $value = this.getValue();
        result = result * 59 + ($value == null ? 43 : $value.hashCode());
        return result;
    }
}

