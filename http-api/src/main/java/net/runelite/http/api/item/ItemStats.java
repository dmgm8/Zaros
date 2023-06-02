/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.gson.annotations.SerializedName
 */
package net.runelite.http.api.item;

import com.google.gson.annotations.SerializedName;
import net.runelite.http.api.item.ItemEquipmentStats;

public final class ItemStats {
    private final boolean equipable;
    private final double weight;
    @SerializedName(value="ge_limit")
    private final int geLimit;
    private final ItemEquipmentStats equipment;

    public ItemStats subtract(ItemStats other) {
        ItemEquipmentStats newEquipment;
        if (other == null) {
            return this;
        }
        double newWeight = this.weight - other.weight;
        if (other.equipment != null) {
            ItemEquipmentStats equipment = this.equipment != null ? this.equipment : new ItemEquipmentStats.ItemEquipmentStatsBuilder().build();
            newEquipment = new ItemEquipmentStats.ItemEquipmentStatsBuilder().slot(equipment.getSlot()).astab(equipment.getAstab() - other.equipment.getAstab()).aslash(equipment.getAslash() - other.equipment.getAslash()).acrush(equipment.getAcrush() - other.equipment.getAcrush()).amagic(equipment.getAmagic() - other.equipment.getAmagic()).arange(equipment.getArange() - other.equipment.getArange()).dstab(equipment.getDstab() - other.equipment.getDstab()).dslash(equipment.getDslash() - other.equipment.getDslash()).dcrush(equipment.getDcrush() - other.equipment.getDcrush()).dmagic(equipment.getDmagic() - other.equipment.getDmagic()).drange(equipment.getDrange() - other.equipment.getDrange()).str(equipment.getStr() - other.equipment.getStr()).rstr(equipment.getRstr() - other.equipment.getRstr()).mdmg(equipment.getMdmg() - other.equipment.getMdmg()).prayer(equipment.getPrayer() - other.equipment.getPrayer()).aspeed(equipment.getAspeed() - other.equipment.getAspeed()).build();
        } else {
            newEquipment = this.equipment;
        }
        return new ItemStats(this.equipable, newWeight, 0, newEquipment);
    }

    public ItemStats(boolean equipable, double weight, int geLimit, ItemEquipmentStats equipment) {
        this.equipable = equipable;
        this.weight = weight;
        this.geLimit = geLimit;
        this.equipment = equipment;
    }

    public boolean isEquipable() {
        return this.equipable;
    }

    public double getWeight() {
        return this.weight;
    }

    public int getGeLimit() {
        return this.geLimit;
    }

    public ItemEquipmentStats getEquipment() {
        return this.equipment;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ItemStats)) {
            return false;
        }
        ItemStats other = (ItemStats)o;
        if (this.isEquipable() != other.isEquipable()) {
            return false;
        }
        if (Double.compare(this.getWeight(), other.getWeight()) != 0) {
            return false;
        }
        if (this.getGeLimit() != other.getGeLimit()) {
            return false;
        }
        ItemEquipmentStats this$equipment = this.getEquipment();
        ItemEquipmentStats other$equipment = other.getEquipment();
        return !(this$equipment == null ? other$equipment != null : !((Object)this$equipment).equals(other$equipment));
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + (this.isEquipable() ? 79 : 97);
        long $weight = Double.doubleToLongBits(this.getWeight());
        result = result * 59 + (int)($weight >>> 32 ^ $weight);
        result = result * 59 + this.getGeLimit();
        ItemEquipmentStats $equipment = this.getEquipment();
        result = result * 59 + ($equipment == null ? 43 : ((Object)$equipment).hashCode());
        return result;
    }

    public String toString() {
        return "ItemStats(equipable=" + this.isEquipable() + ", weight=" + this.getWeight() + ", geLimit=" + this.getGeLimit() + ", equipment=" + this.getEquipment() + ")";
    }
}

