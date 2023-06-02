/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  org.jetbrains.annotations.Range
 */
package net.runelite.api.widgets;

import java.awt.Rectangle;
import java.util.Collection;
import javax.annotation.Nullable;
import net.runelite.api.FontTypeFace;
import net.runelite.api.Point;
import net.runelite.api.widgets.WidgetItem;
import org.jetbrains.annotations.Range;

public interface Widget {
    public int getId();

    public int getType();

    public void setType(int var1);

    public int getContentType();

    public Widget setContentType(int var1);

    public int getClickMask();

    public Widget setClickMask(int var1);

    public Widget getParent();

    public int getParentId();

    public Widget getChild(int var1);

    @Nullable
    public Widget[] getChildren();

    public void setChildren(Widget[] var1);

    public Widget[] getDynamicChildren();

    public Widget[] getStaticChildren();

    public Widget[] getNestedChildren();

    public int getRelativeX();

    @Deprecated
    public void setRelativeX(int var1);

    public int getRelativeY();

    @Deprecated
    public void setRelativeY(int var1);

    public void setForcedPosition(int var1, int var2);

    public String getText();

    public Widget setText(String var1);

    public int getTextColor();

    public Widget setTextColor(int var1);

    public int getOpacity();

    public Widget setOpacity(int var1);

    public String getName();

    public Widget setName(String var1);

    public int getModelId();

    public Widget setModelId(int var1);

    public int getModelType();

    public Widget setModelType(int var1);

    public int getAnimationId();

    public Widget setAnimationId(int var1);

    public @Range(from=0L, to=2047L) int getRotationX();

    public Widget setRotationX(@Range(from=0L, to=2047L) int var1);

    public @Range(from=0L, to=2047L) int getRotationY();

    public Widget setRotationY(@Range(from=0L, to=2047L) int var1);

    public @Range(from=0L, to=2047L) int getRotationZ();

    public Widget setRotationZ(@Range(from=0L, to=2047L) int var1);

    public int getModelZoom();

    public Widget setModelZoom(int var1);

    public int getSpriteId();

    public boolean getSpriteTiling();

    public Widget setSpriteTiling(boolean var1);

    public Widget setSpriteId(int var1);

    public boolean isHidden();

    public boolean isSelfHidden();

    public Widget setHidden(boolean var1);

    public int getIndex();

    public Point getCanvasLocation();

    public int getWidth();

    @Deprecated
    public void setWidth(int var1);

    public int getHeight();

    @Deprecated
    public void setHeight(int var1);

    public Rectangle getBounds();

    @Deprecated
    public Collection<WidgetItem> getWidgetItems();

    @Deprecated
    public WidgetItem getWidgetItem(int var1);

    public int getItemId();

    public Widget setItemId(int var1);

    public int getItemQuantity();

    public Widget setItemQuantity(int var1);

    public boolean contains(Point var1);

    public int getScrollX();

    public Widget setScrollX(int var1);

    public int getScrollY();

    public Widget setScrollY(int var1);

    public int getScrollWidth();

    public Widget setScrollWidth(int var1);

    public int getScrollHeight();

    public Widget setScrollHeight(int var1);

    public int getOriginalX();

    public Widget setOriginalX(int var1);

    public int getOriginalY();

    public Widget setOriginalY(int var1);

    public Widget setPos(int var1, int var2);

    public Widget setPos(int var1, int var2, int var3, int var4);

    public int getOriginalHeight();

    public Widget setOriginalHeight(int var1);

    public int getOriginalWidth();

    public Widget setOriginalWidth(int var1);

    public Widget setSize(int var1, int var2);

    public Widget setSize(int var1, int var2, int var3, int var4);

    public String[] getActions();

    public Widget createChild(int var1, int var2);

    public Widget createChild(int var1);

    public void deleteAllChildren();

    public void setAction(int var1, String var2);

    public void setOnOpListener(Object ... var1);

    public void setOnDialogAbortListener(Object ... var1);

    public void setOnKeyListener(Object ... var1);

    public void setOnMouseOverListener(Object ... var1);

    public void setOnMouseRepeatListener(Object ... var1);

    public void setOnMouseLeaveListener(Object ... var1);

    public void setOnTimerListener(Object ... var1);

    public void setOnTargetEnterListener(Object ... var1);

    public void setOnTargetLeaveListener(Object ... var1);

    public boolean hasListener();

    public Widget setHasListener(boolean var1);

    public boolean isIf3();

    public void revalidate();

    public void revalidateScroll();

    public Object[] getOnOpListener();

    public Object[] getOnKeyListener();

    public Object[] getOnLoadListener();

    public Object[] getOnInvTransmitListener();

    public int getFontId();

    public Widget setFontId(int var1);

    public int getBorderType();

    public void setBorderType(int var1);

    public boolean getTextShadowed();

    public Widget setTextShadowed(boolean var1);

    public int getDragDeadZone();

    public void setDragDeadZone(int var1);

    public int getDragDeadTime();

    public void setDragDeadTime(int var1);

    public int getItemQuantityMode();

    public Widget setItemQuantityMode(int var1);

    public int getXPositionMode();

    public Widget setXPositionMode(int var1);

    public int getYPositionMode();

    public Widget setYPositionMode(int var1);

    public int getLineHeight();

    public Widget setLineHeight(int var1);

    public int getXTextAlignment();

    public Widget setXTextAlignment(int var1);

    public int getYTextAlignment();

    public Widget setYTextAlignment(int var1);

    public int getWidthMode();

    public Widget setWidthMode(int var1);

    public int getHeightMode();

    public Widget setHeightMode(int var1);

    public FontTypeFace getFont();

    public boolean isFilled();

    public Widget setFilled(boolean var1);

    public String getTargetVerb();

    public void setTargetVerb(String var1);

    public boolean getNoClickThrough();

    public void setNoClickThrough(boolean var1);

    public boolean getNoScrollThrough();

    public void setNoScrollThrough(boolean var1);

    public void setVarTransmitTrigger(int ... var1);

    public void setOnClickListener(Object ... var1);

    public void setOnHoldListener(Object ... var1);

    public void setOnReleaseListener(Object ... var1);

    public void setOnDragCompleteListener(Object ... var1);

    public void setOnDragListener(Object ... var1);

    public Widget getDragParent();

    public Widget setDragParent(Widget var1);

    public Object[] getOnVarTransmitListener();

    public void setOnVarTransmitListener(Object ... var1);
}

