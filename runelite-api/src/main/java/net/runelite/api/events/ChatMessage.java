/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api.events;

import net.runelite.api.ChatMessageType;
import net.runelite.api.MessageNode;

public class ChatMessage {
    private MessageNode messageNode;
    private ChatMessageType type;
    private String name;
    private String message;
    private String sender;
    private int timestamp;

    public MessageNode getMessageNode() {
        return this.messageNode;
    }

    public ChatMessageType getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }

    public String getMessage() {
        return this.message;
    }

    public String getSender() {
        return this.sender;
    }

    public int getTimestamp() {
        return this.timestamp;
    }

    public void setMessageNode(MessageNode messageNode) {
        this.messageNode = messageNode;
    }

    public void setType(ChatMessageType type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ChatMessage)) {
            return false;
        }
        ChatMessage other = (ChatMessage)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getTimestamp() != other.getTimestamp()) {
            return false;
        }
        MessageNode this$messageNode = this.getMessageNode();
        MessageNode other$messageNode = other.getMessageNode();
        if (this$messageNode == null ? other$messageNode != null : !this$messageNode.equals(other$messageNode)) {
            return false;
        }
        ChatMessageType this$type = this.getType();
        ChatMessageType other$type = other.getType();
        if (this$type == null ? other$type != null : !((Object)((Object)this$type)).equals((Object)other$type)) {
            return false;
        }
        String this$name = this.getName();
        String other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) {
            return false;
        }
        String this$message = this.getMessage();
        String other$message = other.getMessage();
        if (this$message == null ? other$message != null : !this$message.equals(other$message)) {
            return false;
        }
        String this$sender = this.getSender();
        String other$sender = other.getSender();
        return !(this$sender == null ? other$sender != null : !this$sender.equals(other$sender));
    }

    protected boolean canEqual(Object other) {
        return other instanceof ChatMessage;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getTimestamp();
        MessageNode $messageNode = this.getMessageNode();
        result = result * 59 + ($messageNode == null ? 43 : $messageNode.hashCode());
        ChatMessageType $type = this.getType();
        result = result * 59 + ($type == null ? 43 : ((Object)((Object)$type)).hashCode());
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        String $message = this.getMessage();
        result = result * 59 + ($message == null ? 43 : $message.hashCode());
        String $sender = this.getSender();
        result = result * 59 + ($sender == null ? 43 : $sender.hashCode());
        return result;
    }

    public String toString() {
        return "ChatMessage(messageNode=" + this.getMessageNode() + ", type=" + (Object)((Object)this.getType()) + ", name=" + this.getName() + ", message=" + this.getMessage() + ", sender=" + this.getSender() + ", timestamp=" + this.getTimestamp() + ")";
    }

    public ChatMessage(MessageNode messageNode, ChatMessageType type, String name, String message, String sender, int timestamp) {
        this.messageNode = messageNode;
        this.type = type;
        this.name = name;
        this.message = message;
        this.sender = sender;
        this.timestamp = timestamp;
    }

    public ChatMessage() {
    }
}

