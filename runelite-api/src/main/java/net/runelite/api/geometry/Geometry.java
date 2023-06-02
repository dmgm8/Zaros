/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api.geometry;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

public class Geometry {
    public static Point2D.Float lineIntersectionPoint(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
        float p1x = x2 - x1;
        float p1y = y2 - y1;
        float p2x = x4 - x3;
        float p2y = y4 - y3;
        float s = (-p1y * (x1 - x3) + p1x * (y1 - y3)) / (-p2x * p1y + p1x * p2y);
        float t = (p2x * (y1 - y3) - p2y * (x1 - x3)) / (-p2x * p1y + p1x * p2y);
        if (s >= 0.0f && s <= 1.0f && t >= 0.0f && t <= 1.0f) {
            float x = x1 + t * p1x;
            float y = y1 + t * p1y;
            return new Point2D.Float(x, y);
        }
        return null;
    }

    public static List<Point2D.Float> intersectionPoints(Shape shape, float x1, float y1, float x2, float y2) {
        LinkedList<Point2D.Float> intersections = new LinkedList<Point2D.Float>();
        PathIterator it = shape.getPathIterator(new AffineTransform());
        float[] coords = new float[2];
        float[] prevCoords = new float[2];
        float[] start = new float[2];
        while (!it.isDone()) {
            Point2D.Float intersection;
            int type = it.currentSegment(coords);
            if (type == 0) {
                start[0] = coords[0];
                start[1] = coords[1];
                prevCoords[0] = coords[0];
                prevCoords[1] = coords[1];
            } else if (type == 1) {
                intersection = Geometry.lineIntersectionPoint(prevCoords[0], prevCoords[1], coords[0], coords[1], x1, y1, x2, y2);
                if (intersection != null) {
                    intersections.add(intersection);
                }
                prevCoords[0] = coords[0];
                prevCoords[1] = coords[1];
            } else if (type == 4 && (intersection = Geometry.lineIntersectionPoint(coords[0], coords[1], start[0], start[1], x1, y1, x2, y2)) != null) {
                intersections.add(intersection);
            }
            it.next();
        }
        return intersections;
    }

    public static GeneralPath transformPath(PathIterator it, Consumer<float[]> method) {
        GeneralPath path = new GeneralPath();
        float[] coords = new float[2];
        while (!it.isDone()) {
            int type = it.currentSegment(coords);
            if (type == 0) {
                method.accept(coords);
                path.moveTo(coords[0], coords[1]);
            } else if (type == 1) {
                method.accept(coords);
                path.lineTo(coords[0], coords[1]);
            } else if (type == 4) {
                path.closePath();
            }
            it.next();
        }
        return path;
    }

    public static GeneralPath transformPath(GeneralPath path, Consumer<float[]> method) {
        return Geometry.transformPath(path.getPathIterator(new AffineTransform()), method);
    }

    private static void appendSegmentLines(GeneralPath path, float segmentLength, float x1, float y1, float x2, float y2) {
        float x = x1;
        float y = y1;
        float angle = (float)Math.atan2(y2 - y1, x2 - x1);
        float dx = (float)Math.cos(angle) * segmentLength;
        float dy = (float)Math.sin(angle) * segmentLength;
        float length = (float)Math.hypot(x2 - x1, y2 - y1);
        int steps = (int)(((double)length - 1.0E-4) / (double)segmentLength);
        for (int i = 0; i < steps; ++i) {
            path.lineTo(x += dx, y += dy);
        }
    }

    public static GeneralPath splitIntoSegments(PathIterator it, float segmentLength) {
        GeneralPath newPath = new GeneralPath();
        float[] prevCoords = new float[2];
        float[] coords = new float[2];
        float[] startCoords = new float[2];
        while (!it.isDone()) {
            int type = it.currentSegment(coords);
            if (type == 0) {
                startCoords[0] = coords[0];
                startCoords[1] = coords[1];
                newPath.moveTo(coords[0], coords[1]);
                prevCoords[0] = coords[0];
                prevCoords[1] = coords[1];
            } else if (type == 1) {
                Geometry.appendSegmentLines(newPath, segmentLength, prevCoords[0], prevCoords[1], coords[0], coords[1]);
                newPath.lineTo(coords[0], coords[1]);
                prevCoords[0] = coords[0];
                prevCoords[1] = coords[1];
            } else if (type == 4) {
                Geometry.appendSegmentLines(newPath, segmentLength, coords[0], coords[1], startCoords[0], startCoords[1]);
                newPath.closePath();
            }
            it.next();
        }
        return newPath;
    }

