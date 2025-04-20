package com.gtnewhorizons.wdmla.impl.ui.style;

import org.jetbrains.annotations.Nullable;

import com.gtnewhorizons.wdmla.api.ui.ColorPalette;
import com.gtnewhorizons.wdmla.api.ui.IDrawable;
import com.gtnewhorizons.wdmla.api.ui.style.IAmountStyle;

public class AmountStyle implements IAmountStyle {

    private int filledColor;
    private int alternateFilledColor;
    @Nullable
    private IDrawable overlay;

    public AmountStyle(int filledColor, int alternateFilledColor,
                       @Nullable IDrawable overlay) {
        this.filledColor = filledColor;
        this.alternateFilledColor = alternateFilledColor;
        this.overlay = overlay;
    }

    public AmountStyle() {
        this.filledColor = ColorPalette.AMOUNT_FILLED;
        this.alternateFilledColor = ColorPalette.AMOUNT_FILLED_ALTERNATE;
        this.overlay = null;
    }

    public AmountStyle singleColor(int color) {
        this.filledColor = color;
        this.alternateFilledColor = color;
        return this;
    }

    public AmountStyle filledColor(int filledColor) {
        this.filledColor = filledColor;
        return this;
    }

    public AmountStyle alternateFilledColor(int alternateFilledColor) {
        this.alternateFilledColor = alternateFilledColor;
        return this;
    }

    public AmountStyle overlay(IDrawable overlay) {
        this.overlay = overlay;
        return this;
    }

    @Override
    public int getFilledColor() {
        return filledColor;
    }

    @Override
    public int getAlternateFilledColor() {
        return alternateFilledColor;
    }

    @Override
    public @Nullable IDrawable getOverlay() {
        return overlay;
    }
}
