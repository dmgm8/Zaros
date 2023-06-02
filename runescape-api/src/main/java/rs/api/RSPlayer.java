/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.Player
 */
package rs.api;

import net.runelite.api.Player;
import net.runelite.mapping.Import;
import net.runelite.rs.api.RSActor;
import net.runelite.rs.api.RSName;
import net.runelite.rs.api.RSPlayerComposition;

public interface RSPlayer
extends RSActor,
Player {
    @Import(value="playerId")
    public int getId();

    @Import(value="name")
    public RSName getRsName();

    @Import(value="composition")
    public RSPlayerComposition getPlayerComposition();

    @Import(value="combatLevel")
    public int getCombatLevel();

    @Import(value="totalLevel")
    public int getTotalLevel();

    @Import(value="team")
    public int getTeam();

    @Import(value="isFriendsChatMember")
    public boolean isFriendsChatMember();

    @Import(value="isFriend")
    public boolean isFriend();

    @Import(value="isClanMember")
    public boolean isClanMember();

    public boolean isTeam();

    @Import(value="overheadIcon")
    public int getRsOverheadIcon();

    @Import(value="skullIcon")
    public int getRsSkullIcon();
}

