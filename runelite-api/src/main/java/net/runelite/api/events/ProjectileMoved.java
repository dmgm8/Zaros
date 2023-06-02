/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api.events;

import net.runelite.api.Projectile;
import net.runelite.api.coords.LocalPoint;

public class ProjectileMoved {
    private Projectile projectile;
    private LocalPoint position;
    private int z;

    public Projectile getProjectile() {
        return this.projectile;
    }

    public LocalPoint getPosition() {
        return this.position;
    }

    public int getZ() {
        return this.z;
    }

    public void setProjectile(Projectile projectile) {
        this.projectile = projectile;
    }

    public void setPosition(LocalPoint position) {
        this.position = position;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ProjectileMoved)) {
            return false;
        }
        ProjectileMoved other = (ProjectileMoved)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getZ() != other.getZ()) {
            return false;
        }
        Projectile this$projectile = this.getProjectile();
        Projectile other$projectile = other.getProjectile();
        if (this$projectile == null ? other$projectile != null : !this$projectile.equals(other$projectile)) {
            return false;
        }
        LocalPoint this$position = this.getPosition();
        LocalPoint other$position = other.getPosition();
        return !(this$position == null ? other$position != null : !((Object)this$position).equals(other$position));
    }

    protected boolean canEqual(Object other) {
        return other instanceof ProjectileMoved;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getZ();
        Projectile $projectile = this.getProjectile();
        result = result * 59 + ($projectile == null ? 43 : $projectile.hashCode());
        LocalPoint $position = this.getPosition();
        result = result * 59 + ($position == null ? 43 : ((Object)$position).hashCode());
        return result;
    }

    public String toString() {
        return "ProjectileMoved(projectile=" + this.getProjectile() + ", position=" + this.getPosition() + ", z=" + this.getZ() + ")";
    }
}

