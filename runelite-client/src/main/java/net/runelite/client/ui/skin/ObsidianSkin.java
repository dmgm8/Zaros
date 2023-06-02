/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.pushingpixels.substance.api.ComponentState
 *  org.pushingpixels.substance.api.SubstanceColorSchemeBundle
 *  org.pushingpixels.substance.api.SubstanceSkin
 *  org.pushingpixels.substance.api.SubstanceSkin$ColorSchemes
 *  org.pushingpixels.substance.api.SubstanceSlices$ColorSchemeAssociationKind
 *  org.pushingpixels.substance.api.SubstanceSlices$DecorationAreaType
 *  org.pushingpixels.substance.api.colorscheme.ColorSchemeSingleColorQuery
 *  org.pushingpixels.substance.api.colorscheme.SubstanceColorScheme
 *  org.pushingpixels.substance.api.painter.border.ClassicBorderPainter
 *  org.pushingpixels.substance.api.painter.border.CompositeBorderPainter
 *  org.pushingpixels.substance.api.painter.border.DelegateBorderPainter
 *  org.pushingpixels.substance.api.painter.border.StandardBorderPainter
 *  org.pushingpixels.substance.api.painter.border.SubstanceBorderPainter
 *  org.pushingpixels.substance.api.painter.decoration.MatteDecorationPainter
 *  org.pushingpixels.substance.api.painter.fill.FractionBasedFillPainter
 *  org.pushingpixels.substance.api.painter.highlight.ClassicHighlightPainter
 *  org.pushingpixels.substance.api.painter.overlay.BottomLineOverlayPainter
 *  org.pushingpixels.substance.api.painter.overlay.BottomShadowOverlayPainter
 *  org.pushingpixels.substance.api.painter.overlay.SubstanceOverlayPainter
 *  org.pushingpixels.substance.api.painter.overlay.TopBezelOverlayPainter
 *  org.pushingpixels.substance.api.painter.overlay.TopLineOverlayPainter
 *  org.pushingpixels.substance.api.shaper.ClassicButtonShaper
 *  org.pushingpixels.substance.internal.utils.SubstanceColorUtilities
 */
package net.runelite.client.ui.skin;

import java.awt.Color;
import java.net.URL;
import javax.swing.AbstractButton;
import org.pushingpixels.substance.api.ComponentState;
import org.pushingpixels.substance.api.SubstanceColorSchemeBundle;
import org.pushingpixels.substance.api.SubstanceSkin;
import org.pushingpixels.substance.api.SubstanceSlices;
import org.pushingpixels.substance.api.colorscheme.ColorSchemeSingleColorQuery;
import org.pushingpixels.substance.api.colorscheme.SubstanceColorScheme;
import org.pushingpixels.substance.api.painter.border.ClassicBorderPainter;
import org.pushingpixels.substance.api.painter.border.CompositeBorderPainter;
import org.pushingpixels.substance.api.painter.border.DelegateBorderPainter;
import org.pushingpixels.substance.api.painter.border.StandardBorderPainter;
import org.pushingpixels.substance.api.painter.border.SubstanceBorderPainter;
import org.pushingpixels.substance.api.painter.decoration.MatteDecorationPainter;
import org.pushingpixels.substance.api.painter.fill.FractionBasedFillPainter;
import org.pushingpixels.substance.api.painter.highlight.ClassicHighlightPainter;
import org.pushingpixels.substance.api.painter.overlay.BottomLineOverlayPainter;
import org.pushingpixels.substance.api.painter.overlay.BottomShadowOverlayPainter;
import org.pushingpixels.substance.api.painter.overlay.SubstanceOverlayPainter;
import org.pushingpixels.substance.api.painter.overlay.TopBezelOverlayPainter;
import org.pushingpixels.substance.api.painter.overlay.TopLineOverlayPainter;
import org.pushingpixels.substance.api.shaper.ClassicButtonShaper;
import org.pushingpixels.substance.internal.utils.SubstanceColorUtilities;

