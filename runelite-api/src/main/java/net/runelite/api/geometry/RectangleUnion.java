/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package net.runelite.api.geometry;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.annotation.Nullable;
import net.runelite.api.geometry.Shapes;
import net.runelite.api.geometry.SimplePolygon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RectangleUnion {
    private static final Logger log = LoggerFactory.getLogger(RectangleUnion.class);

    private RectangleUnion() {
    }

    @Nullable
    public static Shapes<SimplePolygon> union(List<Rectangle> lefts) {
        if (lefts.size() == 0) {
            return null;
        }
        boolean trace = log.isTraceEnabled();
        lefts.sort(Comparator.comparingInt(Rectangle::getX1));
        ArrayList<Rectangle> rights = new ArrayList<Rectangle>(lefts);
        rights.sort(Comparator.comparingInt(Rectangle::getX2));
        Segments segments = new Segments();
        Shapes<SimplePolygon> out = new Shapes<SimplePolygon>(new ArrayList());
        ChangingState cs = new ChangingState(out);
        int l = 0;
        int r = 0;
        while (true) {
            Rectangle rect;
            boolean remove;
            Rectangle lr = null;
            Rectangle rr = null;
            if (l < lefts.size()) {
                lr = lefts.get(l);
            }
            if (r < rights.size()) {
                rr = (Rectangle)rights.get(r);
            }
            if (lr == null && rr == null) break;
            boolean bl = remove = lr == null || rr != null && rr.x2 < lr.x1;
            if (remove) {
                cs.delta = -1;
                cs.x = rr.x2;
                ++r;
                rect = rr;
            } else {
                cs.delta = 1;
                cs.x = lr.x1;
                ++l;
                rect = lr;
            }
            if (trace) {
                log.trace("{}{}", (Object)(remove ? "-" : "+"), (Object)rect);
            }
            int y1 = rect.y1;
            int y2 = rect.y2;
            Segment n = segments.findLE(y1);
            if (n == null) {
                n = segments.insertAfter(null, y1);
            }
            if (n.y != y1) {
                n = segments.insertAfter(n, y1);
                n.value = n.previous.value;
            }
            do {
                if (n.next == null || n.next.y > y2) {
                    segments.insertAfter(n, y2);
                }
                cs.touch(n);
                n = n.next;
            } while (n.y != y2);
            cs.finish(n);
            if (!trace) continue;
            Segment s = segments.first;
            while (s != null) {
                String chunk = "";
                if (s.chunk != null) {
                    chunk = (s.left ? ">" : "[") + System.identityHashCode(s.chunk) + (s.left ? "]" : "<");
                }
                log.trace("{} = {} {}", new Object[]{s.y, s.value, chunk});
                s = s.next;
            }
            log.trace("");
        }
        assert (segments.allZero());
        return out;
    }

    private static class Chunk
    extends SimplePolygon {
        Segment left;
        Segment right;

        private Chunk() {
        }

        @Override
        public void reverse() {
            super.reverse();
            assert (!this.right.left);
            assert (this.left.left);
            Segment tr = this.left;
            this.left = this.right;
            this.right = tr;
            this.right.left = false;
            this.left.left = true;
        }
    }

    private static class Segments {
        Segment first;

        Segment findLE(int y) {
            Segment s = this.first;
            if (s == null || s.y > y) {
                return null;
            }
            while (s.y != y) {
                Segment n = s.next;
                if (n == null || n.y > y) {
                    return s;
                }
                s = n;
            }
            return s;
        }

        Segment insertAfter(Segment before, int y) {
            Segment n = new Segment();
            n.y = y;
            if (before != null) {
                if (before.next != null) {
                    n.next = before.next;
                    n.next.previous = n;
                }
                n.value = before.value;
                before.next = n;
                n.previous = before;
            } else {
                if (this.first != null) {
                    n.next = this.first;
                    this.first.previous = n;
                }
                this.first = n;
            }
            return n;
        }

        boolean allZero() {
            Segment s = this.first;
            while (s != null) {
                if (s.value != 0 || s.chunk != null) {
                    return false;
                }
                s = s.next;
            }
            return true;
        }
    }

    private static class Segment {
        Segment next;
        Segment previous;
        Chunk chunk;
        boolean left;
        int y;
        int value;
    }

    private static class ChangingState {
        final Shapes<SimplePolygon> out;
        int x;
        int delta;
        Segment first;

        void touch(Segment s) {
            int oldValue = s.value;
            s.value += this.delta;
            if (oldValue <= 0 ^ s.value <= 0) {
                if (this.first == null) {
                    this.first = s;
                }
            } else {
                this.finish(s);
            }
        }

        void finish(Segment s) {
            if (this.first == null) {
                return;
            }
            if (this.first.chunk != null && s.chunk != null) {
                this.push(this.first);
                this.push(s);
                if (this.first.chunk == s.chunk) {
                    Chunk c = this.first.chunk;
                    this.first.chunk = null;
                    s.chunk = null;
                    c.left = null;
                    c.right = null;
                    this.out.getShapes().add(c);
                } else {
                    Chunk rightChunk;
                    Chunk leftChunk;
                    if (!s.left) {
                        leftChunk = s.chunk;
                        rightChunk = this.first.chunk;
                    } else {
                        leftChunk = this.first.chunk;
                        rightChunk = s.chunk;
                    }
                    log.trace("Joining {} onto {}", (Object)System.identityHashCode(rightChunk), (Object)System.identityHashCode(leftChunk));
                    if (this.first.left == s.left) {
                        log.trace("reverse");
                        if (this.first.left) {
                            leftChunk.reverse();
                        } else {
                            rightChunk.reverse();
                        }
                    }
                    log.trace("{} {}", (Object)this.first.y, (Object)s.y);
                    rightChunk.appendTo(leftChunk);
                    this.first.chunk = null;
                    s.chunk = null;
                    leftChunk.right.chunk = null;
                    rightChunk.left.chunk = null;
                    leftChunk.right = rightChunk.right;
                    leftChunk.left.chunk = leftChunk;
                    leftChunk.right.chunk = leftChunk;
                }
            } else if (this.first.chunk == null && s.chunk == null) {
                this.first.chunk = new Chunk();
                this.first.chunk.right = this.first;
                this.first.left = false;
                s.chunk = this.first.chunk;
                this.first.chunk.left = s;
                s.left = true;
                this.push(this.first);
                this.push(s);
            } else if (this.first.chunk == null) {
                this.push(s);
                this.move(this.first, s);
                this.push(this.first);
            } else {
                this.push(this.first);
                this.move(s, this.first);
                this.push(s);
            }
            this.first = null;
        }

        private void move(Segment dst, Segment src) {
            dst.chunk = src.chunk;
            dst.left = src.left;
            src.chunk = null;
            if (dst.left) {
                assert (dst.chunk.left == src);
                dst.chunk.left = dst;
            } else {
                assert (dst.chunk.right == src);
                dst.chunk.right = dst;
            }
        }

        private void push(Segment s) {
            if (s.left) {
                s.chunk.pushLeft(this.x, s.y);
                assert (s.chunk.left == s);
            } else {
                s.chunk.pushRight(this.x, s.y);
                assert (s.chunk.right == s);
            }
        }

        public ChangingState(Shapes<SimplePolygon> out) {
            this.out = out;
        }
    }

    public static class Rectangle {
        private final int x1;
        private final int y1;
        private final int x2;
        private final int y2;

        public Rectangle(int x1, int y1, int x2, int y2) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }

        public int getX1() {
            return this.x1;
        }

        public int getY1() {
            return this.y1;
        }

        public int getX2() {
            return this.x2;
        }

        public int getY2() {
            return this.y2;
        }

        public String toString() {
            return "RectangleUnion.Rectangle(x1=" + this.getX1() + ", y1=" + this.getY1() + ", x2=" + this.getX2() + ", y2=" + this.getY2() + ")";
        }
    }
}

