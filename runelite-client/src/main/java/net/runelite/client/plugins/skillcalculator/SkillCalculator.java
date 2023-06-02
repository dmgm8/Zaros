/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  net.runelite.api.Client
 *  net.runelite.api.Experience
 *  net.runelite.api.Skill
 *  net.runelite.api.VarPlayer
 *  net.runelite.api.WorldType
 */
package net.runelite.client.plugins.skillcalculator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import net.runelite.api.Client;
import net.runelite.api.Experience;
import net.runelite.api.Skill;
import net.runelite.api.VarPlayer;
import net.runelite.api.WorldType;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.plugins.skillcalculator.CalculatorType;
import net.runelite.client.plugins.skillcalculator.UIActionSlot;
import net.runelite.client.plugins.skillcalculator.UICalculatorInputArea;
import net.runelite.client.plugins.skillcalculator.UICombinedActionSlot;
import net.runelite.client.plugins.skillcalculator.skills.SkillAction;
import net.runelite.client.plugins.skillcalculator.skills.SkillBonus;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.DynamicGridLayout;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.components.IconTextField;

class SkillCalculator
extends JPanel {
    private static final int MAX_XP = 200000000;
    private static final JLabel EMPTY_PANEL = new JLabel("No F2P actions to show.");
    private final UICalculatorInputArea uiInput;
    private final Client client;
    private final ClientThread clientThread;
    private final SpriteManager spriteManager;
    private final ItemManager itemManager;
    private final List<UIActionSlot> uiActionSlots = new ArrayList<UIActionSlot>();
    private final UICombinedActionSlot combinedActionSlot;
    private final ArrayList<UIActionSlot> combinedActionSlots = new ArrayList();
    private final List<JCheckBox> bonusCheckBoxes = new ArrayList<JCheckBox>();
    private final IconTextField searchBar = new IconTextField();
    private CalculatorType currentCalculator;
    private int currentLevel = 1;
    private int currentXP = Experience.getXpForLevel((int)this.currentLevel);
    private int targetLevel = this.currentLevel + 1;
    private int targetXP = Experience.getXpForLevel((int)this.targetLevel);
    private SkillBonus currentBonus = null;

    @Inject
    SkillCalculator(Client client, ClientThread clientThread, UICalculatorInputArea uiInput, SpriteManager spriteManager, ItemManager itemManager) {
        this.client = client;
        this.clientThread = clientThread;
        this.uiInput = uiInput;
        this.spriteManager = spriteManager;
        this.itemManager = itemManager;
        this.combinedActionSlot = new UICombinedActionSlot(spriteManager);
        this.searchBar.setIcon(IconTextField.Icon.SEARCH);
        this.searchBar.setPreferredSize(new Dimension(205, 30));
        this.searchBar.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        this.searchBar.setHoverBackgroundColor(ColorScheme.DARK_GRAY_HOVER_COLOR);
        this.searchBar.addClearListener(this::onSearch);
        this.searchBar.addKeyListener(new KeyAdapter(){

            @Override
            public void keyTyped(KeyEvent e) {
                SkillCalculator.this.onSearch();
            }
        });
        this.setLayout(new DynamicGridLayout(0, 1, 0, 5));
        uiInput.getUiFieldCurrentLevel().addActionListener(e -> {
            this.onFieldCurrentLevelUpdated();
            uiInput.getUiFieldTargetLevel().requestFocusInWindow();
        });
        uiInput.getUiFieldCurrentXP().addActionListener(e -> {
            this.onFieldCurrentXPUpdated();
            uiInput.getUiFieldTargetXP().requestFocusInWindow();
        });
        uiInput.getUiFieldTargetLevel().addActionListener(e -> this.onFieldTargetLevelUpdated());
        uiInput.getUiFieldTargetXP().addActionListener(e -> this.onFieldTargetXPUpdated());
        uiInput.getUiFieldCurrentLevel().addFocusListener(SkillCalculator.buildFocusAdapter(e -> this.onFieldCurrentLevelUpdated()));
        uiInput.getUiFieldCurrentXP().addFocusListener(SkillCalculator.buildFocusAdapter(e -> this.onFieldCurrentXPUpdated()));
        uiInput.getUiFieldTargetLevel().addFocusListener(SkillCalculator.buildFocusAdapter(e -> this.onFieldTargetLevelUpdated()));
        uiInput.getUiFieldTargetXP().addFocusListener(SkillCalculator.buildFocusAdapter(e -> this.onFieldTargetXPUpdated()));
    }

    void openCalculator(CalculatorType calculatorType, boolean forceReload) {
        this.currentXP = this.client.getSkillExperience(calculatorType.getSkill());
        this.currentLevel = Experience.getLevelForXp((int)this.currentXP);
        if (forceReload || this.currentCalculator != calculatorType) {
            this.currentCalculator = calculatorType;
            this.currentBonus = null;
            VarPlayer endGoalVarp = SkillCalculator.endGoalVarpForSkill(calculatorType.getSkill());
            int endGoal = this.client.getVarpValue(endGoalVarp);
            if (endGoal != -1) {
                this.targetLevel = Experience.getLevelForXp((int)endGoal);
                this.targetXP = endGoal;
            } else {
                this.targetLevel = SkillCalculator.enforceSkillBounds(this.currentLevel + 1);
                this.targetXP = Experience.getXpForLevel((int)this.targetLevel);
            }
            this.removeAll();
            this.searchBar.setText(null);
            this.clearCombinedSlots();
            if (this.client.getWorldType().contains((Object)WorldType.MEMBERS)) {
                this.renderBonusOptions();
            }
            this.add(this.combinedActionSlot);
            this.add(this.searchBar);
            this.renderActionSlots();
        }
        this.updateInputFields();
    }

    private void updateCombinedAction() {
        int size = this.combinedActionSlots.size();
        if (size > 1) {
            this.combinedActionSlot.setTitle(size + " actions selected");
        } else if (size == 1) {
            this.combinedActionSlot.setTitle("1 action selected");
        } else {
            this.combinedActionSlot.setTitle("No action selected");
            this.combinedActionSlot.setText("Shift-click to select multiple");
            return;
        }
        int actionCount = 0;
        int neededXP = this.targetXP - this.currentXP;
        int xp = 0;
        for (UIActionSlot slot : this.combinedActionSlots) {
            xp += slot.getValue();
        }
        if (neededXP > 0) {
            assert (xp != 0);
            actionCount = ((neededXP *= 10) - 1) / xp + 1;
        }
        this.combinedActionSlot.setText(SkillCalculator.formatXPActionString(xp, actionCount, "exp - "));
    }

    private void clearCombinedSlots() {
        for (UIActionSlot slot : this.combinedActionSlots) {
            slot.setSelected(false);
        }
        this.combinedActionSlots.clear();
    }

    private void renderBonusOptions() {
        SkillBonus[] skillBonuses = this.currentCalculator.getSkillBonuses();
        if (skillBonuses == null) {
            return;
        }
        for (SkillBonus bonus : skillBonuses) {
            JPanel checkboxPanel = this.buildCheckboxPanel(bonus);
            this.add(checkboxPanel);
            this.add(Box.createRigidArea(new Dimension(0, 5)));
        }
    }

    private JPanel buildCheckboxPanel(SkillBonus bonus) {
        JPanel uiOption = new JPanel(new BorderLayout());
        JLabel uiLabel = new JLabel(bonus.getName());
        JCheckBox uiCheckbox = new JCheckBox();
        uiLabel.setForeground(Color.WHITE);
        uiLabel.setFont(FontManager.getRunescapeSmallFont());
        uiOption.setBorder(BorderFactory.createEmptyBorder(3, 7, 3, 0));
        uiOption.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        uiCheckbox.addActionListener(event -> this.adjustCheckboxes(uiCheckbox, bonus));
        uiCheckbox.setBackground(ColorScheme.MEDIUM_GRAY_COLOR);
        uiOption.add((Component)uiLabel, "West");
        uiOption.add((Component)uiCheckbox, "East");
        this.bonusCheckBoxes.add(uiCheckbox);
        return uiOption;
    }

    private void adjustCheckboxes(JCheckBox target, SkillBonus bonus) {
        for (JCheckBox otherSelectedCheckbox : this.bonusCheckBoxes) {
            if (otherSelectedCheckbox == target) continue;
            otherSelectedCheckbox.setSelected(false);
        }
        this.adjustXPBonus(target.isSelected() ? bonus : null);
    }

    private void renderActionSlots() {
        this.uiActionSlots.clear();
        for (SkillAction action : this.currentCalculator.getSkillActions()) {
            JLabel uiIcon = new JLabel();
            if (action.getIcon() != -1) {
                this.itemManager.getImage(action.getIcon()).addTo(uiIcon);
            } else if (action.getSprite() != -1) {
                this.spriteManager.addSpriteTo(uiIcon, action.getSprite(), 0);
            }
            final UIActionSlot slot = new UIActionSlot(action, this.clientThread, this.itemManager, uiIcon);
            this.uiActionSlots.add(slot);
            slot.addMouseListener(new MouseAdapter(){

                @Override
                public void mousePressed(MouseEvent e) {
                    if (!e.isShiftDown()) {
                        SkillCalculator.this.clearCombinedSlots();
                    }
                    if (slot.isSelected()) {
                        SkillCalculator.this.combinedActionSlots.remove(slot);
                    } else {
                        SkillCalculator.this.combinedActionSlots.add(slot);
                    }
                    slot.setSelected(!slot.isSelected());
                    SkillCalculator.this.updateCombinedAction();
                }
            });
        }
        if (this.client.getWorldType().contains((Object)WorldType.MEMBERS)) {
            this.uiActionSlots.forEach(this::add);
            this.revalidate();
            this.repaint();
        } else {
            this.clientThread.invokeLater(() -> {
                List membersActions = this.uiActionSlots.stream().filter(slot -> !slot.getAction().isMembers(this.itemManager)).collect(Collectors.toList());
                SwingUtilities.invokeLater(() -> {
                    if (membersActions.isEmpty()) {
                        this.add(EMPTY_PANEL);
                    } else {
                        membersActions.forEach(this::add);
                    }
                    this.revalidate();
                    this.repaint();
                });
            });
        }
    }

    private void calculate() {
        for (UIActionSlot slot : this.uiActionSlots) {
            int actionCount = 0;
            int neededXP = this.targetXP - this.currentXP;
            SkillAction action = slot.getAction();
            float bonus = 1.0f;
            if (this.currentBonus != null && action.isBonusApplicable(this.currentBonus)) {
                bonus = this.currentBonus.getValue();
            }
            int xp = Math.round(action.getXp() * bonus * 10.0f);
            if (neededXP > 0) {
                actionCount = ((neededXP *= 10) - 1) / xp + 1;
            }
            slot.setText("Lvl. " + action.getLevel() + " (" + SkillCalculator.formatXPActionString(xp, actionCount, "exp) - "));
            slot.setAvailable(this.currentLevel >= action.getLevel());
            slot.setOverlapping(action.getLevel() < this.targetLevel);
            slot.setValue(xp);
        }
        this.updateCombinedAction();
    }

    private static String formatXPActionString(int xp, int actionCount, String expExpression) {
        int integer = xp / 10;
        int frac = xp % 10;
        return (frac != 0 ? integer + "." + frac : Integer.valueOf(integer)) + expExpression + NumberFormat.getIntegerInstance().format(actionCount) + (actionCount == 1 ? " action" : " actions");
    }

    private void updateInputFields() {
        if (this.targetXP < this.currentXP) {
            this.targetLevel = SkillCalculator.enforceSkillBounds(this.currentLevel + 1);
            this.targetXP = Experience.getXpForLevel((int)this.targetLevel);
        }
        String cXP = String.format("%,d", this.currentXP);
        String tXP = String.format("%,d", this.targetXP);
        String nXP = String.format("%,d", this.targetXP - this.currentXP);
        this.uiInput.setCurrentLevelInput(this.currentLevel);
        this.uiInput.setCurrentXPInput(cXP);
        this.uiInput.setTargetLevelInput(this.targetLevel);
        this.uiInput.setTargetXPInput(tXP);
        this.uiInput.setNeededXP(nXP + " XP required to reach target XP");
        this.calculate();
    }

    private void adjustXPBonus(SkillBonus bonus) {
        this.currentBonus = bonus;
        this.calculate();
    }

    private void onFieldCurrentLevelUpdated() {
        this.currentLevel = SkillCalculator.enforceSkillBounds(this.uiInput.getCurrentLevelInput());
        this.currentXP = Experience.getXpForLevel((int)this.currentLevel);
        this.updateInputFields();
    }

    private void onFieldCurrentXPUpdated() {
        this.currentXP = SkillCalculator.enforceXPBounds(this.uiInput.getCurrentXPInput());
        this.currentLevel = Experience.getLevelForXp((int)this.currentXP);
        this.updateInputFields();
    }

    private void onFieldTargetLevelUpdated() {
        this.targetLevel = SkillCalculator.enforceSkillBounds(this.uiInput.getTargetLevelInput());
        this.targetXP = Experience.getXpForLevel((int)this.targetLevel);
        this.updateInputFields();
    }

    private void onFieldTargetXPUpdated() {
        this.targetXP = SkillCalculator.enforceXPBounds(this.uiInput.getTargetXPInput());
        this.targetLevel = Experience.getLevelForXp((int)this.targetXP);
        this.updateInputFields();
    }

    private static int enforceSkillBounds(int input) {
        return Math.min(126, Math.max(1, input));
    }

    private static int enforceXPBounds(int input) {
        return Math.min(200000000, Math.max(0, input));
    }

    private void onSearch() {
        this.uiActionSlots.forEach(slot -> {
            if (SkillCalculator.slotContainsText(slot, this.searchBar.getText())) {
                super.add((Component)slot);
            } else {
                super.remove((Component)slot);
            }
            this.revalidate();
        });
    }

    private static boolean slotContainsText(UIActionSlot slot, String text) {
        return slot.getActionName().toLowerCase().contains(text.toLowerCase());
    }

    private static FocusAdapter buildFocusAdapter(final Consumer<FocusEvent> focusLostConsumer) {
        return new FocusAdapter(){

            @Override
            public void focusLost(FocusEvent e) {
                focusLostConsumer.accept(e);
            }
        };
    }

    private static VarPlayer endGoalVarpForSkill(Skill skill) {
        switch (skill) {
            case ATTACK: {
                return VarPlayer.ATTACK_GOAL_END;
            }
            case MINING: {
                return VarPlayer.MINING_GOAL_END;
            }
            case WOODCUTTING: {
                return VarPlayer.WOODCUTTING_GOAL_END;
            }
            case DEFENCE: {
                return VarPlayer.DEFENCE_GOAL_END;
            }
            case MAGIC: {
                return VarPlayer.MAGIC_GOAL_END;
            }
            case RANGED: {
                return VarPlayer.RANGED_GOAL_END;
            }
            case HITPOINTS: {
                return VarPlayer.HITPOINTS_GOAL_END;
            }
            case AGILITY: {
                return VarPlayer.AGILITY_GOAL_END;
            }
            case STRENGTH: {
                return VarPlayer.STRENGTH_GOAL_END;
            }
            case PRAYER: {
                return VarPlayer.PRAYER_GOAL_END;
            }
            case SLAYER: {
                return VarPlayer.SLAYER_GOAL_END;
            }
            case FISHING: {
                return VarPlayer.FISHING_GOAL_END;
            }
            case RUNECRAFT: {
                return VarPlayer.RUNECRAFT_GOAL_END;
            }
            case HERBLORE: {
                return VarPlayer.HERBLORE_GOAL_END;
            }
            case FIREMAKING: {
                return VarPlayer.FIREMAKING_GOAL_END;
            }
            case CONSTRUCTION: {
                return VarPlayer.CONSTRUCTION_GOAL_END;
            }
            case HUNTER: {
                return VarPlayer.HUNTER_GOAL_END;
            }
            case COOKING: {
                return VarPlayer.COOKING_GOAL_END;
            }
            case FARMING: {
                return VarPlayer.FARMING_GOAL_END;
            }
            case CRAFTING: {
                return VarPlayer.CRAFTING_GOAL_END;
            }
            case SMITHING: {
                return VarPlayer.SMITHING_GOAL_END;
            }
            case THIEVING: {
                return VarPlayer.THIEVING_GOAL_END;
            }
            case FLETCHING: {
                return VarPlayer.FLETCHING_GOAL_END;
            }
        }
        throw new IllegalArgumentException();
    }

    static {
        EMPTY_PANEL.setHorizontalAlignment(0);
        EMPTY_PANEL.setBorder(new EmptyBorder(50, 0, 0, 0));
    }
}

