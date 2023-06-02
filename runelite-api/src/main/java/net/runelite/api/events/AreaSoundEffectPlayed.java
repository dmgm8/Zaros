/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package net.runelite.api.events;

import javax.annotation.Nullable;
import net.runelite.api.Actor;

public class AreaSoundEffectPlayed {
    @Nullable
    private final Actor source;
    private int soundId;
    private int sceneX;
    private int sceneY;
    private int range;
    private int delay;
    private boolean consumed;

    public void consume() {
        this.consumed = true;
    }

    public AreaSoundEffectPlayed(@Nullable Actor source) {
        this.source = source;
    }

    @Nullable
    public Actor getSource() {
        return this.source;
    }

    public int getSoundId() {
        return this.soundId;
    }

    public int getSceneX() {
        return this.sceneX;
    }

    public int getSceneY() {
        return this.sceneY;
    }

    public int getRange() {
        return this.range;
    }

    public int getDelay() {
        return this.delay;
    }

    public boolean isConsumed() {
        return this.consumed;
    }

    public void setSoundId(int soundId) {
        this.soundId = soundId;
    }

    public void setSceneX(int sceneX) {
        this.sceneX = sceneX;
    }

    public void setSceneY(int sceneY) {
        this.sceneY = sceneY;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public void setConsumed(boolean consumed) {
        this.consumed = consumed;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AreaSoundEffectPlayed)) {
            return false;
        }
        AreaSoundEffectPlayed other = (AreaSoundEffectPlayed)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getSoundId() != other.getSoundId()) {
            return false;
        }
        if (this.getSceneX() != other.getSceneX()) {
            return false;
        }
        if (this.getSceneY() != other.getSceneY()) {
            return false;
        }
        if (this.getRange() != other.getRange()) {
            return false;
        }
        if (this.getDelay() != other.getDelay()) {
            return false;
        }
        if (this.isConsumed() != other.isConsumed()) {
            return false;
        }
        Actor this$source = this.getSource();
        Actor other$source = other.getSource();
        return !(this$source == null ? other$source != null : !this$source.equals(other$source));
    }

    protected boolean canEqual(Object other) {
        return other instanceof AreaSoundEffectPlayed;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getSoundId();
        result = result * 59 + this.getSceneX();
        result = result * 59 + this.getSceneY();
        result = result * 59 + this.getRange();
        result = result * 59 + this.getDelay();
        result = result * 59 + (this.isConsumed() ? 79 : 97);
        Actor $source = this.getSource();
        result = result * 59 + ($source == null ? 43 : $source.hashCode());
        return result;
    }

    public String toString() {
        return "AreaSoundEffectPlayed(source=" + this.getSource() + ", soundId=" + this.getSoundId() + ", sceneX=" + this.getSceneX() + ", sceneY=" + this.getSceneY() + ", range=" + this.getRange() + ", delay=" + this.getDelay() + ", consumed=" + this.isConsumed() + ")";
    }
}

