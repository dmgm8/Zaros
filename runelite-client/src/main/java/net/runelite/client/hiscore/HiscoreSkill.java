/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.hiscore;

import net.runelite.client.hiscore.HiscoreSkillType;

public enum HiscoreSkill {
    OVERALL("Overall", HiscoreSkillType.OVERALL),
    ATTACK("Attack", HiscoreSkillType.SKILL),
    DEFENCE("Defence", HiscoreSkillType.SKILL),
    STRENGTH("Strength", HiscoreSkillType.SKILL),
    HITPOINTS("Hitpoints", HiscoreSkillType.SKILL),
    RANGED("Ranged", HiscoreSkillType.SKILL),
    PRAYER("Prayer", HiscoreSkillType.SKILL),
    MAGIC("Magic", HiscoreSkillType.SKILL),
    COOKING("Cooking", HiscoreSkillType.SKILL),
    WOODCUTTING("Woodcutting", HiscoreSkillType.SKILL),
    FLETCHING("Fletching", HiscoreSkillType.SKILL),
    FISHING("Fishing", HiscoreSkillType.SKILL),
    FIREMAKING("Firemaking", HiscoreSkillType.SKILL),
    CRAFTING("Crafting", HiscoreSkillType.SKILL),
    SMITHING("Smithing", HiscoreSkillType.SKILL),
    MINING("Mining", HiscoreSkillType.SKILL),
    HERBLORE("Herblore", HiscoreSkillType.SKILL),
    AGILITY("Agility", HiscoreSkillType.SKILL),
    THIEVING("Thieving", HiscoreSkillType.SKILL),
    SLAYER("Slayer", HiscoreSkillType.SKILL),
    FARMING("Farming", HiscoreSkillType.SKILL),
    RUNECRAFT("Runecraft", HiscoreSkillType.SKILL),
    HUNTER("Hunter", HiscoreSkillType.SKILL),
    CONSTRUCTION("Construction", HiscoreSkillType.SKILL),
    LEAGUE_POINTS("League Points", HiscoreSkillType.ACTIVITY),
    BOUNTY_HUNTER_HUNTER("Bounty Hunter - Hunter", HiscoreSkillType.ACTIVITY),
    BOUNTY_HUNTER_ROGUE("Bounty Hunter - Rogue", HiscoreSkillType.ACTIVITY),
    CLUE_SCROLL_ALL("Clue Scrolls (all)", HiscoreSkillType.ACTIVITY),
    CLUE_SCROLL_BEGINNER("Clue Scrolls (beginner)", HiscoreSkillType.ACTIVITY),
    CLUE_SCROLL_EASY("Clue Scrolls (easy)", HiscoreSkillType.ACTIVITY),
    CLUE_SCROLL_MEDIUM("Clue Scrolls (medium)", HiscoreSkillType.ACTIVITY),
    CLUE_SCROLL_HARD("Clue Scrolls (hard)", HiscoreSkillType.ACTIVITY),
    CLUE_SCROLL_ELITE("Clue Scrolls (elite)", HiscoreSkillType.ACTIVITY),
    CLUE_SCROLL_MASTER("Clue Scrolls (master)", HiscoreSkillType.ACTIVITY),
    LAST_MAN_STANDING("Last Man Standing", HiscoreSkillType.ACTIVITY),
    PVP_ARENA_RANK("PvP Arena - Rank", HiscoreSkillType.ACTIVITY),
    SOUL_WARS_ZEAL("Soul Wars Zeal", HiscoreSkillType.ACTIVITY),
    RIFTS_CLOSED("Rifts closed", HiscoreSkillType.ACTIVITY),
    ABYSSAL_SIRE("Abyssal Sire", HiscoreSkillType.BOSS),
    ALCHEMICAL_HYDRA("Alchemical Hydra", HiscoreSkillType.BOSS),
    BARROWS_CHESTS("Barrows Chests", HiscoreSkillType.BOSS),
    BRYOPHYTA("Bryophyta", HiscoreSkillType.BOSS),
    CALLISTO("Callisto", HiscoreSkillType.BOSS),
    CERBERUS("Cerberus", HiscoreSkillType.BOSS),
    CHAMBERS_OF_XERIC("Chambers of Xeric", HiscoreSkillType.BOSS),
    CHAMBERS_OF_XERIC_CHALLENGE_MODE("Chambers of Xeric: Challenge Mode", HiscoreSkillType.BOSS),
    CHAOS_ELEMENTAL("Chaos Elemental", HiscoreSkillType.BOSS),
    CHAOS_FANATIC("Chaos Fanatic", HiscoreSkillType.BOSS),
    COMMANDER_ZILYANA("Commander Zilyana", HiscoreSkillType.BOSS),
    CORPOREAL_BEAST("Corporeal Beast", HiscoreSkillType.BOSS),
    CRAZY_ARCHAEOLOGIST("Crazy Archaeologist", HiscoreSkillType.BOSS),
    DAGANNOTH_PRIME("Dagannoth Prime", HiscoreSkillType.BOSS),
    DAGANNOTH_REX("Dagannoth Rex", HiscoreSkillType.BOSS),
    DAGANNOTH_SUPREME("Dagannoth Supreme", HiscoreSkillType.BOSS),
    DERANGED_ARCHAEOLOGIST("Deranged Archaeologist", HiscoreSkillType.BOSS),
    GENERAL_GRAARDOR("General Graardor", HiscoreSkillType.BOSS),
    GIANT_MOLE("Giant Mole", HiscoreSkillType.BOSS),
    GROTESQUE_GUARDIANS("Grotesque Guardians", HiscoreSkillType.BOSS),
    HESPORI("Hespori", HiscoreSkillType.BOSS),
    KALPHITE_QUEEN("Kalphite Queen", HiscoreSkillType.BOSS),
    KING_BLACK_DRAGON("King Black Dragon", HiscoreSkillType.BOSS),
    KRAKEN("Kraken", HiscoreSkillType.BOSS),
    KREEARRA("Kree'arra", HiscoreSkillType.BOSS),
    KRIL_TSUTSAROTH("K'ril Tsutsaroth", HiscoreSkillType.BOSS),
    MIMIC("Mimic", HiscoreSkillType.BOSS),
    NEX("Nex", HiscoreSkillType.BOSS),
    NIGHTMARE("Nightmare", HiscoreSkillType.BOSS),
    PHOSANIS_NIGHTMARE("Phosani's Nightmare", HiscoreSkillType.BOSS),
    OBOR("Obor", HiscoreSkillType.BOSS),
    SARACHNIS("Sarachnis", HiscoreSkillType.BOSS),
    SCORPIA("Scorpia", HiscoreSkillType.BOSS),
    SKOTIZO("Skotizo", HiscoreSkillType.BOSS),
    TEMPOROSS("Tempoross", HiscoreSkillType.BOSS),
    THE_GAUNTLET("The Gauntlet", HiscoreSkillType.BOSS),
    THE_CORRUPTED_GAUNTLET("The Corrupted Gauntlet", HiscoreSkillType.BOSS),
    THEATRE_OF_BLOOD("Theatre of Blood", HiscoreSkillType.BOSS),
    THEATRE_OF_BLOOD_HARD_MODE("Theatre of Blood: Hard Mode", HiscoreSkillType.BOSS),
    THERMONUCLEAR_SMOKE_DEVIL("Thermonuclear Smoke Devil", HiscoreSkillType.BOSS),
    TOMBS_OF_AMASCUT("Tombs of Amascut", HiscoreSkillType.BOSS),
    TOMBS_OF_AMASCUT_EXPERT("Tombs of Amascut: Expert Mode", HiscoreSkillType.BOSS),
    TZKAL_ZUK("TzKal-Zuk", HiscoreSkillType.BOSS),
    TZTOK_JAD("TzTok-Jad", HiscoreSkillType.BOSS),
    VENENATIS("Venenatis", HiscoreSkillType.BOSS),
    VETION("Vet'ion", HiscoreSkillType.BOSS),
    VORKATH("Vorkath", HiscoreSkillType.BOSS),
    WINTERTODT("Wintertodt", HiscoreSkillType.BOSS),
    ZALCANO("Zalcano", HiscoreSkillType.BOSS),
    ZULRAH("Zulrah", HiscoreSkillType.BOSS);

    private final String name;
    private final HiscoreSkillType type;

    private HiscoreSkill(String name, HiscoreSkillType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return this.name;
    }

    public HiscoreSkillType getType() {
        return this.type;
    }
}

