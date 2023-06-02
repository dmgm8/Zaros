/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api;

public enum InstanceTemplates {
    RAIDS_LOBBY(3264, 5184, 0, 96, 32),
    RAIDS_START(3264, 5696, 0, 96, 32),
    RAIDS_END(3264, 5152, 0, 64, 32),
    RAIDS_SCAVENGERS(3264, 5216, 0, 96, 32),
    RAIDS_SHAMANS(3264, 5248, 0, 96, 32),
    RAIDS_VASA(3264, 5280, 0, 96, 32),
    RAIDS_VANGUARDS(3264, 5312, 0, 96, 32),
    RAIDS_ICE_DEMON(3264, 5344, 0, 96, 32),
    RAIDS_THIEVING(3264, 5376, 0, 96, 32),
    RAIDS_FARMING(3264, 5440, 0, 96, 32),
    RAIDS_SCAVENGERS2(3264, 5216, 1, 96, 32),
    RAIDS_MUTTADILES(3264, 5312, 1, 96, 32),
    RAIDS_MYSTICS(3264, 5248, 1, 96, 32),
    RAIDS_TEKTON(3264, 5280, 1, 96, 32),
    RAIDS_TIGHTROPE(3264, 5344, 1, 96, 32),
    RAIDS_FARMING2(3264, 5440, 1, 96, 32),
    RAIDS_GUARDIANS(3264, 5248, 2, 96, 32),
    RAIDS_VESPULA(3264, 5280, 2, 96, 32),
    RAIDS_CRABS(3264, 5344, 2, 96, 32);

    private final int baseX;
    private final int baseY;
    private final int plane;
    private final int width;
    private final int height;

    public static InstanceTemplates findMatch(int chunkData) {
        int rotation = chunkData >> 1 & 3;
        int y = (chunkData >> 3 & 0x7FF) * 8;
        int x = (chunkData >> 14 & 0x3FF) * 8;
        int plane = chunkData >> 24 & 3;
        for (InstanceTemplates template : InstanceTemplates.values()) {
            if (plane != template.getPlane() || x < template.getBaseX() || x >= template.getBaseX() + template.getWidth() || y < template.getBaseY() || y >= template.getBaseY() + template.getHeight()) continue;
            return template;
        }
        return null;
    }

    private InstanceTemplates(int baseX, int baseY, int plane, int width, int height) {
        this.baseX = baseX;
        this.baseY = baseY;
        this.plane = plane;
        this.width = width;
        this.height = height;
    }

    public int getBaseX() {
        return this.baseX;
    }

    public int getBaseY() {
        return this.baseY;
    }

    public int getPlane() {
        return this.plane;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }
}

