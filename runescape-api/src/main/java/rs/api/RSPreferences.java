/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.Preferences
 */
package rs.api;

import net.runelite.api.Preferences;
import net.runelite.mapping.Import;

public interface RSPreferences
extends Preferences {
    @Import(value="rememberedUsername")
    public String getRememberedUsername();

    @Import(value="rememberedUsername")
    public void setRememberedUsername(String var1);

    @Import(value="musicVolume")
    public int getMusicVolume();

    @Import(value="musicVolume")
    public void setMusicVolume(int var1);

    @Import(value="soundEffectVolume")
    public int getSoundEffectVolume();

    @Import(value="soundEffectVolume")
    public void setSoundEffectVolume(int var1);

    @Import(value="areaSoundEffectVolume")
    public int getAreaSoundEffectVolume();

    @Import(value="areaSoundEffectVolume")
    public void setAreaSoundEffectVolume(int var1);

    @Import(value="hideUsername")
    public boolean getHideUsername();
}

