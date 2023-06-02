/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api.events;

import net.runelite.api.HealthBar;

public class PostHealthBar {
    private HealthBar healthBar;

    public HealthBar getHealthBar() {
        return this.healthBar;
    }

    public void setHealthBar(HealthBar healthBar) {
        this.healthBar = healthBar;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PostHealthBar)) {
            return false;
        }
        PostHealthBar other = (PostHealthBar)o;
        if (!other.canEqual(this)) {
            return false;
        }
        HealthBar this$healthBar = this.getHealthBar();
        HealthBar other$healthBar = other.getHealthBar();
        return !(this$healthBar == null ? other$healthBar != null : !this$healthBar.equals(other$healthBar));
    }

    protected boolean canEqual(Object other) {
        return other instanceof PostHealthBar;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        HealthBar $healthBar = this.getHealthBar();
        result = result * 59 + ($healthBar == null ? 43 : $healthBar.hashCode());
        return result;
    }

    public String toString() {
        return "PostHealthBar(healthBar=" + this.getHealthBar() + ")";
    }
}

