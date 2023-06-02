/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  com.google.common.collect.ImmutableList
 *  com.google.common.collect.ImmutableMap$Builder
 *  javax.annotation.Nullable
 */
package net.runelite.client.plugins.slayer;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.List;
import java.util.Map;
import java.util.function.IntPredicate;
import javax.annotation.Nullable;

enum Task {
    ABERRANT_SPECTRES("Aberrant spectres", 4144, "Spectre"),
    ABYSSAL_DEMONS("Abyssal demons", 4149, xp -> xp != 50),
    ABYSSAL_SIRE("Abyssal Sire", 13262, xp -> xp != 50),
    ADAMANT_DRAGONS("Adamant dragons", 23270, new String[0]),
    ALCHEMICAL_HYDRA("Alchemical Hydra", 22746, new String[0]),
    ANKOU("Ankou", 20095, new String[0]),
    AVIANSIES("Aviansies", 13504, new String[0]),
    BANDITS("Bandits", 4625, "Bandit", "Black Heather", "Donny the Lad", "Speedy Keith"),
    BANSHEES("Banshees", 4135, new String[0]),
    BARROWS_BROTHERS("Barrows Brothers", 4732, new String[0]),
    BASILISKS("Basilisks", 4139, new String[0]),
    BATS("Bats", 20875, "Death wing"),
    BEARS("Bears", 13462, new String[0]),
    BIRDS("Birds", 314, "Chicken", "Rooster", "Terrorbird", "Seagull", "Vulture", "Duck", "Penguin"),
    BLACK_DEMONS("Black demons", 20026, new String[0]),
    BLACK_DRAGONS("Black dragons", 12524, "Baby black dragon"),
    BLACK_KNIGHTS("Black Knights", 1165, "Black Knight"),
    BLOODVELD("Bloodveld", 4141, new String[0]),
    BLUE_DRAGONS("Blue dragons", 12520, "Baby blue dragon"),
    BRINE_RATS("Brine rats", 11047, new String[0]),
    BRONZE_DRAGONS("Bronze dragons", 12363, new String[0]),
    CALLISTO("Callisto", 13178, new String[0]),
    CATABLEPON("Catablepon", 9008, new String[0]),
    CAVE_BUGS("Cave bugs", 4521, new String[0]),
    CAVE_CRAWLERS("Cave crawlers", 4134, "Chasm crawler"),
    CAVE_HORRORS("Cave horrors", 8900, "Cave abomination"),
    CAVE_KRAKEN("Cave kraken", 3272, new String[0]),
    CAVE_SLIMES("Cave slimes", 4520, new String[0]),
    CERBERUS("Cerberus", 13247, new String[0]),
    CHAOS_DRUIDS("Chaos druids", 20595, "Elder Chaos druid", "Chaos druid"),
    CHAOS_ELEMENTAL("Chaos Elemental", 11995, new String[0]),
    CHAOS_FANATIC("Chaos Fanatic", 4675, new String[0]),
    COCKATRICE("Cockatrice", 4137, "Cockathrice"),
    COWS("Cows", 11919, new String[0]),
    CRAWLING_HANDS("Crawling hands", 4133, "Crushing hand"),
    CRAZY_ARCHAEOLOGIST("Crazy Archaeologists", 11990, "Crazy Archaeologist"),
    CROCODILES("Crocodiles", 10149, new String[0]),
    DAGANNOTH("Dagannoth", 8141, new String[0]),
    DAGANNOTH_KINGS("Dagannoth Kings", 12644, new String[0]),
    DARK_BEASTS("Dark beasts", 6637, "Night beast"),
    DARK_WARRIORS("Dark warriors", 1151, "Dark warrior"),
    DERANGED_ARCHAEOLOGIST("Deranged Archaeologist", 21629, new String[0]),
    DOGS("Dogs", 8132, "Jackal"),
    DRAKES("Drakes", 23041, new String[0]),
    DUST_DEVILS("Dust devils", 4145, "Choke devil"),
    DWARVES("Dwarves", 11200, "Dwarf", "Black Guard"),
    EARTH_WARRIORS("Earth warriors", 12221, new String[0]),
    ELVES("Elves", 6105, "Elf", "Iorwerth Warrior", "Iorwerth Archer"),
    ENTS("Ents", 8174, "Ent"),
    FEVER_SPIDERS("Fever spiders", 6709, new String[0]),
    FIRE_GIANTS("Fire giants", 1393, new String[0]),
    FLESH_CRAWLERS("Fleshcrawlers", 13459, "Flesh crawler"),
    FOSSIL_ISLAND_WYVERNS("Fossil island wyverns", 21507, "Ancient wyvern", "Long-tailed wyvern", "Spitting wyvern", "Taloned wyvern"),
    GARGOYLES("Gargoyles", 4147, 9, 4162, new String[0]),
    GENERAL_GRAARDOR("General Graardor", 12650, new String[0]),
    GHOSTS("Ghosts", 552, "Death wing", "Tortured soul"),
    GHOULS("Ghouls", 6722, new String[0]),
    GIANT_MOLE("Giant Mole", 12646, new String[0]),
    GOBLINS("Goblins", 13447, new String[0]),
    GREATER_DEMONS("Greater demons", 20023, new String[0]),
    GREEN_DRAGONS("Green dragons", 12518, "Baby green dragon", "Elvarg"),
    GROTESQUE_GUARDIANS("Grotesque Guardians", 21750, 0, 4162, "Dusk", "Dawn"),
    HARPIE_BUG_SWARMS("Harpie bug swarms", 7050, new String[0]),
    HELLHOUNDS("Hellhounds", 8137, new String[0]),
    HILL_GIANTS("Hill giants", 13474, "Cyclops"),
    HOBGOBLINS("Hobgoblins", 8133, new String[0]),
    HYDRAS("Hydras", 23042, new String[0]),
    ICEFIENDS("Icefiends", 4671, new String[0]),
    ICE_GIANTS("Ice giants", 4671, new String[0]),
    ICE_WARRIORS("Ice warriors", 12293, "Icelord"),
    INFERNAL_MAGES("Infernal mages", 4140, "Malevolent mage"),
    IRON_DRAGONS("Iron dragons", 12365, new String[0]),
    JAD("TzTok-Jad", 13225, xp -> xp == 25250),
    JELLIES("Jellies", 4142, "Jelly"),
    JUNGLE_HORROR("Jungle horrors", 13486, new String[0]),
    KALPHITE("Kalphite", 8139, new String[0]),
    KALPHITE_QUEEN("Kalphite Queen", 12647, new String[0]),
    KILLERWATTS("Killerwatts", 7160, new String[0]),
    KING_BLACK_DRAGON("King Black Dragon", 12653, new String[0]),
    KRAKEN("Cave Kraken Boss", 12655, "Kraken"),
    KREEARRA("Kree'arra", 12649, new String[0]),
    KRIL_TSUTSAROTH("K'ril Tsutsaroth", 12652, new String[0]),
    KURASK("Kurask", 4146, new String[0]),
    LAVA_DRAGONS("Lava Dragons", 11992, "Lava dragon"),
    LESSER_DEMONS("Lesser demons", 20020, new String[0]),
    LIZARDMEN("Lizardmen", 13391, "Lizardman"),
    LIZARDS("Lizards", 6695, "Desert lizard", "Sulphur lizard", "Small lizard", "Lizard"),
    MAGIC_AXES("Magic axes", 1363, "Magic axe"),
    MAMMOTHS("Mammoths", 10516, "Mammoth"),
    MINIONS_OF_SCABARAS("Minions of scabaras", 9028, "Scarab swarm", "Locust rider", "Scarab mage"),
    MINOTAURS("Minotaurs", 13456, new String[0]),
    MITHRIL_DRAGONS("Mithril dragons", 12369, new String[0]),
    MOGRES("Mogres", 6661, new String[0]),
    MOLANISKS("Molanisks", 10997, new String[0]),
    MONKEYS("Monkeys", 13450, "Tortured gorilla"),
    MOSS_GIANTS("Moss giants", 22374, new String[0]),
    MUTATED_ZYGOMITES("Mutated zygomites", 7420, 7, 7431, "Zygomite", "Fungi"),
    NECHRYAEL("Nechryael", 4148, "Nechryarch"),
    OGRES("Ogres", 13477, new String[0]),
    OTHERWORLDLY_BEING("Otherworldly beings", 6109, new String[0]),
    PIRATES("Pirates", 8950, "Pirate"),
    PYREFIENDS("Pyrefiends", 4138, "Flaming pyrelord"),
    RATS("Rats", 300, new String[0]),
    RED_DRAGONS("Red dragons", 8134, "Baby red dragon"),
    REVENANTS("Revenants", 21816, "Revenant imp", "Revenant goblin", "Revenant pyrefiend", "Revenant hobgoblin", "Revenant cyclops", "Revenant hellhound", "Revenant demon", "Revenant ork", "Revenant dark beast", "Revenant knight", "Revenant dragon"),
    ROCKSLUGS("Rockslugs", 4136, 4, 4161, new String[0]),
    ROGUES("Rogues", 5554, "Rogue"),
    RUNE_DRAGONS("Rune dragons", 23273, new String[0]),
    SARACHNIS("Sarachnis", 23495, new String[0]),
    SCORPIA("Scorpia", 13181, new String[0]),
    SCORPIONS("Scorpions", 13459, new String[0]),
    SEA_SNAKES("Sea snakes", 7576, new String[0]),
    SHADES("Shades", 546, "Loar Shadow", "Loar Shade", "Phrin Shadow", "Phrin Shade", "Riyl Shadow", "Riyl Shade", "Asyn Shadow", "Asyn Shade", "Fiyr Shadow", "Fiyr Shade"),
    SHADOW_WARRIORS("Shadow warriors", 1165, new String[0]),
    SKELETAL_WYVERNS("Skeletal wyverns", 6811, new String[0]),
    SKELETONS("Skeletons", 8131, new String[0]),
    SMOKE_DEVILS("Smoke devils", 5349, new String[0]),
    SOURHOGS("Sourhogs", 24944, new String[0]),
    SPIDERS("Spiders", 8135, new String[0]),
    SPIRITUAL_CREATURES("Spiritual creatures", 11840, "Spiritual ranger", "Spiritual mage", "Spiritual warrior"),
    STEEL_DRAGONS("Steel dragons", 8142, new String[0]),
    SULPHUR_LIZARDS("Sulphur Lizards", 23043, new String[0]),
    SUQAHS("Suqahs", 9079, new String[0]),
    TEMPLE_SPIDERS("Temple Spiders", 223, new String[0]),
    TERROR_DOGS("Terror dogs", 10591, new String[0]),
    THERMONUCLEAR_SMOKE_DEVIL("Thermonuclear Smoke Devil", 12648, new String[0]),
    TROLLS("Trolls", 8136, "Dad", "Arrg"),
    TUROTH("Turoth", 4143, new String[0]),
    TZHAAR("Tzhaar", 13498, new String[0]),
    UNDEAD_DRUIDS("Undead Druids", 23522, new String[0]),
    VAMPYRES("Vampyres", 1549, "Vyrewatch", "Vampire"),
    VENENATIS("Venenatis", 13177, new String[0]),
    VETION("Vet'ion", 13179, new String[0]),
    VORKATH("Vorkath", 21992, new String[0]),
    WALL_BEASTS("Wall beasts", 4519, new String[0]),
    WATERFIENDS("Waterfiends", 571, new String[0]),
    WEREWOLVES("Werewolves", 2952, "Werewolf"),
    WOLVES("Wolves", 958, "Wolf"),
    WYRMS("Wyrms", 23040, new String[0]),
    ZILYANA("Commander Zilyana", 12651, new String[0]),
    ZOMBIES("Zombies", 6722, "Undead"),
    ZUK("TzKal-Zuk", 22319, xp -> xp == 101890),
    ZULRAH("Zulrah", 12921, new String[0]);

