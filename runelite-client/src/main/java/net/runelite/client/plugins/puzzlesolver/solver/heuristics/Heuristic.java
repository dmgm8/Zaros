/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.puzzlesolver.solver.heuristics;

import net.runelite.client.plugins.puzzlesolver.solver.PuzzleState;

public interface Heuristic {
    public int computeValue(PuzzleState var1);
}

