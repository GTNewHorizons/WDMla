package com.gtnewhorizons.wdmla.impl.ui.style;

import org.jetbrains.annotations.Nullable;

import com.gtnewhorizons.wdmla.api.ui.ColorPalette;
import com.gtnewhorizons.wdmla.api.ui.IDrawable;
import com.gtnewhorizons.wdmla.api.ui.style.IAmountStyle;

public class AmountStyle implements IAmountStyle {

    private int borderColor;
    private int backgroundColor;
    private int filledColor;
    private int alternateFilledColor;
    @Nullable
    private IDrawable overlay;

    public AmountStyle(int borderColor, int backgroundColor, int filledColor, int alternateFilledColor,
                       @Nullable IDrawable overlay) {
        this.borderColor = borderColor;
        this.backgroundColor = backgroundColor;
        this.filledColor = filledColor;
        this.alternateFilledColor = alternateFilledColor;
        this.overlay = overlay;
    }

    public AmountStyle() {
        this.borderColor = ColorPalette.AMOUNT_BORDER_WAILA;
        this.backgroundColor = ColorPalette.AMOUNT_BACKGROUND_WAILA;
        this.filledColor = ColorPalette.AMOUNT_FILLED_WAILA;
        this.alternateFilledColor = ColorPalette.AMOUNT_FILLED_ALTERNATE_WAILA;
        this.overlay = null;
    }

    public AmountStyle borderColor(int borderColor) {
        this.borderColor = borderColor;
        return this;
    }

    public AmountStyle backgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
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
    public int getBorderColor() {
        return borderColor;
    }

    @Override
    public int getBackgroundColor1() {
        return backgroundColor;
    }

    @Override
    public int getBackgroundColor2() {
        return backgroundColor;
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