    private static final Map<String, Task> tasks;
    static final List<String> LOCATIONS;
    private final String name;
    private final int itemSpriteId;
    private final String[] targetNames;
    private final int weaknessThreshold;
    private final int weaknessItem;
    @Nullable
    private final IntPredicate xpMatcher;

    private Task(String name, int itemSpriteId, String ... targetNames) {
        Preconditions.checkArgument((itemSpriteId >= 0 ? 1 : 0) != 0);
        this.name = name;
        this.itemSpriteId = itemSpriteId;
        this.weaknessThreshold = -1;
        this.weaknessItem = -1;
        this.targetNames = targetNames;
        this.xpMatcher = null;
    }

    private Task(String name, int itemSpriteId, int weaknessThreshold, int weaknessItem, String ... targetNames) {
        Preconditions.checkArgument((itemSpriteId >= 0 ? 1 : 0) != 0);
        this.name = name;
        this.itemSpriteId = itemSpriteId;
        this.weaknessThreshold = weaknessThreshold;
        this.weaknessItem = weaknessItem;
        this.targetNames = targetNames;
        this.xpMatcher = null;
    }

    private Task(String name, int itemSpriteId, IntPredicate xpMatcher) {
        Preconditions.checkArgument((itemSpriteId >= 0 ? 1 : 0) != 0);
        this.name = name;
        this.itemSpriteId = itemSpriteId;
        this.weaknessThreshold = -1;
        this.weaknessItem = -1;
        this.targetNames = new String[0];
        this.xpMatcher = xpMatcher;
    }

