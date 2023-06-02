/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package net.runelite.api.overlay;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OverlayIndex {
    private static final Logger log = LoggerFactory.getLogger(OverlayIndex.class);
    private static final Set<Integer> overlays = new HashSet<Integer>();

    public static boolean hasOverlay(int indexId, int archiveId) {
        return overlays.contains(indexId << 16 | archiveId);
    }

    static {
        try (InputStream indexStream = OverlayIndex.class.getResourceAsStream("/runelite/index");
             DataInputStream in = new DataInputStream(indexStream);){
            int id;
            while ((id = in.readInt()) != -1) {
                overlays.add(id);
            }
        }
        catch (IOException ex) {
            log.warn("unable to load overlay index", (Throwable)ex);
        }
    }
}

