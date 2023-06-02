/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.skillcalculator.skills;

import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.skillcalculator.skills.NamedSkillAction;

public enum AgilityAction implements NamedSkillAction
{
    GNOME_STRONGHOLD_COURSE("Gnome Stronghold", 1, 86.5f, 2150),
    SHAYZIEN_BASIC_COURSE("Shayzien Basic Course", 5, 133.2f, 13359),
    DRAYNOR_VILLAGE_ROOFTOP("Draynor Village Rooftop", 10, 120.0f, 11849),
    LEAPING_TROUT("Leaping trout", 15, 5.0f, 11328),
    AL_KHARID_ROOFTOP("Al Kharid Rooftop", 20, 180.0f, 11849),
    VARROCK_ROOFTOP("Varrock Rooftop", 30, 238.0f, 11849),
    PENGUIN_AGILITY_COURSE("Penguin Agility Course", 30, 540.0f, 10595),
    LEAPING_SALMON("Leaping salmon", 30, 6.0f, 11330),
    BARBARIAN_OUTPOST("Barbarian Outpost", 35, 152.5f, 1365),
    CANIFIS_ROOFTOP("Canifis Rooftop", 40, 240.0f, 11849),
    LEAPING_STURGEON("Leaping sturgeon", 45, 7.0f, 11332),
    APE_ATOLL_COURSE("Ape Atoll", 48, 580.0f, 4026),
    SHAYZIEN_ADVANCED_COURSE("Shayzien Advanced Course", 48, 474.3f, 13379),
    FALADOR_ROOFTOP("Falador Rooftop", 50, 440.0f, 11849),
    WILDERNESS_AGILITY_COURSE("Wilderness Agility Course", 52, 571.0f, 964),
    HALLOWED_SEPULCHRE_FLOOR_1("Hallowed Sepulchre Floor 1", 52, 575.0f, 24736),
    SEERS_VILLAGE_ROOFTOP("Seers' Village Rooftop", 60, 570.0f, 11849),
    WEREWOLF_AGILITY_COURSE("Werewolf Agility Course", 60, 730.0f, 4179),
    HALLOWED_SEPULCHRE_FLOOR_2("Hallowed Sepulchre Floor 2", 62, 925.0f, 24736),
    POLLNIVNEACH_ROOFTOP("Pollnivneach Rooftop", 70, 890.0f, 11849),
    HALLOWED_SEPULCHRE_FLOOR_3("Hallowed Sepulchre Floor 3", 72, 1500.0f, 24736),
    PRIFDDINAS_AGILITY_COURSE("Prifddinas Agility Course", 75, 1337.0f, 23962),
    RELLEKKA_ROOFTOP("Rellekka Rooftop", 80, 780.0f, 11849),
    HALLOWED_SEPULCHRE_FLOOR_4("Hallowed Sepulchre Floor 4", 82, 2700.0f, 24736),
    ARDOUGNE_ROOFTOP("Ardougne Rooftop", 90, 793.0f, 11849),
    HALLOWED_SEPULCHRE_FLOOR_5("Hallowed Sepulchre Floor 5", 92, 6000.0f, 24736);

    private final String name;
    private final int level;
    private final float xp;
    private final int icon;

    @Override
    public boolean isMembers(ItemManager itemManager) {
        return true;
    }

    private AgilityAction(String name, int level, float xp, int icon) {
        this.name = name;
        this.level = level;
        this.xp = xp;
        this.icon = icon;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getLevel() {
        return this.level;
    }

    @Override
    public float getXp() {
        return this.xp;
    }

    @Override
    public int getIcon() {
        return this.icon;
    }
}

