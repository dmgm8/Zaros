/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api.coords;

import net.runelite.api.coords.Direction;

public final class Angle {
    private final int angle;

    public Direction getNearestDirection() {
        int round = this.angle >>> 9;
        int up = this.angle & 0x100;
        if (up != 0) {
            ++round;
        }
        switch (round & 3) {
            case 0: {
                return Direction.SOUTH;
            }
            case 1: {
                return Direction.WEST;
            }
            case 2: {
                return Direction.NORTH;
            }
            case 3: {
                return Direction.EAST;
            }
        }
        throw new IllegalStateException();
    }

    public Angle(int angle) {
        this.angle = angle;
    }

    public int getAngle() {
        return this.angle;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Angle)) {
            return false;
        }
        Angle other = (Angle)o;
        return this.getAngle() == other.getAngle();
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getAngle();
        return result;
    }

    public String toString() {
        return "Angle(angle=" + this.getAngle() + ")";
    }
}

