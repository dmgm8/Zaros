/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.runelite.api.Item
 */
package net.runelite.client.plugins.devtools;

import java.util.Arrays;
import javax.annotation.Nullable;
import net.runelite.api.Item;

final class InventoryLog {
    private final int containerId;
    @Nullable
    private final String containerName;
    private final Item[] items;
    private final int tick;

    public InventoryLog(int containerId, @Nullable String containerName, Item[] items, int tick) {
        this.containerId = containerId;
        this.containerName = containerName;
        this.items = items;
        this.tick = tick;
    }

    public int getContainerId() {
        return this.containerId;
    }

    @Nullable
    public String getContainerName() {
        return this.containerName;
    }

    public Item[] getItems() {
        return this.items;
    }

    public int getTick() {
        return this.tick;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof InventoryLog)) {
            return false;
        }
        InventoryLog other = (InventoryLog)o;
        if (this.getContainerId() != other.getContainerId()) {
            return false;
        }
        if (this.getTick() != other.getTick()) {
            return false;
        }
        String this$containerName = this.getContainerName();
        String other$containerName = other.getContainerName();
        if (this$containerName == null ? other$containerName != null : !this$containerName.equals(other$containerName)) {
            return false;
        }
        return Arrays.deepEquals((Object[])this.getItems(), (Object[])other.getItems());
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getContainerId();
        result = result * 59 + this.getTick();
        String $containerName = this.getContainerName();
        result = result * 59 + ($containerName == null ? 43 : $containerName.hashCode());
        result = result * 59 + Arrays.deepHashCode((Object[])this.getItems());
        return result;
    }

    public String toString() {
        return "InventoryLog(containerId=" + this.getContainerId() + ", containerName=" + this.getContainerName() + ", items=" + Arrays.deepToString((Object[])this.getItems()) + ", tick=" + this.getTick() + ")";
    }
}

