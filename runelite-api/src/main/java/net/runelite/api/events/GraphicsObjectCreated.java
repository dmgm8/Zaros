/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api.events;

import net.runelite.api.GraphicsObject;

public final class GraphicsObjectCreated {
    private final GraphicsObject graphicsObject;

    public GraphicsObjectCreated(GraphicsObject graphicsObject) {
        this.graphicsObject = graphicsObject;
    }

    public GraphicsObject getGraphicsObject() {
        return this.graphicsObject;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof GraphicsObjectCreated)) {
            return false;
        }
        GraphicsObjectCreated other = (GraphicsObjectCreated)o;
        GraphicsObject this$graphicsObject = this.getGraphicsObject();
        GraphicsObject other$graphicsObject = other.getGraphicsObject();
        return !(this$graphicsObject == null ? other$graphicsObject != null : !this$graphicsObject.equals(other$graphicsObject));
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        GraphicsObject $graphicsObject = this.getGraphicsObject();
        result = result * 59 + ($graphicsObject == null ? 43 : $graphicsObject.hashCode());
        return result;
    }

    public String toString() {
        return "GraphicsObjectCreated(graphicsObject=" + this.getGraphicsObject() + ")";
    }
}

