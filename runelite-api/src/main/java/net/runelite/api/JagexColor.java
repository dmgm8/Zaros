/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api;

import java.awt.Color;

public final class JagexColor {
    public static final int HUE_MAX = 63;
    public static final int SATURATION_MAX = 7;
    public static final int LUMINANCE_MAX = 127;

    public static short packHSL(int hue, int saturation, int luminance) {
        return (short)((short)(hue & 0x3F) << 10 | (short)(saturation & 7) << 7 | (short)(luminance & 0x7F));
    }

    public static int unpackHue(short hsl) {
        return hsl >> 10 & 0x3F;
    }

    public static int unpackSaturation(short hsl) {
        return hsl >> 7 & 7;
    }

    public static int unpackLuminance(short hsl) {
        return hsl & 0x7F;
    }

    public static String formatHSL(short hsl) {
        return String.format("%02Xh%Xs%02Xl", JagexColor.unpackHue(hsl), JagexColor.unpackSaturation(hsl), JagexColor.unpackLuminance(hsl));
    }

    public static short rgbToHSL(int rgb, double brightness) {
        if (rgb == 1) {
            return 0;
        }
        brightness = 1.0 / brightness;
        double r = (double)(rgb >> 16 & 0xFF) / 256.0;
        double g = (double)(rgb >> 8 & 0xFF) / 256.0;
        double b = (double)(rgb & 0xFF) / 256.0;
        r = Math.pow(r, brightness);
        g = Math.pow(g, brightness);
        b = Math.pow(b, brightness);
        float[] hsv = Color.RGBtoHSB((int)(r * 256.0), (int)(g * 256.0), (int)(b * 256.0), null);
        double hue = hsv[0];
        double luminance = hsv[2] - hsv[2] * hsv[1] / 2.0f;
        double saturation = ((double)hsv[2] - luminance) / Math.min(luminance, 1.0 - luminance);
        return JagexColor.packHSL((int)(Math.ceil(hue * 64.0) % 63.0), (int)Math.ceil(saturation * 7.0), (int)Math.ceil(luminance * 127.0));
    }
}

