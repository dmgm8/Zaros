/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package net.runelite.api;

import javax.annotation.Nullable;
import net.runelite.api.ParamHolder;

public interface ItemComposition
extends ParamHolder {
    public String getName();

    public String getMembersName();

    public void setName(String var1);

    public int getId();

    public int getNote();

    public int getLinkedNoteId();

    public int getPlaceholderId();

    public int getPlaceholderTemplateId();

    public int getPrice();

    public int getHaPrice();

    public boolean isMembers();

    public boolean isStackable();

    public boolean isTradeable();

    public String[] getInventoryActions();

    public int getShiftClickActionIndex();

    public void setShiftClickActionIndex(int var1);

    public int getInventoryModel();

    public void setInventoryModel(int var1);

    @Nullable
    public short[] getColorToReplace();

    public void setColorToReplace(short[] var1);

    @Nullable
    public short[] getColorToReplaceWith();

    public void setColorToReplaceWith(short[] var1);

    @Nullable
    public short[] getTextureToReplace();

    public void setTextureToReplace(short[] var1);

    @Nullable
    public short[] getTextureToReplaceWith();

    public void setTextureToReplaceWith(short[] var1);

    public int getXan2d();

    public int getYan2d();

    public int getZan2d();

    public void setXan2d(int var1);

    public void setYan2d(int var1);

    public void setZan2d(int var1);
}

