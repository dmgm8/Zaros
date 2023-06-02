/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.events;

import java.awt.TrayIcon;

public final class NotificationFired {
    private final String message;
    private final TrayIcon.MessageType type;

    public NotificationFired(String message, TrayIcon.MessageType type) {
        this.message = message;
        this.type = type;
    }

    public String getMessage() {
        return this.message;
    }

    public TrayIcon.MessageType getType() {
        return this.type;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof NotificationFired)) {
            return false;
        }
        NotificationFired other = (NotificationFired)o;
        String this$message = this.getMessage();
        String other$message = other.getMessage();
        if (this$message == null ? other$message != null : !this$message.equals(other$message)) {
            return false;
        }
        TrayIcon.MessageType this$type = this.getType();
        TrayIcon.MessageType other$type = other.getType();
        return !(this$type == null ? other$type != null : !((Object)((Object)this$type)).equals((Object)other$type));
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $message = this.getMessage();
        result = result * 59 + ($message == null ? 43 : $message.hashCode());
        TrayIcon.MessageType $type = this.getType();
        result = result * 59 + ($type == null ? 43 : ((Object)((Object)$type)).hashCode());
        return result;
    }

    public String toString() {
        return "NotificationFired(message=" + this.getMessage() + ", type=" + (Object)((Object)this.getType()) + ")";
    }
}

