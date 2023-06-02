/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.rs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import net.runelite.client.rs.ClientConfigLoader;
import net.runelite.client.rs.RSConfig;

public class LocalClientConfigLoader
implements ClientConfigLoader {
    @Override
    public RSConfig fetch() throws IOException {
        RSConfig config = new RSConfig();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(ClientConfigLoader.class.getResourceAsStream("/jav_config.ws"), StandardCharsets.UTF_8));){
            this.processResponse(config, in);
        }
        return config;
    }
}

