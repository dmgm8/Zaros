/*
 * Decompiled with CFR 0.150.
 */
package rs.api;

import net.runelite.mapping.Import;
import net.runelite.rs.api.RSCombatInfoList;
import net.runelite.rs.api.RSHealthBar;
import net.runelite.rs.api.RSNode;

public interface RSCombatInfoListHolder
extends RSNode {
    @Import(value="combatInfo1")
    public RSCombatInfoList getCombatInfo1();

    @Import(value="healthBar")
    public RSHealthBar getHealthBar();
}

