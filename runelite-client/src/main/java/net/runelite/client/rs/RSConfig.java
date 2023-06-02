/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.rs;

import java.util.HashMap;
import java.util.Map;

class RSConfig {
    private final Map<String, String> appletProperties = new HashMap<String, String>();

    RSConfig() {
    }

    String getCodeBase() {
        return this.appletProperties.get("codebase");
    }

    void setCodebase(String codebase) {
        this.appletProperties.put("codebase", codebase);
    }

    String getInitialJar() {
        return this.appletProperties.get("initial_jar");
    }

    String getInitialClass() {
        return this.appletProperties.get("initial_class").replace(".class", "");
    }

    boolean isFallback() {
        return this.getRuneLiteGamepack() != null;
    }

    String getRuneLiteGamepack() {
        return this.appletProperties.get("runelite.gamepack");
    }

    String getRuneLiteWorldParam() {
        return this.appletProperties.get("runelite.worldparam");
    }

    public Map<String, String> getAppletProperties() {
        return this.appletProperties;
    }
}

