/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.Buffer
 *  net.runelite.api.widgets.Widget
 */
package rs.api;

import net.runelite.api.Buffer;
import net.runelite.api.widgets.Widget;
import net.runelite.mapping.Import;
import net.runelite.rs.api.RSFillMode;
import net.runelite.rs.api.RSFontTypeFace;

public interface RSWidget
extends Widget {
    @Import(value="children")
    public RSWidget[] getChildren();

    @Import(value="children")
    public void setChildren(Widget[] var1);

    @Import(value="id")
    public int getId();

    public void setRenderParentId(int var1);

    public void setRenderX(int var1);

    public void setRenderY(int var1);

    @Import(value="id")
    public void setId(int var1);

    @Import(value="parentId")
    public int getRSParentId();

    @Import(value="parentId")
    public void setParentId(int var1);

    @Import(value="clickMask")
    public int getClickMask();

    @Import(value="clickMask")
    public RSWidget setClickMask(int var1);

    @Import(value="boundsIndex")
    public int getBoundsIndex();

    @Import(value="modelId")
    public int getModelId();

    @Import(value="modelId")
    public RSWidget setModelId(int var1);

    @Import(value="itemIds")
    public int[] getItemIds();

    @Import(value="itemQuantities")
    public int[] getItemQuantities();

    @Import(value="modelType")
    public int getModelType();

    @Import(value="modelType")
    public RSWidget setModelType(int var1);

    @Import(value="animation")
    public int getAnimationId();

    @Import(value="animation")
    public RSWidget setAnimationId(int var1);

    @Import(value="actions")
    public String[] getActions();

    @Import(value="text")
    public String getRSText();

    @Import(value="name")
    public String getRSName();

    @Import(value="name")
    public void setRSName(String var1);

    @Import(value="text")
    public RSWidget setText(String var1);

    @Import(value="textColor")
    public int getTextColor();

    @Import(value="textColor")
    public RSWidget setTextColor(int var1);

    @Import(value="fillMode")
    public RSFillMode getFillMode();

    @Import(value="opacity")
    public int getOpacity();

    @Import(value="opacity")
    public RSWidget setOpacity(int var1);

    @Import(value="relativeX")
    public int getRelativeX();

    @Import(value="relativeX")
    public void setRelativeX(int var1);

    @Import(value="relativeY")
    public int getRelativeY();

    @Import(value="relativeY")
    public void setRelativeY(int var1);

    @Import(value="width")
    public int getWidth();

    @Import(value="width")
    public void setWidth(int var1);

    @Import(value="height")
    public int getHeight();

    @Import(value="height")
    public void setHeight(int var1);

    @Import(value="isHidden")
    public boolean isSelfHidden();

    @Import(value="isHidden")
    public RSWidget setHidden(boolean var1);

    @Import(value="index")
    public int getIndex();

    @Import(value="index")
    public void setIndex(int var1);

    @Import(value="rotationX")
    public int getRotationX();

    @Import(value="rotationX")
    public RSWidget setRotationX(int var1);

    @Import(value="rotationY")
    public int getRotationY();

    @Import(value="rotationY")
    public RSWidget setRotationY(int var1);

    @Import(value="rotationZ")
    public int getRotationZ();

    @Import(value="rotationZ")
    public RSWidget setRotationZ(int var1);

    @Import(value="modelZoom")
    public int getModelZoom();

    @Import(value="modelZoom")
    public RSWidget setModelZoom(int var1);

    @Import(value="contentType")
    public int getContentType();

    @Import(value="contentType")
    public RSWidget setContentType(int var1);

    @Import(value="type")
    public int getType();

    @Import(value="type")
    public void setType(int var1);

    @Import(value="scrollX")
    public int getScrollX();

    @Import(value="scrollX")
    public RSWidget setScrollX(int var1);

    @Import(value="scrollY")
    public int getScrollY();

    @Import(value="scrollY")
    public RSWidget setScrollY(int var1);

    @Import(value="scrollWidth")
    public int getScrollWidth();

    @Import(value="scrollWidth")
    public RSWidget setScrollWidth(int var1);

    @Import(value="scrollHeight")
    public int getScrollHeight();

    @Import(value="scrollHeight")
    public RSWidget setScrollHeight(int var1);

    @Import(value="spriteId")
    public int getSpriteId();

    @Import(value="spriteTiling")
    public boolean getSpriteTiling();

    @Import(value="spriteTiling")
    public RSWidget setSpriteTiling(boolean var1);

    @Import(value="spriteId")
    public RSWidget setSpriteId(int var1);

    @Import(value="borderType")
    public int getBorderType();

    @Import(value="borderType")
    public void setBorderType(int var1);

    @Import(value="itemId")
    public int getItemId();

    @Import(value="itemId")
    public RSWidget setItemId(int var1);

    @Import(value="itemQuantity")
    public int getItemQuantity();

    @Import(value="itemQuantity")
    public RSWidget setItemQuantity(int var1);

    @Import(value="originalX")
    public int getOriginalX();

    @Import(value="originalX")
    public RSWidget setOriginalX(int var1);

    @Import(value="originalY")
    public int getOriginalY();

    @Import(value="originalY")
    public RSWidget setOriginalY(int var1);

    @Import(value="originalHeight")
    public int getOriginalHeight();

    @Import(value="originalHeight")
    public RSWidget setOriginalHeight(int var1);

    @Import(value="originalWidth")
    public int getOriginalWidth();

    @Import(value="originalWidth")
    public RSWidget setOriginalWidth(int var1);

    @Import(value="xPitch")
    public int getXPitch();

    @Import(value="yPitch")
    public int getYPitch();

    public void broadcastHidden(boolean var1);

    @Import(value="onOpListener")
    public void setOnOpListener(Object ... var1);

    @Import(value="setAction")
    public void setAction(int var1, String var2);

    @Import(value="isIf3")
    public boolean isIf3();

    @Import(value="isIf3")
    public void setIsIf3(boolean var1);

    @Import(value="hasListener")
    public boolean hasListener();

    @Import(value="hasListener")
    public RSWidget setHasListener(boolean var1);

    @Import(value="onOpListener")
    public Object[] getOnOpListener();

    @Import(value="onKeyListener")
    public Object[] getOnKeyListener();

    @Import(value="onLoadListener")
    public Object[] getOnLoadListener();

    @Import(value="onInvTransmitListener")
    public Object[] getOnInvTransmitListener();

    @Import(value="onDialogAbortListener")
    public void setOnDialogAbortListener(Object ... var1);

    @Import(value="onKeyListener")
    public void setOnKeyListener(Object ... var1);

    @Import(value="onMouseOverListener")
    public void setOnMouseOverListener(Object ... var1);

    @Import(value="onMouseRepeatListener")
    public void setOnMouseRepeatListener(Object ... var1);

    @Import(value="onMouseLeaveListener")
    public void setOnMouseLeaveListener(Object ... var1);

    @Import(value="onTimerListener")
    public void setOnTimerListener(Object ... var1);

    @Import(value="onTargetEnterListener")
    public void setOnTargetEnterListener(Object ... var1);

    @Import(value="onTargetLeaveListener")
    public void setOnTargetLeaveListener(Object ... var1);

    @Import(value="fontId")
    public int getFontId();

    @Import(value="fontId")
    public RSWidget setFontId(int var1);

    @Import(value="textShadowed")
    public boolean getTextShadowed();

    @Import(value="textShadowed")
    public RSWidget setTextShadowed(boolean var1);

    @Import(value="dragDeadZone")
    public int getDragDeadZone();

    @Import(value="dragDeadZone")
    public void setDragDeadZone(int var1);

    @Import(value="dragDeadTime")
    public int getDragDeadTime();

    @Import(value="dragDeadTime")
    public void setDragDeadTime(int var1);

    @Import(value="itemQuantityMode")
    public int getItemQuantityMode();

    @Import(value="itemQuantityMode")
    public RSWidget setItemQuantityMode(int var1);

    @Import(value="xPositionMode")
    public int getXPositionMode();

    @Import(value="xPositionMode")
    public RSWidget setXPositionMode(int var1);

    @Import(value="yPositionMode")
    public int getYPositionMode();

    @Import(value="yPositionMode")
    public RSWidget setYPositionMode(int var1);

    @Import(value="lineHeight")
    public int getLineHeight();

    @Import(value="lineHeight")
    public RSWidget setLineHeight(int var1);

    @Import(value="xTextAlignment")
    public int getXTextAlignment();

    @Import(value="xTextAlignment")
    public RSWidget setXTextAlignment(int var1);

    @Import(value="yTextAlignment")
    public int getYTextAlignment();

    @Import(value="yTextAlignment")
    public RSWidget setYTextAlignment(int var1);

    @Import(value="widthMode")
    public int getWidthMode();

    @Import(value="widthMode")
    public RSWidget setWidthMode(int var1);

    @Import(value="heightMode")
    public int getHeightMode();

    @Import(value="heightMode")
    public RSWidget setHeightMode(int var1);

    @Import(value="getFont")
    public RSFontTypeFace getFont();

    @Import(value="filled")
    public boolean isFilled();

    @Import(value="filled")
    public RSWidget setFilled(boolean var1);

    @Import(value="targetVerb")
    public String getTargetVerb();

    @Import(value="targetVerb")
    public void setTargetVerb(String var1);

    @Import(value="noClickThrough")
    public boolean getNoClickThrough();

    @Import(value="noClickThrough")
    public void setNoClickThrough(boolean var1);

    @Import(value="noScrollThrough")
    public boolean getNoScrollThrough();

    @Import(value="noScrollThrough")
    public void setNoScrollThrough(boolean var1);

    @Import(value="varTransmitTriggers")
    public void setVarTransmitTrigger(int ... var1);

    @Import(value="onClickListener")
    public void setOnClickListener(Object ... var1);

    @Import(value="onHoldListener")
    public void setOnHoldListener(Object ... var1);

    @Import(value="onReleaseListener")
    public void setOnReleaseListener(Object ... var1);

    @Import(value="onDragCompleteListener")
    public void setOnDragCompleteListener(Object ... var1);

    @Import(value="onDragListener")
    public void setOnDragListener(Object ... var1);

    @Import(value="dragParent")
    public Widget getDragParent();

    @Import(value="dragParent")
    public RSWidget setDragParent(Widget var1);

    @Import(value="onVarTransmitListener")
    public Object[] getOnVarTransmitListener();

    @Import(value="onVarTransmitListener")
    public void setOnVarTransmitListener(Object ... var1);

    @Import(value="decodeIf3")
    public void decode(Buffer var1);
}

