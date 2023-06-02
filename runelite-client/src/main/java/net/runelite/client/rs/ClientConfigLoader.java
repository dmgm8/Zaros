/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.rs;

import java.io.BufferedReader;
import java.io.IOException;
import net.runelite.client.rs.RSConfig;

interface ClientConfigLoader {
    public RSConfig fetch() throws IOException;

    default public void processResponse(RSConfig config, BufferedReader in) throws IOException {
        String str;
        block8: while ((str = in.readLine()) != null) {
            String s;
            int idx = str.indexOf(61);
            if (idx == -1) continue;
            switch (s = str.substring(0, idx)) {
                case "param": {
                    str = str.substring(idx + 1);
                    idx = str.indexOf(61);
                    s = str.substring(0, idx);
                    config.getAppletProperties().put(s, str.substring(idx + 1));
                    continue block8;
                }
                case "msg": {
                    continue block8;
                }
            }
            config.getAppletProperties().put(s, str.substring(idx + 1));
        }
    }
}

