/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api.model;

import java.util.List;
import net.runelite.api.Point;
import net.runelite.api.geometry.SimplePolygon;

public class Jarvis {
    @Deprecated
    public static List<Point> convexHull(List<Point> points) {
        int[] xs = new int[points.size()];
        int[] ys = new int[xs.length];
        for (int i = 0; i < xs.length; ++i) {
            Point p = points.get(i);
            xs[i] = p.getX();
            ys[i] = p.getY();
        }
        SimplePolygon poly = Jarvis.convexHull(xs, ys);
        if (poly == null) {
            return null;
        }
        return poly.toRuneLitePointList();
    }

    public static SimplePolygon convexHull(int[] xs, int[] ys) {
        int next;
        int left;
        int i;
        int length = xs.length;
        int offset = 0;
        for (i = 0; i < length; ++i) {
            if (xs[i] != Integer.MIN_VALUE) continue;
            ++offset;
            ++i;
            break;
        }
        while (i < length) {
            if (xs[i] == Integer.MIN_VALUE) {
                ++offset;
            } else {
                xs[i - offset] = xs[i];
                ys[i - offset] = ys[i];
            }
            ++i;
        }
        if ((length -= offset) < 3) {
            return null;
        }
        int current = left = Jarvis.findLeftMost(xs, ys, length);
        SimplePolygon out = new SimplePolygon(new int[16], new int[16], 0);
        do {
            int cx = xs[current];
            int cy = ys[current];
            out.pushRight(cx, cy);
            if (out.size() > length) {
                return null;
            }
            next = 0;
            int nx = xs[next];
            int ny = ys[next];
            for (int i2 = 1; i2 < length; ++i2) {
                long cp = Jarvis.crossProduct(cx, cy, xs[i2], ys[i2], nx, ny);
                if (cp <= 0L && (cp != 0L || Jarvis.square(cx - xs[i2]) + Jarvis.square(cy - ys[i2]) <= Jarvis.square(cx - nx) + Jarvis.square(cy - ny))) continue;
                next = i2;
                nx = xs[next];
                ny = ys[next];
            }
        } while ((current = next) != left);
        return out;
    }

    private static int square(int x) {
        return x * x;
    }

    private static int findLeftMost(int[] xs, int[] ys, int length) {
        int idx = 0;
        int x = xs[idx];
        int y = ys[idx];
        for (int i = 1; i < length; ++i) {
            int ix = xs[i];
            if (ix >= x && (ix != x || ys[i] >= y)) continue;
            idx = i;
            x = xs[idx];
            y = ys[idx];
        }
        return idx;
    }

    private static long crossProduct(int px, int py, int qx, int qy, int rx, int ry) {
        long val = (long)(qy - py) * (long)(rx - qx) - (long)(qx - px) * (long)(ry - qy);
        return val;
    }
}

