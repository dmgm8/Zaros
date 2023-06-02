/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.ui.components;

import java.awt.Component;
import java.awt.Container;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.dnd.DragSource;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BoxLayout;
import javax.swing.JLayeredPane;
import javax.swing.SwingUtilities;

public class DragAndDropReorderPane
extends JLayeredPane {
    private Point dragStartPoint;
    private Component draggingComponent;
    private int dragYOffset = 0;
    private int dragIndex = -1;

    public DragAndDropReorderPane() {
        this.setLayout(new DragAndDropReorderLayoutManager());
        DragAndDropReorderMouseAdapter mouseAdapter = new DragAndDropReorderMouseAdapter();
        this.addMouseListener(mouseAdapter);
        this.addMouseMotionListener(mouseAdapter);
    }

    @Override
    public void setLayout(LayoutManager layoutManager) {
        if (layoutManager != null && !(layoutManager instanceof DragAndDropReorderLayoutManager)) {
            throw new IllegalArgumentException("DragAndDropReorderPane only supports DragAndDropReorderLayoutManager");
        }
        super.setLayout(layoutManager);
    }

    private void startDragging(Point point) {
        this.draggingComponent = this.getDefaultLayerComponentAt(this.dragStartPoint);
        if (this.draggingComponent == null) {
            this.dragStartPoint = null;
            return;
        }
        this.dragYOffset = SwingUtilities.convertPoint((Component)this, (Point)this.dragStartPoint, (Component)this.draggingComponent).y;
        this.dragIndex = this.getPosition(this.draggingComponent);
        this.setLayer(this.draggingComponent, DRAG_LAYER);
        this.moveDraggingComponent(point);
    }

    private void drag(Point point) {
        this.moveDraggingComponent(point);
        Point draggingComponentMidPoint = SwingUtilities.convertPoint(this.draggingComponent, new Point(this.draggingComponent.getWidth() / 2, this.draggingComponent.getHeight() / 2), this);
        Component component = this.getDefaultLayerComponentAt(draggingComponentMidPoint);
        if (component != null) {
            int index = this.getPosition(component);
            this.dragIndex = index < this.dragIndex ? index : index + 1;
            this.revalidate();
        }
    }

    private void finishDragging() {
        if (this.draggingComponent != null) {
            this.setLayer(this.draggingComponent, DEFAULT_LAYER, this.dragIndex);
            this.draggingComponent = null;
            this.dragYOffset = 0;
            this.dragIndex = -1;
            this.revalidate();
        }
        this.dragStartPoint = null;
    }

    private void moveDraggingComponent(Point point) {
        int y = point.y - this.dragYOffset;
        y = Math.max(y, 0);
        y = Math.min(y, this.getHeight() - this.draggingComponent.getHeight());
        this.draggingComponent.setLocation(new Point(0, y));
    }

    private Component getDefaultLayerComponentAt(Point point) {
        for (Component component : this.getComponentsInLayer(DEFAULT_LAYER)) {
            if (!component.contains(point.x - component.getX(), point.y - component.getY())) continue;
            return component;
        }
        return null;
    }

    private class DragAndDropReorderMouseAdapter
    extends MouseAdapter {
        private DragAndDropReorderMouseAdapter() {
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (SwingUtilities.isLeftMouseButton(e) && DragAndDropReorderPane.this.getComponentCount() > 1) {
                DragAndDropReorderPane.this.dragStartPoint = e.getPoint();
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (SwingUtilities.isLeftMouseButton(e) && DragAndDropReorderPane.this.dragStartPoint != null) {
                Point point = e.getPoint();
                if (DragAndDropReorderPane.this.draggingComponent != null) {
                    DragAndDropReorderPane.this.drag(point);
                } else if (point.distance(DragAndDropReorderPane.this.dragStartPoint) > (double)DragSource.getDragThreshold()) {
                    DragAndDropReorderPane.this.startDragging(point);
                }
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (SwingUtilities.isLeftMouseButton(e)) {
                DragAndDropReorderPane.this.finishDragging();
            }
        }
    }

    private class DragAndDropReorderLayoutManager
    extends BoxLayout {
        private DragAndDropReorderLayoutManager() {
            super(DragAndDropReorderPane.this, 1);
        }

        @Override
        public void layoutContainer(Container target) {
            if (DragAndDropReorderPane.this.draggingComponent != null) {
                Point location = DragAndDropReorderPane.this.draggingComponent.getLocation();
                DragAndDropReorderPane.this.setLayer(DragAndDropReorderPane.this.draggingComponent, JLayeredPane.DEFAULT_LAYER, DragAndDropReorderPane.this.dragIndex);
                super.layoutContainer(target);
                DragAndDropReorderPane.this.setLayer(DragAndDropReorderPane.this.draggingComponent, JLayeredPane.DRAG_LAYER);
                DragAndDropReorderPane.this.draggingComponent.setLocation(location);
            } else {
                super.layoutContainer(target);
            }
        }
    }
}

