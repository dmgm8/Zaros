/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.http.api.account;

public class OAuthResponse {
    private String oauthUrl;

    public String getOauthUrl() {
        return this.oauthUrl;
    }

    public void setOauthUrl(String oauthUrl) {
        this.oauthUrl = oauthUrl;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof OAuthResponse)) {
            return false;
        }
        OAuthResponse other = (OAuthResponse)o;
        if (!other.canEqual(this)) {
            return false;
        }
        String this$oauthUrl = this.getOauthUrl();
        String other$oauthUrl = other.getOauthUrl();
        return !(this$oauthUrl == null ? other$oauthUrl != null : !this$oauthUrl.equals(other$oauthUrl));
    }

    protected boolean canEqual(Object other) {
        return other instanceof OAuthResponse;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $oauthUrl = this.getOauthUrl();
        result = result * 59 + ($oauthUrl == null ? 43 : $oauthUrl.hashCode());
        return result;
    }

    public String toString() {
        return "OAuthResponse(oauthUrl=" + this.getOauthUrl() + ")";
    }
}

