/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api;

public enum GameState {
    UNKNOWN(-1),
    STARTING(0),
    LOGIN_SCREEN(10),
    LOGIN_SCREEN_AUTHENTICATOR(11),
    LOGGING_IN(20),
    LOADING(25),
    LOGGED_IN(30),
    CONNECTION_LOST(40),
    HOPPING(45);

    private final int state;

    private GameState(int state) {
        this.state = state;
    }

    public static GameState of(int state) {
        for (GameState gs : GameState.values()) {
            if (gs.state != state) continue;
            return gs;
        }
        return UNKNOWN;
    }

    public int getState() {
        return this.state;
    }
}

