/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.util;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;

public class AsyncBufferedImage
extends BufferedImage {
    private final List<Runnable> listeners = new ArrayList<Runnable>();
    private boolean loaded;

    public AsyncBufferedImage(int width, int height, int imageType) {
        super(width, height, imageType);
    }

    public synchronized void loaded() {
        this.loaded = true;
        for (Runnable r : this.listeners) {
            r.run();
        }
        this.listeners.clear();
    }

    public synchronized void onLoaded(Runnable r) {
        if (this.loaded) {
            return;
        }
        this.listeners.add(r);
    }

    public void addTo(JButton c) {
        c.setIcon(this.makeIcon(c));
    }

    public void addTo(JLabel c) {
        c.setIcon(this.makeIcon(c));
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private ImageIcon makeIcon(JComponent c) {
        AsyncBufferedImage asyncBufferedImage = this;
        synchronized (asyncBufferedImage) {
            if (!this.loaded) {
                this.listeners.add(c::repaint);
            }
        }
        return new ImageIcon(this);
    }
}