    public static GeneralPath splitIntoSegments(GeneralPath path, float segmentLength) {
        return Geometry.splitIntoSegments(path.getPathIterator(new AffineTransform()), segmentLength);
    }

    public static GeneralPath filterPath(PathIterator it, BiPredicate<float[], float[]> method) {
        GeneralPath newPath = new GeneralPath();
        float[] prevCoords = new float[2];
        float[] coords = new float[2];
        float[] start = new float[2];
        boolean shouldMoveNext = false;
        while (!it.isDone()) {
            int type = it.currentSegment(coords);
            if (type == 0) {
                start[0] = coords[0];
                start[1] = coords[1];
                prevCoords[0] = coords[0];
                prevCoords[1] = coords[1];
                shouldMoveNext = true;
            } else if (type == 1) {
                if (method.test(prevCoords, coords)) {
                    if (shouldMoveNext) {
                        newPath.moveTo(prevCoords[0], prevCoords[1]);
                        shouldMoveNext = false;
                    }
                    newPath.lineTo(coords[0], coords[1]);
                } else {
                    shouldMoveNext = true;
                }
                prevCoords[0] = coords[0];
                prevCoords[1] = coords[1];
            } else if (type == 4) {
                if (shouldMoveNext) {
                    newPath.moveTo(prevCoords[0], prevCoords[1]);
                }
                if (method.test(prevCoords, start)) {
                    newPath.lineTo(start[0], start[1]);
                }
                shouldMoveNext = false;
            }
            it.next();
        }
        return newPath;
    }

    public static GeneralPath filterPath(GeneralPath path, BiPredicate<float[], float[]> method) {
        return Geometry.filterPath(path.getPathIterator(new AffineTransform()), method);
    }

    public static GeneralPath clipPath(PathIterator it, Shape shape) {
        GeneralPath newPath = new GeneralPath();
        float[] prevCoords = new float[2];
        float[] coords = new float[2];
        float[] start = new float[2];
        float[] nextMove = new float[2];
        boolean shouldMove = false;
        boolean wasInside = false;
        while (!it.isDone()) {
            int type = it.currentSegment(coords);
            if (type == 0) {
                start[0] = coords[0];
                start[1] = coords[1];
                wasInside = shape.contains(coords[0], coords[1]);
                if (wasInside) {
                    nextMove[0] = coords[0];
                    nextMove[1] = coords[1];
                    shouldMove = true;
                }
                prevCoords[0] = coords[0];
                prevCoords[1] = coords[1];
            } else if (type == 1 || type == 4) {
                if (type == 4) {
                    coords[0] = start[0];
                    coords[1] = start[1];
                }
                List<Point2D.Float> intersections = Geometry.intersectionPoints(shape, prevCoords[0], prevCoords[1], coords[0], coords[1]);
                intersections.sort((a, b) -> {
                    double diff = a.distance(prevCoords[0], prevCoords[1]) - b.distance(prevCoords[0], prevCoords[1]);
                    if (diff < 0.0) {
                        return -1;
                    }
                    if (diff > 0.0) {
                        return 1;
                    }
                    return 0;
                });
                for (Point2D.Float intersection : intersections) {
                    if (wasInside) {
                        if (shouldMove) {
                            newPath.moveTo(nextMove[0], nextMove[1]);
                            shouldMove = false;
                        }
                        newPath.lineTo(intersection.getX(), intersection.getY());
                    } else {
                        nextMove[0] = intersection.x;
                        nextMove[1] = intersection.y;
                        shouldMove = true;
                    }
                    wasInside = !wasInside;
                    prevCoords[0] = intersection.x;
                    prevCoords[1] = intersection.y;
                }
                wasInside = shape.contains(coords[0], coords[1]);
                if (wasInside) {
                    if (shouldMove) {
                        newPath.moveTo(nextMove[0], nextMove[1]);
                        shouldMove = false;
                    }
                    newPath.lineTo(coords[0], coords[1]);
                } else {
                    nextMove[0] = coords[0];
                    nextMove[1] = coords[1];
                    shouldMove = true;
                }
                prevCoords[0] = coords[0];
                prevCoords[1] = coords[1];
            }
            it.next();
        }
        return newPath;
    }

    public static GeneralPath clipPath(GeneralPath path, Shape shape) {
        return Geometry.clipPath(path.getPathIterator(new AffineTransform()), shape);
    }
}

