/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api.geometry;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Shapes<T extends Shape>
implements Shape {
    private final List<T> shapes;

    public Shapes(T ... shape) {
        this(Arrays.asList(shape));
    }

    @Override
    public Rectangle getBounds() {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;
        for (Shape shape : this.shapes) {
            Rectangle bounds = shape.getBounds();
            minX = Math.min(bounds.x, minX);
            minY = Math.min(bounds.y, minY);
            maxX = Math.max(bounds.x + bounds.width, maxX);
            maxY = Math.max(bounds.y + bounds.height, maxY);
        }
        return new Rectangle(minX, minY, maxX - minX, maxY - minY);
    }

    @Override
    public Rectangle2D getBounds2D() {
        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        double maxY = Double.MIN_VALUE;
        for (Shape shape : this.shapes) {
            Rectangle2D bounds = shape.getBounds2D();
            minX = Math.min(bounds.getX(), minX);
            minY = Math.min(bounds.getY(), minY);
            maxX = Math.max(bounds.getMaxX(), maxX);
            maxY = Math.max(bounds.getMaxY(), maxY);
        }
        return new Rectangle2D.Double(minX, minY, maxX - minX, maxY - minY);
    }

    @Override
    public boolean contains(double x, double y) {
        return this.shapes.stream().anyMatch(s -> s.contains(x, y));
    }

    @Override
    public boolean contains(Point2D p) {
        return this.shapes.stream().anyMatch(s -> s.contains(p));
    }

    @Override
    public boolean intersects(double x, double y, double w, double h) {
        return this.shapes.stream().anyMatch(s -> s.intersects(x, y, w, h));
    }

    @Override
    public boolean intersects(Rectangle2D r) {
        return this.shapes.stream().anyMatch(s -> s.intersects(r));
    }

    @Override
    public boolean contains(double x, double y, double w, double h) {
        return this.shapes.stream().anyMatch(s -> s.contains(x, y, w, h));
    }

    @Override
    public boolean contains(Rectangle2D r) {
        return this.shapes.stream().anyMatch(s -> s.contains(r));
    }

    @Override
    public PathIterator getPathIterator(AffineTransform at) {
        return new ShapeIterator(this.shapes.stream().map(s -> s.getPathIterator(at)).iterator());
    }

    @Override
    public PathIterator getPathIterator(AffineTransform at, double flatness) {
        return new ShapeIterator(this.shapes.stream().map(s -> s.getPathIterator(at, flatness)).iterator());
    }

    public Shapes(List<T> shapes) {
        this.shapes = shapes;
    }

    public List<T> getShapes() {
        return this.shapes;
    }

    private static class ShapeIterator
    implements PathIterator {
        private final Iterator<PathIterator> iter;
        private PathIterator current = null;
        private final int windingRule;

        ShapeIterator(Iterator<PathIterator> iter) {
            this.iter = iter;
            if (iter.hasNext()) {
                this.current = iter.next();
                this.windingRule = this.current.getWindingRule();
                this.checkDone();
            } else {
                this.windingRule = 0;
            }
        }

        @Override
        public int getWindingRule() {
            return this.windingRule;
        }

        @Override
        public boolean isDone() {
            return this.current == null;
        }

        @Override
        public void next() {
            this.current.next();
            this.checkDone();
        }

        private void checkDone() {
            while (this.current != null && this.current.isDone()) {
                if (this.iter.hasNext()) {
                    this.current = this.iter.next();
                    assert (this.windingRule == this.current.getWindingRule());
                    continue;
                }
                this.current = null;
            }
        }

        @Override
        public int currentSegment(float[] coords) {
            return this.current.currentSegment(coords);
        }

        @Override
        public int currentSegment(double[] coords) {
            return this.current.currentSegment(coords);
        }
    }
}

