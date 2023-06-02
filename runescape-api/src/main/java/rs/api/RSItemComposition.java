/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.runelite.api.ItemComposition
 *  net.runelite.api.IterableHashTable
 *  net.runelite.api.Node
 */
package rs.api;

import javax.annotation.Nullable;
import net.runelite.api.ItemComposition;
import net.runelite.api.IterableHashTable;
import net.runelite.api.Node;
import net.runelite.mapping.Import;

public interface RSItemComposition
extends ItemComposition {
    @Import(value="name")
    public String getName();

    @Import(value="name")
    public void setName(String var1);

    @Import(value="id")
    public int getId();

    @Import(value="notedTemplate")
    public int getNote();

    @Import(value="note")
    public int getLinkedNoteId();

    @Import(value="placeholderId")
    public int getPlaceholderId();

    @Import(value="placeholderTemplateId")
    public int getPlaceholderTemplateId();

    @Import(value="price")
    public int getPrice();

    @Import(value="isMembers")
    public boolean isMembers();

    @Import(value="isTradable")
    public boolean isTradeable();

    @Import(value="isStackable")
    public int getIsStackable();

    @Import(value="maleModel")
    public int getMaleModel();

    @Import(value="inventoryActions")
    public String[] getInventoryActions();

    @Import(value="getShiftClickActionIndex")
    public int getShiftClickActionIndex();

    @Import(value="inventoryModel")
    public int getInventoryModel();

    @Import(value="inventoryModel")
    public void setInventoryModel(int var1);

    @Import(value="colourToReplace")
    @Nullable
    public short[] getColorToReplace();

    @Import(value="colourToReplace")
    public void setColorToReplace(short[] var1);

    @Import(value="colourToReplaceWith")
    @Nullable
    public short[] getColorToReplaceWith();

    @Import(value="colourToReplaceWith")
    public void setColorToReplaceWith(short[] var1);

    @Import(value="textureToReplace")
    @Nullable
    public short[] getTextureToReplace();

    @Import(value="textureToReplace")
    public void setTextureToReplace(short[] var1);

    @Import(value="textToReplaceWith")
    @Nullable
    public short[] getTextureToReplaceWith();

    @Import(value="textToReplaceWith")
    public void setTextureToReplaceWith(short[] var1);

    @Import(value="xan2d")
    public int getXan2d();

    @Import(value="xan2d")
    public void setXan2d(int var1);

    @Import(value="yan2d")
    public int getYan2d();

    @Import(value="yan2d")
    public void setYan2d(int var1);

    @Import(value="zan2d")
    public int getZan2d();

    @Import(value="zan2d")
    public void setZan2d(int var1);

    @Import(value="params")
    public IterableHashTable<Node> getParams();

    @Import(value="params")
    public void setParams(IterableHashTable<Node> var1);
}

