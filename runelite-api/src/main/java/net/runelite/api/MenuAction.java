/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api;

import java.util.HashMap;
import java.util.Map;

public enum MenuAction {
    ITEM_USE_ON_GAME_OBJECT(1),
    WIDGET_TARGET_ON_GAME_OBJECT(2),
    GAME_OBJECT_FIRST_OPTION(3),
    GAME_OBJECT_SECOND_OPTION(4),
    GAME_OBJECT_THIRD_OPTION(5),
    GAME_OBJECT_FOURTH_OPTION(6),
    GAME_OBJECT_FIFTH_OPTION(1001),
    ITEM_USE_ON_NPC(7),
    WIDGET_TARGET_ON_NPC(8),
    NPC_FIRST_OPTION(9),
    NPC_SECOND_OPTION(10),
    NPC_THIRD_OPTION(11),
    NPC_FOURTH_OPTION(12),
    NPC_FIFTH_OPTION(13),
    ITEM_USE_ON_PLAYER(14),
    WIDGET_TARGET_ON_PLAYER(15),
    ITEM_USE_ON_GROUND_ITEM(16),
    WIDGET_TARGET_ON_GROUND_ITEM(17),
    GROUND_ITEM_FIRST_OPTION(18),
    GROUND_ITEM_SECOND_OPTION(19),
    GROUND_ITEM_THIRD_OPTION(20),
    GROUND_ITEM_FOURTH_OPTION(21),
    GROUND_ITEM_FIFTH_OPTION(22),
    WALK(23),
    WIDGET_TYPE_1(24),
    WIDGET_TARGET(25),
    WIDGET_CLOSE(26),
    WIDGET_TYPE_4(28),
    WIDGET_TYPE_5(29),
    WIDGET_CONTINUE(30),
    ITEM_USE_ON_ITEM(31),
    WIDGET_USE_ON_ITEM(32),
    ITEM_FIRST_OPTION(33),
    ITEM_SECOND_OPTION(34),
    ITEM_THIRD_OPTION(35),
    ITEM_FOURTH_OPTION(36),
    ITEM_FIFTH_OPTION(37),
    ITEM_USE(38),
    WIDGET_FIRST_OPTION(39),
    WIDGET_SECOND_OPTION(40),
    WIDGET_THIRD_OPTION(41),
    WIDGET_FOURTH_OPTION(42),
    WIDGET_FIFTH_OPTION(43),
    PLAYER_FIRST_OPTION(44),
    PLAYER_SECOND_OPTION(45),
    PLAYER_THIRD_OPTION(46),
    PLAYER_FOURTH_OPTION(47),
    PLAYER_FIFTH_OPTION(48),
    PLAYER_SIXTH_OPTION(49),
    PLAYER_SEVENTH_OPTION(50),
    PLAYER_EIGTH_OPTION(51),
    CC_OP(57),
    WIDGET_TARGET_ON_WIDGET(58),
    RUNELITE_HIGH_PRIORITY(999),
    EXAMINE_OBJECT(1002),
    EXAMINE_NPC(1003),
    EXAMINE_ITEM_GROUND(1004),
    EXAMINE_ITEM(1005),
    CANCEL(1006),
    CC_OP_LOW_PRIORITY(1007),
    RUNELITE(1500),
    RUNELITE_OVERLAY(1501),
    RUNELITE_OVERLAY_CONFIG(1502),
    RUNELITE_PLAYER(1503),
    RUNELITE_INFOBOX(1504),
    RUNELITE_SUBMENU(1505),
    UNKNOWN(-1);

    public static final int MENU_ACTION_DEPRIORITIZE_OFFSET = 2000;
    private static final Map<Integer, MenuAction> map;
    private final int id;

    private MenuAction(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static MenuAction of(int id) {
        return map.getOrDefault(id, UNKNOWN);
    }

    static {
        map = new HashMap<Integer, MenuAction>();
        for (MenuAction menuAction : MenuAction.values()) {
            map.put(menuAction.getId(), menuAction);
        }
    }
}