public class ObsidianSkin
extends SubstanceSkin {
    private static final String NAME = "RuneLite";

    ObsidianSkin() {
        SubstanceSkin.ColorSchemes schemes = SubstanceSkin.getColorSchemes((URL)((Object)((Object)this)).getClass().getResource("RuneLite.colorschemes"));
        SubstanceColorScheme activeScheme = schemes.get("RuneLite Active");
        SubstanceColorScheme enabledScheme = schemes.get("RuneLite Enabled");
        SubstanceColorSchemeBundle defaultSchemeBundle = new SubstanceColorSchemeBundle(activeScheme, enabledScheme, enabledScheme);
        defaultSchemeBundle.registerColorScheme(enabledScheme, 0.6f, new ComponentState[]{ComponentState.DISABLED_UNSELECTED});
        defaultSchemeBundle.registerColorScheme(activeScheme, 0.6f, new ComponentState[]{ComponentState.DISABLED_SELECTED});
        defaultSchemeBundle.registerHighlightColorScheme(schemes.get("RuneLite Highlight"), new ComponentState[]{ComponentState.SELECTED, ComponentState.ROLLOVER_SELECTED, ComponentState.ROLLOVER_UNSELECTED});
        SubstanceColorScheme borderDisabledSelectedScheme = schemes.get("RuneLite Selected Disabled Border");
        SubstanceColorScheme borderScheme = schemes.get("RuneLite Border");
        defaultSchemeBundle.registerColorScheme(borderDisabledSelectedScheme, SubstanceSlices.ColorSchemeAssociationKind.BORDER, new ComponentState[]{ComponentState.DISABLED_SELECTED});
        defaultSchemeBundle.registerColorScheme(borderScheme, SubstanceSlices.ColorSchemeAssociationKind.BORDER, new ComponentState[0]);
        SubstanceColorScheme markActiveScheme = schemes.get("RuneLite Mark Active");
        defaultSchemeBundle.registerColorScheme(markActiveScheme, SubstanceSlices.ColorSchemeAssociationKind.MARK, ComponentState.getActiveStates());
        defaultSchemeBundle.registerColorScheme(markActiveScheme, 0.6f, SubstanceSlices.ColorSchemeAssociationKind.MARK, new ComponentState[]{ComponentState.DISABLED_SELECTED, ComponentState.DISABLED_UNSELECTED});
        SubstanceColorScheme separatorScheme = schemes.get("RuneLite Separator");
        defaultSchemeBundle.registerColorScheme(separatorScheme, SubstanceSlices.ColorSchemeAssociationKind.SEPARATOR, new ComponentState[0]);
        defaultSchemeBundle.registerColorScheme(schemes.get("RuneLite Tab Border"), SubstanceSlices.ColorSchemeAssociationKind.TAB_BORDER, ComponentState.getActiveStates());
        SubstanceColorScheme watermarkScheme = schemes.get("RuneLite Watermark");
        this.registerDecorationAreaSchemeBundle(defaultSchemeBundle, watermarkScheme, new SubstanceSlices.DecorationAreaType[]{SubstanceSlices.DecorationAreaType.NONE});
        SubstanceColorSchemeBundle decorationsSchemeBundle = new SubstanceColorSchemeBundle(activeScheme, enabledScheme, enabledScheme);
        decorationsSchemeBundle.registerColorScheme(enabledScheme, 0.5f, new ComponentState[]{ComponentState.DISABLED_UNSELECTED});
        decorationsSchemeBundle.registerColorScheme(borderDisabledSelectedScheme, SubstanceSlices.ColorSchemeAssociationKind.BORDER, new ComponentState[]{ComponentState.DISABLED_SELECTED});
        decorationsSchemeBundle.registerColorScheme(borderScheme, SubstanceSlices.ColorSchemeAssociationKind.BORDER, new ComponentState[0]);
        decorationsSchemeBundle.registerColorScheme(markActiveScheme, SubstanceSlices.ColorSchemeAssociationKind.MARK, ComponentState.getActiveStates());
        SubstanceColorScheme separatorDecorationsScheme = schemes.get("RuneLite Decorations Separator");
        decorationsSchemeBundle.registerColorScheme(separatorDecorationsScheme, SubstanceSlices.ColorSchemeAssociationKind.SEPARATOR, new ComponentState[0]);
        SubstanceColorScheme decorationsWatermarkScheme = schemes.get("RuneLite Decorations Watermark");
        this.registerDecorationAreaSchemeBundle(decorationsSchemeBundle, decorationsWatermarkScheme, new SubstanceSlices.DecorationAreaType[]{SubstanceSlices.DecorationAreaType.TOOLBAR, SubstanceSlices.DecorationAreaType.GENERAL, SubstanceSlices.DecorationAreaType.FOOTER});
        SubstanceColorSchemeBundle headerSchemeBundle = new SubstanceColorSchemeBundle(activeScheme, enabledScheme, enabledScheme);
        headerSchemeBundle.registerColorScheme(enabledScheme, 0.5f, new ComponentState[]{ComponentState.DISABLED_UNSELECTED});
        SubstanceColorScheme headerBorderScheme = schemes.get("RuneLite Header Border");
        headerSchemeBundle.registerColorScheme(borderDisabledSelectedScheme, SubstanceSlices.ColorSchemeAssociationKind.BORDER, new ComponentState[]{ComponentState.DISABLED_SELECTED});
        headerSchemeBundle.registerColorScheme(headerBorderScheme, SubstanceSlices.ColorSchemeAssociationKind.BORDER, new ComponentState[0]);
        headerSchemeBundle.registerColorScheme(markActiveScheme, SubstanceSlices.ColorSchemeAssociationKind.MARK, ComponentState.getActiveStates());
        headerSchemeBundle.registerHighlightColorScheme(activeScheme, 0.7f, new ComponentState[]{ComponentState.ROLLOVER_UNSELECTED, ComponentState.ROLLOVER_ARMED, ComponentState.ARMED});
        headerSchemeBundle.registerHighlightColorScheme(activeScheme, 0.8f, new ComponentState[]{ComponentState.SELECTED});
        headerSchemeBundle.registerHighlightColorScheme(activeScheme, 1.0f, new ComponentState[]{ComponentState.ROLLOVER_SELECTED});
        SubstanceColorScheme headerWatermarkScheme = schemes.get("RuneLite Header Watermark");
        this.registerDecorationAreaSchemeBundle(headerSchemeBundle, headerWatermarkScheme, new SubstanceSlices.DecorationAreaType[]{SubstanceSlices.DecorationAreaType.PRIMARY_TITLE_PANE, SubstanceSlices.DecorationAreaType.SECONDARY_TITLE_PANE, SubstanceSlices.DecorationAreaType.HEADER});
        this.setTabFadeStart(0.2);
        this.setTabFadeEnd(0.9);
        this.addOverlayPainter((SubstanceOverlayPainter)BottomShadowOverlayPainter.getInstance(), new SubstanceSlices.DecorationAreaType[]{SubstanceSlices.DecorationAreaType.TOOLBAR});
        this.addOverlayPainter((SubstanceOverlayPainter)BottomShadowOverlayPainter.getInstance(), new SubstanceSlices.DecorationAreaType[]{SubstanceSlices.DecorationAreaType.FOOTER});
        BottomLineOverlayPainter toolbarBottomLineOverlayPainter = new BottomLineOverlayPainter(scheme -> scheme.getUltraDarkColor().darker());
        this.addOverlayPainter((SubstanceOverlayPainter)toolbarBottomLineOverlayPainter, new SubstanceSlices.DecorationAreaType[]{SubstanceSlices.DecorationAreaType.TOOLBAR});
        TopLineOverlayPainter toolbarTopLineOverlayPainter = new TopLineOverlayPainter(scheme -> SubstanceColorUtilities.getAlphaColor((Color)scheme.getForegroundColor(), (int)32));
        this.addOverlayPainter((SubstanceOverlayPainter)toolbarTopLineOverlayPainter, new SubstanceSlices.DecorationAreaType[]{SubstanceSlices.DecorationAreaType.TOOLBAR});
        TopBezelOverlayPainter footerTopBezelOverlayPainter = new TopBezelOverlayPainter(scheme -> scheme.getUltraDarkColor().darker(), scheme -> SubstanceColorUtilities.getAlphaColor((Color)scheme.getForegroundColor(), (int)32));
        this.addOverlayPainter((SubstanceOverlayPainter)footerTopBezelOverlayPainter, new SubstanceSlices.DecorationAreaType[]{SubstanceSlices.DecorationAreaType.FOOTER});
        this.setTabFadeStart(0.18);
        this.setTabFadeEnd(0.18);
        this.buttonShaper = new ClassicButtonShaper(){

            public float getCornerRadius(AbstractButton button, float insets) {
                return 0.0f;
            }
        };
        this.watermark = null;
        this.fillPainter = new FractionBasedFillPainter(NAME, new float[]{0.0f, 0.5f, 1.0f}, new ColorSchemeSingleColorQuery[]{ColorSchemeSingleColorQuery.ULTRALIGHT, ColorSchemeSingleColorQuery.LIGHT, ColorSchemeSingleColorQuery.LIGHT});
        this.decorationPainter = new MatteDecorationPainter();
        this.highlightPainter = new ClassicHighlightPainter();
        this.borderPainter = new CompositeBorderPainter(NAME, (SubstanceBorderPainter)new ClassicBorderPainter(), (SubstanceBorderPainter)new DelegateBorderPainter("RuneLite Inner", (StandardBorderPainter)new ClassicBorderPainter(), 0x40FFFFFF, 0x20FFFFFF, 0xFFFFFF, scheme -> scheme.tint((double)0.2f)));
    }

    public String getDisplayName() {
        return NAME;
    }
}

