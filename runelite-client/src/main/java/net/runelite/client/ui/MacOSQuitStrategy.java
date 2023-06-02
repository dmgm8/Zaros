/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.apple.eawt.Application
 *  com.apple.eawt.QuitStrategy
 */
package net.runelite.client.ui;

import com.apple.eawt.Application;
import com.apple.eawt.QuitStrategy;

/*
 * Multiple versions of this class in jar - see https://www.benf.org/other/cfr/multi-version-jar.html
 */
class MacOSQuitStrategy {
    MacOSQuitStrategy() {
    }

    public static void setup() {
        try {
            Application.getApplication().setQuitStrategy(QuitStrategy.CLOSE_ALL_WINDOWS);
        }
        catch (NoClassDefFoundError noClassDefFoundError) {
            // empty catch block
        }
    }
}

