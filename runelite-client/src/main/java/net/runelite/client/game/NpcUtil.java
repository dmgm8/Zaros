/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  javax.inject.Inject
 *  javax.inject.Singleton
 *  net.runelite.api.Actor
 *  net.runelite.api.NPC
 *  net.runelite.api.NPCComposition
 *  net.runelite.api.events.AnimationChanged
 *  net.runelite.api.events.NpcChanged
 *  org.apache.commons.lang3.ArrayUtils
 */
package net.runelite.client.game;

import java.util.Set;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.Actor;
import net.runelite.api.NPC;
import net.runelite.api.NPCComposition;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.NpcChanged;
import net.runelite.client.RuntimeConfig;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import org.apache.commons.lang3.ArrayUtils;

@Singleton
public class NpcUtil {
    private final RuntimeConfig runtimeConfig;

    @Inject
    private NpcUtil(@Nullable RuntimeConfig runtimeConfig, EventBus eventBus) {
        this.runtimeConfig = runtimeConfig;
        eventBus.register(this);
    }

    public boolean isDying(NPC npc) {
        NPCComposition npcComposition;
        int id = npc.getId();
        switch (id) {
            case 412: 
            case 413: 
            case 421: 
            case 422: 
            case 458: 
            case 459: 
            case 460: 
            case 461: 
            case 462: 
            case 463: 
            case 476: 
            case 537: 
            case 682: 
            case 963: 
            case 1024: 
            case 1072: 
            case 1073: 
            case 1134: 
            case 1475: 
            case 1543: 
            case 1605: 
            case 1606: 
            case 1607: 
            case 1608: 
            case 3897: 
            case 3898: 
            case 3899: 
            case 3996: 
            case 3997: 
            case 3998: 
            case 4863: 
            case 5353: 
            case 5354: 
            case 6333: 
            case 6334: 
            case 6335: 
            case 6337: 
            case 6338: 
            case 6346: 
            case 6394: 
            case 6395: 
            case 6396: 
            case 6398: 
            case 6399: 
            case 6477: 
            case 6594: 
            case 6611: 
            case 7234: 
            case 7392: 
            case 7407: 
            case 7408: 
            case 7797: 
            case 7849: 
            case 7850: 
            case 7851: 
            case 7852: 
            case 7853: 
            case 7854: 
            case 7855: 
            case 7882: 
            case 7883: 
            case 7884: 
            case 7885: 
            case 7886: 
            case 7887: 
            case 7888: 
            case 7889: 
            case 8194: 
            case 10955: 
            case 10956: {
                return false;
            }
            case 8341: 
            case 8613: 
            case 8622: 
            case 9424: 
            case 9433: 
            case 10401: 
            case 10769: 
            case 10773: {
                return true;
            }
            case 9050: {
                return npc.isDead();
            }
        }
        if (this.runtimeConfig != null) {
            Set<Integer> ignoredNpcs = this.runtimeConfig.getIgnoreDeadNpcs();
            if (ignoredNpcs != null && ignoredNpcs.contains(id)) {
                return false;
            }
            Set<Integer> forceDeadNpcs = this.runtimeConfig.getForceDeadNpcs();
            if (forceDeadNpcs != null && forceDeadNpcs.contains(id)) {
                return true;
            }
            Set<Integer> pureIsDeadNpcs = this.runtimeConfig.getNonAttackNpcs();
            if (pureIsDeadNpcs != null && pureIsDeadNpcs.contains(id)) {
                return npc.isDead();
            }
        }
        boolean hasAttack = (npcComposition = npc.getTransformedComposition()) != null && ArrayUtils.contains((Object[])npcComposition.getActions(), (Object)"Attack");
        return hasAttack && npc.isDead();
    }

    @Subscribe
    void onNpcChanged(NpcChanged e) {
        NPC npc = e.getNpc();
        int id = npc.getId();
        switch (id) {
            case 377: 
            case 378: 
            case 683: 
            case 965: 
            case 1074: 
            case 1135: 
            case 1609: 
            case 3900: 
            case 3999: 
            case 5355: 
            case 6336: 
            case 6339: 
            case 6347: 
            case 6397: 
            case 6400: 
            case 6476: 
            case 6612: 
            case 7288: 
            case 7290: 
            case 7292: 
            case 7294: {
                npc.setDead(false);
                break;
            }
            default: {
                Set<Integer> resetDeadOnChangeNpcs;
                if (this.runtimeConfig == null || (resetDeadOnChangeNpcs = this.runtimeConfig.getResetDeadOnChangeNpcs()) == null || !resetDeadOnChangeNpcs.contains(id)) break;
                npc.setDead(false);
            }
        }
    }

    @Subscribe
    public void onAnimationChanged(AnimationChanged animationChanged) {
        Actor actor = animationChanged.getActor();
        int anim = actor.getAnimation();
        switch (anim) {
            case 1676: {
                if (!(actor instanceof NPC) || ((NPC)actor).getId() != 319) break;
            }
            case 7992: 
            case 8000: 
            case 8006: 
            case 8078: 
            case 8097: {
                actor.setDead(true);
                break;
            }
            default: {
                Set<Integer> forceDeadAnimations;
                if (this.runtimeConfig == null || (forceDeadAnimations = this.runtimeConfig.getForceDeadAnimations()) == null || !forceDeadAnimations.contains(anim)) break;
                actor.setDead(true);
            }
        }
    }
}

