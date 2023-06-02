/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.rs;

import java.applet.Applet;
import java.applet.AppletContext;
import java.applet.AppletStub;
import java.applet.AudioClip;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;
import javax.swing.SwingUtilities;
import net.runelite.client.RuntimeConfig;
import net.runelite.client.RuntimeConfigLoader;
import net.runelite.client.rs.RSConfig;
import net.runelite.client.ui.FatalErrorDialog;

class RSAppletStub
implements AppletStub {
    private final RSConfig config;
    private final RuntimeConfigLoader runtimeConfigLoader;

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public URL getDocumentBase() {
        return this.getCodeBase();
    }

    @Override
    public URL getCodeBase() {
        try {
            return new URL(this.config.getCodeBase());
        }
        catch (MalformedURLException ex) {
            return null;
        }
    }

    @Override
    public String getParameter(String name) {
        return this.config.getAppletProperties().get(name);
    }

    @Override
    public AppletContext getAppletContext() {
        return new AppletContext(){

            @Override
            public AudioClip getAudioClip(URL url) {
                return null;
            }

            @Override
            public Image getImage(URL url) {
                return null;
            }

            @Override
            public Applet getApplet(String name) {
                return null;
            }

            @Override
            public Enumeration<Applet> getApplets() {
                return null;
            }

            @Override
            public void showDocument(URL url) {
                if (url.getPath().startsWith("/error_game_")) {
                    try {
                        RuntimeConfig rtc;
                        if (RSAppletStub.this.runtimeConfigLoader != null && (rtc = RSAppletStub.this.runtimeConfigLoader.get()).showOutageMessage()) {
                            return;
                        }
                    }
                    catch (Exception rtc) {
                        // empty catch block
                    }
                    String code = url.getPath().replace("/", "").replace(".ws", "");
                    SwingUtilities.invokeLater(() -> new FatalErrorDialog("OldSchool RuneScape has crashed with the message: " + code + "").setTitle("RuneLite", "OldSchool RuneScape has crashed").addHelpButtons().open());
                }
            }

            @Override
            public void showDocument(URL url, String target) {
                this.showDocument(url);
            }

            @Override
            public void showStatus(String status) {
            }

            @Override
            public void setStream(String key, InputStream stream) throws IOException {
            }

            @Override
            public InputStream getStream(String key) {
                return null;
            }

            @Override
            public Iterator<String> getStreamKeys() {
                return null;
            }
        };
    }

    @Override
    public void appletResize(int width, int height) {
    }

    public RSAppletStub(RSConfig config, RuntimeConfigLoader runtimeConfigLoader) {
        this.config = config;
        this.runtimeConfigLoader = runtimeConfigLoader;
    }
}

