/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.http.api.chat;

public class Duels {
    private int wins;
    private int losses;
    private int winningStreak;
    private int losingStreak;

    public int getWins() {
        return this.wins;
    }

    public int getLosses() {
        return this.losses;
    }

    public int getWinningStreak() {
        return this.winningStreak;
    }

    public int getLosingStreak() {
        return this.losingStreak;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public void setWinningStreak(int winningStreak) {
        this.winningStreak = winningStreak;
    }

    public void setLosingStreak(int losingStreak) {
        this.losingStreak = losingStreak;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Duels)) {
            return false;
        }
        Duels other = (Duels)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getWins() != other.getWins()) {
            return false;
        }
        if (this.getLosses() != other.getLosses()) {
            return false;
        }
        if (this.getWinningStreak() != other.getWinningStreak()) {
            return false;
        }
        return this.getLosingStreak() == other.getLosingStreak();
    }

    protected boolean canEqual(Object other) {
        return other instanceof Duels;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getWins();
        result = result * 59 + this.getLosses();
        result = result * 59 + this.getWinningStreak();
        result = result * 59 + this.getLosingStreak();
        return result;
    }

    public String toString() {
        return "Duels(wins=" + this.getWins() + ", losses=" + this.getLosses() + ", winningStreak=" + this.getWinningStreak() + ", losingStreak=" + this.getLosingStreak() + ")";
    }
}

