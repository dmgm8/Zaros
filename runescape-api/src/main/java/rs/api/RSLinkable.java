/*
 * Decompiled with CFR 0.150.
 */
package rs.api;

import net.runelite.mapping.Import;

public interface RSLinkable {
    @Import(value="previous")
    public RSLinkable getPrevious();

    @Import(value="unlink")
    public void unlink();
}

