/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.http.api.feed;

import net.runelite.http.api.feed.FeedItemType;

public class FeedItem {
    private final FeedItemType type;
    private String avatar;
    private final String title;
    private final String content;
    private final String url;
    private final long timestamp;

    public FeedItemType getType() {
        return this.type;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public String getTitle() {
        return this.title;
    }

    public String getContent() {
        return this.content;
    }

    public String getUrl() {
        return this.url;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof FeedItem)) {
            return false;
        }
        FeedItem other = (FeedItem)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getTimestamp() != other.getTimestamp()) {
            return false;
        }
        FeedItemType this$type = this.getType();
        FeedItemType other$type = other.getType();
        if (this$type == null ? other$type != null : !((Object)((Object)this$type)).equals((Object)other$type)) {
            return false;
        }
        String this$avatar = this.getAvatar();
        String other$avatar = other.getAvatar();
        if (this$avatar == null ? other$avatar != null : !this$avatar.equals(other$avatar)) {
            return false;
        }
        String this$title = this.getTitle();
        String other$title = other.getTitle();
        if (this$title == null ? other$title != null : !this$title.equals(other$title)) {
            return false;
        }
        String this$content = this.getContent();
        String other$content = other.getContent();
        if (this$content == null ? other$content != null : !this$content.equals(other$content)) {
            return false;
        }
        String this$url = this.getUrl();
        String other$url = other.getUrl();
        return !(this$url == null ? other$url != null : !this$url.equals(other$url));
    }

    protected boolean canEqual(Object other) {
        return other instanceof FeedItem;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        long $timestamp = this.getTimestamp();
        result = result * 59 + (int)($timestamp >>> 32 ^ $timestamp);
        FeedItemType $type = this.getType();
        result = result * 59 + ($type == null ? 43 : ((Object)((Object)$type)).hashCode());
        String $avatar = this.getAvatar();
        result = result * 59 + ($avatar == null ? 43 : $avatar.hashCode());
        String $title = this.getTitle();
        result = result * 59 + ($title == null ? 43 : $title.hashCode());
        String $content = this.getContent();
        result = result * 59 + ($content == null ? 43 : $content.hashCode());
        String $url = this.getUrl();
        result = result * 59 + ($url == null ? 43 : $url.hashCode());
        return result;
    }

    public String toString() {
        return "FeedItem(type=" + (Object)((Object)this.getType()) + ", avatar=" + this.getAvatar() + ", title=" + this.getTitle() + ", content=" + this.getContent() + ", url=" + this.getUrl() + ", timestamp=" + this.getTimestamp() + ")";
    }

    public FeedItem(FeedItemType type, String title, String content, String url, long timestamp) {
        this.type = type;
        this.title = title;
        this.content = content;
        this.url = url;
        this.timestamp = timestamp;
    }

    public FeedItem(FeedItemType type, String avatar, String title, String content, String url, long timestamp) {
        this.type = type;
        this.avatar = avatar;
        this.title = title;
        this.content = content;
        this.url = url;
        this.timestamp = timestamp;
    }
}

