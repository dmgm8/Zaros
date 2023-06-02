/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.jocl.NativePointerObject
 *  org.jocl.Pointer
 *  org.jocl.cl_mem
 */
package net.runelite.client.plugins.gpu;

import org.jocl.NativePointerObject;
import org.jocl.Pointer;
import org.jocl.cl_mem;

class GLBuffer {
    int glBufferId = -1;
    int size = -1;
    cl_mem cl_mem;

    GLBuffer() {
    }

    Pointer ptr() {
        return this.cl_mem != null ? Pointer.to((NativePointerObject)this.cl_mem) : null;
    }
}

