/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.inject.Provides
 *  javax.inject.Inject
 *  net.runelite.api.Client
 *  net.runelite.api.events.GameTick
 *  net.runelite.api.events.MenuOptionClicked
 *  net.runelite.api.events.WidgetLoaded
 *  net.runelite.api.widgets.Widget
 *  net.runelite.api.widgets.WidgetInfo
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package net.runelite.client.plugins.puzzlesolver;

import com.google.inject.Provides;
import java.awt.Color;
import java.util.Arrays;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.puzzlesolver.PuzzleSolverConfig;
import net.runelite.client.plugins.puzzlesolver.PuzzleSolverOverlay;
import net.runelite.client.plugins.puzzlesolver.VarrockMuseumAnswer;
import net.runelite.client.plugins.puzzlesolver.lightbox.Combination;
import net.runelite.client.plugins.puzzlesolver.lightbox.LightboxSolution;
import net.runelite.client.plugins.puzzlesolver.lightbox.LightboxSolver;
import net.runelite.client.plugins.puzzlesolver.lightbox.LightboxState;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.ColorUtil;
import net.runelite.client.util.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(name="Puzzle Solver", description="Show you where to click to solve puzzle boxes", tags={"clues", "scrolls", "overlay"}, forceDisabled=true)
public class PuzzleSolverPlugin
extends Plugin {
    private static final Logger log = LoggerFactory.getLogger(PuzzleSolverPlugin.class);
    private static final Color CORRECT_MUSEUM_PUZZLE_ANSWER_COLOR = new Color(0, 248, 128);
    @Inject
    private OverlayManager overlayManager;
    @Inject
    private PuzzleSolverOverlay overlay;
    @Inject
    private Client client;
    private LightboxState lightbox;
    private LightboxState[] changes = new LightboxState[8];
    private Combination lastClick;
    private boolean lastClickInvalid;

    @Override
    protected void startUp() throws Exception {
        this.overlayManager.add(this.overlay);
    }

    @Override
    protected void shutDown() throws Exception {
        this.overlayManager.remove(this.overlay);
    }

    @Provides
    PuzzleSolverConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(PuzzleSolverConfig.class);
    }

    @Subscribe
    public void onWidgetLoaded(WidgetLoaded widget) {
        if (widget.getGroupId() != 533) {
            return;
        }
        Widget questionWidget = this.client.getWidget(WidgetInfo.VARROCK_MUSEUM_QUESTION);
        if (questionWidget == null) {
            return;
        }
        Widget answerWidget = VarrockMuseumAnswer.findCorrect(this.client, questionWidget.getText(), WidgetInfo.VARROCK_MUSEUM_FIRST_ANSWER, WidgetInfo.VARROCK_MUSEUM_SECOND_ANSWER, WidgetInfo.VARROCK_MUSEUM_THIRD_ANSWER);
        if (answerWidget == null) {
            return;
        }
        String answerText = answerWidget.getText();
        if (answerText.equals(Text.removeTags(answerText))) {
            answerWidget.setText(ColorUtil.wrapWithColorTag(answerText, CORRECT_MUSEUM_PUZZLE_ANSWER_COLOR));
        }
    }

    @Subscribe
    public void onMenuOptionClicked(MenuOptionClicked menuOptionClicked) {
        Combination combination;
        int widgetId = menuOptionClicked.getParam1();
        if (WidgetInfo.TO_GROUP((int)widgetId) != 322) {
            return;
        }
        if (widgetId == WidgetInfo.LIGHT_BOX_BUTTON_A.getId()) {
            combination = Combination.A;
        } else if (widgetId == WidgetInfo.LIGHT_BOX_BUTTON_B.getId()) {
            combination = Combination.B;
        } else if (widgetId == WidgetInfo.LIGHT_BOX_BUTTON_C.getId()) {
            combination = Combination.C;
        } else if (widgetId == WidgetInfo.LIGHT_BOX_BUTTON_D.getId()) {
            combination = Combination.D;
        } else if (widgetId == WidgetInfo.LIGHT_BOX_BUTTON_E.getId()) {
            combination = Combination.E;
        } else if (widgetId == WidgetInfo.LIGHT_BOX_BUTTON_F.getId()) {
            combination = Combination.F;
        } else if (widgetId == WidgetInfo.LIGHT_BOX_BUTTON_G.getId()) {
            combination = Combination.G;
        } else if (widgetId == WidgetInfo.LIGHT_BOX_BUTTON_H.getId()) {
            combination = Combination.H;
        } else {
            return;
        }
        if (this.lastClick != null) {
            this.lastClickInvalid = true;
        } else {
            this.lastClick = combination;
        }
    }

    @Subscribe
    public void onGameTick(GameTick event) {
        Widget lightbox;
        LightboxState diff;
        Widget lightboxWidget = this.client.getWidget(WidgetInfo.LIGHT_BOX_CONTENTS);
        if (lightboxWidget == null) {
            if (this.lightbox != null) {
                this.lastClick = null;
                this.lastClickInvalid = false;
                this.lightbox = null;
                Arrays.fill(this.changes, null);
            }
            return;
        }
        LightboxState lightboxState = new LightboxState();
        int index = 0;
        for (Widget light : lightboxWidget.getDynamicChildren()) {
            boolean lit = light.getItemId() == 20357;
            lightboxState.setState(index / 5, index % 5, lit);
            ++index;
        }
        if (lightboxState.equals(this.lightbox)) {
            return;
        }
        log.debug("Lightbox changed!");
        LightboxState prev = this.lightbox;
        this.lightbox = lightboxState;
        if (this.lastClick == null || this.lastClickInvalid) {
            this.lastClick = null;
            this.lastClickInvalid = false;
            return;
        }
        this.changes[this.lastClick.ordinal()] = diff = lightboxState.diff(prev);
        log.debug("Recorded diff for {}", (Object)this.lastClick);
        this.lastClick = null;
        LightboxSolver solver = new LightboxSolver();
        solver.setInitial(this.lightbox);
        int idx = 0;
        for (LightboxState state : this.changes) {
            if (state != null) {
                Combination combination = Combination.values()[idx];
                solver.setSwitchChange(combination, state);
            }
            ++idx;
        }
        LightboxSolution solution = solver.solve();
        if (solution != null) {
            log.debug("Got solution: {}", (Object)solution);
        }
        if ((lightbox = this.client.getWidget(WidgetInfo.LIGHT_BOX)) != null) {
            Widget title = lightbox.getChild(1);
            if (solution != null && solution.numMoves() > 0) {
                title.setText("Light box - Solution: " + solution);
            } else if (solution != null) {
                title.setText("Light box - Solution: solved!");
            } else {
                title.setText("Light box - Solution: unknown");
            }
        }
    }
}

