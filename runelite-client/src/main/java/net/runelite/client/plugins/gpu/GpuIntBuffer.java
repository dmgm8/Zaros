/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.gpu;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

class GpuIntBuffer {
    private IntBuffer buffer = GpuIntBuffer.allocateDirect(65536);

    GpuIntBuffer() {
    }

    void put(int x, int y, int z) {
        this.buffer.put(x).put(y).put(z);
    }

    void put(int x, int y, int z, int c) {
        this.buffer.put(x).put(y).put(z).put(c);
    }

    void flip() {
        this.buffer.flip();
    }

    void clear() {
        this.buffer.clear();
    }

    void ensureCapacity(int size) {
        int position;
        int capacity = this.buffer.capacity();
        if (capacity - (position = this.buffer.position()) < size) {
            while ((capacity *= 2) - position < size) {
            }
            IntBuffer newB = GpuIntBuffer.allocateDirect(capacity);
            this.buffer.flip();
            newB.put(this.buffer);
            this.buffer = newB;
        }
    }

    IntBuffer getBuffer() {
        return this.buffer;
    }

    static IntBuffer allocateDirect(int size) {
        return ByteBuffer.allocateDirect(size * 4).order(ByteOrder.nativeOrder()).asIntBuffer();
    }
}

