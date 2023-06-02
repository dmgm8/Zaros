/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  javax.inject.Inject
 *  javax.inject.Singleton
 *  net.runelite.api.Client
 *  net.runelite.api.GameState
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package net.runelite.client;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.client.SessionClient;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ClientShutdown;
import net.runelite.client.util.RunnableExceptionLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class ClientSessionManager {
    private static final Logger log = LoggerFactory.getLogger(ClientSessionManager.class);
    private final ScheduledExecutorService executorService;
    private final Client client;
    private final SessionClient sessionClient;
    private ScheduledFuture<?> scheduledFuture;
    private UUID sessionId;

    @Inject
    ClientSessionManager(ScheduledExecutorService executorService, @Nullable Client client, SessionClient sessionClient) {
        this.executorService = executorService;
        this.client = client;
        this.sessionClient = sessionClient;
    }

    public void start() {
        this.executorService.execute(() -> {
            try {
                this.sessionId = this.sessionClient.open();
                log.debug("Opened session {}", this.sessionId);
            }
            catch (IOException ex) {
                log.warn("error opening session", ex);
            }
        });
        this.scheduledFuture = this.executorService.scheduleWithFixedDelay(RunnableExceptionLogger.wrap(this::ping), 1L, 10L, TimeUnit.MINUTES);
    }

    @Subscribe
    private void onClientShutdown(ClientShutdown e) {
        this.scheduledFuture.cancel(true);
        e.waitFor(this.executorService.submit(() -> {
            try {
                UUID localUuid = this.sessionId;
                if (localUuid != null) {
                    this.sessionClient.delete(localUuid);
                }
            }
            catch (IOException ex) {
                log.warn(null, ex);
            }
            this.sessionId = null;
        }));
    }

    private void ping() {
        try {
            if (this.sessionId == null) {
                this.sessionId = this.sessionClient.open();
                log.debug("Opened session {}", this.sessionId);
                return;
            }
        }
        catch (IOException ex) {
            log.warn("unable to open session", ex);
            return;
        }
        boolean loggedIn = false;
        if (this.client != null) {
            GameState gameState = this.client.getGameState();
            loggedIn = gameState.getState() >= GameState.LOADING.getState();
        }
        try {
            this.sessionClient.ping(this.sessionId, loggedIn);
        }
        catch (IOException ex) {
            log.warn("Resetting session", ex);
            this.sessionId = null;
        }
    }
}

