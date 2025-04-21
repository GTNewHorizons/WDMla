package com.gtnewhorizons.wdmla.impl.ui.style;

import com.gtnewhorizons.wdmla.api.ui.ColorPalette;
import com.gtnewhorizons.wdmla.api.ui.ComponentAlignment;
import com.gtnewhorizons.wdmla.api.ui.style.IPanelStyle;
import com.gtnewhorizons.wdmla.config.General;

public class PanelStyle implements IPanelStyle {

    private int borderColor;
    private ComponentAlignment alignment;
    private final int spacing;
    private final int borderThickness;

    public PanelStyle(int spacing, int borderThickness) {
        this.borderColor = ColorPalette.TRANSPARENT;
        this.alignment = ComponentAlignment.TOPLEFT;
        this.spacing = spacing;
        this.borderThickness = borderThickness;
    }

    public PanelStyle() {
        IPanelStyle theme = General.currentTheme.get().panelStyle;
        this.borderColor = ColorPalette.TRANSPARENT;
        this.alignment = ComponentAlignment.TOPLEFT;
        this.spacing = theme.getSpacing();
        this.borderThickness = theme.getBorderThickness();
    }

    public PanelStyle borderColor(int borderColor) {
        this.borderColor = borderColor;
        return this;
    }

    public PanelStyle alignment(ComponentAlignment alignment) {
        this.alignment = alignment;
        return this;
    }

    @Override
    public int getBorderColor() {
        return borderColor;
    }

    @Override
    public int getBorderThickness() {
        if (borderColor == ColorPalette.TRANSPARENT) {
            return 0;
        }

        return borderThickness;
    }

    @Override
    public ComponentAlignment getAlignment() {
        return alignment;
    }

    @Override
    public int getSpacing() {
        return spacing;
    }
}