    @Nullable
    static Task getTask(String taskName) {
        return tasks.get(taskName.toLowerCase());
    }

    public String getName() {
        return this.name;
    }

    public int getItemSpriteId() {
        return this.itemSpriteId;
    }

    public String[] getTargetNames() {
        return this.targetNames;
    }

    public int getWeaknessThreshold() {
        return this.weaknessThreshold;
    }

    public int getWeaknessItem() {
        return this.weaknessItem;
    }

    @Nullable
    public IntPredicate getXpMatcher() {
        return this.xpMatcher;
    }

    static {
        LOCATIONS = ImmutableList.of((Object)"", (Object)"Abyss", (Object)"Ancient Cavern", (Object)"Asgarnian Ice Dungeon", (Object)"Battlefront", (Object)"Brimhaven Dungeon", (Object)"Brine Rat Cavern", (Object)"Catacombs of Kourend", (Object)"Chasm of Fire", (Object)"Clan Wars", (Object)"Death Plateau", (Object)"Evil Chicken's Lair", (Object[])new String[]{"Fossil Island", "Forthos Dungeon", "Fremennik Slayer Dungeon", "God Wars Dungeon", "Iorwerth Dungeon", "Isle of Souls", "Jormungand's Prison", "Kalphite Lair", "Karuulm Slayer Dungeon", "Keldagrim", "Kraken Cove", "Lighthouse", "Lithkren Vault", "Lizardman Canyon", "Lizardman Settlement", "Meiyerditch Laboratories", "Molch", "Mount Quidamortem", "Mourner Tunnels", "Myths' Guild Dungeon", "Ogre Enclave", "Slayer Tower", "Smoke Devil Dungeon", "Smoke Dungeon", "Stronghold of Security", "Stronghold Slayer Dungeon", "task-only Kalphite Cave", "Taverley Dungeon", "Troll Stronghold", "Waterbirth Island", "Waterfall Dungeon", "Wilderness", "Witchaven Dungeon", "Zanaris"});
        ImmutableMap.Builder builder = new ImmutableMap.Builder();
        for (Task task : Task.values()) {
            builder.put((Object)task.getName().toLowerCase(), (Object)task);
        }
        tasks = builder.build();
    }
}

