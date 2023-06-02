/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.Actor
 */
package rs.api;

import net.runelite.api.Actor;
import net.runelite.mapping.Import;
import net.runelite.rs.api.RSCombatInfoList;
import net.runelite.rs.api.RSRenderable;

public interface RSActor
extends RSRenderable,
Actor {
    @Import(value="interacting")
    public int getRSInteracting();

    @Import(value="overhead")
    public String getOverheadText();

    @Import(value="overhead")
    public void setOverheadText(String var1);

    @Import(value="overheadTextCyclesRemaining")
    public int getOverheadCycle();

    @Import(value="overheadTextCyclesRemaining")
    public void setOverheadCycle(int var1);

    @Import(value="x")
    public int getX();

    @Import(value="y")
    public int getY();

    @Import(value="pathX")
    public int[] getPathX();

    @Import(value="pathY")
    public int[] getPathY();

    @Import(value="animation")
    public int getAnimation();

    @Import(value="animation")
    public void setAnimation(int var1);

    @Import(value="graphic")
    public int getGraphic();

    @Import(value="graphic")
    public void setGraphic(int var1);

    @Import(value="spotAnimHeight")
    public int getGraphicHeight();

    @Import(value="spotAnimHeight")
    public void setGraphicHeight(int var1);

    @Import(value="combatInfoList")
    public RSCombatInfoList getCombatInfoList();

    @Import(value="orientation")
    public int getOrientation();

    @Import(value="currentOrientation")
    public int getCurrentOrientation();

    @Import(value="logicalHeight")
    public int getLogicalHeight();

    @Import(value="idlePoseAnimation")
    public int getIdlePoseAnimation();

    @Import(value="idlePoseAnimation")
    public void setIdlePoseAnimation(int var1);

    @Import(value="turnLeftAnimation")
    public int getIdleRotateLeft();

    @Import(value="turnLeftAnimation")
    public void setIdleRotateLeft(int var1);

    @Import(value="turnRightAnimation")
    public int getIdleRotateRight();

    @Import(value="turnRightAnimation")
    public void setIdleRotateRight(int var1);

    @Import(value="walkForwardAnimation")
    public int getWalkAnimation();

    @Import(value="walkForwardAnimation")
    public void setWalkAnimation(int var1);

    @Import(value="walkLeftAnimation")
    public int getWalkRotateLeft();

    @Import(value="walkLeftAnimation")
    public void setWalkRotateLeft(int var1);

    @Import(value="walkRightAnimation")
    public int getWalkRotateRight();

    @Import(value="walkRightAnimation")
    public void setWalkRotateRight(int var1);

    @Import(value="walkRotate180")
    public int getWalkRotate180();

    @Import(value="walkRotate180")
    public void setWalkRotate180(int var1);

    @Import(value="runAnimation")
    public int getRunAnimation();

    @Import(value="runAnimation")
    public void setRunAnimation(int var1);

    @Import(value="poseAnimation")
    public int getPoseAnimation();

    @Import(value="poseAnimation")
    public void setPoseAnimation(int var1);

    @Import(value="animationFrame")
    public int getAnimationFrame();

    @Import(value="animationFrame")
    public void setAnimationFrame(int var1);

    @Import(value="animationFrameCycle")
    public int getAnimationFrameCycle();

    @Import(value="poseFrame")
    public int getPoseAnimationFrame();

    @Import(value="poseFrame")
    public void setPoseAnimationFrame(int var1);

    @Import(value="poseFrameCycle")
    public int getPoseFrameCycle();

    @Import(value="spotAnimFrame")
    public int getSpotAnimFrame();

    @Import(value="spotAnimFrame")
    public void setSpotAnimFrame(int var1);

    @Import(value="spotAnimFrameCycle")
    public int getSpotAnimFrameCycle();
}

