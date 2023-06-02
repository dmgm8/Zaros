/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.herbiboars;

import java.util.List;
import net.runelite.client.plugins.herbiboars.HerbiboarSearchSpot;
import net.runelite.client.plugins.herbiboars.HerbiboarStart;

enum HerbiboarRule {
    A_SOUTH(HerbiboarSearchSpot.Group.A, HerbiboarStart.MIDDLE),
    C_WEST(HerbiboarSearchSpot.Group.C, HerbiboarStart.MIDDLE),
    D_WEST_1(HerbiboarSearchSpot.Group.D, HerbiboarStart.MIDDLE),
    D_WEST_2(HerbiboarSearchSpot.Group.D, HerbiboarSearchSpot.Group.C),
    E_NORTH(HerbiboarSearchSpot.Group.E, HerbiboarSearchSpot.Group.A),
    F_EAST(HerbiboarSearchSpot.Group.F, HerbiboarSearchSpot.Group.G),
    G_NORTH(HerbiboarSearchSpot.Group.G, HerbiboarSearchSpot.Group.F),
    H_NORTH(HerbiboarSearchSpot.Group.H, HerbiboarSearchSpot.Group.D),
    H_EAST(HerbiboarSearchSpot.Group.H, HerbiboarStart.DRIFTWOOD),
    I_EAST(HerbiboarSearchSpot.Group.I, HerbiboarStart.LEPRECHAUN),
    I_SOUTH_1(HerbiboarSearchSpot.Group.I, HerbiboarStart.GHOST_MUSHROOM),
    I_SOUTH_2(HerbiboarSearchSpot.Group.I, HerbiboarStart.CAMP_ENTRANCE),
    I_WEST(HerbiboarSearchSpot.Group.I, HerbiboarSearchSpot.Group.E);

    private final HerbiboarSearchSpot.Group to;
    private final HerbiboarStart fromStart;
    private final HerbiboarSearchSpot.Group fromGroup;

    private HerbiboarRule(HerbiboarSearchSpot.Group to, HerbiboarSearchSpot.Group from) {
        this(to, null, from);
    }

    private HerbiboarRule(HerbiboarSearchSpot.Group to, HerbiboarStart fromStart) {
        this(to, fromStart, null);
    }

    static boolean canApplyRule(HerbiboarStart start, List<HerbiboarSearchSpot> currentPath) {
        if (start == null || currentPath.isEmpty()) {
            return false;
        }
        int lastIndex = currentPath.size() - 1;
        HerbiboarSearchSpot.Group goingTo = currentPath.get(lastIndex).getGroup();
        for (HerbiboarRule rule : HerbiboarRule.values()) {
            if ((lastIndex <= 0 || !rule.matches(currentPath.get(lastIndex - 1).getGroup(), goingTo)) && (lastIndex != 0 || !rule.matches(start, goingTo))) continue;
            return true;
        }
        return false;
    }

    boolean matches(HerbiboarStart from, HerbiboarSearchSpot.Group to) {
        return this.matches(from, null, to);
    }

    boolean matches(HerbiboarSearchSpot.Group from, HerbiboarSearchSpot.Group to) {
        return this.matches(null, from, to);
    }

    boolean matches(HerbiboarStart fromStart, HerbiboarSearchSpot.Group fromGroup, HerbiboarSearchSpot.Group to) {
        return this.to == to && (fromStart != null && this.fromStart == fromStart || fromGroup != null && this.fromGroup == fromGroup);
    }

    private HerbiboarRule(HerbiboarSearchSpot.Group to, HerbiboarStart fromStart, HerbiboarSearchSpot.Group fromGroup) {
        this.to = to;
        this.fromStart = fromStart;
        this.fromGroup = fromGroup;
    }
}

