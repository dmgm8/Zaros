/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.inject.Singleton
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package net.runelite.client.plugins.motherlode;

import java.time.Duration;
import java.time.Instant;
import javax.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class MotherlodeSession {
    private static final Logger log = LoggerFactory.getLogger(MotherlodeSession.class);
    private static final Duration HOUR = Duration.ofHours(1L);
    private int perHour;
    private Instant lastPayDirtMined;
    private int totalMined;
    private Instant recentPayDirtMined;
    private int recentMined;
    private Instant lastGemFound;
    private int diamondsFound;
    private int rubiesFound;
    private int emeraldsFound;
    private int sapphiresFound;
    private int nuggetsFound;
    private int coalFound;
    private int goldFound;
    private int mithrilFound;
    private int adamantiteFound;
    private int runiteFound;

    void incrementGemFound(int gemID) {
        this.lastGemFound = Instant.now();
        switch (gemID) {
            case 1617: {
                ++this.diamondsFound;
                break;
            }
            case 1619: {
                ++this.rubiesFound;
                break;
            }
            case 1621: {
                ++this.emeraldsFound;
                break;
            }
            case 1623: {
                ++this.sapphiresFound;
                break;
            }
            default: {
                log.debug("Invalid gem type specified. The gem count will not be incremented.");
            }
        }
    }

    void updateOreFound(int item, int count) {
        switch (item) {
            case 12012: {
                this.nuggetsFound += count;
                break;
            }
            case 453: {
                this.coalFound += count;
                break;
            }
            case 444: {
                this.goldFound += count;
                break;
            }
            case 447: {
                this.mithrilFound += count;
                break;
            }
            case 449: {
                this.adamantiteFound += count;
                break;
            }
            case 451: {
                this.runiteFound += count;
                break;
            }
            default: {
                log.debug("Invalid ore specified. The ore count will not be updated.");
            }
        }
    }

    public void incrementPayDirtMined() {
        Instant now;
        this.lastPayDirtMined = now = Instant.now();
        ++this.totalMined;
        if (this.recentMined == 0) {
            this.recentPayDirtMined = now;
        }
        ++this.recentMined;
        Duration timeSinceStart = Duration.between(this.recentPayDirtMined, now);
        if (!timeSinceStart.isZero()) {
            this.perHour = (int)((double)this.recentMined * (double)HOUR.toMillis() / (double)timeSinceStart.toMillis());
        }
    }

    public void resetRecent() {
        this.recentPayDirtMined = null;
        this.recentMined = 0;
    }

    public int getPerHour() {
        return this.perHour;
    }

    public Instant getLastPayDirtMined() {
        return this.lastPayDirtMined;
    }

    public int getTotalMined() {
        return this.totalMined;
    }

    public Instant getRecentPayDirtMined() {
        return this.recentPayDirtMined;
    }

    public int getRecentMined() {
        return this.recentMined;
    }

    Instant getLastGemFound() {
        return this.lastGemFound;
    }

    int getDiamondsFound() {
        return this.diamondsFound;
    }

    int getRubiesFound() {
        return this.rubiesFound;
    }

    int getEmeraldsFound() {
        return this.emeraldsFound;
    }

    int getSapphiresFound() {
        return this.sapphiresFound;
    }

    int getNuggetsFound() {
        return this.nuggetsFound;
    }

    int getCoalFound() {
        return this.coalFound;
    }

    int getGoldFound() {
        return this.goldFound;
    }

    int getMithrilFound() {
        return this.mithrilFound;
    }

    int getAdamantiteFound() {
        return this.adamantiteFound;
    }

    int getRuniteFound() {
        return this.runiteFound;
    }
}

