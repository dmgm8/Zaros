/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.util;

import java.util.ArrayList;
import java.util.List;

public class WebhookBody {
    private String content;
    private List<Embed> embeds = new ArrayList<Embed>();

    public String getContent() {
        return this.content;
    }

    public List<Embed> getEmbeds() {
        return this.embeds;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setEmbeds(List<Embed> embeds) {
        this.embeds = embeds;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof WebhookBody)) {
            return false;
        }
        WebhookBody other = (WebhookBody)o;
        if (!other.canEqual(this)) {
            return false;
        }
        String this$content = this.getContent();
        String other$content = other.getContent();
        if (this$content == null ? other$content != null : !this$content.equals(other$content)) {
            return false;
        }
        List<Embed> this$embeds = this.getEmbeds();
        List<Embed> other$embeds = other.getEmbeds();
        return !(this$embeds == null ? other$embeds != null : !((Object)this$embeds).equals(other$embeds));
    }

    protected boolean canEqual(Object other) {
        return other instanceof WebhookBody;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $content = this.getContent();
        result = result * 59 + ($content == null ? 43 : $content.hashCode());
        List<Embed> $embeds = this.getEmbeds();
        result = result * 59 + ($embeds == null ? 43 : ((Object)$embeds).hashCode());
        return result;
    }

    public String toString() {
        return "WebhookBody(content=" + this.getContent() + ", embeds=" + this.getEmbeds() + ")";
    }

    static class UrlEmbed {
        final String url;

        public UrlEmbed(String url) {
            this.url = url;
        }

        public String getUrl() {
            return this.url;
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof UrlEmbed)) {
                return false;
            }
            UrlEmbed other = (UrlEmbed)o;
            if (!other.canEqual(this)) {
                return false;
            }
            String this$url = this.getUrl();
            String other$url = other.getUrl();
            return !(this$url == null ? other$url != null : !this$url.equals(other$url));
        }

        protected boolean canEqual(Object other) {
            return other instanceof UrlEmbed;
        }

        public int hashCode() {
            int PRIME = 59;
            int result = 1;
            String $url = this.getUrl();
            result = result * 59 + ($url == null ? 43 : $url.hashCode());
            return result;
        }

        public String toString() {
            return "WebhookBody.UrlEmbed(url=" + this.getUrl() + ")";
        }
    }

    static class Embed {
        final UrlEmbed image;

        public Embed(UrlEmbed image) {
            this.image = image;
        }

        public UrlEmbed getImage() {
            return this.image;
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof Embed)) {
                return false;
            }
            Embed other = (Embed)o;
            if (!other.canEqual(this)) {
                return false;
            }
            UrlEmbed this$image = this.getImage();
            UrlEmbed other$image = other.getImage();
            return !(this$image == null ? other$image != null : !((Object)this$image).equals(other$image));
        }

        protected boolean canEqual(Object other) {
            return other instanceof Embed;
        }

        public int hashCode() {
            int PRIME = 59;
            int result = 1;
            UrlEmbed $image = this.getImage();
            result = result * 59 + ($image == null ? 43 : ((Object)$image).hashCode());
            return result;
        }

        public String toString() {
            return "WebhookBody.Embed(image=" + this.getImage() + ")";
        }
    }
}

